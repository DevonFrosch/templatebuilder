package de.stsFanGruppe.templatebuilder.strecken;

import java.util.List;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;

public class Streckenabschnitt
{	
	protected String name;
	protected FirstLastList<Strecke> strecken;
	
	// Konstruktoren
	public Streckenabschnitt(String name, List<Strecke> strecken)
	{
		this(name);
		this.setStrecken(strecken);
	}
	public Streckenabschnitt(String name)
	{
		this.setName(name);
		this.strecken = new FirstLastLinkedList<>();
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
	public FirstLastList<Strecke> getStrecken()
	{
		return strecken;
	}
	protected void setStrecken(List<Strecke> strecken)
	{
		this.strecken = new FirstLastLinkedList<>(strecken);
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
