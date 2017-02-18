package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung;

import java.awt.Color;
import de.stsFanGruppe.templatebuilder.config.ConfigController;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype.LineType;
import de.stsFanGruppe.tools.PreferenceHandler;

public class FahrtDarstellungConfig extends ConfigController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FahrtDarstellungConfig.class);
	private static FahrtDarstellungSettingsGUI gui;
	
	// Farbeinstellungen
	public static final String CONFIG_STANDARD_LINIEN_FARBE = "bildfahrplan/darstellung/linie/farben";
	public static final String CONFIG_STANDARD_LINIEN_STAERKE = "bildfahrplan/darstellung/linie/staerke";
	public static final String CONFIG_STANDARD_LINIEN_TYP = "bildfahrplan/darstellung/linie/typ";
	
	public static final Color DEFAULT_STANDARD_LINIEN_FARBE = Color.BLACK;
	public static final int DEFAULT_STANDARD_LINIEN_STAERKE = 1;
	public static final LineType DEFAULT_STANDARD_LINIEN_TYP = LineType.SOLID_LINE;
	
	// Konstruktoren
	public FahrtDarstellungConfig()
	{
		log.debug("Neue FahrtDarstellungConfig()");
		this.prefs = new PreferenceHandler(FahrtDarstellungConfig.class, () -> notifyChange());
		assert prefs != null;
	}
	
	// Getter / Setter
	public Color getStandardLinienFarbe()
	{
		return prefs.getColor(CONFIG_STANDARD_LINIEN_FARBE, DEFAULT_STANDARD_LINIEN_FARBE);
	}
	public void setStandardLinienFarbe(Color standardLinienFarbe)
	{
		prefs.setColor("standardLinienFarbe", CONFIG_STANDARD_LINIEN_FARBE, standardLinienFarbe);
	}
	public int getStandardLinienStärke()
	{
		return prefs.getInt(CONFIG_STANDARD_LINIEN_STAERKE, DEFAULT_STANDARD_LINIEN_STAERKE);
	}
	public void setStandardLinienStärke(int standardLinienStärke)
	{
		prefs.setInt("standardLinienStärke", CONFIG_STANDARD_LINIEN_STAERKE, standardLinienStärke);
	}
	public LineType getStandardLinienTyp()
	{
		String name = prefs.getString(CONFIG_STANDARD_LINIEN_TYP, null);
		if(name == null)
		{
			return DEFAULT_STANDARD_LINIEN_TYP;
		}
		
		try
		{
			return Enum.valueOf(LineType.class, name);
		}
		catch(IllegalArgumentException e)
		{
			log.warn("Unknown LineType {}", name);
		}
		return DEFAULT_STANDARD_LINIEN_TYP;
	}
	public String getStandardLinienTypString()
	{
		return prefs.getString(CONFIG_STANDARD_LINIEN_TYP, DEFAULT_STANDARD_LINIEN_TYP.name());
	}
	public void setStandardLinienTyp(LineType standardLinienFarbe)
	{
		prefs.setString("standardLinienFarbe", CONFIG_STANDARD_LINIEN_TYP, standardLinienFarbe.name());
	}
	public void setStandardLinienTyp(String standardLinienFarbe)
	{
		prefs.setString("standardLinienFarbe", CONFIG_STANDARD_LINIEN_TYP, standardLinienFarbe);
	}
}
