package de.stsFanGruppe.templatebuilder.editor.tabEditor;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class TableHeaderRenderer implements TableCellRenderer
{
	TableCellRenderer renderer;
	
	public TableHeaderRenderer(JTable table)
	{
		renderer = table.getTableHeader().getDefaultRenderer();
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
	{
		return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
	}
}
