package de.stsFanGruppe.templatebuilder.strecken;

import java.util.LinkedList;
import java.util.OptionalDouble;

public class Gleisabschnitt implements Comparable<Gleisabschnitt>
{
	protected String name;
	protected OptionalDouble kmAnfang;
	protected OptionalDouble kmEnde;
	
	protected Gleis parent;
	
	public Gleisabschnitt(String name, Gleis parent)
	{
		this.setName(name);
		this.setParent(parent);
		this.resetKmAnfang();
		this.resetKmEnde();
	}
	public Gleisabschnitt(String name, Gleis parent, double kmAnfang, double kmEnde)
	{
		this(name, parent);
		this.setKmAnfang(kmAnfang);
		this.setKmEnde(kmEnde);
	}
	protected Gleisabschnitt(String name, Gleis parent, OptionalDouble kmAnfang, OptionalDouble kmEnde)
	{
		this(name, parent);
		this.kmAnfang = kmAnfang;
		this.kmEnde = kmEnde;
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
	public void setKmAnfang(double kmAnfang)
	{
		this.kmAnfang = OptionalDouble.of(kmAnfang);
	}
	public void resetKmAnfang()
	{
		this.kmAnfang = OptionalDouble.empty();
	}
	public OptionalDouble getKmEnde()
	{
		return kmEnde;
	}
	public void setKmEnde(double kmEnde)
	{
		this.kmEnde = OptionalDouble.of(kmEnde);
	}
	public void resetKmEnde()
	{
		this.kmEnde = OptionalDouble.empty();
	}
	public Gleis getParent()
	{
		return parent;
	}
	protected void setParent(Gleis parent)
	{
		this.parent = parent;
	}
	
	public String toString()
	{
		StringBuilder str = new StringBuilder("Gleisabschnitt "+getName()+" { ");
		
		LinkedList<String> opts = new LinkedList<>();
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
	
	/**
	 * Vergleicht zwei Gleisabschnitte.
	 * Sind Ortsangaben gegeben, sind die Gleisabschnitte nach kmAnfang und dann nach kmEnde geordnet.
	 * Ohne Ortsangaben sind die Orte gleich. Dies gilt auch, wenn bei einem der Anfang und bei einem
	 * das Ende angegeben ist.
	 * @param other der andere Gleisabschnitt
	 * @return einen Wert kleiner, gleich oder größer Null, wenn dieser Gleisabschnitt kleiner, gleich
	 * oder größer als der andere ist.
	 * @throws NullPointerException falls other gleich null ist.
	 */
	public int compareTo(Gleisabschnitt other)
	{
		if(other == null)
		{
			throw new NullPointerException();
		}
		
		int compared;
		// Sortiere erst nach Anfang...
		if(this.kmAnfang.isPresent() && other.kmAnfang.isPresent())
		{
			compared = Double.compare(this.kmAnfang.getAsDouble(), other.kmAnfang.getAsDouble());
			if(compared != 0)
			{
				return compared;
			}
		}
		// ... dann nach Ende
		if(this.kmEnde.isPresent() && other.kmEnde.isPresent())
		{
			compared = Double.compare(this.kmEnde.getAsDouble(), other.kmEnde.getAsDouble());
			if(compared != 0)
			{
				return compared;
			}
		}
		// Ohne Ortsangabe ist die Reihenfolge undefiniert
		return 0;
	}
}
