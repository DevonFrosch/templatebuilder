package de.stsFanGruppe.templatebuilder.external;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;

public interface Exporter
{	
	public void exportStreckenabschnitt(OutputStream input, Streckenabschnitt streckenabschnitt) throws ExportException;
	public void exportFahrten(InputStream input, Set<Fahrt> fahrten) throws ImportException;
}
