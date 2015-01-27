package de.stsFanGruppe.strecken;

import java.util.LinkedList;
import java.util.List;

public class Streckenabschnitt
{	
	protected String name;
	protected List<Strecke> strecken;
	
	public Streckenabschnitt(String name, List<Strecke> strecken)
	{
		this.name = name;
		this.strecken = strecken;
	}
	public Streckenabschnitt(String name)
	{
		this(name, new LinkedList<>());
	}
	
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public boolean hasStrecken()
	{
		return !strecken.isEmpty();
	}
	public void addStrecke(Strecke strecke)
	{
		this.strecken.add(strecke);
	}
	public void addStrecke(int index, Strecke strecke)
	{
		this.strecken.add(index, strecke);
	}
	public void removeStrecke(Strecke strecke)
	{
		this.strecken.remove(strecke);
	}
}
