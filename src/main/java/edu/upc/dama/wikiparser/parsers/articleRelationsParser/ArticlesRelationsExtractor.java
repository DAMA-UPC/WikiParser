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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.stream.XMLStreamException;
import edu.upc.dama.wikiparser.utils.ProgressCounter;
import edu.upc.dama.wikiparser.utils.SimpleXMLParser;

/**
 *
 * @author joan
 */
public class ArticlesRelationsExtractor extends SimpleXMLParser
{

private final ArticlesIdsRelations articlesIdRelations;
private Map<String, String> redirectsTo;
private Map<String, List<String>> aricleIdLinks;

private String title;
private String text;
private String id;
private final ProgressCounter pageCounter = new ProgressCounter();
private String redirect;
private static final Pattern LINK_PATTERN = Pattern.compile("\\[\\[(.+?)\\]\\]");

ArticlesRelationsExtractor()
{
	super(Arrays.asList("page", "title", "id", "text"), Arrays.asList("redirect"));
	this.articlesIdRelations = null;
}

ArticlesRelationsExtractor(ArticlesIdsRelations articlesId)
{
	super(Arrays.asList("page", "title", "id", "text"), Arrays.asList("redirect"));
	this.articlesIdRelations = articlesId;
	this.redirectsTo = new HashMap<>();
	this.aricleIdLinks = new HashMap<>();

}

public int getPageCount()
{
	return pageCounter.getCount();
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
				writeRedirecsLinks(title, id, text, redirect);
			} catch (XMLStreamException streamException)
			{
				throw new RuntimeException(streamException);

			}
		}
		title = null;
		text = null;
		id = null;
		redirect = null;
	} else if ("title".equals(element))
	{
		title = value;
	} else if ("text".equals(element))
	{
		text = value;
	} else if ("id".equals(element))
	{
		if (id == null)
			id = value;
	} else if ("redirect".equals(element))
	{
		redirect = value;
	}
}

private void writeRedirecsLinks(String title, String id, String text, String redirect) throws XMLStreamException
{

	identifyRedirects(title, id, redirect);
	identifyLinks(title, id);

	pageCounter.increment();
}

public Map<String, String> getRedirects()
{
	return redirectsTo;
}

public Map<String, List<String>> getLinks()
{
	return aricleIdLinks;
}

private Set<String> parseLinks(String text)
{
	Set<String> links = new HashSet<String>();
	if (text != null)
	{
		Matcher matcher = LINK_PATTERN.matcher(text);
		while (matcher.find())
		{
			String link = matcher.group(1);
			if (!link.contains(":"))
			{
				if (link.contains("|"))
				{
					link = link.substring(0, link.lastIndexOf('|'));
				}
				links.add(link);
			}
		}
	}
	return links;
}

private void identifyRedirects(String title, String id, String redirect) throws XMLStreamException
{
	if (redirect != null && redirectsTo != null)
	{

		String articleId = articlesIdRelations.getTitle(title);

		String redirectionId = articlesIdRelations.getTitle(redirect);
		if (articleId != null && redirectionId != null)
		{
			if (!articleId.equals(id))
				System.out.println("Possible error with article: " + title);
			redirectsTo.put(articleId, redirectionId);
		}
	}
}

private void identifyLinks(String title, String id) throws XMLStreamException
{

	Set<String> links = parseLinks(text);
	links.remove(title);

	if (links.size() == 1)
	{
		String redirectTo = redirectsTo.get(id);
		if (redirectTo != null)
		{
			String linkTitle = (String) links.toArray()[0];
			String linkId = getArticleId(linkTitle);
			if (linkId != null && redirectTo.equals(linkId))
			{
				return;
			}
		}
	}
	if (links.isEmpty())
		return;

	List<String> linksList = new ArrayList<>();
	for (String link : links)
	{
		if (articlesIdRelations != null)
		{
			String linkId = getArticleId(link);
			if (linkId != null)
			{
				linksList.add(linkId);
				//System.out.println("Article "+ link+ "has id: "+linkId);
			}
		}

	}
	if (articlesIdRelations != null)
	{
		aricleIdLinks.put(id, linksList);
	}
}

private String getArticleId(String linkTitle)
{
	String linkId = articlesIdRelations.getTitle(linkTitle);
	if (linkId == null & linkTitle.contains("#"))
	{
		String linkTitleTmp = linkTitle.substring(0, linkTitle.indexOf("#"));
		linkId = articlesIdRelations.getTitle(linkTitleTmp);
	}
	if (linkId == null)
	{
		linkId = articlesIdRelations.getNormalizedTitle(linkTitle);
		if (linkId == null & linkTitle.contains("#"))
		{
			String linkTitleTmp = linkTitle.substring(0, linkTitle.indexOf("#"));
			linkId = articlesIdRelations.getNormalizedTitle(linkTitleTmp);
		}

	}
	return linkId;
}
}
