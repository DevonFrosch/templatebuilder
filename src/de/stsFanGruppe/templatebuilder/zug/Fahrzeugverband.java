package de.stsFanGruppe.templatebuilder.zug;

import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;

public class Fahrzeugverband
{
	protected String name;
	protected List<Fahrzeugtyp> fahrzeuge;
	
	public Fahrzeugverband(String name, List<Fahrzeugtyp> fahrzeuge)
	{
		this.name = name;
		this.fahrzeuge = fahrzeuge;
	}
	public Fahrzeugverband(String name)
	{
		this(name, new LinkedList<>());
	}
	
	/**
	 * Shortcut: Legt einen Fahrzeugverband mit einem neuen Fahrzeug an.
	 * <br>
	 * Zu den Formatierungen siehe {@link Fahrzeugtyp#Fahrzeugtyp(double, int) Konstruktor von Fahrzeugtyp}
	 * 
	 * @param name Name des Fahrzeugverbands
	 * @param laenge Länge des Fahrzeugs
	 * @param hoechstgeschwindigkeit Höchstgeschwindigkeit des Fahrzeugs
	 */
	public Fahrzeugverband(String name, double laenge, int hoechstgeschwindigkeit)
	{
		this(name);
		this.fahrzeuge.add(new Fahrzeugtyp(laenge, hoechstgeschwindigkeit));
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public boolean hasFahrzeuge()
	{
		return !fahrzeuge.isEmpty();
	}
	public List<Fahrzeugtyp> getFahrzeuge()
	{
		return fahrzeuge;
	}
	public void addFahrzeug(Fahrzeugtyp fahrzeug)
	{
		this.fahrzeuge.add(fahrzeug);
	}
	public void addFahrzeug(int index, Fahrzeugtyp fahrzeug)
	{
		this.fahrzeuge.add(index, fahrzeug);
	}
	public void removeFahrzeug(Fahrzeugtyp fahrzeug)
	{
		this.fahrzeuge.remove(fahrzeug);
	}
	
	public double getLaenge()
	{
		// Länge des Verbandes = Summe der Längen der Fahrzeuge
		return fahrzeuge.stream().mapToDouble(Fahrzeugtyp::getLaenge).sum();
	}
	/**
	 * Fragt die Höchstgeschwindigkeit ab.
	 * @return Die Höchstgeschwindigkeit oder eine leere Variable, wenn keine Fahrzeuge vorhanden sind
	 */
	public OptionalInt getHoechstgeschwindigkeit()
	{
		// Höchstgeschwindigkeit des Verbands = Minimum der Geschwindigkeiten der Fahrzeuge
		return fahrzeuge.stream().mapToInt(Fahrzeugtyp::getHoechstgeschwindigkeit).min();
	}
}
