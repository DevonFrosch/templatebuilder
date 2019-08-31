package de.stsFanGruppe.templatebuilder.strecken;

import java.util.*;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.XMLExportable;

public class Gleis implements XMLExportable
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Gleis.class);
	
	protected String name;
	protected NavigableSet<Gleisabschnitt> gleisabschnitte;
	protected boolean verwendetGleisabschnitte;
	protected Betriebsstelle parent;
	
	/**
	 * Erstellt ein Gleis ohne Gleisabschnitte.
	 * 
	 * @param name der Name des Gleises.
	 */
	public Gleis(String name, double km)
	{
		this.setName(name);
		this.resetGleisabschnitte(km);
	}
	
	/**
	 * Erstellt ein Gleis mit den gegebenen Gleisabschnitten.
	 * 
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
			// Aufräumen
			Gleisabschnitt alt = this.gleisabschnitte.first();
			
			if(alt.getParent() != this)
			{
				log.error("Dummy-Gleisabschnitt {} aus Gleis {} löschen: bin nicht parent!", alt.getName(), getName());
				throw new IllegalStateException("Interner Gleisabschnitt asynchron!");
			}
			
			alt.setParent(null);
			this.gleisabschnitte.clear();
		}
		
		// Parent prüfen und setzten
		if(gleisabschnitt.getParent() != null)
		{
			log.error("addGleisabschnitt: Gleisabschnitt hat schon parent: " + gleisabschnitt.getParent().getName());
			throw new IllegalStateException("Gleisabschnitt wird schon verwendet!");
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
		if(!this.verwendetGleisabschnitte || !gleisabschnitte.contains(gleisabschnitt))
		{
			return false;
		}
		
		if(gleisabschnitt.getParent() == this)
		{
			gleisabschnitt.setParent(null);
		}
		else
		{
			log.error("Gleisabschnitt {} aus Gleis {} löschen: bin nicht parent!", gleisabschnitt.getName(), getName());
			throw new IllegalStateException("Gleisabschnitt asynchron!");
		}
		boolean erfolg = this.gleisabschnitte.remove(gleisabschnitt);
		
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
	
	void setParent(Betriebsstelle parent)
	{
		this.parent = parent;
	}
	
	public double getMinKm()
	{
		return gleisabschnitte.stream().min((a, b) -> Double.compare(a.getKm(), b.getKm())).get().getKm();
	}
	
	public double getMaxKm()
	{
		return gleisabschnitte.stream().min((a, b) -> Double.compare(a.getKm(), b.getKm())).get().getKm();
	}
	
	public String toString()
	{
		if(verwendetGleisabschnitte)
		{
			return "Gleis " + getName();
		}
		else
		{
			return "Gleis " + getName() + " { " + gleisabschnitte.size() + " Gleisabschnitte }";
		}
	}
	
	public String toXML(String indent)
	{
		StringJoiner xml = new StringJoiner("\n");
		xml.add(indent + "<gleis name=\"" + getName() + "\">");
		
		if(!gleisabschnitte.isEmpty())
		{
			for(Gleisabschnitt ga : gleisabschnitte)
			{
				xml.add(ga.toXML(indent + "  "));
			}
		}
		
		xml.add(indent + "</gleis>");
		return xml.toString();
	}
}
