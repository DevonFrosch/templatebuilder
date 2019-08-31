package de.stsFanGruppe.templatebuilder.strecken;

import java.util.*;
import de.stsFanGruppe.tools.*;

/**
 * Eine Sammlung von Strecken ohne Abzweige.
 * Ein Streckenabschnitt darf an den Zwischenpunkten an keine anderen Strecken grenzen. Ist dies der Fall, muss der Streckenabschnitt an der Stelle geteilt
 * werden.
 * Weiterhin dürfen keine Sprünge enthalten sein. Sind Sprünge vorhanden, verhalten sich einige Funktionen undefiniert.
 */
public class Streckenabschnitt implements Cloneable, XMLExportable
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
		return strecken.stream().min((a, b) -> Double.compare(a.getMinKm(), b.getMinKm())).get().getMinKm();
	}
	
	public double getMaxKm()
	{
		return strecken.stream().max((a, b) -> Double.compare(a.getMaxKm(), b.getMaxKm())).get().getMaxKm();
	}
	
	public FirstLastList<Betriebsstelle> getBetriebsstellen()
	{
		FirstLastList<Betriebsstelle> bs = new FirstLastLinkedList<>();
		
		if(strecken.isEmpty())
		{
			// Keine Strecken -> Leere Liste zurückgeben
			return bs;
		}
		
		bs.add(strecken.first().anfang);
		for(Strecke s : strecken)
		{
			bs.add(s.ende);
		}
		
		return bs;
	}
	
	public Object clone()
	{
		return new Streckenabschnitt(name, new FirstLastLinkedList<Strecke>(strecken));
	}
	
	// String-Produkte
	public String toString()
	{
		return "Streckenabschnitt " + getName() + " { " + strecken.size() + " Strecken }";
	}
	
	public String toXML(String indent)
	{
		StringJoiner xml = new StringJoiner("\n");
		xml.add(indent + "<streckenabschnitt name=\"" + getName() + "\">");
		
		if(!strecken.isEmpty())
		{
			for(Strecke s : strecken)
			{
				xml.add(s.toXML(indent + "  "));
			}
		}
		
		xml.add(indent + "</streckenabschnitt>");
		return xml.toString();
	}
}
