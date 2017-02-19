package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung;

import java.awt.Color;
import de.stsFanGruppe.templatebuilder.config.ConfigController;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.filter.FahrtFilter;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype.LineType;
import de.stsFanGruppe.templatebuilder.zug.FahrtDarstellung;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;
import de.stsFanGruppe.tools.PreferenceHandler;

public class FahrtDarstellungConfig extends ConfigController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FahrtDarstellungConfig.class);
	private static FahrtDarstellungSettingsGUI gui;
	
	public static final String CONFIG_FAHRTDARSTELLUNG_POSTFIX_FILTER = "/filter";
	public static final String CONFIG_FAHRTDARSTELLUNG_POSTFIX_MUSTER = "/muster";
	public static final String CONFIG_FAHRTDARSTELLUNG_POSTFIX_FARBE = "/farben";
	public static final String CONFIG_FAHRTDARSTELLUNG_POSTFIX_BREITE = "/breite";
	public static final String CONFIG_FAHRTDARSTELLUNG_POSTFIX_TYP = "/typ";
	
	// Farbeinstellungen
	public static final String CONFIG_STANDARD_LINIEN_PREFIX = "bildfahrplan/darstellung/linie/standard";
	public static final String CONFIG_STANDARD_LINIEN_MUSTER = CONFIG_STANDARD_LINIEN_PREFIX+CONFIG_FAHRTDARSTELLUNG_POSTFIX_MUSTER;
	public static final String CONFIG_STANDARD_LINIEN_BREITE = CONFIG_STANDARD_LINIEN_PREFIX+CONFIG_FAHRTDARSTELLUNG_POSTFIX_FARBE;
	public static final String CONFIG_STANDARD_LINIEN_TYP = CONFIG_STANDARD_LINIEN_PREFIX+CONFIG_FAHRTDARSTELLUNG_POSTFIX_BREITE;
	
	public static final Color DEFAULT_FAHRTDARSTELLUNG_FARBE = Color.BLACK;
	public static final int DEFAULT_FAHRTDARSTELLUNG_BREITE = 1;
	public static final LineType DEFAULT_FAHRTDARSTELLUNG_TYP = LineType.SOLID_LINE;

	public static final String CONFIG_REGEL_FAHRTNAME_PREFIX = "bildfahrplan/darstellung/linie/fahrtname";
	
	public static final FahrtFilter DEFAULT_FAHRTDARSTELLUNG_FILTER = FahrtFilter.BEGINNT_MIT;
	public static final String DEFAULT_FAHRTDARSTELLUNG_MUSTER = "";
	
	// Konstruktoren
	public FahrtDarstellungConfig()
	{
		this.prefs = new PreferenceHandler(FahrtDarstellungConfig.class, () -> notifyChange());
		assert prefs != null;
	}
	
	// Getter / Setter
	public Color getStandardLinienFarbe()
	{
		return prefs.getColor(CONFIG_STANDARD_LINIEN_MUSTER, DEFAULT_FAHRTDARSTELLUNG_FARBE);
	}
	public void setStandardLinienFarbe(Color standardLinienFarbe)
	{
		prefs.setColor("standardLinienFarbe", CONFIG_STANDARD_LINIEN_MUSTER, standardLinienFarbe);
	}
	public int getStandardLinienStärke()
	{
		return prefs.getInt(CONFIG_STANDARD_LINIEN_BREITE, DEFAULT_FAHRTDARSTELLUNG_BREITE);
	}
	public void setStandardLinienBreite(int standardLinienStärke)
	{
		prefs.setInt("standardLinienStärke", CONFIG_STANDARD_LINIEN_BREITE, standardLinienStärke);
	}
	public LineType getStandardLinienTyp()
	{
		String name = prefs.getString(CONFIG_STANDARD_LINIEN_TYP, null);
		if(name == null)
		{
			return DEFAULT_FAHRTDARSTELLUNG_TYP;
		}
		
		try
		{
			return Enum.valueOf(LineType.class, name);
		}
		catch(IllegalArgumentException e)
		{
			log.warn("Unknown LineType {}", name);
		}
		return DEFAULT_FAHRTDARSTELLUNG_TYP;
	}
	public String getStandardLinienTypString()
	{
		return prefs.getString(CONFIG_STANDARD_LINIEN_TYP, DEFAULT_FAHRTDARSTELLUNG_TYP.name());
	}
	public void setStandardLinienTyp(LineType standardLinienFarbe)
	{
		prefs.setString("standardLinienFarbe", CONFIG_STANDARD_LINIEN_TYP, standardLinienFarbe.name());
	}
	public void setStandardLinienTyp(String standardLinienFarbe)
	{
		prefs.setString("standardLinienFarbe", CONFIG_STANDARD_LINIEN_TYP, standardLinienFarbe);
	}
	
	protected void setFahrtDarstellung(String logName, String configName, FahrtDarstellung value)
	{
		assert value != null;
		
		prefs.setString(logName+"-Filter", configName+CONFIG_FAHRTDARSTELLUNG_POSTFIX_FILTER, value.getFilter().name());
		prefs.setString(logName+"-Name", configName+CONFIG_FAHRTDARSTELLUNG_POSTFIX_MUSTER, value.getMuster());
		prefs.setColor(logName+"-Farbe", configName+CONFIG_FAHRTDARSTELLUNG_POSTFIX_FARBE, value.getFarbe());
		prefs.setInt(logName+"-Breite", configName+CONFIG_FAHRTDARSTELLUNG_POSTFIX_BREITE, value.getBreite());
		prefs.setString(logName+"-Typ", configName+CONFIG_FAHRTDARSTELLUNG_POSTFIX_TYP, value.getTyp().name());
	}
	protected FahrtDarstellung getFahrtDarstellung(String configName, FahrtDarstellung defaultValue)
	{
		assert defaultValue != null;
		
		try
		{
			String filterString = prefs.getString(configName+CONFIG_FAHRTDARSTELLUNG_POSTFIX_FILTER, defaultValue.getFilter().name());
			String name = prefs.getString(configName+CONFIG_FAHRTDARSTELLUNG_POSTFIX_MUSTER, defaultValue.getMuster());
			Color farbe = prefs.getColor(configName+CONFIG_FAHRTDARSTELLUNG_POSTFIX_FARBE, defaultValue.getFarbe());
			int breite = prefs.getInt(configName+CONFIG_FAHRTDARSTELLUNG_POSTFIX_BREITE, defaultValue.getBreite());
			String typString = prefs.getString(configName+CONFIG_FAHRTDARSTELLUNG_POSTFIX_TYP, defaultValue.getTyp().name());
			
			FahrtFilter filter = null;
			if(filterString != null)
			{
				filter = FahrtFilter.valueOf(filterString);
			}
			
			LineType typ = null;
			if(typString != null)
			{
				typ = LineType.valueOf(typString);
			}
			return new FahrtDarstellung(filter, name, farbe, breite, typ);
		}
		catch(IllegalArgumentException e)
		{
			log.error("NumberFormatException beim Lesen von {}", configName, e);
		}
		
		return defaultValue;
	}
	// Fahrtdarstellungen = xxx/count, xxx/i/muster, xxx/i/farbe
	protected void setFahrtDarstellungen(FahrtDarstellung[] values)
	{
		int i=0;
		for(FahrtDarstellung fd: values)
		{
			setFahrtDarstellung("FahrtDarstellung "+i, CONFIG_REGEL_FAHRTNAME_PREFIX+"/"+i, fd);
			i++;
		}
		prefs.setInt("Anzahl FahrtDarstellungen", CONFIG_REGEL_FAHRTNAME_PREFIX+"/count", i);
	}
	protected FahrtDarstellung[] getFahrtDarstellungen()
	{
		int count = prefs.getInt(CONFIG_REGEL_FAHRTNAME_PREFIX+"/count", 0);
		
		FahrtDarstellung defaultValue = new FahrtDarstellung(DEFAULT_FAHRTDARSTELLUNG_FILTER, DEFAULT_FAHRTDARSTELLUNG_MUSTER,
				DEFAULT_FAHRTDARSTELLUNG_FARBE, DEFAULT_FAHRTDARSTELLUNG_BREITE, DEFAULT_FAHRTDARSTELLUNG_TYP);
		FirstLastList<FahrtDarstellung> list = new FirstLastLinkedList<>();
		
		for(int i=0; i < count; i++)
		{
			list.add(getFahrtDarstellung(CONFIG_REGEL_FAHRTNAME_PREFIX+"/"+i, defaultValue));
		}
		
		return list.toArray(new FahrtDarstellung[list.size()]);
	}
}
