package de.stsFanGruppe.tools;

import java.awt.Graphics2D;
import java.util.Set;

public interface FahrtPaintable
{
	public Set<FahrtabschnittCalculatableLine> paint(Graphics2D g);
}
