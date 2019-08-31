package de.stsFanGruppe.templatebuilder.zug;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import de.stsFanGruppe.tools.XMLExportable;

public class Template implements Comparable<Template>, XMLExportable {
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Template.class);
	
	protected String name = null;
	protected int tid = 0;
	
	protected Set<Fahrt> fahrten = new HashSet<>();
	
	public Template(String name, int tid, Collection<Fahrt> fahrten) {
		setName(name);
		setTid(tid);
		addFahrten(fahrten);
	}
	public Template(Collection<Fahrt> fahrten) {
		addFahrten(fahrten);
	}
	public Template(String name, int tid) {
		setName(name);
		setTid(tid);
	}
	public Template(String name) {
		setName(name);
	}
	public Template(int tid) {
		setTid(tid);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(name == null || name.isEmpty()) {
			this.name = null;
		}
		this.name = name;
	}
	
	public int getTid() {
		return tid;
	}
	public String getTidOrNull() {
		if(tid != 0) {
			return String.valueOf(tid);
		}
		return null;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public void setTid(String tid) {
		try {
			this.tid = Integer.parseInt(tid); 
		}
		catch(NumberFormatException e) {
			this.tid = 0;
		}
	}
	
	public void addFahrten(Collection<Fahrt> fahrten) {
		this.fahrten.addAll(fahrten);
	}
	public void addFahrt(Fahrt fahrt) {
		fahrten.add(fahrt);
	}
	public void removeFahrt(Fahrt fahrt) {
		fahrten.remove(fahrt);
	}
	public void clearFahrten() {
		fahrten.clear();
	}
	
	public String toString()
	{
		if(name != null)
		{
			if(tid != 0)
			{
				return name + " (" + tid + ")";
			}
			return name;
		}
		if(tid != 0)
		{
			return String.valueOf(tid);
		}
		return "";
	}
	
	public String toXML(String indent) {
		StringJoiner xml = new StringJoiner("\n");
		StringBuilder template = new StringBuilder();
		template.append("<template");

		if(tid != 0) {
			template.append(" tid=\"" + tid + "\"");
		}
		if(name != null) {
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
	public int compareTo(Template other) {
		if(tid != 0) {
			return Integer.compare(this.tid, other.tid);
		}
		
		if(name == null) {
			return other.name == null ? 0 : -1;
		}
		
		return name.compareTo(other.name);
	}
	
}
