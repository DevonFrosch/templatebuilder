package de.stsFanGruppe.templatebuilder.editor.bildfahrplan;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.config.GeneralConfig;
import de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.FahrtDarstellungHandler;
import de.stsFanGruppe.templatebuilder.editor.EditorDaten;
import de.stsFanGruppe.templatebuilder.editor.EditorGUIController;
import de.stsFanGruppe.templatebuilder.editor.fahrtEditor.FahrtEditorGUIController;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Gleisabschnitt;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.types.Schachtelung;
import de.stsFanGruppe.templatebuilder.zug.*;
import de.stsFanGruppe.tools.*;

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
	protected Set<FahrtabschnittCalculatableLine> zugLinien = new HashSet<>();
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
		editorDaten.setBildfahrplan(this);
		editorDaten.registerFahrtenGeladenCallback(() -> {
			gui.recalculatePanelSize();
		});
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
	
	public Set<FahrtabschnittCalculatableLine> getZugLinien()
	{
		synchronized(zugLinienLock)
		{
			return new HashSet<>(zugLinien);
		}
	}
	
	public void setZugLinien(Set<FahrtabschnittCalculatableLine> zugLinien)
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
	
	public void ladeTemplates(Collection<? extends Template> templates)
	{
		editorDaten.ladeTemplates(templates);
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
		java.util.List<HervorgehobeneFahrtabschnitt> zuege = bildfahrplanConfig.getFahrtDarstellungConfig().getHervorgehobeneFahrtabschnitte(editorDaten);
		
		if(lastClickTime + 5.0e8 > System.nanoTime() && zuege.size() > 0 && zuege != null)
		{
			chooseZug(zuege);
		}
		else
		{
			Map<Fahrt, FahrtabschnittCalculatableLine> linienMap = new HashMap<>();
			for(FahrtabschnittCalculatableLine linie: this.getZugLinien())
			{
				Fahrt fahrt = linie.getFahrtabschnitt().getFahrt();
				
				if(linie.isPunktAufLinie(e.getX(), e.getY(), 10) && !linienMap.containsKey(fahrt))
				{
					linienMap.put(fahrt, linie);
				}
			}
			
			Set<Fahrtabschnitt> abschnitte = linienMap
					.values()
					.stream()
					.map(linie -> linie.getFahrtabschnitt())
					.collect(Collectors.toSet());
			
			bildfahrplanConfig.getFahrtDarstellungConfig().setHervorgehobeneZuege(abschnitte);
		}
		lastClickTime = System.nanoTime();
	}
	
	private void chooseZug(List<HervorgehobeneFahrtabschnitt> hervorgehobeneFahrtabschnitte) {
		
		//Nur wenn es mehr als 1 Zug in diesem Abschnitt gibt.
		if(hervorgehobeneFahrtabschnitte.size() > 1) 
		{
			JDialog zuegeAuswahlDialog = new JDialog();
			zuegeAuswahlDialog.setTitle("Zugauswahl");
			
			JLabel zuegeAuswahlUeberschrift = new JLabel("<html>Es wurden mehrere Züge gefunden.<p/>Bitte ein Zug auswählen:</html>");
			zuegeAuswahlUeberschrift.setSize(225, 30);
			zuegeAuswahlDialog.add(zuegeAuswahlUeberschrift);
			
			for(HervorgehobeneFahrtabschnitt hervorgehobeneFahrtabschnitt : hervorgehobeneFahrtabschnitte) 
			{
				Fahrtabschnitt abschnitt = hervorgehobeneFahrtabschnitt.getFahrtabschnitt();
				Fahrt fahrt = abschnitt.getFahrt();
				String abfahrt = TimeFormater.doubleToString(abschnitt.getFahrt().getMinZeit());
				String zuegeAuswahlName = fahrt.getName() + " (ab " + abfahrt + ")";
				
				JButton zuegeAuswahlButton = new JButton(zuegeAuswahlName);
				zuegeAuswahlButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) {
						zuegeAuswahlDialog.setVisible(false);
						openFahrtenEditor(editorDaten, fahrt);
					}
				});
				zuegeAuswahlDialog.add(zuegeAuswahlButton);
			}
			zuegeAuswahlDialog.setLayout(new FlowLayout());
			zuegeAuswahlDialog.setSize(250, (30 * hervorgehobeneFahrtabschnitte.size()) + 70);
			zuegeAuswahlDialog.setVisible(true);
		} else {
			openFahrtenEditor(editorDaten, hervorgehobeneFahrtabschnitte.get(0).getFahrtabschnitt().getFahrt());
		}
	}
	
	private void openFahrtenEditor(EditorDaten editorDaten, Fahrt fahrt)
	{
		if(editorDaten == null)
		{
			log.error("openFahrtenEditor: Keine editorDaten vorhanden!");
			return;
		}
		
		FahrtEditorGUIController.openOrFocus(editorDaten, fahrt);
	}
	
	// Interne Funktionen
	protected void optimizeHeight()
	{
		if(gui == null || !editorDaten.hasFahrten())
		{
			return;
		}
		log.trace("optimizeHeight()");
		
		double minZeit = editorDaten.getMinZeit().getAsDouble();
		double maxZeit = editorDaten.getMaxZeit().getAsDouble();
		
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
			Schachtelung schachtelung = editorDaten.getSchachtelung();
			int schachtelungMinuten = editorDaten.getSchachtelungMinuten();
			Template schachtelungTemplate = editorDaten.getSchachtelungTemplate();
			Set<Integer> verschiebungen = getVerschiebungen(schachtelungTemplate);
			log.info("Schachtelung {} Minuten {}", schachtelung, schachtelungMinuten);
			
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
					Fahrplanhalt letzterFahrplanhalt = null;
					String vollerZugName = fahrt.getName();
					Template template = fahrt.getTemplate();
					
					if(bildfahrplanConfig.testIgnorierteZuege(vollerZugName)
							|| bildfahrplanConfig.testIgnorierteTemplates(template))
					{
						continue;
					}
					
					// Darstellung
					FahrtDarstellung[] fahrtDarstellungen = darstellungHandler.getFahrtDarstellungen(vollerZugName, template);
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
						double showTextWidth = fontMetrics.stringWidth(name) * 0.9;
						
						// für ZeigeZeiten
						int diffX = 5;
						int diffY = -3;
						
						// Minuten aus den Zeiten auslesen
						String abMinute = TimeFormater.doubleToMinute(ab);
						String anMinute = TimeFormater.doubleToMinute(an);
						int abMinuteBreite = fontMetrics.stringWidth(abMinute) - 2;
						int anMinuteBreite = fontMetrics.stringWidth(anMinute) - 2;
						
						// erst ab dem 2. Fahrplanhalt zeichnen (die Linie, die zum Fahrplanhalt hinführt)
						if(letzterFahrplanhalt != null && letzteZeit >= 0 && letzterKm >= 0 && letzteZeit >= minZeit && kmAb >= 0)
						{
							// nicht zeichnen, wenn Richtung ausgeblendet
							if((kmAb < kmAn && (zeigeRichtung & 1) != 0) || (kmAb > kmAn && (zeigeRichtung & 2) != 0))
							{
								Fahrtabschnitt abschnitt = new Fahrtabschnitt(letzterFahrplanhalt, fh);
								
								fahrtPaints.add(g -> {
									Set<FahrtabschnittCalculatableLine> zugLinien = new HashSet<>();
									int x1 = ph.getWegPos(kmAb);
									int x2 = ph.getWegPos(kmAn);
									
									int zeitenLTRx1 = x1 + diffX;
									int zeitenLTRx2 = x2 - anMinuteBreite - diffX;
									int zeitenRTLx1 = x1 - abMinuteBreite - diffX;
									int zeitenRTLx2 = x2 + diffX;
									
									EbenenZeichner ebenenZeichner = (int verschiebung) -> {
										int y1 = ph.getZeitPos(ab - verschiebung);
										int y2 = ph.getZeitPos(an - verschiebung);
										
										for(FahrtDarstellung darstellung: fahrtDarstellungen)
										{
											ph.paintLine(g, x1, y1, x2, y2, darstellung);
										}
										
										zugLinien.add(new FahrtabschnittCalculatableLine(abschnitt, x1, y1, x2, y2));
										
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
									};
									
									switch(schachtelung) {
										case MINUTEN:
											for(int verschiebung = 0; verschiebung < diffZeit; verschiebung += schachtelungMinuten)
											{
												ebenenZeichner.zeichne(verschiebung);
											}
											break;
										case TEMPLATE:
											for(Integer verschiebung: verschiebungen) {
												ebenenZeichner.zeichne(verschiebung);
											}
											break;
										case KEINE:
										default:
											ebenenZeichner.zeichne(0);
											break;
									}
									return zugLinien;
								});
							}
						}
						
						// für nächsten Eintrag
						letzterFahrplanhalt = fh;
						letzteZeit = fh.getMaxZeit();
						letzterKm = editorDaten.getStreckenKm(fh.getGleisabschnitt().getParent().getParent());
						
					} // for(Fahrplanhalt fh: fahrt.getFahrplanhalte())
				} // for(Fahrt fahrt: templates)
			} // synchronized(templates)
		}
		
		// Paint-Objekte überschreiben
		gui.setPaintables(guiPaints);
		gui.setFahrtPaintables(fahrtPaints);
		spaltenGui.setPaintables(spaltenGuiPaints);
		zeilenGui.setPaintables(zeilenGuiPaints);
		
		log.trace("recalc fertig");
	}
	
	private Set<Integer> getVerschiebungen(Template template) {
		Set<Integer> verschiebungen = new HashSet<>();
		verschiebungen.add(0);
		
		if(template == null) {
			return verschiebungen;
		}
		
		SortedSet<Fahrt> templateFahrten = new TreeSet<>(template.getFahrten());
		Fahrt ersteFahrt = templateFahrten.first();
		Fahrplanhalt ersterHalt = ersteFahrt
				.getFahrplanhalte()
				.stream()
				.filter(fh -> fh.getAbfahrt().isPresent())
				.findFirst()
				.orElse(null);
		
		if(ersterHalt == null) {
			log.warn("getVerschiebungen(): Keinen passenden Fahrplanhalt in der ersten Fahrt gefunden! Schachtelung wird ignoriert.");
			return verschiebungen;
		}
		
		double ersteAbfahrt = ersterHalt.getAbfahrt().getAsDouble();
		Gleisabschnitt gleisabschnitt = ersterHalt.getGleisabschnitt();
		
		for(Fahrt fahrt: templateFahrten) {
			Fahrplanhalt halt = fahrt.getFahrplanhalt(gleisabschnitt);
			
			if(halt == null) {
				log.info("getVerschiebungen(): Überspringe Fahrt {}, Fahrplanhalt nicht gefunden", fahrt.getName());
				continue;
			}
			
			double differenz = halt.getMaxZeit() - ersteAbfahrt;
			int verschiebung = (int) Math.floor(differenz);
			verschiebungen.add(verschiebung);
		}
		
		return verschiebungen;
	}
	
	private interface EbenenZeichner {
		void zeichne(int verschiebung);
	}
}
