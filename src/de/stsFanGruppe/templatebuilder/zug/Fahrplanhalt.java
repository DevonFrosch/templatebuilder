package de.stsFanGruppe.templatebuilder.zug;

import de.stsFanGruppe.templatebuilder.strecken.Gleisabschnitt;
import de.stsFanGruppe.tools.NullTester;

/**
 * Ein Fahrplanhalt ist ein Eintrag in einem Fahrplan eines Zuges, der einem Gleisabschnitt
 * eine Ankunft und eine Abfahrt zuweist.
 * Fahrplanhalte sind zeitlich geordnet, zuerst nach Ankunft, dann nach Abfahrt.
 * @author DevonFrosch
 */
public class Fahrplanhalt implements Comparable<Fahrplanhalt>
{
	protected Gleisabschnitt gleisabschnitt;
	protected double ankunft;
	protected double abfahrt;
	protected FahrplanhaltEigenschaften eigenschaften;
	protected Fahrt parent = null;

	public Fahrplanhalt(Gleisabschnitt gleisabschnitt, double ankunft, double abfahrt, FahrplanhaltEigenschaften eigenschaften)
	{
		this.setGleisabschnitt(gleisabschnitt);
		this.setAnkunft(ankunft);
		this.setAbfahrt(abfahrt);
		this.setEigenschaften(eigenschaften);
	}
	protected Fahrplanhalt(Fahrt parent, Gleisabschnitt gleisabschnitt, double ankunft, double abfahrt, FahrplanhaltEigenschaften eigenschaften)
	{
		this(gleisabschnitt, ankunft, abfahrt, eigenschaften);
		this.setParent(parent);
	}
	
	public Gleisabschnitt getGleisabschnitt()
	{
		return gleisabschnitt;
	}
	public void setGleisabschnitt(Gleisabschnitt gleisabschnitt)
	{
		NullTester.test(gleisabschnitt);
		this.gleisabschnitt = gleisabschnitt;
	}
	public double getAnkunft()
	{
		return ankunft;
	}
	public void setAnkunft(double ankunft)
	{
		if(ankunft < 0)
		{
			throw new IllegalArgumentException("Zeit muss größer als 0 sein.");
		}
		this.ankunft = ankunft;
	}
	public double getAbfahrt()
	{
		return abfahrt;
	}
	public void setAbfahrt(double abfahrt)
	{
		if(abfahrt < 0)
		{
			throw new IllegalArgumentException("Zeit muss größer als 0 sein.");
		}
		this.abfahrt = abfahrt;
	}
	public FahrplanhaltEigenschaften getEigenschaften()
	{
		return eigenschaften;
	}
	public void setEigenschaften(FahrplanhaltEigenschaften eigenschaften)
	{
		NullTester.test(eigenschaften);
		this.eigenschaften = eigenschaften;
	}
	public Fahrt getParent()
	{
		return parent;
	}
	protected void setParent(Fahrt parent)
	{
		this.parent = parent;
	}
	
	public String toString()
	{
		return "Fahrplanhalt { gleisabschnitt: "+gleisabschnitt.getName()+", ankunft: "+ankunft+", abfahrt: "+abfahrt+", "+eigenschaften.toString()+" }";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringBuilder str = new StringBuilder();
		
		str.append(indent+"<fahrplanhalt gleisabschnitt=\""+gleisabschnitt.getName()+"\" ankunft=\""+ankunft+"\" abfahrt=\""+abfahrt+"\">\n");
		str.append(eigenschaften.toXML(indent+"  ")+"\n");
		str.append(indent+"</fahrplanhalt>");
		
		return str.toString();
	}
	
	/**
	 * Vergleicht den Fahrplanhalt mit einem anderen. Sie sind zuerst nach Ankunft, dann nach Abfahrt sortiert.
	 * @param other der andere Fahrplanhalt.
	 * @return einen negativen Wert, 0, oder einen positiven Wert, wenn dieser Fahrplanhalt kleiner, gleich
	 * oder größer als der andere Fahrplanhalt ist.
	 * @throws NullPointerException falls der andere Fahrplanhalt null ist.
	 */
	public int compareTo(Fahrplanhalt other)
	{
		if(other == null)
		{
			throw new NullPointerException();
		}
		
		// Sortiere erst nach Ankunft...
		if(Double.compare(this.ankunft, other.ankunft) != 0)
		{
			return Double.compare(this.ankunft, other.ankunft);
		}
		// ... dann nach Abfahrt
		return Double.compare(this.abfahrt, other.abfahrt);
	}
}
