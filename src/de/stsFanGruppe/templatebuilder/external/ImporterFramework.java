package de.stsFanGruppe.templatebuilder.external;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Set;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.templatebuilder.zug.Linie;

public class ImporterFramework
{
	private Importer importer;
	
	public ImporterFramework(Importer importer)
	{
		this.importer = importer;
	}
	
	public Streckenabschnitt importStreckenabschnitt(String file) throws FileNotFoundException, ImportException
	{
		InputStream input = new java.io.FileInputStream(file);
		return importer.importStreckenabschnitt(input);
		
	}
	public Set<Fahrt> importFahrten(String file, Streckenabschnitt streckenabschnitt, Linie linie) throws FileNotFoundException, ImportException
	{
		InputStream input = new java.io.FileInputStream(file);
		return importer.importFahrten(input, streckenabschnitt, linie);
	}
}
