package de.stsFanGruppe.templatebuilder.gui;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Set;
import de.stsFanGruppe.templatebuilder.external.ImportException;
import de.stsFanGruppe.templatebuilder.external.jtraingraph.JTrainGraphImporter;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.templatebuilder.gui.bildfahrplan.BildfahrplanGUIController;
import de.stsFanGruppe.templatebuilder.gui.external.JTrainGraphImportGUI;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;

public class TemplateBuilderGUIController
{
	private TemplateBuilderGUI gui = null;
	private BildfahrplanGUIController bildfahrplanController = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			javax.swing.UIManager.setLookAndFeel( javax.swing.UIManager.getSystemLookAndFeelClassName() );
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
		{
			java.util.logging.Logger.getLogger(TemplateBuilderGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		
		EventQueue.invokeLater(() -> {
			try
			{
				TemplateBuilderGUIController controller = new TemplateBuilderGUIController();
				TemplateBuilderGUI window = new TemplateBuilderGUI(controller);
				window.setVisible(true);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		});
	}
	
	public TemplateBuilderGUIController()
	{
		
	}
	
	public void setGUI(TemplateBuilderGUI gui)
	{
		this.gui = gui;
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
					bildfahrplanController.ladeStreckenabschnitt(streckenabschnitt);
					
					input = new java.io.FileInputStream(ergebnis.getPfad());
					Set<Fahrt> fahrten = importer.importFahrten(input, streckenabschnitt, ergebnis.getLinie());
					bildfahrplanController.ladeZüge(fahrten);
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
