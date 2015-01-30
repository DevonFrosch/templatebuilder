package de.stsFanGruppe.templatebuilder.strecken;

import java.util.LinkedList;
import java.util.List;

public class Streckenabschnitt
{	
	protected String name;
	protected List<Strecke> strecken;
	
	// Konstruktoren
	public Streckenabschnitt(String name, List<Strecke> strecken)
	{
		this.name = name;
		this.strecken = strecken;
	}
	public Streckenabschnitt(String name)
	{
		this(name, new LinkedList<>());
	}
	
	// primitive Getter/Setter
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public boolean hasStrecken()
	{
		return !strecken.isEmpty();
	}
	public void addStrecke(Strecke strecke)
	{
		this.strecken.add(strecke);
	}
	public void addStrecke(int index, Strecke strecke)
	{
		this.strecken.add(index, strecke);
	}
	public void removeStrecke(Strecke strecke)
	{
		this.strecken.remove(strecke);
	}
	
	// String-Produkte
	public String toString()
	{
		return "Streckenabschnitt "+getName()+" { "+strecken.size()+" Strecken }";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringBuilder str = new StringBuilder(indent+"<streckenabschnitt name=\""+getName()+"\">");
		
		if(!strecken.isEmpty())
		{
			for(Strecke s: strecken)
			{
				str.append("\n");
				str.append(s.toXML(indent+"  "));
			}
			str.append("\n"+indent);
		}
		
		str.append("</streckenabschnitt>");
		return str.toString();
	}
}
