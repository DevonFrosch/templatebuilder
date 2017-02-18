package de.stsFanGruppe.templatebuilder.config.fahrtdarstellung.linetype;

import java.awt.BasicStroke;
import java.awt.Stroke;

public enum LineType
{
	SOLID_LINE {
		public Stroke getStroke() {
			return new BasicStroke(3);
		}
	},
	DOTTED_LINE {
		public Stroke getStroke() {
			return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, new float[]{10, 10}, 1);
		}
	},
	DASEHED_LINE {
		public Stroke getStroke() {
			return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, new float[]{30, 10}, 1);
		}
	},
	SHORT_LONG_LINE {
		public Stroke getStroke() {
			return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, new float[]{10, 10, 30, 10}, 1);
		}
	},
	SHORT_SHORT_LONG_LINE {
		public Stroke getStroke() {
			return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, new float[]{10, 10, 10, 10, 30, 10}, 1);
		}
	},
	SHORT_LONG_LONG_LINE {
		public Stroke getStroke() {
			return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, new float[]{10, 10, 30, 10, 30, 10}, 1);
		}
	};
	
	public abstract Stroke getStroke();
	
	public static LineType valueOf(String name, LineType defaultValue)
	{
		if(name == null)
		{
			return defaultValue;
		}
		
		try
		{
			return Enum.valueOf(LineType.class, name);
		}
		catch(IllegalArgumentException e)
		{}
		return defaultValue;
	}
}