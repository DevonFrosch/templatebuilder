package de.stsFanGruppe.templatebuilder.fahrtenFarbe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import de.stsFanGruppe.tools.PreferenceHandler;

public class FahrtenFarbeConfig {

	private static PreferenceHandler prefs;
	private static FahrtenFarbeSettingsGUI gui;
	
	//Farbeinstellungen
	public static final String CONFIG_LINIEN_FARBE = "bildfahrplan/darstellung/linie/farben";
	public static final String CONFIG_LINIEN_STAERKE  = "bildfahrplan/darstellung/linie/staerke";
	public static final String CONFIG_LINIEN_ART = "bildfahrplan/darstellung/linie/art";
	
	public static final float[] DURCHGEZOGENE_LINIE = {0};
	public static final float[] GEPUNKTE_LINIE = {10, 10};
	public static final float[] GESTRICHELTE_LINIE = {30, 10};
	public static final float[] KURZ_LANG_LINIE = {10, 10, 30, 10};
	public static final float[] KURZ_KURZ_LANG_LINIE = {10, 10, 10, 10, 30, 10};
	public static final float[] KURZ_LANG_LANG_LINIE = {10, 10, 30, 10, 30, 10};
	
	private static final Color DEFAULT_LINIEN_FARBE = Color.BLACK;
	private static final int DEFAULT_LINIEN_STAERKE = 1;
	private static final float[] DEFAULT_LINIEN_ART = DURCHGEZOGENE_LINIE;
	
	//Linienarten
		public static enum LineType {

	        SOLID_LINE {
	            @Override
	            public Stroke getStroke() {
	                return new BasicStroke(3);
	            }
	        },
	        DOTTED_LINE {
	            @Override
	            public Stroke getStroke() {
	                return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, GEPUNKTE_LINIE, 1);
	            }
	        },
	        DASEHED_LINE {
	            @Override
	            public Stroke getStroke() {
	                return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, GESTRICHELTE_LINIE, 1);
	            }
	        },
			SHORT_LONG_LINE {
	            @Override
	            public Stroke getStroke() {
	                return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, KURZ_LANG_LINIE, 1);
	            }
	        },
	        SHORT_SHORT_LONG_LINE {
	            @Override
	            public Stroke getStroke() {
	                return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, KURZ_KURZ_LANG_LINIE, 1);
	            }
	        },
	        SHORT_LONG_LONG_LINE {
	            @Override
	            public Stroke getStroke() {
	                return new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, KURZ_LANG_LANG_LINIE, 1);
	            }
	        };
	        public abstract Stroke getStroke();
	    }
	
	//Standardwerte fürs Zeichnen (NICHT ÄNDERN!)
//	private static final int x1 = 10;
//	private static final int x2 = gui.comboBoxLinienArt.getWidth() - 10;
//	private static final int y = gui.comboBoxLinienArt.getHeight()/2;
	
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

	public static float getDefaultLinienArt(int i) {
		return DEFAULT_LINIEN_ART[i];
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

	public class LineRenderer extends JPanel implements ListCellRenderer {
        private LineType value;

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof LineType) {
                setLineType((LineType) value);
            } else {
                setLineType(null);
            }
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            if (value != null) {
                g2d.setStroke(value.getStroke());
                g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
            }

        }

        private void setLineType(LineType value) {
            this.value = value;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(50, 20);
        }

    }
	protected Component getComboBoxLineStyle(){
		final JComboBox comboBox = new JComboBox(LineType.values());
	    comboBox.setRenderer(new LineRenderer());
	    comboBox.setSelectedItem(null);
	    comboBox.addActionListener(new ActionListener() {
	
	        public void actionPerformed(ActionEvent e) {
	            SwingUtilities.invokeLater(new Runnable() {
	
	                @Override
	                public void run() {
	                    JOptionPane.showMessageDialog(comboBox, "You have selected " + comboBox.getSelectedItem());
	                }
	            });
	        }
	    });
		return comboBox;
	}
//	protected void paintComponent( Graphics g )
//	{
//		Graphics2D g2 = (Graphics2D) g;
//		g2.setStroke(new BasicStroke(DEFAULT_LINIEN_ART[0]));
//		g2.drawLine( x1, y, x2, y );
//	}
}
