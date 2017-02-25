package de.stsFanGruppe.templatebuilder.external.jtraingraph;

import java.io.*;
import java.util.*;
import de.stsFanGruppe.templatebuilder.external.*;
import de.stsFanGruppe.templatebuilder.strecken.*;
import de.stsFanGruppe.templatebuilder.zug.*;
import de.stsFanGruppe.tools.*;

public class JTrainGraphExporter extends Exporter
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JTrainGraphExporter.class);
	protected boolean useDS100;
	
	public JTrainGraphExporter(boolean useDS100)
	{
		this.useDS100 = useDS100;
	}
	
	public void exportStreckenabschnitt(OutputStream output, Streckenabschnitt streckenabschnitt) throws ExportException
	{
		this.exportFahrten(output, streckenabschnitt, null);
	}
	
	public void exportFahrten(OutputStream output, Streckenabschnitt streckenabschnitt, Set<Fahrt> fahrten) throws ExportException
	{
		log.info("JTrainGraphExport");
		NullTester.test(output);
		NullTester.test(streckenabschnitt);
		
		if(streckenabschnitt.getBetriebsstellen().isEmpty())
			log.info("Export ohne Betriebsstellen!");
		if(fahrten == null || fahrten.isEmpty())
			log.trace("Export ohne Fahrten");
		
		StringJoiner xml = new StringJoiner("\n");
		xml.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		
		int tMin, tMax;
		if(fahrten == null || fahrten.isEmpty())
		{
			// keine Fahrten
			tMin = tMax = -1;
		}
		else
		{
			tMin = (int) fahrten.stream().min((a, b) -> Double.compare(a.getMinZeit(), b.getMinZeit())).get().getMinZeit();
			tMax = (int) fahrten.stream().max((a, b) -> Double.compare(a.getMaxZeit(), b.getMaxZeit())).get().getMaxZeit();
			
			// tMax > tMin ist zwar hier nicht weiter schlimm, aber trotzdem eine verletzte Nachbedingung
			assert tMax > tMin;
			assert tMin > 0;
		}
		
		// Enthält diverse Werte aus einer Original-Datei, deren Zweck nicht geklärt ist
		// Formatierungen sind aber auch nicht Ziel des Exports
		xml.add("<jTrainGraph_timetable version=\"008\" name=\"" + streckenabschnitt.getName() + "\" tMin=\"" + tMin + "\" tMax=\"" + tMax + "\" d=\"1111111\" "
				+ "bgC=\"weiß\" sFont=\"font(SansSerif;0;12)\" trFont=\"font(SansSerif;0;12)\" hFont=\"font(SansSerif;0;12)\" "
				+ "tFont=\"font(SansSerif;0;12)\" sHor=\"true\" sLine=\"0\" shKm=\"false\" sStation=\"-1\" eStation=\"-1\" "
				+ "cNr=\"1\" exW=\"-1\" exH=\"-1\" shV=\"false\">");
		
		xml.add("<stations>");
		
		FirstLastList<Betriebsstelle> betriebsstellen = streckenabschnitt.getBetriebsstellen();
		
		for(Betriebsstelle b : betriebsstellen)
		{
			assert b != null;
			String name = (useDS100 && b.getDS100() != null ? b.getDS100() : b.getName());
			double km = 0;
			try
			{
				km = b.getMaxKm();
			}
			catch(NoSuchElementException e)
			{
				throw new ExportException("Ungültige Betriebsstelle " + b.getName(), e);
			}
			// enthält wie jTrainGraph_timetable ungeklärte Formatierungen
			xml.add("<sta name=\"" + name + "\" km=\"" + km + "\" cl=\"schwarz\" sh=\"true\" sz=\"1\" sy=\"0\"></sta>");
		}
		
		xml.add("</stations>");
		
		if(fahrten != null)
		{
			xml.add("<trains>");
			
			if(!fahrten.isEmpty() && !betriebsstellen.isEmpty())
			{
				for(Fahrt fahrt : fahrten)
				{
					assert fahrt != null;
					NavigableSet<Fahrplanhalt> fahrplanhalte = fahrt.getFahrplanhalte();
					FirstLastList<String> halte = new FirstLastLinkedList<>();
					Betriebsstelle anfang = null;
					Betriebsstelle ende = null;
					
					for(Betriebsstelle bs : betriebsstellen)
					{
						Optional<Fahrplanhalt> ofh = fahrplanhalte.stream().filter((a) -> a.getGleisabschnitt().getParent().getParent().equals(bs)).findFirst();
						
						String ankunft = "";
						String abfahrt = "";
						
						if(ofh.isPresent())
						{
							Fahrplanhalt fh = ofh.get();
							if(fh == fahrt.getFahrplanhalte().first())
							{
								anfang = bs;
							}
							else
							{
								ankunft = TimeFormater.optionalDoubleToString(fh.getAnkunft());
							}
							if(fh == fahrt.getFahrplanhalte().last())
							{
								ende = bs;
							}
							else
							{
								abfahrt = TimeFormater.optionalDoubleToString(fh.getAbfahrt());
							}
						}
						
						// Halt an sta, Reihenfolge und Anzahl exakt wie unter stations angegeben, egal in welcher Richtung
						halte.add("<t a=\"" + ankunft + "\" d=\"" + abfahrt + "\"></t>");
					}
					
					// ti = hin, ta = rück
					// hängt hier an Anfangs- und Endbetriebsstelle der Fahrt, bei an=ab ist es ti
					String richtung = (betriebsstellen.indexOf(anfang) <= betriebsstellen.indexOf(ende)) ? "ti" : "ta";
					
					// enthält wie jTrainGraph_timetable ungeklärte Formatierungen
					xml.add("<" + richtung + " name=\"" + fahrt.getName() + "\" cm=\"\" cl=\"schwarz\" sh=\"true\" sz=\"1\" sy=\"0\" d=\"1111111\" id=\"\">");
					for(String halt : halte)
					{
						xml.add(halt);
					}
					xml.add("</" + richtung + ">");
				}
			}
			xml.add("</trains>");
		}
		
		xml.add("</jTrainGraph_timetable>");
		
		// in die Datei schreiben
		// Am Ende, damit bei Exceptions im Export die Datei nicht überschrieben wird
		log.trace("Export erzeugt, schreibe in Datei...");
		BufferedWriter writer;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
		}
		catch(UnsupportedEncodingException e)
		{
			throw new ExportException("UTF-8 nicht unterstützt", e);
		}
		
		try
		{
			writer.write(xml.toString());
			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			throw new ExportException("Fehler beim Schreiben der Datei", e);
		}
	}
}
