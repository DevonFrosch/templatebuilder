package de.stsFanGruppe.templatebuilder.gui;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Set;
import java.util.StringJoiner;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUI;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUIController;
import de.stsFanGruppe.templatebuilder.config.GeneralConfig;
import de.stsFanGruppe.templatebuilder.editor.EditorDaten;
import de.stsFanGruppe.templatebuilder.editor.bildfahrplan.BildfahrplanGUIController;
import de.stsFanGruppe.templatebuilder.editor.fahrtEditor.FahrtEditorGUI;
import de.stsFanGruppe.templatebuilder.editor.fahrtEditor.FahrtEditorGUIController;
import de.stsFanGruppe.templatebuilder.editor.streckenEditor.StreckenEditorGUI;
import de.stsFanGruppe.templatebuilder.editor.streckenEditor.StreckenEditorGUIController;
import de.stsFanGruppe.templatebuilder.editor.tabEditor.TabEditorGUIController;
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
	
	protected Hashtable<String, Boolean> windowLocks = new Hashtable<>();
	
	protected TemplateBuilderGUI gui = null;
	protected GeneralConfig config = null;
	protected BildfahrplanConfig bildfahrplanConfig = null;
	protected TemplateBuilderTabs tabs = null;
	
	public TemplateBuilderGUIController(GeneralConfig config, String version, boolean dev, String update)
	{
		NullTester.test(config);
		this.config = config;
		this.bildfahrplanConfig = config.getBildfahrplanConfig();
		this.version = version;
		this.dev = dev;
		this.gui = new TemplateBuilderGUI(this, update);
		this.tabs = new TemplateBuilderTabs(gui.tabbedPane);
	}
	
	public BildfahrplanConfig getBildfahrplanConfig()
	{
		return bildfahrplanConfig;
	}
	
	// ActionHandler
	public void menuAction(ActionEvent event)
	{
		switch(event.getActionCommand())
		{
			case "neu":
			{
				tabs.addTabEditorTab("Neuer Tab", null, null, new TabEditorGUIController(config, true));
				break;
			}
			case "importFromJTG":
			{
				if(!GUILocker.lock(JTrainGraphImportGUI.class))
					break;
				JTrainGraphImportGUI jtgi = new JTrainGraphImportGUI(gui.getFrame(), config, (ergebnis) -> {
					assert ergebnis != null;
					
					if(ergebnis.success())
					{
						log.info("JTG-Import von {}", ergebnis.getPfad());
						BildfahrplanGUIController bfpController = new BildfahrplanGUIController(gui, config);
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
								bfpController.ladeZ�ge(fahrten);
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
							gui.errorMessage("Fehler beim Import!\n"+e.getLocalizedMessage());
							return;
						}
						
						// Zum Panel hinzuf�gen
						tabs.addBildfahrplanTab(name, null, null, bfpController);
					}
					
					GUILocker.unlock(JTrainGraphImportGUI.class);
				});
				break;
			}
			case "importRulesFromJTG":
			{
				if(!GUILocker.lock(JTrainGraphZugregelImportGUI.class))
					break;
				new JTrainGraphZugregelImportGUI(gui.getFrame(), (ergebnis) -> {
					assert ergebnis != null;
					
					if(ergebnis.success())
					{
						log.info("JTG-Regelimport von {}", ergebnis.getPfad());
						try
						{
							JTrainGraphZugregelImporter importer = new JTrainGraphZugregelImporter();
							
							InputStream input = new java.io.FileInputStream(ergebnis.getPfad());
							importer.importRegeln(input, bildfahrplanConfig.getFahrtDarstellungConfig());
						}
						catch(FileNotFoundException e)
						{
							log.error("JTG-Regelimport", e);
							gui.errorMessage("Datei nicht gefunden!");
						}
						catch(ImportException e)
						{
							log.error("JTG-Regelimport", e);
							gui.errorMessage("Fehler beim Import!\n"+e.getLocalizedMessage());
						}
					}
					GUILocker.unlock(JTrainGraphZugregelImportGUI.class);
				});
				break;
			}
			case "exportToJTG":
			{
				EditorDaten editorDaten = tabs.getSelectedEditorDaten();
				if(editorDaten == null)
				{
					log.info("exportToJTG: Aktueller Tab ist keine EditorGUI.");
					gui.errorMessage("Aktueller Tab ist nicht exportierbar.\nBitte anderen Tab ausw�hlen.");
				}
				
				if(!GUILocker.lock(JTrainGraphExportGUI.class))
					break;
				JTrainGraphExportGUI jtge = new JTrainGraphExportGUI(gui.getFrame(), (ergebnis) -> {
					assert ergebnis != null;
					
					if(ergebnis.success())
					{
						log.info("JTG-Export von {}", ergebnis.getPfad());
						try
						{
							JTrainGraphExporter exporter = new JTrainGraphExporter(ergebnis.useDS100());
							OutputStream output = new java.io.FileOutputStream(ergebnis.getPfad());
							
							Streckenabschnitt streckenabschnitt = editorDaten.getStreckenabschnitt();
							
							assert streckenabschnitt != null;
							
							Set<Fahrt> fahrten = null;
							if(ergebnis.exportZuege())
							{
								fahrten = editorDaten.getFahrten();
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
							gui.errorMessage("Fehler beim Export!\n"+e.getLocalizedMessage());
						}
					}
					GUILocker.unlock(JTrainGraphExportGUI.class);
				});
				break;
			}
			case "bearbeiteFahrten":
			{
				EditorDaten editorDaten = tabs.getSelectedEditorDaten();
				if(editorDaten == null)
				{
					break;
				}
				
				if(!GUILocker.lock(FahrtEditorGUI.class))
					break;
				
				FahrtEditorGUIController controller = new FahrtEditorGUIController(config, editorDaten, () -> GUILocker.unlock(FahrtEditorGUI.class));
				
				break;
			}
			case "sucheZug":
			{
				String initialValue = bildfahrplanConfig.getFahrtDarstellungConfig().getZugsucheText();
				String suchString = gui.inputMessage("Suche nach Zugname:", initialValue);
				if(suchString == null)
				{
					break;
				}
				else if(suchString.equals(""))
				{
					suchString = null;
				}
				bildfahrplanConfig.getFahrtDarstellungConfig().setZugsucheText(suchString);
				break;
			}
			case "sucheTemplate":
			{
				String initialValue = bildfahrplanConfig.getFahrtDarstellungConfig().getTemplatesucheText();
				String suchString = gui.inputMessage("Suche nach Templatename:", initialValue);
				if(suchString == null)
				{
					break;
				}
				else if(suchString.equals(""))
				{
					suchString = null;
				}
				bildfahrplanConfig.getFahrtDarstellungConfig().setTemplatesucheText(suchString);
				break;
			}
			case "streckenEdit":
			{
				EditorDaten editorDaten = tabs.getSelectedEditorDaten();
				if(editorDaten == null)
				{
					break;
				}
				
				if(!GUILocker.lock(StreckenEditorGUI.class))
					break;
				
				StreckenEditorGUIController controller = new StreckenEditorGUIController(editorDaten, () -> GUILocker.unlock(StreckenEditorGUI.class));
				StreckenEditorGUI bssg = new StreckenEditorGUI(controller, gui.getFrame());
				break;
			}
			case "zeigeBildfahrplan":
			{
				EditorDaten editorDaten = tabs.getSelectedEditorDaten();
				if(!tabs.selectedTabIsEditor() || tabs.selectedTabIsBildfahrplan() || editorDaten == null)
				{
					break;
				}
				if(editorDaten.hasBildfahrplan())
				{
					tabs.setSelectedTab(tabs.getTabIndexOf(editorDaten.getBildfahrplan().getBildfahrplanGUI()));
					break;
				}
				
				int index = tabs.addBildfahrplanTab(editorDaten.getName(), null, null, new BildfahrplanGUIController(gui, editorDaten, config));
				tabs.setSelectedTab(index);
				break;
			}
			case "zeigeTabEditorHin":
			{
				EditorDaten editorDaten = tabs.getSelectedEditorDaten();
				if(!tabs.selectedTabIsEditor() || tabs.selectedTabIsTabEditorHin() || editorDaten == null)
				{
					break;
				}
				if(editorDaten.hasTabEditorHin())
				{
					log.info("Wechseln auf EditorHin");
					tabs.setSelectedTab(tabs.getTabIndexOf(editorDaten.getTabEditorHin().getTabEditorGUI()));
					break;
				}
				
				int index = tabs.addTabEditorTab(tabs.getSelectedEditorDaten().getName(), null, null,
						new TabEditorGUIController(editorDaten, config, true));
				tabs.setSelectedTab(index);
				break;
			}
			case "zeigeTabEditorR�ck":
			{
				EditorDaten editorDaten = tabs.getSelectedEditorDaten();
				if(!tabs.selectedTabIsEditor() || tabs.selectedTabIsTabEditorR�ck() || editorDaten == null)
				{
					break;
				}
				if(editorDaten.hasTabEditorR�ck())
				{
					tabs.setSelectedTab(tabs.getTabIndexOf(editorDaten.getTabEditorR�ck().getTabEditorGUI()));
					break;
				}
				
				int index = tabs.addTabEditorTab(tabs.getSelectedEditorDaten().getName(), null, null,
						new TabEditorGUIController(editorDaten, config, false));
				tabs.setSelectedTab(index);
				break;
			}
			case "zeigeMarkierte":
			{
				EditorDaten editorDaten = tabs.getSelectedEditorDaten();
				String zuege = bildfahrplanConfig.getFahrtDarstellungConfig().getHervorgehobeneZuegeText(editorDaten);
				
				if(zuege.length() > 0)
				{
					gui.infoMessage("Markierte Z�ge:\n"+zuege, "Markierte Z�ge");
				}
				else
				{
					gui.infoMessage("Keine Z�ge markiert", "Markierte Z�ge");
				}
				break;
			}
			case "options":
				if(!GUILocker.lock(BildfahrplanSettingsGUI.class))
					break;
				BildfahrplanSettingsGUI sg = new BildfahrplanSettingsGUI(
						new BildfahrplanSettingsGUIController(bildfahrplanConfig, () -> GUILocker.unlock(BildfahrplanSettingsGUI.class)), gui.getFrame());
				break;
			case "about":
				StringJoiner aboutText = new StringJoiner("\n");
				aboutText.add("TemplateBuilder " + version + ((dev) ? " (Entwicklungsversion)" : ""));
				aboutText.add("Copyright DevonFrosch, Koschi");
				aboutText.add("Webseite: https://sts-fan-gruppe.de/?seite=templatebuilder.");
				aboutText.add("Fehler bitte unter https://sts-fan-gruppe.de/mantis/ melden.");
				
				gui.infoMessage(aboutText.toString(), "�ber");
				break;
			case "locks":
				gui.infoMessage(GUILocker.lockInfo(), "Locks");
				break;
			case "exit":
				log.info("Programm beendet");
				System.exit(0);
			default:
				log.error("Men� {} nicht erkannt", event.getActionCommand());
				break;
		}
	}
}
