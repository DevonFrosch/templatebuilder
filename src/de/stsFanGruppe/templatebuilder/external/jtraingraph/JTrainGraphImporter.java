package de.stsFanGruppe.templatebuilder.external.jtraingraph;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.stream.XMLStreamException;
import de.stsFanGruppe.templatebuilder.external.*;
import de.stsFanGruppe.templatebuilder.external.xmlhelper.*;
import de.stsFanGruppe.templatebuilder.strecken.*;
import de.stsFanGruppe.templatebuilder.zug.*;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.TimeFormater;

public class JTrainGraphImporter extends Importer
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JTrainGraphImporter.class);
	
	public Streckenabschnitt importStreckenabschnitt(InputStream input) throws ImportException
	{
		log.info("JTrainGraphImport Streckenabschnitt");
		NullTester.test(input);
		
		Streckenabschnitt streckenabschnitt = null;
		
		try
		{
			XMLReader xml = new XMLReader(input);
			
			XMLElement tJtgTt = xml.findTag("jTrainGraph_timetable", "fahrplandaten");
			if(tJtgTt.getName().toLowerCase() == "fahrplandaten")
			{
				throw new ImportException("Zu alte Version einer JTG-Datei");
			}
			
			XMLElement tStations = xml.findTag("stations");
			
			FirstLastList<Betriebsstelle> betriebsstellen = new FirstLastLinkedList<>();
			XMLElement tSta = null;
			
			// suche alle sta (Betriebsstellen)
			while((tSta = xml.findTagUntil("stations", "sta")) != null)
			{
				String stName = tSta.getAttribute("name");
				Betriebsstelle betriebsstelle = new Betriebsstelle(stName);
				
				// Ortsangabe
				String km = tSta.getAttribute("km");
				if(isEmpty(km))
				{
					throw new ImportException("Keine Ortsangabe für Station " + stName);
				}
				
				Gleis gleis = new Gleis(stName, Double.parseDouble(km.replace(',', '.')));
				Gleisabschnitt gleisabschnitt = gleis.getGleisabschnitte().first();
				
				betriebsstelle.addGleis(gleis);
				betriebsstellen.add(betriebsstelle);
			}
			betriebsstellen.sort((Betriebsstelle a, Betriebsstelle b) -> a.compareByKM(b));
			String strName = tJtgTt.getAttribute("name");
			if(isEmpty(strName))
			{
				strName = makeName(betriebsstellen.first(), betriebsstellen.last());
			}
			
			// Strecken erzeugen
			streckenabschnitt = new Streckenabschnitt(strName);
			for(int i = 0; i <= betriebsstellen.size() - 2; i++)
			{
				Betriebsstelle anfang = betriebsstellen.get(i);
				Betriebsstelle ende = betriebsstellen.get(i + 1);
				streckenabschnitt.addStrecke(new Strecke(makeName(anfang, ende), anfang, ende));
			}
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
		catch(Exception e)
		{
			log.error("Exception", e);
			throw new ImportException(e);
		}
		
		return streckenabschnitt;
	}
	
	public Set<Fahrt> importFahrten(InputStream input, Streckenabschnitt streckenabschnitt, Linie linie) throws ImportException
	{
		log.info("JTrainGraphImport Fahrten");
		NullTester.test(input);
		NullTester.test(streckenabschnitt);
		NullTester.test(linie);
		
		Set<Fahrt> fahrten = new HashSet<Fahrt>();
		
		// Erstelle eine Liste der Gleisabschnitte
		FirstLastList<Gleisabschnitt> gleisabschnitte = new FirstLastLinkedList<>();
		FirstLastList<Strecke> strecken = streckenabschnitt.getStrecken();
		gleisabschnitte.add(getGleisabschnitt(strecken.first().getAnfang()));
		strecken.forEach((Strecke s) -> gleisabschnitte.add(getGleisabschnitt(s.getEnde())));
		
		try
		{
			XMLReader xml = new XMLReader(input);

			XMLElement tJtgTt = xml.findTag("jTrainGraph_timetable", "fahrplandaten");
			if(tJtgTt.getName().toLowerCase() == "fahrplandaten")
			{
				throw new ImportException("Zu alte Version einer JTG-Datei");
			}
			
			XMLElement tTrains = xml.findTag("trains");
			
			XMLElement train = null;
			List<Fahrplanhalt> halte = new LinkedList<>();
			
			for(int fahrtCounter = 1; (train = xml.findTagUntil("trains", "ti", "ta")) != null; fahrtCounter++)
			{
				XMLElement tTime = null;
				boolean richtung = train.getName().toLowerCase() == "ti";
				NavigableSet<Fahrplanhalt> fahrplanhalte = new TreeSet<>();
				
				for(int i = 0; (tTime = xml.findTagUntil(train.getName().toLowerCase(), "t")) != null; i++)
				{
					String an = tTime.getAttribute("a");
					String ab = tTime.getAttribute("d");
					
					if(isEmpty(an) && isEmpty(ab))
					{
						continue;
					}
					
					try
					{
						OptionalDouble doubleAn = TimeFormater.stringToOptionalDouble(an);
						OptionalDouble doubleAb = TimeFormater.stringToOptionalDouble(ab);
						
						// An > Ab -> ignorieren da vermutlich Tagesübergang
						if(doubleAn.isPresent() && doubleAb.isPresent() && doubleAn.getAsDouble() > doubleAb.getAsDouble())
						{
							continue;
						}
						
						Fahrplanhalt f = new Fahrplanhalt(gleisabschnitte.get(i), doubleAn, doubleAb, new FahrplanhaltEigenschaften());
						fahrplanhalte.add(f);
					}
					catch(IllegalArgumentException e)
					{
						log.error("Exception bei Zug {}", train.getAttribute("name"), e);
						throw new ImportException(e);
					}
				}
				
				// leere Fahrten brauchen wir nicht importieren
				if(!fahrplanhalte.isEmpty())
				{
					String templateName = null;
					String comment = train.getAttribute("cm");
					Matcher templateNameMatcher = Pattern.compile("Template: (.*)").matcher(comment);
					
					if(templateNameMatcher.find())
					{
						try
						{
							templateName = templateNameMatcher.group(1);
						}
						catch(IndexOutOfBoundsException e)
						{}
					}
					
					fahrten.add(new Fahrt(train.getAttribute("name"), linie, templateName, fahrplanhalte));
				}
			}
		}
		catch(XMLStreamException | NumberFormatException e)
		{
			log.error(e.getMessage());
			throw new ImportException(e);
		}
		return fahrten;
	}
	
	private static Gleisabschnitt getGleisabschnitt(Betriebsstelle betriebsstelle)
	{
		assert betriebsstelle != null;
		
		return betriebsstelle.getGleise().get(0).getGleisabschnitte().first();
	}
}
