#!/bin/bash

# WikiParser is free software developed by Joan Guisado-Gamez: 
# you can redistribute it and/or modify it under the terms of
# the GNU General Public License as published by the Free 
# Software Foundation, either version 3 of the License, or
# (at your option) any later version.

# Wikiparser is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.

# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.

# export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_75.jdk/Contents/Home


echo ""
echo ""
echo "========================================================================="                                 
echo "This script:"
echo "1) Splits the file XXX-categorylinks.sql (path in arg1) into smaller sql files that are  saved in arg2." 
echo "2) Splits the file XXX-page.sql (path in arg3) into smaller sql files that are  saved in arg4." 
echo "3) Loads the files into the DB with the and user (arg5) and  password(arg6) and name(arg7)"
echo "4) Parses Wikipedia"
echo "========================================================================="                                 
echo ""
echo ""

if [ "$#" -ne 7 ]; then
	echo "illegal number of parameters"
	echo "1 - enwiki-XXX-categorylinks.sql file"
	echo "2 - Category Links Temp Files Path"
	echo "3 - enwiki-XXX-page.sql file"
	echo "4 - Pages Temp Files Path" 
	echo "5 - DB user"
	echo "6 - DB password"
	echo "7 - DB name"
	exit;
else
	echo "1 - enwiki-XXX-categorylinks.sql file: $1"
	echo "2 - Category Links Temp Files Path: $2"
	echo "3 - enwiki-XXX-page.sql file: $3"
	echo "4 - Pages Temp Files Path: $4"
	echo "5 - DB user: $5"
	echo "6 - DB password: $6"
	echo "7 - DB name $7" 
fi

loadingSQLScripts/run.sh $1 $2 $3 $4 $5 $6 $7

mvn install
mvn exec:java
