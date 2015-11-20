package de.stsFanGruppe.templatebuilder.gui.bildfahrplan;

import java.awt.*;
import java.util.*;
import javax.swing.JPanel;
import de.stsFanGruppe.templatebuilder.strecken.*;
import de.stsFanGruppe.templatebuilder.zug.*;
import de.stsFanGruppe.tools.NullTester;

public class BildfahrplanZeichner extends JPanel
{
	protected BildfahrplanConfig config;
	protected BildfahrplanController controller;
	
	protected Streckenabschnitt streckenabschnitt;
	protected Map<Betriebsstelle, Double> streckenKm;
	protected  Set<Fahrt> fahrten;
	
	protected double diffKm;
	
	protected boolean changed = true;
	protected boolean paint = true;
	protected boolean firstPaint = true;
	
	public BildfahrplanZeichner(BildfahrplanConfig config, BildfahrplanController controller)
	{
		NullTester.test(config);
		this.config = config;
		this.controller = controller;
	}
	public BildfahrplanZeichner(BildfahrplanConfig config, BildfahrplanController controller, Streckenabschnitt streckenabschnitt)
	{
		this(config, controller);
		setStreckenabschnitt(streckenabschnitt);
	}
	
	public void setStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		NullTester.test(streckenabschnitt);
		
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
		for(Strecke s: streckenabschnitt.getStrecken())
		{
			b = s.getEnde();
			double alterKm = (b.getMaxKm() + b.getMinKm()) / 2;
			double neuerKm = alterKm - letzterAlterKm + letzterNeuerKm;
			streckenKm.put(b, new Double(neuerKm));
			letzterAlterKm = alterKm;
			letzterNeuerKm = neuerKm;
			this.diffKm = neuerKm;
		}
		
		if(controller != null)
		{
			controller.setPanelSize();
		}
		
		controller.repaint();
	}
	public void zeichneFahrt(Fahrt fahrt)
	{
		NullTester.test(fahrt);
		
		if(fahrten == null)
		{
			fahrten = new HashSet<>();
		}
		
		this.fahrten.add(fahrt);
		this.changed = true;
	}
	public void zeichneFahrten(Collection<? extends Fahrt> fahrten)
	{
		fahrten.forEach((Fahrt f) -> this.zeichneFahrt(f));
	}
	
	public void optimizeHeight()
	{
		if(fahrten == null)
		{
			return;
		}
		
		double minZeit = fahrten.stream().min((a, b) -> Double.compare(a.getMinZeit(), b.getMinZeit())).get().getMinZeit();
		double maxZeit = fahrten.stream().max((a, b) -> Double.compare(a.getMaxZeit(), b.getMaxZeit())).get().getMaxZeit();
		
		config.setMaxZeit(maxZeit);
		config.setMinZeit(minZeit);
		
		controller.setPanelSize();
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if(firstPaint)
		{
			firstPaint = false;
			g.setColor(Color.BLACK);
		}
		
		if(!paint || this.streckenabschnitt == null || this.fahrten == null || streckenKm == null)
		{
			return;
		}
		
		if(changed)
		{
			optimizeHeight();
		}
		
		for(Fahrt fahrt: fahrten)
		{
			double ab = -1;
			double kmAb = -1;
			
			for(Fahrplanhalt fh: fahrt.getFahrplanhalte())
			{
				if(ab >= 0 && kmAb >= 0)
				{
					double an = fh.getAnkunft();
					double kmAn = streckenKm.get(fh.getGleisabschnitt().getParent().getParent());
					
					drawLine(g, kmAb, ab, kmAn, an);
				}
				
				// f�r n�chsten Eintrag
				ab = fh.getAbfahrt();
				kmAb = streckenKm.get(fh.getGleisabschnitt().getParent().getParent()).doubleValue();
			}
		}
		changed = false;
	}
	
	protected void drawLine(Graphics g, double kmAb, double ab, double kmAn, double an)
	{
		assert config != null;
		if(ab < config.getMinZeit() || an > config.getMaxZeit())
		{
			return;
		}
		
		g.drawLine(getWegPos(kmAb), getZeitPos(ab), getWegPos(kmAn), getZeitPos(an));
	}
	protected int getZeitPos(double zeit)
	{
		assert config != null;
		int min = config.margin_top;
		int hoehe = config.zeichnenHoehe(this);
		double diffZeit = config.getMaxZeit() - config.getMinZeit();
		
		double zeitFaktor = zeit - config.getMinZeit();
		
		return (int) ((zeitFaktor / diffZeit * hoehe) + min);
	}
	protected int getWegPos(double km)
	{
		assert config != null;
		int minBreite = config.margin_left;
		int diffBreite = config.zeichnenBreite(this);
		
		return (int) ((km / diffKm * diffBreite) + minBreite);
	}
	protected void reset()
	{
		streckenabschnitt = null;
		streckenKm = new HashMap<>();
		fahrten = null;
		diffKm = -1;
		changed = true;
		firstPaint = true;
	}
	
	private static void log(String text)
	{
		System.out.println("BildfahrplanZeichner: "+text);
	}
}