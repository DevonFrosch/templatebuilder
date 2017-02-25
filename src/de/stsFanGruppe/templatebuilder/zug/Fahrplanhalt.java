package de.stsFanGruppe.templatebuilder.zug;

import java.util.*;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Gleisabschnitt;
import de.stsFanGruppe.tools.NullTester;

/**
 * Ein Fahrplanhalt ist ein Eintrag in einem Fahrplan eines Zuges, der einem Gleisabschnitt
 * eine Ankunft und eine Abfahrt zuweist.
 * Fahrplanhalte sind zeitlich geordnet, zuerst nach Ankunft, dann nach Abfahrt.
 * 
 * @author DevonFrosch
 */
public class Fahrplanhalt implements Comparable<Fahrplanhalt>
{
	protected Gleisabschnitt gleisabschnitt;
	protected OptionalDouble ankunft;
	protected OptionalDouble abfahrt;
	protected FahrplanhaltEigenschaften eigenschaften;
	protected Fahrt parent = null;
	
	public Fahrplanhalt(Gleisabschnitt gleisabschnitt, OptionalDouble ankunft, OptionalDouble abfahrt, FahrplanhaltEigenschaften eigenschaften)
	{
		this.setGleisabschnitt(gleisabschnitt);
		this.setZeiten(ankunft, abfahrt);
		this.setEigenschaften(eigenschaften);
	}
	
	protected Fahrplanhalt(Fahrt parent, Gleisabschnitt gleisabschnitt, OptionalDouble ankunft, OptionalDouble abfahrt, FahrplanhaltEigenschaften eigenschaften)
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
	
	public Betriebsstelle getBetriebsstelle()
	{
		return gleisabschnitt.getParent().getParent();
	}
	
	public OptionalDouble getAnkunft()
	{
		return ankunft;
	}
	
	/**
	 * Setzt Ankunft und Abfahrt zeitgleich.
	 * 
	 * @param ankunft Die Ankunft am Gleisabschnitt.
	 * @throws IllegalArgumentException Falls eine Zeit kleiner 0 oder die Abfahrt kleiner als (vor der) Ankunft ist.
	 */
	public void setAnkunft(OptionalDouble ankunft)
	{
		if(ankunft.isPresent())
		{
			if(ankunft.getAsDouble() < 0)
			{
				throw new IllegalArgumentException("Zeit muss größer gleich 0 sein");
			}
			if(abfahrt.isPresent() && ankunft.getAsDouble() > abfahrt.getAsDouble())
			{
				throw new IllegalArgumentException("Abfahrt vor Ankunft");
			}
		}
		this.ankunft = ankunft;
	}
	
	public OptionalDouble getAbfahrt()
	{
		return abfahrt;
	}
	
	/**
	 * Setzt Ankunft und Abfahrt zeitgleich.
	 * 
	 * @param abfahrt Die Abfahrt am Gleisabschnitt.
	 * @throws IllegalArgumentException Falls eine Zeit kleiner 0 oder die Abfahrt kleiner als (vor der) Ankunft ist.
	 */
	public void setAbfahrt(OptionalDouble abfahrt)
	{
		if(abfahrt.isPresent())
		{
			if(abfahrt.getAsDouble() < 0)
			{
				throw new IllegalArgumentException("Zeit muss größer gleich 0 sein");
			}
			if(ankunft.isPresent() && ankunft.getAsDouble() > abfahrt.getAsDouble())
			{
				throw new IllegalArgumentException("Abfahrt vor Ankunft");
			}
		}
		this.abfahrt = abfahrt;
	}
	
	/**
	 * Setzt Ankunft und Abfahrt zeitgleich.
	 * 
	 * @param ankunft Die Ankunft am Gleisabschnitt.
	 * @param abfahrt Die Abfahrt am Gleisabschnitt.
	 * @throws IllegalArgumentException Falls eine Zeit kleiner 0 oder die Abfahrt kleiner als (vor der) Ankunft ist.
	 */
	public void setZeiten(OptionalDouble ankunft, OptionalDouble abfahrt)
	{
		if(!ankunft.isPresent() && !abfahrt.isPresent())
		{
			throw new IllegalArgumentException("Keine Ankunft und keine Abfahrt");
		}
		if((abfahrt.isPresent() && abfahrt.getAsDouble() < 0) || (ankunft.isPresent() && ankunft.getAsDouble() < 0))
		{
			throw new IllegalArgumentException("Zeit muss größer gleich 0 sein");
		}
		if(ankunft.isPresent() && abfahrt.isPresent() && ankunft.getAsDouble() > abfahrt.getAsDouble())
		{
			throw new IllegalArgumentException("Abfahrt vor Ankunft");
		}
		this.ankunft = ankunft;
		this.abfahrt = abfahrt;
	}
	
	public double getMinZeit()
	{
		if(ankunft.isPresent())
		{
			return ankunft.getAsDouble();
		}
		return abfahrt.getAsDouble();
	}
	
	public double getMaxZeit()
	{
		if(abfahrt.isPresent())
		{
			return abfahrt.getAsDouble();
		}
		return ankunft.getAsDouble();
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
		return "Fahrplanhalt { gleisabschnitt: " + gleisabschnitt.getName() + ((ankunft.isPresent()) ? ", ankunft: " + ankunft.getAsDouble() : "")
				+ ((abfahrt.isPresent()) ? ", abfahrt: " + abfahrt.getAsDouble() : "") + ", " + eigenschaften.toString() + " }";
	}
	
	public String toXML()
	{
		return toXML("");
	}
	
	public String toXML(String indent)
	{
		StringJoiner xml = new StringJoiner("\n");
		xml.add(indent + "<fahrplanhalt gleisabschnitt=\"" + gleisabschnitt.getName() + "\" ankunft=\"" + (ankunft.isPresent() ? ankunft.getAsDouble() : "")
				+ "\" abfahrt=\"" + (abfahrt.isPresent() ? abfahrt.getAsDouble() : "") + "\">");
		xml.add(eigenschaften.toXML(indent + "  "));
		xml.add(indent + "</fahrplanhalt>");
		return xml.toString();
	}
	
	/**
	 * Vergleicht den Fahrplanhalt mit einem anderen. Sie sind zuerst nach Ankunft, dann nach Abfahrt sortiert.
	 * 
	 * @param other der andere Fahrplanhalt.
	 * @return einen negativen Wert, 0, oder einen positiven Wert, wenn dieser Fahrplanhalt kleiner, gleich
	 *         oder größer als der andere Fahrplanhalt ist.
	 * @throws NullPointerException falls der andere Fahrplanhalt null ist.
	 */
	public int compareTo(Fahrplanhalt other)
	{
		if(other == null)
		{
			throw new NullPointerException();
		}
		
		// Sortiere erst nach Ankunft...
		if(this.ankunft.isPresent() && other.ankunft.isPresent())
		{
			if(Double.compare(this.ankunft.getAsDouble(), other.ankunft.getAsDouble()) != 0)
			{
				return Double.compare(this.ankunft.getAsDouble(), other.ankunft.getAsDouble());
			}
		}
		// ... dann nach Abfahrt
		if(this.abfahrt.isPresent() && other.abfahrt.isPresent())
		{
			return Double.compare(this.abfahrt.getAsDouble(), other.abfahrt.getAsDouble());
		}
		// Eindeutigkeiten bei nicht definierten Zeiten
		if(this.getMinZeit() > other.getMaxZeit())
		{
			return 1;
		}
		if(this.getMaxZeit() < other.getMinZeit())
		{
			return -1;
		}
		// Bei unterschiedlichen Zeittypen einfach so sortieren
		if(Double.compare(this.getMinZeit(), other.getMinZeit()) != 0)
		{
			return Double.compare(this.getMinZeit(), other.getMinZeit());
		}
		return Double.compare(this.getMaxZeit(), other.getMaxZeit());
	}
	
	public static class StrictComparator implements Comparator<Fahrplanhalt>
	{
		/**
		 * Vergleicht den Fahrplanhalt mit einem anderen. Sie sind zuerst nach Ankunft, dann nach Abfahrt sortiert.
		 * 
		 * @param a der eine Fahrplanhalt.
		 * @param b der andere Fahrplanhalt.
		 * @return einen negativen Wert, 0, oder einen positiven Wert, wenn dieser Fahrplanhalt kleiner, gleich
		 *         oder größer als der andere Fahrplanhalt ist.
		 * @throws NullPointerException falls der andere Fahrplanhalt null ist.
		 * @throws IllegalStateException falls bei mindestens einem Objekt Abfahrt < Ankunft ist.
		 */
		public int compare(Fahrplanhalt a, Fahrplanhalt b) throws IllegalStateException
		{
			int comp = a.compareTo(b);
			
			if(a.abfahrt.isPresent() && b.ankunft.isPresent()
					&& (comp < 0 && a.abfahrt.getAsDouble() > b.ankunft.getAsDouble() || comp > 0 && a.abfahrt.getAsDouble() < b.ankunft.getAsDouble()))
			{
				throw new IllegalStateException("Abfahrt vor Ankunft");
			}
			
			return comp;
		}
	}
}
