package de.stsFanGruppe.templatebuilder.editor;

import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.tools.NullTester;

public abstract class EditorGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EditorGUIController.class);
	
	protected BildfahrplanConfig config;
	private Object changeHandleId;
	protected GUI parent;
	protected EditorDaten editorDaten;
	
	protected EditorGUIController(EditorDaten editorDaten, BildfahrplanConfig config, TemplateBuilderGUI parent)
	{
		NullTester.test(editorDaten);
		NullTester.test(config);
		NullTester.test(parent);
		
		this.editorDaten = editorDaten;
		this.config = config;
		this.changeHandleId = config.registerChangeHandler(() -> configChanged());
		this.parent = parent;
	}
	protected EditorGUIController(BildfahrplanConfig config, TemplateBuilderGUI parent)
	{
		this(new EditorDaten(), config, parent);
	}
	
	public abstract void configChanged();
	public void close()
	{
		config.unregisterChangeHandler(changeHandleId);
	}
	
	public EditorDaten getEditorDaten()
	{
		return editorDaten;
	}
}
