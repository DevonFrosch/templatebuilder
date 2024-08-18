package de.stsFanGruppe.templatebuilder.editor.fahrtEditor;

import javax.swing.table.DefaultTableModel;


public class FahrtEditorFahrplanTableModel extends DefaultTableModel
{
	public FahrtEditorFahrplanTableModel(String[] columns)
	{
		super(columns, 0);
	}
	public FahrtEditorFahrplanTableModel(String[][] cells, String[] columns)
	{
		super(cells, columns);
	}
	
	public boolean isCellEditable(int row, int col)
	{
		return col > 1;
	}
}
