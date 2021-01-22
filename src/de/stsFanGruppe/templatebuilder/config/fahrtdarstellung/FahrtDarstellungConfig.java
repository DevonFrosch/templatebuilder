package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung;

import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import de.stsFanGruppe.templatebuilder.config.ConfigController;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.filter.FahrtFilter;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype.LineType;
import de.stsFanGruppe.templatebuilder.editor.EditorDaten;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.templatebuilder.zug.FahrtDarstellung;
import de.stsFanGruppe.templatebuilder.zug.Fahrtabschnitt;
import de.stsFanGruppe.templatebuilder.zug.HervorgehobeneFahrtabschnitt;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.PreferenceHandler;
import de.stsFanGruppe.tools.TimeFormater;

public class FahrtDarstellungConfig extends ConfigController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FahrtDarstellungConfig.class);
	
	public static final String CONFIG_FAHRTDARSTELLUNG_POSTFIX_FILTER = "/filter";
	public static final String CONFIG_FAHRTDARSTELLUNG_POSTFIX_MUSTER = "/muster";
	public static final String CONFIG_FAHRTDARSTELLUNG_POSTFIX_FARBE = "/farben";
	public static final String CONFIG_FAHRTDARSTELLUNG_POSTFIX_BREITE = "/breite";
	public static final String CONFIG_FAHRTDARSTELLUNG_POSTFIX_TYP = "/typ";
	
	// Farbeinstellungen
	public static final String CONFIG_STANDARD_LINIEN_PREFIX = "bildfahrplan/darstellung/linie/standard";
	public static final String CONFIG_STANDARD_LINIEN_FARBE = CONFIG_STANDARD_LINIEN_PREFIX + CONFIG_FAHRTDARSTELLUNG_POSTFIX_FARBE;
	public static final String CONFIG_STANDARD_LINIEN_BREITE = CONFIG_STANDARD_LINIEN_PREFIX + CONFIG_FAHRTDARSTELLUNG_POSTFIX_BREITE;
	public static final String CONFIG_STANDARD_LINIEN_TYP = CONFIG_STANDARD_LINIEN_PREFIX + CONFIG_FAHRTDARSTELLUNG_POSTFIX_TYP;
	
	public static final Color DEFAULT_FAHRTDARSTELLUNG_FARBE = Color.BLACK;
	public static final int DEFAULT_FAHRTDARSTELLUNG_BREITE = 1;
	public static final LineType DEFAULT_FAHRTDARSTELLUNG_TYP = LineType.SOLID_LINE;
	
	public static final String CONFIG_REGEL_FAHRTNAME_PREFIX = "bildfahrplan/darstellung/linie/fahrtname";
	
	public static final FahrtFilter DEFAULT_FAHRTDARSTELLUNG_FILTER = FahrtFilter.BEGINNT_MIT;
	public static final String DEFAULT_FAHRTDARSTELLUNG_MUSTER = "";
	
	// nicht persistente Einstellungen
	private String zugsucheText = null;
	private String templatesucheText = null;
	private Set<Fahrtabschnitt> hervorgehobeneFahrten = new HashSet<>();
	
	// Konstruktoren
	public FahrtDarstellungConfig()
	{
		this.prefs = new PreferenceHandler(FahrtDarstellungConfig.class);
		assert prefs != null;
	}
	
	// Getter / Setter
	public Color getStandardLinienFarbe()
	{
		return prefs.getColor(CONFIG_STANDARD_LINIEN_FARBE, DEFAULT_FAHRTDARSTELLUNG_FARBE);
	}
	
	public void setStandardLinienFarbe(Color standardLinienFarbe)
	{
		prefs.setColor("standardLinienFarbe", CONFIG_STANDARD_LINIEN_FARBE, standardLinienFarbe);
		notifyChange();
	}
	
	public int getStandardLinienBreite()
	{
		return prefs.getInt(CONFIG_STANDARD_LINIEN_BREITE, DEFAULT_FAHRTDARSTELLUNG_BREITE);
	}
	
	public void setStandardLinienBreite(int standardLinienBreite)
	{
		prefs.setInt("standardLinienBreite", CONFIG_STANDARD_LINIEN_BREITE, standardLinienBreite);
		notifyChange();
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
		notifyChange();
	}
	
	public void setStandardLinienTyp(String standardLinienFarbe)
	{
		prefs.setString("standardLinienFarbe", CONFIG_STANDARD_LINIEN_TYP, standardLinienFarbe);
		notifyChange();
	}
	
	public String getZugsucheText()
	{
		return zugsucheText;
	}
	
	public void setZugsucheText(String zugsucheText)
	{
		this.zugsucheText = zugsucheText;
		notifyChange();
	}
	
	public String getTemplatesucheText()
	{
		return templatesucheText;
	}
	
	public void setTemplatesucheText(String templatesucheText)
	{
		this.templatesucheText = templatesucheText;
		notifyChange();
	}
	
	public Set<Fahrtabschnitt> getHervorgehobeneFahrten()
	{
		synchronized(hervorgehobeneFahrten)
		{
			return hervorgehobeneFahrten;
		}
	}
	
	public String getHervorgehobeneZuegeText(EditorDaten editorDaten)
	{
		if(editorDaten == null)
		{
			return "";
		}
		
		StringJoiner zuege = new StringJoiner("\n");
		
		FirstLastList<Fahrtabschnitt> abschnitte = new FirstLastLinkedList<Fahrtabschnitt>(getHervorgehobeneFahrten());
		
		abschnitte.sort((a, b) -> a.getFahrt().getName().compareTo(b.getFahrt().getName()));
		
		for(Fahrtabschnitt abschnitt: abschnitte)
		{
			Fahrt fahrt = abschnitt.getFahrt();
			String template = fahrt.getTemplate().toString();

			StringBuilder builder = new StringBuilder(fahrt.getName());
			
			if(!template.isEmpty())
			{
				builder.append(" [" + template + "]");
			}
			
			String abfahrt = TimeFormater.doubleToString(abschnitt.getStart().getMinZeit());
			builder.append(" (Abfahrt " + abfahrt + ")");
			
			zuege.add(builder.toString());
		}
		
		return zuege.toString();
	}
	
	public List<HervorgehobeneFahrtabschnitt> getHervorgehobeneFahrtabschnitte(EditorDaten editorDaten) 
	{
		if(editorDaten == null)
		{
			return null;
		}
		
		FirstLastList<Fahrtabschnitt> abschnitte = new FirstLastLinkedList<Fahrtabschnitt>(getHervorgehobeneFahrten());
		
		abschnitte.sort((a, b) -> a.getFahrt().getName().compareTo(b.getFahrt().getName()));
		return abschnitte.stream().map(abschnitt -> new HervorgehobeneFahrtabschnitt(abschnitt)).collect(Collectors.toList());
			
	}
	
	public void setHervorgehobeneZuege(Collection<Fahrtabschnitt> abschnitte)
	{
		NullTester.test(abschnitte);
		synchronized(hervorgehobeneFahrten)
		{
			hervorgehobeneFahrten = new HashSet<>(abschnitte);
		}
		notifyChange();
	}
	
	public FahrtDarstellung getStandardFahrtDarstellung()
	{
		return new FahrtDarstellung(DEFAULT_FAHRTDARSTELLUNG_FILTER, DEFAULT_FAHRTDARSTELLUNG_MUSTER, DEFAULT_FAHRTDARSTELLUNG_FARBE,
				DEFAULT_FAHRTDARSTELLUNG_BREITE, DEFAULT_FAHRTDARSTELLUNG_TYP);
	}
	
	protected void setFahrtDarstellung(String logName, String configName, FahrtDarstellung value)
	{
		assert value != null;
		
		prefs.setString(logName + "-Filter", configName + CONFIG_FAHRTDARSTELLUNG_POSTFIX_FILTER, value.getFilter().name());
		prefs.setString(logName + "-Name", configName + CONFIG_FAHRTDARSTELLUNG_POSTFIX_MUSTER, value.getMuster());
		prefs.setColor(logName + "-Farbe", configName + CONFIG_FAHRTDARSTELLUNG_POSTFIX_FARBE, value.getFarbe());
		prefs.setInt(logName + "-Breite", configName + CONFIG_FAHRTDARSTELLUNG_POSTFIX_BREITE, value.getBreite());
		prefs.setString(logName + "-Typ", configName + CONFIG_FAHRTDARSTELLUNG_POSTFIX_TYP, value.getTyp().name());
		notifyChange();
	}
	
	protected FahrtDarstellung getFahrtDarstellung(String configName, FahrtDarstellung defaultValue)
	{
		assert defaultValue != null;
		
		try
		{
			String filterString = prefs.getString(configName + CONFIG_FAHRTDARSTELLUNG_POSTFIX_FILTER, defaultValue.getFilter().name());
			String name = prefs.getString(configName + CONFIG_FAHRTDARSTELLUNG_POSTFIX_MUSTER, defaultValue.getMuster());
			Color farbe = prefs.getColor(configName + CONFIG_FAHRTDARSTELLUNG_POSTFIX_FARBE, defaultValue.getFarbe());
			int breite = prefs.getInt(configName + CONFIG_FAHRTDARSTELLUNG_POSTFIX_BREITE, defaultValue.getBreite());
			String typString = prefs.getString(configName + CONFIG_FAHRTDARSTELLUNG_POSTFIX_TYP, defaultValue.getTyp().name());
			
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
	public void setFahrtDarstellungen(FahrtDarstellung[] values)
	{
		int i = 0;
		for(FahrtDarstellung fd: values)
		{
			setFahrtDarstellung("FahrtDarstellung " + i, CONFIG_REGEL_FAHRTNAME_PREFIX + "/" + i, fd);
			i++;
		}
		prefs.setInt("Anzahl FahrtDarstellungen", CONFIG_REGEL_FAHRTNAME_PREFIX + "/count", i);
		notifyChange();
	}
	
	public FahrtDarstellung[] getFahrtDarstellungen()
	{
		int count = prefs.getInt(CONFIG_REGEL_FAHRTNAME_PREFIX + "/count", 0);
		
		FahrtDarstellung defaultValue = getStandardFahrtDarstellung();
		FirstLastList<FahrtDarstellung> liste = new FirstLastLinkedList<>();
		
		for(int i = 0; i < count; i++)
		{
			liste.add(getFahrtDarstellung(CONFIG_REGEL_FAHRTNAME_PREFIX + "/" + i, defaultValue));
		}
		
		return liste.toArray(new FahrtDarstellung[liste.size()]);
	}
	
	public FahrtDarstellung getZugsucheFahrtDarstellung()
	{
		if(zugsucheText == null)
		{
			return null;
		}
		return new FahrtDarstellung(FahrtFilter.ENTHAELT, zugsucheText, Color.RED, 2, LineType.DASHED_LINE);
	}
	
	public FahrtDarstellung getTemplatesucheFahrtDarstellung()
	{
		if(templatesucheText == null)
		{
			return null;
		}
		return new FahrtDarstellung(FahrtFilter.ENTHAELT, templatesucheText, Color.RED, 2, LineType.DOTTED_LINE);
	}
	
	public FahrtDarstellung[] getFahrtHervorhebungsDarstellungen()
	{
		FirstLastList<FahrtDarstellung> liste = new FirstLastLinkedList<>();
		
		Color blau = new Color(0, 0, 255, 32);
		synchronized(hervorgehobeneFahrten)
		{
			for(Fahrtabschnitt abschnitt: hervorgehobeneFahrten)
			{
				Fahrt fahrt = abschnitt.getFahrt();
				String zugName = fahrt.getName();
				liste.add(new FahrtDarstellung(FahrtFilter.GLEICH, zugName, blau, 5, LineType.SOLID_LINE));
			}
		}
		
		return liste.toArray(new FahrtDarstellung[liste.size()]);
	}
}
