package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.table;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype.JLineTypeComboBox;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype.LineType;

/**
 * Zelleneditoreinstellung für die Spalte, die eine Combobox erhalten soll, wo die Linientypen gezeichnet werden.
 * Um dies aufzurufen, wird an der Tabelle die Spalte gesucht und die Zelleditordarstellung neu gesetzt:
 * table.getColumnModel().getColumn(n).setCellEditor(new LineTypeCellEditor());
 */
public class LineTypeCellEditor extends AbstractCellEditor implements TableCellEditor
{
	JLineTypeComboBox comboBox;
	
	public LineTypeCellEditor()
	{
		comboBox = new JLineTypeComboBox();
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int colIndex)
	{
		LineType lineType = (LineType) table.getValueAt(rowIndex, colIndex);
		comboBox.setSelectedItem(lineType);
		return comboBox;
	}
	
	public Object getCellEditorValue()
	{
		return comboBox.getSelectedItem();
	}
}
