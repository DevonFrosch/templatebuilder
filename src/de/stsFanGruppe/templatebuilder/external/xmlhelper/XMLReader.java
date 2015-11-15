package de.stsFanGruppe.templatebuilder.external.xmlhelper;

import java.io.InputStream;
import java.util.*;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import de.stsFanGruppe.tools.NullTester;

public class XMLReader
{
	private XMLEventReader parser;
	private Stack<XMLElement> nesting = new Stack<>();
	
	public XMLReader(InputStream input) throws XMLStreamException
	{
		NullTester.test(input);
		XMLInputFactory factory = XMLInputFactory.newInstance();
		this.parser = factory.createXMLEventReader(input);
	}
	
	public XMLElement findTag(String... searchNames) throws XMLStreamException, EndOfXMLException
	{
		return findTagUntil(null, searchNames);
	}
	public XMLElement findTagUntil(String untilName, String... searchNames) throws XMLStreamException, EndOfXMLException
	{
		assert parser != null;
		
		List<String> tagNames = Arrays.asList(searchNames);
		if(tagNames.contains(null))
		{
			throw new NullPointerException();
		}
		XMLElement element = null;
		
		while(parser.hasNext())
		{
			XMLEvent event = parser.nextEvent();
			switch(event.getEventType())
			{
				case XMLStreamConstants.END_DOCUMENT:
					parser.close();
					log("End of Document");
					throw new EndOfXMLException("Reached end of document");
				case XMLStreamConstants.START_ELEMENT:
					element = new XMLElement(event.asStartElement());
					nesting.push(element);
					
					if(tagNames.isEmpty() || tagNames.contains(element.getName()))
					{
						return element;
					}
					else
					{
						continue;
					}
				case XMLStreamConstants.END_ELEMENT:
					if(!nesting.isEmpty())
					{
						throw new EndOfXMLException("Element stack empty");
					}
					
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
		log("End of File");
		throw new EndOfXMLException("Nothing left to parse");
	}
	
	public Stack<XMLElement> getNesting()
	{
		return nesting;
	}

	private void log(String text)
	{
		System.out.println("XMLReader: "+text);
	}
}
