package de.stsFanGruppe.templatebuilder.streckenConfig;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.JColorChooser;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import de.stsFanGruppe.templatebuilder.editor.EditorData;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUIController;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.TimeFormater;

public class BildfahrplanSettingsStreckenGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsStreckenGUIController.class);
	
	protected EditorData editorDaten;
	protected static Streckenabschnitt streckenabschnitt;
	private BildfahrplanSettingsStreckenGUI gui;
	private BildfahrplanStreckenConfig config;
	private Runnable onClose;
	
	public BildfahrplanSettingsStreckenGUIController(EditorData editorDaten, BildfahrplanStreckenConfig config, Runnable onClose)
	{
		NullTester.test(editorDaten);
		NullTester.test(config);
		NullTester.test(onClose);
		this.editorDaten = editorDaten;
		this.config = config;
		this.onClose = onClose;
	}
	
	public void setSettingsGUI(BildfahrplanSettingsStreckenGUI gui)
	{
		NullTester.test(gui);
		this.gui = gui;
		gui.saveEnabled = config.schreibTest();
	}
	
	public BildfahrplanStreckenConfig getConfig()
	{
		return config;
	}
	
	public String getMinZeit()
	{
		return TimeFormater.doubleToString(config.getMinZeit());
	}
	public String getMaxZeit()
	{
		return TimeFormater.doubleToString(config.getMaxZeit());
	}
	
	public void farbButton(ActionEvent event)
	{
		assert config != null;
		
		// Ohne GUI können wir nichts machen
		if(gui == null)
		{
			log.error("farbButton(): Keine GUI gesetzt!");
			return;
		}
		
		Color c = null;
		switch(event.getActionCommand())
		{
//			s
		}
	}
	/**
	 * Liest den ActionCommand vom event aus und führt dann etwas aus.
	 * @param event
	 */
	public void tableButtonAction(ActionEvent event)
	{
		assert config != null;
		
		// Ohne GUI können wir nichts machen
		if(gui == null)
		{
			log.error("actionButton(): Keine GUI gesetzt!");
			return;
		}
		//Tabellenmodell wird ausgelesen
		DefaultTableModel model = (DefaultTableModel)gui.table.getModel();
		//Selectionmodell wird ausgelesen
		ListSelectionModel selectionModel = gui.table.getSelectionModel();
		//Erhalte ausgewählte Zeilen
		int[] rows = gui.table.getSelectedRows();
		Object[] defaultRowdata = {"", ""};
		switch(event.getActionCommand())
		{
			//Zeile(n) nach oben verschieben
			case "moveUpRow":
				if(rows[0]==0)
				{
					gui.infoMessage("Erste Zeile kann nicht nach oben verschoben werden.", "Info: Zeile nach oben verschieben");
				}
				else
				{
					selectionModel.clearSelection();
					for(int i=0; i < rows.length; i++)
					{
						model.moveRow(rows[i], rows[i], rows[i]-1);
						gui.table.addRowSelectionInterval(rows[i]-1, rows[i]-1);
					}
				}
				break;
			//Zeile(n) nach unten verschieben
			case "moveDownRow":
				selectionModel.clearSelection();
				for(int i=0; i < rows.length; i++)
				{
					model.moveRow(rows[i], rows[i], rows[i]+1);
					gui.table.addRowSelectionInterval(rows[i]+1, rows[i]+1);
				}
				break;
			/* Zeile(n) hinzufügen
			 * Zeile(n) wird/ werden nach ausgewählten Zeilen hinzugefügt oder nach der letzten Zeile
			 */
			case "addRow":
				if(rows.length > 0)
				{
					for(int i=0; i < rows.length; i++)
					{
						model.insertRow(rows[i]+i+1, defaultRowdata);
					}
					gui.table.clearSelection();
					break;
				} else {
					model.addRow(defaultRowdata);
					break;
				}
			/* Zeile(n) löschen
			 * Wenn keine Zeile ausgewählt, erscheint Fehlerfenster.
			 */
			case "removeRow":
				if(rows.length < 0)
				{
					gui.errorMessage("Zeile auswählen, die gelöscht werden soll", "Zeile löschen fehlgeschlagen!");
					break;
				} 
				else
				{
					for(int i=0; i < rows.length; i++)
					{
						model.removeRow(rows[i]-i);
					}
					break;
				}
		}
		
	}
	
	public void actionButton(ActionEvent event)
	{
		assert config != null;
		
		// Ohne GUI können wir nichts machen
		if(gui == null)
		{
			log.error("actionButton(): Keine GUI gesetzt!");
			return;
		}
		
		switch(event.getActionCommand())
		{
			case "save":
				config.exportSettings(gui, gui);
				break;
			case "load":
				config.importSettings(gui, gui);
				// Einstellungen neu laden
				ladeStrecken();
				break;
			case "cancel":
				close();
				return;
			case "apply":
			case "ok":
				try
				{
					speichereStrecken();
				}
				catch(NumberFormatException e)
				{
					gui.errorMessage("Fehler beim Speichern der Einstellungen");
					return;
				}
				
				if(!config.schreibeEinstellungen())
				{
					log.error("Fehler beim Speichern der Einstellungen");
					gui.errorMessage("Fehler beim Speichern der Einstellungen");
					return;
				}
				
				// OK: Fenster schließen
				if(event.getActionCommand() == "ok")
				{
					close();
			        return;
				}
				break;
			default:
				log.error("actionButton: actionCommand nicht erkannt: {}", event.getActionCommand());
		}
	}
	
	public void ladeStrecken() {
		assert editorDaten != null;
		
		DefaultTableModel model = new DefaultTableModel(BildfahrplanSettingsStreckenGUI.columnName, 0)
		{
            @Override
            public Class<?> getColumnClass( int column ) {
                switch( column ){
                    case 0: return Integer.class;
                    case 1: return String.class;
                    default: return Object.class;
                }
            }
        };
        
        streckenabschnitt = editorDaten.getStreckenabschnitt();
        
		if(streckenabschnitt != null)
		{
			for(Betriebsstelle bs: streckenabschnitt.getBetriebsstellen())
			{
				double km = editorDaten.getStreckenKm(bs);
				String name = bs.getName();
				Object[] row = {km, name};
				
				model.addRow(row);
			}
		}
		gui.table.setModel(model);
	}
	
	public void speichereStrecken()
	{
		
	}
	
	protected void close()
	{
		gui.close();
        gui = null;
        onClose.run();
	}
	
	protected int parseIntField(String name, String input)
	{
		if(input == null || input.trim().isEmpty())
		{
			log.error("{}: Leerer String", name);
			gui.errorMessage(name+": Dargestellte Zeit: Feld ist leer");
			throw new NumberFormatException();
		}
		try
		{
			int hpsInt = Integer.parseInt(input);
			if(hpsInt < 0)
			{
				log.error("{}: Wert kleiner 0", name);
				gui.errorMessage(name+": Nur positive ganze Zahlen erlaubt.");
				throw new NumberFormatException();
			}
			return hpsInt;
		}
		catch(NumberFormatException e)
		{
			log.error(name+": NumberformatException", e);
			gui.errorMessage(name+": Nur positive ganze Zahlen erlaubt.");
			throw new NumberFormatException();
		}
	}
}
