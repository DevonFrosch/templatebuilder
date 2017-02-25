package de.stsFanGruppe.tools;

import java.util.List;

public interface FirstLastList<E> extends List<E>
{
	public E first();
	
	public E last();
	
	public FirstLastList<E> clone();
	
	public E[] toArray();
}
