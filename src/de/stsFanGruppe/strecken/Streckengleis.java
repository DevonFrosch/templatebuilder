package de.stsFanGruppe.strecken;

public class Streckengleis
{
	protected Typ typ;
	
	public Streckengleis(Typ typ)
	{
		this.typ = typ;
	}
	
	public Typ getTyp()
	{
		return typ;
	}
	public void setTyp(Typ typ)
	{
		this.typ = typ;
	}
	
	enum Typ
	{
		EINRICHTUNG,
		FALSCHFAHRBETRIEB,
		LINKSFAHRBETRIEB,
		GLEISWECHSELBETRIEB
	}
}
