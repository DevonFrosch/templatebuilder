package de.stsFanGruppe.templatebuilder.editor;

import java.awt.Component;

import de.stsFanGruppe.templatebuilder.config.GeneralConfig;
import de.stsFanGruppe.tools.NullTester;

public abstract class EditorGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EditorGUIController.class);
	
	protected GeneralConfig config;
	private Object configChangeHandleId;
	private Object fahrtConfigChangeHandleId;
	private Object schachtelungChangedHandleId;
	protected EditorDaten editorDaten;
	
	protected EditorGUIController(EditorDaten editorDaten, GeneralConfig config)
	{
		NullTester.test(config);
		
		this.config = config;
		configChangeHandleId = config.getBildfahrplanConfig().registerChangeHandler(() -> configChanged());
		fahrtConfigChangeHandleId = config.getFahrtDarstellungConfig().registerChangeHandler(() -> configChanged());
		schachtelungChangedHandleId = editorDaten.registerSchachtelungChangedCallback(() -> configChanged());
		setEditorDaten(editorDaten);
	}
	
	protected EditorGUIController(GeneralConfig config)
	{
		this(new EditorDaten(), config);
	}
	
	public EditorDaten getEditorDaten()
	{
		return editorDaten;
	}
	protected void setEditorDaten(EditorDaten editorDaten)
	{
		NullTester.test(editorDaten);
		this.editorDaten = editorDaten;
	}
	
	public abstract Component getGUI();
	
	public abstract Component getColumnHeader();
	
	public abstract Component getRowHeader();
	
	public void close()
	{
		config.getBildfahrplanConfig().unregisterChangeHandler(configChangeHandleId);
		config.getFahrtDarstellungConfig().unregisterChangeHandler(fahrtConfigChangeHandleId);
		if(editorDaten != null)
		{
			editorDaten.unregisterSchachtelungChangedCallback(schachtelungChangedHandleId);
			editorDaten.close();
		}
	}
	
	public abstract void configChanged();
}
