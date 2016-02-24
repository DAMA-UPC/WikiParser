#!/bin/bash

export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_75.jdk/Contents/Home


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

if [ "$#" -ne 8 ]; then
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

loadingSQLScripts/run.sh

mvn install
mvn exec:java
