package de.stsFanGruppe.templatebuilder.zug;

import java.util.LinkedList;
import java.util.List;
import de.stsFanGruppe.templatebuilder.strecken.Gleisabschnitt;

public class Fahrplanhalt
{
	protected Gleisabschnitt gleisabschnitt;
	protected double ankunft;
	protected double abfahrt;
	protected Eigenschaften eigenschaften;
	
	public Fahrplanhalt(Gleisabschnitt gleisabschnitt, double ankunft, double abfahrt, Eigenschaften eigenschaften)
	{
		this.gleisabschnitt = gleisabschnitt;
		this.ankunft = ankunft;
		this.abfahrt = abfahrt;
		this.eigenschaften = eigenschaften;
	}
	
	public Gleisabschnitt getGleisabschnitt()
	{
		return gleisabschnitt;
	}
	public void setGleisabschnitt(Gleisabschnitt gleisabschnitt)
	{
		this.gleisabschnitt = gleisabschnitt;
	}
	public double getAnkunft()
	{
		return ankunft;
	}
	public void setAnkunft(double ankunft)
	{
		this.ankunft = ankunft;
	}
	public double getAbfahrt()
	{
		return abfahrt;
	}
	public void setAbfahrt(double abfahrt)
	{
		this.abfahrt = abfahrt;
	}
	public Eigenschaften getEigenschaften()
	{
		return eigenschaften;
	}
	public void setEigenschaften(Eigenschaften eigenschaften)
	{
		this.eigenschaften = eigenschaften;
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
	
	public class Eigenschaften
	{
		// TODO Kuppeln und Flügeln implementieren
		protected boolean hasDurchfahrt;
		protected boolean hasRichtungswechsel;
		protected boolean hasNamenswechsel;
		protected Fahrt namenswechsel;

		public Eigenschaften(boolean hasDurchfahrt, boolean hasRichtungswechsel, Fahrt namenswechsel)
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
		 * Gibt den Nachfolger zurück. Ist kein Namenswechsel aktiv, oder ist kein Nachfolger gesetzt, wird null zurück gegeben.
		 * @return Die Fahrt, auf die der Name geändert wird, oder null
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
				opts.add("fährt durch");
			}
			if(hasRichtungswechsel)
			{
				opts.add("ändert Richtung");
			}
			if(hasNamenswechsel)
			{
				if(namenswechsel != null)
				{
					opts.add("ändert Name zu "+namenswechsel.getName());
				}
				else
				{
					opts.add("ändert Name (kein Ziel)");
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
}
