package de.stsFanGruppe.templatebuilder.config;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.io.InputStream;
import java.io.OutputStream;
import de.stsFanGruppe.tools.PreferenceHandler;

public class FahrtenFarbeConfig extends ConfigController
{
	
	private PreferenceHandler prefs;
	private static FahrtenFarbeSettingsGUI gui;
	
	// Farbeinstellungen
	public static final String CONFIG_STANDARD_LINIEN_FARBE = "bildfahrplan/darstellung/linie/farben";
	public static final String CONFIG_STANDARD_LINIEN_STAERKE = "bildfahrplan/darstellung/linie/staerke";
	public static final String CONFIG_STANDARD_LINIEN_TYP = "bildfahrplan/darstellung/linie/typ";
	
	public static final Color DEFAULT_STANDARD_LINIEN_FARBE = Color.BLACK;
	public static final int DEFAULT_STANDARD_LINIEN_STAERKE = 1;
	public static final LineType DEFAULT_STANDARD_LINIEN_TYP = LineType.SOLID_LINE;
	
	// Konstruktoren
	public FahrtenFarbeConfig()
	{
		log.debug("Neue BildfahrplanConfig()");
		this.prefs = new PreferenceHandler(FahrtenFarbeConfig.class, () -> notifyChange());
		assert prefs != null;
	}

	// Getter / Setter
	public Color getStandardLinienFarbe()
	{
		return prefs.getColor(CONFIG_STANDARD_LINIEN_FARBE, DEFAULT_STANDARD_LINIEN_FARBE);
	}
	public void setStandardLinienFarbe(Color standardLinienFarbe)
	{
		prefs.setColor("standardLinienFarbe", CONFIG_STANDARD_LINIEN_FARBE, standardLinienFarbe);
	}
	public int getStandardLinienStärke()
	{
		return prefs.getInt(CONFIG_STANDARD_LINIEN_STAERKE, DEFAULT_STANDARD_LINIEN_STAERKE);
	}
	public void setStandardLinienStärke(int standardLinienStärke)
	{
		prefs.setInt("standardLinienStärke", CONFIG_STANDARD_LINIEN_STAERKE, standardLinienStärke);
	}
	public LineType getStandardLinienTyp()
	{
		String name = prefs.getString(CONFIG_STANDARD_LINIEN_TYP, null);
		return getLineType(name);
	}
	public String getStandardLinienTypString()
	{
		return prefs.getString(CONFIG_STANDARD_LINIEN_TYP, DEFAULT_STANDARD_LINIEN_TYP.name());
	}
	public void setStandardLinienTyp(LineType standardLinienFarbe)
	{
		prefs.setString("standardLinienFarbe", CONFIG_STANDARD_LINIEN_TYP, standardLinienFarbe.name());
	}
	public void setStandardLinienTyp(String standardLinienFarbe)
	{
		prefs.setString("standardLinienFarbe", CONFIG_STANDARD_LINIEN_TYP, standardLinienFarbe);
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
	
	// Linientypen
	public static final float[] DURCHGEZOGENE_LINIE = {0};
	public static final float[] GEPUNKTE_LINIE = {10, 10};
	public static final float[] GESTRICHELTE_LINIE = {30, 10};
	public static final float[] KURZ_LANG_LINIE = {10, 10, 30, 10};
	public static final float[] KURZ_KURZ_LANG_LINIE = {10, 10, 10, 10, 30, 10};
	public static final float[] KURZ_LANG_LANG_LINIE = {10, 10, 30, 10, 30, 10};
	
	public static enum LineType
	{
		SOLID_LINE {
			public Stroke getStroke() {
				return new BasicStroke(3);
			}
		},
		DOTTED_LINE {
			public Stroke getStroke() {
				return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, GEPUNKTE_LINIE, 1);
			}
		},
		DASEHED_LINE {
			public Stroke getStroke() {
				return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, GESTRICHELTE_LINIE, 1);
			}
		},
		SHORT_LONG_LINE {
			public Stroke getStroke() {
				return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, KURZ_LANG_LINIE, 1);
			}
		},
		SHORT_SHORT_LONG_LINE {
			public Stroke getStroke() {
				return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, KURZ_KURZ_LANG_LINIE, 1);
			}
		},
		SHORT_LONG_LONG_LINE {
			public Stroke getStroke() {
				return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, KURZ_LANG_LANG_LINIE, 1);
			}
		};
		public abstract Stroke getStroke();
	}
	
	public static String getLineTypeName(LineType type)
	{
		return type.name();
	}
	public static LineType getLineType(String name)
	{
		if(name == null)
		{
			return DEFAULT_STANDARD_LINIEN_TYP;
		}
		
		try
		{
			return Enum.valueOf(LineType.class, name);
		}
		catch(IllegalArgumentException e)
		{
			log.warn("Unknown LineType {}", name);
			return DEFAULT_STANDARD_LINIEN_TYP;
		}
	}
}
