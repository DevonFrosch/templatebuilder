package de.stsFanGruppe.templatebuilder.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.stsFanGruppe.templatebuilder.editor.GUIType;
import de.stsFanGruppe.templatebuilder.types.Schachtelung;
import de.stsFanGruppe.templatebuilder.zug.Template;
import de.stsFanGruppe.tools.NullTester;

public class TemplateBuilderGUI implements GUI
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TemplateBuilderGUI.class);
	
	protected TemplateBuilderGUIController controller;
	private boolean initialized = false;
	
	protected JCheckBoxMenuItem mntmBildfahrplan;
	protected JCheckBoxMenuItem mntmTabellarischHin;
	protected JCheckBoxMenuItem mntmTabellarischRück;
	
	protected JFrame frmTemplatebauer;
	protected JTabbedPane tabbedPane;
	
	protected ButtonGroup radioGroupSchachtelung;
	protected JRadioButton radioSchachtelungKeine;
	protected JRadioButton radioSchachtelungMinuten;
	protected JRadioButton radioSchachtelungTemplate;
	protected JTextField inputSchachtelungMinuten;
	protected JComboBox<Template> inputSchachtelungTemplate;
	
	/**
	 * Create the application.
	 * 
	 * @param update
	 * @wbp.parser.entryPoint
	 */
	public TemplateBuilderGUI(TemplateBuilderGUIController controller, String update)
	{
		NullTester.test(controller);
		this.controller = controller;
		initialize(update);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String update)
	{
		assert controller != null;
		
		if(initialized)
		{
			throw new IllegalStateException("GUI bereits initialisiert!");
		}
		initialized = true;
		
		String title = "TemplateBauer";
		if(update != null)
		{
			title = "TemplateBauer (Update verfügbar)";
		}
		
		frmTemplatebauer = new JFrame();
		frmTemplatebauer.setTitle(title);
		frmTemplatebauer.setMinimumSize(new Dimension(450, 300));
		frmTemplatebauer.setBounds(100, 100, 1000, 600);
		// Fenster maximieren
		frmTemplatebauer.setExtendedState(frmTemplatebauer.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frmTemplatebauer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmTemplatebauer.setJMenuBar(menuBar);
		
		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);
		
		JMenuItem mntmNeu = new JMenuItem("Neu...");
		mntmNeu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		mntmNeu.setActionCommand("neu");
		mntmNeu.addActionListener((event) -> controller.menuAction(event));
		mnDatei.add(mntmNeu);
		
		JMenuItem mntmffnen = new JMenuItem("\u00D6ffnen...");
		mntmffnen.setEnabled(false);
		mntmffnen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		mntmffnen.setActionCommand("öffnen");
		mntmffnen.addActionListener((event) -> controller.menuAction(event));
		mnDatei.add(mntmffnen);
		
		mnDatei.add(new JSeparator());
		
		JMenuItem mntmSpeichern = new JMenuItem("Speichern");
		mntmSpeichern.setEnabled(false);
		mntmSpeichern.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		mntmSpeichern.setActionCommand("speichern");
		mntmSpeichern.addActionListener((event) -> controller.menuAction(event));
		mnDatei.add(mntmSpeichern);
		
		JMenuItem mntmSpeichernUnter = new JMenuItem("Speichern unter...");
		mntmSpeichernUnter.setEnabled(false);
		mntmSpeichernUnter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		mntmSpeichernUnter.setActionCommand("speichernUnter");
		mntmSpeichernUnter.addActionListener((event) -> controller.menuAction(event));
		mnDatei.add(mntmSpeichernUnter);
		
		mnDatei.add(new JSeparator());
		
		JMenu mnImport = new JMenu("Import");
		mnDatei.add(mnImport);
		
		JMenuItem mntmImportAusJtraingraph = new JMenuItem("Import aus JTrainGraph");
		mntmImportAusJtraingraph.setActionCommand("importFromJTG");
		mntmImportAusJtraingraph.addActionListener((event) -> controller.menuAction(event));
		mnImport.add(mntmImportAusJtraingraph);
		
		JMenuItem mntmRegelimportAusJtraingraph = new JMenuItem("Regelimport aus JTrainGraph");
		mntmRegelimportAusJtraingraph.setActionCommand("importRulesFromJTG");
		mntmRegelimportAusJtraingraph.addActionListener((event) -> controller.menuAction(event));
		mnImport.add(mntmRegelimportAusJtraingraph);
		
		JMenu mnExport = new JMenu("Export");
		mnDatei.add(mnExport);
		
		JMenuItem mntmExportNachJtraingraph = new JMenuItem("Export nach JTrainGraph");
		mntmExportNachJtraingraph.setActionCommand("exportToJTG");
		mntmExportNachJtraingraph.addActionListener((event) -> controller.menuAction(event));
		mnExport.add(mntmExportNachJtraingraph);
		
		mnDatei.add(new JSeparator());
		
		JMenuItem mntmSchliessen = new JMenuItem("Schlie\u00DFen");
		mntmSchliessen.setActionCommand("exit");
		mntmSchliessen.addActionListener((event) -> controller.menuAction(event));
		mntmSchliessen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
		mnDatei.add(mntmSchliessen);
		
		JMenu mnBearbeiten = new JMenu("Bearbeiten");
		menuBar.add(mnBearbeiten);
		
		JMenuItem mntmFahrten = new JCheckBoxMenuItem("Fahrten...");
		mntmFahrten.setActionCommand("bearbeiteFahrten");
		mntmFahrten.addActionListener((event) -> controller.menuAction(event));
		mnBearbeiten.add(mntmFahrten);
		
		mnBearbeiten.add(new JSeparator());
		
		JMenuItem mntmSucheZug = new JMenuItem("Suche Zug...");
		mntmSucheZug.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
		mntmSucheZug.setActionCommand("sucheZug");
		mntmSucheZug.addActionListener((event) -> controller.menuAction(event));
		mnBearbeiten.add(mntmSucheZug);
		
		JMenuItem mntmSucheTemplate = new JMenuItem("Suche Template...");
		mntmSucheTemplate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		mntmSucheTemplate.setActionCommand("sucheTemplate");
		mntmSucheTemplate.addActionListener((event) -> controller.menuAction(event));
		mnBearbeiten.add(mntmSucheTemplate);
		
		JMenu mnStrecken = new JMenu("Strecken");
		menuBar.add(mnStrecken);
		
		JMenuItem mntmStreckeEdit = new JMenuItem("Strecke bearbeiten");
		mntmStreckeEdit.setActionCommand("streckenEdit");
		mntmStreckeEdit.addActionListener((event) -> controller.menuAction(event));
		mnStrecken.add(mntmStreckeEdit);
		
		JMenu mnZge = new JMenu("Z\u00FCge");
		mnZge.setEnabled(false);
		menuBar.add(mnZge);
		
		JMenu mnAnsicht = new JMenu("Ansicht");
		menuBar.add(mnAnsicht);
		
		mntmBildfahrplan = new JCheckBoxMenuItem("Bildfahrplan");
		mntmBildfahrplan.setActionCommand("zeigeBildfahrplan");
		mntmBildfahrplan.addActionListener((event) -> controller.menuAction(event));
		mnAnsicht.add(mntmBildfahrplan);
		
		mntmTabellarischHin = new JCheckBoxMenuItem("Tabellarisch (hin)");
		mntmTabellarischHin.setActionCommand("zeigeTabEditorHin");
		mntmTabellarischHin.addActionListener((event) -> controller.menuAction(event));
		mnAnsicht.add(mntmTabellarischHin);
		
		mntmTabellarischRück = new JCheckBoxMenuItem("Tabellarisch (zurück)");
		mntmTabellarischRück.setActionCommand("zeigeTabEditorRück");
		mntmTabellarischRück.addActionListener((event) -> controller.menuAction(event));
		mnAnsicht.add(mntmTabellarischRück);
		
		mnAnsicht.add(new JSeparator());
		
		JMenuItem mntmZeigeMarkierte = new JMenuItem("Zeige markierte Züge");
		mntmZeigeMarkierte.setActionCommand("zeigeMarkierte");
		mntmZeigeMarkierte.addActionListener((event) -> controller.menuAction(event));
		mnAnsicht.add(mntmZeigeMarkierte);
		
		JMenu mnEinstellungen = new JMenu("Einstellungen");
		menuBar.add(mnEinstellungen);
		
		JMenuItem mntmOptionen = new JMenuItem("Optionen");
		mntmOptionen.setActionCommand("options");
		mntmOptionen.addActionListener((event) -> controller.menuAction(event));
		mnEinstellungen.add(mntmOptionen);
		
		JMenu mnHilfe = new JMenu("?");
		menuBar.add(mnHilfe);
		
		JMenuItem mntmHilfe = new JMenuItem("Hilfe");
		mntmHilfe.setEnabled(false);
		mntmHilfe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mnHilfe.add(mntmHilfe);
		
		JMenuItem mntmUeber = new JMenuItem("\u00DCber...");
		mntmUeber.setActionCommand("about");
		mntmUeber.addActionListener((event) -> controller.menuAction(event));
		mnHilfe.add(mntmUeber);
		
		if(controller.dev)
		{
			// Übersicht über bestehende Locks
			JMenuItem mntmLocks = new JMenuItem("GUI-Locks");
			mntmLocks.setActionCommand("locks");
			mntmLocks.addActionListener((event) -> controller.menuAction(event));
			mnHilfe.add(mntmLocks);
		}
		
		JPanel panel = new JPanel();
		frmTemplatebauer.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		{
			JToolBar toolBar = new JToolBar();
			toolBar.setFloatable(false);
			panel.add(toolBar, BorderLayout.NORTH);
			
			ImageIcon jtgImportIcon = new ImageIcon(TemplateBuilderGUI.class.getResource("/Import_JTG.png"));
			jtgImportIcon = new ImageIcon(jtgImportIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
			JButton jtgImportButton = new JButton(jtgImportIcon);
			jtgImportButton.setActionCommand("importFromJTG");
			jtgImportButton.addActionListener((event) -> controller.menuAction(event));
			toolBar.add(jtgImportButton);
			
			toolBar.addSeparator();
			
			ImageIcon bildfahrplanIcon = new ImageIcon(TemplateBuilderGUI.class.getResource("/Bildfahrplan.png"));
			bildfahrplanIcon = new ImageIcon(bildfahrplanIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
			JButton bildfahrplanButton = new JButton(bildfahrplanIcon);
			bildfahrplanButton.setActionCommand("zeigeBildfahrplan");
			bildfahrplanButton.addActionListener((event) -> controller.menuAction(event));
			toolBar.add(bildfahrplanButton);
			
			ImageIcon tabelleHinIcon = new ImageIcon(TemplateBuilderGUI.class.getResource("/Tabelle_hin.png"));
			tabelleHinIcon = new ImageIcon(tabelleHinIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
			JButton tabelleHinButton = new JButton(tabelleHinIcon);
			tabelleHinButton.setActionCommand("zeigeTabEditorHin");
			tabelleHinButton.addActionListener((event) -> controller.menuAction(event));
			toolBar.add(tabelleHinButton);
			
			ImageIcon tabelleRueckIcon = new ImageIcon(TemplateBuilderGUI.class.getResource("/Tabelle_rueck.png"));
			tabelleRueckIcon = new ImageIcon(tabelleRueckIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
			JButton tabelleRueckButton = new JButton(tabelleRueckIcon);
			tabelleRueckButton.setActionCommand("zeigeTabEditorRück");
			tabelleRueckButton.addActionListener((event) -> controller.menuAction(event));
			toolBar.add(tabelleRueckButton);
			
			toolBar.addSeparator();
			
			radioGroupSchachtelung = new ButtonGroup();
			radioSchachtelungKeine = new JRadioButton("Aus");
			radioSchachtelungKeine.setEnabled(false);
			radioSchachtelungKeine.setSelected(true);
			radioSchachtelungKeine.setActionCommand(Schachtelung.KEINE.toString());
			radioSchachtelungKeine.addActionListener(event -> updateOnSchachtelungClick(event));
			radioGroupSchachtelung.add(radioSchachtelungKeine);
			toolBar.add(radioSchachtelungKeine);
			
			radioSchachtelungMinuten = new JRadioButton("Minuten");
			radioSchachtelungMinuten.setEnabled(false);
			radioSchachtelungMinuten.setActionCommand(Schachtelung.MINUTEN.toString());
			radioSchachtelungMinuten.addActionListener(event -> updateOnSchachtelungClick(event));
			radioGroupSchachtelung.add(radioSchachtelungMinuten);
			toolBar.add(radioSchachtelungMinuten);
			
			inputSchachtelungMinuten = new JTextField();
			inputSchachtelungMinuten.setEnabled(false);
			inputSchachtelungMinuten.addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent e) {}
				public void focusLost(FocusEvent e) {
					controller.schachtelungMinutenChanged();
				}
			});
			inputSchachtelungMinuten.getDocument().addDocumentListener(new DocumentListener()
			{
				@Override
				public void changedUpdate(DocumentEvent e) {
					
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					if(!inputSchachtelungMinuten.getText().equals("")) {
						if(Integer.parseInt(inputSchachtelungMinuten.getText()) >= 10) {
							controller.schachtelungMinutenChanged();
						}
					}
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					if(!inputSchachtelungMinuten.getText().equals("")) {
						if(Integer.parseInt(inputSchachtelungMinuten.getText()) >= 10) {
							controller.schachtelungMinutenChanged();
						}
					}
				}
			});
			inputSchachtelungMinuten.setMaximumSize(new Dimension(50, (int) inputSchachtelungMinuten.getPreferredSize().getHeight()));
			inputSchachtelungMinuten.setColumns(10);
			toolBar.add(inputSchachtelungMinuten);
			
			radioSchachtelungTemplate = new JRadioButton("Template");
			radioSchachtelungTemplate.setEnabled(false);
			radioSchachtelungTemplate.setActionCommand(Schachtelung.TEMPLATE.toString());
			radioSchachtelungTemplate.addActionListener(event -> updateOnSchachtelungClick(event));
			radioGroupSchachtelung.add(radioSchachtelungTemplate);
			toolBar.add(radioSchachtelungTemplate);
			
			inputSchachtelungTemplate = new JComboBox<Template>();
			inputSchachtelungTemplate.setEnabled(false);
			inputSchachtelungTemplate.addActionListener(event -> controller.schachtelungTemplateChanged());
			inputSchachtelungTemplate.setMaximumSize(new Dimension(250, 20));
			toolBar.add(inputSchachtelungTemplate);
		}
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		panel.add(tabbedPane);
		tabbedPane.addChangeListener((event) -> updateAnsichtAuswahl());
		
		frmTemplatebauer.setVisible(true);
	}
	
	public JFrame getFrame()
	{
		return frmTemplatebauer;
	}
	
	public void updateAnsichtAuswahl()
	{
		GUIType guiType = controller.getGUIType();
		mntmBildfahrplan.setSelected(guiType == GUIType.BILDFAHRPLAN);
		mntmTabellarischHin.setSelected(guiType == GUIType.TABELLE_AUFSTEIGEND);
		mntmTabellarischRück.setSelected(guiType == GUIType.TABELLE_ABSTEIGEND);
		
		setSchachtelung(guiType != null, controller.getSchachtelung(), controller.getSchachtelungMinuten(), controller.getSchachtelungTemplate());
		updateSchachtelungTemplates(guiType != null);
	}
	
	private void updateSchachtelungTemplates(boolean isEditable)
	{
		SortedSet<Template> sorted = new TreeSet<>(controller.getTemplates());
		DefaultComboBoxModel<Template> model = new DefaultComboBoxModel<>(sorted.toArray(new Template[sorted.size()]));
		inputSchachtelungTemplate.setModel(model);
		
		radioSchachtelungTemplate.setEnabled(isEditable && sorted.size() > 1);
		if(sorted.size() <= 1 && radioSchachtelungTemplate.isSelected())
		{
			setSelectedSchachtelung(Schachtelung.KEINE);
		}
	}
	
	public Schachtelung getSchachtelungTyp()
	{
		return Schachtelung.valueOf(radioGroupSchachtelung.getSelection().getActionCommand());
	}
	
	public String getSchachtelungMinuten()
	{
		return inputSchachtelungMinuten.getText();
	}
	
	public Template getSchachtelungTemplate()
	{
		return (Template) inputSchachtelungTemplate.getSelectedItem();
	}
	
	public void setSchachtelung(boolean isEditable, Schachtelung typ, int minuten, Template template)
	{
		radioSchachtelungKeine.setEnabled(isEditable);
		radioSchachtelungMinuten.setEnabled(isEditable);
		radioSchachtelungTemplate.setEnabled(isEditable);
		setSelectedSchachtelung(typ);
		
		switch(typ)
		{
			case KEINE:
				return;
			case MINUTEN:
				setSchachtelungMinuten(minuten);
				return;
			case TEMPLATE:
				setSchachtelungTemplate(template);
				return;
			default:
				log.warn("setSchachtelung(): Schachtelung {} nicht erkannt", typ);
				return;
		}
	}
	
	private void updateOnSchachtelungClick(ActionEvent e)
	{
		setSelectedSchachtelung(Schachtelung.valueOf(e.getActionCommand()));
	}
	
	private void setSelectedSchachtelung(Schachtelung typ)
	{
		controller.schachtelungChanged(typ);
		
		radioSchachtelungKeine.setSelected(typ == Schachtelung.KEINE);
		
		radioSchachtelungMinuten.setSelected(typ == Schachtelung.MINUTEN);
		inputSchachtelungMinuten.setEnabled(typ == Schachtelung.MINUTEN);
		
		radioSchachtelungTemplate.setSelected(typ == Schachtelung.TEMPLATE);
		inputSchachtelungTemplate.setEnabled(typ == Schachtelung.TEMPLATE);
	}
	
	public void setSchachtelungMinuten(int minuten)
	{
		inputSchachtelungMinuten.setText(String.valueOf(minuten));
	}
	
	public void setSchachtelungTemplate(Template template)
	{
		inputSchachtelungTemplate.setSelectedItem(template);
	}
	
	public void errorMessage(String text, String titel)
	{
		JComponent pane = this.createSelectableTextPane(text);
		JOptionPane.showMessageDialog(frmTemplatebauer, pane, titel, JOptionPane.ERROR_MESSAGE);
	}
	
	public void warningMessage(String text, String titel)
	{
		JComponent pane = this.createSelectableTextPane(text);
		JOptionPane.showMessageDialog(frmTemplatebauer, pane, titel, JOptionPane.WARNING_MESSAGE);
	}
	
	public void infoMessage(String text, String titel)
	{
		JComponent pane = this.createSelectableTextPane(text);
		JOptionPane.showMessageDialog(frmTemplatebauer, pane, titel, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public String inputMessage(String text, String initialSelectionValue)
	{
		JComponent pane = this.createSelectableTextPane(text);
		return JOptionPane.showInputDialog(frmTemplatebauer, pane, initialSelectionValue);
	}
}
