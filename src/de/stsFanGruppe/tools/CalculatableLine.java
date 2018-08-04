package de.stsFanGruppe.tools;

import java.awt.geom.Line2D;

public class CalculatableLine
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CalculatableLine.class);
	
	private String name;
	private Line2D linie;
	
	public CalculatableLine(String name, int startX, int startY, int endeX, int endeY)
	{
		this.name = name;
		this.linie = new Line2D.Double(startX, startY, endeX, endeY);
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isPunktAufLinie(int punktX, int punktY, int intervall)
	{
		return this.linie.intersects(punktX - intervall, punktY - intervall, 2 * intervall, 2 * intervall);
	}
}
