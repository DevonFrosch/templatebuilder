package de.stsFanGruppe.templatebuilder.gui;

import java.awt.*;
import java.util.*;
import javax.swing.JPanel;
import de.stsFanGruppe.templatebuilder.strecken.*;
import de.stsFanGruppe.templatebuilder.zug.*;
import de.stsFanGruppe.tools.NullTester;

public class BildfahrplanZeichner extends JPanel
{
	private BildfahrplanConfig config;
	Streckenabschnitt streckenabschnitt = null;
	private Set<Fahrt> fahrten;
	
	double diffZeit = -1;
	double diffKm = -1;
	
	boolean changed = true;
	
	public BildfahrplanZeichner(BildfahrplanConfig config)
	{
		NullTester.test(config);
		this.config = config;
		this.fahrten = new HashSet<Fahrt>();
	}
	
	public void setStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		NullTester.test(streckenabschnitt);
		changed = true;
		this.streckenabschnitt = streckenabschnitt;
		
		this.diffKm = streckenabschnitt.getMaxKm() - streckenabschnitt.getMinKm();

		log("x.Breite: "+getWidth());
		log("y.Höhe: "+getHeight());
		log("x.diffKm: "+diffKm+" ("+streckenabschnitt.getMaxKm()+" - "+streckenabschnitt.getMinKm()+")");
	}
	public void zeichneFahrt(Fahrt fahrt)
	{
		NullTester.test(fahrt);
		this.fahrten.add(fahrt);
		this.changed = true;
	}
	public void zeichneFahrten(Collection<? extends Fahrt> fahrten)
	{
		fahrten.forEach((Fahrt f) -> this.zeichneFahrt(f));
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if(this.streckenabschnitt == null || this.fahrten.isEmpty())
		{
			return;
		}
		
		if(changed)
		{
			double minZeit = fahrten.stream().min((a, b) -> Double.compare(a.getMinZeit(), b.getMinZeit())).get().getMinZeit();
			double maxZeit = fahrten.stream().max((a, b) -> Double.compare(a.getMaxZeit(), b.getMaxZeit())).get().getMaxZeit();
			this.diffZeit = maxZeit - minZeit;
			log("y.diffZeit: "+diffZeit);
		}
		
		Iterator<Fahrt> f = this.fahrten.iterator();
		while(f.hasNext())
		{
			Fahrt fahrt = f.next();
			log(fahrt.getFahrplanhalte().size()+" Fahrplanhalte...");
			Iterator<Fahrplanhalt> it = fahrt.getFahrplanhalte().iterator();
			
			double ab = -1;
			double kmAb = -1;
			while(it.hasNext())
			{
				Fahrplanhalt fh = it.next();
				log("-- Fahrplanhalt "+fh.getGleisabschnitt().getName());
				
				if(ab >= 0 && kmAb >= 0)
				{
					double an = fh.getAnkunft();
					double kmAn = fh.getGleisabschnitt().getKm();
					log("Fahrplanhalt: y.an = "+an+", x.kmAn = "+kmAn);
					
					g.setColor(Color.BLACK);
					g.drawLine(getWegPos(kmAb), getZeitPos(ab), getWegPos(kmAn), getZeitPos(an));
					log("drawLine: (x "+getWegPos(kmAb)+", y "+getZeitPos(an)+") -> (x "+getWegPos(kmAn)+", y "+getZeitPos(ab)+")");
				}
				else
				{
					log("Erster Fahrplanhalt!");
				}
				
				// für nächsten Eintrag
				ab = fh.getAbfahrt();
				kmAb = fh.getGleisabschnitt().getKm();
				log("Daten: ab = "+ab+", kmAb = "+kmAb);
			}
		}
		changed = false;
	}
	
	
	int getZeitPos(double zeit)
	{
		assert config != null;
		int min = config.margin_top;
		int diff = config.zeichnenHoehe(this);
		
		return (int) (min + (diff * zeit / diffZeit));
	}
	int getWegPos(double km)
	{
		assert config != null;
		int min = config.margin_left;
		int diff = config.zeichnenBreite(this);
		
		return (int) (min + (diff * km / diffKm));
	}
	
	public void log(String text)
	{
		if(changed)
			System.out.println(text);
	}
}
