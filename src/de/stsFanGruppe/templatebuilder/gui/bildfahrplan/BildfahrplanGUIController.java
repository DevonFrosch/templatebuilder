package de.stsFanGruppe.templatebuilder.gui.bildfahrplan;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Set;
import de.stsFanGruppe.templatebuilder.external.ImportException;
import de.stsFanGruppe.templatebuilder.external.jtraingraph.JTrainGraphImporter;
import de.stsFanGruppe.templatebuilder.gui.external.JTrainGraphImportGUI;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.NullTester;

public class BildfahrplanGUIController implements BildfahrplanController
{
	private BildfahrplanGUI gui = null;
	private BildfahrplanZeichner zeichner = null;
	
	public BildfahrplanGUIController()
	{
		
	}
	
	public void setBildfahrplanGUI(BildfahrplanGUI gui)
	{
		this.gui = gui;
		this.zeichner = gui.getBildfahrplanZeichner();
		// Sicherstellen, dass die Operation einen Effekt hatte...
		assert zeichner != null;
	}
	
	public void setPanelSize()
	{
		NullTester.test(zeichner);
		
		BildfahrplanConfig config = zeichner.config;
		Dimension size = zeichner.getMinimumSize();
		size.setSize(size.getWidth(), config.getPanelHeight());
		zeichner.setMinimumSize(size);
		zeichner.setPreferredSize(size);
		zeichner.revalidate();
	}
	
	public void ladeStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		NullTester.test(zeichner);
		gui.bildfahrplanZeichner.setStreckenabschnitt(streckenabschnitt);
		repaint();
	}
	public void ladeZüge(Set<Fahrt> fahrten)
	{
		NullTester.test(zeichner);
		zeichner.zeichneFahrten(fahrten);
		zeichner.optimizeHeight();
		repaint();
	}
	public void repaint()
	{
		zeichner.repaint();
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
