package de.stsFanGruppe.templatebuilder.strecken;

import java.util.Collection;
import java.util.StringJoiner;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;
import de.stsFanGruppe.tools.NullTester;

/**
 * Eine Sammlung von Strecken ohne Abzweige.
 * 
 * Ein Streckenabschnitt darf an den Zwischenpunkten an keine anderen Strecken grenzen. Ist dies der Fall, muss der Streckenabschnitt an der Stelle geteilt werden.
 * 
 * Weiterhin d�rfen keine Spr�nge enthalten sein. Sind Spr�nge vorhanden, verhalten sich einige Funktionen undefiniert.
 */
public class Streckenabschnitt
{	
	protected String name;
	protected FirstLastList<Strecke> strecken = new FirstLastLinkedList<>();
	
	// Konstruktoren
	public Streckenabschnitt(String name, Collection<? extends Strecke> strecken)
	{
		this(name);
		this.addStrecken(strecken);
	}
	public Streckenabschnitt(String name)
	{
		this.setName(name);
	}
	
	// primitive Getter/Setter
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		NullTester.test(name);
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
	public void addStrecke(Strecke strecke)
	{
		NullTester.test(strecke);
		this.strecken.add(strecke);
	}
	public void addStrecke(int index, Strecke strecke)
	{
		NullTester.test(strecke);
		this.strecken.add(index, strecke);
	}
	protected void addStrecken(Collection<? extends Strecke> strecken)
	{
		NullTester.test(strecken);
		strecken.forEach((Strecke s) -> this.addStrecke(s));
	}
	public boolean removeStrecke(Strecke strecke)
	{
		NullTester.test(strecke);
		return this.strecken.remove(strecke);
	}
	
	public double getMinKm()
	{
		return strecken.stream().min((a,b) -> Double.compare(a.getMinKm(), b.getMinKm())).get().getMinKm();
	}
	public double getMaxKm()
	{
		return strecken.stream().max((a,b) -> Double.compare(a.getMaxKm(), b.getMaxKm())).get().getMaxKm();
	}
	public FirstLastList<Betriebsstelle> getBetriebsstellen()
	{
		FirstLastList<Betriebsstelle> bs = new FirstLastLinkedList<>();
		
		if(strecken.isEmpty())
		{
			return bs;
		}
		
		bs.add(strecken.first().anfang);
		for(Strecke s: strecken)
		{
			bs.add(s.ende);
		}
		
		return bs;
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
		StringJoiner xml = new StringJoiner("\n");
		xml.add(indent+"<streckenabschnitt name=\""+getName()+"\">");
		
		if(!strecken.isEmpty())
		{
			for(Strecke s: strecken)
			{
				xml.add(s.toXML(indent+"  "));
			}
		}
		
		xml.add(indent+"</streckenabschnitt>");
		return xml.toString();
	}
}
