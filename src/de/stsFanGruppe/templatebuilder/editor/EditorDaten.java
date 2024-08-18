package de.stsFanGruppe.templatebuilder.editor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

import de.stsFanGruppe.templatebuilder.editor.bildfahrplan.BildfahrplanGUIController;
import de.stsFanGruppe.templatebuilder.editor.tabEditor.TabEditorGUIController;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Strecke;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.types.Schachtelung;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.templatebuilder.zug.Template;
import de.stsFanGruppe.tools.CallbackHandler;
import de.stsFanGruppe.tools.FeedbackCallbackHandler;
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
	
	protected FeedbackCallbackHandler noEditorCallbacks = new FeedbackCallbackHandler();
	protected CallbackHandler nameChangedCallbacks = new CallbackHandler();
	protected CallbackHandler schachtelungChangedCallbacks = new CallbackHandler();
	protected CallbackHandler fahrtenGeladenCallbacks = new CallbackHandler();
	protected CallbackHandler streckeGeladenCallbacks = new CallbackHandler();
	
	protected String name = null;
	
	protected Streckenabschnitt streckenabschnitt;
	protected Object saLock = new Object();
	
	protected Map<Betriebsstelle, Double> streckenKm = new HashMap<>();
	protected Object streckenKmLock = new Object();
	
	protected double diffKm = -1;
	
	protected Set<Template> templates = new HashSet<>();
	protected Object templateLock = new Object();
	
	protected Schachtelung schachtelung = Schachtelung.KEINE;
	protected int schachtelungMinuten = 1440;
	protected Template schachtelungTemplate = null;
	
	// Konstruktoren
	
	public EditorDaten()
	{}
	
	public EditorDaten(String name)
	{
		this.name = name;
	}
	
	public EditorDaten(BildfahrplanGUIController controller)
	{
		this.setBildfahrplan(controller);
	}
	
	public EditorDaten(BildfahrplanGUIController controller, String name)
	{
		this(controller);
		this.name = name;
	}
	
	public EditorDaten(TabEditorGUIController controller, boolean richtungAufsteigend)
	{
		this.setTabEditor(controller, richtungAufsteigend);
	}
	
	public EditorDaten(TabEditorGUIController controller, boolean richtungAufsteigend, String name)
	{
		this(controller, richtungAufsteigend);
		this.name = name;
	}
	
	// Getter / Setter
	
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
			if(!hasBildfahrplan() && !hasTabEditorRück() && noEditorCallbacks.runAll())
			{
				return;
			}
			this.tabHinController = controller;
		}
		else
		{
			// Wenn das der letzte Editor ist, nachfragen
			if(!hasBildfahrplan() && !hasTabEditorHin() && noEditorCallbacks.runAll())
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
	
	public TabEditorGUIController getTabEditorHin()
	{
		return getTabEditor(true);
	}
	
	public void setTabEditorHin(TabEditorGUIController controller)
	{
		setTabEditor(controller, true);
	}
	
	public boolean hasTabEditorRück()
	{
		return hasTabEditor(false);
	}
	
	public TabEditorGUIController getTabEditorRück()
	{
		return getTabEditor(false);
	}
	
	public void setTabEditorRück(TabEditorGUIController controller)
	{
		setTabEditor(controller, false);
	}
	
	public boolean hasEditoren()
	{
		return hasBildfahrplan() || hasTabEditorHin() || hasTabEditorRück();
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
		nameChangedCallbacks.runAll();
	}
	
	public Schachtelung getSchachtelung()
	{
		return schachtelung;
	}
	
	public void setSchachtelung(Schachtelung schachtelung)
	{
		if(schachtelung == null)
		{
			this.schachtelung = Schachtelung.KEINE;
		}
		this.schachtelung = schachtelung;
		schachtelungChangedCallbacks.runAll();
	}
	
	public int getSchachtelungMinuten()
	{
		return schachtelungMinuten;
	}
	
	public void setSchachtelungMinuten(int schachtelungMinuten)
	{
		this.schachtelungMinuten = schachtelungMinuten;
		schachtelungChangedCallbacks.runAll();
	}
	
	public Template getSchachtelungTemplate()
	{
		return schachtelungTemplate;
	}
	
	public void setSchachtelungTemplate(Template schachtelungTemplate)
	{
		this.schachtelungTemplate = schachtelungTemplate;
		schachtelungChangedCallbacks.runAll();
	}
	
	// Callbacks
	
	public Object registerNoEditorCallback(BooleanSupplier callback)
	{
		return noEditorCallbacks.register(callback);
	}
	
	public void unregisterNoEditorCallback(Object callbackId)
	{
		noEditorCallbacks.unregister(callbackId);
	}
	
	public Object registerNameChangedCallback(Runnable callback)
	{
		return nameChangedCallbacks.register(callback);
	}
	
	public void unregisterNameChangedCallback(Object callbackId)
	{
		nameChangedCallbacks.unregister(callbackId);
	}
	
	public Object registerSchachtelungChangedCallback(Runnable callback)
	{
		return schachtelungChangedCallbacks.register(callback);
	}
	
	public void unregisterSchachtelungChangedCallback(Object callbackId)
	{
		schachtelungChangedCallbacks.unregister(callbackId);
	}
	
	public Object registerFahrtenGeladenCallback(Runnable callback)
	{
		return fahrtenGeladenCallbacks.register(callback);
	}
	
	public void unregisterFahrtenGeladenCallback(Object callbackId)
	{
		fahrtenGeladenCallbacks.unregister(callbackId);
	}
	
	public Object registerStreckeGeladenCallback(Runnable callback)
	{
		return streckeGeladenCallbacks.register(callback);
	}
	
	public void unregisterStreckeGeladenCallback(Object callbackId)
	{
		streckeGeladenCallbacks.unregister(callbackId);
	}
	
	// Strecken und Fahrten
	
	public void ladeStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		NullTester.test(streckenabschnitt);
		log.trace("ladeStreckenabschnitt()");
		
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
			streckenKm.put(b, 0.0);
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
				streckenKm.put(b, Double.valueOf(neuerKm));
			}
			
			letzterAlterKm = alterKm;
			letzterNeuerKm = neuerKm;
		}
		diffKm = neuerKm;
		
		this.name = streckenabschnitt.getName();
		
		streckeGeladenCallbacks.runAll();
	}
	
	public void ladeTemplates(Collection<? extends Template> templates)
	{
		NullTester.test(templates);
		synchronized(templateLock)
		{
			log.trace("ladeTemplates()");
			this.templates.clear();
			
			templates.forEach((f) -> {
				NullTester.test(f);
				this.templates.add(f);
			});
		}
		
		fahrtenGeladenCallbacks.runAll();
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
		return true;
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
	
	public Set<Template> getTemplates()
	{
		synchronized(templateLock)
		{
			return new HashSet<>(templates);
		}
	}
	
	public Set<Fahrt> getFahrten()
	{
		synchronized(templateLock)
		{
			Set<Fahrt> fahrten = new HashSet<>();
			
			for(Template template : templates)
			{
				fahrten.addAll(template.getFahrten());
			}
			
			return fahrten;
		}
	}
	
	public String[] getFahrtTreffer(String suchmuster)
	{
		synchronized(templateLock)
		{
			return getFahrten().stream().filter(f -> f.getName().toLowerCase().contains(suchmuster.toLowerCase())).map(Fahrt::getName).toArray(String[]::new);
		}
	}
	
	public Fahrt getFahrt(String name)
	{
		if(name == null)
		{
			return null;
		}
		synchronized(templateLock)
		{
			return templates.stream().map(t -> t.findFahrt(name)).filter(t -> t != null).findFirst().orElse(null);
		} 
	}
	
	public Fahrt getFahrt(long fahrtId)
	{
		synchronized(templateLock)
		{
			return templates.stream().map(t -> t.findFahrt(fahrtId)).filter(t -> t != null).findFirst().orElse(null);
		}
	}
	
	public boolean hasFahrten()
	{
		synchronized(templateLock)
		{
			return templates.stream().anyMatch(t -> t.hasFahrten());
		}
	}
	
	public void updateFahrt(long fahrtId, Function<Fahrt, Fahrt> updater)
	{
		synchronized(templateLock)
		{
			Fahrt fahrt = templates.stream().map(t -> t.findFahrt(fahrtId)).filter(t -> t != null).findFirst().orElse(null);
			fahrt = updater.apply(fahrt);
		}
		fahrtenGeladenCallbacks.runAll();
	}
	
	public double getDiffKm()
	{
		return diffKm;
	}
	
	public OptionalDouble getMinZeit()
	{
		synchronized(templateLock)
		{
			return templates.stream().map(t -> t.getMinZeit()).filter(optional -> optional.isPresent())
					.min((a, b) -> Double.compare(a.getAsDouble(), b.getAsDouble())).orElse(OptionalDouble.empty());
		}
	}
	
	public OptionalDouble getMaxZeit()
	{
		synchronized(templateLock)
		{
			return templates.stream().map(t -> t.getMaxZeit()).filter(optional -> optional.isPresent())
					.max((a, b) -> Double.compare(a.getAsDouble(), b.getAsDouble())).orElse(OptionalDouble.empty());
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
			streckenKm.clear();
		}
		synchronized(templateLock)
		{
			templates.clear();
		}
		
		schachtelung = Schachtelung.KEINE;
		schachtelungMinuten = 1440;
		schachtelungTemplate = null;
	}
}
