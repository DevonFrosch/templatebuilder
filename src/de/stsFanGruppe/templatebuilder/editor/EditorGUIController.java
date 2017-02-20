package de.stsFanGruppe.templatebuilder.editor;

import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.templatebuilder.gui.TabController;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.tools.NullTester;

public abstract class EditorGUIController implements TabController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EditorGUIController.class);
	
	protected BildfahrplanConfig config;
	private Object changeHandleId;
	protected GUI parent;
	protected EditorData editorData;
	
	protected EditorGUIController(EditorData editorData, BildfahrplanConfig config, TemplateBuilderGUI parent)
	{
		NullTester.test(editorData);
		NullTester.test(config);
		NullTester.test(parent);
		
		this.editorData = editorData;
		this.config = config;
		this.changeHandleId = config.registerChangeHandler(() -> configChanged());
		this.parent = parent;
	}
	protected EditorGUIController(BildfahrplanConfig config, TemplateBuilderGUI parent)
	{
		this(new EditorData(), config, parent);
	}
	
	public abstract void configChanged();
	public void close()
	{
		config.unregisterChangeHandler(changeHandleId);
	}
	
	public EditorData getEditorData()
	{
		return editorData;
	}
}
