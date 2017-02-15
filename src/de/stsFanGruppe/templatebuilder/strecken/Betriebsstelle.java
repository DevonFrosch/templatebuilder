package de.stsFanGruppe.templatebuilder.strecken;

import java.util.*;
import de.stsFanGruppe.tools.*;

public class Betriebsstelle
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Betriebsstelle.class);
	
	protected String name;
	protected String ds100;
	protected FirstLastList<Gleis> gleise = new FirstLastLinkedList<>();

	public Betriebsstelle(String name, String ds100, Collection<? extends Gleis> gleise)
	{
		this(name, ds100);
		this.addGleise(gleise);
	}
	public Betriebsstelle(String name, Collection<? extends Gleis> gleise)
	{
		this(name);
		this.addGleise(gleise);
	}
	public Betriebsstelle(String name, String ds100)
	{
		this(name);
		this.ds100 = ds100;
	}
	public Betriebsstelle(String name)
	{
		this.setName(name);
		this.ds100 = null;
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
	public String getDS100()
	{
		return ds100;
	}
	public void setDS100(String ds100)
	{
		this.ds100 = ds100;
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
		if(gleis.getParent() != null)
		{
			log.error("addGleis: Gleis hat schon parent: "+gleis.getParent().getName());
			throw new IllegalStateException("Gleis wird schon verwendet!");
		}
		gleis.setParent(this);
	}
	public void addGleis(int index, Gleis gleis)
	{
		NullTester.test(gleis);
		if(gleis.getParent() != null)
		{
			log.error("addGleis: Gleis hat schon parent: "+gleis.getParent().getName());
			throw new IllegalStateException("Gleis wird schon verwendet!");
		}
		gleis.setParent(this);
		this.gleise.add(index, gleis);
	}
	protected void addGleise(Collection<? extends Gleis> gleise)
	{
		NullTester.test(gleise);
		gleise.forEach((Gleis g) -> this.addGleis(g));
	}
	public boolean removeGleis(Gleis gleis)
	{
		NullTester.test(gleis);
		if(!gleise.contains(gleis))
		{
			return false;
		}
		
		if(gleis.getParent() == this)
		{
			gleis.setParent(null);
		}
		else
		{
			log.error("Gleis {} aus Betriebsstelle {} löschen: bin nicht parent!", gleis.getName(), getName());
			throw new IllegalStateException("Gleise asynchron!");
		}
		return this.gleise.remove(gleis);
	}
	
	public double getMinKm() throws NoSuchElementException
	{
		return gleise.stream().min((a,b) -> Double.compare(a.getMinKm(), b.getMinKm())).get().getMinKm();
	}
	public double getMaxKm() throws NoSuchElementException
	{
		return gleise.stream().min((a,b) -> Double.compare(a.getMaxKm(), b.getMaxKm())).get().getMaxKm();
	}
	
	public String toString()
	{
		if(getDS100() != null)
		{
			return "Betriebsstelle "+getDS100()+" { "+gleise.size()+" Gleise }";
		}
		return "Betriebsstelle "+getName()+" { "+gleise.size()+" Gleise }";
		
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringJoiner xml = new StringJoiner("\n");
		if(ds100 != null)
		{
			xml.add(indent+"<betriebsstelle name=\""+getName()+"\" ds100=\""+getDS100()+"\">");	
		}
		else
		{
			xml.add(indent+"<betriebsstelle name=\""+getName()+"\">");	
		}
		
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
	public boolean equals(Object other)
	{
		if(other == null)
		{
			return false;
		}
		Betriebsstelle o = (Betriebsstelle) other;
		if(this.ds100 != null && o.ds100 != null)
		{
			return this.ds100 == o.ds100;
		}
		return this.name == o.name;
	}
	public int compareByKM(Betriebsstelle other)
	{
		NullTester.test(other);
		
		// Sortiere erst nach Minkm...
		if(Double.compare(this.getMinKm(), other.getMinKm()) != 0)
		{
			return Double.compare(this.getMinKm(), other.getMinKm());
		}
		// ... dann nach Maxkm...
		return Double.compare(this.getMaxKm(), other.getMaxKm());
	}
}
