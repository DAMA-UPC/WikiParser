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
package edu.upc.dama.wikiparser.parsers.articleCategoriesRelations;

import edu.upc.dama.wikiparser.model.ArticlesIdsRelations;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import edu.upc.dama.wikiparser.utils.SQLHandle;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtractArticleCategoriesRelations
{

BiMap<String, String> cateogiresIds = HashBiMap.create();
Map<String, List<String>> articleToCategories = new HashMap<>();
Map<String, List<String>> categoryToCategories = new HashMap<>();


private final ArticlesIdsRelations articlesRelations;

public ExtractArticleCategoriesRelations(ArticlesIdsRelations articlesRelations)
{
	this.articlesRelations = articlesRelations;
}

public void extract(SQLHandle sqlHandles) throws IOException, InterruptedException
{
	Connection connection = sqlHandles.getConnection();
	
	

	calculateCategoriesIds(connection);
	calculateArticlesToCategories(connection);
	calculateCategoriesHierarchy(connection);
}

private void calculateCategoriesIds(Connection connection) throws UnsupportedEncodingException
{
	String sqlGetCategoriesIds = "SELECT page_id, page_title FROM page WHERE page_namespace=14";
	try
	{
		PreparedStatement getCategoriesIds = connection.prepareStatement(sqlGetCategoriesIds);
		ResultSet executeQuery = getCategoriesIds.executeQuery();
		while (executeQuery.next())
		{

			String categoryId = executeQuery.getString("page_id");
			String categoryTitle = new String(executeQuery.getBytes("page_title"), "latin1");
			if (!categoryTitle.equalsIgnoreCase("Hidden_categories"))
			{
				cateogiresIds.put(categoryId, categoryTitle.replace("_", " "));
			}
		}
	} catch (SQLException ex)
	{
		System.out.println("[EXCEPTION 3] " + ex);
	}

}

private void calculateArticlesToCategories(Connection connection)
{

	String sqlGetArticlesCategories = "SELECT p1.page_id, p2.page_id "
			+ "FROM page p1, page p2, categorylinks cl "
			+ "WHERE   p1.page_namespace=0 and "
			+ "        p2.page_namespace=14 and "
			+ "        p1.page_id=cl.cl_from and "
			+ "        p2.page_title=cl.cl_to";
	try
	{
		PreparedStatement getArticlesCategories = connection.prepareStatement(sqlGetArticlesCategories);
		ResultSet executeQuery = getArticlesCategories.executeQuery();
		while (executeQuery.next())
		{
			String articleId = executeQuery.getString("p1.page_id");
			String categoryId = executeQuery.getString("p2.page_id");

			if (articlesRelations.containsId(articleId) && cateogiresIds.containsKey(categoryId))
			{
				List<String> listOfCategories = articleToCategories.get(articleId);
				if (listOfCategories == null)
				{
					listOfCategories = new ArrayList<>();
				}
				listOfCategories.add(categoryId);
				articleToCategories.put(articleId, listOfCategories);
			}

		}
	} catch (SQLException ex)
	{
		System.out.println("[EXCEPTION 1] " + ex);
	}
}

private void calculateCategoriesHierarchy(Connection connection)
{

	String sqlGetCategoryHierarchy = "SELECT p1.page_id, p2.page_id "
			+ "FROM page p1, page p2, categorylinks cl "
			+ "WHERE p1.page_namespace=14 and "
			+ "      p2.page_namespace=14 and "
			+ "      p1.page_id=cl.cl_from and"
			+ "      p2.page_title=cl.cl_to";
	try
	{
		PreparedStatement getCategoryHierarchy = connection.prepareStatement(sqlGetCategoryHierarchy);
		ResultSet executeQuery = getCategoryHierarchy.executeQuery();
		while (executeQuery.next())
		{
			String categoryId1 = executeQuery.getString("p1.page_id");
			String categoryId2 = executeQuery.getString("p2.page_id");

			if (cateogiresIds.containsKey(categoryId1) && cateogiresIds.containsKey(categoryId2))
			{

				List<String> listOfCategories = categoryToCategories.get(categoryId1);
				if (listOfCategories == null)
				{
					listOfCategories = new ArrayList<>();
				}
				listOfCategories.add(categoryId2);
				categoryToCategories.put(categoryId1, listOfCategories);
			}

		}
	} catch (SQLException ex)
	{
		System.out.println("[EXCEPTION 2] " + ex);
	}
}

public Map<String, List<String>> getCategoryToCategories()
{
	return categoryToCategories;
}

public BiMap<String, String> getCateogiresIds()
{
	return cateogiresIds;
}

public Map<String, List<String>> getArticleToCategories()
{
	return articleToCategories;
}
}