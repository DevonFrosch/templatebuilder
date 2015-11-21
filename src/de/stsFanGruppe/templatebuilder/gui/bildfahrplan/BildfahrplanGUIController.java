package de.stsFanGruppe.templatebuilder.gui.bildfahrplan;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Set;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.external.ImportException;
import de.stsFanGruppe.templatebuilder.external.jtraingraph.JTrainGraphImporter;
import de.stsFanGruppe.templatebuilder.gui.external.JTrainGraphImportGUI;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.NullTester;

public class BildfahrplanGUIController
{
	private BildfahrplanGUI gui = null;
	
	public BildfahrplanGUIController()
	{
		
	}
	
	public void setBildfahrplanGUI(BildfahrplanGUI gui)
	{
		NullTester.test(gui);
		this.gui = gui;
	}
	
	public void setPanelSize()
	{
		NullTester.test(gui);
		
		BildfahrplanConfig config = gui.config;
		Dimension size = gui.getMinimumSize();
		size.setSize(size.getWidth(), config.getPanelHeight());
		gui.setMinimumSize(size);
		gui.setPreferredSize(size);
		gui.revalidate();
	}
	
	public void ladeStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		NullTester.test(gui);
		gui.setStreckenabschnitt(streckenabschnitt);
		repaint();
	}
	public void ladeZüge(Set<Fahrt> fahrten)
	{
		NullTester.test(gui);
		gui.zeichneFahrten(fahrten);
		gui.optimizeHeight();
		repaint();
	}
	public void repaint()
	{
		gui.repaint();
	}
	
	// ActionHandler
	public void menuImportJTG()
	{
		JTrainGraphImportGUI jtgi = new JTrainGraphImportGUI((ergebnis) -> {
			if(ergebnis.success())
			{
				try
				{
					JTrainGraphImporter importer = new JTrainGraphImporter();
					InputStream input = new java.io.FileInputStream(ergebnis.getPfad());
					Streckenabschnitt streckenabschnitt = importer.importStreckenabschnitt(input);
					ladeStreckenabschnitt(streckenabschnitt);
					
					input = new java.io.FileInputStream(ergebnis.getPfad());
					Set<Fahrt> fahrten = importer.importFahrten(input, streckenabschnitt, ergebnis.getLinie());
					ladeZüge(fahrten);
				}
				catch(FileNotFoundException e)
				{
					gui.errorMessage("Datei nicht gefunden!");
				}
				catch(ImportException e)
				{
					gui.errorMessage("Fehler beim Import!");
					e.printStackTrace();
				}
			}
		});
	}
	
	private static void log(String text)
	{
		System.out.println("BildfahrplanGUIController: "+text);
	}
}
