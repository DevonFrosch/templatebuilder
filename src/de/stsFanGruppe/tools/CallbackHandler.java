package de.stsFanGruppe.tools;

import java.awt.EventQueue;
import java.util.HashSet;
import java.util.Set;

public class CallbackHandler
{
	private Set<Runnable> callbacks = new HashSet<>();
	
	public void runAll()
	{
		synchronized(callbacks)
		{
			for(Runnable callback : callbacks)
			{
				EventQueue.invokeLater(callback);
			}
		}
	}
	
	public void register(Runnable callback)
	{
		synchronized(callbacks)
		{
			callbacks.add(callback);
		}
	}
	
	public void unregister(Runnable callback)
	{
		synchronized(callbacks)
		{
			callbacks.remove(callback);
		}
	}
}
