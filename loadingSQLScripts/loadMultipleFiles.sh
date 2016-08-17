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

globalTime=0
t1=0
resTime=0

function getTime()
{
	globalTime=`date +%s`
}


getTime
t1=$globalTime
mv $4/enwiki-*-categorylinks_0.sql $4/0_enwiki-categorylinks_0.sql
cp ./needFiles/0_enwiki-categorylinks_0.sql $4 
for file in `ls -1v $4`; do
	mysql --user=$1 --password=$2 $3 < $4/$file 
	getTime
	resTime=$((globalTime-t1))
	t1=$globalTime

	echo "Data from $file loaded ("$resTime"ms.)"
done

getTime
t1=$globalTime
mv $5/enwiki-*-page_0.sql $5/0_enwiki-page_0.sql
cp ./needFiles/0_enwiki-page_0.sql $5 
for file in `ls -1v $5`; do
	mysql --user=$1 --password=$2 $3 < $5/$file 
	getTime
	resTime=$((globalTime-t1))
	t1=$globalTime
	echo "Data from $file loaded ("$resTime"ms.)"
done
