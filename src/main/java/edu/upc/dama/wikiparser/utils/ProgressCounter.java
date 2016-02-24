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

public class ProgressCounter {

    private static final int THOUSAND = 1000;
    private static final int SMALL = 1 * THOUSAND;
    private static final int BIG = 50 * THOUSAND;

    private int count = 0;

    public int getCount() {
        return count;
    }

    public void increment() {
        count++;
        if (count % BIG == 0) {
            System.out.println(". "+ count / THOUSAND +"k");
        } else if (count % SMALL == 0) {
            System.out.print(".");
        }
    }

}
