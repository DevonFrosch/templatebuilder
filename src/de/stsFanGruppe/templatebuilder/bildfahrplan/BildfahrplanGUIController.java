package de.stsFanGruppe.templatebuilder.bildfahrplan;

import java.awt.*;
import java.util.Set;
import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.NullTester;

public class BildfahrplanGUIController
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BildfahrplanGUIController.class);
	
	protected BildfahrplanGUI gui = null;
	protected BildfahrplanSpaltenheaderGUI spaltenGui = null;
	protected BildfahrplanZeilenheaderGUI zeilenGui = null;
	protected BildfahrplanConfig config;
	
	public BildfahrplanGUIController(BildfahrplanConfig config)
	{
		NullTester.test(config);
		this.config = config;
		config.registerChangeHandler(() -> setPanelSize());
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
	
	public void setPanelSize()
	{
		if(gui == null)
		{
			return;
		}
		
		BildfahrplanConfig config = gui.config;
		Dimension size = gui.getMinimumSize();
		size.setSize(size.getWidth(), config.getPanelHeight());
		gui.setMinimumSize(size);
		gui.setPreferredSize(size);
		gui.revalidate();
		gui.repaint();
	}
	public void optimizeHeight()
	{
		if(gui == null || gui.fahrten == null)
		{
			return;
		}
		
		double minZeit = gui.fahrten.stream().min((a, b) -> Double.compare(a.getMinZeit(), b.getMinZeit())).get().getMinZeit();
		double maxZeit = gui.fahrten.stream().max((a, b) -> Double.compare(a.getMaxZeit(), b.getMaxZeit())).get().getMaxZeit();
		
		gui.config.setZeiten(minZeit - 5, maxZeit);
		
		setPanelSize();
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
	/**
	 * Nachricht von der GUI, dass sie gerade am repainten ist
	 */
	public void guiRepaint()
	{
		if(config.needsAutoSize())
		{
			optimizeHeight();
		}
	}
	
	public void ladeStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		NullTester.test(gui);
		gui.setStreckenabschnitt(streckenabschnitt);
		repaint();
	}
	public void ladeZüge(Set<Fahrt> fahrten)
	{
		NullTester.test(gui);
		gui.zeichneFahrten(fahrten);
		repaint();
	}
	
	public Streckenabschnitt getStreckenabschnitt()
	{
		return gui.streckenabschnitt;
	}
	public Set<Fahrt> getFahrten()
	{
		return gui.fahrten;
	}
}
