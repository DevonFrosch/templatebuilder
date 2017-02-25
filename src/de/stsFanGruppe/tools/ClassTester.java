package de.stsFanGruppe.tools;

public class ClassTester
{
	public static boolean isAssignableFrom(Object tab, Class<?> superClass)
	{
		if(tab == null)
		{
			return false;
		}
		return tab.getClass().isAssignableFrom(superClass);
	}
}
