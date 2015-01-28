package de.stsFanGruppe.strecken;

public class Streckengleis
{
	protected int typ;
	
	public Streckengleis(int typ)
	{
		this.typ = typ;
	}
	
	public int getTyp()
	{
		return typ;
	}
	public void setTyp(int typ)
	{
		this.typ = typ;
	}
	
	public static final int EINRICHTUNG = 0;
	public static final int FALSCHFAHRBETRIEB = 1;
	public static final int LINKSFAHRBETRIEB = 2;
	public static final int GLEISWECHSELBETRIEB = 3;
}
