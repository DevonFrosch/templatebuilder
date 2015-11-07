package de.stsFanGruppe.templatebuilder.gui;

import java.util.*;
import javax.swing.JPanel;
import eu.devonfrosch.sts.util.*;

public class BildfahrplanZeichner
{
	private JPanel p;
	private BildfahrplanConfig config;
	
	int startZeit = 0; // 0:00:00
	int endeZeit = 86400; // 24:00:00
	int diffZeit = endeZeit - startZeit;
	
	int startKm = 0;
	int endeKm = 50;
	int diffKm = endeKm - startKm;
	
	public BildfahrplanZeichner(JPanel p)
	{
		config = new BildfahrplanConfig();
		this.p = p;
	}
	
	public void zeichneZug(Zug zug)
	{
		Fahrplan fpl = zug.getFahrplan();
		Iterator<FahrplanEintrag> it = fpl.iterator();
		
		int ab = -1;
		int kmAb = -1;
		while(it.hasNext())
		{
			FahrplanEintrag fe = it.next();
			
			if(ab < 0 || kmAb < 0)
			{
				int an = fe.getAnkunft().minuten();
				
				// draw(getZeitPos(p, an), getZeitPos(p, ab));
				
			}
			
			// für nächsten Eintrag
			ab = fe.getAbfahrt().minuten();
			// abKM = getKM(fe.getGleis());
		}
		int start = 0;
		int ende = 0;
		
		
	}
	
	
	int getZeitPos(int zeit)
	{
		int min = config.margin_top;
		int diff = config.zeichnenHoehe(p) - min;
		
		return  min + diff * zeit / diffZeit;
	}
	int getWegPos(int km)
	{
		int min = config.margin_left;
		int diff = config.zeichnenBreite(p) - min;
		
		return  min + diff * km / diffKm;
	}
	
}
