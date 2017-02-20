package de.stsFanGruppe.templatebuilder.editor.tabEditor;

import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.editor.EditorGUIController;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;

public class TabEditorGUIController extends EditorGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TabEditorGUIController.class);
	
	protected TabEditorGUI gui;
	
	public TabEditorGUIController(BildfahrplanConfig config, TemplateBuilderGUI parent)
	{
		super(config, parent);
		
		this.gui = new TabEditorGUI(this, parent);
	}
	
	@Override
	public void configChanged()
	{
		// TODO Auto-generated method stub
		
	}
}
