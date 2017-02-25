package de.stsFanGruppe.templatebuilder.external.xmlhelper;

import java.io.InputStream;
import java.util.*;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import de.stsFanGruppe.tools.NullTester;

public class XMLReader
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(XMLReader.class);
	
	protected XMLEventReader parser;
	
	public XMLReader(InputStream input) throws XMLStreamException
	{
		NullTester.test(input);
		XMLInputFactory factory = XMLInputFactory.newInstance();
		this.parser = factory.createXMLEventReader(input);
	}
	
	public XMLElement findTag(String... searchNames) throws XMLStreamException
	{
		return findTagUntil(null, searchNames);
	}
	
	/**
	 * Sucht nach Tags, bis es auf das Dateiende oder untilName stößt
	 * 
	 * @param untilName Tag-Name, bei dem die Suche abgebrochen wird (als öffnender oder schließender Tag).
	 * @param searchNames Beliebig viele Namen von Tags, die gefunden werden sollen. Wird kein Name angegeben, ist jeder Tag-Name außer untilName ein Treffer.
	 * @return Der gefundene Tag mit einem Namen aus searchNames oder null, falls untilName oder das Ende des XML erreicht wurden.
	 * @throws XMLStreamException Bei Fehlern des XML-Lesers
	 */
	public XMLElement findTagUntil(String untilName, String... searchNames) throws XMLStreamException
	{
		NullTester.test(parser);
		assert searchNames != null;
		List<String> tagNames = Arrays.asList(searchNames);
		if(tagNames.contains(null))
		{
			throw new NullPointerException();
		}
		
		// alles auf lower case
		if(untilName != null)
		{
			untilName = untilName.toLowerCase();
		}
		for(int i = 0; i < tagNames.size(); i++)
		{
			tagNames.set(i, tagNames.get(i).toLowerCase());
		}
		
		XMLElement element = null;
		String elementName;
		
		while(parser.hasNext())
		{
			XMLEvent event = parser.nextEvent();
			switch(event.getEventType())
			{
				case XMLStreamConstants.START_ELEMENT:
					element = new XMLElement(event.asStartElement());
					elementName = element.getName().toLowerCase();
					
					if(untilName != null && elementName == untilName)
					{
						return null;
					}
					
					if(tagNames.isEmpty() || tagNames.contains(elementName))
					{
						return element;
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					elementName = event.asEndElement().getName().getLocalPart().toLowerCase();
					if(untilName != null && elementName == untilName)
					{
						return null;
					}
					break;
				case XMLStreamConstants.END_DOCUMENT:
					// nichts tun, beim nächsten parser.hasNext() kommt eh false raus
					assert !parser.hasNext();
					break;
			}
		}
		
		parser.close();
		log.trace("End of Document");
		return null;
	}
}
