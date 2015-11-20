package de.stsFanGruppe.templatebuilder.external.jtraingraph;

import java.io.InputStream;
import java.util.*;
import javax.xml.stream.XMLStreamException;
import de.stsFanGruppe.templatebuilder.external.*;
import de.stsFanGruppe.templatebuilder.external.xmlhelper.*;
import de.stsFanGruppe.templatebuilder.strecken.*;
import de.stsFanGruppe.templatebuilder.zug.*;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;
import de.stsFanGruppe.tools.NullTester;

public class JTrainGraphImporter implements Importer
{
	public Streckenabschnitt importStreckenabschnitt(InputStream input) throws ImportException
	{
		NullTester.test(input);
		Streckenabschnitt streckenabschnitt = null;
		
		try
		{
			XMLReader xml = new XMLReader(input);
			
			XMLElement tJtgTt = xml.findTag("jTrainGraph_timetable");
			XMLElement tStations = xml.findTag("stations");
			
			FirstLastList<Betriebsstelle> betriebsstellen = new FirstLastLinkedList<>();
			XMLElement tSta = null;
			
			while((tSta = xml.findTagUntil("stations", "sta")) != null)
			{
				String stName = tSta.getAttribute("name");
				Betriebsstelle betriebsstelle = new Betriebsstelle(stName);
				
				// Ortsangabe
				String km = tSta.getAttribute("km");
				if(isEmpty(km))
				{
					throw new ImportException("Keine Ortsangabe f�r Station.");
				}
				
				double dKm = Double.parseDouble(km);
				Gleis gleis = new Gleis(stName, dKm);
				Gleisabschnitt gleisabschnitt = gleis.getGleisabschnitte().first();
				
				betriebsstelle.addGleis(gleis);
				betriebsstellen.add(betriebsstelle);
			}
			
			String strName = tJtgTt.getAttribute("name");
			if(isEmpty(strName))
			{
				strName = makeName(betriebsstellen.first(), betriebsstellen.last());
			}
			
			// Strecken erzeugen
			streckenabschnitt = new Streckenabschnitt(strName);
			for(int i=0; i <= betriebsstellen.size()-2; i++)
			{
				Betriebsstelle anfang = betriebsstellen.get(i);
				Betriebsstelle ende = betriebsstellen.get(i+1);
				streckenabschnitt.addStrecke(new Strecke(makeName(anfang, ende), anfang, ende));
			}
		}
		catch(XMLStreamException | NumberFormatException e)
		{
			log(e.getMessage());
			throw new ImportException(e);
		}
		return streckenabschnitt;
	}
	
	public Set<Fahrt> importFahrten(InputStream input, Streckenabschnitt streckenabschnitt, Linie linie) throws ImportException
	{
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
			
			XMLElement tJtgTt = xml.findTag("jTrainGraph_timetable");
			XMLElement tTrains = xml.findTag("trains");
			
			XMLElement train = null;
			List<Fahrplanhalt> halte = new LinkedList<>();
			
			for(int fahrtCounter = 1; (train = xml.findTagUntil("trains", "ti", "ta")) != null; fahrtCounter++)
			{
				XMLElement tTime = null;
				boolean richtung = train.getName().toLowerCase() == "ti";
				NavigableSet<Fahrplanhalt> fahrplanhalte = new TreeSet<>();
				
				for(int i=0; (tTime = xml.findTagUntil(train.getName().toLowerCase(), "t")) != null; i++)
				{
					String an = tTime.getAttribute("a");
					String ab = tTime.getAttribute("d");
					
					if(isEmpty(an))
					{
						if(isEmpty(ab))
						{
							continue;
						}
						else
						{
							an = ab;
						}
					}
					if(isEmpty(ab))
					{
						ab = an;
					}
					
					try
					{
						double doubleAn = parseTime(an);
						double doubleAb = parseTime(ab);
						Fahrplanhalt f = new Fahrplanhalt(gleisabschnitte.get(i), doubleAn, doubleAb, new FahrplanhaltEigenschaften());
						fahrplanhalte.add(f);
					}
					catch(NumberFormatException e)
					{
						log("NumberFormatException");
						throw new ImportException(e);
					}
				}
				
				// leere Fahrten brauchen wir nicht importieren
				if(!fahrplanhalte.isEmpty())
				{
					fahrten.add(new Fahrt(train.getAttribute("name"), linie, fahrplanhalte));
				}
			}
		}
		catch(XMLStreamException | NumberFormatException e)
		{
			log(e.getMessage());
			throw new ImportException(e);
		}
		return fahrten;
	}
	
	private static String makeName(Betriebsstelle anfang, Betriebsstelle ende)
	{
		assert anfang != null;
		assert ende != null;
		
		// Anfang - Ende
		return anfang.getName()+" - "+ende.getName();
	}
	private static double parseTime(String time) throws NumberFormatException
	{
		if(isEmpty(time) || !time.contains(":") || (time.indexOf(':') != time.lastIndexOf(':')))
			throw new NumberFormatException();
		
		String[] ts = time.split(":");
		return (Integer.parseInt(ts[0]) * 60) + Integer.parseInt(ts[1]);
	}
	private static boolean isEmpty(String str)
	{
		return str == null || str.isEmpty();
	}
	private static Gleisabschnitt getGleisabschnitt(Betriebsstelle betriebsstelle)
	{
		assert betriebsstelle != null;
		
		return betriebsstelle.getGleise().get(0).getGleisabschnitte().first();
	}
	private void log(String text)
	{
		System.out.println("JTG-Import: "+text);
	}
}
