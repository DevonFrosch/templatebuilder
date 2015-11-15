package de.stsFanGruppe.templatebuilder.external;

import java.io.InputStream;
import java.util.Set;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.templatebuilder.zug.Linie;

public interface Importer
{	
	public Streckenabschnitt importStreckenabschnitt(InputStream input) throws ImportException;
	public Set<Fahrt> importFahrten(InputStream input, Streckenabschnitt streckenabschnitt, Linie linie) throws ImportException;
}
