package de.stsFanGruppe.templatebuilder.external.jtraingraph;

import java.io.*;
import java.util.*;
import de.stsFanGruppe.templatebuilder.external.*;
import de.stsFanGruppe.templatebuilder.strecken.*;
import de.stsFanGruppe.templatebuilder.zug.*;
import de.stsFanGruppe.tools.*;

public class JTrainGraphExporter
{
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
		NullTester.test(streckenabschnitt);
		
		BufferedWriter writer;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
		}
		catch(UnsupportedEncodingException e)
		{
			throw new ExportException("UTF-8 nicht unterstützt", e);
		}
		
		StringJoiner xml = new StringJoiner("\n");
		xml.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		
		int tMin, tMax;
		if(fahrten == null)
		{
			tMin = tMax = -1;
		}
		else
		{
			tMin = (int) fahrten.stream().min((a, b) -> Double.compare(a.getMinZeit(), b.getMinZeit())).get().getMinZeit();
			tMax = (int) fahrten.stream().max((a, b) -> Double.compare(a.getMaxZeit(), b.getMaxZeit())).get().getMaxZeit();
		}
		
		xml.add("<jTrainGraph_timetable version=\"008\" name=\""+streckenabschnitt.getName()+"\" tMin=\""+tMin+"\" tMax=\""+tMax+"\" d=\"1111111\" "
				+"bgC=\"weiß\" sFont=\"font(SansSerif;0;12)\" trFont=\"font(SansSerif;0;12)\" hFont=\"font(SansSerif;0;12)\" "
				+"tFont=\"font(SansSerif;0;12)\" sHor=\"true\" sLine=\"0\" shKm=\"false\" sStation=\"-1\" eStation=\"-1\" "
				+"cNr=\"1\" exW=\"-1\" exH=\"-1\" shV=\"false\">");
		xml.add("<stations>");
		
		for(Betriebsstelle b: streckenabschnitt.getBetriebsstellen())
		{
			String name = (useDS100 && b.getDS100() != null ? b.getDS100() : b.getName());
			double km = 0;
			try
			{
				km = b.getMaxKm();
			}
			catch(IllegalStateException e)
			{
				throw new ExportException(e.getMessage());
			}
			xml.add("<sta name=\""+name+"\" km=\""+km+"\" cl=\"schwarz\" sh=\"true\" sz=\"1\" sy=\"0\"></sta>");
		}
		
		xml.add("</stations>");
		xml.add("<trains>");
		
		if(fahrten != null && !fahrten.isEmpty())
		{
			for(Fahrt fahrt: fahrten)
			{
				NavigableSet<Fahrplanhalt> fahrplanhalte = fahrt.getFahrplanhalte();
				FirstLastList<String> halte = new FirstLastLinkedList<>();
				FirstLastList<Betriebsstelle> betriebsstellen = streckenabschnitt.getBetriebsstellen();
				Betriebsstelle anfang = null;
				Betriebsstelle ende = null;
				
				for(Betriebsstelle bs: betriebsstellen)
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
							ankunft = TimeFormater.doubleToString(ofh.get().getAnkunft());
						}
						if(fh == fahrt.getFahrplanhalte().last())
						{
							ende = bs;
						}
						else
						{
							abfahrt = TimeFormater.doubleToString(ofh.get().getAbfahrt());
						}
					}
					
					halte.add("<t a=\""+ankunft+"\" d=\""+abfahrt+"\"></t>");
				}
				
				char richtung = (betriebsstellen.indexOf(anfang) < betriebsstellen.indexOf(ende)) ? 'i' : 'a';
				
				xml.add("<t"+richtung+" name=\""+fahrt.getName()+"\" cm=\"\" cl=\"schwarz\" sh=\"true\" sz=\"1\" sy=\"0\" d=\"1111111\" id=\"\">");
				for(String halt: halte)
				{
					xml.add(halt);
				}
				xml.add("</t"+richtung+">");
			}
		}
		xml.add("</trains>");
		xml.add("</jTrainGraph_timetable>");
		
		try
		{
			writer.write(xml.toString());
			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			throw new ExportException("IOException", e);
		}
	}
}
