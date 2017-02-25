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
import de.stsFanGruppe.tools.FirstLastLinkedList;
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
	
	protected FirstLastLinkedList<BooleanSupplier> noEditorCallbacks = new FirstLastLinkedList<>();
	protected FirstLastLinkedList<Runnable> nameChangedCallbacks = new FirstLastLinkedList<>();
	protected FirstLastLinkedList<Runnable> fahrtenGeladenCallbacks = new FirstLastLinkedList<>();
	protected FirstLastLinkedList<Runnable> streckeGeladenCallbacks = new FirstLastLinkedList<>();
	
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
			for(BooleanSupplier callback : noEditorCallbacks)
			{
				if(!callback.getAsBoolean())
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public void addNoEditorCallback(BooleanSupplier noEditorCallback)
	{
		synchronized(noEditorCallbacks)
		{
			noEditorCallbacks.add(noEditorCallback);
		}
	}
	
	protected void nameChanged()
	{
		synchronized(nameChangedCallbacks)
		{
			for(Runnable callback : nameChangedCallbacks)
			{
				EventQueue.invokeLater(callback);
			}
		}
	}
	
	public void addNameChangedCallback(Runnable callback)
	{
		synchronized(nameChangedCallbacks)
		{
			nameChangedCallbacks.add(callback);
		}
	}
	
	protected void fahrtenGeladen()
	{
		synchronized(fahrtenGeladenCallbacks)
		{
			for(Runnable callback : fahrtenGeladenCallbacks)
			{
				EventQueue.invokeLater(callback);
			}
		}
	}
	
	public void addFahrtenGeladenCallback(Runnable callback)
	{
		synchronized(fahrtenGeladenCallbacks)
		{
			fahrtenGeladenCallbacks.add(callback);
		}
	}
	
	protected void streckeGeladen()
	{
		synchronized(streckeGeladenCallbacks)
		{
			for(Runnable callback : streckeGeladenCallbacks)
			{
				EventQueue.invokeLater(callback);
			}
		}
	}
	
	public void addStreckeGeladenCallback(Runnable callback)
	{
		synchronized(streckeGeladenCallbacks)
		{
			streckeGeladenCallbacks.add(callback);
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
