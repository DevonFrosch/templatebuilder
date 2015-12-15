package de.stsFanGruppe.templatebuilder.zug;

import java.util.Collection;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.StringJoiner;
import java.util.TreeSet;
import de.stsFanGruppe.tools.NullTester;

public class Fahrt
{
	protected String name;
	protected Linie linie;
	protected NavigableSet<Fahrplanhalt> fahrplanhalte = new TreeSet<>(new Fahrplanhalt.StrictComparator());
	
	public Fahrt(String name, Linie linie)
	{
		this.setName(name);
		this.setLinie(linie);
	}
	public Fahrt(String name, Linie linie, Collection<? extends Fahrplanhalt> fahrplanhalte)
	{
		this(name, linie);
		this.addFahrplanhalte(fahrplanhalte);
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
	public Linie getLinie()
	{
		return linie;
	}
	public void setLinie(Linie linie)
	{
		NullTester.test(linie);
		this.linie = linie;
	}
	public boolean hasFahrplanhalte()
	{
		return !fahrplanhalte.isEmpty();
	}
	public NavigableSet<Fahrplanhalt> getFahrplanhalte()
	{
		return fahrplanhalte;
	}
	public void addFahrplanhalt(Fahrplanhalt halt)
	{
		NullTester.test(halt);
		this.fahrplanhalte.add(halt);
		halt.setParent(this);
	}
	protected void addFahrplanhalte(Collection<? extends Fahrplanhalt> fahrplanhalte)
	{
		NullTester.test(fahrplanhalte);
		fahrplanhalte.forEach((Fahrplanhalt f) -> this.addFahrplanhalt(f));
	}
	public boolean removeFahrplanhalt(Fahrplanhalt halt)
	{
		NullTester.test(halt);
		boolean erfolg = this.fahrplanhalte.remove(halt);
		if(halt.getParent() == this)
		{
			halt.setParent(null);
		}
		else
		{
			System.out.println("Fahrplanhalt aus Fahrt "+getName()+" löschen: bin nicht parent!");
		}
		return erfolg;
	}
	
	public double getMinZeit()
	{
		if(fahrplanhalte.isEmpty())
		{
			throw new NoSuchElementException("Keine Fahrplanhalte.");
		}
		return fahrplanhalte.stream().min((a, b) -> Double.compare(a.getAnkunft(), b.getAnkunft())).get().getAnkunft();
	}
	public double getMaxZeit()
	{
		if(fahrplanhalte.isEmpty())
		{
			throw new NoSuchElementException("Keine Fahrplanhalte.");
		}
		return fahrplanhalte.stream().max((a, b) -> Double.compare(a.getAbfahrt(), b.getAbfahrt())).get().getAbfahrt();
	}
	
	public String toString()
	{
		return "Fahrt "+getName()+" { Linie: "+getLinie()+", "+fahrplanhalte.size()+" Fahrplanhalte }";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringJoiner xml = new StringJoiner("\n");
		xml.add(indent+"<fahrt linie=\""+linie.getName()+"\">");
		
		if(!fahrplanhalte.isEmpty())
		{
			xml.add(indent+"  <fahrplanhalte>");
			
			for(Fahrplanhalt fh: fahrplanhalte)
			{
				xml.add(fh.toXML(indent+"  "+"  "));
			}
			
			xml.add(indent+"  </fahrplanhalte>");
		}
		else
		{
			xml.add(indent+"  <fahrplanhalte />");
		}
		
		xml.add(indent+"</fahrt>");
		return xml.toString();
	}
}
