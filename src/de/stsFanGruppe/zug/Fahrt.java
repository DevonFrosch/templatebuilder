package de.stsFanGruppe.zug;

import java.util.List;
import java.util.LinkedList;

public class Fahrt
{
	protected String name;
	protected Fahrzeugverband fahrzeugverband;
	protected Linie linie;
	protected List<Fahrplanhalt> fahrplanhalte;
	
	public Fahrt(String name, Fahrzeugverband fahrzeugverband, Linie linie, List<Fahrplanhalt> fahrplanhalte)
	{
		this.name = name;
		this.fahrzeugverband = fahrzeugverband;
		this.linie = linie;
		this.fahrplanhalte = fahrplanhalte;
	}
	public Fahrt(String name, Fahrzeugverband fahrzeugverband, Linie linie)
	{
		this(name, fahrzeugverband, linie, new LinkedList<>());
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Fahrzeugverband getFahrzeugverband()
	{
		return fahrzeugverband;
	}
	public void setFahrzeugverband(Fahrzeugverband fahrzeugverband)
	{
		this.fahrzeugverband = fahrzeugverband;
	}
	public Linie getLinie()
	{
		return linie;
	}
	public void setLinie(Linie linie)
	{
		this.linie = linie;
	}
	public boolean hasFahrplanhalte()
	{
		return !fahrplanhalte.isEmpty();
	}
	public List<Fahrplanhalt> getFahrplanhalte()
	{
		return fahrplanhalte;
	}
	public void addFahrplanhalt(Fahrplanhalt halt)
	{
		this.fahrplanhalte.add(halt);
	}
	public void addFahrplanhalt(int index, Fahrplanhalt halt)
	{
		this.fahrplanhalte.add(index, halt);
	}
	public void removeFahrplanhalt(Fahrplanhalt halt)
	{
		this.fahrplanhalte.remove(halt);
	}
}
