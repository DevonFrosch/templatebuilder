package de.stsFanGruppe.templatebuilder.gui.external;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.*;
import com.jgoodies.forms.factories.FormFactory;
import de.stsFanGruppe.templatebuilder.zug.Linie;
import de.stsFanGruppe.tools.NullTester;

public class JTrainGraphImportGUI extends JDialog
{
	public final int CANCEL_OPTION = 0;
	public final int APPROVE_OPTION = 1;
	
	private Callback callback;
	
	private Object buttonClickEvent = new Object();
	
	private final JPanel contentPanel = new JPanel();
	private JTextField pfadInput;
	private JButton pfadButton;
	private JTextField linieInput;
	private JButton linieButton;
	private JLabel lblPfadZurDatei;
	private final JLabel lblLinie = new JLabel("Linie");
	private JCheckBox chckbxStreckenImportieren;
	private JCheckBox chckbxZgeImportieren;
	
	private JFileChooser fileChooser = new JFileChooser();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			JTrainGraphImportGUI dialog = new JTrainGraphImportGUI((a) -> {});
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public JTrainGraphImportGUI(Callback callback)
	{
		this.callback = callback;
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout(0, 0));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:default:grow"),
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.PARAGRAPH_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			lblPfadZurDatei = new JLabel("Pfad zur Datei");
			contentPanel.add(lblPfadZurDatei, "2, 2, fill, fill");
		}
		{
		}
		{
			pfadInput = new JTextField();
			contentPanel.add(pfadInput, "4, 2, fill, default");
			pfadInput.setColumns(10);
			//pfadInput.setText("D:\\Dateien\\Informatik\\Eigene_Erzeugnisse\\Java\\templatebuilder");
		}
		pfadButton = new JButton("...");
		pfadButton.addActionListener((ActionEvent arg0) -> dateiChooser(arg0));
		contentPanel.add(pfadButton, "6, 2, right, default");
		{
			chckbxStreckenImportieren = new JCheckBox("Strecken importieren");
			chckbxStreckenImportieren.setEnabled(false);
			chckbxStreckenImportieren.setSelected(true);
			contentPanel.add(chckbxStreckenImportieren, "2, 4, left, default");
		}
		{
			chckbxZgeImportieren = new JCheckBox("Z\u00FCge importieren");
			chckbxZgeImportieren.setEnabled(false);
			chckbxZgeImportieren.setSelected(true);
			contentPanel.add(chckbxZgeImportieren, "2, 6, left, default");
		}
		contentPanel.add(lblLinie, "2, 8, fill, fill");
		{
			linieInput = new JTextField();
			contentPanel.add(linieInput, "4, 8, fill, default");
			linieInput.setColumns(10);
		}
		{
			linieButton = new JButton("...");
			contentPanel.add(linieButton, "6, 8, right, default");
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
				JButton cancelButton = new JButton("Cancel");
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
		fileChooser.setDialogTitle("Importieren...");
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
				if(linie.trim().isEmpty() && chckbxZgeImportieren.isSelected())
				{
					errorMessage("Linie ist leer!");
					return;
				}
				
				callback.call(new DoneFuture(pfad, new Linie(linie)));
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
		private boolean importStrecke = true;
		private boolean importZuege = true;
		
		private String pfad;
		private Linie linie;

		private DoneFuture()
		{
			this.success = false;
			this.pfad = null;
			this.linie = null;
		}
		private DoneFuture(String pfad, Linie linie)
		{
			this.success = true;
			this.pfad = pfad;
			this.linie = linie;
		}
		
		public boolean success()
		{
			return success;
		}
		public boolean importStrecke()
		{
			return success && importStrecke;
		}
		public boolean importZuege()
		{
			return success && importZuege;
		}
		public String getPfad()
		{
			if(success)
			{
				return pfad;
			}
			return null;
		}
		public Linie getLinie()
		{
			if(success)
			{
				return linie;
			}
			return null;
		}
	}
	
	public void errorMessage(String text)
	{
		JOptionPane.showMessageDialog(null, text, "Fehler", JOptionPane.ERROR_MESSAGE);
	}
	
	private static void log(String text)
	{
		System.out.println("JTrainGraphImportGUI: "+text);
	}
}