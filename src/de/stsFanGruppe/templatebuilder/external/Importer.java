package de.stsFanGruppe.templatebuilder.external;

import java.io.InputStream;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;

public interface Importer
{	
	public Streckenabschnitt importStreckenabschnitt(String name, InputStream input) throws ImportException;
}
