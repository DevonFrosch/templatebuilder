package de.stsFanGruppe.tools;

public class NullTester
{
	public static void test(Object arg)
	{
		if(arg == null)
		{
			throw new NullPointerException();
		}
	}
}
