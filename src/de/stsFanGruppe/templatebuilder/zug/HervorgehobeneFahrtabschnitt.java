package de.stsFanGruppe.templatebuilder.zug;

import de.stsFanGruppe.tools.TimeFormater;

public class HervorgehobeneFahrtabschnitt {
	
	protected Fahrtabschnitt fahrtabschnitt;
	
	public HervorgehobeneFahrtabschnitt(Fahrtabschnitt fahrtabschnitt) 
	{
		this.fahrtabschnitt = fahrtabschnitt;
	}
	
	public Fahrtabschnitt getFahrtabschnitt()
	{
		return this.fahrtabschnitt;
	}
	
	public String toString() {
		Fahrt fahrt = fahrtabschnitt.getFahrt();
		String template = fahrt.getTemplate().toString();

		StringBuilder builder = new StringBuilder(fahrt.getName());
		
		if(!template.isEmpty())
		{
			builder.append(" [" + template + "]");
		}
		
		String abfahrt = TimeFormater.doubleToString(fahrtabschnitt.getStart().getMinZeit());
		builder.append(" (Abfahrt " + abfahrt + ")");
		
		return builder.toString();
	}
}
