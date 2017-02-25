package de.stsFanGruppe.templatebuilder.zug;

import java.util.Collection;
import java.util.StringJoiner;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;
import de.stsFanGruppe.tools.NullTester;

public class Laufweg
{
	protected String name;
	protected FirstLastList<Streckenabschnitt> streckenabschnitte = new FirstLastLinkedList<>();
	
	public Laufweg(String name)
	{
		this.setName(name);
	}
	
	public Laufweg(String name, Collection<? extends Streckenabschnitt> streckenabschnitte)
	{
		this(name);
		this.addStreckenabschnitte(streckenabschnitte);
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		NullTester.test(name);
		this.name = name;
	}
	
	public boolean hasStreckenabschnitte()
	{
		return !streckenabschnitte.isEmpty();
	}
	
	public FirstLastList<Streckenabschnitt> getStreckenabschnitte()
	{
		return streckenabschnitte;
	}
	
	public void addStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		NullTester.test(streckenabschnitt);
		this.streckenabschnitte.add(streckenabschnitt);
	}
	
	public void addStreckenabschnitt(int index, Streckenabschnitt streckenabschnitt)
	{
		NullTester.test(streckenabschnitt);
		this.streckenabschnitte.add(index, streckenabschnitt);
	}
	
	public void addStreckenabschnitte(Collection<? extends Streckenabschnitt> streckenabschnitte)
	{
		NullTester.test(streckenabschnitte);
		streckenabschnitte.forEach((Streckenabschnitt s) -> this.addStreckenabschnitt(s));
	}
	
	public boolean removeStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		return this.streckenabschnitte.remove(streckenabschnitt);
	}
	
	public String toString()
	{
		return "Laufweg " + getName() + " { von " + streckenabschnitte.first().getName() + " nach " + streckenabschnitte.last().getName() + ", "
				+ streckenabschnitte.size() + " Streckenabschnitte }";
	}
	
	public String toXML()
	{
		return toXML("");
	}
	
	public String toXML(String indent)
	{
		StringJoiner xml = new StringJoiner("\n");
		xml.add(indent + "<laufweg name=\"" + getName() + "\">");
		
		if(!streckenabschnitte.isEmpty())
		{
			for(Streckenabschnitt sa : streckenabschnitte)
			{
				xml.add(sa.toXML(indent + "  "));
			}
		}
		
		xml.add(indent + "</laufweg>");
		return xml.toString();
	}
}
