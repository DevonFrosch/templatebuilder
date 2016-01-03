package de.stsFanGruppe.templatebuilder.fahrtenFarbe;

import java.awt.Color;

public class FahrtenFarbeConfig {

	//Farbeinstellungen
	public static final String CONFIG_LINIEN_FARBE = "bildfahrplan/darstellung/farben/zeiten";
	public static final String CONFIG_LINIEN_STAERKE  = "bildfahrplan/darstellung/farben/fahrten";
	public static final String CONFIG_LINIEN_ART = "bildfahrplan/darstellung/farben/betriebsstellen";
	
	private static final Color DEFAULT_LINIEN_FARBE = Color.RED;
	private static final int DEFAULT_LINIEN_STAERKE = 1;
	private static final Color DEFAULT_LINIEN_ART = Color.BLUE;
	
	public void exportSettings(FahrtenFarbeSettingsGUI gui, FahrtenFarbeSettingsGUI gui2) {
		// TODO Auto-generated method stub
		
	}

	public void importSettings(FahrtenFarbeSettingsGUI gui, FahrtenFarbeSettingsGUI gui2) {
		// TODO Auto-generated method stub
		
	}

	public boolean schreibeEinstellungen() {
		// TODO Auto-generated method stub
		return false;
	}

}
