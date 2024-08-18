package de.stsFanGruppe.templatebuilder.gui;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import de.stsFanGruppe.templatebuilder.editor.EditorDaten;
import de.stsFanGruppe.templatebuilder.editor.EditorGUI;
import de.stsFanGruppe.templatebuilder.editor.EditorGUIController;
import de.stsFanGruppe.templatebuilder.editor.GUIType;
import de.stsFanGruppe.templatebuilder.editor.bildfahrplan.BildfahrplanGUIController;
import de.stsFanGruppe.tools.ButtonTabComponent;
import de.stsFanGruppe.tools.NullTester;

public class TemplateBuilderTabs
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TemplateBuilderTabs.class);
	
	protected JTabbedPane pane;
	
	public TemplateBuilderTabs(JTabbedPane pane)
	{
		NullTester.test(pane);
		this.pane = pane;
	}
	
	protected int addTab(String name, Icon icon, String toolTip, EditorGUIController guiController)
	{
		assert guiController != null;
		
		JScrollPane scrollPane = new JScrollPane();
		
		if(guiController.getClass().isAssignableFrom(BildfahrplanGUIController.class))
		{
			scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		}
		
		scrollPane.setViewportView(guiController.getGUI());
		if(guiController.getColumnHeader() != null)
		{
			scrollPane.setColumnHeaderView(guiController.getColumnHeader());
		}
		if(guiController.getRowHeader() != null)
		{
			scrollPane.setRowHeaderView(guiController.getRowHeader());
		}
		
		if(name == null)
		{
			name = "Leer";
		}
		
		int tabIndex = 0;
		synchronized(this)
		{
			pane.addTab(name, icon, scrollPane, toolTip);
			tabIndex = pane.getTabCount() - 1;
			
			// Kopie für Lambda-Funktion
			int localTabIndex = tabIndex;
			
			pane.setTabComponentAt(tabIndex, new ButtonTabComponent(pane, (index) -> {
				if(index == localTabIndex)
				{
					guiController.close();
				}
				return true;
			}));
		}
		return tabIndex;
	}
	
	public int getSelectedTab()
	{
		return pane.getSelectedIndex();
	}
	
	public void setSelectedTab(int index)
	{
		pane.setSelectedIndex(index);
	}
	
	public EditorGUI getSelectedGUI()
	{
		return getGUIAt(getSelectedTab());
	}
	
	public EditorGUI getGUIAt(int index)
	{
		if(index < 0)
		{
			return null;
		}
		JScrollPane scrollPane = (JScrollPane) pane.getComponentAt(index);
		if(scrollPane == null || scrollPane.getViewport() == null)
		{
			return null;
		}
		Component view = scrollPane.getViewport().getView();
		if(!(view instanceof EditorGUI))
		{
			return null;
		}
		return (EditorGUI) view;
	}
	
	public GUIType getGUITypeOfSelectedTab() {
		EditorGUI view = getSelectedGUI();
		if(view == null) {
			return null;
		}
		return view.getGUIType();
	}
	
	public int getTabIndexOf(Component gui)
	{
		for(int i = 0; i < pane.getTabCount(); i++)
		{
			if(gui.equals(getGUIAt(i)))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Holt das EditorDaten-Objekt des aktuellen Tabs.
	 * 
	 * @return Das EditorDaten-Objekt oder null falls der Tab keine EditorDaten hat
	 */
	public EditorDaten getSelectedEditorDaten()
	{
		try
		{
			EditorGUI bfpGUI = getSelectedGUI();
			
			if(bfpGUI == null)
			{
				return null;
			}
			
			return bfpGUI.getController().getEditorDaten();
		}
		catch(ClassCastException e)
		{
			log.warn("ClassCastException", e);
		}
		
		return null;
	}
}
