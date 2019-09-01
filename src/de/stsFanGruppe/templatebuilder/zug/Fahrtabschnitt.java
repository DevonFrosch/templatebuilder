package de.stsFanGruppe.templatebuilder.zug;

import de.stsFanGruppe.tools.NullTester;

public class Fahrtabschnitt
{
	protected Fahrplanhalt start;
	protected Fahrplanhalt ende;
	
	public Fahrtabschnitt(Fahrplanhalt start, Fahrplanhalt ende)
	{
		NullTester.test(start);
		NullTester.test(ende);
		
		if(start.getParent() != ende.getParent()) {
			throw new IllegalArgumentException("Fahrplanhalte start und ende gehören nicht zur gleichen Fahrt");
		}
		if(start.getParent() == null) {
			throw new NullPointerException("Fahrplanhalte haben keine zugehörige Fahrt");
		}
		
		this.start = start;
		this.ende = ende;
	}
	
	public Fahrplanhalt getStart()
	{
		return start;
	}
	
	public Fahrplanhalt getEnde()
	{
		return ende;
	}
	
	public Fahrt getFahrt()
	{
		return start.getParent();
	}
}
