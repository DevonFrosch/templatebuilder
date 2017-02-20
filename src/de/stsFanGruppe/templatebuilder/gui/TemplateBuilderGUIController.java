package de.stsFanGruppe.templatebuilder.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Set;
import java.util.StringJoiner;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUI;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUIController;
import de.stsFanGruppe.templatebuilder.editor.EditorData;
import de.stsFanGruppe.templatebuilder.editor.EditorGUI;
import de.stsFanGruppe.templatebuilder.editor.bildfahrplan.BildfahrplanGUI;
import de.stsFanGruppe.templatebuilder.editor.bildfahrplan.BildfahrplanGUIController;
import de.stsFanGruppe.templatebuilder.editor.tabEditor.TabEditorGUI;
import de.stsFanGruppe.templatebuilder.external.*;
import de.stsFanGruppe.templatebuilder.external.jtraingraph.*;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.GUILocker;
import de.stsFanGruppe.tools.NullTester;

public class TemplateBuilderGUIController extends GUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TemplateBuilderGUIController.class);
	
	public final String version;
	public final boolean dev;
	
	protected Hashtable<String, Boolean> windowLocks = new Hashtable<>();
	
	protected TemplateBuilderGUI gui = null;
	protected BildfahrplanConfig config;
	protected FirstLastLinkedList<TabController> tabController = new FirstLastLinkedList<>();
	
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
	public BildfahrplanConfig getConfig()
	{
		return config;
	}
	
	// ActionHandler
	public void menuAction(ActionEvent event)
	{
		assert gui != null;
		
		switch(event.getActionCommand())
		{
			case "neu":
			{
				addBildfahrplanTab(null, null, null, new BildfahrplanGUIController(config, gui));
				break;
			}
			case "importFromJTG":
			{
				if(!GUILocker.lock(JTrainGraphImportGUI.class)) break;
				JTrainGraphImportGUI jtgi = new JTrainGraphImportGUI(gui.getFrame(), (ergebnis) -> {
					assert ergebnis != null;
					
					if(ergebnis.success())
					{
						log.info("JTG-Import von {}", ergebnis.getPfad());
						BildfahrplanGUIController bfpController = new BildfahrplanGUIController(config, gui);
						String name = "Import";
						
						try
						{
							JTrainGraphImporter importer = new JTrainGraphImporter();
							
							InputStream input = new java.io.FileInputStream(ergebnis.getPfad());
							Streckenabschnitt streckenabschnitt = importer.importStreckenabschnitt(input);
							assert streckenabschnitt != null;
							
							bfpController.ladeStreckenabschnitt(streckenabschnitt);
							name = streckenabschnitt.getName();
							
							if(ergebnis.importZuege())
							{
								assert ergebnis.getPfad() != null;
								input = new java.io.FileInputStream(ergebnis.getPfad());
								Set<Fahrt> fahrten = importer.importFahrten(input, streckenabschnitt, ergebnis.getLinie());
								bfpController.ladeZüge(fahrten);
							}
						}
						catch(FileNotFoundException e)
						{
							log.error("JTG-Import", e);
							gui.errorMessage("Datei nicht gefunden!");
							return;
						}
						catch(ImportException e)
						{
							log.error("JTG-Import", e);
							gui.errorMessage("Fehler beim Import!");
							return;
						}
						
						// Zum Panel hinzufügen
						addBildfahrplanTab(name, null, null, bfpController);
					}
					
					GUILocker.unlock(JTrainGraphImportGUI.class);
				});
				break;
			}
			case "importRulesFromJTG":
			{
				if(!GUILocker.lock(JTrainGraphZugregelImportGUI.class)) break;
				new JTrainGraphZugregelImportGUI(gui.getFrame(), (ergebnis) -> {
					assert ergebnis != null;
					
					if(ergebnis.success())
					{
						log.info("JTG-Regelimport von {}", ergebnis.getPfad());
						try
						{
							JTrainGraphZugregelImporter importer = new JTrainGraphZugregelImporter();
							
							InputStream input = new java.io.FileInputStream(ergebnis.getPfad());
							importer.importRegeln(input);
						}
						catch(FileNotFoundException e)
						{
							log.error("JTG-Regelimport", e);
							gui.errorMessage("Datei nicht gefunden!");
						}
						catch(ImportException e)
						{
							log.error("JTG-Regelimport", e);
							gui.errorMessage("Fehler beim Import!");
						}
					}
					GUILocker.unlock(JTrainGraphZugregelImportGUI.class);
				});
				break;
			}
			case "exportToJTG":
			{
				EditorGUI bfpGUI = null;
				Component com = getSelectedGUI();
				
				if(com == null || !com.getClass().isAssignableFrom(EditorGUI.class))
				{
					log.info("exportToJTG: Aktueller Tab ist keine EditorGUI.");
					gui.errorMessage("Aktueller Tab ist nicht exportierbar.\nBitte anderen Tab auswählen.");
					return;
				}
				
				try
				{
					
					bfpGUI = (EditorGUI) com;
				}
				catch(ClassCastException e)
				{
					// Das ist wohl klein 
					return;
				}
				
				EditorData editoData = bfpGUI.getController().getEditorData();
				
				if(!GUILocker.lock(JTrainGraphExportGUI.class)) break;
				JTrainGraphExportGUI jtge = new JTrainGraphExportGUI(gui.getFrame(), (ergebnis) -> {
					assert ergebnis != null;
					
					if(ergebnis.success())
					{
						log.info("JTG-Export von {}", ergebnis.getPfad());
						try
						{
							JTrainGraphExporter exporter = new JTrainGraphExporter(ergebnis.useDS100());
							OutputStream output = new java.io.FileOutputStream(ergebnis.getPfad());
							
							Streckenabschnitt streckenabschnitt = editoData.getStreckenabschnitt();
							
							assert streckenabschnitt != null;
							
							Set<Fahrt> fahrten = null;
							if(ergebnis.exportZuege())
							{
								fahrten = editoData.getFahrten();
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
			}
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
	
	public int addBildfahrplanTab(String name, Icon icon, String toolTip, BildfahrplanGUIController bfpController)
	{
		return gui.addTab(name, icon, toolTip, bfpController.getBildfahrplanGUI(),
				bfpController.getBildfahrplanSpaltenHeaderGUI(), bfpController.getBildfahrplanZeilenHeaderGUI());
	}
	public boolean selectedTabIsBildfahrplan()
	{
		Class<? extends Component> tab = gui.getSelectedTab().getClass();
		if(tab == null)
		{
			return false;
		}
		return tab.equals(BildfahrplanGUI.class);
	}
	
	public boolean selectedTabIsTabEditor()
	{
		Class<? extends Component> tab = gui.getSelectedTab().getClass();
		if(tab == null)
		{
			return false;
		}
		return tab.equals(TabEditorGUI.class);
	}
	
	public Component getSelectedGUI()
	{
		JScrollPane scrollPane = (JScrollPane) gui.getSelectedTab();
		if(scrollPane == null || scrollPane.getViewport() == null)
		{
			return null;
		}
		return scrollPane.getViewport().getView();
	}
}
