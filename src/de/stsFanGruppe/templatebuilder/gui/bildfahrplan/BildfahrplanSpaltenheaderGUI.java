package de.stsFanGruppe.templatebuilder.gui.bildfahrplan;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import de.stsFanGruppe.templatebuilder.config.BildfahrplanConfig;
import de.stsFanGruppe.templatebuilder.gui.TemplateBuilderGUI;

public class BildfahrplanSpaltenheaderGUI extends JComponent {
	
	protected BildfahrplanConfig config;
	protected BildfahrplanGUIController controller;
	protected TemplateBuilderGUI parent;
	
	JComponent component;
	int        columns;
	
	/**
	* Wird evtl benötigt?
	* public ColumnHeader(JComponent component, int columns)
	*{
	*	this.component = component;
	*	this.columns   = columns;
	*}
	**/
	
	public void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		// wir nehmen mal an, dass wir Graphics2D haben, sonst wird's schwierig...
		Graphics2D g = (Graphics2D) graphics;
		
		System.setProperty("swing.aatext", "true");
		System.setProperty("awt.useSystemAAFontSettings", "lcd");
		
		controller.guiRepaint();
		
	}
	protected void writeBetriebsstelle(Graphics2D g, double km, String bs)
	{
		assert config != null;
		assert g != null;
		/**
		 * Code überlegen, wie am besten der Text an einer bestimmten Stelle zentriert geschrieben wird und zusätzlich zentriert eine Linie
		 * zu der senkrechten Linie geführt wird. 
		 */
		
	}
}
