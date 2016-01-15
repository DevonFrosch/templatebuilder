package de.stsFanGruppe.templatebuilder.fahrtenFarbe;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import de.stsFanGruppe.bibliothek.FahrtenFarbe;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUIController;
import de.stsFanGruppe.templatebuilder.fahrtenFarbe.FahrtenFarbeConfig.LineType;
import de.stsFanGruppe.tools.NullTester;

public class FahrtenFarbeSettingsGUIController {
	
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUIController.class);
	
	private FahrtenFarbe fahrtenFarbe;
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
	
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int colIndex) 
    {

	    // Set the model data of the table
	    if(isSelected)
	    {
	    gui.comboBoxLinienArt.setSelectedItem(value);
	    TableModel model = table.getModel();
	    model.setValueAt(value, rowIndex, colIndex);
	    }
	    
	    return gui.comboBoxLinienArt;
    }
	
	public Object getCellEditorValue() 
    {
		return gui.comboBoxLinienArt.getSelectedItem();
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
		Object[] defaultRowdata = {"", "", gui.txtStandardLinienStaerke.getText(),gui.comboBoxLinienArt.getSelectedItem()};
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
						gui.testFarben.add(rows[i]+i+1, gui.panelStandardFarbeVorschau.getBackground());
					}
					gui.table.clearSelection();
					break;
				} else {
					gui.testFarben.add(gui.panelStandardFarbeVorschau.getBackground());
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
						gui.testFarben.remove(rows[i]-i); 
						model.removeRow(rows[i]-i);
					}
					break;
				}
		}
		
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
			case "zeitenFarbe":
				c = JColorChooser.showDialog(gui, "Farbe wählen", gui.panelStandardFarbeVorschau.getBackground());
				if(c != null)
				{
					gui.panelStandardFarbeVorschau.setBackground(c);
				}
				break;
			default:
				log.error("farbButton: actionCommand nicht erkannt: {}", event.getActionCommand());
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
