package de.stsFanGruppe.templatebuilder.fahrtenFarbe;

import java.awt.Color;

import de.stsFanGruppe.tools.PreferenceHandler;

public class FahrtenFarbeConfig {

	private static PreferenceHandler prefs;
	
	//Farbeinstellungen
	public static final String CONFIG_LINIEN_FARBE = "bildfahrplan/darstellung/linie/farben";
	public static final String CONFIG_LINIEN_STAERKE  = "bildfahrplan/darstellung/linie/staerke";
	public static final String CONFIG_LINIEN_ART = "bildfahrplan/darstellung/linie/art";
	
	private static final Color DEFAULT_LINIEN_FARBE = Color.BLACK;
	private static final int DEFAULT_LINIEN_STAERKE = 1;
	private static final float[] DEFAULT_LINIEN_ART = {10, 10};
	
	
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

	public Color getDefaultLinienFarbe() {
		return DEFAULT_LINIEN_FARBE;
	}

	public static int getDefaultLinienStaerke() {
		return DEFAULT_LINIEN_STAERKE;
	}
	
	public static String getDefaultLinienStaerkeToString() {
		StringBuilder sb = new StringBuilder();
		sb.append(DEFAULT_LINIEN_STAERKE);		
		String linienStaerke = sb.toString();
		return linienStaerke;
	}

	public static float[] getDefaultLinienArt() {
		return DEFAULT_LINIEN_ART;
	}
	
	public String colorToString(Color color)
	{
		if (Color.BLACK.equals(color))
		{
			String c = "Schwarz";
			return c;
		}
		return "Schwarz2";
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
