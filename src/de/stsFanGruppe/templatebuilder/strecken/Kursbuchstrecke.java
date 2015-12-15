package de.stsFanGruppe.templatebuilder.strecken;

import java.util.Collection;
import java.util.StringJoiner;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;
import de.stsFanGruppe.tools.NullTester;

public class Kursbuchstrecke
{	
	protected String name;
	protected String nummer;
	protected FirstLastList<Streckenabschnitt> streckenabschnitte = new FirstLastLinkedList<>();
	
	public Kursbuchstrecke(String name, String nummer, Collection<Streckenabschnitt> streckenabschnitte)
	{
		this(name, nummer);
		this.setStreckenabschnitte(streckenabschnitte);
	}
	public Kursbuchstrecke(String name, String nummer)
	{
		this.setName(name);
		this.setNummer(nummer);
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
	public String getNummer()
	{
		return nummer;
	}
	public void setNummer(String nummer)
	{
		NullTester.test(nummer);
		this.nummer = nummer;
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
		return "Kursbuchstrecke "+getName()+" ("+getNummer()+") { "+streckenabschnitte.size()+" Gleise }";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringJoiner xml = new StringJoiner("\n");
		xml.add(indent+"<kursbuchstrecke name=\""+getName()+"\" nummer=\""+getNummer()+"\">");
		
		if(!streckenabschnitte.isEmpty())
		{
			for(Streckenabschnitt sa: streckenabschnitte)
			{
				xml.add(sa.toXML(indent+"  "));
			}
		}
		
		xml.add(indent+"</kursbuchstrecke>");
		return xml.toString();
	}
}
