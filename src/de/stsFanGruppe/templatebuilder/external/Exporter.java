package de.stsFanGruppe.templatebuilder.external;

import java.io.OutputStream;
import java.util.Set;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;

public class Exporter
{
	public void exportStreckenabschnitt(OutputStream output, Streckenabschnitt streckenabschnitt) throws ExportException
	{
		throw new ExportException("Nicht implementiert");
	}
	public void exportFahrten(OutputStream output, Streckenabschnitt streckenabschnitt, Set<Fahrt> fahrten) throws ExportException
	{
		throw new ExportException("Nicht implementiert");
	}
}
