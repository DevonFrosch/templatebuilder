package de.stsFanGruppe.templatebuilder.hafas;

public class Coordinate
{
	public final double x;
	public final double y;
	
	public Coordinate(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public String toString()
	{
		return "("+x+", "+y+")";
	}
}
