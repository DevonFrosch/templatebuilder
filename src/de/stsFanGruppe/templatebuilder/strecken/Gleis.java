package de.stsFanGruppe.templatebuilder.strecken;

import java.util.NavigableSet;
import java.util.TreeSet;

public class Gleis
{
	protected String name;
	protected NavigableSet<Gleisabschnitt> gleisabschnitte;
	protected boolean verwendetGleisabschnitte;
	
	/**
	 * Erstellt ein Gleis mit den gegebenen Gleisabschnitten.
	 * @param name der Name des Gleises.
	 * @param gleisabschnitte die Gleisabschnitte, die das Gleis umfasst.
	 * @thows NullPointerException falls die Liste der Gleisabschnitte null ist.
	 */
	public Gleis(String name, NavigableSet<Gleisabschnitt> gleisabschnitte)
	{
		this(name);
		this.addGleisabschnitte(gleisabschnitte);
	}
	/**
	 * Erstellt ein Gleis ohne Gleisabschnitte.
	 * @param name der Name des Gleises.
	 */
	public Gleis(String name)
	{
		this.name = name;
		this.resetGleisabschnitte();
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
	}
	/**
	 * 
	 * @param gleisabschnitte
	 * @thows NullPointerException falls die Liste der Gleisabschnitte null ist.
	 */
	// protected da hier ungülitge Werte kommen können
	protected void addGleisabschnitte(NavigableSet<Gleisabschnitt> gleisabschnitte)
	{
		if(gleisabschnitte == null)
		{
			throw new NullPointerException();
		}
		this.gleisabschnitte = gleisabschnitte;
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
		
		if(this.gleisabschnitte.isEmpty())
		{
			this.resetGleisabschnitte();
		}
	}
	public void resetGleisabschnitte()
	{
		this.gleisabschnitte = initGleisabschnitte(name);
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
	
	// Gleisabschnittliste mit einem Gleisabschnitt
	private static NavigableSet<Gleisabschnitt> initGleisabschnitte(String name)
	{
		TreeSet<Gleisabschnitt> ga = new TreeSet<>();
		ga.add(new Gleisabschnitt(name));
		return ga;
	}
}
