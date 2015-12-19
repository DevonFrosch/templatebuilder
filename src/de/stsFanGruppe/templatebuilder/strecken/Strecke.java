package de.stsFanGruppe.templatebuilder.strecken;

import java.util.*;
import java.util.function.DoubleSupplier;
import de.stsFanGruppe.tools.NullTester;

/**
 * Eine Strecke ist eine Verbindung zwischen zwei Betriebsstellen.
 * Jede Strecke hat einen Anfang, ein Ende sowie ein oder zwei Streckengleise.
 * Eine Strecke mit mehr als zwei Gleisen kann durch mehrere verschiedene Strecken modelliert werden.
 * <br>
 * Bei zweigleisigen Strecken sind die Streckengleise durchnummeriert, Streckengleis 0 ist dabei
 * das Gleis in Fahrtrichtung Anfang -> Ende, Streckengleis 1 ist das Gleis Ende -> Anfang.
 * 
 * @author DevonFrosch
 */
public class Strecke
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Strecke.class);
	
	protected String name;
	protected Betriebsstelle anfang;
	protected Betriebsstelle ende;
	
	protected int anzahlGleise;
	
	/**
	 * Erzeugt eine zweigleisige Strecke
	 * @param name Name der Strecke
	 * @param anfang
	 * @param ende
	 */
	public Strecke(String name, Betriebsstelle anfang, Betriebsstelle ende)
	{
		this.setName(name);
		this.setAnfang(anfang);
		this.setEnde(ende);
		this.setAnzahlGleise(2);
	}
	/**
	 * Erzeugt eine Strecke
	 * @param name
	 * @param anfang
	 * @param ende
	 * @param anzahlGleise
	 */
	public Strecke(String name, Betriebsstelle anfang, Betriebsstelle ende, int anzahlGleise)
	{
		this(name, anfang, ende);
		this.setAnzahlGleise(anzahlGleise);
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
	public Betriebsstelle getAnfang()
	{
		return anfang;
	}
	public void setAnfang(Betriebsstelle anfang)
	{
		NullTester.test(anfang);
		this.anfang = anfang;
	}
	public Betriebsstelle getEnde()
	{
		return ende;
	}
	public void setEnde(Betriebsstelle ende)
	{
		NullTester.test(ende);
		this.ende = ende;
	}
	public int getAnzahlGleise()
	{
		return anzahlGleise;
	}
	public void setAnzahlGleise(int anzahlGleise)
	{
		if(anzahlGleise <= 0)
		{
			throw new IllegalArgumentException("Es muss mindestens ein Gleis geben.");
		}
		this.anzahlGleise = anzahlGleise;
	}

	public double getMinKm()
	{
		double anfangKm = getOrDefault(() -> anfang.getMinKm(), Double.MAX_VALUE);
		double endeKm = getOrDefault(() -> ende.getMinKm(), Double.MAX_VALUE);
		
		return Double.min(anfangKm, endeKm);
	}
	public double getMaxKm()
	{
		double anfangKm = getOrDefault(() -> anfang.getMaxKm(), Double.MIN_VALUE);
		double endeKm = getOrDefault(() -> ende.getMaxKm(), Double.MIN_VALUE);
		
		return Double.max(anfangKm, endeKm);
	}
	protected double getOrDefault(DoubleSupplier func, double defaultValue)
	{
		try
		{
			return func.getAsDouble();
		}
		catch(Exception e)
		{
			log.debug("getOrDefault: Exception", e);
			return defaultValue;
		}
	}
	
	/**
	 * Vergleicht diese Strecke mit einer anderen Strecke anhand ihrer Namen.
	 * @param other Die andere Strecke.
	 * @return true, wenn other nicht null ist und den selben Namen hat.
	 */
	public boolean equals(Object other)
	{
		if(other == null)
		{
			return false;
		}
		Strecke o = (Strecke) other;
		return this.name == o.name; 
	}
	public String toString()
	{
		return "Strecke "+getName()+" { Anfang: "+anfang.getName()+", Ende: "+ende.getName()+", Anzahl Gleise: "+anzahlGleise+" }";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringJoiner xml = new StringJoiner("\n");
		xml.add(indent+"<strecke name=\""+getName()+"\" anzahlGleise=\""+anzahlGleise+"\">");
		
		if(ende != null)
		{
			xml.add(indent+"  <verbindung typ=\"anfang\">");
			xml.add(anfang.toXML(indent+"    "));
			xml.add(indent+"  </verbindung>");
		}
		
		if(ende != null)
		{
			xml.add(indent+"  <verbindung typ=\"ende\">");
			xml.add(ende.toXML(indent+"    "));
			xml.add(indent+"  </verbindung>");
		}
		
		xml.add(indent+"</strecke>");
		return xml.toString();
	}
}
