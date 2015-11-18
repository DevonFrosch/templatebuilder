package de.stsFanGruppe.templatebuilder.strecken;

import java.util.Collection;
import java.util.NavigableSet;
import java.util.TreeSet;
import de.stsFanGruppe.tools.NullTester;

public class Gleis
{
	protected String name;
	protected NavigableSet<Gleisabschnitt> gleisabschnitte;
	protected boolean verwendetGleisabschnitte;
	protected Betriebsstelle parent;

	/**
	 * Erstellt ein Gleis ohne Gleisabschnitte.
	 * @param name der Name des Gleises.
	 */
	public Gleis(String name, double km)
	{
		this.setName(name);
		this.resetGleisabschnitte(km);
	}
	/**
	 * Erstellt ein Gleis mit den gegebenen Gleisabschnitten.
	 * @param name der Name des Gleises.
	 * @param gleisabschnitte die Gleisabschnitte, die das Gleis umfasst.
	 * @thows NullPointerException falls die Liste der Gleisabschnitte null ist.
	 */
	protected Gleis(String name, Collection<? extends Gleisabschnitt> gleisabschnitte)
	{
		this.setName(name);
		this.addGleisabschnitte(gleisabschnitte);
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
	public boolean hasGleisabschnitte()
	{
		return verwendetGleisabschnitte;
	}
	public NavigableSet<Gleisabschnitt> getGleisabschnitte()
	{
		return gleisabschnitte;
	}
	public void addGleisabschnitt(Gleisabschnitt gleisabschnitt)
	{
		NullTester.test(gleisabschnitt);
		if(!this.verwendetGleisabschnitte)
		{
			Gleisabschnitt alt = this.gleisabschnitte.first();
			alt.setParent(null);
			this.gleisabschnitte.clear();
		}
		this.gleisabschnitte.add(gleisabschnitt);
		gleisabschnitt.setParent(this);
		this.verwendetGleisabschnitte = true;
	}
	protected void addGleisabschnitte(Collection<? extends Gleisabschnitt> gleisabschnitte)
	{
		NullTester.test(gleisabschnitte);
		gleisabschnitte.forEach((Gleisabschnitt g) -> this.addGleisabschnitt(g));
	}
	public boolean removeGleisabschnitt(Gleisabschnitt gleisabschnitt)
	{
		NullTester.test(gleisabschnitt);
		if(!this.verwendetGleisabschnitte)
		{
			return false;
		}
		
		boolean erfolg = this.gleisabschnitte.remove(gleisabschnitt);
		if(gleisabschnitt.getParent() == this)
			gleisabschnitt.setParent(null);
		else
			System.out.println("Gleisabschnitt "+gleisabschnitt.getName()+" aus Gleis "+getName()+" löschen: bin nicht parent!");
		
		if(this.gleisabschnitte.isEmpty())
		{
			this.resetGleisabschnitte(gleisabschnitt.getKm());
		}
		return erfolg;
	}
	public void resetGleisabschnitte(double km)
	{
		this.gleisabschnitte = new TreeSet<>();
		this.gleisabschnitte.add(new Gleisabschnitt(name, this, km));
		this.verwendetGleisabschnitte = false;
	}
	public Betriebsstelle getParent()
	{
		return parent;
	}
	protected void setParent(Betriebsstelle parent)
	{
		this.parent = parent;
	}
	
	public double getMinKm()
	{
		return gleisabschnitte.stream().min((a,b) -> Double.compare(a.getKm(), b.getKm())).get().getKm();
	}
	public double getMaxKm()
	{
		return gleisabschnitte.stream().min((a,b) -> Double.compare(a.getKm(), b.getKm())).get().getKm();
	}
	
	public String toString()
	{
		if(verwendetGleisabschnitte)
		{
			return "Gleis "+getName();
		}
		else
		{
			return "Gleis "+getName()+" { "+gleisabschnitte.size()+" Gleisabschnitte }";
		}
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
