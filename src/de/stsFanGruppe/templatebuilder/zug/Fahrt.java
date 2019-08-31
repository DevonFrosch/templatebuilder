package de.stsFanGruppe.templatebuilder.zug;

import java.util.*;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.XMLExportable;

public class Fahrt implements Comparable<Fahrt>, XMLExportable
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Fahrt.class);
	
	protected static long nextId = 0;
	
	protected long fahrtId;
	protected String name = null;
	protected Linie linie = null;
	protected String templateName = null;
	protected String templateTid = null;
	protected Fahrt vorgaenger = null;
	protected Fahrt nachfolger = null;
	protected NavigableSet<Fahrplanhalt> fahrplanhalte = new TreeSet<>(new Fahrplanhalt.StrictComparator());
	
	public Fahrt(String name, Linie linie)
	{
		this.fahrtId = nextId ++;
		this.setName(name);
		this.setLinie(linie);
	}
	public Fahrt(String name, Linie linie, String templateName, String templateTid)
	{
		this(name, linie);
		this.setTemplateName(templateName);
		this.setTemplateTid(templateTid);
	}

	public Fahrt(String name, Linie linie, Collection<? extends Fahrplanhalt> fahrplanhalte)
	{
		this(name, linie);
		this.addFahrplanhalte(fahrplanhalte);
	}
	public Fahrt(String name, Linie linie, String templateName, String templateTid, Collection<? extends Fahrplanhalt> fahrplanhalte)
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
	
	public String getTemplateName()
	{
		return templateName;
	}
	
	public void setTemplateName(String templateName)
	{
		this.templateName = templateName;
	}
	
	public String getTemplateTid()
	{
		return templateTid;
	}
	
	public void setTemplateTid(String templateTid)
	{
		this.templateTid = templateTid;
	}
	
	public String getTemplateString()
	{
		if(getTemplateName() != null)
		{
			if(getTemplateTid() != null)
			{
				return getTemplateName() + " (" + getTemplateTid() + ")";
			}
			return getTemplateName();
		}
		if(getTemplateTid() != null)
		{
			return getTemplateTid();
		}
		return null;
	}
	
	public Fahrt getVorgaenger()
	{
		return this.vorgaenger;
	}
	
	public void setVorgaenger(Fahrt vorgaenger)
	{
		this.vorgaenger = vorgaenger;
	}
	
	public Fahrt getNachfolger()
	{
		return this.nachfolger;
	}
	
	public void setNachfolger(Fahrt nachfolger)
	{
		this.nachfolger = nachfolger;
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
			
			if(getTemplateTid() != null)
			{
				stringBuilder.append(" (" + getTemplateTid() + ")");
			}
		}
		else if(getTemplateTid() != null)
		{
			stringBuilder.append(", TID: " + getTemplateTid());
		}
		
		stringBuilder.append(", " + fahrplanhalte.size() + " Fahrplanhalte }");
		return stringBuilder.toString();
	}
	
	public String toXML(String indent)
	{
		StringJoiner xml = new StringJoiner("\n");
		StringBuilder fahrt = new StringBuilder(indent + "<fahrt linie=\"" + linie.getName() + "\"");
		
		if(getTemplateName() != null)
		{
			fahrt.append(" data-template=\"" + getTemplateName() + "\"");
		}

		if(getTemplateTid() != null)
		{
			fahrt.append(" data-tid=\"" + getTemplateTid() + "\"");
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
