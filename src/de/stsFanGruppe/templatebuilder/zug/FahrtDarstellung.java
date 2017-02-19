package de.stsFanGruppe.templatebuilder.zug;

import java.awt.Color;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.filter.FahrtFilter;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype.LineType;

public class FahrtDarstellung
{
	protected FahrtFilter filter;
	protected String muster;
	protected Color farbe;
	protected int breite;
	protected LineType typ;
	
	public FahrtDarstellung(FahrtFilter filter, String muster, Color farbe, int breite, LineType typ)
	{
		this.filter = filter;
		this.muster = muster;
		this.farbe = farbe;
		this.breite = breite;
		this.typ = typ;
	}
	
	public FahrtFilter getFilter()
	{
		return filter;
	}
	public void setFilter(FahrtFilter filter)
	{
		this.filter = filter;
	}
	public String getMuster()
	{
		return muster;
	}
	public void setMuster(String muster)
	{
		this.muster = muster;
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
		return new Object[]{filter, muster, farbe, Integer.toString(breite), typ};
	}
}
