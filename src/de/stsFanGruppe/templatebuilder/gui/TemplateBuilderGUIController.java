package de.stsFanGruppe.templatebuilder.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Set;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.external.ImportException;
import de.stsFanGruppe.templatebuilder.external.jtraingraph.JTrainGraphImporter;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.templatebuilder.gui.bildfahrplan.BildfahrplanGUIController;
import de.stsFanGruppe.templatebuilder.gui.external.JTrainGraphImportGUI;
import de.stsFanGruppe.templatebuilder.gui.settings.SettingsGUI;
import de.stsFanGruppe.templatebuilder.gui.settings.SettingsGUIController;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.NullTester;

public class TemplateBuilderGUIController
{
	private TemplateBuilderGUI gui = null;
	private BildfahrplanGUIController bildfahrplanController = null;
	
	// TODO: allgemeines Config-Objekt
	protected BildfahrplanConfig config;
	
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
				TemplateBuilderGUIController controller = new TemplateBuilderGUIController(new BildfahrplanConfig());
				TemplateBuilderGUI window = new TemplateBuilderGUI(controller);
				window.setVisible(true);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		});
	}
	
	public TemplateBuilderGUIController(BildfahrplanConfig config)
	{
		NullTester.test(config);
		this.config = config;
	}
	
	public void setGUI(TemplateBuilderGUI gui)
	{
		this.gui = gui;
	}
	public void setBildfahrplanController(BildfahrplanGUIController controller)
	{
		NullTester.test(controller);
		this.bildfahrplanController = controller;
	}
	public BildfahrplanConfig getConfig()
	{
		return config;
	}
	
	// ActionHandler
	public void menuAction(ActionEvent event)
	{
		switch(event.getActionCommand())
		{
			case "importFromJTG":
				JTrainGraphImportGUI jtgi = new JTrainGraphImportGUI((ergebnis) -> {
					assert bildfahrplanController != null;
					assert ergebnis != null;
					
					if(ergebnis.success())
					{
						try
						{
							JTrainGraphImporter importer = new JTrainGraphImporter();
							InputStream input = new java.io.FileInputStream(ergebnis.getPfad());
							Streckenabschnitt streckenabschnitt = importer.importStreckenabschnitt(input);
							assert streckenabschnitt != null;
							assert bildfahrplanController != null;
							
							
							bildfahrplanController.ladeStreckenabschnitt(streckenabschnitt);
							
							assert ergebnis.getPfad() != null;
							input = new java.io.FileInputStream(ergebnis.getPfad());
							Set<Fahrt> fahrten = importer.importFahrten(input, streckenabschnitt, ergebnis.getLinie());
							bildfahrplanController.ladeZ�ge(fahrten);
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
				break;
			case "options":
				SettingsGUI sg = new SettingsGUI(new SettingsGUIController(config));
				break;
			default:
				log("Men�: nicht erkannt");
				break;
		}
	}
	
	private static void log(String text)
	{
		System.out.println("TemplateBuilderGUIController: "+text);
	}
}
