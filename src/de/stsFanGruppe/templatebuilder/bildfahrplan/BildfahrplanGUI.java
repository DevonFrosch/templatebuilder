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
import de.stsFanGruppe.tools.TimeFormater;

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
	protected boolean repaint = true;
	
	protected Set<Paintable> paintables = new HashSet<>();
	
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
		
		// km f�r Betriebsstelle: Mittelwert aus getMinKm und getMaxKm:
		// (max+min)/2
		Betriebsstelle b = streckenabschnitt.getStrecken().first().getAnfang();
		streckenKm.put(b, new Double(0.0));
		letzterAlterKm = (b.getMaxKm() + b.getMinKm()) / 2;
		
		// Vorbereitung f�r unterschiedliche Strecken-km
		for(Strecke s : streckenabschnitt.getStrecken())
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
		// wir nehmen mal an, dass wir Graphics2D haben, sonst wird's
		// schwierig...
		Graphics2D g = (Graphics2D) graphics;
		
		if(firstPaint)
		{
			firstPaint = false;
			
			// Anti-Aliasing an
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			System.setProperty("swing.aatext", "true");
			System.setProperty("awt.useSystemAAFontSettings", "lcd");
		}
		controller.guiRepaint();
		
		if(!paint || this.streckenabschnitt == null || this.fahrten == null || streckenKm == null)
		{
			// nichts zu tun
			return;
		}
		
		if(paintables.isEmpty() || repaint)
		{
			recalculate(g);
			repaint = false;
		}
		
		for(Paintable p : paintables)
		{
			p.paint(g);
		}
	}
	
	protected void recalculate(Graphics2D g)
	{
		boolean ersteLinie = true;
		// Verschiebung der senkrechten Linie zum 0, falls bei der 1. Abfrage
		// bs.getMaxKm() > 0 ist
		double linienVerschiebung = 0;
		
		double minZeit = config.getMinZeit();
		double maxZeit = config.getMaxZeit();
		
		Color c = config.getBetriebsstellenFarbe();
		
		for(Betriebsstelle bs : streckenabschnitt.getBetriebsstellen())
		{
			double km = bs.getMaxKm();
			
			if(ersteLinie)
			{
				linienVerschiebung -= km;
			}
			km += linienVerschiebung;
			
			paintables.add(new PaintableLine(getWegPos(km), getZeitPos(minZeit), getWegPos(km), getZeitPos(maxZeit), c));
			ersteLinie = false;
		}
		
		// Zeichnet die waagerechte Linie bei jeder angegebenen Zeit
		int zeitIntervall = config.getZeitIntervall();
		
		int zeitLinienStart = 5;
		int zeitLinienEnde = getWidth();
		int zeit = (int) minZeit;
		
		c = config.getZeitenFarbe();
		
		if(zeit % 10 != 0)
		{
			zeit += 10 - (zeit % 10);
		}
		
		Stroke duenneLinie = new BasicStroke(1);
		Stroke dickeLinie = new BasicStroke(3);
		
		while(zeit <= maxZeit)
		{
			Stroke s = (zeit % 60 == 0) ?  dickeLinie : duenneLinie;
			
			paintables.add(new PaintableLine(zeitLinienStart, getZeitPos(zeit), zeitLinienEnde, getZeitPos(zeit), c, s));
			zeit += zeitIntervall;
		}
		
		c = config.getFahrtenFarbe();
		/*
		 * Schachtelung der Z�ge nach Minuten
		 */
		int verschachtelungVerschiebung = 0;
		int schachtelung = config.getSchachtelung();
		
		for(int i = (int) minZeit; i <= maxZeit;)
		{
			// Fahrten im Bildfahrplan malen
			
			for(Fahrt fahrt : fahrten)
			{
				double ab = -1;
				double kmAb = -1;
				
				for(Fahrplanhalt fh : fahrt.getFahrplanhalte())
				{
					if(ab >= 0 && ab >= i && kmAb >= 0)
					{
						double an = fh.getAnkunft();
						
						if(ab < config.getMinZeit() || an > config.getMaxZeit()) { return; }
						
						String name = fahrt.getName();
						double kmAn = streckenKm.get(fh.getGleisabschnitt().getParent().getParent());
						
						if(!config.getZeigeZugnamenKommentare() && name.indexOf('%') >= 0)
						{
							// entferne alles ab dem ersten %, falls vorhanden
							name = name.substring(0, name.indexOf('%'));
						}
						
						int x1 = getWegPos(kmAb);
						int y1 = getZeitPos(ab - verschachtelungVerschiebung);
						int x2 = getWegPos(kmAn);
						int y2 = getZeitPos(an - verschachtelungVerschiebung);
						
						paintables.add(new PaintableLine(x1, y1, x2, y2, c));
						
						// Zeiten zeichnen, wenn in der Config, dies aktiv ist.
						if(config.getZeigeZeiten())
						{
							// Minuten aus den Zeiten auslesen
							String abMinute = TimeFormater.doubleToMinute(ab);
							String anMinute = TimeFormater.doubleToMinute(an);
							int verschiebungX = 13;
							
							// Zeiten zeichnen
							if(x1 < x2)
							{
								// Wenn die Linie von Links nach Rechts gezeichnet wird.
								paintables.add(new PaintableText(x1 + 2, y1, c, abMinute));
								paintables.add(new PaintableText(x2 - verschiebungX, y2 + g.getFontMetrics().getHeight() - 2, c, anMinute));
							}
							else
							{
								// Wenn die Linie von Rechts nach Links gezeichnet wird.
								paintables.add(new PaintableText(x1 - verschiebungX, y1, c, abMinute));
								paintables.add(new PaintableText(x2 + 2, y2 + g.getFontMetrics().getHeight() - 2, c, anMinute));
							}
						}
						
						FontMetrics f = g.getFontMetrics();
						int stringWidth = f.stringWidth(name);
						
						if(config.getZeigeZugnamen() == 0) { return; }
						
						if(config.getZeigeZugnamen() == 2)
						{
							// Zugnamen nur wenn Platz ist
							// Textgr��e holen
							double lineLenght = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
							
							// Text nur, wenn dieser weniger als doppelt so
							// breit ist
							if(stringWidth * 0.9 > lineLenght) { return; }
						}
						
						paintables.add(new PaintableRotatedText(x1, y1, x2, y2, c, name, -2));
					}
					
					// f�r n�chsten Eintrag
					ab = fh.getAbfahrt();
					kmAb = streckenKm.get(fh.getGleisabschnitt().getParent().getParent()).doubleValue();
				}
			}
			if(schachtelung == 0)
			{
				// Keine Schachtelung, abbrechen
				break;
			}
			i += schachtelung;
			verschachtelungVerschiebung += schachtelung;
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
