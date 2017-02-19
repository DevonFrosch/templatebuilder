package de.stsFanGruppe.templatebuilder.external.jtraingraph;

import java.awt.Color;
import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.FahrtDarstellungConfig;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.filter.FahrtFilter;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype.LineType;
import de.stsFanGruppe.templatebuilder.external.ImportException;
import de.stsFanGruppe.templatebuilder.external.xmlhelper.XMLElement;
import de.stsFanGruppe.templatebuilder.external.xmlhelper.XMLReader;
import de.stsFanGruppe.templatebuilder.zug.FahrtDarstellung;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.NullTester;

public class JTrainGraphZugregelImporter
{
protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JTrainGraphZugregelImporter.class);
	
	public void importRegeln(InputStream input) throws ImportException
	{
		log.info("JTrainGraphZugregelImporter");
		NullTester.test(input);
		
		FahrtDarstellungConfig config = new FahrtDarstellungConfig();
		
		try
		{
			XMLReader xml = new XMLReader(input);
			
			XMLElement tJtgR = xml.findTag("jTrainGraph_rules");
			
			// StandardFarbe
			config.setStandardLinienFarbe(stringToColor(tJtgR.getAttribute("defaultcl"), config.getStandardLinienFarbe()));
			
			// StandardBreite
			String defaultsz = tJtgR.getAttribute("defaultsz");
			if(!defaultsz.equalsIgnoreCase("keine"))
			{
				try
				{
					config.setStandardLinienBreite(Integer.parseInt(defaultsz));
				}
				catch(NumberFormatException e)
				{
					log.error("jTrainGraph_rules defaultsz: {} ist kein Int.", defaultsz);
				}
			}
			
			// StandardTyp
			config.setStandardLinienTyp(stringToLineType(tJtgR.getAttribute("defaultsy"), config.getStandardLinienTyp()));
			
			XMLElement rule = null;
			FahrtDarstellung defaultDarstellung = config.getStandardFahrtDarstellung();
			FirstLastLinkedList<FahrtDarstellung> list = new FirstLastLinkedList<>();
			
			// suche alle rule
			while((rule = xml.findTagUntil("jTrainGraph_rules", "rule")) != null)
			{
				FahrtFilter filter = stringToFilter(rule.getAttribute("type"), defaultDarstellung.getFilter());
				String muster = rule.getAttribute("text");
				Color farbe = stringToColor(rule.getAttribute("cl"), defaultDarstellung.getFarbe());
				int breite = defaultDarstellung.getBreite();
				LineType typ = stringToLineType(rule.getAttribute("sy"), defaultDarstellung.getTyp());
				// sh (Zug wird gezeichnet) wird ignoriert
				
				try
				{
					breite = Integer.parseInt(rule.getAttribute("sz"));
				}
				catch(NumberFormatException|NullPointerException e)
				{
					log.error("Regel {} hat Exception bei Linienbreite sz={}", muster, rule.getAttribute("sz"));
					throw e;
				}
				
				list.add(new FahrtDarstellung(filter, muster, farbe, breite, typ));
			}
			
			log.debug("Schreibe {} Einstellungen.", list.size());
			config.setFahrtDarstellungen(FahrtDarstellung.toArray(list));
			config.schreibeEinstellungen();
		}
		catch(XMLStreamException e)
		{
			log.error("XML-Exception: {}", e.getMessage());
			throw new ImportException("Fehler im XML", e);
		}
		catch(NumberFormatException e)
		{
			log.error("NumberFormatException: {}", e.getMessage());
			throw new ImportException("Numerischer Wert nicht erkannt", e);
		}
	}
	
	protected FahrtFilter stringToFilter(String input, FahrtFilter defaultValue)
	{
		switch(input)
		{
			case "enthält":
				return FahrtFilter.ENTHAELT;
			case "ist":
				return FahrtFilter.GLEICH;
			case "beginnt mit":
				return FahrtFilter.BEGINNT_MIT;
			case "endet mit":
				return FahrtFilter.ENDET_MIT;
			case "ist kleiner als":
				return FahrtFilter.KLEINER_ALS;
			case "ist größer als":
				return FahrtFilter.GROESSER_ALS;
			case "ist gerade":
				return FahrtFilter.GERADE;
			case "ist ungerade":
				return FahrtFilter.UNGERADE;
			default:
				log.info("Filter {} ignoriert.", input);
				break;
		}
		return defaultValue;
	}
	private Color stringToColor(String input, Color defaultValue)
	{
		switch(input)
		{
			case "schwarz":
				return Color.BLACK;
			case "grau":
				return Color.GRAY;
			case "weiß":
				return Color.WHITE;
			case "rot":
				return Color.RED;
			case "orange":
				return Color.ORANGE;
			case "gelb":
				return Color.YELLOW;
			case "blau":
				return Color.BLUE;
			case "hellblau":
				return new Color(0, 191, 255);
			case "grün":
				return Color.GREEN;
			case "dunkelgrün":
				return new Color(0, 128, 0);
			case "braun":
				return new Color(139, 69, 19);
			case "magenta":
				return Color.MAGENTA;
			default:
				log.info("Farbe {} ignoriert.", input);
				break;
		}
		return defaultValue;
	}
	protected LineType stringToLineType(String input, LineType defaultValue)
	{
		switch(input.toLowerCase())
		{
			case "0":
				// Normal
				return LineType.SOLID_LINE;
			case "1":
				// Gepunktet
				return LineType.DOTTED_LINE;
			case "2":
				// Kurz gestrichelt
				return LineType.DASEHED_LINE;
			case "3":
				// Länger gestrichelt (6,3)
				return LineType.SHORT_LONG_LINE;
			case "4":
				// Länger gestrichelt (9,3)
				return LineType.SHORT_SHORT_LONG_LINE;
			case "5":
				// Lang gestrichelt
				return LineType.SHORT_LONG_LONG_LINE;
			case "keine":
			default:
				log.info("Linientyp {} ignoriert.", input);
				break;
		}
		return defaultValue;
	}
	private static boolean isEmpty(String str)
	{
		return str == null || str.isEmpty();
	}
}
