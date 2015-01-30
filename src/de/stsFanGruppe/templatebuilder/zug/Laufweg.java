package de.stsFanGruppe.templatebuilder.zug;

import java.util.List;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;

public class Laufweg
{
	protected String name;
	protected List<Streckenabschnitt> streckenabschnitte;
	
	public Laufweg(String name, List<Streckenabschnitt> streckenabschnitte)
	{
		super();
		this.name = name;
		this.streckenabschnitte = streckenabschnitte;
	}

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
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
		return "Laufweg "+getName()+" { "+streckenabschnitte.size()+" Streckenabschnitte }";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringBuilder str = new StringBuilder(indent+"<laufweg name=\""+getName()+"\">");
		
		if(!streckenabschnitte.isEmpty())
		{
			for(Streckenabschnitt sa: streckenabschnitte)
			{
				str.append("\n");
				str.append(sa.toXML(indent+"  "));
			}
			str.append("\n"+indent);
		}
		
		str.append("</laufweg>");
		return str.toString();
	}
}
