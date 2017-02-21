package de.stsFanGruppe.templatebuilder.editor.tabEditor;

import javax.swing.JPanel;
import de.stsFanGruppe.templatebuilder.editor.EditorGUI;
import de.stsFanGruppe.templatebuilder.editor.EditorGUIController;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.tools.NullTester;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.util.Collection;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;

public class TabEditorGUI extends JPanel implements EditorGUI
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TabEditorGUI.class);
	
	protected TabEditorGUIController controller;
	protected TemplateBuilderGUI parent;
	private JTable table;
	
	public TabEditorGUI(TabEditorGUIController controller, TemplateBuilderGUI parent)
	{
		NullTester.test(controller);
		NullTester.test(parent);
		
		this.controller = controller;
		this.parent = parent;
		
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
		//table.setModel(new DefaultTableModel(new String[] {"Zug 1"}, 1));
		add(table);
		
		JButton button = new JButton("+");
		add(button, BorderLayout.EAST);
		
		JButton button_1 = new JButton("+");
		add(button_1, BorderLayout.SOUTH);
		
	}
	
	public boolean isRichtungAufsteigend()
	{
		return controller.richtungAufsteigend;
	}
	
	public int getColumnCount()
	{
		return table.getColumnCount();
	}
	public String getColumnName(int index)
	{
		return table.getColumnName(index);
	}
	public void getColumn(int index)
	{
		
	}
	public void addColumn()
	{
		
	}
	public void insertColumn(int index)
	{
		
	}
	public void removeColumn(int index)
	{
		
	}
	public void removeAllColumns()
	{
		
	}
	
	public int getRowCount()
	{
		return table.getRowCount();
	}
	public void getRow(int index)
	{
		
	}
	public void setRows(Collection<String> names)
	{
		DefaultTableModel model = new DefaultTableModel();
		
	}
	public void addRow(String name)
	{
		
	}
	public void insertRow(int index)
	{
		
	}
	public void removeRow(int index)
	{
		
	}
	public void removeAllRows()
	{
		
	}
	
	public EditorGUIController getController()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
