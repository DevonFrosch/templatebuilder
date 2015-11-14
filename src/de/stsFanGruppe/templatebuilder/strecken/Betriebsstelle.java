package de.stsFanGruppe.templatebuilder.strecken;

import java.util.List;
import java.util.LinkedList;

public class Betriebsstelle
{	
	protected String name;
	protected List<Gleis> gleise;
	
	public Betriebsstelle(String name, List<Gleis> gleise)
	{
		this(name);
		gleise.forEach((Gleis g) -> this.addGleis(g));
	}
	public Betriebsstelle(String name)
	{
		this.name = name;
		this.gleise = new LinkedList<>();
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
		gleis.setParent(this);
	}
	public void addGleis(int index, Gleis gleis)
	{
		this.gleise.add(index, gleis);
		gleis.setParent(this);
	}
	public void removeGleis(Gleis gleis)
	{
		this.gleise.remove(gleis);
		if(gleis.getParent() == this)
			gleis.setParent(null);
		else
			System.out.println("Gleis "+gleis.getName()+" aus Betriebsstelle "+getName()+" löschen: bin nicht parent!");
	}
	
	public String toString()
	{
		return "Betriebsstelle "+getName()+" { "+gleise.size()+" Gleise }";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringBuilder str = new StringBuilder(indent+"<betriebsstelle name=\""+getName()+"\">");
		
		if(!gleise.isEmpty())
		{
			for(Gleis g: gleise)
			{
				str.append("\n");
				str.append(g.toXML(indent+"  "));
			}
			str.append("\n"+indent);
		}
		
		str.append("</betriebsstelle>");
		return str.toString();
	}
}
