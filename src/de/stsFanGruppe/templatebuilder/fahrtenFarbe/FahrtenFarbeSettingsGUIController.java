package de.stsFanGruppe.templatebuilder.fahrtenFarbe;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUIController;
import de.stsFanGruppe.tools.NullTester;

public class FahrtenFarbeSettingsGUIController {
	
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUIController.class);
	
	private FahrtenFarbeSettingsGUI gui;
	private FahrtenFarbeConfig config;
	private Runnable onClose;

	public FahrtenFarbeSettingsGUIController(FahrtenFarbeConfig config, Runnable onClose)
	{
		NullTester.test(config);
		NullTester.test(onClose);
		this.config = config;
		this.onClose = onClose;
	}

	public void setSettingsGui(FahrtenFarbeSettingsGUI gui)
	{
		NullTester.test(gui);
		this.gui = gui;
		//gui.saveEnabled = config.schreibTest();
	}
	
	public FahrtenFarbeConfig getConfig()
	{
		return config;
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
		DefaultTableModel model = (DefaultTableModel)gui.table.getModel();
		ListSelectionModel selectionModel = gui.table.getSelectionModel();
		int[] rows = gui.table.getSelectedRows();
		switch(event.getActionCommand())
		{
			//Zeile(n) nach oben verschieben
			case "moveUpRow":
				selectionModel.clearSelection();
				for(int i=0; i < rows.length; i++)
				{
					model.moveRow(rows[i], rows[i], rows[i]-1);
					gui.table.addRowSelectionInterval(rows[i]-1, rows[i]-1);
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
						model.insertRow(rows[i]+i+1, new Object[] {"RE 123", "ok", "nicht ok", "na gut"});
					}
					gui.table.clearSelection();
					break;
				} else {
					model.addRow(new Object[] {"RE 123", "ok", "nicht ok", "na gut"});
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
				gui.loadSettings();
				break;
			case "cancel":
				close();
				return;
			case "apply":
			case "ok":
				try
				{
					//speichereTabAllgemein();
					
				}
				catch(NumberFormatException e)
				{
					gui.errorMessage("Fehler beim Speichern der Einstellungen");
					return;
				}
				
				//if(!config.schreibeEinstellungen())
				//{
				//	log.error("Fehler beim Speichern der Einstellungen");
				//	gui.errorMessage("Fehler beim Speichern der Einstellungen");
				//	return;
				//}
				
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

	protected void close()
	{
		gui.close();
        gui = null;
        onClose.run();
	}
}
