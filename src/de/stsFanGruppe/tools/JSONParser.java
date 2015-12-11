package de.stsFanGruppe.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONParser
{
	public static final int ACTION_NEXT_ITEM = 0;
	public static final int ACTION_OPEN_ARRAY = 1;
	public static final int ACTION_OPEN_OBJECT = 2;
	public static final int ACTION_CLOSE_ARRAY = 3;
	public static final int ACTION_CLOSE_OBJECT = 4;
	
	protected BufferedReader input;
	protected StringBuilder zeile;
	
	public JSONParser(InputStream input)
	{
		this.input = new BufferedReader(new InputStreamReader(input));
	}
	
	public JSONAction nextAction() throws IOException
	{
		String name = null;
		String value = null;
		// vom identifyer weiß man noch nicht, was er werden wird...
		StringBuilder identifyer = new StringBuilder();
		
		while(true)
		{
			// TODO End of file?
			char c = read();
			
			// Variablen abschließen, wenn nötig
			switch(c)
			{
				case '{':
				case '[':
				case '}':
				case ']':
				case ';':
				case ',':
					if(identifyer.length() == 0)
					{
						break;
					}
					else if(name == null)
					{
						name = identifyer.toString();
						identifyer = new StringBuilder();
						break;
					}
					else if(value == null)
					{
						value = identifyer.toString();
						identifyer = new StringBuilder();
						break;
					}

					if(value.toLowerCase() == "true")
					{
						value = "1";
					}
					if(value.toLowerCase() == "false")
					{
						value = "0";
					}
			}
			
			switch(c)
			{
				case '{':
					return new JSONAction(ACTION_OPEN_OBJECT, name, value);
				case '[':
					return new JSONAction(ACTION_OPEN_ARRAY, name, value);
				case '}':
					return new JSONAction(ACTION_CLOSE_OBJECT);
				case ']':
					return new JSONAction(ACTION_CLOSE_ARRAY);
				case ';':
				case ',':
					return new JSONAction(ACTION_NEXT_ITEM, name, value);
				
				case ':':
					name = identifyer.toString();
					identifyer = new StringBuilder();
					break;
				case '"':
					if(identifyer.length() > 0)
					{
						throw new JSONParserException();
					}
					
					boolean escape = false;
					boolean breakloop = false;
					while(!breakloop)
					{
						// TODO: End of file?
						char c2 = read();
						
						// Escape-Sequences
						if(escape)
						{
							switch(c2)
							{
								case '"':
									breakloop = true;
									break;
								case '\\':
									identifyer.append('\\');
									break;
								case '/':
									identifyer.append('/');
									break;
								case 't':
									identifyer.append('\t');
									break;
								case 'r':
									identifyer.append('\r');
									break;
								case 'n':
									identifyer.append('\n');
									break;
								// unicode
								case 'u':
									// TODO Unicode \\uxxxx
									
									identifyer.append('\n');
								default:
							}
						}
						else
						{
							// nästes Escape?
							if(c2 == '\\')
							{
								escape = true;
							}
							else
							{
								identifyer.append(c2);
							}
						}
					}
					break;
				default:
					identifyer.append(c);
			}
		}
		
	}
	
	protected char read() throws IOException
	{
		if(zeile == null || zeile.length() == 0)
		{
			zeile = new StringBuilder(input.readLine());
		}
		char c = zeile.charAt(0);
		zeile.delete(0, 1);
		return c;
	}
	
	public class JSONAction
	{
		public final int type;
		public final String name;
		public final String value;
		
		protected JSONAction(int type)
		{
			this(type, null, null);
		}
		protected JSONAction(int type, String name, String value)
		{
			this.type = type;
			this.name = name;
			this.value = value;
		}
	}
	
	public class JSONParserException extends RuntimeException
	{
		public JSONParserException()
		{
			super();
		}
		public JSONParserException(String message)
		{
			super(message);
		}
		public JSONParserException(Throwable cause)
		{
			super(cause);
		}
		public JSONParserException(String message, Throwable cause)
		{
			super(message, cause);
		}
	}
}
