package de.stsFanGruppe.templatebuilder.fahrtenFarbe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.io.InputStream;
import java.io.OutputStream;
import de.stsFanGruppe.templatebuilder.config.ConfigController;
import de.stsFanGruppe.templatebuilder.config.FahrtenFarbeSettingsGUI;
import de.stsFanGruppe.tools.PreferenceHandler;

public class FahrtenFarbeConfig extends ConfigController
{
	
	private static PreferenceHandler prefs;
	private static FahrtenFarbeSettingsGUI gui;
	
	// Farbeinstellungen
	public static final String CONFIG_LINIEN_FARBE = "bildfahrplan/darstellung/linie/farben";
	public static final String CONFIG_LINIEN_STAERKE = "bildfahrplan/darstellung/linie/staerke";
	public static final String CONFIG_LINIEN_ART = "bildfahrplan/darstellung/linie/art";
	
	// Linienarten
	public static final float[] DURCHGEZOGENE_LINIE = {0};
	public static final float[] GEPUNKTE_LINIE = {10, 10};
	public static final float[] GESTRICHELTE_LINIE = {30, 10};
	public static final float[] KURZ_LANG_LINIE = {10, 10, 30, 10};
	public static final float[] KURZ_KURZ_LANG_LINIE = {10, 10, 10, 10, 30, 10};
	public static final float[] KURZ_LANG_LANG_LINIE = {10, 10, 30, 10, 30, 10};
	
	private static final Color DEFAULT_LINIEN_FARBE = Color.BLACK;
	private static final int DEFAULT_LINIEN_STAERKE = 1;
	private static final float[] DEFAULT_LINIEN_ART = DURCHGEZOGENE_LINIE;
	
	// Linienarten
	public static enum LineType
	{
		SOLID_LINE
		{
			public Stroke getStroke()
			{
				return new BasicStroke(3);
			}
		},
		DOTTED_LINE
		{
			public Stroke getStroke()
			{
				return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, GEPUNKTE_LINIE, 1);
			}
		},
		DASEHED_LINE
		{
			public Stroke getStroke()
			{
				return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, GESTRICHELTE_LINIE, 1);
			}
		},
		SHORT_LONG_LINE
		{
			public Stroke getStroke()
			{
				return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, KURZ_LANG_LINIE, 1);
			}
		},
		SHORT_SHORT_LONG_LINE
		{
			public Stroke getStroke()
			{
				return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, KURZ_KURZ_LANG_LINIE,
						1);
			}
		},
		SHORT_LONG_LONG_LINE
		{
			public Stroke getStroke()
			{
				return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, KURZ_LANG_LANG_LINIE,
						1);
			}
		};
		
		public abstract Stroke getStroke();
	}
	
	public Color getDefaultLinienFarbe()
	{
		return DEFAULT_LINIEN_FARBE;
	}
	
	public static int getDefaultLinienStaerke()
	{
		return DEFAULT_LINIEN_STAERKE;
	}
	
	public static String getDefaultLinienStaerkeToString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(DEFAULT_LINIEN_STAERKE);
		String linienStaerke = sb.toString();
		return linienStaerke;
	}
	
	public static float getDefaultLinienArt(int i)
	{
		return DEFAULT_LINIEN_ART[i];
	}
	
	public String colorToString(Color color)
	{
		if(Color.BLACK.equals(color))
		{
			String c = "Schwarz";
			return c;
		}
		return "Schwarz2";
	}

	public boolean schreibeEinstellungen()
	{
		return prefs.schreibeEinstellungen();
	}
	public boolean importXML(InputStream is)
	{
		return prefs.importXML(is);
	}
	public boolean exportXML(OutputStream os)
	{
		return prefs.exportXML(os);
	}
}
