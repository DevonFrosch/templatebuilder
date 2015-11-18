package de.stsFanGruppe.templatebuilder.strecken;

import java.util.LinkedList;
import de.stsFanGruppe.tools.NullTester;

public class Gleisabschnitt implements Comparable<Gleisabschnitt>
{
	protected String name;
	protected double kmAnfang = 0;
	protected double kmEnde = 0;
	
	protected Gleis parent = null;
	
	public Gleisabschnitt(String name, Gleis parent, double kmAnfang, double kmEnde)
	{
		this.setName(name);
		this.setParent(parent);
		this.setKmAnfang(kmAnfang);
		this.setKmEnde(kmEnde);
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
	public double getKmAnfang()
	{
		return kmAnfang;
	}
	public void setKmAnfang(double kmAnfang)
	{
		this.kmAnfang = kmAnfang;
	}
	public double getKmEnde()
	{
		return kmEnde;
	}
	public void setKmEnde(double kmEnde)
	{
		this.kmEnde = kmEnde;
	}
	public Gleis getParent()
	{
		return parent;
	}
	protected void setParent(Gleis parent)
	{
		this.parent = parent;
	}
	
	public double getMinKm()
	{
		return Double.min(kmAnfang, kmEnde);
	}
	public double getMaxKm()
	{
		return Double.max(kmAnfang, kmEnde);
	}
	
	public String toString()
	{
		StringBuilder str = new StringBuilder("Gleisabschnitt "+getName()+" { ");
		
		LinkedList<String> opts = new LinkedList<>();
		opts.add("kmAnfang: "+getKmEnde());
		opts.add("kmEnde: "+getKmEnde());
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
		return indent+"<gleisabschnitt name=\""+getName()+"\" kmAnfang=\""+getKmAnfang()+"\" kmEnde=\""+getKmEnde()+"\" />";
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
		NullTester.test(other);
		
		int compared;
		// Sortiere erst nach Anfang...
		if((compared = Double.compare(this.kmAnfang, other.kmAnfang)) != 0)
		{
			return compared;
		}
		// ... dann nach Ende
		return Double.compare(this.kmEnde, other.kmEnde);
	}
}
