package de.stsFanGruppe.templatebuilder.zug;

public class Fahrzeugtyp
{
	protected final String name;
	protected final double laenge;
	protected final int hoechstgeschwindigkeit;
	
	/**
	 * Legt einen neuen Fahrzeugtyp an
	 * @param name Name des Fahrzeugtyps
	 * @param laenge Länge des Fahrzeugs in Metern
	 * @param hoechstgeschwindigkeit Höchstgeschwindigkeit des Fahrzeugs in km/h
	 */
	public Fahrzeugtyp(String name, double laenge, int hoechstgeschwindigkeit)
	{
		this.name = name;
		this.laenge = laenge;
		this.hoechstgeschwindigkeit = hoechstgeschwindigkeit;
	}
	/**
	 * Legt einen neuen Fahrzeugtyp ohne Namen an
	 * @param laenge Länge des Fahrzeugs in Metern
	 * @param hoechstgeschwindigkeit Höchstgeschwindigkeit des Fahrzeugs in km/h
	 */
	public Fahrzeugtyp(double laenge, int hoechstgeschwindigkeit)
	{
		this(null, laenge, hoechstgeschwindigkeit);
	}
	
	/**
	 * @return Den Fahrzeugtypnamen oder null, wenn kein Name festgelegt wurde 
	 */
	public String getName()
	{
		return name;
	}
	public double getLaenge()
	{
		return laenge;
	}
	public int getHoechstgeschwindigkeit()
	{
		return hoechstgeschwindigkeit;
	}
}
