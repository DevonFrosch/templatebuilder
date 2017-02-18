package de.stsFanGruppe.templatebuilder.config.fahrtenfarbe;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanSettingsGUI;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;

public class FahrtenFarbeGUITableModel extends AbstractTableModel
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanSettingsGUI.class);
	
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
	protected FirstLastList<FahrtDarstellung> darstellungen;
	public static final String[] UEBERSCHRIFTEN = {"Zugname", "Farbe", "Breite [px]", "Typ"};
	
	public FahrtenFarbeGUITableModel()
	{
		darstellungen = new FirstLastLinkedList<>();
		
		// FIXME: Test
		darstellungen.add(new FahrtDarstellung("Test", Color.RED, 1, LineType.SOLID_LINE));
	}
	
	/**
	 * Anhand der ArrayList (aktuell) testFarben erhält jede Zelle in seiner Zeile die Hintergrundfarbe definiert,
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
	
	public int getColumnCount()
	{
		return UEBERSCHRIFTEN.length;
	}
	
	public int getRowCount()
	{
		return darstellungen.size();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		switch(columnIndex)
		{
			case 0:
				return darstellungen.get(rowIndex).getName();
			case 1:
				return darstellungen.get(rowIndex).getFarbe();
			case 2:
				return darstellungen.get(rowIndex).getBreite();
			case 3:
				return darstellungen.get(rowIndex).getTyp();
			default:
				log.error("Tabelle hat zu viele Spalten (angefragt: {})", columnIndex);
		}
		return null;
	}
}
