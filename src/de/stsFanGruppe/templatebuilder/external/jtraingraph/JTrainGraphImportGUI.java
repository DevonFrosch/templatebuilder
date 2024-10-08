package de.stsFanGruppe.templatebuilder.external.jtraingraph;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.jgoodies.forms.layout.*;
import de.stsFanGruppe.templatebuilder.config.GeneralConfig;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.templatebuilder.zug.Linie;
import de.stsFanGruppe.tools.NullTester;

public class JTrainGraphImportGUI extends JDialog implements GUI
{
	public final int CANCEL_OPTION = 0;
	public final int APPROVE_OPTION = 1;
	
	protected Callback callback;
	protected GeneralConfig config = null;
	
	final JPanel contentPanel = new JPanel();
	JTextField pfadInput;
	JButton pfadButton;
	JTextField linieInput;
	JButton linieButton;
	JLabel lblPfadZurDatei;
	final JLabel lblLinie = new JLabel("Linie");
	JCheckBox chckbxZgeImportieren;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			new JTrainGraphImportGUI(null, new GeneralConfig(), (a) -> {});
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public JTrainGraphImportGUI(JFrame parent, GeneralConfig config, Callback callback)
	{
		super(parent, "JTrainGraph-Import");
		this.config = config;
		this.callback = callback;
		
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				close();
			}
		});
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout(0, 0));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(
				new ColumnSpec[] {com.jgoodies.forms.layout.FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("center:default:grow"),
						com.jgoodies.forms.layout.FormSpecs.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
						com.jgoodies.forms.layout.FormSpecs.RELATED_GAP_COLSPEC, com.jgoodies.forms.layout.FormSpecs.DEFAULT_COLSPEC,
						com.jgoodies.forms.layout.FormSpecs.RELATED_GAP_COLSPEC,},
				new RowSpec[] {com.jgoodies.forms.layout.FormSpecs.LINE_GAP_ROWSPEC, com.jgoodies.forms.layout.FormSpecs.DEFAULT_ROWSPEC,
						com.jgoodies.forms.layout.FormSpecs.PARAGRAPH_GAP_ROWSPEC, com.jgoodies.forms.layout.FormSpecs.DEFAULT_ROWSPEC,
						com.jgoodies.forms.layout.FormSpecs.RELATED_GAP_ROWSPEC, com.jgoodies.forms.layout.FormSpecs.DEFAULT_ROWSPEC,
						com.jgoodies.forms.layout.FormSpecs.RELATED_GAP_ROWSPEC, com.jgoodies.forms.layout.FormSpecs.DEFAULT_ROWSPEC,}));
		{
			lblPfadZurDatei = new JLabel("Pfad zur Datei");
			contentPanel.add(lblPfadZurDatei, "2, 2, fill, fill");
		}
		{}
		{
			pfadInput = new JTextField();
			contentPanel.add(pfadInput, "4, 2, fill, default");
			pfadInput.setColumns(10);
		}
		pfadButton = new JButton("...");
		pfadButton.setEnabled(false);
		pfadButton.addActionListener((ActionEvent arg0) -> dateiChooser());
		contentPanel.add(pfadButton, "6, 2, right, default");
		{
			chckbxZgeImportieren = new JCheckBox("Z\u00FCge importieren");
			chckbxZgeImportieren.setSelected(true);
			contentPanel.add(chckbxZgeImportieren, "2, 4, left, default");
		}
		contentPanel.add(lblLinie, "2, 6, fill, fill");
		{
			linieInput = new JTextField();
			contentPanel.add(linieInput, "4, 6, fill, default");
			linieInput.setColumns(10);
			// TODO Testinput entfernen wenn Linienmanagement fertig ist
			linieInput.setText("1");
			linieInput.setEnabled(false);
		}
		{
			linieButton = new JButton("...");
			contentPanel.add(linieButton, "6, 6, right, default");
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
		
		// TODO: Entfernen wenn wirklich Optionen auswählbar sind
		dateiChooser();
	}
	
	// ActionHandler
	private void dateiChooser()
	{
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		JFileChooser fileChooser = new JFileChooser(config.getLastOpenFilePath());
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
		fileChooser.setDialogTitle("Importieren...");
		fileChooser.setFileFilter(new FileNameExtensionFilter("Bildfahrpläne (*.fpl, *.xml)", "fpl", "xml"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			String filePath = fileChooser.getSelectedFile().getPath();
			pfadInput.setText(filePath);
			config.setLastOpenFilePath(filePath);
			
			// TODO: Entfernen wenn wirklich Optionen auswählbar sind
			start();
			dispose();
			setVisible(false);
		}
	}
	private void start()
	{
		String pfad = pfadInput.getText();
		String linie = linieInput.getText();
		
		if(pfad.trim().isEmpty())
		{
			errorMessage("Pfad ist leer!");
			return;
		}
		
		if(chckbxZgeImportieren.isSelected())
		{
			if(linie.trim().isEmpty())
			{
				errorMessage("Linie ist leer!");
				return;
			}
			callback.call(new DoneFuture(pfad, new Linie(linie)));
		}
		else
		{
			callback.call(new DoneFuture(pfad));
		}
	}
	
	private void actionButton(ActionEvent e)
	{
		NullTester.test(callback);
		
		switch(e.getActionCommand())
		{
			case "Cancel":
				close();
				break;
			case "OK":
				start();
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
		private Linie linie = null;
		private boolean importZuege = false;
		
		private DoneFuture()
		{
			this.success = false;
		}
		
		private DoneFuture(String pfad)
		{
			this.success = true;
			this.pfad = pfad;
			this.importZuege = false;
		}
		
		private DoneFuture(String pfad, Linie linie)
		{
			this.success = true;
			this.pfad = pfad;
			this.linie = linie;
			this.importZuege = true;
		}
		
		public boolean success()
		{
			return success;
		}
		
		public boolean importZuege()
		{
			return importZuege;
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
	
	public void close()
	{
		dispose();
		setVisible(false);
		callback.call(new DoneFuture());
	}
	
	public void errorMessage(String text, String titel)
	{
		JComponent pane = this.createSelectableTextPane(text);
		JOptionPane.showMessageDialog(contentPanel, pane, titel, JOptionPane.ERROR_MESSAGE);
	}
	
	public void warningMessage(String text, String titel)
	{
		JComponent pane = this.createSelectableTextPane(text);
		JOptionPane.showMessageDialog(contentPanel, pane, titel, JOptionPane.WARNING_MESSAGE);
	}
	
	public void infoMessage(String text, String titel)
	{
		JComponent pane = this.createSelectableTextPane(text);
		JOptionPane.showMessageDialog(contentPanel, pane, titel, JOptionPane.INFORMATION_MESSAGE);
	}
}
