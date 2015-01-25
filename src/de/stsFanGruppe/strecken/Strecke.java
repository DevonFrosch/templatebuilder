package de.stsFanGruppe.strecken;

public class Strecke
{	
	String name;
	Betriebsstelle anfang;
	Betriebsstelle ende;
	
	int anzahlGleise = 1;
	Typ typ0;
	Typ typ1;
	
	enum Typ
	{
		EINRICHTUNG,
		FALSCHFAHRBETRIEB,
		LINKSFAHRBETRIEB,
		GLEISWECHSELBETRIEB
	}
}
