package de.stsFanGruppe.templatebuilder.config.fahrtenfarbe;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUI;

public class FahrtenFarbeGUITableModel
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUI.class);
	
	/**
	 * Anhand der ArrayList (aktuell) testFarben erhält jede Zelle in seiner Zeile die Hintergrundfarbe definiert,
	 * die ihm gegeben wird.
	 * 
	 * Um dies aufzurufen, wird an der Tabelle die Spalte gesucht und die Zelldarstellung neu gesetzt:
	 * table.getColumnModel().getColumn(n).setCellRenderer(new BackgroundTableCellRenderer());
	 */
	public static class BackgroundTableCellRenderer implements TableCellRenderer
	{
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Color color = (Color) value;
			
			// TODO: Performance testen
			Component comp = new JPanel();
			comp.setBackground(color);
			return comp;
		}
	}
	
	/**
	 * Zelleneditoreinstellung für die Spalte, die eine Combobox erhalten soll, wo die Linientypen gezeichnet werden.
	 *
	 * Um dies aufzurufen, wird an der Tabelle die Spalte gesucht und die Zelleditordarstellung neu gesetzt:
	 * table.getColumnModel().getColumn(n).setCellEditor(new LineTypeCellEditor());
	 */
	public static class LineTypeCellEditor extends AbstractCellEditor implements TableCellEditor
	{
		JLineTypeComboBox comboBoxLinienTyp;
		
		public LineTypeCellEditor()
		{
			comboBoxLinienTyp = new JLineTypeComboBox();
		}
		
		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int colIndex)
		{
//			//Setzt das Modell für die Tabelle
//			if(isSelected)
//			{
//				comboBoxLinienTyp.setSelectedItem(value);
//				TableModel model = (TableModel) table.getModel();
//				model.setValueAt(value, rowIndex, colIndex);
//			}
			return comboBoxLinienTyp;
		}
		
		@Override
		public Object getCellEditorValue()
		{
			return comboBoxLinienTyp.getSelectedItem();
		}
	}
}
