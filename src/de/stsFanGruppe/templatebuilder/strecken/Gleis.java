package de.stsFanGruppe.templatebuilder.strecken;

import java.util.List;
import java.util.LinkedList;

public class Gleis
{
	protected String name;
	protected List<Gleisabschnitt> gleisabschnitte;
	
	public Gleis(String name, List<Gleisabschnitt> gleisabschnitte)
	{
		this.name = name;
		this.gleisabschnitte = gleisabschnitte;
	}
	public Gleis(String name)
	{
		this(name, new LinkedList<>());
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
		return !gleisabschnitte.isEmpty();
	}
	public List<Gleisabschnitt> getGleisabschnitte()
	{
		return gleisabschnitte;
	}
	public void addGleisabschnitt(Gleisabschnitt gleisabschnitt)
	{
		this.gleisabschnitte.add(gleisabschnitt);
	}
	public void addGleisabschnitt(int index, Gleisabschnitt gleisabschnitt)
	{
		this.gleisabschnitte.add(index, gleisabschnitt);
	}
	public void removeGleisabschnitt(Gleisabschnitt gleisabschnitt)
	{
		this.gleisabschnitte.remove(gleisabschnitt);
	}
}
