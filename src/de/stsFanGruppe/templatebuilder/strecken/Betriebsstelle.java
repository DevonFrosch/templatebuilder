package de.stsFanGruppe.templatebuilder.strecken;

import java.util.List;
import java.util.LinkedList;

public class Betriebsstelle
{	
	protected String name;
	protected List<Gleis> gleise;
	
	public Betriebsstelle(String name, List<Gleis> gleise)
	{
		this.name = name;
		this.gleise = gleise;
	}
	public Betriebsstelle(String name)
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
	public boolean hasGleise()
	{
		return !gleise.isEmpty();
	}
	public List<Gleis> getGleise()
	{
		return gleise;
	}
	public void addGleis(Gleis gleis)
	{
		this.gleise.add(gleis);
	}
	public void addGleis(int index, Gleis gleis)
	{
		this.gleise.add(index, gleis);
	}
	public void removeGleis(Gleis gleis)
	{
		this.gleise.remove(gleis);
	}
}
