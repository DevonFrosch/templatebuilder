package de.stsFanGruppe.templatebuilder.external;

import java.io.InputStream;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;

public interface Exporter
{	
	public Streckenabschnitt exportStreckenabschnitt(InputStream input) throws ExportException;
}
