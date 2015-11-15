package de.stsFanGruppe.templatebuilder.external;

import java.io.InputStream;
import java.util.Set;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;

public interface Importer
{	
	public Streckenabschnitt importStreckenabschnitt(InputStream input) throws ImportException;
	public Set<Fahrt> importFahrten(InputStream input) throws ImportException;
}
