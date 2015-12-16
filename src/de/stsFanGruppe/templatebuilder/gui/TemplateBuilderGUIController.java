package de.stsFanGruppe.templatebuilder.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.StringJoiner;
import javax.swing.JTabbedPane;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.external.*;
import de.stsFanGruppe.templatebuilder.external.jtraingraph.*;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.templatebuilder.gui.bildfahrplan.BildfahrplanGUIController;
import de.stsFanGruppe.templatebuilder.gui.external.*;
import de.stsFanGruppe.templatebuilder.gui.settings.*;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.NullTester;

public class TemplateBuilderGUIController
{
	private TemplateBuilderGUI gui = null;
	private BildfahrplanGUIController bildfahrplanController = null;
	private JTabbedPane tabs = null;
	
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
	public void setTabbedPane(JTabbedPane tabbedPane)
	{
		this.tabs = tabbedPane;
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
							
							if(ergebnis.importZuege())
							{
								assert ergebnis.getPfad() != null;
								input = new java.io.FileInputStream(ergebnis.getPfad());
								Set<Fahrt> fahrten = importer.importFahrten(input, streckenabschnitt, ergebnis.getLinie());
								bildfahrplanController.ladeZüge(fahrten);
							}
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
			case "exportToJTG":
				JTrainGraphExportGUI jtge = new JTrainGraphExportGUI((ergebnis) -> {
					assert bildfahrplanController != null;
					assert ergebnis != null;
					
					if(ergebnis.success())
					{
						try
						{
							JTrainGraphExporter exporter = new JTrainGraphExporter(ergebnis.useDS100());
							OutputStream output = new java.io.FileOutputStream(ergebnis.getPfad());
							
							Streckenabschnitt streckenabschnitt = bildfahrplanController.getStreckenabschnitt();
							
							assert streckenabschnitt != null;
							
							Set<Fahrt> fahrten = null;
							if(ergebnis.exportZuege())
							{
								fahrten = bildfahrplanController.getFahrten();
								exporter.exportFahrten(output, streckenabschnitt, fahrten);
							}
							else
							{
								exporter.exportStreckenabschnitt(output, streckenabschnitt);
							}
							
						}
						catch(FileNotFoundException e)
						{
							gui.errorMessage("Datei nicht gefunden!");
						}
						catch(ExportException e)
						{
							gui.errorMessage("Fehler beim Export!");
							e.printStackTrace();
						}
					}
				});
				break;
			case "options":
				BildfahrplanSettingsGUI sg = new BildfahrplanSettingsGUI(new BildfahrplanSettingsGUIController(config));
				break;
			case "about":
				String version = "0.2dev1";
				boolean dev = true;
				
				StringJoiner text = new StringJoiner("\n");
				text.add("TemplateBuilder "+version);
				text.add("Copyright DevonFrosch (http://sts-fan-gruppe.de/)");
				if(dev) text.add("Dies ist eine Testversion, die noch Fehler enthält!");
				
				gui.infoMessage(text.toString(), "Über");
				break;
			default:
				log("Menü: nicht erkannt");
				break;
		}
	}
	
	private static void log(String text)
	{
		System.out.println("TemplateBuilderGUIController: "+text);
	}
}
