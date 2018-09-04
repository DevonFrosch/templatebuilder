package de.stsFanGruppe.templatebuilder.editor.bildfahrplan;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.config.GeneralConfig;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.FahrtDarstellungHandler;
import de.stsFanGruppe.templatebuilder.editor.EditorDaten;
import de.stsFanGruppe.templatebuilder.editor.EditorGUIController;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrplanhalt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.templatebuilder.zug.FahrtDarstellung;
import de.stsFanGruppe.tools.CalculatableLine;
import de.stsFanGruppe.tools.FahrtPaintable;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;
import de.stsFanGruppe.tools.Paintable;
import de.stsFanGruppe.tools.TimeFormater;

public class BildfahrplanGUIController extends EditorGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanGUIController.class);

	protected GUI parentGui = null; 
	protected BildfahrplanGUI gui = null;
	protected BildfahrplanSpaltenheaderGUI spaltenGui = null;
	protected BildfahrplanZeilenheaderGUI zeilenGui = null;
	
	protected BildfahrplanConfig bildfahrplanConfig;
	protected BildfahrplanPaintHelper ph;
	protected FahrtDarstellungHandler darstellungHandler;
	protected static int stringHeight = 0;
	
	protected Object zugLinienLock = new Object();
	protected FirstLastLinkedList<CalculatableLine> zugLinien = new FirstLastLinkedList<>();
	protected long lastClickTime = 0;
	
	public BildfahrplanGUIController(GUI parentGui, EditorDaten daten, GeneralConfig config)
	{
		super(daten, config);
		initVariables(parentGui, config);
	}
	
	public BildfahrplanGUIController(GUI parentGui, GeneralConfig config)
	{
		super(config);
		initVariables(parentGui, config);
	}
	
	private void initVariables(GUI parentGui, GeneralConfig config)
	{
		super.getEditorDaten().setBildfahrplan(this);
		this.parentGui = parentGui;
		this.bildfahrplanConfig = config.getBildfahrplanConfig();
		this.gui = new BildfahrplanGUI(this);
		this.ph = new BildfahrplanPaintHelper(this.bildfahrplanConfig, this.gui);
		this.spaltenGui = new BildfahrplanSpaltenheaderGUI(this.gui, this);
		this.zeilenGui = new BildfahrplanZeilenheaderGUI(this.gui, this);
		this.darstellungHandler = new FahrtDarstellungHandler(config.getFahrtDarstellungConfig());
	}
	
	public void configChanged()
	{
		gui.recalculatePanelSize();
		repaint();
	}
	
	// Offizielle Funktionen
	public BildfahrplanGUI getBildfahrplanGUI()
	{
		return gui;
	}
	
	public BildfahrplanSpaltenheaderGUI getBildfahrplanSpaltenHeaderGUI()
	{
		return spaltenGui;
	}
	
	public BildfahrplanZeilenheaderGUI getBildfahrplanZeilenHeaderGUI()
	{
		return zeilenGui;
	}
	
	public FirstLastLinkedList<CalculatableLine> getZugLinien()
	{
		synchronized(zugLinienLock)
		{
			return zugLinien.clone();
		}
	}
	
	public void setZugLinien(FirstLastLinkedList<CalculatableLine> zugLinien)
	{
		synchronized(zugLinienLock)
		{
			this.zugLinien = zugLinien;
		}
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
	
	// Funktionen von EditorGUIController
	public void ladeStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		editorDaten.ladeStreckenabschnitt(streckenabschnitt);
		ph.setDiffKm(editorDaten.getDiffKm());
		gui.recalculatePanelSize();
	}
	
	public void ladeZüge(Collection<? extends Fahrt> fahrten)
	{
		editorDaten.ladeZüge(fahrten);
		gui.recalculatePanelSize();
	}
	
	protected void reset()
	{
		editorDaten.reset();
		ph.setDiffKm(-1);
	}
	
	// Nachricht von der GUI, dass sie gerade am repainten ist
	void guiRepaint()
	{
		if(bildfahrplanConfig.needsAutoSize())
		{
			optimizeHeight();
		}
	}
	
	int getPanelHeight()
	{
		return bildfahrplanConfig.getPanelHeight();
	}
	
	int getSpaltenHeaderHoehe()
	{
		return bildfahrplanConfig.getSpaltenHeaderHoehe(stringHeight);
	}
	
	int getZeilenHeaderBreite()
	{
		return bildfahrplanConfig.getZeilenHeaderBreite();
	}
	
	// action handler
	public void mouseClicked(MouseEvent e)
	{
		String zuege = bildfahrplanConfig.getFahrtDarstellungConfig().getHervorgehobeneZuegeText(editorDaten);
		
		if(lastClickTime + 5.0e8 > System.nanoTime() && zuege.length() > 0)
		{
			parentGui.infoMessage("Markierte Züge:\n"+zuege, "Markierte Züge");
		}
		else
		{
			Set<String> zugNamen = new HashSet<>();
			for(CalculatableLine linie: this.getZugLinien())
			{
				if(linie.isPunktAufLinie(e.getX(), e.getY(), 10))
				{
					zugNamen.add(linie.getName());
				}
			}
			
			bildfahrplanConfig.getFahrtDarstellungConfig().setHervorgehobeneZuege(new FirstLastLinkedList<>(zugNamen));
		}
		lastClickTime = System.nanoTime();
	}
	
	// Interne Funktionen
	protected void optimizeHeight()
	{
		if(gui == null || !editorDaten.hasFahrten())
		{
			return;
		}
		log.trace("optimizeHeight()");
		
		double minZeit = editorDaten.getMinZeit();
		double maxZeit = editorDaten.getMaxZeit();
		
		assert minZeit <= maxZeit;
		bildfahrplanConfig.setZeiten(minZeit, maxZeit);
		gui.recalculatePanelSize();
	}
	
	// alle Grafikobjekte neu berechnen
	protected void recalculate(FontMetrics fontMetrics)
	{
		/*
		 * Berechnet alles neu
		 * Wichtig: Die Berechnung ist aufgeteilt:
		 * - hier wird (außerhalb der Lambdas) berechnet, was nicht von der GUI abhängig ist
		 * - in den Lambdas und im BildfahrplanPaintHelper ph wird berechnet, was von der GUI abhängig ist
		 * Alles, was aus der Config kommt etc. wird also vorberechnet und in Variablen gespeichert, die dann im Lambda verwendet werden können.
		 * Die im Lambda verwendeten Variablen müssen "quasi final" sein, also für mehrere Aufrufe des selben Lambdas nicht verändert werden.
		 * Schleifenzähler müssen innerhalb der Schleife in eine lokale Variable kopiert werden, letztere kann dann verwendet werden.
		 */
		
		if(!editorDaten.hasStreckenabschnitt() || !editorDaten.hasFahrten() || bildfahrplanConfig == null || gui == null || spaltenGui == null || zeilenGui == null)
		{
			return;
		}
		
		log.trace("recalc begonnen");
		
		// Hintergrundfarbe einstellen
		Color bg = bildfahrplanConfig.getHintergrundFarbe();
		gui.getParent().getParent().setBackground(bg);
		gui.getParent().setBackground(bg);
		spaltenGui.getParent().setBackground(bg);
		zeilenGui.getParent().setBackground(bg);
		
		FirstLastLinkedList<Paintable> guiPaints = new FirstLastLinkedList<>();
		FirstLastLinkedList<FahrtPaintable> fahrtPaints = new FirstLastLinkedList<>();
		FirstLastLinkedList<Paintable> spaltenGuiPaints = new FirstLastLinkedList<>();
		FirstLastLinkedList<Paintable> zeilenGuiPaints = new FirstLastLinkedList<>();
		
		double minZeit = bildfahrplanConfig.getMinZeit();
		double maxZeit = bildfahrplanConfig.getMaxZeit();
		double diffZeit = maxZeit - minZeit;
		
		// lokale Kopie zur Verwendung in Lambdas
		int stringHeight = fontMetrics.getHeight();
		BildfahrplanGUIController.stringHeight = stringHeight;
		
		// Config für PaintHelper neu laden
		ph.readConfig();
		
		// ### Betriebsstellen-Linien ###
		{
			Color c = bildfahrplanConfig.getBetriebsstellenFarbe();
			FirstLastList<Betriebsstelle> betriebsstellen = editorDaten.getStreckenabschnitt().getBetriebsstellen();
			
			int bsCounter = 0;
			for(Betriebsstelle bs : betriebsstellen)
			{
				double km = editorDaten.getStreckenKm(bs);
				int lineHeight = bildfahrplanConfig.getLineHeight();
				final int offsetX = bildfahrplanConfig.getOffsetX();
				final int textY = bildfahrplanConfig.getTextMarginTop() + ((stringHeight + bildfahrplanConfig.getOffsetY()) * (bsCounter % bildfahrplanConfig.getZeilenAnzahl()))
						+ bildfahrplanConfig.getTextMarginBottom();
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
			Color c = bildfahrplanConfig.getZeitenFarbe();
			
			// Zeichnet die waagerechte Linie bei jeder angegebenen Zeit
			int zeitIntervall = bildfahrplanConfig.getZeitIntervall();
			// Min-Zeit, auf 10 abgerundet
			int zeit = (int) Math.ceil(minZeit / 10) * 10;
			int zeitLinienStart = 5;
			int textPosX = 5;
			
			Stroke duenneLinie = new BasicStroke(1);
			Stroke dickeLinie = new BasicStroke(3);
			
			while(zeit <= maxZeit)
			{
				int thisZeit = zeit;
				Stroke s = (thisZeit % 60 == 0) ? dickeLinie : duenneLinie;
				String text = TimeFormater.doubleToString(zeit);
				
				// Linie in gui
				guiPaints.add(g -> ph.paintLine(g, zeitLinienStart, ph.getZeitPos(thisZeit), gui.getWidth(), ph.getZeitPos(thisZeit), c, s));
				
				// Text in zeilenGui
				zeilenGuiPaints.add(g -> ph.paintText(g, textPosX, ph.getZeitPos(thisZeit) + (stringHeight / 3), c, text));
				
				zeit += zeitIntervall;
			}
		}
		
		// ### Fahrten-Linien ###
		{
			int schachtelung = bildfahrplanConfig.getSchachtelung();
			int zeigeZugnamen = bildfahrplanConfig.getZeigeZugnamen();
			boolean zeigeZeiten = bildfahrplanConfig.getZeigeZeiten();
			int zeigeRichtung = bildfahrplanConfig.getZeigeRichtung();
			
			Set<Fahrt> fahrten = editorDaten.getFahrten();
			
			synchronized(fahrten)
			{
				for(Fahrt fahrt : fahrten)
				{
					double letzteZeit = -1;
					double letzterKm = -1;
					String vollerZugName = fahrt.getName();
					String templateName = fahrt.getTemplateName();
					String templateZid = fahrt.getTemplateZid();
					
					if(bildfahrplanConfig.testIgnorierteZuege(vollerZugName)
							|| bildfahrplanConfig.testIgnorierteTemplates(templateName))
					{
						log.trace("Zug {} ignoriert", vollerZugName);
						continue;
					}
					
					// Darstellung
					FahrtDarstellung[] fahrtDarstellungen = darstellungHandler.getFahrtDarstellungen(vollerZugName, templateName, templateZid);
					Color fahrtFarbe = fahrtDarstellungen[0].getFarbe();
					
					for(Fahrplanhalt fh : fahrt.getFahrplanhalte())
					{
						double an = fh.getMinZeit();
						if(an > maxZeit)
						{
							continue;
						}
						
						double kmAn = editorDaten.getStreckenKm(fh.getGleisabschnitt().getParent().getParent());
						
						String zugName = vollerZugName;
						if(!bildfahrplanConfig.getZeigeZugnamenKommentare() && vollerZugName.indexOf('%') >= 0)
						{
							// entferne alles ab dem ersten %, falls vorhanden
							zugName = vollerZugName.substring(0, vollerZugName.indexOf('%'));
						}
						
						// Übersetzten von dynamischen Variablen
						double kmAb = letzterKm;
						double ab = letzteZeit;
						String name = zugName;
						String vollerName = vollerZugName;
						double showTextWidth = fontMetrics.stringWidth(name) * 0.9;
						
						// für ZeigeZeiten
						int diffX = 5;
						int diffY = -3;
						
						// Minuten aus den Zeiten auslesen
						String abMinute = TimeFormater.doubleToMinute(ab);
						String anMinute = TimeFormater.doubleToMinute(an);
						int abMinuteBreite = fontMetrics.stringWidth(abMinute) - 2;
						int anMinuteBreite = fontMetrics.stringWidth(anMinute) - 2;
						int abVerschiebungY = diffY;
						int anVerschiebungY = stringHeight + diffY;
						
						// erst ab dem 2. Fahrplanhalt zeichnen (die Linie, die zum Fahrplanhalt hinführt)
						if(letzteZeit >= 0 && letzterKm >= 0 && letzteZeit >= minZeit && kmAb >= 0)
						{
							// nicht zeichnen, wenn Richtung ausgeblendet
							if((kmAb < kmAn && (zeigeRichtung & 1) != 0) || (kmAb > kmAn && (zeigeRichtung & 2) != 0))
							{
								fahrtPaints.add(g -> {
									FirstLastLinkedList<CalculatableLine> zugLinien = new FirstLastLinkedList<>();
									int x1 = ph.getWegPos(kmAb);
									int x2 = ph.getWegPos(kmAn);
									
									int zeitenLTRx1 = x1 + diffX;
									int zeitenLTRx2 = x2 - anMinuteBreite - diffX;
									int zeitenRTLx1 = x1 - abMinuteBreite - diffX;
									int zeitenRTLx2 = x2 + diffX;
									
									// Schachtelung der Züge nach Minuten
									// for(int schachtelungVerschiebung = (int) minZeit; schachtelungVerschiebung <= maxZeit;)
									for(int sch = 0; sch < diffZeit; sch += schachtelung)
									{
										int y1 = ph.getZeitPos(ab - sch);
										int y2 = ph.getZeitPos(an - sch);
										
										for(FahrtDarstellung darstellung: fahrtDarstellungen)
										{
											ph.paintLine(g, x1, y1, x2, y2, darstellung);
										}
										zugLinien.add(new CalculatableLine(vollerName, x1, y1, x2, y2));
										
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
											// Textgröße holen
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
									return zugLinien;
								});
							}
						}
						
						// für nächsten Eintrag
						letzteZeit = fh.getMaxZeit();
						letzterKm = editorDaten.getStreckenKm(fh.getGleisabschnitt().getParent().getParent());
						
					} // for(Fahrplanhalt fh: fahrt.getFahrplanhalte())
				} // for(Fahrt fahrt: fahrten)
			} // synchronized(fahrten)
		}
		
		// Paint-Objekte überschreiben
		gui.setPaintables(guiPaints);
		gui.setFahrtPaintables(fahrtPaints);
		spaltenGui.setPaintables(spaltenGuiPaints);
		zeilenGui.setPaintables(zeilenGuiPaints);
		
		log.trace("recalc fertig");
	}
}
