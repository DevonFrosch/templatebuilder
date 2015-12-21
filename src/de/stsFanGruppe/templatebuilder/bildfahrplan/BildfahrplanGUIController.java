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
import de.stsFanGruppe.templatebuilder.strecken.Betriebsstelle;
import de.stsFanGruppe.templatebuilder.strecken.Strecke;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrplanhalt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.FirstLastLinkedList;
import de.stsFanGruppe.tools.FirstLastList;
import de.stsFanGruppe.tools.NullTester;
import de.stsFanGruppe.tools.TimeFormater;

public class BildfahrplanGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanGUIController.class);

	protected BildfahrplanConfig config;
	protected BildfahrplanGUI gui = null;
	protected BildfahrplanSpaltenheaderGUI spaltenGui = null;
	protected BildfahrplanZeilenheaderGUI zeilenGui = null;
	
	protected int stringHeight = 0;
	
	protected Streckenabschnitt streckenabschnitt;
	protected Map<Betriebsstelle, Double> streckenKm;
	protected Set<Fahrt> fahrten;
	
	protected double diffKm;
	
	public BildfahrplanGUIController(BildfahrplanConfig config)
	{
		NullTester.test(config);
		this.config = config;
		config.registerChangeHandler(() -> gui.recalculatePanelSize());
	}
	
	public void setBildfahrplanGUI(BildfahrplanGUI gui)
	{
		NullTester.test(gui);
		this.gui = gui;
	}
	
	public void setBildfahrplanSpaltenHeaderGUI(BildfahrplanSpaltenheaderGUI spaltenGui)
	{
		NullTester.test(spaltenGui);
		this.spaltenGui = spaltenGui;
	}
	
	public void setBildfahrplanZeilenheaderGUI(BildfahrplanZeilenheaderGUI zeilenGui)
	{
		NullTester.test(zeilenGui);
		this.zeilenGui = zeilenGui;
	}
	
	public BildfahrplanConfig getConfig()
	{
		return config;
	}
	
	// Offizielle Funktionen
	public void ladeStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		NullTester.test(gui);
		NullTester.test(streckenabschnitt);
		
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
		
		gui.recalculatePanelSize();
	}
	public void ladeZüge(Collection<? extends Fahrt> fahrten)
	{
		NullTester.test(gui);
		NullTester.test(fahrten);
		if(this.fahrten == null)
		{
			this.fahrten = new HashSet<>();
		}
		
		fahrten.forEach((f) -> {
			NullTester.test(f);
			this.fahrten.add(f);
		});
		
		gui.recalculatePanelSize();
	}
	public Streckenabschnitt getStreckenabschnitt()
	{
		return streckenabschnitt;
	}
	public Set<Fahrt> getFahrten()
	{
		return fahrten;
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
	protected synchronized void optimizeHeight()
	{
		if(gui == null || fahrten == null || fahrten.isEmpty())
		{
			return;
		}
		
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
			diffKm = -1;
		}
	}
	
	protected int getZeitPos(double zeit)
	{
		assert config != null;
		int min = config.getMarginTop();
		int hoehe = config.getZeichnenHoehe(gui);
		double diffZeit = config.getMaxZeit() - config.getMinZeit();
		
		double zeitFaktor = zeit - config.getMinZeit();
		
		return (int) ((zeitFaktor / diffZeit * hoehe) + min);
	}
	protected int getWegPos(double km)
	{
		assert config != null;
		int minBreite = config.getMarginLeft();
		int diffBreite = config.getZeichnenBreite(gui);
		
		return (int) ((km / diffKm * diffBreite) + minBreite);
	}
	
	// alle Grafikobjekte neu berechnen
	protected void recalculate(FontMetrics fontMetrics)
	{
		if(config == null || streckenabschnitt == null || streckenKm == null || fahrten == null)
		{
			return;
		}
		
		assert gui != null;
		assert spaltenGui != null;
		assert zeilenGui != null;

		Collection<Paintable> guiPaints = new FirstLastLinkedList<>();
		Collection<Paintable> spaltenGuiPaints = new FirstLastLinkedList<>();
		Collection<Paintable> zeilenGuiPaints = new FirstLastLinkedList<>();
		
		double minZeit = config.getMinZeit();
		double maxZeit = config.getMaxZeit();
		stringHeight = fontMetrics.getHeight();
		
		// ### Betriebsstellen-Linien ###
		{
			Color c = config.getBetriebsstellenFarbe();
			FirstLastList<Betriebsstelle> betriebsstellen = streckenabschnitt.getBetriebsstellen();
			
			int bsCounter=0;
			for(Betriebsstelle bs: streckenabschnitt.getBetriebsstellen())
			{
				double km = streckenKm.get(bs);
				
				// Linie in gui
				guiPaints.add(new PaintableLine(getWegPos(km), getZeitPos(minZeit), getWegPos(km), getZeitPos(maxZeit), c));
				
				// Linie in spaltenGui
				int x = getWegPos(km);
				int y1 = spaltenGui.getHeight();
				int y2 = y1 - config.getLineHeight();
				
				spaltenGuiPaints.add(new PaintableLine(x, y1, x, y2, c));
				
				// Text in spaltenGui
				int offsetX = config.getOffsetX();
				int textY = config.getTextMarginTop() + ((stringHeight + config.getOffsetY()) * (bsCounter % config.getZeilenAnzahl())) + config.getTextMarginBottom();
				String name = bs.getName();
				int stringWidth = fontMetrics.stringWidth(name);
				
				if(x < stringWidth)
				{
					// linker Rand
					spaltenGuiPaints.add(new PaintableText(x - offsetX, textY, c, name));
				}
				else if((x + stringWidth) > spaltenGui.getWidth())
				{
					// rechter Rand
					spaltenGuiPaints.add(new PaintableText(x - stringWidth + offsetX, textY, c, name));
				}
				else
				{
					// sonstwo
					spaltenGuiPaints.add(new PaintableText(x - (stringWidth / 2), textY, c, name));
				}
				
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
			int zeitLinienEnde = gui.getWidth();
			int textPosX = 5;
			int maxWidth = 0;
			
			Stroke duenneLinie = new BasicStroke(1);
			Stroke dickeLinie = new BasicStroke(3);
			
			while(zeit <= maxZeit)
			{
				Stroke s = (zeit % 60 == 0) ?  dickeLinie : duenneLinie;
				int zeitPos = getZeitPos(zeit);
				
				// Linie in gui
				guiPaints.add(new PaintableLine(zeitLinienStart, zeitPos, zeitLinienEnde, zeitPos, c, s));
				
				// Schriftbreite erkennen
				String text = TimeFormater.doubleToString(zeit);
				int stringWidth = fontMetrics.stringWidth(text);
				maxWidth = Integer.max(maxWidth, stringWidth);
				zeit += zeitIntervall;
				
				// Text in zeilenGui
				zeilenGuiPaints.add(new PaintableText(textPosX , zeitPos + (stringHeight/3), c, text));
			}
		}
		
		// ### Fahrten-Linien ###
		{
			Color c = config.getFahrtenFarbe();
			
			// Schachtelung der Züge nach Minuten
			int verschachtelungVerschiebung = 0;
			int schachtelung = config.getSchachtelung();
			
			for(int i = (int) minZeit; i <= maxZeit;)
			{
				for(Fahrt fahrt : fahrten)
				{
					double ab = -1;
					double kmAb = -1;
					
					for(Fahrplanhalt fh : fahrt.getFahrplanhalte())
					{
						if(ab >= 0 && ab >= i && kmAb >= 0)
						{
							double an = fh.getAnkunft();
							
							if(ab < minZeit || an > maxZeit) { return; }
							
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
							
							guiPaints.add(new PaintableLine(x1, y1, x2, y2, c));
							
							// Zeiten zeichnen, wenn in der Config, dies aktiv ist.
							if(config.getZeigeZeiten())
							{
								int diffX = 5;
								int diffY = -3;
								
								// Minuten aus den Zeiten auslesen
								String abMinute = TimeFormater.doubleToMinute(ab);
								String anMinute = TimeFormater.doubleToMinute(an);
								int abVerschiebungY = diffY;
								int anVerschiebungY = stringHeight + diffY;
								
								// Zeiten zeichnen
								if(x1 < x2)
								{
									// Wenn die Linie von Links nach Rechts gezeichnet wird.
									guiPaints.add(new PaintableText(x1 + diffX, y1 + diffY, c, abMinute));
									guiPaints.add(new PaintableText(x2 - (fontMetrics.stringWidth(anMinute)-2) - diffX, y2 + stringHeight + diffY, c, anMinute));
								}
								else
								{
									// Wenn die Linie von Rechts nach Links gezeichnet wird.
									guiPaints.add(new PaintableText(x1 - (fontMetrics.stringWidth(anMinute)-2) - diffX, y1 + diffY, c, abMinute));
									guiPaints.add(new PaintableText(x2 + diffX, y2 + stringHeight + diffY, c, anMinute));
								}
							}
							
							boolean zeichneZugnamen = config.getZeigeZugnamen() != 0;
							
							if(config.getZeigeZugnamen() == 2)
							{
								// Zugnamen nur wenn Platz ist
								// Textgröße holen
								double lineLenght = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
								
								// Text nur, wenn dieser weniger als doppelt so
								// breit ist
								if(fontMetrics.stringWidth(name) * 0.9 > lineLenght)
								{
									zeichneZugnamen = false;
								}
							}
							
							if(zeichneZugnamen)
							{
								guiPaints.add(new PaintableRotatedText(x1, y1, x2, y2, c, name, -2));
							}
						}
						
						// für nächsten Eintrag
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
		
		// Paint-Objekte überschreiben
		gui.setPaintables(guiPaints);
		spaltenGui.setPaintables(spaltenGuiPaints);
		zeilenGui.setPaintables(zeilenGuiPaints);
	}
}
