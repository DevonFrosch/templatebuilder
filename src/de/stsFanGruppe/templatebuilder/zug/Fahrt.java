package de.stsFanGruppe.templatebuilder.zug;

import java.util.NavigableSet;
import java.util.TreeSet;

public class Fahrt
{
	protected String name;
	protected Linie linie;
	protected NavigableSet<Fahrplanhalt> fahrplanhalte;

	public Fahrt(String name, Linie linie)
	{
		this.name = name;
		this.linie = linie;
		this.fahrplanhalte = new TreeSet<>();
	}
	public Fahrt(String name, Linie linie, NavigableSet<Fahrplanhalt> fahrplanhalte)
	{
		this(name, linie);
		fahrplanhalte.forEach((Fahrplanhalt f) -> this.addFahrplanhalt(f));
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
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
	public NavigableSet<Fahrplanhalt> getFahrplanhalte()
	{
		return fahrplanhalte;
	}
	public void addFahrplanhalt(Fahrplanhalt halt)
	{
		this.fahrplanhalte.add(halt);
		halt.setParent(this);
	}
	public void removeFahrplanhalt(Fahrplanhalt halt)
	{
		this.fahrplanhalte.remove(halt);
		if(halt.getParent() == this)
			halt.setParent(null);
		else
			System.out.println("Fahrplanhalt aus Fahrt "+getName()+" l�schen: bin nicht parent!");
	}
	
	public String toString()
	{
		return "Fahrt "+getName()+" { Linie: "+getLinie()+", "+fahrplanhalte.size()+" Fahrplanhalte }";
	}
	public String toXML()
	{
		return toXML("");
	}
	public String toXML(String indent)
	{
		StringBuilder str = new StringBuilder(indent+"<fahrt linie=\""+linie.getName()+"\">");
		String newLine = "\n"+indent+"  ";
		
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
