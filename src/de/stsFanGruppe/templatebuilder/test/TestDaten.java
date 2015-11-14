package de.stsFanGruppe.templatebuilder.test;

import java.util.NavigableSet;
import java.util.TreeSet;
import de.stsFanGruppe.templatebuilder.zug.*;
import de.stsFanGruppe.templatebuilder.strecken.*;

public class TestDaten
{
	// Paket Strecke
	public static Gleis gleis()
	{
		return new Gleis("Testgleis 1");
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
	public static NavigableSet<Fahrplanhalt> fahrplanhalte()
	{
		NavigableSet<Fahrplanhalt> fp = new TreeSet<>();
		Gleis g1 = new Gleis("A-Stadt");
		Gleis g2 = new Gleis("B-Hausen");
		Gleis g3 = new Gleis("C-Dorf");
		fp.add(new Fahrplanhalt(g1.getGleisabschnitte().first(), 0, 0, eigenschaften()));
		fp.add(new Fahrplanhalt(g2.getGleisabschnitte().first(), 10, 15, eigenschaften()));
		fp.add(new Fahrplanhalt(g3.getGleisabschnitte().first(), 25, 25, eigenschaften()));
		return fp;
	}
	public static Fahrt fahrt()
	{
		return new Fahrt("Testfahrt 1", linie(), fahrplanhalte());
	}
}
