package de.stsFanGruppe.tools;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;

public class FeedbackCallbackHandler
{
	private Set<BooleanSupplier> callbacks = new HashSet<>();
	
	public boolean runAll()
	{
		synchronized(callbacks)
		{
			for(BooleanSupplier callback : callbacks)
			{
				if(!callback.getAsBoolean())
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public void register(BooleanSupplier callback)
	{
		synchronized(callbacks)
		{
			callbacks.add(callback);
		}
	}
	
	public void unregister(BooleanSupplier callback)
	{
		synchronized(callbacks)
		{
			callbacks.remove(callback);
		}
	}
}
