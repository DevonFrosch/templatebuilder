package de.stsFanGruppe.zug;

import de.stsFanGruppe.strecken.Gleisabschnitt;

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
	}
}
