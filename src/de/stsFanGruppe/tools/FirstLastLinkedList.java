package de.stsFanGruppe.tools;

import java.util.Collection;
import java.util.LinkedList;

public class FirstLastLinkedList<E> extends LinkedList<E> implements
		FirstLastList<E>
{
	public FirstLastLinkedList()
	{
		super();
	}
	
	public FirstLastLinkedList(Collection<? extends E> arg0)
	{
		super(arg0);
	}
	
	@Override
	public E first() throws IndexOutOfBoundsException
	{
		return this.get(0);
	}
	
	@Override
	public E last() throws IndexOutOfBoundsException
	{
		return this.get(this.size()-1);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FirstLastLinkedList<E> clone()
	{
		return (FirstLastLinkedList<E>) super.clone();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public E[] toArray()
	{
		return (E[]) super.toArray();
	}
}
