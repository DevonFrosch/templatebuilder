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
	protected BooleanSupplier noEditorCallback = null;
	
	protected Streckenabschnitt streckenabschnitt;
	protected Map<Betriebsstelle, Double> streckenKm;
	protected double diffKm = -1;
	protected Set<Fahrt> fahrten;
	
	public EditorDaten()
	{
		
	}
	public EditorDaten(BooleanSupplier noEditorCallback)
	{
		this.addNoEditorCallback(noEditorCallback);
	}
	public EditorDaten(BildfahrplanGUIController controller)
	{
		this();
		this.setBildfahrplan(controller);
	}
	public EditorDaten(BildfahrplanGUIController controller, BooleanSupplier noEditorCallback)
	{
		this(controller);
		this.addNoEditorCallback(noEditorCallback);
	}
	public EditorDaten(TabEditorGUIController controller, boolean richtungAufsteigend)
	{
		this();
		this.setTabEditor(controller, richtungAufsteigend);
	}
	public EditorDaten(TabEditorGUIController controller, boolean richtungAufsteigend, BooleanSupplier noEditorCallback)
	{
		this(controller, richtungAufsteigend);
		this.addNoEditorCallback(noEditorCallback);
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
		return ((richtungAufsteigend) ? tabHinController : tabRückController);
	}
	public void setTabEditor(TabEditorGUIController controller, boolean richtungAufsteigend)
	{
		if(richtungAufsteigend)
		{
			// Wenn das der letzte Editor ist, nachfragen
			if(!hasBildfahrplan() && !hasTabEditorRück() && noEditorCallback.getAsBoolean())
			{
				return;
			}
			this.tabHinController = controller;
		}
		else
		{
			// Wenn das der letzte Editor ist, nachfragen
			if(!hasBildfahrplan() && !hasTabEditorHin() && noEditorCallback.getAsBoolean())
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
	
	public void addNoEditorCallback(BooleanSupplier noEditorCallback)
	{
		this.noEditorCallback = noEditorCallback;
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
		
		this.streckenabschnitt = streckenabschnitt;
		
		// km für Betriebsstelle: Mittelwert aus getMinKm und getMaxKm: (max+min)/2
		Betriebsstelle b = streckenabschnitt.getStrecken().first().getAnfang();
		streckenKm.put(b, new Double(0.0));
		letzterAlterKm = (b.getMaxKm() + b.getMinKm()) / 2;
		
		// Vorbereitung für unterschiedliche Strecken-km
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
	}
	public void ladeZüge(Collection<? extends Fahrt> fahrten)
	{
		NullTester.test(fahrten);
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
