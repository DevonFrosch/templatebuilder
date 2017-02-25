package de.stsFanGruppe.templatebuilder.editor.tabEditor;

import javax.swing.table.TableModel;

public interface TableContainer
{
	public void setTableModel(TableModel model);
	public void setTableValueAt(String content, int row, int column);
}
