package de.stsFanGruppe.templatebuilder.gui;

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
}
