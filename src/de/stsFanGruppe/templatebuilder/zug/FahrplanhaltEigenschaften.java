package de.stsFanGruppe.templatebuilder.zug;

import java.util.LinkedList;
import java.util.List;

public class FahrplanhaltEigenschaften
{
	// TODO Kuppeln und Fl�geln implementieren
	protected boolean hasDurchfahrt;
	protected boolean hasRichtungswechsel;
	protected boolean hasNamenswechsel;
	protected Fahrt namenswechsel;

	public FahrplanhaltEigenschaften(boolean hasDurchfahrt, boolean hasRichtungswechsel, Fahrt namenswechsel)
	{
		this.hasDurchfahrt = hasDurchfahrt;
		this.hasRichtungswechsel = hasRichtungswechsel;
		this.namenswechsel = namenswechsel;
		if(namenswechsel != null)
			this.hasNamenswechsel = true;
	}
	
	public boolean hasDurchfahrt()
	{
		return hasDurchfahrt;
	}
	public void setDurchfahrt(boolean hasDurchfahrt)
	{
		this.hasDurchfahrt = hasDurchfahrt;
	}
	public boolean hasRichtungswechsel()
	{
		return hasRichtungswechsel;
	}
	public void setRichtungswechsel(boolean hasRichtungswechsel)
	{
		this.hasRichtungswechsel = hasRichtungswechsel;
	}
	public boolean hasNamenswechsel()
	{
		return hasNamenswechsel;
	}
	/**
	 * Gibt den Nachfolger zur�ck. Ist kein Namenswechsel aktiv, oder ist kein Nachfolger gesetzt, wird null zur�ck gegeben.
	 * @return Die Fahrt, auf die der Name ge�ndert wird, oder null
	 */
	public Fahrt getNamenswechselNachfolger()
	{
		if(hasNamenswechsel)
			return namenswechsel;
		else
			return null;
	}
	public void setNamenswechselNachfolger(Fahrt namenswechsel)
	{
		this.hasNamenswechsel = true;
		this.namenswechsel = namenswechsel;
	}
	public void removeNamenswechsel()
	{
		this.namenswechsel = null;
		this.hasNamenswechsel = false;
	}
	
	public String toString()
	{
		StringBuilder str = new StringBuilder("Fahrplanhalt-Eigenschaften { ");
		
		List<String> opts = new LinkedList<>();
		if(hasDurchfahrt)
		{
			opts.add("f�hrt durch");
		}
		if(hasRichtungswechsel)
		{
			opts.add("�ndert Richtung");
		}
		if(hasNamenswechsel)
		{
			if(namenswechsel != null)
			{
				opts.add("�ndert Name zu "+namenswechsel.getName());
			}
			else
			{
				opts.add("�ndert Name (kein Ziel)");
			}
		}
		str.append(String.join(", ", (String[]) opts.toArray()));
		
		str.append(" }");
		return str.toString();
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringBuilder str = new StringBuilder(indent+"<fahrplanhalt");
		
		if(hasDurchfahrt)
		{
			str.append(" durchfahrt=\"true\"");
		}
		if(hasRichtungswechsel)
		{
			str.append(" richtungswechsel=\"true\"");
		}
		if(hasNamenswechsel)
		{
			if(namenswechsel != null)
			{
				str.append(" namenswechsel=\""+namenswechsel.getName()+"\"");
			}
			else
			{
				str.append(" namenswechsel=\"null\"");
			}
		}
		
		str.append(" />\n");
		return str.toString();
	}
}
