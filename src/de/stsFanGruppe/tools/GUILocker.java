package de.stsFanGruppe.tools;

import java.time.Instant;
import java.util.Hashtable;

public abstract class GUILocker
{
	protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GUILocker.class);
	private static Hashtable<String, Boolean> stringLocks = new Hashtable<>();
	private static Hashtable<Class<?>, Boolean> classLocks = new Hashtable<>();
	
	/**
	 * Locks the lock, returning after first try.
	 * @param name Name of lock.
	 * @return true if lock was acquired, else false.
	 */
	public static boolean lock(String name)
	{
		assert name != null;
		
		synchronized(stringLocks)
		{
			if(stringLocks.get(name) != null && stringLocks.get(name))
			{
				return false;
			}
			stringLocks.put(name, true);
		}
		
		log.debug("WindowLock für {}", name);
		return true;
	}
	
	/**
	 * Locks the lock, retrying until it is locked or timeout is reached.
	 * @param name Name of lock.
	 * @param timeout Timeout in milliseconds.
	 * @return true if lock was acquired, false if timeout is reached.
	 */
	public static boolean lock(String name, int timeout)
	{
		Instant start = Instant.now();
		Instant stop = start.plusMillis(timeout);
		while(Instant.now().isBefore(stop))
		{
			if(lock(name))
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
	 * @param name Name of lock.
	 */
	public static void unlock(String name)
	{
		assert name != null;
		synchronized(stringLocks)
		{
			stringLocks.put(name, false);
		}
		log.debug("WindowUnlock für {}", name);
	}
	
	/**
	 * Locks the lock, returning after first try.
	 * @param reference Class reference.
	 * @return true if lock was acquired, else false.
	 */
	public static boolean lock(Class<?> reference)
	{
		assert reference != null;
		
		synchronized(classLocks)
		{
			if(classLocks.get(reference) != null && stringLocks.get(reference))
			{
				return false;
			}
			classLocks.put(reference, true);
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
		assert reference != null;
		synchronized(classLocks)
		{
			classLocks.put(reference, false);
		}
		log.debug("ClassUnlock für {}", reference);
	}
}
