package de.stsFanGruppe.templatebuilder.test;

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
	public static Fahrt fahrt()
	{
		Fahrt f = new Fahrt("Testfahrt 1", new Linie("1"));
		Gleis g1 = new Gleis("A-Stadt");
		Gleis g2 = new Gleis("B-Hausen");
		Gleis g3 = new Gleis("C-Dorf");
		f.addFahrplanhalt(new Fahrplanhalt(g1.getGleisabschnitte().first(), 0, 0, eigenschaften()));
		f.addFahrplanhalt(new Fahrplanhalt(g2.getGleisabschnitte().first(), 10, 15, eigenschaften()));
		f.addFahrplanhalt(new Fahrplanhalt(g3.getGleisabschnitte().first(), 25, 25, eigenschaften()));
		return f;
	}
}
