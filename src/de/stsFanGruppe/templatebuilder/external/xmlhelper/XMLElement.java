package de.stsFanGruppe.templatebuilder.external.xmlhelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

public class XMLElement
{
	protected StartElement elem;
	protected Map<String, String> attrs;
	
	protected XMLElement(StartElement elem)
	{
		this.elem = elem;
		this.attrs = new HashMap<>();
		for(Iterator<?> it = elem.getAttributes(); it.hasNext(); )
		{
			Attribute attr = (Attribute) it.next();
			if(attr.isSpecified())
			{
				attrs.put(attr.getName().getLocalPart(), attr.getValue());
			}
		}
	}
	public String getName()
	{
		return elem.getName().getLocalPart();
	}
	public String getAttribute(String name)
	{
		return attrs.get(name);
	}
	public Map<String, String> getAttributes()
	{
		return attrs;
	}
}