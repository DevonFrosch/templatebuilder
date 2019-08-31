package de.stsFanGruppe.templatebuilder.strecken;

import java.util.*;
import de.stsFanGruppe.tools.*;

public class Bildfahrplanstrecke implements XMLExportable
{
	protected String name;
	protected FirstLastList<Streckenabschnitt> streckenabschnitte = new FirstLastLinkedList<>();
	
	public Bildfahrplanstrecke(String name, Collection<Streckenabschnitt> streckenabschnitte)
	{
		this(name);
		this.setStreckenabschnitte(streckenabschnitte);
	}
	
	public Bildfahrplanstrecke(String name)
	{
		this.setName(name);
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
	
	protected void setStreckenabschnitte(Collection<? extends Streckenabschnitt> streckenabschnitte)
	{
		NullTester.test(streckenabschnitte);
		streckenabschnitte.forEach((Streckenabschnitt s) -> this.addStreckenabschnitt(s));
	}
	
	public boolean removeStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		NullTester.test(streckenabschnitt);
		return this.streckenabschnitte.remove(streckenabschnitt);
	}
	
	public String toString()
	{
		return "Bildfahrplanstrecke " + getName() + " { " + streckenabschnitte.size() + " Gleise }";
	}
	
	public String toXML(String indent)
	{
		StringJoiner xml = new StringJoiner("\n");
		xml.add(indent + "<bildfahrplanstrecke name=\"" + getName() + "\">");
		
		if(!streckenabschnitte.isEmpty())
		{
			for(Streckenabschnitt sa : streckenabschnitte)
			{
				xml.add(sa.toXML(indent + "  "));
			}
		}
		
		xml.add(indent + "</bildfahrplanstrecke>");
		return xml.toString();
	}
}
