package de.stsFanGruppe.templatebuilder.strecken;

import de.stsFanGruppe.tools.NullTester;

public class Gleisabschnitt implements Comparable<Gleisabschnitt>
{
	protected String name;
	protected double km = 0;
	
	protected Gleis parent = null;
	
	public Gleisabschnitt(String name, Gleis parent, double km)
	{
		this.setName(name);
		this.setParent(parent);
		this.setKm(km);
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
	
	public double getKm()
	{
		return km;
	}
	
	public void setKm(double km)
	{
		this.km = km;
	}
	
	public Gleis getParent()
	{
		return parent;
	}
	
	void setParent(Gleis parent)
	{
		this.parent = parent;
	}
	
	public String toString()
	{
		return "Gleisabschnitt " + getName() + " { km = " + km + " }";
	}
	
	public String toXML()
	{
		return toXML("");
	}
	
	public String toXML(String indent)
	{
		return indent + "<gleisabschnitt name=\"" + getName() + "\" km=\"" + getKm() + "\" />";
	}
	
	/**
	 * Vergleicht zwei Gleisabschnitte.
	 * Sind Ortsangaben gegeben, sind die Gleisabschnitte nach kmAnfang und dann nach kmEnde geordnet.
	 * Ohne Ortsangaben sind die Orte gleich. Dies gilt auch, wenn bei einem der Anfang und bei einem
	 * das Ende angegeben ist.
	 * 
	 * @param other der andere Gleisabschnitt
	 * @return einen Wert kleiner, gleich oder größer Null, wenn dieser Gleisabschnitt kleiner, gleich
	 *         oder größer als der andere ist.
	 * @throws NullPointerException falls other gleich null ist.
	 */
	public int compareTo(Gleisabschnitt other)
	{
		NullTester.test(other);
		return Double.compare(this.km, other.km);
	}
}
