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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.codehaus.stax2.XMLInputFactory2;

public abstract class SimpleXMLParser
{

private static final String FILENAME = "-";
private static final XMLInputFactory XML_FACTORY = XMLInputFactory2.newInstance();

private final List<String> goalAttributes;
private final List<String> goalElements;

public SimpleXMLParser(List<String> goalElements)
{
	this.goalElements = goalElements;
	this.goalAttributes = null;
}

public SimpleXMLParser(List<String> goalElements, List<String> goalAttributes)
{
	this.goalElements = goalElements;
	this.goalAttributes = goalAttributes;
}

protected abstract void handleElement(String element, String value);

public void parse(String fileName) throws IOException, XMLStreamException
{
	if (FILENAME.equals(fileName))
	{
		parse(System.in);
	} else
	{
		parse(new FileInputStream(fileName));
	}
}

private void parse(InputStream inputStream) throws IOException, XMLStreamException
{
	InputStreamReader ss =  new java.io.InputStreamReader(inputStream, "UTF-8");
	
	XMLStreamReader reader = XML_FACTORY.createXMLStreamReader(ss);
	try
	{
		parseElements(reader);
	} finally
	{
		reader.close();
		inputStream.close();
	}
}

private void parseElements(XMLStreamReader reader) throws XMLStreamException
{
	
	LinkedList<String> elementStack = new LinkedList<>();
	StringBuilder textBuffer = new StringBuilder();

	while (reader.hasNext())
	{
		switch (reader.next())
		{
			case XMLEvent.START_ELEMENT:
				elementStack.push(reader.getName().getLocalPart());
				if (isGoalAttribute(reader.getName().getLocalPart()))
				{
					handleElement(reader.getName().getLocalPart(), reader.getAttributeValue(0));
				} 
				//else
					textBuffer.setLength(0);
				break;
			case XMLEvent.END_ELEMENT:
				String element = elementStack.pop();
				if (isGoalElement(element))
				{
					handleElement(element, textBuffer.toString().trim());
				}
				break;
			case XMLEvent.CHARACTERS:
				if (isGoalElement(elementStack.peek()))
				{
					textBuffer.append(reader.getText());
				}
				break;
		}
	}
}

private boolean isGoalElement(String element)
{
	return goalElements.contains(element);
}

private boolean isGoalAttribute(String attribute)
{
	if (attribute == null)
		return false;
	if (goalAttributes == null)
		return false;
	return goalAttributes.contains(attribute);
}

}
