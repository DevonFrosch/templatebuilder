package de.stsFanGruppe.templatebuilder.gui;

import javax.swing.*;

public interface GUI
{
	public void errorMessage(String text, String titel);
	
	public void warningMessage(String text, String titel);
	
	public void infoMessage(String text, String titel);
	
	default public void errorMessage(String text)
	{
		errorMessage(text, "Fehler");
	}
	
	default public void warningMessage(String text)
	{
		warningMessage(text, "Warnung");
	}
	
	default public void infoMessage(String text)
	{
		infoMessage(text, "Information");
	}
	
	default public JComponent createSelectableTextPane(String text)
	{
		JTextPane pane = new JTextPane();
		pane.setText(text);
		pane.setEditable(false);
		pane.setBackground(null);
		pane.setBorder(null);
		return pane;
	}
}
