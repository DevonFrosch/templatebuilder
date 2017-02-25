package de.stsFanGruppe.templatebuilder.editor.tabEditor;

import javax.swing.JPanel;
import de.stsFanGruppe.templatebuilder.editor.EditorGUI;
import de.stsFanGruppe.templatebuilder.editor.EditorGUIController;
import de.stsFanGruppe.tools.NullTester;
import javax.swing.JTable;
import java.awt.BorderLayout;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

public class TabEditorGUI extends JPanel implements EditorGUI
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TabEditorGUI.class);
	
	protected TabEditorGUIController controller;
	private JTable table;
	
	public TabEditorGUI(TabEditorGUIController controller)
	{
		NullTester.test(controller);
		
		this.controller = controller;
		
		init();
	}
	
	/**
	 * Create the panel.
	 */
	private TabEditorGUI()
	{
		init();
	}
	
	public void init()
	{
		setLayout(new BorderLayout(0, 0));
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		// table.setModel(new DefaultTableModel(new String[] {"", "Zug 1"}, 1));
		add(table);
	}
	
	public boolean isRichtungAufsteigend()
	{
		return controller.richtungAufsteigend;
	}
	
	public JTableHeader getTableHeader()
	{
		return table.getTableHeader();
	}
	
	public void setTableModel(TableModel model)
	{
		table.setModel(model);
	}
	
	public void setTableValueAt(String content, int row, int column)
	{
		table.setValueAt(content, row, column);
	}
	
	public EditorGUIController getController()
	{
		return controller;
	}
}
