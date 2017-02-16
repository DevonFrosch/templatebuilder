package de.stsFanGruppe.tools;

import java.time.Instant;
import java.util.Hashtable;
import java.util.StringJoiner;

public abstract class GUILocker
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GUILocker.class);
	private static Hashtable<Class<?>, Boolean> locks = new Hashtable<>();
	
	/**
	 * Locks the lock, returning after first try.
	 * @param reference Class reference.
	 * @return true if lock was acquired, else false.
	 */
	public static boolean lock(Class<?> reference)
	{
		assert locks != null;
		assert reference != null;
		
		synchronized(locks)
		{
			assert locks != null;
			assert reference != null;
			
			if(locks.containsKey(reference) && locks.get(reference))
			{
				return false;
			}
			locks.put(reference, true);
		}
		
		log.debug("ClassLock für {}", reference);
		return true;
	}
	
	/**
	 * Locks the lock, retrying until it is locked or timeout is reached.
	 * @param reference Class reference.
	 * @param timeout Timeout in milliseconds.
	 * @return true if lock was acquired, false if timeout is reached.
	 */
	public static boolean lock(Class<?> reference, int timeout)
	{
		Instant start = Instant.now();
		Instant stop = start.plusMillis(timeout);
		while(Instant.now().isBefore(stop))
		{
			if(lock(reference))
			{
				return true;
			}
			
			// Sleep to use less cpu time
			if(timeout >= 100)
			{
				try
				{
					Thread.sleep(Integer.min(200, timeout / 20));
				}
				catch(InterruptedException e)
				{}
			}
		}
		return false;
	}
	
	/**
	 * Unlocks the lock.
	 * @param reference Class reference.
	 */
	public static void unlock(Class<?> reference)
	{
		assert locks != null;
		assert reference != null;
		synchronized(locks)
		{
			locks.put(reference, false);
		}
		log.debug("ClassUnlock für {}", reference);
	}
	
	/**
	 * Returns a String with information about the locks currently active.
	 * @returns Printable String
	 */
	public static String lockInfo()
	{
		StringJoiner text = new StringJoiner("\n");
		
		synchronized(locks)
		{
			if(locks.isEmpty())
			{
				text.add("Keine Locks registriert.");
			}
			else
			{
				text.add("Locks:");
				locks.forEach((reference, locked) ->
					text.add(reference.toString()+": "+((locked) ? "locked" : "unlocked"))
				);
			}
		}
		return text.toString();
	}
}
