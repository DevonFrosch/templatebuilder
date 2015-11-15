package de.stsFanGruppe.templatebuilder.external.xmlhelper;

import java.io.InputStream;
import java.util.*;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

public class XMLReader
{
	private XMLEventReader parser;
	private XMLEvent lastEvent;
	private Stack<XMLElement> nesting;
	
	public XMLReader(InputStream input) throws XMLStreamException
	{
		XMLInputFactory factory = XMLInputFactory.newInstance();
		this.parser = factory.createXMLEventReader(input);
		this.lastEvent = null;
		this.nesting = new Stack<>();
	}
	
	public XMLElement findTag(String... searchNames) throws XMLStreamException, EndOfXMLException
	{
		return findTagUntil(null, searchNames);
	}
	public XMLElement findTagUntil(String untilName, String... searchNames) throws XMLStreamException, EndOfXMLException
	{
		List<String> tagNames = Arrays.asList(searchNames);
		XMLElement element = null;
		
		while(parser.hasNext())
		{
			XMLEvent event = parser.nextEvent();
			switch(event.getEventType())
			{
				case XMLStreamConstants.END_DOCUMENT:
					parser.close();
					throw new EndOfXMLException("Reached end of document");
				case XMLStreamConstants.START_ELEMENT:
					element = new XMLElement(event.asStartElement());
					nesting.push(element);
					
					if(tagNames.isEmpty() || tagNames.contains(element.getName()))
					{
						this.lastEvent = event;
						return element;
					}
					else
					{
						System.out.println("Nicht: "+element.getName());
						continue;
					}
				case XMLStreamConstants.END_ELEMENT:
					element = nesting.pop();
					if(untilName != null && event.asEndElement().getName().getLocalPart() == untilName)
					{
						return null;
					}
					break;
				default:
					break;
			}
		}
		parser.close();
		throw new EndOfXMLException("Nothing left to parse");
	}
	
	public Stack<XMLElement> getNesting()
	{
		return nesting;
	}
}
