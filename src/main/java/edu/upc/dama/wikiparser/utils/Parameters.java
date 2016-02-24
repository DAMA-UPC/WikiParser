 /*WikiParser is free software developed by Joan Guisado-Gámez: 
   you can redistribute it and/or modify it under the terms of
   the GNU General Public License as published by the Free 
   Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   Wikiparser is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.*/
package edu.upc.dama.wikiparser.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Parameters
{

Map<String, Object> parameters = new HashMap<>();

public Parameters(String filePath)
{
	File fin = new File(filePath);
	try
	{
		FileInputStream fis = new FileInputStream(fin);

		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line = null;
		while ((line = br.readLine()) != null)
		{
			if(line.startsWith("#")||line.trim().isEmpty()) continue;
			line = line.replace("\"", "");
			System.out.println("line = "+ line );
			String[] split = line.split("=");
			parameters.put(split[0].trim().toLowerCase(), split[1].trim());
		}
	} catch (Exception e)
	{
		System.err.println("Error: " + e);
	}
}

public String getString(String var)
{
	Object object = parameters.get(var.toLowerCase());
	if (object == null)
		return null;
	return String.valueOf(object);
}

public boolean readBoolean(boolean b, String var)
{
	Object object = parameters.get(var.toLowerCase());
	if (object == null)
		return b;
	else
		return "true".equals(String.valueOf(object).toLowerCase());
}

	String readString(String default_var, String var)
	{
		Object object = parameters.get(var.toLowerCase());
	if (object == null)
		return default_var;
	return String.valueOf(object);
	}
}
