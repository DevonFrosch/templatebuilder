package de.stsFanGruppe.templatebuilder.strecken;

import java.util.NavigableSet;
import java.util.TreeSet;

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
	public Gleis(String name)
	{
		this.name = name;
		this.resetGleisabschnitte();
	}
	/**
	 * Erstellt ein Gleis mit den gegebenen Gleisabschnitten.
	 * @param name der Name des Gleises.
	 * @param gleisabschnitte die Gleisabschnitte, die das Gleis umfasst.
	 * @thows NullPointerException falls die Liste der Gleisabschnitte null ist.
	 */
	protected Gleis(String name, NavigableSet<Gleisabschnitt> gleisabschnitte)
	{
		this(name);
		gleisabschnitte.forEach((Gleisabschnitt g) -> this.addGleisabschnitt(g));
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
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
		if(!this.verwendetGleisabschnitte)
		{
			this.resetGleisabschnitte();
		}
		this.gleisabschnitte.add(gleisabschnitt);
		gleisabschnitt.setParent(this);
		this.verwendetGleisabschnitte = true;
	}
	public void removeGleisabschnitt(Gleisabschnitt gleisabschnitt)
	{
		if(!this.verwendetGleisabschnitte)
		{
			// keine offiziellen Gleisabschnitte
			throw new NullPointerException();
		}
		
		this.gleisabschnitte.remove(gleisabschnitt);
		if(gleisabschnitt.getParent() == this)
			gleisabschnitt.setParent(null);
		else
			System.out.println("Gleisabschnitt "+gleisabschnitt.getName()+" aus Gleis "+getName()+" löschen: bin nicht parent!");
		
		if(this.gleisabschnitte.isEmpty())
		{
			this.resetGleisabschnitte();
		}
	}
	public void resetGleisabschnitte()
	{
		this.gleisabschnitte = new TreeSet<>();
		this.gleisabschnitte.add(new Gleisabschnitt(name, this));
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
