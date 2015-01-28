package de.stsFanGruppe.templatebuilder.external.xmlhelper;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

public class XMLReader
{
	private XMLEventReader parser;
	private XMLEvent lastEvent;
	private Stack<QName> nesting;
	
	public XMLReader(InputStream input) throws XMLStreamException
	{
		XMLInputFactory factory = XMLInputFactory.newInstance();
		this.parser = factory.createXMLEventReader(input);
		this.lastEvent = null;
		this.nesting = new Stack<>();
	}
	
	public StartElement findTag(String... names) throws XMLStreamException, EndOfXMLException
	{
		List<String> tagNames = Arrays.asList(names);
		
		while(parser.hasNext())
		{
			XMLEvent event = parser.nextEvent();
			switch(event.getEventType())
			{
				case XMLStreamConstants.END_DOCUMENT:
					parser.close();
					throw new EndOfXMLException("Reached end of document");
				case XMLStreamConstants.START_ELEMENT:
					StartElement element = event.asStartElement();
					nesting.push(element.getName());
					
					if(tagNames.isEmpty() || tagNames.contains(element.getName().getLocalPart()))
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
					nesting.pop();
					break;
				default:
					break;
			}
		}
		parser.close();
		throw new EndOfXMLException("Nothing left to parse");
	}
	
	public Map<String, Attribute> getAttributes()
	{
		return this.getAttributes(lastEvent.asStartElement());
	}
	public Map<String, Attribute> getAttributes(StartElement elem)
	{
		Map<String, Attribute> attrs = new HashMap<>();

		for(Iterator<?> it = elem.getAttributes(); it.hasNext(); )
		{
			Attribute attr = (Attribute) it.next();
			attrs.put(attr.getName().getLocalPart(), attr);
		}
		
		return attrs;
	}
	
	public Stack<QName> getNesting()
	{
		return nesting;
	}
}
