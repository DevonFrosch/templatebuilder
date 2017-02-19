package de.stsFanGruppe.templatebuilder.editor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.templatebuilder.gui.TabController;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Strecke;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.NullTester;

public abstract class EditorGUIController implements TabController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EditorGUIController.class);
	
	protected BildfahrplanConfig config;
	private Object changeHandleId;
	protected GUI parent;
	
	protected Streckenabschnitt streckenabschnitt;
	protected Map<Betriebsstelle, Double> streckenKm;
	protected double diffKm = -1;
	protected Set<Fahrt> fahrten;
	
	protected EditorGUIController(BildfahrplanConfig config, TemplateBuilderGUI parent)
	{
		NullTester.test(config);
		NullTester.test(parent);
		
		this.config = config;
		this.parent = parent;
		this.changeHandleId = config.registerChangeHandler(() -> configChanged());
	}
	
	public abstract void configChanged();
	public void close()
	{
		config.unregisterChangeHandler(changeHandleId);
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
	public Set<Fahrt> getFahrten()
	{
		return fahrten;
	}
	
	protected void reset()
	{
		synchronized(this)
		{
			streckenabschnitt = null;
			streckenKm = new HashMap<>();
			fahrten = null;
		}
	}
}
