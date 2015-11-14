package de.stsFanGruppe.templatebuilder.strecken;

import java.util.NavigableSet;
import java.util.TreeSet;

public class Gleis
{
	protected String name;
	protected NavigableSet<Gleisabschnitt> gleisabschnitte;
	protected boolean verwendetGleisabschnitte;

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
		assert gleisabschnitte != null;
		
		gleisabschnitte.iterator().forEachRemaining((Gleisabschnitt g) -> this.addGleisabschnitt(g));
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
		gleisabschnitt.setParent(null);
		
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
