package de.stsFanGruppe.templatebuilder.editor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
	
	protected FirstLastLinkedList<EditorGUIController> editoren = new FirstLastLinkedList<>();
	
	protected Streckenabschnitt streckenabschnitt;
	protected Map<Betriebsstelle, Double> streckenKm;
	protected double diffKm = -1;
	protected Set<Fahrt> fahrten;
	
	public EditorDaten()
	{
		
	}
	public EditorDaten(EditorGUIController controller)
	{
		this();
		addEditor(controller);
	}
	
	public void addEditor(EditorGUIController controller)
	{
		editoren.add(controller);
	}
	public void removeEditor(EditorGUIController controller)
	{
		if(!editoren.contains(controller))
		{
			log.warn("Versuche {} zu entfernen, aber nicht in Liste.", controller);
		}
		if(editoren.size() < 0)
		{
			log.error("removeEditor bei leerer Editoren-Liste");
			throw new IllegalStateException("Editoren-Liste ist leer");
		}
		if(editoren.size() < 1)
		{
			// TODO Letzter Tab hierzu, nachfrage, ob man speichern möchte
		}
		editoren.remove(controller);
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
		synchronized(fahrten)
		{
			return fahrten.stream().min((a, b) -> Double.compare(a.getMinZeit(), b.getMinZeit())).get().getMinZeit();
		}
	}
	public double getMaxZeit()
	{
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
