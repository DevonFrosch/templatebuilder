package de.stsFanGruppe.tools;

public interface XMLExportable
{
	public default String toXML() {
		return toXML("");
	}
	
	public String toXML(String indent);
}
