package de.stsFanGruppe.templatebuilder.editor;

import de.stsFanGruppe.templatebuilder.config.GeneralConfig;
import de.stsFanGruppe.tools.NullTester;

public abstract class EditorGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EditorGUIController.class);
	
	protected GeneralConfig config;
	private Object configChangeHandleId;
	private Object fahrtConfigChangeHandleId;
	private Object editorDatenStreckeChangeHandleId = null;
	private Object editorDatenFahrtenChangeHandleId = null;
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
		
		this.setEditorDaten(new EditorDaten());
	}
	
	protected EditorGUIController(EditorDaten editorDaten, GeneralConfig config)
	{
		this(config);
		this.setEditorDaten(editorDaten);
	}
	
	protected void setEditorDaten(EditorDaten daten)
	{
		NullTester.test(daten);
		this.editorDaten = daten;
		log.info("setEditorDaten: callbacks");
		this.editorDatenStreckeChangeHandleId = daten.registerStreckeGeladenCallback(() -> {
			log.info("Callback für StreckeGeladen");
			dataChanged();
		});
		this.editorDatenFahrtenChangeHandleId = daten.registerFahrtenGeladenCallback(() -> {
			log.info("Callback für FahrtenGeladen");
			dataChanged();
		});
	}
	
	/**
	 * Sollte von Kindklassen überschrieben werden
	 */
	public void configChanged()
	{
		// nichts tun
	}

	/**
	 * Sollte von Kindklassen überschrieben werden
	 */
	public void dataChanged()
	{
		// nichts tun
	}
	
	public void close()
	{
		config.getBildfahrplanConfig().unregisterChangeHandler(configChangeHandleId);
		config.getFahrtDarstellungConfig().unregisterChangeHandler(fahrtConfigChangeHandleId);
		if(editorDaten != null) {
			editorDaten.unregisterStreckeGeladenCallback(editorDatenStreckeChangeHandleId);
			editorDaten.unregisterFahrtenGeladenCallback(editorDatenFahrtenChangeHandleId);
		}
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
