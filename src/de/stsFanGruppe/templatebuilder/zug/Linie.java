package de.stsFanGruppe.templatebuilder.zug;

import de.stsFanGruppe.tools.NullTester;

public class Linie
{	
	protected String name;
	protected String nummer;
	
	public Linie(String name, String nummer)
	{
		this.setName(name);
		this.setNummer(nummer);
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
		NullTester.test(name);
		this.name = name;
	}
	public String getNummer()
	{
		return nummer;
	}
	public void setNummer(String nummer)
	{
		NullTester.test(nummer);
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
