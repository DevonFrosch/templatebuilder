package de.stsFanGruppe.tools;

public class JSONBuilder
{
	protected StringBuilder json;
	protected StringBuilder indent;
	protected String indentChars;
	
	public JSONBuilder()
	{
		json = new StringBuilder();
		indent = new StringBuilder();
		indentChars = "  ";
	}
	public JSONBuilder(String indentChars)
	{
		this();
		NullTester.test(indentChars);
		this.indentChars = indentChars;
	}
	
	public void add(String value)
	{
		if(value == null)
		{
			value = "null";
		}
		else
		{
			value = "\""+value+"\"";
		}
		json.append(indent+value+",\n");
	}
	public void add(boolean value)
	{
		json.append(indent+String.valueOf(value)+",\n");
	}
	public void add(char value)
	{
		json.append(indent+String.valueOf(value)+",\n");
	}
	public void add(char[] value)
	{
		json.append(indent+String.valueOf(value)+",\n");
	}
	public void add(int value)
	{
		json.append(indent+String.valueOf(value)+",\n");
	}
	public void add(long value)
	{
		json.append(indent+String.valueOf(value)+",\n");
	}
	public void add(float value)
	{
		json.append(indent+String.valueOf(value)+",\n");
	}
	public void add(double value)
	{
		json.append(indent+String.valueOf(value)+",\n");
	}
	
	public void add(String name, String value)
	{
		if(name == null)
		{
			name = "null";
		}
		else
		{
			name = "\""+name+"\"";
		}
		if(value == null)
		{
			value = "null";
		}
		else
		{
			value = "\""+value+"\"";
		}
		json.append(indent+name+": "+value+",\n");
	}
	public void add(String name, boolean value)
	{
		json.append(indent+name+": "+String.valueOf(value)+",\n");
	}
	public void add(String name, char value)
	{
		json.append(indent+name+": "+String.valueOf(value)+",\n");
	}
	public void add(String name, char[] value)
	{
		json.append(indent+name+": "+String.valueOf(value)+",\n");
	}
	public void add(String name, int value)
	{
		json.append(indent+name+": "+String.valueOf(value)+",\n");
	}
	public void add(String name, long value)
	{
		json.append(indent+name+": "+String.valueOf(value)+",\n");
	}
	public void add(String name, float value)
	{
		json.append(indent+name+": "+String.valueOf(value)+",\n");
	}
	public void add(String name, double value)
	{
		json.append(indent+name+": "+String.valueOf(value)+",\n");
	}
	
	public void beginObject()
	{
		json.append(indent+"{\n");
		indent.append(indentChars);
	}
	public void beginObject(String name)
	{
		json.append(indent+name+": {\n");
		indent.append(indentChars);
	}
	public void endObject()
	{
		if(indent.length() >= indentChars.length())
		{
			indent.delete(indent.length() - indentChars.length(), indent.length());
		}
		json.append(indent+"}\n");
	}
	public void beginArray()
	{
		json.append(indent+"[\n");
		indent.append(indentChars);
	}
	public void beginArray(String name)
	{
		json.append(indent+name+": [\n");
		indent.append(indentChars);
	}
	public void endArray()
	{
		if(indent.length() >= indentChars.length())
		{
			indent.delete(indent.length() - indentChars.length(), indent.length());
		}
		json.append(indent+"]\n");
	}
	
	public String toString()
	{
		return json.toString();
	}
}
