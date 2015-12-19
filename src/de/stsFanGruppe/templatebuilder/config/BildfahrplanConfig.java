package de.stsFanGruppe.templatebuilder.config;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import de.stsFanGruppe.tools.JSONBuilder;
import de.stsFanGruppe.tools.NullTester;

public class BildfahrplanConfig
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanConfig.class);
	
	private Map<Object, Runnable> callbacks = new HashMap<>();
	private int callbackCounter = 0;
	
	protected int marginRight = 20;
	protected int marginLeft = 20;
	protected int marginTop = 0;
	protected int marginBottom = 20;
	
	protected int hoeheProStunde = 400;
	protected double minZeit = 360;
	protected double maxZeit = 1260;
	protected boolean autoSize = true;
	protected int schachtelung = 24;
	
	protected int zeigeZugnamen = 2;
	protected boolean zeigeZugnamenKommentare = true;
	
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
		log.trace("setZeiten({}, {})", min, max);
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
		log.trace("enableAutoSize()");
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
		log.trace("setHoeheProStunde({})", hoeheProStunde);
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
	public int getZeigeZugnamen()
	{
		return zeigeZugnamen;
	}
	public void setZeigeZugnamen(int zeigeZugnamen)
	{
		log.trace("setZeigeZugnamen({}", zeigeZugnamen);
		this.zeigeZugnamen = zeigeZugnamen;
	}
	public void setZeigeZugnamen(String zeigeZugnamen)
	{
		log.trace("setZeigeZugnamen({}", zeigeZugnamen);
		switch(zeigeZugnamen)
		{
			case "nie":
				this.zeigeZugnamen = 0;
				break;
			case "immer":
				this.zeigeZugnamen = 1;
				break;
			case "auto":
			default:
				this.zeigeZugnamen = 2;
		}
	}
	public boolean getZeigeZugnamenKommentare()
	{
		return zeigeZugnamenKommentare;
	}
	public void setZeigeZugnamenKommentare(boolean zeigeZugnamenKommentare)
	{
		log.trace("setZeigeZugnamenKommentare({}", zeigeZugnamenKommentare);
		this.zeigeZugnamenKommentare = zeigeZugnamenKommentare;
	}
	public int getSchachtelung()
	{
		return schachtelung;
	}
	public void setSchachtelung(int schachtelung)
	{
		log.trace("setSchachtelung({}", schachtelung);
		this.schachtelung = schachtelung;
	}
	
	public int getPanelHeight()
	{
		return (int) ((maxZeit - minZeit) / 60 * hoeheProStunde) + marginTop + marginBottom;
	}
	public int zeichnenBreite(JComponent p)
	{
		assert p != null;
		return p.getWidth() - marginLeft - marginRight;
	}
	public int zeichnenHoehe(JComponent p)
	{
		assert p != null;
		return p.getHeight() - marginTop - marginBottom;
	}
	
	public Object registerChangeHandler(Runnable callback)
	{
		NullTester.test(callback);
		Object handlerID = Integer.valueOf(callbackCounter++);
		log.debug("registerChangeHandler (ID {})", handlerID);
		callbacks.put(handlerID, callback);
		return handlerID;
	}
	public boolean unregisterChangeHandler(Object handlerID)
	{
		NullTester.test(handlerID);
		log.debug("unregisterChangeHandler (ID {})", handlerID);
		return callbacks.remove(handlerID) != null;
	}
	protected void notifyChange()
	{
		log.debug("Config geändert");
		callbacks.forEach((k, v) -> v.run());
	}
	
	public void parseJSON(String json)
	{
		log.debug("parseJSON(json)");
		// TODO JSONParser einbauen
		log.error("Funktion noch nicht implementiert!");
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
}
