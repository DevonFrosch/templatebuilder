package de.stsFanGruppe.templatebuilder.zug;

import java.util.StringJoiner;

public class FahrplanhaltEigenschaften
{
	// TODO Kuppeln und Fl�geln implementieren
	protected boolean hasDurchfahrt = false;
	protected boolean hasRichtungswechsel = false;
	protected boolean hasNamenswechsel = false;
	protected Fahrt namenswechsel = null;

	public FahrplanhaltEigenschaften(boolean hasDurchfahrt, boolean hasRichtungswechsel, Fahrt namenswechsel)
	{
		this(hasDurchfahrt, hasRichtungswechsel);
		this.setNamenswechselNachfolger(namenswechsel);
	}
	public FahrplanhaltEigenschaften(boolean hasDurchfahrt, boolean hasRichtungswechsel)
	{
		this.setDurchfahrt(hasDurchfahrt);
		this.setRichtungswechsel(hasRichtungswechsel);
	}
	public FahrplanhaltEigenschaften()
	{
		
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
		StringJoiner str = new StringJoiner(", ", "Fahrplanhalt-Eigenschaften { ", " }");
		
		if(hasDurchfahrt)
		{
			str.add("f�hrt durch");
		}
		if(hasRichtungswechsel)
		{
			str.add("�ndert Richtung");
		}
		if(hasNamenswechsel)
		{
			if(namenswechsel != null)
			{
				str.add("�ndert Name zu "+namenswechsel.getName());
			}
			else
			{
				str.add("�ndert Name (kein Ziel)");
			}
		}
		
		return str.toString();
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringJoiner xml = new StringJoiner(" ", indent+"<fahrplanhaltEigenschaften", " />");
		
		if(hasDurchfahrt)
		{
			xml.add("durchfahrt=\"true\"");
		}
		if(hasRichtungswechsel)
		{
			xml.add("richtungswechsel=\"true\"");
		}
		if(hasNamenswechsel)
		{
			if(namenswechsel != null)
			{
				xml.add("namenswechsel=\""+namenswechsel.getName()+"\"");
			}
			else
			{
				xml.add("namenswechsel=\"null\"");
			}
		}
		
		return xml.toString();
	}
}
