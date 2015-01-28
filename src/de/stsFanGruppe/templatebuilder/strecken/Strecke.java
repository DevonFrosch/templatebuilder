package de.stsFanGruppe.templatebuilder.strecken;

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
	
	protected int anzahlGleise = 1;
	protected Streckengleis streckengleis0;
	protected Streckengleis streckengleis1;
	
	protected Strecke(String name, Betriebsstelle anfang, Betriebsstelle ende, int anzahlGleise,
			Streckengleis streckengleis0, Streckengleis streckengleis1)
	{
		this.name = name;
		this.anfang = anfang;
		this.ende = ende;
		this.anzahlGleise = anzahlGleise;
		this.streckengleis0 = streckengleis0;
		this.streckengleis1 = streckengleis1;
	}
	/**
	 * Erzeugt eine zweigleisige Strecke
	 * @param name Name der Strecke
	 * @param anfang
	 * @param ende
	 * @param streckengleis0
	 * @param streckengleis1
	 */
	public Strecke(String name, Betriebsstelle anfang, Betriebsstelle ende,
			Streckengleis streckengleis0, Streckengleis streckengleis1)
	{
		this(name, anfang, ende, 2, streckengleis0, streckengleis1);
	}
	/**
	 * Erzeugt eine eingleisige Strecke
	 * @param name
	 * @param anfang
	 * @param ende
	 * @param streckengleis
	 */
	public Strecke(String name, Betriebsstelle anfang, Betriebsstelle ende,
			Streckengleis streckengleis)
	{
		this(name, anfang, ende, 1, streckengleis, null);
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Betriebsstelle getAnfang()
	{
		return anfang;
	}
	public void setAnfang(Betriebsstelle anfang)
	{
		this.anfang = anfang;
	}
	public Betriebsstelle getEnde()
	{
		return ende;
	}
	public void setEnde(Betriebsstelle ende)
	{
		this.ende = ende;
	}
	public int getAnzahlGleise()
	{
		return anzahlGleise;
	}
	public Streckengleis getStreckengleis0()
	{
		return streckengleis0;
	}
	public void setStreckengleis0(Streckengleis streckengleis0)
	{
		this.streckengleis0 = streckengleis0;
	}
	public Streckengleis getStreckengleis1()
	{
		if(anzahlGleise > 1)
			return streckengleis1;
		return null;
	}
	public void setStreckengleis1(Streckengleis streckengleis1)
	{
		this.streckengleis1 = streckengleis1;
		this.anzahlGleise = 2;
	}
	public void removeStreckengleis1()
	{
		this.streckengleis1 = null;
		this.anzahlGleise = 1;
	}
}
