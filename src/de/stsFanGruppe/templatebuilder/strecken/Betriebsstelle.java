package de.stsFanGruppe.templatebuilder.strecken;

import java.util.Collection;
import java.util.StringJoiner;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;
import de.stsFanGruppe.tools.NullTester;

public class Betriebsstelle
{	
	protected String name;
	protected FirstLastList<Gleis> gleise = new FirstLastLinkedList<>();
	
	public Betriebsstelle(String name, Collection<? extends Gleis> gleise)
	{
		this(name);
		this.addGleise(gleise);
	}
	public Betriebsstelle(String name)
	{
		this.setName(name);
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
	public boolean hasGleise()
	{
		return !gleise.isEmpty();
	}
	public FirstLastList<Gleis> getGleise()
	{
		return gleise;
	}
	public void addGleis(Gleis gleis)
	{
		NullTester.test(gleis);
		this.gleise.add(gleis);
		gleis.setParent(this);
	}
	public void addGleis(int index, Gleis gleis)
	{
		NullTester.test(gleis);
		this.gleise.add(index, gleis);
		gleis.setParent(this);
	}
	protected void addGleise(Collection<? extends Gleis> gleise)
	{
		NullTester.test(gleise);
		gleise.forEach((Gleis g) -> this.addGleis(g));
	}
	public boolean removeGleis(Gleis gleis)
	{
		NullTester.test(gleis);
		boolean erfolg = this.gleise.remove(gleis);
		if(gleis.getParent() == this)
			gleis.setParent(null);
		else
			System.out.println("Gleis "+gleis.getName()+" aus Betriebsstelle "+getName()+" löschen: bin nicht parent!");
		return erfolg;
	}

	public double getMinKm()
	{
		return gleise.stream().min((a,b) -> Double.compare(a.getMinKm(), b.getMinKm())).get().getMinKm();
	}
	public double getMaxKm()
	{
		return gleise.stream().min((a,b) -> Double.compare(a.getMaxKm(), b.getMaxKm())).get().getMaxKm();
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
		StringJoiner xml = new StringJoiner("\n");
		xml.add(indent+"<betriebsstelle name=\""+getName()+"\">");
		
		if(!gleise.isEmpty())
		{
			for(Gleis g: gleise)
			{
				xml.add(g.toXML(indent+"  "));
			}
		}
		
		xml.add(indent+"</betriebsstelle>");
		return xml.toString();
	}
}
