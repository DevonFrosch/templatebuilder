package de.stsFanGruppe.templatebuilder.types;

public enum Schachtelung
{
	KEINE("KEINE"),
	MINUTEN("MINUTEN"),
	TEMPLATE("TEMPLATE");
	
	private String text;
	
	Schachtelung(String text) {
		this.text = text;
	}
	
	public String toString() {
		return text;
	}
}
