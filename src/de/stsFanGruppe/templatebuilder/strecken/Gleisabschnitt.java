package de.stsFanGruppe.templatebuilder.strecken;

import java.util.LinkedList;
import java.util.List;
import java.util.OptionalDouble;

public class Gleisabschnitt
{
	protected String name;
	protected OptionalDouble kmAnfang;
	protected OptionalDouble kmEnde;
	
	protected Gleisabschnitt(String name, OptionalDouble kmAnfang, OptionalDouble kmEnde)
	{
		this.name = name;
		this.kmAnfang = kmAnfang;
		this.kmEnde = kmEnde;
	}
	public Gleisabschnitt(String name, double kmAnfang, double kmEnde)
	{
		this(name, OptionalDouble.of(kmAnfang), OptionalDouble.of(kmEnde));
	}
	public Gleisabschnitt(String name)
	{
		this(name, OptionalDouble.empty(), OptionalDouble.empty());
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public OptionalDouble getKmAnfang()
	{
		return kmAnfang;
	}
	public void setKmAnfang(double km_anfang)
	{
		this.kmAnfang = OptionalDouble.of(km_anfang);
	}
	public void removeKmAnfang()
	{
		this.kmAnfang = OptionalDouble.empty();
	}
	public OptionalDouble getKmEnde()
	{
		return kmEnde;
	}
	public void setKmEnde(double km_ende)
	{
		this.kmEnde = OptionalDouble.of(km_ende);
	}
	public void removeKmEnde()
	{
		this.kmEnde = OptionalDouble.empty();
	}
	
	public String toString()
	{
		StringBuilder str = new StringBuilder("Gleisabschnitt "+getName()+" { ");
		
		List<String> opts = new LinkedList<>();
		if(kmAnfang.isPresent())
		{
			opts.add("kmAnfang: "+getKmEnde().getAsDouble());
		}
		if(kmEnde.isPresent())
		{
			opts.add("kmEnde: "+getKmEnde().getAsDouble());
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
		StringBuilder str = new StringBuilder(indent+"<gleis name=\""+getName()+"\"");
		
		if(kmAnfang.isPresent())
		{
			str.append(" kmAnfang=\""+getKmAnfang().getAsDouble()+"\"");
		}
		if(kmEnde.isPresent())
		{
			str.append(" kmEnde=\""+getKmEnde().getAsDouble()+"\"");
		}
		
		str.append(" />");
		return str.toString();
	}
}
