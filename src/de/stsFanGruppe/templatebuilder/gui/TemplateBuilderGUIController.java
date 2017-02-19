package de.stsFanGruppe.templatebuilder.gui;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Set;
import java.util.StringJoiner;
import javax.swing.JTabbedPane;
import de.stsFanGruppe.templatebuilder.bildfahrplan.BildfahrplanGUIController;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUI;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUIController;
import de.stsFanGruppe.templatebuilder.external.*;
import de.stsFanGruppe.templatebuilder.external.jtraingraph.*;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.GUILocker;
import de.stsFanGruppe.tools.NullTester;

public class TemplateBuilderGUIController extends GUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TemplateBuilderGUIController.class);
	
	public final String version;
	public final boolean dev;
	
	private TemplateBuilderGUI gui = null;
	private BildfahrplanGUIController bildfahrplanController = null;
	private JTabbedPane tabs = null;
	
	private Hashtable<String, Boolean> windowLocks = new Hashtable<>();
	
	protected BildfahrplanConfig config;
	
	public TemplateBuilderGUIController(BildfahrplanConfig config, String version, boolean dev)
	{
		NullTester.test(config);
		this.config = config;
		this.version = version;
		this.dev = dev;
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
				if(!GUILocker.lock(JTrainGraphImportGUI.class)) break;
				JTrainGraphImportGUI jtgi = new JTrainGraphImportGUI(gui.getFrame(), (ergebnis) -> {
					assert bildfahrplanController != null;
					assert ergebnis != null;
					
					if(ergebnis.success())
					{
						log.info("JTG-Import von {}", ergebnis.getPfad());
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
							log.error("JTG-Import", e);
							gui.errorMessage("Datei nicht gefunden!");
						}
						catch(ImportException e)
						{
							log.error("JTG-Import", e);
							gui.errorMessage("Fehler beim Import!");
						}
					}
					GUILocker.unlock(JTrainGraphImportGUI.class);
				});
				break;
			case "exportToJTG":
				if(!GUILocker.lock(JTrainGraphExportGUI.class)) break;
				JTrainGraphExportGUI jtge = new JTrainGraphExportGUI(gui.getFrame(), (ergebnis) -> {
					assert bildfahrplanController != null;
					assert ergebnis != null;
					
					if(ergebnis.success())
					{
						log.info("JTG-Export von {}", ergebnis.getPfad());
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
							log.error("JTG-Export", e);
							gui.errorMessage("Datei nicht gefunden!");
						}
						catch(ExportException e)
						{
							log.error("JTG-Export", e);
							gui.errorMessage("Fehler beim Export!");
						}
					}
					GUILocker.unlock(JTrainGraphExportGUI.class);
				});
				break;
			case "options":
				if(!GUILocker.lock(BildfahrplanSettingsGUI.class)) break;
				BildfahrplanSettingsGUI sg = new BildfahrplanSettingsGUI(new BildfahrplanSettingsGUIController(config, () -> GUILocker.unlock(BildfahrplanSettingsGUI.class)), gui.getFrame());
				break;
			case "about":
				StringJoiner aboutText = new StringJoiner("\n");
				aboutText.add("TemplateBuilder "+version+ ((dev) ? " (Entwicklungsversion)" : ""));
				aboutText.add("Copyright DevonFrosch, Koschi");
				aboutText.add("Fehler bitte unter https://sts-fan-gruppe.de/mantis/ melden.");
				
				gui.infoMessage(aboutText.toString(), "Über");
				break;
			case "locks":
				gui.infoMessage(GUILocker.lockInfo(), "Locks");
				break;
			case "exit":
				log.info("Programm beendet");
				System.exit(0);
			default:
				log.error("Menü: nicht erkannt");
				break;
		}
	}
}
