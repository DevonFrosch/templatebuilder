package de.stsFanGruppe.tools;

import java.awt.geom.Line2D;

public class CalculatableLine
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CalculatableLine.class);
	
	private String id;
	private Line2D linie;
	
	public CalculatableLine(String id, int startX, int startY, int endeX, int endeY)
	{
		this.id = id;
		this.linie = new Line2D.Double(startX, startY, endeX, endeY);
	}
	
	public String getId()
	{
		return id;
	}
	
	public boolean isPunktAufLinie(int punktX, int punktY, int intervall)
	{
		return this.linie.intersects(punktX - intervall, punktY - intervall, 2 * intervall, 2 * intervall);
	}
}
