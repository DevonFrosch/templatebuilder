package de.stsFanGruppe.templatebuilder.zug;

import java.util.*;
import de.stsFanGruppe.templatebuilder.strecken.Gleisabschnitt;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.XMLExportable;

public class Fahrt implements Comparable<Fahrt>, XMLExportable
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Fahrt.class);
	
	protected static long nextId = 0;
	
	protected long fahrtId;
	protected String name = null;
	protected Linie linie = null;
	protected Template template = null;
	protected NavigableSet<Fahrplanhalt> fahrplanhalte = new TreeSet<>(new Fahrplanhalt.StrictComparator());
	
	public Fahrt(String name, Linie linie)
	{
		fahrtId = nextId ++;
		setName(name);
		setLinie(linie);
	}
	public Fahrt(String name, Linie linie, Template template) {
		this(name, linie);
		setTemplate(template);
	}
	public Fahrt(String name, Linie linie, String templateName, int templateTid)
	{
		this(name, linie);
		setTemplate(new Template(templateName, templateTid));
	}

	public Fahrt(String name, Linie linie, Collection<? extends Fahrplanhalt> fahrplanhalte)
	{
		this(name, linie);
		this.addFahrplanhalte(fahrplanhalte);
	}
	public Fahrt(String name, Linie linie, String templateName, int templateTid, Collection<? extends Fahrplanhalt> fahrplanhalte)
	{
		this(name, linie, templateName, templateTid);
		this.addFahrplanhalte(fahrplanhalte);
	}
	
	public long getFahrtId()
	{
		return fahrtId;
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
	
	public Linie getLinie()
	{
		return linie;
	}
	
	public void setLinie(Linie linie)
	{
		NullTester.test(linie);
		this.linie = linie;
	}
	
	public Template getTemplate() {
		return template;
	}
	public void setTemplate(Template template) {
		this.template = template;
	}
	
	public boolean hasFahrplanhalte()
	{
		return !fahrplanhalte.isEmpty();
	}
	
	public FirstLastLinkedList<Fahrplanhalt> getFahrplanhalte()
	{
		return new FirstLastLinkedList<>(fahrplanhalte);
	}
	
	public Fahrplanhalt getFahrplanhalt(Gleisabschnitt gleisabschnitt)
	{
		return fahrplanhalte.stream()
				.filter(fh -> fh.getGleisabschnitt() == gleisabschnitt)
				.findFirst()
				.orElse(null);
	}
	
	public Fahrplanhalt getFahrplanhalt(int index)
	{
		return getFahrplanhalte().get(index);
	}
	
	public void addFahrplanhalt(Fahrplanhalt halt)
	{
		NullTester.test(halt);
		this.fahrplanhalte.add(halt);
		if(halt.getParent() != null)
		{
			log.error("addFahrplanhalt: Fahrplanhalt hat schon parent: " + halt.getParent().getName());
			throw new IllegalStateException("Fahrplanhalt wird schon verwendet!");
		}
		halt.setParent(this);
	}
	
	protected void addFahrplanhalte(Collection<? extends Fahrplanhalt> fahrplanhalte)
	{
		NullTester.test(fahrplanhalte);
		fahrplanhalte.forEach((Fahrplanhalt f) -> this.addFahrplanhalt(f));
	}
	
	public boolean removeFahrplanhalt(Fahrplanhalt halt)
	{
		NullTester.test(halt);
		if(!fahrplanhalte.contains(halt))
		{
			return false;
		}
		
		if(halt.getParent() == this)
		{
			halt.setParent(null);
		}
		else
		{
			log.error("Fahrplanhalt bei {} aus Fahrt {} löschen: bin nicht parent!", halt.getGleisabschnitt().getName(), getName());
			throw new IllegalStateException("Fahrplanhalte asynchron!");
		}
		return this.fahrplanhalte.remove(halt);
	}
	
	public double getMinZeit()
	{
		return fahrplanhalte.stream().min((a, b) -> Double.compare(a.getMinZeit(), b.getMinZeit())).get().getMinZeit();
	}
	
	public double getMaxZeit()
	{
		return fahrplanhalte.stream().max((a, b) -> Double.compare(a.getMaxZeit(), b.getMaxZeit())).get().getMaxZeit();
	}
	
	public boolean hasAbschnitteRichtung(boolean richtungAufsteigend)
	{
		if(fahrplanhalte.size() < 2)
		{
			return false;
		}
		double letzterKm = -1;
		
		for(Fahrplanhalt halt : fahrplanhalte)
		{
			if(letzterKm == -1)
			{
				letzterKm = halt.getGleisabschnitt().getKm();
				continue;
			}
			
			if(richtungAufsteigend && halt.getGleisabschnitt().getKm() > letzterKm)
			{
				return true;
			}
			if(!richtungAufsteigend && halt.getGleisabschnitt().getKm() < letzterKm)
			{
				return true;
			}
			
			halt.getGleisabschnitt().getKm();
		}
		
		return false;
	}
	
	public boolean isSichtbar()
	{
		return fahrplanhalte.size() > 1;
	}
	
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder("Fahrt " + getName() + " { Linie: " + getLinie());
		stringBuilder.append(", Template: \"" + template.toString() + "\"");
		stringBuilder.append(", " + fahrplanhalte.size() + " Fahrplanhalte }");
		return stringBuilder.toString();
	}
	
	public String toXML(String indent)
	{
		StringJoiner xml = new StringJoiner("\n");
		String fahrt = indent + "<fahrt linie=\"" + linie.getName() + "\" template=\"" + template.toString() + "\">";
		
		xml.add(fahrt);
		
		if(!fahrplanhalte.isEmpty())
		{
			xml.add(indent + "  <fahrplanhalte>");
			
			for(Fahrplanhalt fh : fahrplanhalte)
			{
				xml.add(fh.toXML(indent + "  " + "  "));
			}
			
			xml.add(indent + "  </fahrplanhalte>");
		}
		else
		{
			xml.add(indent + "  <fahrplanhalte />");
		}
		
		xml.add(indent + "</fahrt>");
		return xml.toString();
	}
	
	public int compareTo(Fahrt other)
	{
		return this.fahrplanhalte.first().compareTo(other.fahrplanhalte.first());
	}
}
