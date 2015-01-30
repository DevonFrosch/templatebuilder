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
	
	// Nicht null
	protected Streckengleis streckengleis0;
	// null === nur ein Gleis vorhanden
	protected Streckengleis streckengleis1;
	
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
		this.setName(name);
		this.setAnfang(anfang);
		this.setEnde(ende);
		this.setStreckengleis0(streckengleis0);
		this.setStreckengleis1(streckengleis1);
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
		this(name, anfang, ende, streckengleis, null);
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
		if(streckengleis0 == null)
		{
			return 1;
		}
		else
		{
			return 2;
		}
	}
	public Streckengleis getStreckengleis0()
	{
		return streckengleis0;
	}
	public void setStreckengleis0(Streckengleis streckengleis)
	{
		if(streckengleis == null)
		{
			throw new IllegalArgumentException();
		}
		this.streckengleis0 = streckengleis;
	}
	public Streckengleis getStreckengleis1()
	{
		return streckengleis1;
	}
	public void setStreckengleis1(Streckengleis streckengleis)
	{
		if(streckengleis == null)
		{
			this.removeStreckengleis1();
		}
		else
		{
			this.streckengleis1 = streckengleis;
		}
	}
	public void removeStreckengleis1()
	{
		this.streckengleis1 = null;
	}
	
	public String toString()
	{
		StringBuilder str = new StringBuilder("Strecke "+getName()+" { Anfang: ");
		
		str.append((anfang != null) ? anfang.getName() : "Undefiniert");
		str.append(", Ende: ");
		str.append((ende != null) ? ende.getName() : "Undefiniert");
		str.append(", Streckengleis Hin: "+streckengleis0.toString());
		
		if(getAnzahlGleise() > 1)
		{
			str.append(", Streckengleis Rück: "+streckengleis1.toString());
		}
		
		str.append(" }");
		return str.toString();
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringBuilder str = new StringBuilder(indent+"<strecke name=\""+getName()+"\">");
		String newLine = "\n"+indent+"  ";
		
		if(ende != null)
		{
			str.append(newLine+"<verbindung pos=\"0\" betriebsstelleName=\""+anfang.getName()+"\" />");
		}
		
		if(ende != null)
		{
			str.append(newLine+"<verbindung pos=\"1\" betriebsstelleName=\""+ende.getName()+"\" />");
		}

		str.append(newLine+streckengleis0.toXML("", 0));
		
		if(getAnzahlGleise() > 1)
		{
			str.append(newLine+streckengleis1.toXML("", 1));
		}
		
		str.append("\n"+indent);
		
		str.append("</strecke>");
		return str.toString();
	}
}
