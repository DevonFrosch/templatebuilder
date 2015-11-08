package de.stsFanGruppe.templatebuilder.strecken;

import java.util.NavigableSet;
import java.util.TreeSet;

public class Gleis
{
	protected String name;
	protected NavigableSet<Gleisabschnitt> gleisabschnitte;
	
	public Gleis(String name, NavigableSet<Gleisabschnitt> gleisabschnitte)
	{
		this.name = name;
		this.gleisabschnitte = gleisabschnitte;
	}
	public Gleis(String name)
	{
		this(name, new TreeSet<>());
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public boolean hasGleisabschnitte()
	{
		return !gleisabschnitte.isEmpty();
	}
	public NavigableSet<Gleisabschnitt> getGleisabschnitte()
	{
		return gleisabschnitte;
	}
	public void addGleisabschnitt(Gleisabschnitt gleisabschnitt)
	{
		this.gleisabschnitte.add(gleisabschnitt);
	}
	public void removeGleisabschnitt(Gleisabschnitt gleisabschnitt)
	{
		this.gleisabschnitte.remove(gleisabschnitt);
	}
	
	public String toString()
	{
		return "Gleis "+getName()+" { "+gleisabschnitte.size()+" Gleisabschnitte }";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringBuilder str = new StringBuilder(indent+"<gleis name=\""+getName()+"\">");
		
		if(!gleisabschnitte.isEmpty())
		{
			for(Gleisabschnitt ga: gleisabschnitte)
			{
				str.append("\n");
				str.append(ga.toXML(indent+"  "));
			}
			str.append("\n"+indent);
		}
		
		str.append("</gleis>");
		return str.toString();
	}
}
