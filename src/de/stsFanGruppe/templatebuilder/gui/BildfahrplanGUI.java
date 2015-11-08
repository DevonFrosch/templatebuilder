package de.stsFanGruppe.templatebuilder.gui;

/**
 *
 * @author DevonFrosch
 */
public class BildfahrplanGUI extends javax.swing.JFrame
{
	/**
	 * Creates new form BildfahrplanGUI
	 */
	public BildfahrplanGUI()
	{
		initComponents();
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents()
	{
		java.awt.GridBagConstraints gridBagConstraints;
		
		jPanel1 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jPanel2 = new javax.swing.JPanel();
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("BildfahrplanGUI");
		setLocationByPlatform(true);
		setPreferredSize(new java.awt.Dimension(800, 500));
		getContentPane().setLayout(new java.awt.GridBagLayout());
		
		jPanel1.setBackground(new java.awt.Color(0, 153, 255));
		jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		jPanel1.setMaximumSize(new java.awt.Dimension(32767, 100));
		jPanel1.setMinimumSize(new java.awt.Dimension(500, 100));
		jPanel1.setName(""); // NOI18N
		
		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(
			jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGap(0, 736, Short.MAX_VALUE)
		);
		jPanel1Layout.setVerticalGroup(
			jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGap(0, 100, Short.MAX_VALUE)
		);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		getContentPane().add(jPanel1, gridBagConstraints);
		
		jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		jPanel2.setBackground(new java.awt.Color(255, 255, 255));
		jPanel2.setMinimumSize(new java.awt.Dimension(100, 1000));
		jPanel2.setName(""); // NOI18N
		
		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(
			jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGap(0, 717, Short.MAX_VALUE)
		);
		jPanel2Layout.setVerticalGroup(
			jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGap(0, 1000, Short.MAX_VALUE)
		);
		
		jScrollPane1.setViewportView(jPanel2);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		getContentPane().add(jScrollPane1, gridBagConstraints);
		
		pack();
	}// </editor-fold>//GEN-END:initComponents
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[])
	{
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try
		{
			javax.swing.UIManager.setLookAndFeel( javax.swing.UIManager.getSystemLookAndFeelClassName() );
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
		{
			java.util.logging.Logger.getLogger(BildfahrplanGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>
		
		//</editor-fold>
		
		/* Create and display the form */
		java.awt.EventQueue.invokeLater(() -> {
			BildfahrplanGUI bfp = new BildfahrplanGUI();
			bfp.setVisible(true);
			
			bfp.testPlan();
		});
	}
	
	private void testPlan()
	{
		BildfahrplanZeichner z = new BildfahrplanZeichner(jPanel2);
		
	}
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JScrollPane jScrollPane1;
	// End of variables declaration//GEN-END:variables
}
