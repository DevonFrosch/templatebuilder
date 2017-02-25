package de.stsFanGruppe.templatebuilder.external;

import java.io.InputStream;
import java.util.Set;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.templatebuilder.zug.Linie;

public class Importer
{
	public Streckenabschnitt importStreckenabschnitt(InputStream input) throws ImportException
	{
		throw new ImportException("Nicht implementiert");
	}
	
	public Set<Fahrt> importFahrten(InputStream input, Streckenabschnitt streckenabschnitt, Linie linie) throws ImportException
	{
		throw new ImportException("Nicht implementiert");
	}
	
	public void importRegeln(InputStream input) throws ImportException
	{
		throw new ImportException("Nicht implementiert");
	}
	
	protected static String makeName(Betriebsstelle anfang, Betriebsstelle ende)
	{
		assert anfang != null;
		assert ende != null;
		
		// Anfang - Ende
		return anfang.getName() + " - " + ende.getName();
	}
	
	protected static boolean isEmpty(String str)
	{
		return str == null || str.isEmpty();
	}
}
