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
import edu.upc.dama.wikiparser.utils.Parameters;
import java.util.Arrays;
import javax.xml.stream.XMLStreamException;
import edu.upc.dama.wikiparser.utils.ProgressCounter;
import edu.upc.dama.wikiparser.utils.SimpleXMLParser;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import net.java.textilej.parser.MarkupParser;
import net.java.textilej.parser.builder.HtmlDocumentBuilder;
import net.java.textilej.parser.markup.mediawiki.MediaWikiDialect;

public class ArticleExtractor extends SimpleXMLParser
{

private String title;
private String text;
private String id;
private final ArticlesIdsRelations articleIdArticleTitlesRelation;
private final Map<String, String> articleIdArticleText = new HashMap<>();
private final ProgressCounter pageCounter = new ProgressCounter();
private final Parameters parameters;

ArticleExtractor(Parameters parameters)
{
	super(Arrays.asList("page", "title", "id", "text"));

	articleIdArticleTitlesRelation = new ArticlesIdsRelations();
	this.parameters = parameters;
}

@Override
protected void handleElement(String element, String value)
{
	if ("page".equals(element))
	{
		if (!title.contains(":"))
		{
			try
			{
				writePage(title, id, text);
			} catch (XMLStreamException streamException)
			{
				throw new RuntimeException(streamException);
			} catch (IOException ex)
			{
				Logger.getLogger(ArticleExtractor.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		title = null;
		text = null;
		id = null;
	} else if ("title".equals(element))
	{
		title = value;
	} else if ("text".equals(element))
	{
		if (parameters.readBoolean(false, "GET_ARTICLES_TEXT"))
			text = value;
	} else if ("id".equals(element))
	{
		if (id == null)
			id = value;
	}
}

private void writePage(String title, String id, String text) throws XMLStreamException, IOException
{

	if (articleIdArticleTitlesRelation != null)
	{
		articleIdArticleTitlesRelation.put(title, id);
	}
	if (parameters.readBoolean(false, "GET_ARTICLES_TEXT"))
	{
		text = cleanWikiTest(text);

		if (articleIdArticleText != null)
		{
			articleIdArticleText.put(id, text);
		}
	}
	pageCounter.increment();
}

public ArticlesIdsRelations getArticles()
{
	return articleIdArticleTitlesRelation;
}

public Map<String, String> getArticlesText()
{
	return articleIdArticleText;
}

public int getPageCount()
{
	return pageCounter.getCount();
}

private String cleanWikiTest(String markup) throws IOException
{

	StringWriter writer = new StringWriter();

	HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
	builder.setEmitAsDocument(false);

	MarkupParser parser = new MarkupParser(new MediaWikiDialect());
	parser.setBuilder(builder);
	parser.parse(markup);

	final String html = writer.toString();
	final StringBuilder cleaned = new StringBuilder();

	HTMLEditorKit.ParserCallback callback = new HTMLEditorKit.ParserCallback()
	{
	public void handleText(char[] data, int pos)
	{
		cleaned.append(new String(data)).append(' ');
	}
	};
	new ParserDelegator().parse(new StringReader(html), callback, false);

	return cleaned.toString();
}

}
