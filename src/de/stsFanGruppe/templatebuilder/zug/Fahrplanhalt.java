package de.stsFanGruppe.templatebuilder.zug;

import java.util.*;
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
		this.setZeiten(ankunft, abfahrt);
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
	/**
	 * Setzt Ankunft und Abfahrt zeitgleich.
	 * @param ankunft Die Ankunft am Gleisabschnitt.
	 * @throws IllegalArgumentException Falls eine Zeit kleiner 0 oder die Abfahrt kleiner als (vor der) Ankunft ist.
	 */
	public void setAnkunft(double ankunft)
	{
		if(ankunft < 0)
		{
			throw new IllegalArgumentException("Zeit muss größer gleich 0 sein");
		}
		if(ankunft > abfahrt)
		{
			throw new IllegalArgumentException("Abfahrt vor Ankunft");
		}
		this.ankunft = ankunft;
	}
	public double getAbfahrt()
	{
		return abfahrt;
	}
	/**
	 * Setzt Ankunft und Abfahrt zeitgleich.
	 * @param abfahrt Die Abfahrt am Gleisabschnitt.
	 * @throws IllegalArgumentException Falls eine Zeit kleiner 0 oder die Abfahrt kleiner als (vor der) Ankunft ist.
	 */
	public void setAbfahrt(double abfahrt)
	{
		if(abfahrt < 0)
		{
			throw new IllegalArgumentException("Zeit muss größer gleich 0 sein");
		}
		if(ankunft > abfahrt)
		{
			throw new IllegalArgumentException("Abfahrt vor Ankunft");
		}
		this.abfahrt = abfahrt;
	}
	/**
	 * Setzt Ankunft und Abfahrt zeitgleich.
	 * @param ankunft Die Ankunft am Gleisabschnitt.
	 * @param abfahrt Die Abfahrt am Gleisabschnitt.
	 * @throws IllegalArgumentException Falls eine Zeit kleiner 0 oder die Abfahrt kleiner als (vor der) Ankunft ist.
	 */
	public void setZeiten(double ankunft, double abfahrt)
	{
		if(abfahrt < 0 || ankunft < 0)
		{
			throw new IllegalArgumentException("Zeit muss größer gleich 0 sein");
		}
		if(ankunft > abfahrt)
		{
			throw new IllegalArgumentException("Abfahrt vor Ankunft");
		}
		this.ankunft = ankunft;
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
		StringJoiner xml = new StringJoiner("\n");
		xml.add(indent+"<fahrplanhalt gleisabschnitt=\""+gleisabschnitt.getName()+"\" ankunft=\""+ankunft+"\" abfahrt=\""+abfahrt+"\">");
		xml.add(eigenschaften.toXML(indent+"  "));
		xml.add(indent+"</fahrplanhalt>");
		return xml.toString();
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
		NullTester.test(other);
		
		// Sortiere erst nach Ankunft...
		if(Double.compare(this.ankunft, other.ankunft) != 0)
		{
			return Double.compare(this.ankunft, other.ankunft);
		}
		// ... dann nach Abfahrt
		return Double.compare(this.abfahrt, other.abfahrt);
	}
	public static class StrictComparator implements Comparator<Fahrplanhalt>
	{
		/**
		 * Vergleicht den Fahrplanhalt mit einem anderen. Sie sind zuerst nach Ankunft, dann nach Abfahrt sortiert.
		 * @param a der eine Fahrplanhalt.
		 * @param b der andere Fahrplanhalt.
		 * @return einen negativen Wert, 0, oder einen positiven Wert, wenn dieser Fahrplanhalt kleiner, gleich
		 * oder größer als der andere Fahrplanhalt ist.
		 * @throws NullPointerException falls der andere Fahrplanhalt null ist.
		 * @throws IllegalStateException falls bei mindestens einem Objekt Abfahrt < Ankunft ist.
		 */
		public int compare(Fahrplanhalt a, Fahrplanhalt b) throws IllegalStateException
		{
			int comp = a.compareTo(b);
			
			if(comp < 0 && a.abfahrt > b.ankunft
				|| comp > 0 && a.abfahrt < b.ankunft)
			{
				throw new IllegalStateException("Abfahrt vor Ankunft");
			}
			
			return comp;
		}
	}
}
