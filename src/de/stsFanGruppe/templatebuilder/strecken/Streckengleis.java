package de.stsFanGruppe.templatebuilder.strecken;

public class Streckengleis
{
	protected int typ;
	
	public static final int EINRICHTUNG = 0;
	public static final int FALSCHFAHRBETRIEB = 1;
	public static final int LINKSFAHRBETRIEB = 2;
	public static final int GLEISWECHSELBETRIEB = 3;
	
	public Streckengleis(int typ)
	{
		this.typ = typ;
	}
	public Streckengleis()
	{
		this(GLEISWECHSELBETRIEB);
	}
	
	public int getTyp()
	{
		return typ;
	}
	public String getTypString()
	{
		switch(typ)
		{
			case EINRICHTUNG:
				return "Einrichtung";
			case FALSCHFAHRBETRIEB:
				return "Falschfahrbetrieb";
			case LINKSFAHRBETRIEB:
				return "Linksfahrbetrieb";
			case GLEISWECHSELBETRIEB:
				return "Gleiswechselbetrieb";
			default:
		}
		return "Undefiniert";
	}
	public void setTyp(int typ)
	{
		this.typ = typ;
	}
	
	public String toString()
	{
		return "Streckengleis { "+getTypString()+" }";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		return "<streckengleis typ=\""+getTypString()+"\" />";
	}
	public String toXML(String indent, int nummer)
	{
		return "<streckengleis nummer=\""+nummer+"\" typ=\""+getTypString()+"\" />";
	}
}
