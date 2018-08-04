package de.stsFanGruppe.templatebuilder.editor;

import de.stsFanGruppe.templatebuilder.config.GeneralConfig;
import de.stsFanGruppe.tools.NullTester;

public abstract class EditorGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EditorGUIController.class);
	
	protected GeneralConfig config;
	private Object configChangeHandleId;
	private Object fahrtConfigChangeHandleId;
	protected EditorDaten editorDaten = null;
	
	/**
	 * Erzeugt ein neues Objekt. Dieser Konstruktor wird verwendet, um ein neues
	 * 
	 * @param bildfahrplanConfig
	 * @param parent
	 */
	protected EditorGUIController(GeneralConfig config)
	{
		NullTester.test(config);
		
		this.config = config;
		this.configChangeHandleId = config.getBildfahrplanConfig().registerChangeHandler(() -> configChanged());
		this.fahrtConfigChangeHandleId = config.getFahrtDarstellungConfig().registerChangeHandler(() -> configChanged());
		
		this.editorDaten = new EditorDaten();
	}
	
	protected EditorGUIController(EditorDaten editorDaten, GeneralConfig config)
	{
		this(config);
		this.editorDaten = editorDaten;
	}
	
	protected void setEditorDaten(EditorDaten daten)
	{
		this.editorDaten = daten;
	}
	
	public abstract void configChanged();
	
	public void close()
	{
		config.getBildfahrplanConfig().unregisterChangeHandler(configChangeHandleId);
		config.getFahrtDarstellungConfig().unregisterChangeHandler(fahrtConfigChangeHandleId);
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
