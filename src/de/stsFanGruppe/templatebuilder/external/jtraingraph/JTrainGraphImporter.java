package de.stsFanGruppe.templatebuilder.external.jtraingraph;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import de.stsFanGruppe.templatebuilder.external.*;
import de.stsFanGruppe.templatebuilder.external.xmlhelper.*;
import de.stsFanGruppe.templatebuilder.strecken.*;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;

public class JTrainGraphImporter implements Importer
{
	public Streckenabschnitt importStreckenabschnitt(InputStream input) throws ImportException
	{
		Streckenabschnitt streckenabschnitt = null;
		
		try
		{
			XMLReader xml = new XMLReader(input);
			boolean hasStation = false;
			
			XMLElement tJtgTt = xml.findTag("jTrainGraph_timetable");
			XMLElement tStations = xml.findTag("stations");
			
			List<Betriebsstelle> betriebsstellen = new LinkedList<>();
			XMLElement tSta = null;
			
			while((tSta = xml.findTagUntil("stations", "sta")) != null)
			{
				String stName = tSta.getAttribute("name");
				Betriebsstelle betriebsstelle = new Betriebsstelle(stName);
				Gleis gleis = new Gleis(stName);
				Gleisabschnitt gleisabschnitt = gleis.getGleisabschnitte().first();
				String km = tSta.getAttribute("km");
				if(km != null && !km.isEmpty())
				{
					try
					{
						double dKm = Double.parseDouble(km);
						gleisabschnitt.setKmAnfang(dKm);
						gleisabschnitt.setKmEnde(dKm);
					}
					catch(NumberFormatException e)
					{
						log("Formatfehler in km von Station "+stName);
					}
				}
				betriebsstelle.addGleis(gleis);
				betriebsstellen.add(betriebsstelle);
			}
			
			String strName = tJtgTt.getAttribute("name");
			Betriebsstelle anfang = betriebsstellen.get(0);
			Betriebsstelle ende = betriebsstellen.get(betriebsstellen.size()-1);
			if(strName == null || strName == "")
			{
				// Anfang - Ende
				strName = anfang.getName()+" - "+ende.getName();
			}
			
			Strecke strecke = new Strecke(strName, anfang, ende);
			streckenabschnitt = new Streckenabschnitt(strName);
			streckenabschnitt.addStrecke(strecke);
		}
		catch(XMLStreamException | NumberFormatException | EndOfXMLException e)
		{
			log(e.getMessage());
			throw new ImportException(e);
		}
		return streckenabschnitt;
	}
	
	public Set<Fahrt> importFahrten(InputStream input) throws ImportException
	{
		return null;
	}
	
	public static void main(String[] args) throws Exception
	{
		Importer imp = new JTrainGraphImporter();
		
		Streckenabschnitt sa = imp.importStreckenabschnitt(new java.io.FileInputStream("RB_20_Hauptlauf.fpl"));
		
		System.out.println(sa.toXML());
	}
	
	private void log(String text)
	{
		//System.out.println("JTG-Import: "+text);
	}
}
