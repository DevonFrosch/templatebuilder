package de.stsFanGruppe.templatebuilder.config.fahrtenfarbe;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUI;

public class FahrtenFarbeGUITableModel
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUI.class);
	
	static FahrtenFarbeSettingsGUI gui;
	public static ArrayList<Color> testFarben = new ArrayList<Color>()
	{
		{
			add(Color.BLACK);
			add(Color.RED);
			add(Color.BLUE);
			add(Color.BLACK);
			add(Color.BLACK);
		}
	};
	
	/**
	 * Anhand der ArrayList (aktuell) testFarben erh�lt jede Zelle in seiner Zeile die Hintergrundfarbe definiert,
	 * die ihm gegeben wird.
	 * 
	 * Um dies aufzurufen, wird an der Tabelle die Spalte gesucht und die Zelldarstellung neu gesetzt:
	 * table.getColumnModel().getColumn(n).setCellRenderer(new BackgroundTableCellRenderer());
	 */
	public static class BackgroundTableCellRenderer extends JTable implements TableCellRenderer
	{
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Color color = null;
			try
			{
				if(row >= 0 && testFarben != null) color = testFarben.get(row);
			}
			catch(IndexOutOfBoundsException e)
			{
			//	log.error("Farbenspalte: Farbe konnte nicht gefunden werden - " + e.getMessage() + color);
			}
			setBackground(color);
			return this;
		}
	}
	
	/**
	 * Zelleneditoreinstellung f�r die Spalte, die eine Combobox erhalten soll, wo die Linientypen gezeichnet werden.
	 *
	 * Um dies aufzurufen, wird an der Tabelle die Spalte gesucht und die Zelleditordarstellung neu gesetzt:
	 * table.getColumnModel().getColumn(n).setCellEditor(new LinienTypCellEditor());
	 */
	public static class LinienTypCellEditor extends AbstractCellEditor implements TableCellEditor
	{
		JComboBox<LineType> comboBoxLinienTyp;
		
		public LinienTypCellEditor()
		{
			// FIXME Combobox werden derzeit doppelt im Code geschrieben. Die
			// JComboBox sollte m�glich einmal definiert werden.
			// Erstellt eine Combobox mit den Linientypen.
			comboBoxLinienTyp = new JComboBox<LineType>(LineType.values());
			comboBoxLinienTyp.setRenderer(new LineRenderer());
			comboBoxLinienTyp.setEditable(false);
			comboBoxLinienTyp.setSelectedItem(null);
		}
		
		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int colIndex)
		{
//			//Setzt das Modell f�r die Tabelle
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
	
	/**
	 * MouseListener f�r die Tabelle: Fahrtenfarbekategoriesieren
	 * Es wird nur der mouseClicked(..) mit einer funktionierenden Methode ausgef�hrt.
	 *
	 * Wenn eine Zelle in der 2. Spalte ausgew�hlt wird, �ffnet sich ein Editor zum �ndern der Farbe.
	 */
	public static class CellMouseClickForBackgroundColor extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			JTable target = (JTable) e.getSource();
			int row = target.getSelectedRow();
			int column = target.getSelectedColumn();
			if(column == 1)
			{
				Color c = null;
				c = JColorChooser.showDialog(gui, "Farbe w�hlen", testFarben.get(row));
				if(c != null)
				{
					testFarben.set(row, c);
				}
			}
		}
	}

	public void setValueAt(Object value, int rowIndex, int colIndex)
	{
		// TODO Auto-generated method stub
		
	}
}
