package de.stsFanGruppe.templatebuilder.editor;

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
 * Enth�lt die Daten einer Bildfahrplanstrecke f�r verschiedene Ansichten
 */
public class EditorDaten
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EditorDaten.class);
	
	protected BildfahrplanGUIController bfpController = null;
	protected TabEditorGUIController tabHinController = null;
	protected TabEditorGUIController tabR�ckController = null;
	
	protected FirstLastLinkedList<BooleanSupplier> noEditorCallbacks = new FirstLastLinkedList<>();
	protected FirstLastLinkedList<Runnable> nameChangedCallbacks = new FirstLastLinkedList<>();
	
	protected String name = null;
	protected Streckenabschnitt streckenabschnitt;
	protected Map<Betriebsstelle, Double> streckenKm;
	protected double diffKm = -1;
	protected Set<Fahrt> fahrten;
	
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
		return ((richtungAufsteigend) ? tabHinController : tabR�ckController);
	}
	public void setTabEditor(TabEditorGUIController controller, boolean richtungAufsteigend)
	{
		if(richtungAufsteigend)
		{
			// Wenn das der letzte Editor ist, nachfragen
			if(!hasBildfahrplan() && !hasTabEditorR�ck() && noEditor())
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
			this.tabR�ckController = controller;
		}
	}

	public boolean hasTabEditorHin()
	{
		return hasTabEditor(true);
	}
	public boolean hasTabEditorR�ck()
	{
		return hasTabEditor(false);
	}
	public TabEditorGUIController getTabEditorHin()
	{
		return getTabEditor(true);
	}
	public TabEditorGUIController getTabEditorR�ck()
	{
		return getTabEditor(false);
	}
	public void setTabEditorHin(TabEditorGUIController controller)
	{
		setTabEditor(controller, true);
	}
	public void setTabEditorR�ck(TabEditorGUIController controller)
	{
		setTabEditor(controller, false);
	}
	
	public boolean hasEditoren()
	{
		return hasBildfahrplan() || hasTabEditorHin() || hasTabEditorR�ck();
	}
	
	protected boolean noEditor()
	{
		for(BooleanSupplier callback: noEditorCallbacks)
		{
			if(!callback.getAsBoolean())
			{
				return false;
			}
		}
		return true;
	}
	public void addNoEditorCallback(BooleanSupplier noEditorCallback)
	{
		noEditorCallbacks.add(noEditorCallback);
	}
	protected void nameChanged()
	{
		for(Runnable callback: nameChangedCallbacks)
		{
			callback.run();
		}
	}
	public void addNameChangedCallback(Runnable callback)
	{
		nameChangedCallbacks.add(callback);
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
		
		// Setzte Strecke und Fahrten zur�ck
		reset();
		
		this.streckenabschnitt = streckenabschnitt;
		
		// km f�r Betriebsstelle: Mittelwert aus getMinKm und getMaxKm: (max+min)/2
		Betriebsstelle b = streckenabschnitt.getStrecken().first().getAnfang();
		streckenKm.put(b, new Double(0.0));
		letzterAlterKm = (b.getMaxKm() + b.getMinKm()) / 2;
		
		// Vorbereitung f�r unterschiedliche Strecken-km
		double neuerKm = -1;
		for(Strecke s: streckenabschnitt.getStrecken())
		{
			b = s.getEnde();
			double alterKm = (b.getMaxKm() + b.getMinKm()) / 2;
			neuerKm = alterKm - letzterAlterKm + letzterNeuerKm;
			streckenKm.put(b, new Double(neuerKm));
			letzterAlterKm = alterKm;
			letzterNeuerKm = neuerKm;
		}
		diffKm = neuerKm;
		
		this.name = streckenabschnitt.getName();
	}
	public void ladeZ�ge(Collection<? extends Fahrt> fahrten)
	{
		NullTester.test(fahrten);
		if(this.fahrten == null)
		{
			this.fahrten = new HashSet<>();
		}
		log.trace("ladeZ�ge()");
		
		fahrten.forEach((f) -> {
			NullTester.test(f);
			this.fahrten.add(f);
		});
	}
	
	public Streckenabschnitt getStreckenabschnitt()
	{
		return streckenabschnitt;
	}
	public boolean hasStreckenabschnitt()
	{
		return streckenabschnitt != null && streckenKm != null;
	}
	public Set<Fahrt> getFahrten()
	{
		synchronized(fahrten)
		{
			return fahrten;
		}
	}
	public Set<Fahrt> getFahrtenCopy()
	{
		synchronized(fahrten)
		{
			return new HashSet<>(fahrten);
		}
	}
	public boolean hasFahrten()
	{
		return fahrten != null && !fahrten.isEmpty();
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
		synchronized(fahrten)
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
		synchronized(fahrten)
		{
			return fahrten.stream().max((a, b) -> Double.compare(a.getMaxZeit(), b.getMaxZeit())).get().getMaxZeit();
		}
	}
	public double getStreckenKm(Betriebsstelle index)
	{
		return streckenKm.get(index);
	}
	
	public void reset()
	{
		synchronized(this)
		{
			streckenabschnitt = null;
			streckenKm = new HashMap<>();
			fahrten = null;
		}
	}
}
