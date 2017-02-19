package de.stsFanGruppe.templatebuilder.bildfahrplan;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Stroke;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.FahrtDarstellungConfig;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.FahrtDarstellungHandler;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Strecke;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrplanhalt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.templatebuilder.zug.FahrtDarstellung;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.TimeFormater;

public class BildfahrplanGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanGUIController.class);

	protected BildfahrplanConfig config;
	private Object changeHandleId;
	protected BildfahrplanGUI gui = null;
	protected BildfahrplanSpaltenheaderGUI spaltenGui = null;
	protected BildfahrplanZeilenheaderGUI zeilenGui = null;
	protected GUI parent;
	
	protected BildfahrplanPaintHelper ph;
	protected int stringHeight = 0;
	
	protected Streckenabschnitt streckenabschnitt;
	protected Map<Betriebsstelle, Double> streckenKm;
	protected Set<Fahrt> fahrten;
	
	public BildfahrplanGUIController(BildfahrplanConfig config, GUI parent)
	{
		NullTester.test(config);
		NullTester.test(parent);
		this.config = config;
		this.parent = parent;
		this.changeHandleId = config.registerChangeHandler(() -> configChanged());
	}
	
	public void configChanged()
	{
		gui.recalculatePanelSize();
		repaint();
	}
	public void close()
	{
		config.unregisterChangeHandler(changeHandleId);
	}
	
	// Offizielle Funktionen
	public void setBildfahrplanGUI(BildfahrplanGUI gui)
	{
		NullTester.test(gui);
		log.trace("setBildfahrplanGUI()");
		this.gui = gui;
		this.ph = new BildfahrplanPaintHelper(config, gui);
	}
	public void setBildfahrplanSpaltenHeaderGUI(BildfahrplanSpaltenheaderGUI spaltenGui)
	{
		NullTester.test(spaltenGui);
		log.trace("setBildfahrplanSpaltenHeaderGUI()");
		this.spaltenGui = spaltenGui;
	}
	public void setBildfahrplanZeilenheaderGUI(BildfahrplanZeilenheaderGUI zeilenGui)
	{
		NullTester.test(zeilenGui);
		log.trace("setBildfahrplanZeilenheaderGUI()");
		this.zeilenGui = zeilenGui;
	}
	public void ladeStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		NullTester.test(gui);
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
		for(Strecke s: streckenabschnitt.getStrecken())
		{
			b = s.getEnde();
			double alterKm = (b.getMaxKm() + b.getMinKm()) / 2;
			double neuerKm = alterKm - letzterAlterKm + letzterNeuerKm;
			streckenKm.put(b, new Double(neuerKm));
			letzterAlterKm = alterKm;
			letzterNeuerKm = neuerKm;
			ph.setDiffKm(neuerKm);
		}
		
		gui.recalculatePanelSize();
	}
	public void ladeZ�ge(Collection<? extends Fahrt> fahrten)
	{
		NullTester.test(gui);
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
		
		gui.recalculatePanelSize();
	}

	public void repaint()
	{
		if(gui == null)
		{
			return;
		}
		gui.repaint();
		spaltenGui.repaint();
		zeilenGui.repaint();
	}
	
	// TODO tempor�re Funktionen, Abl�sung durch Datenobjekt (BT 059)
	public Streckenabschnitt getStreckenabschnitt()
	{
		return streckenabschnitt;
	}
	public Set<Fahrt> getFahrten()
	{
		return fahrten;
	}
	
	// Nachricht von der GUI, dass sie gerade am repainten ist
	void guiRepaint()
	{
		if(config.needsAutoSize())
		{
			optimizeHeight();
		}
	}
	int getPanelHeight()
	{
		return config.getPanelHeight();
	}
	int getSpaltenHeaderHoehe()
	{
		return config.getSpaltenHeaderHoehe(stringHeight);
	}
	int getZeilenHeaderBreite()
	{
		return config.getZeilenHeaderBreite();
	}
	
	// Interne Funktionen
	protected void optimizeHeight()
	{
		if(gui == null || fahrten == null || fahrten.isEmpty())
		{
			return;
		}
		log.trace("optimizeHeight()");
		
		double minZeit, maxZeit;
		synchronized(fahrten)
		{
			minZeit = fahrten.stream().min((a, b) -> Double.compare(a.getMinZeit(), b.getMinZeit())).get().getMinZeit();
			maxZeit = fahrten.stream().max((a, b) -> Double.compare(a.getMaxZeit(), b.getMaxZeit())).get().getMaxZeit();
		}
		
		assert minZeit <= maxZeit;
		config.setZeiten(minZeit, maxZeit);
		gui.recalculatePanelSize();
	}
	protected void reset()
	{
		synchronized(this)
		{
			streckenabschnitt = null;
			streckenKm = new HashMap<>();
			fahrten = null;
			ph.setDiffKm(-1);
		}
	}
	
	// alle Grafikobjekte neu berechnen
	protected void recalculate(FontMetrics fontMetrics)
	{
		/* Berechnet alles neu
		 * Wichtig: Die Berechnung ist aufgeteilt:
		 * - hier wird (au�erhalb der Lambdas) berechnet, was nicht von der GUI abh�ngig ist
		 * - in den Lambdas und im BildfahrplanPaintHelper ph wird berechnet, was von der GUI abh�ngig ist
		 * Alles, was aus der Config kommt etc. wird also vorberechnet und in Variablen gespeichert, die dann im Lambda verwendet werden k�nnen.
		 * Die im Lambda verwendeten Variablen m�ssen "quasi final" sein, also f�r mehrere Aufrufe des selben Lambdas nicht ver�ndert werden.
		 * Schleifenz�hler m�ssen innerhalb der Schleife in eine lokale Variable kopiert werden, letztere kann dann verwendet werden. 
		 */
		
		if(config == null || streckenabschnitt == null || streckenKm == null || fahrten == null
				|| gui == null || spaltenGui == null || zeilenGui == null)
		{
			return;
		}
		
		log.trace("recalc begonnen");
		
		// Hintergrundfarbe einstellen
		Color bg = config.getHintergrundFarbe();
		gui.getParent().getParent().setBackground(bg);
		gui.getParent().setBackground(bg);
		spaltenGui.getParent().setBackground(bg);
		zeilenGui.getParent().setBackground(bg);
		
		FirstLastLinkedList<Paintable> guiPaints = new FirstLastLinkedList<>();
		FirstLastLinkedList<Paintable> spaltenGuiPaints = new FirstLastLinkedList<>();
		FirstLastLinkedList<Paintable> zeilenGuiPaints = new FirstLastLinkedList<>();
		
		double minZeit = config.getMinZeit();
		double maxZeit = config.getMaxZeit();
		double diffZeit = maxZeit - minZeit;
		
		// lokale Kopie zur Verwendung in Lambdas
		int stringHeight = fontMetrics.getHeight();
		this.stringHeight = stringHeight;
		
		// Config f�r PaintHelper neu laden
		ph.readConfig();
		
		// ### Betriebsstellen-Linien ###
		{
			Color c = config.getBetriebsstellenFarbe();
			FirstLastList<Betriebsstelle> betriebsstellen = streckenabschnitt.getBetriebsstellen();
			
			int bsCounter = 0;
			for(Betriebsstelle bs: streckenabschnitt.getBetriebsstellen())
			{
				double km = streckenKm.get(bs);
				int lineHeight = config.getLineHeight();
				final int offsetX = config.getOffsetX();
				final int textY = config.getTextMarginTop() + ((stringHeight + config.getOffsetY()) * (bsCounter % config.getZeilenAnzahl())) + config.getTextMarginBottom();
				String name = bs.getName();
				int stringWidth = fontMetrics.stringWidth(name);
				
				// Linie in gui
				guiPaints.add(g -> ph.paintLine(g, ph.getWegPos(km), ph.getZeitPos(minZeit), ph.getWegPos(km), ph.getZeitPos(maxZeit), c));
				
				// Linie in spaltenGui
				spaltenGuiPaints.add(g -> ph.paintLine(g, ph.getWegPos(km), spaltenGui.getHeight(), ph.getWegPos(km), spaltenGui.getHeight() - lineHeight, c));
				
				// Text in spaltenGui
				spaltenGuiPaints.add(g -> {
					int x = ph.getWegPos(km);
					if(x < stringWidth)
					{
						// linker Rand
						ph.paintText(g, x - offsetX, textY, c, name);
					}
					else if((x + stringWidth) > spaltenGui.getWidth())
					{
						// rechter Rand
						ph.paintText(g, x - stringWidth + offsetX, textY, c, name);
					}
					else
					{
						// sonstwo
						ph.paintText(g, x - (stringWidth / 2), textY, c, name);
					}
				});
				
				bsCounter++;
			}
		}
		
		// ### Zeit-Linien ###
		{
			Color c = config.getZeitenFarbe();
			
			// Zeichnet die waagerechte Linie bei jeder angegebenen Zeit
			int zeitIntervall = config.getZeitIntervall();
			// Min-Zeit, auf 10 abgerundet
			int zeit = (int) Math.ceil(minZeit / 10) * 10;
			int zeitLinienStart = 5;
			int textPosX = 5;
			
			Stroke duenneLinie = new BasicStroke(1);
			Stroke dickeLinie = new BasicStroke(3);
			
			while(zeit <= maxZeit)
			{
				int thisZeit = zeit;
				Stroke s = (thisZeit % 60 == 0) ?  dickeLinie : duenneLinie;
				String text = TimeFormater.doubleToString(zeit);
				
				// Linie in gui
				guiPaints.add(g -> ph.paintLine(g, zeitLinienStart, ph.getZeitPos(thisZeit), gui.getWidth(), ph.getZeitPos(thisZeit), c, s));
				
				// Text in zeilenGui
				zeilenGuiPaints.add(g -> ph.paintText(g, textPosX , ph.getZeitPos(thisZeit) + (stringHeight/3), c, text));
				
				zeit += zeitIntervall;
			}
		}
		
		// ### Fahrten-Linien ###
		{
			FahrtDarstellungConfig darstellungConfig = new FahrtDarstellungConfig();
			FahrtDarstellungHandler darstellungHandler = new FahrtDarstellungHandler(darstellungConfig);
			
			int schachtelung = config.getSchachtelung();
			int zeigeZugnamen = config.getZeigeZugnamen();
			boolean zeigeZeiten = config.getZeigeZeiten();
			int zeigeRichtung = config.getZeigeRichtung();
			
			synchronized(fahrten)
			{
				for(Fahrt fahrt: fahrten)
				{
					double letzteZeit = -1;
					double letzterKm = -1;
					String zugName = fahrt.getName();
					
					// Darstellung
					FahrtDarstellung fahrtDarstellung = darstellungHandler.getFahrtDarstellung(zugName);
					Color fahrtFarbe = fahrtDarstellung.getFarbe();
					
					for(Fahrplanhalt fh: fahrt.getFahrplanhalte())
					{
						double an = fh.getAnkunft();
						if(an > maxZeit)
						{
							continue;
						}
						
						double kmAn = streckenKm.get(fh.getGleisabschnitt().getParent().getParent());
						
						if(!config.getZeigeZugnamenKommentare() && zugName.indexOf('%') >= 0)
						{
							// entferne alles ab dem ersten %, falls vorhanden
							zugName = zugName.substring(0, zugName.indexOf('%'));
						}
						
						// �bersetzten von dynamischen Variablen
						double kmAb = letzterKm;
						double ab = letzteZeit;
						String name = zugName;
						double showTextWidth = fontMetrics.stringWidth(name) * 0.9;
						
						// f�r ZeigeZeiten
						int diffX = 5;
						int diffY = -3;
						
						// Minuten aus den Zeiten auslesen
						String abMinute = TimeFormater.doubleToMinute(ab);
						String anMinute = TimeFormater.doubleToMinute(an);
						int abMinuteBreite = fontMetrics.stringWidth(abMinute) - 2;
						int anMinuteBreite = fontMetrics.stringWidth(anMinute) - 2;
						int abVerschiebungY = diffY;
						int anVerschiebungY = stringHeight + diffY;
						
						// erst ab dem 2. Fahrplanhalt zeichnen (die Linie, die zum Fahrplanhalt hinf�hrt)
						if(letzteZeit >= 0 && letzterKm >= 0 && letzteZeit >= minZeit && kmAb >= 0)
						{
							// nicht zeichnen, wenn Richtung ausgeblendet
							if((kmAb < kmAn && (zeigeRichtung & 1) != 0)
									|| (kmAb > kmAn && (zeigeRichtung & 2) != 0))
							{
								guiPaints.add(g ->
								{
									int x1 = ph.getWegPos(kmAb);
									int x2 = ph.getWegPos(kmAn);
									
									int zeitenLTRx1 = x1 + diffX;
									int zeitenLTRx2 = x2 - anMinuteBreite - diffX;
									int zeitenRTLx1 = x1 - abMinuteBreite - diffX;
									int zeitenRTLx2 = x2 + diffX;
									
									// Schachtelung der Z�ge nach Minuten
									//for(int schachtelungVerschiebung = (int) minZeit; schachtelungVerschiebung <= maxZeit;)
									for(int sch=0; sch < diffZeit; sch += schachtelung)
									{
										int y1 = ph.getZeitPos(ab - sch);
										int y2 = ph.getZeitPos(an - sch);
										
										ph.paintLine(g, x1, y1, x2, y2, fahrtDarstellung);
										
										// Zeiten zeichnen, wenn in der Config, dies aktiv ist.
										if(zeigeZeiten)
										{
											// Zeiten zeichnen
											if(x1 < x2)
											{
												// Wenn die Linie von Links nach Rechts gezeichnet wird.
												ph.paintText(g, zeitenLTRx1, y1 + diffY, fahrtFarbe, abMinute);
												ph.paintText(g, zeitenLTRx2, y2 + stringHeight + diffY, fahrtFarbe, anMinute);
											}
											else
											{
												// Wenn die Linie von Rechts nach Links gezeichnet wird.
												ph.paintText(g, zeitenRTLx1, y1 + diffY, fahrtFarbe, abMinute);
												ph.paintText(g, zeitenRTLx2, y2 + stringHeight + diffY, fahrtFarbe, anMinute);
											}
										}
										
										boolean zeichneZugnamen = (zeigeZugnamen != 0);
										
										if(zeigeZugnamen == 2)
										{
											// Zugnamen nur wenn Platz ist
											// Textgr��e holen
											double lineLenght = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
											
											// Text nur, wenn dieser weniger als doppelt so
											// breit ist
											if(showTextWidth > lineLenght)
											{
												zeichneZugnamen = false;
											}
										}
										
										if(zeichneZugnamen)
										{
											ph.paintRotatedText(g, x1, y1, x2, y2, fahrtFarbe, name, -2);
										}
										
										if(schachtelung == 0)
										{
											// Keine Schachtelung, abbrechen
											break;
										}
									}
								});
							}
						}
						
						// f�r n�chsten Eintrag
						letzteZeit = fh.getAbfahrt();
						letzterKm = streckenKm.get(fh.getGleisabschnitt().getParent().getParent()).doubleValue();
						
					} // for(Fahrplanhalt fh: fahrt.getFahrplanhalte())
				} // for(Fahrt fahrt: fahrten)
			} // synchronized(fahrten)
		}
		
		// Paint-Objekte �berschreiben
		gui.setPaintables(guiPaints);
		spaltenGui.setPaintables(spaltenGuiPaints);
		zeilenGui.setPaintables(zeilenGuiPaints);

		log.trace("recalc fertig");
	}
}
