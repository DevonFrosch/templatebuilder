package de.stsFanGruppe.templatebuilder.external;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.*;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.templatebuilder.strecken.Bildfahrplanstrecke;
import de.stsFanGruppe.tools.NullTester;

public class JTrainGraphExportGUI extends JDialog implements GUI
{
	public final int CANCEL_OPTION = 0;
	public final int APPROVE_OPTION = 1;
	
	private Callback callback;
	
	final JPanel contentPanel = new JPanel();
	JTextField pfadInput;
	JButton pfadButton;
	JTextField linieInput;
	JButton linieButton;
	JLabel lblPfadZurDatei;
	final JLabel lblBildfahrplanstrecke = new JLabel("Bildfahrplanstrecke");
	JCheckBox chckbxZgeExportieren;
	
	JFileChooser fileChooser = new JFileChooser();
	private JCheckBox chckbxDs100;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			JTrainGraphExportGUI dialog = new JTrainGraphExportGUI(null, (a) -> {});
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public JTrainGraphExportGUI(JFrame parent, Callback callback)
	{
		super(parent, "JTrainGraph-Export");
		this.callback = callback;
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout(0, 0));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.PARAGRAPH_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,}));
		{
			lblPfadZurDatei = new JLabel("Pfad zur Datei");
			contentPanel.add(lblPfadZurDatei, "2, 2, default, fill");
		}
		{
		}
		{
			pfadInput = new JTextField();
			contentPanel.add(pfadInput, "4, 2, fill, default");
			pfadInput.setColumns(10);
		}
		pfadButton = new JButton("...");
		pfadButton.addActionListener((ActionEvent arg0) -> dateiChooser(arg0));
		contentPanel.add(pfadButton, "6, 2, right, default");
		{
			chckbxDs100 = new JCheckBox("DS100 verwenden wo m\u00F6glich");
			contentPanel.add(chckbxDs100, "2, 4");
		}
		{
			chckbxZgeExportieren = new JCheckBox("Z\u00FCge exportieren");
			chckbxZgeExportieren.setSelected(true);
			contentPanel.add(chckbxZgeExportieren, "2, 6");
		}
		contentPanel.add(lblBildfahrplanstrecke, "2, 8, default, fill");
		{
			linieInput = new JTextField();
			contentPanel.add(linieInput, "4, 8, fill, default");
			linieInput.setColumns(10);
			// TODO Testinput entfernen wenn Bildfahrplanstreckenmanagement fertig ist
			linieInput.setText("default");
			linieInput.setEnabled(false);
		}
		{
			linieButton = new JButton("...");
			contentPanel.add(linieButton, "6, 8, right, default");
			linieButton.setEnabled(false);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener((ActionEvent arg0) -> actionButton(arg0));
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Abbrechen");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener((ActionEvent arg0) -> actionButton(arg0));
				buttonPane.add(cancelButton);
			}
		}
		setVisible(true);
	}
	
	// ActionHandler
	private void dateiChooser(ActionEvent e)
	{
		fileChooser.setDialogTitle("Exportieren...");
		if(!pfadInput.getText().trim().isEmpty())
		{
			File file = new File(pfadInput.getText());
			fileChooser.setCurrentDirectory(file);
		}
		
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			pfadInput.setText(fileChooser.getSelectedFile().getPath());
		}
	}
	
	private void actionButton(ActionEvent e)
	{
		NullTester.test(callback);
		
		switch(e.getActionCommand())
		{
			case "Cancel":
				callback.call(new DoneFuture());
				break;
			case "OK":
				String pfad = pfadInput.getText();
				String linie = linieInput.getText();
				
				if(pfad.trim().isEmpty())
				{
					errorMessage("Pfad ist leer!");
					return;
				}
				if(linie.trim().isEmpty())
				{
					errorMessage("Bildfahrplanstrecke ist leer!");
					return;
				}
				
				callback.call(new DoneFuture(pfad, new Bildfahrplanstrecke(""), chckbxZgeExportieren.isSelected(), this.chckbxDs100.isSelected()));
				break;
			default:
		}
		dispose();
        setVisible(false);
	}
	
	public interface Callback
	{
		public void call(DoneFuture future);
	}
	public class DoneFuture
	{
		private boolean success;
		
		private String pfad = null;
		private Bildfahrplanstrecke bfpStrecke = null;
		private boolean exportZuege = false;
		private boolean useDS100 = false;
		
		private DoneFuture()
		{
			this.success = false;
		}
		private DoneFuture(String pfad, Bildfahrplanstrecke bfpStrecke, boolean exportZuege, boolean useDS100)
		{
			this.success = true;
			this.pfad = pfad;
			this.bfpStrecke = bfpStrecke;
			this.exportZuege = exportZuege;
			this.useDS100 = useDS100;
		}
		
		public boolean success()
		{
			return success;
		}
		public boolean exportZuege()
		{
			return exportZuege;
		}
		public boolean useDS100()
		{
			return useDS100;
		}
		public String getPfad()
		{
			if(success)
			{
				return pfad;
			}
			return null;
		}
		public Bildfahrplanstrecke getBildfahrplanstrecke()
		{
			if(success)
			{
				return bfpStrecke;
			}
			return null;
		}
	}
	
	public void errorMessage(String text, String titel)
	{
		JOptionPane.showMessageDialog(contentPanel, text, titel, JOptionPane.ERROR_MESSAGE);
	}
	public void warningMessage(String text, String titel)
	{
		JOptionPane.showMessageDialog(contentPanel, text, titel, JOptionPane.WARNING_MESSAGE);
	}
	public void infoMessage(String text, String titel)
	{
		JOptionPane.showMessageDialog(contentPanel, text, titel, JOptionPane.INFORMATION_MESSAGE);
	}
}
