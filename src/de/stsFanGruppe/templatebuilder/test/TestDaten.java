package de.stsFanGruppe.templatebuilder.test;

import java.util.NavigableSet;
import java.util.TreeSet;
import de.stsFanGruppe.templatebuilder.zug.*;
import de.stsFanGruppe.templatebuilder.strecken.*;

public class TestDaten
{
	// Paket Strecke
	public static Gleisabschnitt gleisabschnitt()
	{
		return new Gleisabschnitt("Testgleisabschnitt 1.1", 0, 0.5);
	}
	public static NavigableSet<Gleisabschnitt> gleisabschnitte()
	{
		NavigableSet<Gleisabschnitt> ga = new TreeSet<>();
		ga.add(gleisabschnitt());
		return ga;
	}
	public static Gleis gleis()
	{
		return new Gleis("Testgleis 1", gleisabschnitte());
	}
	public static FahrplanhaltEigenschaften eigenschaften()
	{
		return new FahrplanhaltEigenschaften(false, false, null);
	}
	
	// Paket Zug
	public static Linie linie()
	{
		return new Linie("1");
	}
	public static Fahrplanhalt fahrplanhalt()
	{
		return new Fahrplanhalt(gleisabschnitt(), 5.0, 5.1, eigenschaften());
	}
	public static NavigableSet<Fahrplanhalt> fahrplanhalte()
	{
		NavigableSet<Fahrplanhalt> fp = new TreeSet<>();
		fp.add(fahrplanhalt());
		return fp;
	}
	public static Fahrt fahrt()
	{
		return new Fahrt("Testfahrt 1", linie(), fahrplanhalte());
	}
}
