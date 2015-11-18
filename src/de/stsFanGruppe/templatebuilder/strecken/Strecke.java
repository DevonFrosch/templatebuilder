package de.stsFanGruppe.templatebuilder.strecken;

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
		return Double.min(anfang.getMinKm(), ende.getMinKm());
	}
	public double getMaxKm()
	{
		return Double.max(anfang.getMaxKm(), ende.getMaxKm());
	}
	
	public String toString()
	{
		StringBuilder str = new StringBuilder("Strecke "+getName()+" { Anfang: ");
		
		str.append((anfang != null) ? anfang.getName() : "Undefiniert");
		str.append(", Ende: ");
		str.append((ende != null) ? ende.getName() : "Undefiniert");
		str.append(", Anzahl Gleise: "+anzahlGleise+" }");
		return str.toString();
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringBuilder str = new StringBuilder(indent+"<strecke name=\""+getName()+"\" anzahlGleise=\""+anzahlGleise+"\">");
		String newLine = "\n"+indent+"  ";
		
		if(ende != null)
		{
			str.append(newLine+"<verbindung typ=\"anfang\">\n"+anfang.toXML(indent+"    ")+newLine+"</verbindung>");
		}
		
		if(ende != null)
		{
			str.append(newLine+"<verbindung typ=\"ende\">\n"+ende.toXML(indent+"    ")+newLine+"</verbindung>");
		}
		
		str.append("\n"+indent);
		
		str.append("</strecke>");
		return str.toString();
	}
}
