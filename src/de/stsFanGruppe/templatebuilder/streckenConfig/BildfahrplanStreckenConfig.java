package de.stsFanGruppe.templatebuilder.streckenConfig;

import java.awt.Color;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.PreferenceHandler;

public class BildfahrplanStreckenConfig extends ConfigStreckenController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanStreckenConfig.class);
	
	private PreferenceHandler prefs;
	private Map<Object, Runnable> callbacks = new HashMap<>();
	private int callbackCounter = 0;
	
	// Bildfahrplan-R�nder
	public static final String CONFIG_MAR_RIGHT = "bildfahrplan/darstellung/margin/right";
	public static final String CONFIG_MAR_LEFT = "bildfahrplan/darstellung/margin/left";
	public static final String CONFIG_MAR_TOP = "bildfahrplan/darstellung/margin/top";
	public static final String CONFIG_MAR_BOTTOM = "bildfahrplan/darstellung/margin/bottom";

	private static final int DEFAULT_MAR_RIGHT = 20; // Pixel
	private static final int DEFAULT_MAR_LEFT = 20; // Pixel
	private static final int DEFAULT_MAR_TOP = 0; // Pixel
	private static final int DEFAULT_MAR_BOTTOM = 10; // Pixel
	
	// Bildfahrplanspaltenheader
	public static final String CONFIG_SH_LINEHEIGHT = "bildfahrplan/darstellung/spaltenheader/lineHeight";
	public static final String CONFIG_SH_OFFSETX = "bildfahrplan/darstellung/spaltenheader/offsetX";
	public static final String CONFIG_SH_OFFSETY = "bildfahrplan/darstellung/spaltenheader/offsetY";
	public static final String CONFIG_SH_TEXTMARGINTOP = "bildfahrplan/darstellung/spaltenheader/textMarginTop";
	public static final String CONFIG_SH_TEXTMARGINBOTTOM = "bildfahrplan/darstellung/spaltenheader/textMarginBottom";
	public static final String CONFIG_SH_ZEILENANZAHL = "bildfahrplan/darstellung/spaltenheader/zeilenAnzahl";

	private static final int DEFAULT_SH_LINEHEIGHT = 10; // Pixel
	private static final int DEFAULT_SH_OFFSETX = 10; // Pixel
	private static final int DEFAULT_SH_OFFSETY = 5; // Pixel
	private static final int DEFAULT_SH_TEXTMARGINTOP = 5; // Pixel
	private static final int DEFAULT_SH_TEXTMARGINBOTTOM = 5; // Pixel
	private static final int DEFAULT_SH_ZEILENANZAHL = 2;
	
	// Bildfahrplanzeilenheader
	public static final String CONFIG_ZH_BREITE = "bildfahrplan/darstellung/zeilenheader/breite";
	public static final String CONFIG_ZH_ZEITINTERVALL = "bildfahrplan/darstellung/zeilenheader/zeitIntervall";
	
	private static final int DEFAULT_ZH_BREITE = 32; // Pixel
	private static final int DEFAULT_ZH_ZEITINTERVALL = 10; // Minuten
	
	// Darstellung der Linien
	public static final String CONFIG_F_HOEHEPROSTUNDE = "bildfahrplan/darstellung/fahrten/hoeheProStunde";
	public static final String CONFIG_F_MINZEIT = "bildfahrplan/darstellung/fahrten/minZeit";
	public static final String CONFIG_F_MAXZEIT = "bildfahrplan/darstellung/fahrten/maxZeit";
	public static final String CONFIG_F_AUTOSIZE = "bildfahrplan/darstellung/fahrten/autoSize";
	public static final String CONFIG_F_SCHACHTELUNG = "bildfahrplan/darstellung/fahrten/schachtelung";
	
	private static final int DEFAULT_F_HOEHEPROSTUNDE = 400; // Minuten
	private static final double DEFAULT_F_MINZEIT = 360; // Minuten
	private static final double DEFAULT_F_MAXZEIT = 1260; // Minuten
	private static final boolean DEFAULT_F_AUTOSIZE = true;
	private static final int DEFAULT_F_SCHACHTELUNG = 1440; // Minuten
	
	// Darstellung von Texten
	public static final String CONFIG_TEXT_ZEIGEZEITEN = "bildfahrplan/darstellung/text/zeigeZeiten";
	public static final String CONFIG_TEXT_ZEIGEZUGNAMEN = "bildfahrplan/darstellung/text/zeigeZugnamen";
	public static final String CONFIG_TEXT_ZEIGEKOMMENTARE = "bildfahrplan/darstellung/text/zeigeZugnamenKommentare";
	
	private static final boolean DEFAULT_TEXT_ZEIGEZEITEN = true; 
	private static final int DEFAULT_TEXT_ZEIGEZUGNAMEN = 2;
	private static final boolean DEFAULT_TEXT_ZEIGEKOMMENTARE = true;
	
	//Farbeinstellungen
	public static final String CONFIG_FARBEN_ZEITEN = "bildfahrplan/darstellung/farben/zeiten";
	public static final String CONFIG_FARBEN_FAHRTEN = "bildfahrplan/darstellung/farben/fahrten";
	public static final String CONFIG_FARBEN_BETRIEBSS = "bildfahrplan/darstellung/farben/betriebsstellen";
	public static final String CONFIG_FARBEN_HINTERGR = "bildfahrplan/darstellung/farben/hintergrund";

	private static final Color DEFAULT_FARBEN_ZEITEN = Color.RED;
	private static final Color DEFAULT_FARBEN_FAHRTEN = Color.BLACK;
	private static final Color DEFAULT_FARBEN_BETRIEBSS = Color.BLUE;
	private static final Color DEFAULT_FARBEN_HINTERGR = Color.WHITE;
	
	// Konstruktoren
	public BildfahrplanStreckenConfig(double minZeit, double maxZeit)
	{
		log.debug("Neue BildfahrplanConfig(double, double)");
		this.prefs = new PreferenceHandler(BildfahrplanStreckenConfig.class, () -> notifyChange());
		this.setZeiten(minZeit, maxZeit);
		assert prefs != null;
	}
	public BildfahrplanStreckenConfig()
	{
		log.debug("Neue BildfahrplanConfig()");
		this.prefs = new PreferenceHandler(BildfahrplanStreckenConfig.class, () -> notifyChange());
		this.enableAutoSize();
		assert prefs != null;
	}
	
	// Getter / Setter
	public int getMarginRight()
	{
		return prefs.getInt(CONFIG_MAR_RIGHT, DEFAULT_MAR_RIGHT);
	}
	public void setMarginRight(int marginRight)
	{
		prefs.setInt("marginRight", CONFIG_MAR_RIGHT, marginRight);
	}
	public int getMarginLeft()
	{
		return prefs.getInt(CONFIG_MAR_LEFT, DEFAULT_MAR_LEFT);
	}
	public void setMarginLeft(int marginLeft)
	{
		prefs.setInt("marginLeft", CONFIG_MAR_LEFT, marginLeft);
	}
	public int getMarginTop()
	{
		return prefs.getInt(CONFIG_MAR_TOP, DEFAULT_MAR_TOP);
	}
	public void setMarginTop(int marginTop)
	{
		prefs.setInt("marginTop", CONFIG_MAR_TOP, marginTop);
	}
	public int getMarginBottom()
	{
		return prefs.getInt(CONFIG_MAR_BOTTOM, DEFAULT_MAR_BOTTOM);
	}
	public void setMarginBottom(int marginBottom)
	{
		prefs.setInt("marginBottom", CONFIG_MAR_BOTTOM, marginBottom);
	}
	
	public int getLineHeight()
	{
		return prefs.getInt(CONFIG_SH_LINEHEIGHT, DEFAULT_SH_LINEHEIGHT);
	}
	public void setLineHeight(int lineHeight)
	{
		prefs.setInt("lineHeight", CONFIG_SH_LINEHEIGHT, lineHeight);
	}
	public int getOffsetX()
	{
		return prefs.getInt(CONFIG_SH_OFFSETX, DEFAULT_SH_OFFSETX);
	}
	public void setOffsetX(int offsetX)
	{
		prefs.setInt("offsetX", CONFIG_SH_OFFSETX, offsetX);
	}
	public int getOffsetY()
	{
		return prefs.getInt(CONFIG_SH_OFFSETY, DEFAULT_SH_OFFSETY);
	}
	public void setOffsetY(int offsetY)
	{
		prefs.setInt("offsetY", CONFIG_SH_OFFSETY, offsetY);
	}
	public int getTextMarginTop()
	{
		return prefs.getInt(CONFIG_SH_TEXTMARGINTOP, DEFAULT_SH_TEXTMARGINTOP);
	}
	public void setTextMarginTop(int textMarginTop)
	{
		prefs.setInt("textMarginTop", CONFIG_SH_TEXTMARGINTOP, textMarginTop);
	}
	public int getTextMarginBottom()
	{
		return prefs.getInt(CONFIG_SH_TEXTMARGINBOTTOM, DEFAULT_SH_TEXTMARGINBOTTOM);
	}
	public void setTextMarginBottom(int textMarginBottom)
	{
		prefs.setInt("textMarginBottom", CONFIG_SH_TEXTMARGINBOTTOM, textMarginBottom);
	}
	public int getZeilenAnzahl()
	{
		return prefs.getInt(CONFIG_SH_ZEILENANZAHL, DEFAULT_SH_ZEILENANZAHL);
	}
	public void setZeilenAnzahl(int zeilenAnzahl)
	{
		prefs.setInt("zeilenAnzahl", CONFIG_SH_ZEILENANZAHL, zeilenAnzahl);
	}
	
	public int getZeilenHeaderBreite()
	{
		return prefs.getInt(CONFIG_ZH_BREITE, DEFAULT_ZH_BREITE);
	}
	public void setZeilenHeaderBreite(int zeilenHeaderBreite)
	{
		prefs.setInt("zeilenHeaderBreite", CONFIG_ZH_BREITE, zeilenHeaderBreite);
	}
	public int getZeitIntervall()
	{
		return prefs.getInt(CONFIG_ZH_ZEITINTERVALL, DEFAULT_ZH_ZEITINTERVALL);
	}
	public void setZeitIntervall(int zeitIntervall)
	{
		prefs.setInt("zeitIntervall", CONFIG_ZH_ZEITINTERVALL, zeitIntervall);
	}
	
	public int getHoeheProStunde()
	{
		return prefs.getInt(CONFIG_F_HOEHEPROSTUNDE, DEFAULT_F_HOEHEPROSTUNDE);
	}
	public void setHoeheProStunde(int hoeheProStunde)
	{
		if(hoeheProStunde < 0)
		{
			throw new IllegalArgumentException("H�he muss gr��er gleich 0 sein.");
		}
		prefs.setInt("hoeheProStunde", CONFIG_F_HOEHEPROSTUNDE, hoeheProStunde);
	}
	
	public double getMinZeit()
	{
		return prefs.getDouble(CONFIG_F_MINZEIT, DEFAULT_F_MINZEIT);
	}
	public double getMaxZeit()
	{
		return prefs.getDouble(CONFIG_F_MAXZEIT, DEFAULT_F_MAXZEIT);
	}
	public boolean needsAutoSize()
	{
		return prefs.getBoolean(CONFIG_F_AUTOSIZE, DEFAULT_F_AUTOSIZE);
	}
	public void setZeiten(double min, double max)
	{
		if(min < 0 || max < 0)
		{
			throw new IllegalArgumentException("Zeit muss gr��er gleich 0 sein.");
		}
		prefs.setDouble("minZeit", CONFIG_F_MINZEIT, min);
		prefs.setDouble("maxZeit", CONFIG_F_MAXZEIT, max);
		prefs.setBoolean("autoSize", CONFIG_F_AUTOSIZE, false);
	}
	public void enableAutoSize()
	{
		prefs.setBoolean("autoSize", CONFIG_F_AUTOSIZE, true);
	}
	
	public int getSchachtelung()
	{
		return prefs.getInt(CONFIG_F_SCHACHTELUNG, DEFAULT_F_SCHACHTELUNG);
	}
	public void setSchachtelung(int schachtelung)
	{
		prefs.setInt("schachtelung", CONFIG_F_SCHACHTELUNG, schachtelung);
	}
	
	public boolean getZeigeZeiten()
	{
		return prefs.getBoolean(CONFIG_TEXT_ZEIGEZEITEN, DEFAULT_TEXT_ZEIGEZEITEN);
	}
	public void setZeigeZeiten(boolean zeigeZeiten)
	{
		prefs.setBoolean("zeigeZeiten", CONFIG_TEXT_ZEIGEZEITEN, zeigeZeiten);
	}
	public int getZeigeZugnamen()
	{
		return prefs.getInt(CONFIG_TEXT_ZEIGEZUGNAMEN, DEFAULT_TEXT_ZEIGEZUGNAMEN);
	}
	public void setZeigeZugnamen(int zeigeZugnamen)
	{
		prefs.setInt("zeigeZugnamen", CONFIG_TEXT_ZEIGEZUGNAMEN, zeigeZugnamen);
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
		prefs.setInt("zeigeZugnamen", CONFIG_TEXT_ZEIGEZUGNAMEN, zz);
	}
	public boolean getZeigeZugnamenKommentare()
	{
		return prefs.getBoolean(CONFIG_TEXT_ZEIGEKOMMENTARE, DEFAULT_TEXT_ZEIGEKOMMENTARE);
	}
	public void setZeigeZugnamenKommentare(boolean zeigeZugnamenKommentare)
	{
		prefs.setBoolean("zeigeZugnamenKommentare", CONFIG_TEXT_ZEIGEKOMMENTARE, zeigeZugnamenKommentare);
	}
	
	public Color getZeitenFarbe()
	{
		return prefs.getColor(CONFIG_FARBEN_ZEITEN, DEFAULT_FARBEN_ZEITEN);
	}
	public void setZeitenFarbe(Color zeitenFarbe)
	{
		prefs.setColor("zeitenFarbe", CONFIG_FARBEN_ZEITEN, zeitenFarbe);
	}
	public Color getFahrtenFarbe()
	{
		return prefs.getColor(CONFIG_FARBEN_FAHRTEN, DEFAULT_FARBEN_FAHRTEN);
	}
	public void setFahrtenFarbe(Color fahrtenFarbe)
	{
		prefs.setColor("fahrtenFarbe", CONFIG_FARBEN_FAHRTEN, fahrtenFarbe);
	}
	public Color getBetriebsstellenFarbe()
	{
		return prefs.getColor(CONFIG_FARBEN_BETRIEBSS, DEFAULT_FARBEN_BETRIEBSS);
	}
	public void setBetriebsstellenFarbe(Color betriebsstellenFarbe)
	{
		prefs.setColor("betriebsstellenFarbe", CONFIG_FARBEN_BETRIEBSS, betriebsstellenFarbe);
	}
	public Color getHintergrundFarbe()
	{
		return prefs.getColor(CONFIG_FARBEN_HINTERGR, DEFAULT_FARBEN_HINTERGR);
	}
	public void setHintergrundFarbe(Color hintergrundFarbe)
	{
		prefs.setColor("hintergrundFarbe", CONFIG_FARBEN_HINTERGR, hintergrundFarbe);
	}
	
	// Rechnende Funktionen
	public int getPanelHeight()
	{
		return (int) ((getMaxZeit() - getMinZeit()) / 60 * getHoeheProStunde()) + getMarginTop() + getMarginBottom();
	}
	public int getZeichnenBreite(JComponent p)
	{
		NullTester.test(p);
		return p.getWidth() - getMarginLeft() - getMarginRight();
	}
	public int getZeichnenHoehe(JComponent p)
	{
		NullTester.test(p);
		return p.getHeight() - getMarginTop() - getMarginBottom();
	}
	public int getSpaltenHeaderHoehe(int textHeight)
	{
		return getTextMarginTop() + (textHeight + getOffsetY()) * getZeilenAnzahl() + getTextMarginBottom();  
	}
	
	public boolean schreibTest()
	{
		return prefs.speichertest();
	}
	public boolean schreibeEinstellungen()
	{
		return prefs.schreibeEinstellungen();
	}
	public boolean importXML(InputStream is)
	{
		return prefs.importXML(is);
	}
	public boolean exportXML(OutputStream os)
	{
		return prefs.exportXML(os);
	}
	
	public String toString()
	{
		return "BildfahrplanConfig ("+prefs.toString()+")";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		return indent+"<bildfahrplanConfig />";
	}
}