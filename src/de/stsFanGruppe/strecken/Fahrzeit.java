package de.stsFanGruppe.strecken;

public class Fahrzeit
{
	protected String name;
	protected final Streckenabschnitt streckenabschnitt;
	protected int tempo;
	protected int dauer;
	
	/**
	 * Legt eine neue Fahrzeit an
	 * @param name Name der Fahrzeit
	 * @param streckenabschnitt Streckenabschnitt, f�r die die Fahrtzeit gilt (kann nicht mehr ver�ndert werden)
	 * @param tempo Zugtempo, f�r das die Fahrtzeit gilt
	 * @param dauer Fahrzeit, die der Zug ben�tigt
	 */
	public Fahrzeit(String name, Streckenabschnitt streckenabschnitt, int tempo, int dauer)
	{
		this.name = name;
		this.streckenabschnitt = streckenabschnitt;
		this.tempo = tempo;
		this.dauer = dauer;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Streckenabschnitt getStreckenabschnitt()
	{
		return streckenabschnitt;
	}
	public int getTempo()
	{
		return tempo;
	}
	public void setTempo(int tempo)
	{
		this.tempo = tempo;
	}
	public int getDauer()
	{
		return dauer;
	}
	public void setDauer(int dauer)
	{
		this.dauer = dauer;
	}
}
