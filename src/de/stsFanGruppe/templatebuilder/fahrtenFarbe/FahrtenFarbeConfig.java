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
	
	//Linienarten
	public static final float[] DURCHGEZOGENE_LINIE = {0};
	public static final float[] GEPUNKTE_LINIE = {10, 10};
	public static final float[] GESTRICHELTE_LINIE = {30, 10};
	public static final float[] KURZ_LANG_LINIE = {10, 10, 30, 10};
	public static final float[] KURZ_KURZ_LANG_LINIE = {10, 10, 10, 10, 30, 10};
	public static final float[] KURZ_LANG_LANG_LINIE = {10, 10, 30, 10, 30, 10};
	
	float[] comboBoxListe[] = {DURCHGEZOGENE_LINIE, GEPUNKTE_LINIE, GESTRICHELTE_LINIE, KURZ_LANG_LINIE, KURZ_KURZ_LANG_LINIE, KURZ_LANG_LANG_LINIE};
	
	public float[][] getComboBoxListe() {
		return comboBoxListe;
	}

	public void setComboBoxListe(float[][] comboBoxListe) {
		this.comboBoxListe = comboBoxListe;
	}

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
