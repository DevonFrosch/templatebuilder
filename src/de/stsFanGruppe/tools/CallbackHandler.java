package de.stsFanGruppe.tools;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;

public class CallbackHandler
{
	protected int callbackCounter = 0;
	protected Map<Object, Runnable> callbacks = new HashMap<>();
	
	public void runAll()
	{
		synchronized(callbacks)
		{
			for(Runnable callback: callbacks.values())
			{
				EventQueue.invokeLater(callback);
			}
		}
	}
	
	public Object register(Runnable callback)
	{
		NullTester.test(callback);
		Object callbackId = Integer.valueOf(callbackCounter++);
		
		synchronized(callbacks)
		{
			assert !callbacks.containsKey(callbackId);
			callbacks.put(callbackId, callback);
		}
		
		return callbackId;
	}
	
	public void unregister(Object callbackId)
	{
		synchronized(callbacks)
		{
			callbacks.remove(callbackId);
		}
	}
}
