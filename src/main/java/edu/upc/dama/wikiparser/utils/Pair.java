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

public class Pair<A extends Object, B extends Object> extends Object
{

    private A first;
    private B second;

    public Pair(A first, B second)
    {
        this.first = first;
        this.second = second;
    }

	public Pair()
	{
		this.first=null;
		this.second=null;
	}

    public A getFirst()
    {
        return first;
    }

    public void setFirst(A first)
    {
        this.first = first;
    }

    public B getSecond()
    {
        return second;
    }

    public void setSecond(B second)
    {
        this.second = second;
    }

    @Override
    public boolean equals(Object p)
    {

        if (p.getClass() != this.getClass())
        {
            return false;
        } else
        {
            Pair p2 = (Pair) p;
            if (p2.getFirst().getClass() != this.getFirst().getClass() || p2.getSecond().getClass() != this.getSecond().getClass())
            {
                return false;
            } else
            {
                if (!p2.getFirst().equals(this.getFirst()) || !p2.getSecond().equals(this.getSecond()))
                {
                    return false;
                }
            }
        }
        return true;

    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 43 * hash + (this.first != null ? this.first.hashCode() : 0);
        hash = 43 * hash + (this.second != null ? this.second.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString()
    {
		String newFirst = (first == null)? "\"\"" : first.toString();
		String newSecond = (second==null)? "\"\"" : second.toString();
        return "<" + newFirst + "," + newSecond + ">";
    }
}