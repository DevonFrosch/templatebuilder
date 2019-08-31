package de.stsFanGruppe.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class FeedbackCallbackHandler
{
	protected int callbackCounter = 0;
	protected Map<Object, BooleanSupplier> callbacks = new HashMap<>();
	
	public boolean runAll()
	{
		synchronized(callbacks)
		{
			for(BooleanSupplier callback: callbacks.values())
			{
				if(!callback.getAsBoolean())
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public Object register(BooleanSupplier callback)
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
