package de.stsFanGruppe.templatebuilder.bildfahrplan;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import javax.swing.JComponent;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;
import de.stsFanGruppe.templatebuilder.strecken.*;
import de.stsFanGruppe.templatebuilder.zug.*;
import de.stsFanGruppe.tools.NullTester;

public class BildfahrplanGUI extends JComponent
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanGUI.class);
	
	protected BildfahrplanConfig config;
	protected BildfahrplanGUIController controller;
	protected TemplateBuilderGUI parent;
	
	protected Streckenabschnitt streckenabschnitt;
	protected Map<Betriebsstelle, Double> streckenKm;
	protected Set<Fahrt> fahrten;
	
	protected double diffKm;
	
	protected boolean paint = true;
	protected boolean firstPaint = true;
	
	public BildfahrplanGUI(BildfahrplanGUIController controller, TemplateBuilderGUI parent)
	{
		NullTester.test(parent);
		
		this.config = controller.getConfig();
		
		this.controller = controller;
		controller.setBildfahrplanGUI(this);
		
		this.parent = parent;
	}
	
	public void setStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		NullTester.test(streckenabschnitt);
		assert controller != null;
		
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
		
		controller.setPanelSize();
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
	}
	public void zeichneFahrten(Collection<? extends Fahrt> fahrten)
	{
		fahrten.forEach((Fahrt f) -> this.zeichneFahrt(f));
	}
	
	public void errorMessage(String text)
	{
		if(parent == null)
		{
			log.error(text);
		}
		parent.errorMessage(text);
	}
	
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		// wir nehmen mal an, dass wir Graphics2D haben, sonst wird's schwierig...
		Graphics2D g = (Graphics2D) graphics;
		
		// Anti-Aliasing an
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		System.setProperty("swing.aatext", "true");
		System.setProperty("awt.useSystemAAFontSettings", "lcd");
		
		if(firstPaint)
		{
			// Farbe setzten
			firstPaint = false;
			g.setColor(Color.BLACK);
		}
		controller.guiRepaint();
		
		if(!paint || this.streckenabschnitt == null || this.fahrten == null || streckenKm == null)
		{
			// nichts zu tun
			return;
		}
		
		boolean ersteLinie = true;
		// Verschiebung der senkrechten Linie zum 0, falls bei der 1. Abfrage bs.getMaxKm() > 0 ist
		double linienVerschiebung = 0;
		
		double minZeit = config.getMinZeit();
		double maxZeit = config.getMaxZeit();	
		
		g.setColor(config.getBfpBetriebsstellenFarbe());
		
		for(Betriebsstelle bs: streckenabschnitt.getBetriebsstellen())
		{
			double km = bs.getMaxKm();
			
			if(ersteLinie)
			{
				linienVerschiebung -= km;
			}
			km += linienVerschiebung;
			
			drawLine(g, km, minZeit, km, maxZeit, null);
			
			ersteLinie = false;
		}
		
		// Zeichnet die waagerechte Linie bei jeder angegebenen Zeit
		int zeitIntervall = config.getZeitIntervall();
		
		int x1 = 5;
		int x2 = getWidth();
		int zeit = (int) minZeit;
		
		g.setColor(config.getBfpZeitenFarbe());
		
		if (zeit % 10 != 0) 
		{
			zeit -= (zeit % 10);
		}
		while (zeit <= maxZeit) 
		{	
			if(zeit % 60 == 0)
			{
				g.setStroke(new BasicStroke(3));
			}
			else
			{
				g.setStroke(new BasicStroke(1));
			}
			g.drawLine(x1, getZeitPos(zeit), x2, getZeitPos(zeit));
			zeit = zeit + zeitIntervall;
		}
		//Dicke des Strichs wieder auf 1 setzen
		g.setStroke(new BasicStroke(1));
		
		// Fahrten im Bildfahrplan malen
		g.setColor(config.getBfpFahrtenFarbe());
		
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
					
					String name = fahrt.getName();
					
					if(!config.getZeigeZugnamenKommentare() && name.indexOf('%') >= 0)
					{
						// entferne alles ab dem ersten %, falls vorhanden
						name = name.substring(0, name.indexOf('%'));
					}
					drawLine(g, kmAb, ab, kmAn, an, name);
				}
				
				// f�r n�chsten Eintrag
				ab = fh.getAbfahrt();
				kmAb = streckenKm.get(fh.getGleisabschnitt().getParent().getParent()).doubleValue();
			}
		}
	}
	
	protected void drawLine(Graphics2D g, double kmAb, double ab, double kmAn, double an, String beschriftung)
	{
		assert config != null;
		assert g != null;
		
		if(ab < config.getMinZeit() || an > config.getMaxZeit())
		{
			return;
		}
		
		int x1 = getWegPos(kmAb);
		int y1 = getZeitPos(ab);
		int x2 = getWegPos(kmAn);
		int y2 = getZeitPos(an);
		
		// Linie zeichnen
		g.drawLine(x1, y1, x2, y2);
		
		//Zeiten zeichnen, wenn in der Config, dies aktiv ist.
		if(config.getZeigeZeiten())
		{
			// Minuten aus den Zeiten auslesen
			String abMinute = String.valueOf((int) (ab % 60));
			String anMinute = String.valueOf((int) (an % 60));
			int verschiebungX = 13;
			
			// Dreieckberechnung f�r eine bessere Darstellung der Minutenanzeige
			double ankathete = x2 - x1;
			double gegenkathete = y2 - y1;
			double hoeheAnkunft = gegenkathete / ankathete * verschiebungX;
			
			// Zeiten zeichnen
			if(x1 < x2)
			{ 
				//Wenn die Linie von Links nach Rechts gezeichnet wird. 
				g.drawString(abMinute, x1 + 2 , y1);
				g.drawString(anMinute, x2 - verschiebungX , y2 - (int) hoeheAnkunft); 
			}
			else
			{
				// Wenn die Linie von Rechts nach Links gezeichnet wird.
				g.drawString(abMinute, x1 - verschiebungX , y1);
				g.drawString(anMinute, x2 + 2 , y2 + (int) hoeheAnkunft); 
			}
		}
		
		if(config.getZeigeZugnamen() != 0 && beschriftung != null)
		{
			FontMetrics f = g.getFontMetrics();
			int stringWidth = f.stringWidth(beschriftung);
			
			if(config.getZeigeZugnamen() == 2)
			{
				// Zugnamen nur wenn Platz ist
				// Textgr��e holen
				double lineLenght = Math.sqrt((x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));
	
				// Text nur, wenn dieser weniger als doppelt so breit ist
				if(stringWidth * 0.9 > lineLenght)
				{
					return;
				}
			}
			
			// Koordinatensystem drehen
			AffineTransform neu = g.getTransform();
			AffineTransform alt = (AffineTransform)neu.clone(); 
			neu.translate((x1 + x2) / 2, (y2 + y1) / 2);
			neu.rotate(Math.atan((1.0 * y2 - y1) / (x2 - x1)));
			g.setTransform(neu);
				
			// Text einzeichnen
			g.drawString(beschriftung, -stringWidth / 2, -2);
			
			// Koordinatensystem zur�cksetzen
			g.setTransform(alt);
		}
	}
	protected int getZeitPos(double zeit)
	{
		assert config != null;
		int min = config.getMarginTop();
		int hoehe = config.zeichnenHoehe(this);
		double diffZeit = config.getMaxZeit() - config.getMinZeit();
		
		double zeitFaktor = zeit - config.getMinZeit();
		
		return (int) ((zeitFaktor / diffZeit * hoehe) + min);
	}
	protected int getWegPos(double km)
	{
		assert config != null;
		int minBreite = config.getMarginLeft();
		int diffBreite = config.zeichnenBreite(this);
		
		return (int) ((km / diffKm * diffBreite) + minBreite);
	}
	protected void reset()
	{
		streckenabschnitt = null;
		streckenKm = new HashMap<>();
		fahrten = null;
		diffKm = -1;
		firstPaint = true;
	}
}