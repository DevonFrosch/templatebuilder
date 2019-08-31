package de.stsFanGruppe.templatebuilder.config;

import java.awt.Color;
import javax.swing.JComponent;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.FahrtDarstellungConfig;
import de.stsFanGruppe.templatebuilder.config.types.Schachtelung;
import de.stsFanGruppe.templatebuilder.zug.Template;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.PreferenceHandler;
import de.stsFanGruppe.tools.XMLExportable;

public class BildfahrplanConfig extends ConfigController implements XMLExportable
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanConfig.class);
	
	// Bildfahrplan-Ränder
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
	public static final String CONFIG_F_SCHACHTELUNG_TYP = "bildfahrplan/darstellung/fahrten/schachtelung/typ";
	public static final String CONFIG_F_SCHACHTELUNG_MINUTEN = "bildfahrplan/darstellung/fahrten/schachtelung/minuten";
	public static final String CONFIG_F_SCHACHTELUNG_TEMPLATE = "bildfahrplan/darstellung/fahrten/schachtelung/template";
	
	private static final int DEFAULT_F_HOEHEPROSTUNDE = 400; // Minuten
	private static final double DEFAULT_F_MINZEIT = 270; // Minuten, 4:30
	private static final double DEFAULT_F_MAXZEIT = 1290; // Minuten, 21:30
	private static final boolean DEFAULT_F_AUTOSIZE = true;
	private static final String DEFAULT_F_SCHACHTELUNG_TYP = Schachtelung.KEINE.toString();
	private static final int DEFAULT_F_SCHACHTELUNG_MINUTEN = 1440; // Minuten
	private static final String DEFAULT_F_SCHACHTELUNG_TEMPLATE = "";
	
	// Darstellung von Texten
	public static final String CONFIG_TEXT_ZEIGEZEITEN = "bildfahrplan/darstellung/text/zeigeZeiten";
	public static final String CONFIG_TEXT_ZEIGEZUGNAMEN = "bildfahrplan/darstellung/text/zeigeZugnamen";
	public static final String CONFIG_TEXT_ZEIGEKOMMENTARE = "bildfahrplan/darstellung/text/zeigeZugnamenKommentare";
	public static final String CONFIG_TEXT_ZEIGERICHTUNG = "bildfahrplan/darstellung/text/zeigeRichtung";
	
	private static final boolean DEFAULT_TEXT_ZEIGEZEITEN = true;
	private static final int DEFAULT_TEXT_ZEIGEZUGNAMEN = 2;
	private static final boolean DEFAULT_TEXT_ZEIGEKOMMENTARE = true;
	private static final int DEFAULT_TEXT_ZEIGERICHTUNG = 3; // 0 = Keine, 1 = Hin, 2 = Rück, 3 = Beide
	
	// Ignorierte Züge/Templates
	public static final String CONFIG_IGNORIERTE_ZUEGE = "bildfahrplan/darstellung/ignoriere/zuege";
	public static final String CONFIG_IGNORIERTE_TEMPLATES = "bildfahrplan/darstellung/ignoriere/templates";

	public static final String DEFAULT_IGNORIERTE_ZUEGE = "";
	public static final String DEFAULT_IGNORIERTE_TEMPLATES = "";
	
	// Farbeinstellungen
	public static final String CONFIG_FARBEN_ZEITEN = "bildfahrplan/darstellung/farben/zeiten";
	public static final String CONFIG_FARBEN_BETRIEBSS = "bildfahrplan/darstellung/farben/betriebsstellen";
	public static final String CONFIG_FARBEN_HINTERGR = "bildfahrplan/darstellung/farben/hintergrund";
	
	private static final Color DEFAULT_FARBEN_ZEITEN = Color.RED;
	private static final Color DEFAULT_FARBEN_BETRIEBSS = Color.BLUE;
	private static final Color DEFAULT_FARBEN_HINTERGR = Color.WHITE;
	
	protected FahrtDarstellungConfig fahrtDarstellungConfig;
	
	// Konstruktoren
	public BildfahrplanConfig(double minZeit, double maxZeit)
	{
		log.debug("Neue BildfahrplanConfig(double, double)");
		this.fahrtDarstellungConfig = new FahrtDarstellungConfig();
		this.prefs = new PreferenceHandler(BildfahrplanConfig.class);
		this.setZeiten(minZeit, maxZeit);
		assert prefs != null;
	}
	
	public BildfahrplanConfig()
	{
		log.trace("Neue BildfahrplanConfig()");
		this.fahrtDarstellungConfig = new FahrtDarstellungConfig();
		this.prefs = new PreferenceHandler(BildfahrplanConfig.class);
		this.enableAutoSize();
		assert prefs != null;
	}
	
	// Getter / Setter
	public FahrtDarstellungConfig getFahrtDarstellungConfig()
	{
		return fahrtDarstellungConfig;
	}
	
	public int getMarginRight()
	{
		return prefs.getInt(CONFIG_MAR_RIGHT, DEFAULT_MAR_RIGHT);
	}
	
	public void setMarginRight(int marginRight)
	{
		prefs.setInt("marginRight", CONFIG_MAR_RIGHT, marginRight);
		notifyChange();
	}
	
	public int getMarginLeft()
	{
		return prefs.getInt(CONFIG_MAR_LEFT, DEFAULT_MAR_LEFT);
	}
	
	public void setMarginLeft(int marginLeft)
	{
		prefs.setInt("marginLeft", CONFIG_MAR_LEFT, marginLeft);
		notifyChange();
	}
	
	public int getMarginTop()
	{
		return prefs.getInt(CONFIG_MAR_TOP, DEFAULT_MAR_TOP);
	}
	
	public void setMarginTop(int marginTop)
	{
		prefs.setInt("marginTop", CONFIG_MAR_TOP, marginTop);
		notifyChange();
	}
	
	public int getMarginBottom()
	{
		return prefs.getInt(CONFIG_MAR_BOTTOM, DEFAULT_MAR_BOTTOM);
	}
	
	public void setMarginBottom(int marginBottom)
	{
		prefs.setInt("marginBottom", CONFIG_MAR_BOTTOM, marginBottom);
		notifyChange();
	}
	
	public int getLineHeight()
	{
		return prefs.getInt(CONFIG_SH_LINEHEIGHT, DEFAULT_SH_LINEHEIGHT);
	}
	
	public void setLineHeight(int lineHeight)
	{
		prefs.setInt("lineHeight", CONFIG_SH_LINEHEIGHT, lineHeight);
		notifyChange();
	}
	
	public int getOffsetX()
	{
		return prefs.getInt(CONFIG_SH_OFFSETX, DEFAULT_SH_OFFSETX);
	}
	
	public void setOffsetX(int offsetX)
	{
		prefs.setInt("offsetX", CONFIG_SH_OFFSETX, offsetX);
		notifyChange();
	}
	
	public int getOffsetY()
	{
		return prefs.getInt(CONFIG_SH_OFFSETY, DEFAULT_SH_OFFSETY);
	}
	
	public void setOffsetY(int offsetY)
	{
		prefs.setInt("offsetY", CONFIG_SH_OFFSETY, offsetY);
		notifyChange();
	}
	
	public int getTextMarginTop()
	{
		return prefs.getInt(CONFIG_SH_TEXTMARGINTOP, DEFAULT_SH_TEXTMARGINTOP);
	}
	
	public void setTextMarginTop(int textMarginTop)
	{
		prefs.setInt("textMarginTop", CONFIG_SH_TEXTMARGINTOP, textMarginTop);
		notifyChange();
	}
	
	public int getTextMarginBottom()
	{
		return prefs.getInt(CONFIG_SH_TEXTMARGINBOTTOM, DEFAULT_SH_TEXTMARGINBOTTOM);
	}
	
	public void setTextMarginBottom(int textMarginBottom)
	{
		prefs.setInt("textMarginBottom", CONFIG_SH_TEXTMARGINBOTTOM, textMarginBottom);
		notifyChange();
	}
	
	public int getZeilenAnzahl()
	{
		return prefs.getInt(CONFIG_SH_ZEILENANZAHL, DEFAULT_SH_ZEILENANZAHL);
	}
	
	public void setZeilenAnzahl(int zeilenAnzahl)
	{
		prefs.setInt("zeilenAnzahl", CONFIG_SH_ZEILENANZAHL, zeilenAnzahl);
		notifyChange();
	}
	
	public int getZeilenHeaderBreite()
	{
		return prefs.getInt(CONFIG_ZH_BREITE, DEFAULT_ZH_BREITE);
	}
	
	public void setZeilenHeaderBreite(int zeilenHeaderBreite)
	{
		prefs.setInt("zeilenHeaderBreite", CONFIG_ZH_BREITE, zeilenHeaderBreite);
		notifyChange();
	}
	
	public int getZeitIntervall()
	{
		return prefs.getInt(CONFIG_ZH_ZEITINTERVALL, DEFAULT_ZH_ZEITINTERVALL);
	}
	
	public void setZeitIntervall(int zeitIntervall)
	{
		prefs.setInt("zeitIntervall", CONFIG_ZH_ZEITINTERVALL, zeitIntervall);
		notifyChange();
	}
	
	public int getHoeheProStunde()
	{
		return prefs.getInt(CONFIG_F_HOEHEPROSTUNDE, DEFAULT_F_HOEHEPROSTUNDE);
	}
	
	public void setHoeheProStunde(int hoeheProStunde)
	{
		if(hoeheProStunde < 0)
		{
			throw new IllegalArgumentException("Höhe muss größer gleich 0 sein.");
		}
		prefs.setInt("hoeheProStunde", CONFIG_F_HOEHEPROSTUNDE, hoeheProStunde);
		notifyChange();
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
			throw new IllegalArgumentException("Zeit muss größer gleich 0 sein.");
		}
		prefs.setDouble("minZeit", CONFIG_F_MINZEIT, min);
		prefs.setDouble("maxZeit", CONFIG_F_MAXZEIT, max);
		prefs.setBoolean("autoSize", CONFIG_F_AUTOSIZE, false);
		notifyChange();
	}
	
	public void enableAutoSize()
	{
		prefs.setBoolean("autoSize", CONFIG_F_AUTOSIZE, true);
		notifyChange();
	}
	
	public Schachtelung getSchachtelungTyp()
	{
		String typ = prefs.getString(CONFIG_F_SCHACHTELUNG_TYP, DEFAULT_F_SCHACHTELUNG_TYP);
		return Schachtelung.valueOf(typ);
	}
	public void setSchachtelungTyp(Schachtelung schachtelung)
	{
		prefs.setString("schachtelung/typ", CONFIG_F_SCHACHTELUNG_TYP, schachtelung.toString());
		notifyChange();
	}
	
	public int getSchachtelungMinuten()
	{
		return prefs.getInt(CONFIG_F_SCHACHTELUNG_MINUTEN, DEFAULT_F_SCHACHTELUNG_MINUTEN);
	}
	public void setSchachtelungMinuten(int minuten)
	{
		prefs.setInt("schachtelung/minuten", CONFIG_F_SCHACHTELUNG_MINUTEN, minuten);
		notifyChange();
	}
	
	public String getSchachtelungTemplate()
	{
		return prefs.getString(CONFIG_F_SCHACHTELUNG_TEMPLATE, DEFAULT_F_SCHACHTELUNG_TEMPLATE);
	}
	public void setSchachtelungTemplate(String template)
	{
		prefs.setString("schachtelung/template", CONFIG_F_SCHACHTELUNG_TEMPLATE, template);
		notifyChange();
	}
	
	public boolean getZeigeZeiten()
	{
		return prefs.getBoolean(CONFIG_TEXT_ZEIGEZEITEN, DEFAULT_TEXT_ZEIGEZEITEN);
	}
	
	public void setZeigeZeiten(boolean zeigeZeiten)
	{
		prefs.setBoolean("zeigeZeiten", CONFIG_TEXT_ZEIGEZEITEN, zeigeZeiten);
		notifyChange();
	}
	
	public int getZeigeZugnamen()
	{
		return prefs.getInt(CONFIG_TEXT_ZEIGEZUGNAMEN, DEFAULT_TEXT_ZEIGEZUGNAMEN);
	}
	
	public void setZeigeZugnamen(int zeigeZugnamen)
	{
		prefs.setInt("zeigeZugnamen", CONFIG_TEXT_ZEIGEZUGNAMEN, zeigeZugnamen);
		notifyChange();
	}
	
	public void setZeigeZugnamen(String zeigeZugnamen)
	{
		int zz = DEFAULT_TEXT_ZEIGEZUGNAMEN;
		switch(zeigeZugnamen)
		{
			case "nie":
				zz = 0;
				break;
			case "immer":
				zz = 1;
				break;
			case "auto":
				zz = 2;
				break;
			default:
		}
		prefs.setInt("zeigeZugnamen", CONFIG_TEXT_ZEIGEZUGNAMEN, zz);
		notifyChange();
	}
	
	public boolean getZeigeZugnamenKommentare()
	{
		return prefs.getBoolean(CONFIG_TEXT_ZEIGEKOMMENTARE, DEFAULT_TEXT_ZEIGEKOMMENTARE);
	}
	
	public void setZeigeZugnamenKommentare(boolean zeigeZugnamenKommentare)
	{
		prefs.setBoolean("zeigeZugnamenKommentare", CONFIG_TEXT_ZEIGEKOMMENTARE, zeigeZugnamenKommentare);
		notifyChange();
	}
	
	public int getZeigeRichtung()
	{
		return prefs.getInt(CONFIG_TEXT_ZEIGERICHTUNG, DEFAULT_TEXT_ZEIGERICHTUNG);
	}
	
	public void setZeigeRichtung(int zeigeRichtung)
	{
		prefs.setInt("zeigeRichtung", CONFIG_TEXT_ZEIGERICHTUNG, zeigeRichtung);
		notifyChange();
	}
	
	public void setZeigeRichtung(String zeigeRichtung)
	{
		int zr = DEFAULT_TEXT_ZEIGERICHTUNG;
		switch(zeigeRichtung)
		{
			case "keine":
				zr = 0;
				break;
			case "hin":
				zr = 1;
				break;
			case "zurueck":
				zr = 2;
				break;
			case "beide":
				zr = 3;
				break;
			default:
		}
		prefs.setInt("zeigeRichtung", CONFIG_TEXT_ZEIGERICHTUNG, zr);
		notifyChange();
	}
	
	public String getIgnorierteZuege()
	{
		return prefs.getString(CONFIG_IGNORIERTE_ZUEGE, DEFAULT_IGNORIERTE_ZUEGE);
	}
	public String[] getIgnorierteZuegeArray()
	{
		return getIgnorierteZuege().split("\n");
	}
	public boolean testIgnorierteZuege(String suche)
	{
		if(suche == null) {
			return false;
		}
		for(String zugName: getIgnorierteZuegeArray()) {
			if(zugName.length() > 0 && suche.contains(zugName)) {
				return true;
			}
		}
		return false;
	}
	public void setIgnorierteZuege(String zeigeZuege)
	{
		prefs.setString("ignorierteZüge", CONFIG_IGNORIERTE_ZUEGE, zeigeZuege);
		notifyChange();
	}
	
	public String getIgnorierteTemplates()
	{
		return prefs.getString(CONFIG_IGNORIERTE_TEMPLATES, DEFAULT_IGNORIERTE_TEMPLATES);
	}
	public String[] getIgnorierteTemplatesArray()
	{
		return getIgnorierteTemplates().split("\n");
	}
	public boolean testIgnorierteTemplates(Template template)
	{
		if(template == null) {
			return false;
		}
		
		String name = template.getName();
		String tid = template.getTidOrNull();
		
		for(String suchString: getIgnorierteTemplatesArray()) {
			if(suchString.length() > 0) {
				if(name != null && name.contains(suchString)) {
					return true;
				}
				if(tid != null && tid.contains(suchString)) {
					return true;
				}
			}
		}
		return false;
	}
	public void setIgnorierteTemplates(String zeigeTemplates)
	{
		prefs.setString("ignorierteTemplates", CONFIG_IGNORIERTE_TEMPLATES, zeigeTemplates);
		notifyChange();
	}
	
	public Color getZeitenFarbe()
	{
		return prefs.getColor(CONFIG_FARBEN_ZEITEN, DEFAULT_FARBEN_ZEITEN);
	}
	
	public void setZeitenFarbe(Color zeitenFarbe)
	{
		prefs.setColor("zeitenFarbe", CONFIG_FARBEN_ZEITEN, zeitenFarbe);
		notifyChange();
	}
	
	public Color getBetriebsstellenFarbe()
	{
		return prefs.getColor(CONFIG_FARBEN_BETRIEBSS, DEFAULT_FARBEN_BETRIEBSS);
	}
	
	public void setBetriebsstellenFarbe(Color betriebsstellenFarbe)
	{
		prefs.setColor("betriebsstellenFarbe", CONFIG_FARBEN_BETRIEBSS, betriebsstellenFarbe);
		notifyChange();
	}
	
	public Color getHintergrundFarbe()
	{
		return prefs.getColor(CONFIG_FARBEN_HINTERGR, DEFAULT_FARBEN_HINTERGR);
	}
	
	public void setHintergrundFarbe(Color hintergrundFarbe)
	{
		prefs.setColor("hintergrundFarbe", CONFIG_FARBEN_HINTERGR, hintergrundFarbe);
		notifyChange();
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
	
	public String toString()
	{
		return "BildfahrplanConfig (" + prefs.toString() + ")";
	}
	
	public String toXML(String indent)
	{
		return indent + "<bildfahrplanConfig />";
	}
}
