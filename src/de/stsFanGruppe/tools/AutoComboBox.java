package de.stsFanGruppe.tools;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class AutoComboBox extends JComboBox<String>
{
	Vector<String> items = new Vector<>();
	
	public AutoComboBox()
	{
		setModel(new DefaultComboBoxModel<String>(items));
		setSelectedIndex(-1);
		setEditable(true);
		JTextField text = (JTextField) this.getEditor().getEditorComponent();
		text.setFocusable(true);
		text.setText("");
		text.addKeyListener(new ComboListener());
	}
	
	public void setItems(String[] items)
	{
		synchronized(this.items)
		{
			this.items.clear();
			for(String item : items)
			{
				this.items.addElement(item);
			}
		}
		super.setModel(new DefaultComboBoxModel<String>(items));
	}
	
	public void setItems(Vector<String> items)
	{
		synchronized(this.items)
		{
			this.items.clear();
			this.items.addAll(items);
		}
		super.setModel(new DefaultComboBoxModel<String>(items));
	}
	
	public String getSelectedItem()
	{
		return getItemAt(getSelectedIndex());
	}
	
	public class ComboListener extends KeyAdapter
	{
		public void keyReleased(KeyEvent key)
		{
			String text = ((JTextField) key.getSource()).getText();
			
			setModel(new DefaultComboBoxModel<String>(getFilteredList(text)));
			setSelectedIndex(-1);
			((JTextField) getEditor().getEditorComponent()).setText(text);
			showPopup();
		}
		
		public Vector<String> getFilteredList(String search)
		{
			Vector<String> v = new Vector<>();
			
			synchronized(items)
			{
				items.stream().filter(s -> s.toLowerCase().startsWith(search.toLowerCase())).forEach(v::add);
			}
			
			return v;
		}
	}
}
