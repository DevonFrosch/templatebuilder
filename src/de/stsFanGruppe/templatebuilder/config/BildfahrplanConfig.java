package de.stsFanGruppe.templatebuilder.config;

import java.awt.Color;
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
	
	protected int marginRight = 20; // Pixel
	protected int marginLeft = 20; // Pixel
	protected int marginTop = 0; // Pixel
	protected int marginBottom = 10; // Pixel
	
	//Einstellung für den Bildfahrplanspaltenheader
	int lineHeight = 10; // Pixel
	int offsetX = 10; // Pixel
	int offsetY = 5; // Pixel
	int textMarginTop = 5; // Pixel
	int textMarginBottom = 5; // Pixel
	int zeilenAnzahl = 2;
	
	//Einstellung für den Bildfahrplanzeilenheader
	int zeilenHeaderBreite = 32;
	int zeitIntervall = 10;
	
	// Darstellung der Linien
	protected int hoeheProStunde = 400; // Minuten
	protected double minZeit = 360; // Minuten
	protected double maxZeit = 1260; // Minuten
	protected boolean autoSize = true;
	protected int schachtelung = 1440; // Minuten
	
	// Darstellung von Texten
	protected boolean zeichneZeiten = true; 
	protected int zeigeZugnamen = 2;
	protected boolean zeigeZugnamenKommentare = true;
	
	//Farbeinstellungen
	protected Color zeitFarbe = Color.RED;
	protected Color fahrtenFarbe = Color.BLACK;
	protected Color betriebsstelleFarbe = Color.BLUE;
	
	// Konstruktoren
	public BildfahrplanConfig(double minZeit, double maxZeit)
	{
		this.setZeiten(minZeit, maxZeit);
	}
	public BildfahrplanConfig()
	{
		this.enableAutoSize();
	}
	
	// Getter / Setter
	public int getMarginRight()
	{
		return this.marginRight;
	}
	public int getMarginLeft()
	{
		return this.marginLeft;
	}
	public int getMarginTop()
	{
		return this.marginTop;
	}
	public int getMarginBottom()
	{
		return this.marginBottom;
	}
	
	public int getLineHeight()
	{
		return lineHeight;
	}
	public void setLineHeight(int lineHeight)
	{
		this.lineHeight = lineHeight;
		log.debug("Config: lineHeight = {}", lineHeight);
		notifyChange();
	}
	public int getOffsetX()
	{
		return offsetX;
	}
	public void setOffsetX(int offsetX)
	{
		this.offsetX = offsetX;
		log.debug("Config: offsetX = {}", offsetX);
		notifyChange();
	}
	public int getOffsetY()
	{
		return offsetY;
	}
	public void setOffsetY(int offsetY)
	{
		this.offsetY = offsetY;
		log.debug("Config: offsetY = {}", offsetY);
		notifyChange();
	}
	public int getTextMarginTop()
	{
		return textMarginTop;
	}
	public void setTextMarginTop(int textMarginTop)
	{
		this.textMarginTop = textMarginTop;
		log.debug("Config: textMarginTop = {}", textMarginTop);
		notifyChange();
	}
	public int getTextMarginBottom()
	{
		return textMarginBottom;
	}
	public void setTextMarginBottom(int textMarginBottom)
	{
		this.textMarginBottom = textMarginBottom;
		log.debug("Config: textMarginBottom = {}", textMarginBottom);
		notifyChange();
	}
	public int getZeilenAnzahl()
	{
		return zeilenAnzahl;
	}
	public void setZeilenAnzahl(int zeilenAnzahl)
	{
		this.zeilenAnzahl = zeilenAnzahl;
		log.debug("Config: zeilenAnzahl = {}", zeilenAnzahl);
		notifyChange();
	}
	
	public int getZeilenHeaderBreite()
	{
		return zeilenHeaderBreite;
	}
	public void setZeilenHeaderBreite(int zeilenHeaderBreite)
	{
		this.zeilenHeaderBreite = zeilenHeaderBreite;
		log.debug("Config: zeilenHeaderBreite = {}", zeilenHeaderBreite);
		notifyChange();
	}
	public int getZeitIntervall()
	{
		return zeitIntervall;
	}
	public void setZeitIntervall(int zeitIntervall)
	{
		this.zeitIntervall = zeitIntervall;
		log.debug("Config: zeitIntervall = {}", zeitIntervall);
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
		log.debug("Config: hoeheProStunde = {}", hoeheProStunde);
		notifyChange();
	}
	
	public double getMinZeit()
	{
		return minZeit;
	}
	public double getMaxZeit()
	{
		return maxZeit;
	}
	public boolean needsAutoSize()
	{
		return autoSize;
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
		log.debug("Config: minZeit = {}, maxZeit = {}, autoSize = false", min, max);
		notifyChange();
	}
	public void enableAutoSize()
	{
		this.autoSize = true;
		log.debug("Config: autoSize = true");
		notifyChange();
	}
	
	public int getSchachtelung()
	{
		return schachtelung;
	}
	public void setSchachtelung(int schachtelung)
	{
		this.schachtelung = schachtelung;
		log.debug("Config: schachtelung = {}", schachtelung);
		notifyChange();
	}
	
	public boolean getZeichneZeiten()
	{
		return zeichneZeiten;
	}
	public void setZeichneZeiten(boolean zeichneZeiten)
	{
		this.zeichneZeiten = zeichneZeiten;
		log.debug("Config: zeichneZeiten = {}", zeichneZeiten);
		notifyChange();
	}
	public int getZeigeZugnamen()
	{
		return zeigeZugnamen;
	}
	public void setZeigeZugnamen(int zeigeZugnamen)
	{
		this.zeigeZugnamen = zeigeZugnamen;
		log.debug("Config: zeigeZugnamen = {}", zeigeZugnamen);
		notifyChange();
	}
	public void setZeigeZugnamen(String zeigeZugnamen)
	{
		int zz = 0;
		switch(zeigeZugnamen)
		{
			case "nie":
				zz = 0;
				break;
			case "immer":
				zz = 1;
				break;
			case "auto":
			default:
				zz = 2;
		}
		this.zeigeZugnamen = zz;
		log.debug("Config: zeigeZugnamen = {}", zz);
		notifyChange();
	}
	public boolean getZeigeZugnamenKommentare()
	{
		return zeigeZugnamenKommentare;
	}
	public void setZeigeZugnamenKommentare(boolean zeigeZugnamenKommentare)
	{
		this.zeigeZugnamenKommentare = zeigeZugnamenKommentare;
		log.debug("Config: zeigeZugnamenKommentare = {}", zeigeZugnamenKommentare);
		notifyChange();
	}
	
	public Color getZeitFarbe()
	{
		return zeitFarbe;
	}
	public void setZeitFarbe(Color zeitFarbe)
	{
		this.zeitFarbe = zeitFarbe;
		log.debug("Config: zeitFarbe = {}", zeitFarbe);
		notifyChange();
	}
	public Color getFahrtenFarbe()
	{
		return fahrtenFarbe;	}
	public void setFahrtenFarbe(Color fahrtenFarbe)
	{
		this.fahrtenFarbe = fahrtenFarbe;
		log.debug("Config: fahrtenFarbe = {}", fahrtenFarbe);
		notifyChange();
	}
	public Color getBetriebsstelleFarbe()
	{
		return betriebsstelleFarbe;
	}
	public void setBetriebsstelleFarbe(Color betriebsstelleFarbe)
	{
		this.betriebsstelleFarbe = betriebsstelleFarbe;
		log.debug("Config: betriebsstelleFarbe = {}", betriebsstelleFarbe);
		notifyChange();
	}
	
	// Rechnende Funktionen
	public int getPanelHeight()
	{
		return (int) ((maxZeit - minZeit) / 60 * hoeheProStunde) + marginTop + marginBottom;
	}
	public int zeichnenBreite(JComponent p)
	{
		NullTester.test(p);
		return p.getWidth() - marginLeft - marginRight;
	}
	public int zeichnenHoehe(JComponent p)
	{
		NullTester.test(p);
		return p.getHeight() - marginTop - marginBottom;
	}
	public int spaltenHeaderHoehe(int textHeight)
	{
		return textMarginTop + (textHeight + offsetY) * zeilenAnzahl + textMarginBottom;  
	}
	
	// Change-Handler
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
		callbacks.forEach((k, v) -> v.run());
	}
	
	// Import/Export
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
