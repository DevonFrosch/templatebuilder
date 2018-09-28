package de.stsFanGruppe.templatebuilder.editor;

import java.awt.EventQueue;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;
import de.stsFanGruppe.templatebuilder.editor.bildfahrplan.BildfahrplanGUIController;
import de.stsFanGruppe.templatebuilder.editor.tabEditor.TabEditorGUIController;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Strecke;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.NullTester;

/**
 * Enthält die Daten einer Bildfahrplanstrecke für verschiedene Ansichten
 */
public class EditorDaten
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EditorDaten.class);
	
	protected BildfahrplanGUIController bfpController = null;
	protected TabEditorGUIController tabHinController = null;
	protected TabEditorGUIController tabRückController = null;
	
	private Map<Object, BooleanSupplier> noEditorCallbacks = new HashMap<>();
	private int noEditorCallbackCounter = 0;
	private Map<Object, Runnable> nameChangedCallbacks = new HashMap<>();
	private int nameChangedCallbackCounter = 0;
	private Map<Object, Runnable> fahrtenGeladenCallbacks = new HashMap<>();
	private int fahrtenGeladenCallbackCounter = 0;
	private Map<Object, Runnable> streckeGeladenCallbacks = new HashMap<>();
	private int streckeGeladenCallbackCounter = 0;
	
	protected String name = null;
	
	protected Streckenabschnitt streckenabschnitt;
	protected Object saLock = new Object();
	
	protected Map<Betriebsstelle, Double> streckenKm;
	protected Object streckenKmLock = new Object();
	
	protected double diffKm = -1;
	
	protected Set<Fahrt> fahrten;
	protected Object fahrtenLock = new Object();
	
	public EditorDaten()
	{
		
	}
	
	public EditorDaten(String name)
	{
		this();
		this.name = name;
	}
	
	public EditorDaten(BildfahrplanGUIController controller)
	{
		this();
		this.setBildfahrplan(controller);
	}
	
	public EditorDaten(BildfahrplanGUIController controller, String name)
	{
		this(controller);
		this.name = name;
	}
	
	public EditorDaten(TabEditorGUIController controller, boolean richtungAufsteigend)
	{
		this();
		this.setTabEditor(controller, richtungAufsteigend);
	}
	
	public EditorDaten(TabEditorGUIController controller, boolean richtungAufsteigend, String name)
	{
		this(controller, richtungAufsteigend);
		this.name = name;
	}
	
	public boolean hasBildfahrplan()
	{
		return bfpController != null;
	}
	
	public BildfahrplanGUIController getBildfahrplan()
	{
		return bfpController;
	}
	
	public void setBildfahrplan(BildfahrplanGUIController controller)
	{
		this.bfpController = controller;
	}
	
	public boolean hasTabEditor(boolean richtungAufsteigend)
	{
		return getTabEditor(richtungAufsteigend) != null;
	}
	
	public TabEditorGUIController getTabEditor(boolean richtungAufsteigend)
	{
		return((richtungAufsteigend) ? tabHinController : tabRückController);
	}
	
	public void setTabEditor(TabEditorGUIController controller, boolean richtungAufsteigend)
	{
		if(richtungAufsteigend)
		{
			// Wenn das der letzte Editor ist, nachfragen
			if(!hasBildfahrplan() && !hasTabEditorRück() && noEditor())
			{
				return;
			}
			this.tabHinController = controller;
		}
		else
		{
			// Wenn das der letzte Editor ist, nachfragen
			if(!hasBildfahrplan() && !hasTabEditorHin() && noEditor())
			{
				return;
			}
			this.tabRückController = controller;
		}
	}
	
	public boolean hasTabEditorHin()
	{
		return hasTabEditor(true);
	}
	
	public boolean hasTabEditorRück()
	{
		return hasTabEditor(false);
	}
	
	public TabEditorGUIController getTabEditorHin()
	{
		return getTabEditor(true);
	}
	
	public TabEditorGUIController getTabEditorRück()
	{
		return getTabEditor(false);
	}
	
	public void setTabEditorHin(TabEditorGUIController controller)
	{
		setTabEditor(controller, true);
	}
	
	public void setTabEditorRück(TabEditorGUIController controller)
	{
		setTabEditor(controller, false);
	}
	
	public boolean hasEditoren()
	{
		return hasBildfahrplan() || hasTabEditorHin() || hasTabEditorRück();
	}
	
	protected boolean noEditor()
	{
		synchronized(noEditorCallbacks)
		{
			for(BooleanSupplier callback : noEditorCallbacks.values())
			{
				if(!callback.getAsBoolean())
				{
					return false;
				}
			}
		}
		return true;
	}
	public Object registerNoEditorCallback(BooleanSupplier callback)
	{
		if(callback == null) {
			return null;
		}
		synchronized(noEditorCallbacks)
		{
			Object handlerID = Integer.valueOf(noEditorCallbackCounter++);
			log.debug("registerNoEditorCallback (ID {}) auf {}", handlerID, this.getClass().getName());
			noEditorCallbacks.put(handlerID, callback);
			return handlerID;
		}
	}
	public void unregisterNoEditorCallback(Object handlerID) {
		synchronized(noEditorCallbacks)
		{
			if(handlerID == null) {
				return;
			}
			log.debug("unregisterNoEditorCallback (ID {})", handlerID);
			noEditorCallbacks.remove(handlerID);
		}
	}
	
	protected void nameChanged()
	{
		synchronized(nameChangedCallbacks)
		{
			for(Runnable callback : nameChangedCallbacks.values())
			{
				EventQueue.invokeLater(callback);
			}
		}
	}
	public Object registerNameChangedCallback(Runnable callback)
	{
		if(callback == null) {
			return null;
		}
		synchronized(nameChangedCallbacks)
		{
			Object handlerID = Integer.valueOf(nameChangedCallbackCounter++);
			log.debug("registerNameChangedCallback (ID {}) auf {}", handlerID, this.getClass().getName());
			nameChangedCallbacks.put(handlerID, callback);
			return handlerID;
		}
	}
	public void unregisterNameChangedCallback(Object handlerID) {
		synchronized(nameChangedCallbacks)
		{
			if(handlerID == null) {
				return;
			}
			log.debug("unregisterNameChangedCallback (ID {})", handlerID);
			nameChangedCallbacks.remove(handlerID);
		}
	}
	
	protected void fahrtenGeladen()
	{
		synchronized(fahrtenGeladenCallbacks)
		{
			for(Runnable callback : fahrtenGeladenCallbacks.values())
			{
				EventQueue.invokeLater(callback);
			}
		}
	}
	public Object registerFahrtenGeladenCallback(Runnable callback)
	{
		if(callback == null) {
			return null;
		}
		synchronized(fahrtenGeladenCallbacks)
		{
			Object handlerID = Integer.valueOf(fahrtenGeladenCallbackCounter++);
			log.debug("registerFahrtenGeladenCallback (ID {}) auf {}", handlerID, this.getClass().getName());
			fahrtenGeladenCallbacks.put(handlerID, callback);
			return handlerID;
		}
	}
	public void unregisterFahrtenGeladenCallback(Object handlerID) {
		synchronized(fahrtenGeladenCallbacks)
		{
			if(handlerID == null) {
				return;
			}
			log.debug("unregisterFahrtenGeladenCallback (ID {})", handlerID);
			fahrtenGeladenCallbacks.remove(handlerID);
		}
	}
	
	protected void streckeGeladen()
	{
		synchronized(streckeGeladenCallbacks)
		{
			for(Runnable callback : streckeGeladenCallbacks.values())
			{
				EventQueue.invokeLater(callback);
			}
		}
	}
	public Object registerStreckeGeladenCallback(Runnable callback)
	{
		if(callback == null) {
			return null;
		}
		synchronized(streckeGeladenCallbacks)
		{
			Object handlerID = Integer.valueOf(streckeGeladenCallbackCounter++);
			log.debug("registerStreckeGeladenCallback (ID {}) auf {}", handlerID, this.getClass().getName());
			streckeGeladenCallbacks.put(handlerID, callback);
			return handlerID;
		}
	}
	public void unregisterStreckeGeladenCallback(Object handlerID) {
		synchronized(streckeGeladenCallbacks)
		{
			if(handlerID == null) {
				return;
			}
			log.debug("unregisterStreckeGeladenCallback (ID {})", handlerID);
			streckeGeladenCallbacks.remove(handlerID);
		}
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void ladeStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		NullTester.test(streckenabschnitt);
		log.trace("ladeStreckenabschnitt()");
		
		double streckenlaenge = 0;
		double letzterAlterKm = 0;
		double letzterNeuerKm = 0;
		
		// Setzte Strecke und Fahrten zurück
		reset();
		
		synchronized(saLock)
		{
			this.streckenabschnitt = streckenabschnitt;
		}
		
		// km für Betriebsstelle: Mittelwert aus getMinKm und getMaxKm: (max+min)/2
		Betriebsstelle b = streckenabschnitt.getStrecken().first().getAnfang();
		
		synchronized(streckenKmLock)
		{
			streckenKm.put(b, new Double(0.0));
		}
		
		letzterAlterKm = (b.getMaxKm() + b.getMinKm()) / 2;
		
		// Vorbereitung für unterschiedliche Strecken-km
		double neuerKm = -1;
		for(Strecke s : streckenabschnitt.getStrecken())
		{
			b = s.getEnde();
			double alterKm = (b.getMaxKm() + b.getMinKm()) / 2;
			neuerKm = alterKm - letzterAlterKm + letzterNeuerKm;
			
			synchronized(streckenKmLock)
			{
				streckenKm.put(b, new Double(neuerKm));
			}
			
			letzterAlterKm = alterKm;
			letzterNeuerKm = neuerKm;
		}
		diffKm = neuerKm;
		
		this.name = streckenabschnitt.getName();
		
		streckeGeladen();
	}
	
	public void ladeZüge(Collection<? extends Fahrt> fahrten)
	{
		NullTester.test(fahrten);
		synchronized(fahrtenLock)
		{
			if(this.fahrten == null)
			{
				this.fahrten = new HashSet<>();
			}
			log.trace("ladeZüge()");
			
			fahrten.forEach((f) -> {
				NullTester.test(f);
				this.fahrten.add(f);
			});
		}
		
		fahrtenGeladen();
	}
	
	public Streckenabschnitt getStreckenabschnitt()
	{
		synchronized(saLock)
		{
			if(streckenabschnitt == null)
			{
				return null;
			}
			return (Streckenabschnitt) streckenabschnitt.clone();
		}
	}
	
	public boolean hasStreckenabschnitt()
	{
		synchronized(saLock)
		{
			if(streckenabschnitt == null)
			{
				return false;
			}
		}
		synchronized(streckenKmLock)
		{
			if(streckenKm == null)
			{
				return false;
			}
		}
		return true;
	}
	
	public Set<Fahrt> getFahrten()
	{
		synchronized(fahrtenLock)
		{
			if(fahrten == null)
			{
				return null;
			}
			return new HashSet<>(fahrten);
		}
	}
	
	public String[] getFahrtTreffer(String suchmuster)
	{
		synchronized(fahrtenLock)
		{
			return fahrten.stream().filter(f -> f.getName().toLowerCase().contains(suchmuster.toLowerCase())).map(Fahrt::getName).toArray(String[]::new);
		}
	}
	
	public Fahrt getFahrt(String name)
	{
		if(name == null)
		{
			return null;
		}
		synchronized(fahrtenLock)
		{
			return fahrten.stream().filter((f) -> f.getName().equals(name)).findFirst().orElse(null);
		}
	}
	
	public Fahrt getFahrt(long fahrtId)
	{
		synchronized(fahrtenLock)
		{
			return fahrten.stream().filter((f) -> f.getFahrtId() == fahrtId).findFirst().orElse(null);
		}
	}
	
	public boolean hasFahrten()
	{
		synchronized(fahrtenLock)
		{
			return fahrten != null && !fahrten.isEmpty();
		}
	}
	
	public double getDiffKm()
	{
		return diffKm;
	}
	
	public double getMinZeit()
	{
		if(!hasFahrten())
		{
			throw new NullPointerException();
		}
		synchronized(fahrtenLock)
		{
			return fahrten.stream().min((a, b) -> Double.compare(a.getMinZeit(), b.getMinZeit())).get().getMinZeit();
		}
	}
	
	public double getMaxZeit()
	{
		if(!hasFahrten())
		{
			throw new NullPointerException();
		}
		synchronized(fahrtenLock)
		{
			return fahrten.stream().max((a, b) -> Double.compare(a.getMaxZeit(), b.getMaxZeit())).get().getMaxZeit();
		}
	}
	
	public double getStreckenKm(Betriebsstelle index)
	{
		synchronized(streckenKmLock)
		{
			return streckenKm.get(index);
		}
	}
	
	public void reset()
	{
		synchronized(saLock)
		{
			streckenabschnitt = null;
		}
		synchronized(streckenKmLock)
		{
			streckenKm = new HashMap<>();
		}
		synchronized(fahrtenLock)
		{
			fahrten = null;
		}
	}
}
