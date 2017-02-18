package de.stsFanGruppe.templatebuilder.zug;

import java.awt.Color;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype.LineType;

public class FahrtDarstellung
{
	protected String name;
	protected Color farbe;
	protected int breite;
	protected LineType typ;
	
	public FahrtDarstellung(String name, Color farbe, int breite, LineType typ)
	{
		this.name = name;
		this.farbe = farbe;
		this.breite = breite;
		this.typ = typ;
	}

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Color getFarbe()
	{
		return farbe;
	}
	public void setFarbe(Color farbe)
	{
		this.farbe = farbe;
	}
	public int getBreite()
	{
		return breite;
	}
	public void setBreite(int breite)
	{
		this.breite = breite;
	}
	public LineType getTyp()
	{
		return typ;
	}
	public void setTyp(LineType typ)
	{
		this.typ = typ;
	}
	
	public Object[] toArray()
	{
		return new Object[]{name, farbe, Integer.toString(breite), typ};
	}
}
