package de.stsFanGruppe.templatebuilder.external.jtraingraph;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.jgoodies.forms.layout.*;
import de.stsFanGruppe.templatebuilder.gui.GUI;
import de.stsFanGruppe.tools.NullTester;

public class JTrainGraphZugregelImportGUI extends JDialog implements GUI
{
	public final int CANCEL_OPTION = 0;
	public final int APPROVE_OPTION = 1;
	
	private Callback callback;
	
	final JPanel contentPanel = new JPanel();
	JTextField pfadInput;
	JButton pfadButton;
	JLabel lblPfadZurDatei;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			new JTrainGraphZugregelImportGUI(null, (a) -> {});
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public JTrainGraphZugregelImportGUI(JFrame parent, Callback callback)
	{
		super(parent, "JTrainGraph-Import");
		setTitle("JTrainGraph-Zugdastellung-Import");
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
				new ColumnSpec[] {FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("center:default:grow"), FormSpecs.UNRELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"), FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,},
				new RowSpec[] {FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,}));
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
		pfadButton.addActionListener((ActionEvent arg0) -> dateiChooser(arg0));
		contentPanel.add(pfadButton, "6, 2, right, default");
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
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		JFileChooser fileChooser = new JFileChooser();
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
		fileChooser.setDialogTitle("Importieren...");
		fileChooser.setFileFilter(new FileNameExtensionFilter("Regel-Datei (*.xml)", "xml"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		if(!pfadInput.getText().trim().isEmpty())
		{
			File file = new File(pfadInput.getText());
			fileChooser.setCurrentDirectory(file);
		}
		
		if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
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
				close();
				break;
			case "OK":
				String pfad = pfadInput.getText();
				
				if(pfad.trim().isEmpty())
				{
					errorMessage("Pfad ist leer!");
					return;
				}
				
				callback.call(new DoneFuture(pfad));
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
		
		private DoneFuture()
		{
			this.success = false;
		}
		
		private DoneFuture(String pfad)
		{
			this.success = true;
			this.pfad = pfad;
		}
		
		public boolean success()
		{
			return success;
		}
		
		public String getPfad()
		{
			if(success)
			{
				return pfad;
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
