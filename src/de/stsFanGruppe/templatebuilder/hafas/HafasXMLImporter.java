package de.stsFanGruppe.templatebuilder.hafas;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.stream.*;

public class HafasXMLImporter
{
	public static List<Station> getStations(String match) throws IOException, XMLStreamException
	{
		String url = "http://reiseauskunft.bahn.de/bin/query.exe/dn";
		
		String charset = "UTF-8";
		String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
				+ "<ReqC ver=\"1.1\" prod=\"String\" lang=\"DE\" timeTableBegin=\"04.10.2009\">"
				+ "<LocValReq id=\"001\" maxNr=\"20\" sMode=\"1\">"
				+ "<ReqLoc type=\"ST\" match=\"" + match + "*\" />"
				+ "</LocValReq>" + "</ReqC>";
		
		InputStream result = getAnswer(url, xmlData, charset);
		
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory.createXMLStreamReader(result);
		
		StringBuilder spacer = new StringBuilder();
		
		List<Station> stations = new LinkedList<>();
		
		while(parser.hasNext())
		{
			switch(parser.getEventType())
			{
				case XMLStreamConstants.END_DOCUMENT:
					parser.close();
					break;
				
				case XMLStreamConstants.START_ELEMENT:
					if(parser.getLocalName().equals("Station"))
					{
						StationBuilder builder = new StationBuilder();
						for(int i = 0; i < parser.getAttributeCount(); i++)
						{
							switch(parser.getAttributeLocalName(i))
							{
								case "name":
									builder.setName(parser.getAttributeValue(i));
									break;
								case "externalId":
									builder.setExternalId(parser.getAttributeValue(i));
									break;
								case "externalStationNr":
									builder.setExternalStationNr(parser.getAttributeValue(i));
									break;
								case "type":
									builder.setType(parser.getAttributeValue(i));
									break;
								case "x":
									builder.setX(Integer.parseInt(parser.getAttributeValue(i)));
									break;
								case "y":
									builder.setY(Integer.parseInt(parser.getAttributeValue(i)));
									break;
							}
						}
						stations.add(builder.build());
					}
					break;
				default:
					break;
			}
			parser.next();
		}
		
		return stations;
	}
	
	protected static InputStream getAnswer(String url, String xmlData,
			String charset) throws IOException
	{
		URLConnection urlConnection = new URL(url).openConnection();
		urlConnection.setUseCaches(false);
		urlConnection.setDoOutput(true); // Triggers POST.
		urlConnection.setRequestProperty("accept-charset", charset);
		urlConnection.setRequestProperty("content-type",
				"application/xml; charset=" + charset);
		
		OutputStreamWriter writer = null;
		try
		{
			writer = new OutputStreamWriter(urlConnection.getOutputStream(),
					charset);
			writer.write(xmlData);
		}
		finally
		{
			if(writer != null)
			{
				try
				{
					writer.close();
				}
				catch(IOException e)
				{}
			}
		}
		
		return urlConnection.getInputStream();
	}
}
