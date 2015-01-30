package de.stsFanGruppe.templatebuilder.zug;

import java.util.List;
import java.util.LinkedList;

public class Fahrt
{
	protected String name;
	protected Fahrzeug fahrzeug;
	protected Linie linie;
	protected List<Fahrplanhalt> fahrplanhalte;
	
	public Fahrt(String name, Fahrzeug fahrzeug, Linie linie, List<Fahrplanhalt> fahrplanhalte)
	{
		this.name = name;
		this.fahrzeug = fahrzeug;
		this.linie = linie;
		this.fahrplanhalte = fahrplanhalte;
	}
	public Fahrt(String name, Fahrzeug fahrzeug, Linie linie)
	{
		this(name, fahrzeug, linie, new LinkedList<>());
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Fahrzeug getFahrzeug()
	{
		return fahrzeug;
	}
	public void setFahrzeug(Fahrzeug fahrzeug)
	{
		this.fahrzeug = fahrzeug;
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
	
	public String toString()
	{
		return "Fahrt "+getName()+" { Linie: "+getLinie()+", "+fahrplanhalte.size()+" Fahrplanhalte, "+fahrzeug+" }";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringBuilder str = new StringBuilder(indent+"<fahrt linie=\""+linie.getName()+"\">");
		String newLine = "\n"+indent+"  ";
		
		if(fahrzeug != null)
		{
			str.append(newLine+fahrzeug.toXML());
		}
		
		if(!fahrplanhalte.isEmpty())
		{
			str.append(newLine+"<fahrplanhalte>");
			
			for(Fahrplanhalt fh: fahrplanhalte)
			{
				str.append("\n");
				str.append(fh.toXML(indent+"  "+"  "));
			}
			
			str.append(newLine+"</fahrplanhalte>");
		}
		else
		{
			str.append(newLine+"<fahrplanhalte />");
		}
		
		str.append("\n"+indent);
		str.append("</fahrt>");
		return str.toString();
	}
}
