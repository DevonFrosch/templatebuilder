package de.stsFanGruppe.templatebuilder.zug;

import java.util.Collection;
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
