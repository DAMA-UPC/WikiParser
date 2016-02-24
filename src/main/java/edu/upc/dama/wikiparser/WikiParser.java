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

package edu.upc.dama.wikiparser;

//Parameters;
import edu.upc.dama.wikiparser.utils.Parameters;
import edu.upc.dama.wikiparser.model.ArticlesIdsRelations;
import edu.upc.dama.wikiparser.parsers.articleCategoriesRelations.ExtractArticleCategoriesRelations;
import edu.upc.dama.wikiparser.parsers.articleParser.ExtractArticles;
import com.google.common.collect.BiMap;
import java.util.List;
import java.util.Map;
import edu.upc.dama.wikiparser.utils.WikiCSVWriter;
import edu.upc.dama.wikiparser.utils.SQLHandle;
import edu.upc.dama.wikiparser.parsers.articleRelationsParser.ExtractArticleRelations;
import edu.upc.dama.wikiparser.utils.Pair;

public class WikiParser
{

public static void main(String[] args) throws Exception
{
	System.out.println("Loading configuration in: " + args[2]);

	Parameters parameters = new Parameters(args[2]);

	executeMain(parameters);

}

private static void executeMain(Parameters parameters) throws Exception
{
	readConfig(parameters);

	
	//INPUT FILES
	String wikipediaFilePath = parameters.getString("PAGE_ARTICLES_FILE");


	//OUTPUTFILES
	String csvFolder = parameters.getString("CSV_FOLDER");// baseFolder+"CSVs-"+wikipediaVersion+"/";

	ExtractArticles articlesExtractor = new ExtractArticles(parameters);
	Pair<ArticlesIdsRelations, Map<String, String>> articlesIdsRelations_articlesText = articlesExtractor.extract(wikipediaFilePath);
	ArticlesIdsRelations articlesIdsRelations = articlesIdsRelations_articlesText.getFirst();

	WikiCSVWriter.write(articlesIdsRelations, csvFolder + "articleIds.csv", new String[]
	{
		"id", "title"
	});

	if (parameters.readBoolean(false, "GET_ARTICLES_TEXT"))
	{
		Map<String, String> articlesText = articlesIdsRelations_articlesText.getSecond();
		WikiCSVWriter.write_11(articlesText, csvFolder + "articlesText.csv", new String[]
		{
			"id", "text"
		});

	}

	if (parameters.readBoolean(true, "GET_ARTICLES_LINKS") || parameters.readBoolean(true, "GET_ARTICLES_REDIRECTS"))
	{

		ExtractArticleRelations articlesRelations = new ExtractArticleRelations(articlesIdsRelations);
		articlesRelations.extract(wikipediaFilePath);

		if (parameters.readBoolean(true, "GET_ARTICLES_LINKS"))
		{
			Map<String, List<String>> links = articlesRelations.getLinks();
			WikiCSVWriter.write_1N(links, csvFolder + "links.csv", new String[]
			{
				"idFrom", "idTo"
			});
			links = null;
		}

		if (parameters.readBoolean(true, "GET_ARTICLES_REDIRECTS"))
		{
			Map<String, String> redirects = articlesRelations.getRedirects();
			WikiCSVWriter.write_11(redirects, csvFolder + "redirects.csv", new String[]
			{
				"idFrom", "idTo"
			}, false);
			redirects = null;
		}
	}
	if (parameters.readBoolean(true, "GET_CATEGORIES_RELATIONS"))
	{
		SQLHandle sqlHandles = new SQLHandle(parameters);
		ExtractArticleCategoriesRelations articleCategoriesExtractor = new ExtractArticleCategoriesRelations(articlesIdsRelations);
		articleCategoriesExtractor.extract(sqlHandles);
		BiMap<String, String> cateogiresIds = articleCategoriesExtractor.getCateogiresIds();
		WikiCSVWriter.write_11(cateogiresIds, csvFolder + "categoriesIds.csv", new String[]
		{
			"id", "name"
		}, false);
		cateogiresIds = null;
		Map<String, List<String>> articleToCategories = articleCategoriesExtractor.getArticleToCategories();
		WikiCSVWriter.write_1N(articleToCategories, csvFolder + "articleToCategories.csv", new String[]
		{
			"idFrom", "idTo"
		});
		articleToCategories = null;
		Map<String, List<String>> categoryToCategories = articleCategoriesExtractor.getCategoryToCategories();
		WikiCSVWriter.write_1N(categoryToCategories, csvFolder + "categoryToCategories.csv", new String[]
		{
			"idFrom", "idTo"
		});
		sqlHandles.closeConnection();
		categoryToCategories = null;
	}
}

private static void readConfig(Parameters parameters)
{
	//INPUT FILES
	System.out.println("	Wikipedia XML file: " + parameters.getString("PAGE_ARTICLES_FILE"));


	//OUTPUTFILES
	System.out.println("	CSV output folder: " + parameters.getString("CSV_FOLDER"));

	System.out.println("	Get articles text: " + parameters.readBoolean(false, "GET_ARTICLES_TEXT"));
	System.out.println("	Get articles links: " + parameters.readBoolean(true, "GET_ARTICLES_LINKS"));
	System.out.println("	Get articles redirects: " + parameters.readBoolean(true, "GET_ARTICLES_REDIRECTS"));
	System.out.println("	Get categories relations: " + parameters.readBoolean(true, "GET_CATEGORIES_RELATIONS"));
}

}
