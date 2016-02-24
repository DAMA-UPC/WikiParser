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

package edu.upc.dama.wikiparser.parsers.articleRelationsParser;

import edu.upc.dama.wikiparser.model.ArticlesIdsRelations;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.XMLOutputFactory2;

public class ExtractArticleRelations
{

private final ArticlesIdsRelations articlesId;
private Map<String, String> redirects;
private Map<String, List<String>> links;

public ExtractArticleRelations()
{
	this.articlesId = null;
}

public ExtractArticleRelations(ArticlesIdsRelations articlesId)
{
	this.articlesId = articlesId;
}


public void extract(String inputFile) throws IOException, XMLStreamException
{
	System.out.println("Parsing pages and extracting links and redirects...");

	long startTime = System.currentTimeMillis();



	ArticlesRelationsExtractor redirectLinkExtractor = articlesId == null ? new ArticlesRelationsExtractor() : new ArticlesRelationsExtractor(articlesId);
	redirectLinkExtractor.parse(inputFile);

	links = redirectLinkExtractor.getLinks();
	redirects = redirectLinkExtractor.getRedirects();


	long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
	System.out.printf("\n%d pages parsed in %d seconds.\n", redirectLinkExtractor.getPageCount(), elapsedSeconds);
}

	public Map<String, String> getRedirects()
	{
		return (HashMap<String,String>)redirects;
	}

	public Map<String, List<String>> getLinks()
	{
		return links;
	}


}
