package de.stsFanGruppe.templatebuilder.zug;

import java.util.*;
import de.stsFanGruppe.tools.NullTester;

public class Fahrt implements Comparable<Fahrt>
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Fahrt.class);
	
	protected static long nextId = 0;
	
	protected long fahrtId;
	protected String name;
	protected Linie linie;
	protected String templateName;
	protected String templateZid;
	protected NavigableSet<Fahrplanhalt> fahrplanhalte = new TreeSet<>(new Fahrplanhalt.StrictComparator());
	
	public Fahrt(String name, Linie linie)
	{
		this.fahrtId = nextId ++;
		this.setName(name);
		this.setLinie(linie);
	}
	public Fahrt(String name, Linie linie, String templateName, String templateZid)
	{
		this(name, linie);
		this.setTemplateName(templateName);
		this.setTemplateZid(templateZid);
	}

	public Fahrt(String name, Linie linie, Collection<? extends Fahrplanhalt> fahrplanhalte)
	{
		this(name, linie);
		this.addFahrplanhalte(fahrplanhalte);
	}
	public Fahrt(String name, Linie linie, String templateName, String templateZid, Collection<? extends Fahrplanhalt> fahrplanhalte)
	{
		this(name, linie, templateName, templateZid);
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
	
	public String getTemplateName()
	{
		return templateName;
	}
	
	public void setTemplateName(String templateName)
	{
		this.templateName = templateName;
	}
	
	public String getTemplateZid()
	{
		return templateZid;
	}
	
	public void setTemplateZid(String templateZid)
	{
		this.templateZid = templateZid;
	}
	
	public String getTemplateString()
	{
		if(getTemplateName() != null)
		{
			if(getTemplateZid() != null)
			{
				return getTemplateName() + " (" + getTemplateZid() + ")";
			}
			return getTemplateName();
		}
		if(getTemplateZid() != null)
		{
			return getTemplateZid();
		}
		return null;
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
	
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder("Fahrt " + getName() + " { Linie: " + getLinie());
		
		if(getTemplateName() != null)
		{
			stringBuilder.append(", Template: " + getTemplateName());
			
			if(getTemplateZid() != null)
			{
				stringBuilder.append(" (" + getTemplateZid() + ")");
			}
		}
		else if(getTemplateZid() != null)
		{
			stringBuilder.append(", Template-ZID: " + getTemplateZid());
		}
		
		stringBuilder.append(", " + fahrplanhalte.size() + " Fahrplanhalte }");
		return stringBuilder.toString();
	}
	
	public String toXML()
	{
		return toXML("");
	}
	
	public String toXML(String indent)
	{
		StringJoiner xml = new StringJoiner("\n");
		StringBuilder fahrt = new StringBuilder(indent + "<fahrt linie=\"" + linie.getName() + "\"");
		
		if(getTemplateName() != null)
		{
			fahrt.append(" data-template=\"" + getTemplateName() + "\"");
		}

		if(getTemplateZid() != null)
		{
			fahrt.append(" data-zid=\"" + getTemplateZid() + "\"");
		}
		
		xml.add(fahrt.append(">").toString());
		
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
