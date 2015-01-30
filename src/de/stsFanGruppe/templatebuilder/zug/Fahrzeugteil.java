package de.stsFanGruppe.templatebuilder.zug;

public class Fahrzeugteil
{
	protected final String name;
	protected final double laenge;
	protected final int hoechstgeschwindigkeit;
	
	/**
	 * Legt einen neuen Fahrzeugteil an
	 * @param name Name des Fahrzeugteils
	 * @param laenge Länge des Fahrzeugteils in Metern
	 * @param hoechstgeschwindigkeit Höchstgeschwindigkeit des Fahrzeugteils in km/h
	 */
	public Fahrzeugteil(String name, double laenge, int hoechstgeschwindigkeit)
	{
		this.name = name;
		this.laenge = laenge;
		this.hoechstgeschwindigkeit = hoechstgeschwindigkeit;
	}
	/**
	 * Legt einen neuen Fahrzeugteil ohne Namen an
	 * @param laenge Länge des Fahrzeugteils in Metern
	 * @param hoechstgeschwindigkeit Höchstgeschwindigkeit des Fahrzeugteils in km/h
	 */
	public Fahrzeugteil(double laenge, int hoechstgeschwindigkeit)
	{
		this(null, laenge, hoechstgeschwindigkeit);
	}
	
	/**
	 * @return Den Namen des Fahrzeugteils oder null, wenn kein Name festgelegt wurde 
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
	
	public String toString()
	{
		StringBuilder str = new StringBuilder("Fahrzeugteil ");
		
		if(getName() != null)
		{
			str.append(getName()+" ");
		}
		
		str.append("{ Länge: "+getLaenge()+", Höchstgeschwindigkeit: "+getHoechstgeschwindigkeit()+" }");
		return str.toString();
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringBuilder str = new StringBuilder("<fahrzeugteil ");
		
		if(getName() != null)
		{
			str.append("name=\""+getName()+"\" ");
		}
		
		str.append("laenge=\""+getLaenge()+"\" hoechstgeschwindigkeit=\""+getHoechstgeschwindigkeit()+"\" />");
		return str.toString();
	}
}
