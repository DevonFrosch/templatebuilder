package de.stsFanGruppe.templatebuilder.gui.bildfahrplan;

import java.io.FileNotFoundException;
import javax.swing.JPanel;
import javax.xml.stream.XMLStreamException;
import de.stsFanGruppe.templatebuilder.external.ImportException;
import de.stsFanGruppe.templatebuilder.external.xmlhelper.XMLElement;
import de.stsFanGruppe.templatebuilder.external.xmlhelper.XMLReader;

public class BildfahrplanConfig
{
	int margin_right = 20;
	int margin_left = 20;
	int margin_top = 20;
	int margin_bottom = 20;
	
	int hoeheProStunde = 600;
	double minZeit = 360;
	double maxZeit = 470;
	
	public BildfahrplanConfig(double minZeit, double maxZeit)
	{
		this.setMinZeit(minZeit);
		this.setMaxZeit(maxZeit);
	}
	public BildfahrplanConfig(String xml) throws ImportException
	{
		this.readFromXML(xml);
	}
	
	public double getMinZeit()
	{
		return minZeit;
	}
	public void setMinZeit(double zeit)
	{
		if(zeit < 0)
		{
			throw new IllegalArgumentException("Zeit muss größer gleich 0 sein.");
		}
		this.minZeit = zeit;
	}
	public double getMaxZeit()
	{
		return maxZeit;
	}
	public void setMaxZeit(double zeit)
	{
		if(zeit < 0)
		{
			throw new IllegalArgumentException("Zeit muss größer gleich 0 sein.");
		}
		this.maxZeit = zeit;
	}
	
	public int getPanelHeight()
	{
		return (int) ((maxZeit - minZeit) / 60 * hoeheProStunde) + margin_top + margin_bottom;
	}
	int zeichnenBreite(JPanel p)
	{
		assert p != null;
		return p.getWidth() - margin_left - margin_right;
	}
	int zeichnenHoehe(JPanel p)
	{
		assert p != null;
		return p.getHeight() - margin_top - margin_bottom;
	}
	
	public String toString()
	{
		return "{margin: r="+margin_right+", l="+margin_left+", t="+margin_top+", b="+margin_bottom+"}";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		String newLine = "\n"+indent+"  ";
		return "<bildfahrplanConfig>"
				+newLine+"<hoeheProStunde value=\""+hoeheProStunde+"\" />"
				+newLine+"<zeitBereich min="+minZeit+" max=\""+maxZeit+"\" />"
				+"\n"+indent+"</bildfahrplanConfig>";
	}
	public void readFromXML(String xml) throws ImportException
	{
		try
		{
			XMLReader reader = new XMLReader(new java.io.FileInputStream(xml));
			XMLElement elem;
			while((elem = reader.findTagUntil("bildfahrplanConfig", "hoeheProStunde", "zeitBereich")) != null)
			{
				switch(elem.getName().toLowerCase())
				{
					case "hoeheprostunde":
						this.hoeheProStunde = Integer.parseInt(elem.getAttribute("value"));
						break;
					case "zeitbereich":
						this.minZeit = Double.parseDouble(elem.getAttribute("min"));
						this.maxZeit = Double.parseDouble(elem.getAttribute("max"));
					default:
						break;
				}
			}
		}
		catch(FileNotFoundException | XMLStreamException | NumberFormatException e)
		{
			throw new ImportException(e);
		}
	}
	
	private static void log(String text)
	{
		System.out.println("BildfahrplanConfig: "+text);
	}
}
