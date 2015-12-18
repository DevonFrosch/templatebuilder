package de.stsFanGruppe.templatebuilder.strecken;

import de.stsFanGruppe.tools.NullTester;

public class Fahrzeit
{
	protected String name;
	protected Streckenabschnitt streckenabschnitt;
	protected int tempo;
	protected double dauer;
	
	/**
	 * Legt eine neue Fahrzeit an
	 * @param name Name der Fahrzeit
	 * @param streckenabschnitt Streckenabschnitt, für die die Fahrtzeit gilt
	 * @param tempo Zugtempo, für das die Fahrtzeit gilt
	 * @param dauer Fahrzeit, die der Zug benötigt
	 */
	public Fahrzeit(String name, Streckenabschnitt streckenabschnitt, int tempo, int dauer)
	{
		this.setName(name);
		this.setStreckenabschnitt(streckenabschnitt);
		this.setTempo(tempo);
		this.setDauer(dauer);
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
	public Streckenabschnitt getStreckenabschnitt()
	{
		return streckenabschnitt;
	}
	protected void setStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		NullTester.test(streckenabschnitt);
		this.streckenabschnitt = streckenabschnitt;
	}
	public int getTempo()
	{
		return tempo;
	}
	public void setTempo(int tempo)
	{
		if(tempo < 0)
		{
			throw new IllegalArgumentException("Tempo muss größer gleich 0 sein");
		}
		this.tempo = tempo;
	}
	public double getDauer()
	{
		return dauer;
	}
	public void setDauer(double dauer)
	{
		if(dauer < 0)
		{
			throw new IllegalArgumentException("Dauer muss größer gleich 0 sein");
		}
		this.dauer = dauer;
	}
	
	public String toString()
	{
		return "Fahrzeit "+getName()+" { tempo: "+getTempo()+", dauer: "+getDauer()+", streckenabschnitt: "+streckenabschnitt.getName()+" }";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		return indent+"<fahrzeit name=\""+getName()+"\" tempo=\""+getTempo()+"\" dauer=\""+getDauer()+"\" streckenabschnitt=\""+streckenabschnitt.getName()+"\" />";
	}
}
