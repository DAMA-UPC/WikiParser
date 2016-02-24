#!/bin/bash

# WikiParser is free software developed by Joan Guisado-Gámez: 
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

# echo ""
# echo ""
# echo "========================================================================="                                 
# echo "This script:"
# echo "1) Splits the file XXX-categorylinks.sql (path in arg1) into smaller sql files that are  saved in arg2." 
# echo "2) Splits the file XXX-page.sql (path in arg3) into smaller sql files that are  saved in arg4." 
# echo "3) Loads the files into the DB with the and user (arg5) and  password(arg6) and name(arg7)"
# echo "========================================================================="                                 
# echo ""
# echo ""

# if [ "$#" -ne 8 ]; then
#     echo "illegal number of parameters"
#     echo "1 - enwiki-XXX-categorylinks.sql file"
#     echo "2 - Category Links Temp Files Path"
#     echo "3 - enwiki-XXX-page.sql file"
#     echo "4 - Pages Temp Files Path" 
#     echo "5 - DB user"
#     echo "6 - DB password"
#     echo "7 - DB name"
#     exit;
# else
#     echo "1 - enwiki-XXX-categorylinks.sql file: $1"
#     echo "2 - Category Links Temp Files Path: $2"
#     echo "3 - enwiki-XXX-page.sql file: $3"
#     echo "4 - Pages Temp Files Path: $4"
#     echo "5 - DB user: $5"
#     echo "6 - DB password: $6"
#     echo "7 - DB name $7" 
# fi

./splitSQLScript.py $1 $2 #/data/joan/wikipedia/englishWikipedia/categorylinksTempFiles
./splitSQLScript.py $3 $4 #/data/joan/wikipedia/englishWikipedia/pageTempFiles

nohup ./loadMultipleFiles.sh $5 $6 $7 $2 $4
