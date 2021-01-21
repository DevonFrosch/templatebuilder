package de.stsFanGruppe.tools;

import java.awt.geom.Line2D;
import de.stsFanGruppe.templatebuilder.zug.Fahrtabschnitt;

public class FahrtabschnittCalculatableLine extends CalculatableLine
{
	protected Fahrtabschnitt fahrtabschnitt;
	
	public FahrtabschnittCalculatableLine(Fahrtabschnitt start, Line2D linie)
	{
		super(linie);
		this.fahrtabschnitt = start;
	}
	
	public FahrtabschnittCalculatableLine(Fahrtabschnitt start, int startX, int startY, int endeX, int endeY)
	{
		super(startX, startY, endeX, endeY);
		this.fahrtabschnitt = start;
	}
	
	public Fahrtabschnitt getFahrtabschnitt()
	{
		return fahrtabschnitt;
	}
}
