package de.stsFanGruppe.templatebuilder.config;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import de.stsFanGruppe.tools.JSONBuilder;
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
	protected double maxZeit = 1260;
	protected boolean autoSize = true;
	
	protected boolean zeigeZugnamen = true;
	protected int schachtelung = 24;
	
	public BildfahrplanConfig(double minZeit, double maxZeit)
	{
		this.setZeiten(minZeit, maxZeit);
	}
	public BildfahrplanConfig()
	{
		this.enableAutoSize();
	}
	
	public double getMinZeit()
	{
		return minZeit;
	}
	public double getMaxZeit()
	{
		return maxZeit;
	}
	public void setZeiten(double min, double max)
	{
		if(min < 0 || max < 0)
		{
			throw new IllegalArgumentException("Zeit muss größer gleich 0 sein.");
		}
		this.minZeit = min;
		this.maxZeit = max;
		this.autoSize = false;
		notifyChange();
	}
	public boolean needsAutoSize()
	{
		return autoSize;
	}
	public void enableAutoSize()
	{
		this.autoSize = true;
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
	public boolean getZeigeZugnamen()
	{
		return zeigeZugnamen;
	}
	public void setZeigeZugnamen(boolean zeigeZugnamen)
	{
		this.zeigeZugnamen = zeigeZugnamen;
	}
	public int getSchachtelung()
	{
		return schachtelung;
	}
	public void setSchachtelung(int schachtelung)
	{
		this.schachtelung = schachtelung;
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
	
	public void parseJSON(String json)
	{
		// TODO JSONParser einbauen
	}
	
	public String toJSON()
	{
		JSONBuilder json = new JSONBuilder();
		json.beginObject();
		json.add("marginRight", marginRight);
		json.add("marginLeft", marginLeft);
		json.add("marginTop", marginTop);
		json.add("marginBottom", marginBottom);
		json.add("hoeheProStunde", hoeheProStunde);
		json.add("minZeit", minZeit);
		json.add("maxZeit", maxZeit);
		json.add("autoSize", autoSize);
		json.add("schachtelung", schachtelung);
		json.endObject();
		return json.toString();
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
	
	private static void log(String text)
	{
		System.out.println("BildfahrplanConfig: "+text);
	}
	
	public interface ChangeCallback
	{
		public void call();
	}
}
