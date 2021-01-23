package de.stsFanGruppe.templatebuilder.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import de.stsFanGruppe.templatebuilder.editor.EditorDaten;
import de.stsFanGruppe.templatebuilder.editor.fahrtEditor.FahrtEditorGUI;
import de.stsFanGruppe.templatebuilder.editor.fahrtEditor.FahrtEditorGUIController;
import de.stsFanGruppe.templatebuilder.zug.Fahrt;
import de.stsFanGruppe.templatebuilder.zug.Fahrtabschnitt;
import de.stsFanGruppe.templatebuilder.zug.HervorgehobeneFahrtabschnitt;
import de.stsFanGruppe.tools.GUILocker;
import de.stsFanGruppe.tools.TimeFormater;

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
	
	default public void buttonMessage(List<HervorgehobeneFahrtabschnitt> hervorgehobeneFahrtabschnitte, EditorDaten editorDaten) {
		
		JDialog zuegeAuswahlDialog = null;
		//Nur wenn es mehr als 1 Zug in diesem Abschnitt gibt.
		if(hervorgehobeneFahrtabschnitte.size() > 1) 
		{
			zuegeAuswahlDialog = new JDialog();
			zuegeAuswahlDialog.setTitle("Zugauswahl");
			
			JLabel zuegeAuswahlUeberschrift = new JLabel("<html>Es wurden mehrere Züge gefunden.<p/>Bitte ein Zug auswählen:</html>");
			zuegeAuswahlUeberschrift.setSize(225, 30);
			zuegeAuswahlDialog.add(zuegeAuswahlUeberschrift);
			
			for(HervorgehobeneFahrtabschnitt hervorgehobeneFahrtabschnitt : hervorgehobeneFahrtabschnitte) 
			{
				Fahrtabschnitt abschnitt = hervorgehobeneFahrtabschnitt.getFahrtabschnitt();
				Fahrt fahrt = abschnitt.getFahrt();
				String abfahrt = TimeFormater.doubleToString(abschnitt.getFahrt().getMinZeit());
				String zuegeAuswahlName = fahrt.getName() + abfahrt;
				
				JButton zuegeAuswahlButton = new JButton(zuegeAuswahlName);
				zuegeAuswahlButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) {
						openFahrtenEditor(editorDaten, fahrt);
					}
				});
				zuegeAuswahlDialog.add(zuegeAuswahlButton);
			}
			zuegeAuswahlDialog.setLayout(new FlowLayout());
			zuegeAuswahlDialog.setSize(250, (30 * hervorgehobeneFahrtabschnitte.size()) + 50);
			zuegeAuswahlDialog.setVisible(true);
		} else {
			openFahrtenEditor(editorDaten, hervorgehobeneFahrtabschnitte.get(0).getFahrtabschnitt().getFahrt());
		}
	}
	
	default void openFahrtenEditor(EditorDaten editorDaten, Fahrt fahrt)
	{
		FahrtEditorGUIController controller = new FahrtEditorGUIController(editorDaten, () -> GUILocker.unlock(FahrtEditorGUI.class), fahrt);
	}
}
