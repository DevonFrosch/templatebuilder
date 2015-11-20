package de.stsFanGruppe.templatebuilder.gui.bildfahrplan;

import java.awt.*;
import java.util.Set;
import de.stsFanGruppe.templatebuilder.strecken.Streckenabschnitt;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.tools.NullTester;

public class BildfahrplanGUIController implements BildfahrplanController
{
	private BildfahrplanGUI gui = null;
	private BildfahrplanZeichner zeichner = null;
	
	public BildfahrplanGUIController()
	{
		
	}
	
	public void setBildfahrplanGUI(BildfahrplanGUI gui)
	{
		this.gui = gui;
		this.zeichner = gui.getBildfahrplanZeichner();
		// Sicherstellen, dass die Operation einen Effekt hatte...
		assert zeichner != null;
	}
	
	public void setPanelHeight(int panelHeight)
	{
		NullTester.test(zeichner);
		Dimension size = zeichner.getMinimumSize();
		size.setSize(size.getWidth(), panelHeight);
		zeichner.setMinimumSize(size);
	}
	
	public void ladeStreckenabschnitt(Streckenabschnitt streckenabschnitt)
	{
		NullTester.test(zeichner);
		zeichner.setStreckenabschnitt(streckenabschnitt);
	}
	public void ladeZüge(Set<Fahrt> fahrten)
	{
		NullTester.test(zeichner);
		zeichner.zeichneFahrten(fahrten);
		zeichner.optimizeHeight();
	}
	
	private static void log(String text)
	{
		System.out.println("BildfahrplanGUIController: "+text);
	}
}
