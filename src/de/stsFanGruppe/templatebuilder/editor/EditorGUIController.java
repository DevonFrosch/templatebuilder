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
	protected EditorDaten editorDaten = null;
	
	/**
	 * Erzeugt ein neues Objekt. Dieser Konstruktor wird verwendet, um ein neues 
	 * @param config
	 * @param parent
	 */
	protected EditorGUIController(BildfahrplanConfig config, TemplateBuilderGUI parent)
	{
		NullTester.test(config);
		NullTester.test(parent);
		
		this.config = config;
		this.changeHandleId = config.registerChangeHandler(() -> configChanged());
		this.parent = parent;
		this.editorDaten = new EditorDaten();
	}
	protected EditorGUIController(EditorDaten editorDaten, BildfahrplanConfig config, TemplateBuilderGUI parent)
	{
		this(config, parent);
		this.editorDaten = editorDaten;
	}
	
	protected void setEditorDaten(EditorDaten daten)
	{
		this.editorDaten = daten;
	}
	public abstract void configChanged();
	public void close()
	{
		config.unregisterChangeHandler(changeHandleId);
	}
	
	public EditorDaten getEditorDaten()
	{
		if(editorDaten == null)
		{
			throw new IllegalStateException("editorDaten ist null");
		}
		return editorDaten;
	}
}
