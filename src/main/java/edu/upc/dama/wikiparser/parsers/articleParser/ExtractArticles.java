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
package edu.upc.dama.wikiparser.parsers.articleParser;

import edu.upc.dama.wikiparser.model.ArticlesIdsRelations;
import edu.upc.dama.wikiparser.utils.Pair;
import edu.upc.dama.wikiparser.utils.Parameters;
import java.io.IOException;
import java.util.Map;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.XMLOutputFactory2;

/**
 *
 * @author joan
 */
public class ExtractArticles
{

/**
 * @param args the command line arguments
 */

	private final Parameters parameters;

	public ExtractArticles(Parameters parameters)
	{
		this.parameters = parameters;
	}

	private ExtractArticles()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


public Pair<ArticlesIdsRelations, Map<String, String>> extract(String inputFile) throws IOException, XMLStreamException
{
	System.out.println("Parsing pages and extracting Articles...");

	long startTime = System.currentTimeMillis();
	XMLOutputFactory outputFactory = XMLOutputFactory2.newInstance();

	

	ArticleExtractor articleExtractor = new ArticleExtractor(parameters);
	articleExtractor.parse(inputFile);
	ArticlesIdsRelations articles = articleExtractor.getArticles();
	Map<String, String> articlesText = articleExtractor.getArticlesText();
	/*for (Map.Entry<String, String> entry : articles.getTitlesMap().entrySet())
	{
	StringEscapeUtils.escapeCsv(entry.getKey());
	System.out.println(	StringEscapeUtils.escapeCsv(entry.getKey())+ " --> " + entry.getValue());
	}*/


	long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
	System.out.printf("\n%d pages parsed in %d seconds.\n", articleExtractor.getPageCount(), elapsedSeconds);
	return new Pair< > (articles,articlesText);
}

}
