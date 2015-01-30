package de.stsFanGruppe.templatebuilder.zug;

public class Linie
{	
	protected String name;
	protected String nummer;
	
	public Linie(String name, String nummer)
	{
		this.name = name;
		this.nummer = nummer;
	}
	/**
	 * Legt eine neue Linie mit Namen = Nummer an
	 */
	public Linie(String nummer)
	{
		this(nummer, nummer);
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getNummer()
	{
		return nummer;
	}
	public void setNummer(String nummer)
	{
		this.nummer = nummer;
	}
	
	public String toString()
	{
		return getName();
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		return indent+"<linie name=\""+getName()+"\" nummer=\""+getNummer()+"\" />";
	}
}
