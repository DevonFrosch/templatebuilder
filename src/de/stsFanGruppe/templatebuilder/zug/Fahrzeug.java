package de.stsFanGruppe.templatebuilder.zug;

import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;

public class Fahrzeug
{
	protected String name;
	protected List<Fahrzeugteil> fahrzeugteile;
	
	public Fahrzeug(String name, List<Fahrzeugteil> fahrzeugteile)
	{
		this.name = name;
		this.fahrzeugteile = fahrzeugteile;
	}
	public Fahrzeug(String name)
	{
		this(name, new LinkedList<>());
	}
	
	/**
	 * Shortcut: Legt ein Fahrzeug mit einem neuen Fahrzeugteil an.
	 * <br>
	 * Zu den Formatierungen siehe {@link Fahrzeugteil#Fahrzeugteil(double, int) Konstruktor von Fahrzeugteil}
	 * 
	 * @param name Name des Fahrzeugs
	 * @param laenge Länge des Fahrzeugteils
	 * @param hoechstgeschwindigkeit Höchstgeschwindigkeit des Fahrzeugteils
	 */
	public Fahrzeug(String name, double laenge, int hoechstgeschwindigkeit)
	{
		this(name);
		this.fahrzeugteile.add(new Fahrzeugteil(laenge, hoechstgeschwindigkeit));
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public boolean hasFahrzeugteilee()
	{
		return !fahrzeugteile.isEmpty();
	}
	public List<Fahrzeugteil> getFahrzeugteilee()
	{
		return fahrzeugteile;
	}
	public void addFahrzeugteil(Fahrzeugteil fahrzeugteil)
	{
		this.fahrzeugteile.add(fahrzeugteil);
	}
	public void addFahrzeugteil(int index, Fahrzeugteil fahrzeugteil)
	{
		this.fahrzeugteile.add(index, fahrzeugteil);
	}
	public void removeFahrzeugteil(Fahrzeugteil fahrzeugteil)
	{
		this.fahrzeugteile.remove(fahrzeugteil);
	}
	
	public double getLaenge()
	{
		// Länge des Fahrzeugs = Summe der Längen der Teile
		return fahrzeugteile.stream().mapToDouble(Fahrzeugteil::getLaenge).sum();
	}
	/**
	 * Fragt die Höchstgeschwindigkeit ab.
	 * @return Die Höchstgeschwindigkeit oder eine leere Variable, wenn keine Fahrzeuge vorhanden sind
	 */
	public OptionalInt getHoechstgeschwindigkeit()
	{
		// Höchstgeschwindigkeit des Fahrzeugs = Minimum der Geschwindigkeiten der Teile
		return fahrzeugteile.stream().mapToInt(Fahrzeugteil::getHoechstgeschwindigkeit).min();
	}
	
	public String toString()
	{
		return "Fahrzeug "+getName()+" { "+fahrzeugteile.size()+" Fahrzeugteile }";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringBuilder str = new StringBuilder(indent+"<fahrzeug name=\""+getName()+"\">");
		
		if(!fahrzeugteile.isEmpty())
		{
			for(Fahrzeugteil fgt: fahrzeugteile)
			{
				str.append("\n");
				str.append(fgt.toXML(indent+"  "));
			}
			str.append("\n"+indent);
		}
		
		str.append("</fahrzeug>");
		return str.toString();
	}
}
