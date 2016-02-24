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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author joan
 */
public class SQLHandle
{

String database;

private Connection connection;
private String pw;
private String user;
private String url;
private final Parameters parameters;

public SQLHandle(Parameters parameters)
{
	this.parameters = parameters;
}

public Connection getConnection() throws IOException
{
	try
	{
		if(connection==null)
		{
		this.user = parameters.getString("DB_USER"); //"wikiuser_";
		this.pw = parameters.getString("DB_PASSWORD");//"123456";
		String serverName = parameters.readString("localhost", "DB_SERVER");
		this.database = parameters.getString("DB");

		this.url = "jdbc:mysql://" + serverName + "/"+database+"?autoReconnect=true";
		connection = DriverManager.getConnection(url, user, pw);
		}
		return connection;
	} catch (SQLException ex)
	{
		System.out.println("[EXCEPTION] " + ex);
	}
	return null;
}

public String getDatabase()
{
	return database;
}

public String getPw()
{
	return pw;
}

public String getUser()
{
	return user;
}

public void closeConnection()
{
	try
	{
		connection.close();
	} catch (SQLException ex)
	{
		System.out.println("[EXCEPTION] " + ex);
	}
}

}
