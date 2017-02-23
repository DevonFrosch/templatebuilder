package de.stsFanGruppe.templatebuilder.editor.tabEditor;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import de.stsFanGruppe.tools.NullTester;

public class TabEditorZeilenheaderGUI extends JPanel
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TabEditorZeilenheaderGUI.class);
	
	protected TabEditorGUIController controller;
	public JTable table;
	
	public TabEditorZeilenheaderGUI(TabEditorGUIController controller)
	{
		NullTester.test(controller);
		
		this.controller = controller;
		
		init();
	}
	
	public void init()
	{
		setLayout(new BorderLayout(0, 0));
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		add(table);
	}
	
	public int[] getSelectedRows()
	{
		return table.getSelectedRows();
	}
	public void setSelectionInterval(int start, int end)
	{
		table.setRowSelectionInterval(start, end);
	}
	
	public void setRows(String[] rows)
	{
		if(rows == null)
		{
			table.setModel(new DefaultTableModel());
		}
		
		Object[][] data = new Object[rows.length][1];
		
		for(int i=0; i < rows.length; i++)
		{
			data[i][0] = rows[i];
		}
		
		table.setModel(new DefaultTableModel(data, new String[]{""}));
	}
	
}
