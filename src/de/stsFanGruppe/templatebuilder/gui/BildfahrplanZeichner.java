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
	Streckenabschnitt streckenabschnitt;
	private Set<Fahrt> fahrten;
	
	double diffZeit = 0;
	
	double startKm = 0;
	double endeKm = 3;
	double diffKm = endeKm - startKm;
	
	boolean changed = false;
	
	public BildfahrplanZeichner(BildfahrplanConfig config, Streckenabschnitt streckenabschnitt)
	{
		NullTester.test(config);
		NullTester.test(streckenabschnitt);
		this.config = config;
		this.streckenabschnitt = streckenabschnitt;
		this.fahrten = new HashSet<Fahrt>();
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
		
		if(this.fahrten.isEmpty())
		{
			changed = false;
			return;
		}
		
		if(changed)
		{
			double minZeit = fahrten.stream().min((a, b) -> Double.compare(a.getMinZeit(), b.getMinZeit())).get().getMinZeit();
			double maxZeit = fahrten.stream().min((a, b) -> Double.compare(a.getMinZeit(), b.getMinZeit())).get().getMinZeit();
			this.diffZeit = maxZeit - minZeit;
			log("diffZeit: "+diffZeit);
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
					double kmAn = getKm(fh.getGleisabschnitt().getKmAnfang(), kmAb+1);
					log("Fahrplanhalt: an = "+an+", kmAn = "+kmAn);
					
					g.setColor(Color.BLACK);
					g.drawLine(getWegPos(kmAb), getZeitPos(ab), getWegPos(kmAn), getZeitPos(an));
					log("drawLine: "+getWegPos(kmAb)+", "+getZeitPos(an)+", "+getWegPos(kmAn)+", "+getZeitPos(ab));
				}
				else
				{
					log("Erster Fahrplanhalt!");
				}
				
				// für nächsten Eintrag
				ab = fh.getAbfahrt();
				kmAb = getKm(fh.getGleisabschnitt().getKmAnfang(), kmAb+1);
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
	
	double getKm(OptionalDouble km, double defaultValue)
	{
		assert km != null;
		if(km.isPresent())
		{
			return km.getAsDouble();
		}
		return defaultValue;
	}
	public void log(String text)
	{
		if(changed)
			System.out.println(text);
	}
}
