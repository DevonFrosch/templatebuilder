package de.stsFanGruppe.templatebuilder.test;

import de.stsFanGruppe.templatebuilder.zug.*;
import de.stsFanGruppe.templatebuilder.strecken.*;

public class TestDaten
{
	// Paket Zug
	public static Fahrt fahrt()
	{
		Fahrt f = new Fahrt("Testfahrt 1", new Linie("1"));
		Gleis g1 = new Gleis("A-Stadt", 0);
		Gleis g2 = new Gleis("B-Hausen", 1);
		Gleis g3 = new Gleis("C-Dorf", 2);
		f.addFahrplanhalt(new Fahrplanhalt(g1.getGleisabschnitte().first(), 0, 0, new FahrplanhaltEigenschaften()));
		f.addFahrplanhalt(new Fahrplanhalt(g2.getGleisabschnitte().first(), 10, 15, new FahrplanhaltEigenschaften()));
		f.addFahrplanhalt(new Fahrplanhalt(g3.getGleisabschnitte().first(), 25, 25, new FahrplanhaltEigenschaften()));
		return f;
	}
}
