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
package edu.upc.dama.wikiparser.model;

import com.google.common.collect.HashBiMap;


public class ArticlesIdsRelations
{

private final HashBiMap<String, String> titleIdMap;
private final HashBiMap<String, String> normalizedIdMap;

		public HashBiMap<String, String> getTitleIdMap()
		{
			return titleIdMap;
		}



public ArticlesIdsRelations()
{
	this.titleIdMap = HashBiMap.create();
	this.normalizedIdMap = HashBiMap.create();
}
public ArticlesIdsRelations(HashBiMap<String, String> titleIdMap,HashBiMap<String, String> normalizedIdMap)
{
	this.titleIdMap = HashBiMap.create(titleIdMap);
	this.normalizedIdMap = HashBiMap.create(normalizedIdMap);
}

		public void put(String title, String id)
		{
			this.titleIdMap.put(title, id);
			this.normalizedIdMap.put(title.toLowerCase(),id);
		}

		HashBiMap<String, String> getTitlesMap()
		{
			return titleIdMap;
		}

		public String getTitle(String linkTitle)
		{
			return titleIdMap.get(linkTitle);
		}

		public String getNormalizedTitle(String linkTitle)
		{
			return normalizedIdMap.get(linkTitle.toLowerCase());
		}

		public boolean containsId(String articleId)
		{
			return	titleIdMap.containsValue(articleId);
		}
	}
