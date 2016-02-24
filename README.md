# WikiParser

WikiParser is free software developed by Joan Guisado-Gámez:                                                     
you can redistribute it and/or modify it under the terms of
the GNU General Public License as published by the Free
Software Foundation, either version 3 of the License, or
(at your option) any later version.

Wikiparser is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

REQUIREMENTS
===
WikiParser parses English Wikipedia dump files into CSV files. It requires the following 
files:
- enwiki-XXX-pages-articles.xml
- enwiki-XXX-categorylinks.xml
- enwiki-XXX-page.sql
 
Files can be downloaded from https://dumps.wikimedia.org/enwiki/

XXX gives information about the date of the dump. 


CONFIGURE
===

WikiParser can be canfigured to parse the following data:

- articleIds.csv: articleId, articleTitle  //Relates each article pageId with its title.
- articlesText.csv: articleId, articleText //Relates each article with its plain text.
- categoriesIds.csv: wikipedia_categoryId, categoryName //Relates each category pageId with its name.
- links.csv: articleId_From, articleId_To //Relates two linked articles.
- redirects.csv: articleId_From, articleId_To //Relates redirect articles.
- articleToCategories.csv: articleId_From, categoryId_To //Relates linked articles and categories.
- categoryToCategories.csv: categoryId_From, categoryId_To //Relates two linked categories.

In order to configure it, modify the exampleConfig.cfg file to indicate what information to retrieve
and where to store it.

- In **PAGE_ARTICLES_FILE** specify where the XXX--pages-articles.xml is.
- In **CSV_FOLDER specify** where to store the results.
- Use **GET_ARTICLES_TEXT**, **GET_ARTICLES_LINKS**, **GET_ARTICLES_REDIRECTS** and **GET_CATEGORIES_RELATIONS** to indicate what information to parse.

In order to run WikiParser, you need to install mysql into your system. 
MySQL is used to store XXX-categorylinks.sql and XXX-pages.sql, which
are required to extract the links to/of the categories.

DATABASE info (user,pw and DB name) has to be in the exampleConfig.cfg

EXECUTE
===
Once exampleConfig.cfg is written. 
Be sure that mvn is in your PATH as well as Java 1.6, at least.

Execute run.sh 

```
./run.sh enwiki-XXX-categorylinks.sql /tmp/CL enwiki-XXX-page.sql /tmp/PG DB_U DB_PW DB_NAME
```

run.sh requires 7 pameters:
- 1: enwiki-XXX-categorylinks.sql filepath.
- 2: Temporal folder filespath for categorylinks, can be removed after the execution.
- 3: enwiki-XXX-page.sql filepath.
- 4: Temporal folder filepath for page, must be different from $2.
- 5: DB user.
- 6: DB password.
- 7: DB name.

