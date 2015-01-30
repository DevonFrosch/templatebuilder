package de.stsFanGruppe.templatebuilder.strecken;

import java.util.List;
import java.util.LinkedList;

public class Kursbuchstrecke
{	
	protected String name;
	protected String nummer;
	protected List<Streckenabschnitt> streckenabschnitte;
	
	public Kursbuchstrecke(String name, String nummer, List<Streckenabschnitt> streckenabschnitte)
	{
		this.name = name;
		this.nummer = nummer;
		this.streckenabschnitte = streckenabschnitte;
	}
	public Kursbuchstrecke(String name, String nummer)
	{
		this(name, nummer, new LinkedList<>());
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getNummer()
	{
		return nummer;
	}
	public void setNummer(String nummer)
	{
		this.nummer = nummer;
	}
	public boolean hasStreckenabschnitte()
	{
		return !streckenabschnitte.isEmpty();
	}
	public List<Streckenabschnitt> getStreckenabschnitte()
	{
		return streckenabschnitte;
	}
	public void addStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		this.streckenabschnitte.add(streckenabschnitt);
	}
	public void addStreckenabschnitt(int index, Streckenabschnitt streckenabschnitt)
	{
		this.streckenabschnitte.add(index, streckenabschnitt);
	}
	public void removeStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		this.streckenabschnitte.remove(streckenabschnitt);
	}
	
	public String toString()
	{
		return "Kursbuchstrecke "+getName()+" ("+getNummer()+") { "+streckenabschnitte.size()+" Gleise }";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringBuilder str = new StringBuilder(indent+"<kursbuchstrecke name=\""+getName()+"\" nummer=\""+getNummer()+"\">");
		
		if(!streckenabschnitte.isEmpty())
		{
			for(Streckenabschnitt sa: streckenabschnitte)
			{
				str.append("\n");
				str.append(sa.toXML(indent+"  "));
			}
			str.append("\n"+indent);
		}
		
		str.append("</kursbuchstrecke>");
		return str.toString();
	}
}
