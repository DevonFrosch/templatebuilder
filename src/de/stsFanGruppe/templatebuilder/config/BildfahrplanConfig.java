package de.stsFanGruppe.templatebuilder.config;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.xml.stream.XMLStreamException;
import de.stsFanGruppe.templatebuilder.external.ImportException;
import de.stsFanGruppe.templatebuilder.external.xmlhelper.XMLElement;
import de.stsFanGruppe.templatebuilder.external.xmlhelper.XMLReader;
import de.stsFanGruppe.tools.NullTester;

public class BildfahrplanConfig
{
	private Map<Object, ChangeCallback> callbacks = new HashMap<>();
	private int callbackCounter = 0;
	
	protected int marginRight = 20;
	protected int marginLeft = 20;
	protected int marginTop = 20;
	protected int marginBottom = 20;
	
	protected int hoeheProStunde = 400;
	protected double minZeit = 360;
	protected double maxZeit = 470;
	
	public BildfahrplanConfig(double minZeit, double maxZeit)
	{
		this.setMinZeit(minZeit);
		this.setMaxZeit(maxZeit);
	}
	public BildfahrplanConfig(String xml) throws ImportException
	{
		this.readFromXML(xml);
	}
	public BildfahrplanConfig()
	{
		
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
		notifyChange();
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
		notifyChange();
	}
	public int getHoeheProStunde()
	{
		return hoeheProStunde;
	}
	public void setHoeheProStunde(int hoeheProStunde)
	{
		if(hoeheProStunde < 0)
		{
			throw new IllegalArgumentException("Höhe muss größer gleich 0 sein.");
		}
		this.hoeheProStunde = hoeheProStunde;
		notifyChange();
	}
	public int getMarginTop()
	{
		return this.marginTop;
	}
	public int getMarginLeft()
	{
		return this.marginLeft;
	}
	public int getMarginRight()
	{
		return this.marginRight;
	}
	public int getMarginBottom()
	{
		return this.marginBottom;
	}
	
	public Object registerChangeHandler(ChangeCallback callback)
	{
		NullTester.test(callback);
		Object handlerID = Integer.valueOf(callbackCounter++);
		callbacks.put(handlerID, callback);
		return handlerID;
	}
	public boolean unregisterChangeHandler(Object handlerID)
	{
		NullTester.test(handlerID);
		return callbacks.remove(handlerID) != null;
	}
	protected void notifyChange()
	{
		callbacks.forEach((k, v) -> v.call());
	}
	
	public int getPanelHeight()
	{
		return (int) ((maxZeit - minZeit) / 60 * hoeheProStunde) + marginTop + marginBottom;
	}
	public int zeichnenBreite(JPanel p)
	{
		assert p != null;
		return p.getWidth() - marginLeft - marginRight;
	}
	public int zeichnenHoehe(JPanel p)
	{
		assert p != null;
		return p.getHeight() - marginTop - marginBottom;
	}
	
	public String toString()
	{
		return "{margin: r="+marginRight+", l="+marginLeft+", t="+marginTop+", b="+marginBottom+"}";
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
		notifyChange();
	}
	
	private static void log(String text)
	{
		System.out.println("BildfahrplanConfig: "+text);
	}
	
	public interface ChangeCallback
	{
		public void call();
	}
}
