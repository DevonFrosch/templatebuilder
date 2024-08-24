package de.stsFanGruppe.templatebuilder.zug;

import java.util.Collection;
import java.util.HashSet;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.StringJoiner;

import de.stsFanGruppe.tools.XMLExportable;

public class Template implements Comparable<Template>, XMLExportable
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Template.class);
	
	protected String name = null;
	protected int tid = 0;
	
	protected Set<Fahrt> fahrten = new HashSet<>();
	
	public Template(String name, int tid, Collection<Fahrt> fahrten)
	{
		setName(name);
		setTid(tid);
		addFahrten(fahrten);
	}
	
	public Template(String name, int tid, Fahrt fahrt)
	{
		setName(name);
		setTid(tid);
		addFahrt(fahrt);
	}
	
	public Template(Collection<Fahrt> fahrten)
	{
		addFahrten(fahrten);
	}
	
	public Template(String name, int tid)
	{
		setName(name);
		setTid(tid);
	}
	
	public Template(String name)
	{
		setName(name);
	}
	
	public Template(int tid)
	{
		setTid(tid);
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		if(name == null || name.isEmpty())
		{
			this.name = null;
		}
		this.name = name;
	}
	
	public int getTid()
	{
		return tid;
	}
	
	public String getTidOrNull()
	{
		if(tid != 0)
		{
			return String.valueOf(tid);
		}
		return null;
	}
	
	public void setTid(int tid)
	{
		this.tid = tid;
	}
	
	public void setTid(String tid)
	{
		try
		{
			this.tid = Integer.parseInt(tid);
		}
		catch(NumberFormatException e)
		{
			this.tid = 0;
		}
	}
	
	public boolean hasFahrten()
	{
		return !fahrten.isEmpty();
	}
	
	public Set<Fahrt> getFahrten()
	{
		return fahrten;
	}
	
	public void addFahrten(Collection<Fahrt> fahrten)
	{
		this.fahrten.addAll(fahrten);
	}
	
	public void addFahrt(Fahrt fahrt)
	{
		fahrten.add(fahrt);
	}
	
	public Fahrt findFahrt(String name)
	{
		return fahrten.stream().filter((f) -> f.getName().equals(name)).findFirst().orElse(null);
	}
	
	public Fahrt findFahrt(long fahrtId)
	{
		return fahrten.stream().filter((f) -> f.getFahrtId() == fahrtId).findFirst().orElse(null);
	}
	
	public Fahrt getFirstFahrt(double minZeit)
	{
		Fahrt first = null;
		
		for(Fahrt fahrt : fahrten)
		{
			log.debug("Fahrt {}", fahrt);
			if(fahrt.getMaxZeit() < minZeit)
			{
				continue;
			}
			if(first == null || first.compareTo(fahrt) > 0)
			{
				log.debug("... ist früher");
				first = fahrt;
			}
		}
		
		return first;
	}
	
	public OptionalDouble getMinZeit()
	{
		if(!hasFahrten())
		{
			return OptionalDouble.empty();
		}
		
		return OptionalDouble
				.of(fahrten.stream().min((a, b) -> Double.compare(a.getMinZeit(), b.getMinZeit())).get().getMinZeit());
	}
	
	public OptionalDouble getMaxZeit()
	{
		if(!hasFahrten())
		{
			return OptionalDouble.empty();
		}
		
		return OptionalDouble
				.of(fahrten.stream().max((a, b) -> Double.compare(a.getMaxZeit(), b.getMaxZeit())).get().getMaxZeit());
	}
	
	public static String templateNameOf(String name, int tid)
	{
		String tidString = null;
		if(tid != 0)
		{
			return String.valueOf(tid);
		}
		return templateNameOf(name, tidString);
	}
	
	public static String templateNameOf(String name, String tid)
	{
		if(name != null)
		{
			if(tid != null)
			{
				return name + " (" + tid + ")";
			}
			return name;
		}
		if(tid != null)
		{
			return tid;
		}
		return "";
	}
	
	public String toString()
	{
		return templateNameOf(getName(), getTidOrNull());
	}
	
	public String toXML(String indent)
	{
		StringJoiner xml = new StringJoiner("\n");
		StringBuilder template = new StringBuilder();
		template.append("<template");
		
		if(tid != 0)
		{
			template.append(" tid=\"" + tid + "\"");
		}
		if(name != null)
		{
			template.append(" name=\"" + name + "\"");
		}
		
		template.append(">");
		xml.add(indent + template.toString());
		
		if(!fahrten.isEmpty())
		{
			for(Fahrt fahrt : fahrten)
			{
				xml.add(fahrt.toXML(indent + "  "));
			}
		}
		
		xml.add(indent + "  </template>");
		
		return xml.toString();
	}
	
	@Override
	public int compareTo(Template other)
	{
		return toString().compareToIgnoreCase(other.toString());
	}
	
}
