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

import edu.upc.dama.wikiparser.model.ArticlesIdsRelations;
import com.google.common.collect.HashBiMap;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WikiCSVWriter
{

public static void write_1N(Map<String, List<String>> map, String file, String[] header)
{
	List<String[]> entries = new ArrayList<>();
	entries.add(header);
	for (Map.Entry<String, List<String>> e : map.entrySet())
	{
		String id1 = e.getKey();
		for (String id2 : e.getValue())
		{
			String[] entry =
			{
				id1, id2
			};
			entries.add(entry);
		}
	}
	write(entries, file);

}
public static void write_11(Map<String, String> map, String file, String[] header)
{
	write_11(map, file, header, false);
}
public static void write_11(Map<String, String> map, String file, String[] header, boolean inverse)
{
	List<String[]> entries = new ArrayList<>();
	entries.add(header);
	for (Map.Entry<String, String> e : map.entrySet())
	{
		String id1 = !inverse ? e.getKey() : e.getValue();
		String id2 = !inverse ? e.getValue() : e.getKey();
		String[] entry =
		{
			id1, id2
		};
		entries.add(entry);
	}
	write(entries, file);
}

public static void write(ArticlesIdsRelations articlesIds, String file, String[] header)
{
	List<String[]> entries = new ArrayList<>();
	entries.add(header);
	HashBiMap<String, String> titleIdMap = articlesIds.getTitleIdMap();
	for (Map.Entry<String, String> e : titleIdMap.entrySet())
	{
		String id1 = e.getValue();
		String id2 = e.getKey();
		String[] entry =
		{
			id1, id2
		};
		entries.add(entry);
	}
	write(entries, file);

}

private static void write(List<String[]> entries, String filepath)
{
	CSVWriter csv;
	try
	{
		File file = new File(filepath);
		file.getParentFile().mkdirs();
		csv = new CSVWriter(new FileWriter(file));
		csv.writeAll(entries, true);
		csv.close();
	} catch (IOException ex)
	{
		System.err.println("[EXCEPTION] " + ex);
		Logger.getLogger(WikiCSVWriter.class.getName()).log(Level.SEVERE, null, ex);
	}
}
}
