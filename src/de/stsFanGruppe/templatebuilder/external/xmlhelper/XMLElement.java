package de.stsFanGruppe.templatebuilder.external.xmlhelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import de.stsFanGruppe.tools.NullTester;

public class XMLElement
{
	protected StartElement elem;
	protected Map<String, String> attrs = new HashMap<>();
	
	protected XMLElement(StartElement elem)
	{
		NullTester.test(elem);
		this.elem = elem;
		
		for(Iterator<?> it = elem.getAttributes(); it.hasNext();)
		{
			Attribute attr = (Attribute) it.next();
			if(attr.isSpecified())
			{
				attrs.put(attr.getName().getLocalPart().toLowerCase(), attr.getValue());
			}
		}
	}
	
	public String getName()
	{
		return elem.getName().getLocalPart();
	}
	
	public String getAttribute(String name)
	{
		NullTester.test(name);
		return attrs.get(name.toLowerCase());
	}
	
	public Map<String, String> getAttributes()
	{
		return attrs;
	}
	
	public String toString()
	{
		StringJoiner str = new StringJoiner(", ");
		attrs.forEach((String key, String value) -> str.add(key + "='" + value + "'"));
		return "XMLElement " + getName() + " {" + str.toString() + "}";
	}
}
