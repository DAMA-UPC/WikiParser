#!/usr/bin/env python

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
import sys
import os

directory=sys.argv[2]

if not os.path.exists(directory):
	os.makedirs(directory)

i = 0;      
print str(sys.argv)
filebase = directory+"/"+sys.argv[1][sys.argv[1].rfind('/')+1:sys.argv[1].rfind('.')]
writer = open(filebase+"_"+str(i)+".sql",'w')
with open(sys.argv[1]) as f:
	for line in f:
		if line.startswith("INSERT"):
			writer.write("commit;")
			writer.close()
			i=i+1
			print filebase+"_"+str(i)+".sql"
			writer = open(filebase+"_"+str(i)+".sql",'w')
			writer.write(line)
		else:
			writer.write(line)
			
