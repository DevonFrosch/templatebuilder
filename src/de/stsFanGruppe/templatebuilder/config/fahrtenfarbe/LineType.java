package de.stsFanGruppe.templatebuilder.config.fahrtenfarbe;

import java.awt.BasicStroke;
import java.awt.Stroke;

public enum LineType
{
	SOLID_LINE {
		public Stroke getStroke() {
			float[] DURCHGEZOGENE_LINIE = {0};
			return new BasicStroke(3);
		}
	},
	DOTTED_LINE {
		public Stroke getStroke() {
			float[] GEPUNKTE_LINIE = {10, 10};
			return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, GEPUNKTE_LINIE, 1);
		}
	},
	DASEHED_LINE {
		public Stroke getStroke() {
			float[] GESTRICHELTE_LINIE = {30, 10};
			return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, GESTRICHELTE_LINIE, 1);
		}
	},
	SHORT_LONG_LINE {
		public Stroke getStroke() {
			float[] KURZ_LANG_LINIE = {10, 10, 30, 10};
			return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, KURZ_LANG_LINIE, 1);
		}
	},
	SHORT_SHORT_LONG_LINE {
		public Stroke getStroke() {
			float[] KURZ_KURZ_LANG_LINIE = {10, 10, 10, 10, 30, 10};
			return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, KURZ_KURZ_LANG_LINIE, 1);
		}
	},
	SHORT_LONG_LONG_LINE {
		public Stroke getStroke() {
			float[] KURZ_LANG_LANG_LINIE = {10, 10, 30, 10, 30, 10};
			return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, KURZ_LANG_LANG_LINIE, 1);
		}
	};
	
	// Logging
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LineType.class);
	
	public abstract Stroke getStroke();
	public static LineType getLineType(String name)
	{
		if(name == null)
		{
			return SOLID_LINE;
		}
		
		try
		{
			return Enum.valueOf(LineType.class, name);
		}
		catch(IllegalArgumentException e)
		{
			log.warn("Unknown LineType {}", name);
			return SOLID_LINE;
		}
	}
}