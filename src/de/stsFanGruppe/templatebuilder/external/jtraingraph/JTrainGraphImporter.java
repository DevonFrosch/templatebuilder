package de.stsFanGruppe.templatebuilder.external.jtraingraph;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import de.stsFanGruppe.templatebuilder.external.*;
import de.stsFanGruppe.templatebuilder.external.xmlhelper.EndOfXMLException;
import de.stsFanGruppe.templatebuilder.external.xmlhelper.XMLReader;
import de.stsFanGruppe.templatebuilder.strecken.*;

public class JTrainGraphImporter implements Importer
{
	public Streckenabschnitt importStreckenabschnitt(String name, InputStream input) throws ImportException
	{
		List<JTGStation> stations = new LinkedList<>();
		
		try
		{
			XMLReader xml = new XMLReader(input);
			boolean hasStation = false;
			
			// Alle finden, die da sind... Vorsicht #Endlosschleife, unterbrochen durch Exception
			while(true)
			{
				StartElement tag = xml.findTag("stations", "sta");
				
				switch(tag.getName().getLocalPart())
				{
					case "stations":
						System.out.println("stations");
						hasStation = true;
						break;
					case "sta":
						System.out.println("sta");
						if(!xml.getNesting().peek().getLocalPart().equals("sta"))
						{
							throw new ImportException("Malformed file: sta outside stations");
						}
						Map<String, Attribute> attr = xml.getAttributes(tag);
						
						stations.add(new JTGStation(attr.get("name").getValue(), attr.get("km").getValue()));
						break;
					default:
						break;
				}
			}
		}
		catch(XMLStreamException e)
		{
			throw new ImportException(e);
		}
		catch(EndOfXMLException e)
		{}
		
		if(stations.isEmpty())
		{
			throw new ImportException("No stations found!");
		}
		
		Streckenabschnitt streckenabschnitt = new Streckenabschnitt(name);
		
		for(int i=0; i < stations.size()-1; i++)
		{
			System.out.println(stations.get(i).toXML());
			JTGStation bs1 = stations.get(i);
			JTGStation bs2 = stations.get(i+1);
			String strName = bs1.getName() + " - " + bs2.getName();
			
			Strecke strecke = new Strecke(strName, bs1, bs2, new Streckengleis(Streckengleis.GLEISWECHSELBETRIEB), new Streckengleis(Streckengleis.GLEISWECHSELBETRIEB));
			streckenabschnitt.addStrecke(strecke);
		}
		
		return streckenabschnitt;
	}
	
	public static void main(String[] args) throws Exception
	{
		Importer imp = new JTrainGraphImporter();
		
		Streckenabschnitt sa = imp.importStreckenabschnitt("Test", new java.io.FileInputStream("D:/Test.fpl"));
		
		System.out.println(sa.toXML());
	}
	
	protected class JTGStation extends Betriebsstelle
	{
		double km;
		
		public JTGStation(String name, String km)
		{
			super(name);
			this.km = Double.parseDouble(km);
		}
	}
}
