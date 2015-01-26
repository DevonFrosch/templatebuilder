package de.stsFanGruppe.zug;

public class Linie
{	
	protected String name;
	protected String nummer;
	
	public Linie(String name, String nummer)
	{
		this.name = name;
		this.nummer = nummer;
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
}
