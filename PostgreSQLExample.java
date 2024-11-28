///
// delphi::postgres example
//
//<Context>
// <Resource name="jdbc/postgres" auth="Container"
//  type="javax.sql.DataSource" driverClassName="org.postgresql.Driver"
//  username="<user>" password="<secret>" ...
//  url="jdbc:postgresql://localhost:5432/delphi"/>
//</Context>

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.io.Console;
import java.util.Scanner;

public class PostgreSQLExample
{
  // Connect to the database
  protected static Connection getConnection(String url,
					    String username,
					    String password)
  {
    Connection connection = null;
    try {
       connection = DriverManager.getConnection(url, username, password);
    } catch (SQLException ex) {
       ex.printStackTrace();
    }
    return connection;
  }

  public static void main(String[] args)
  {
    final String jdbcUrl = "jdbc:postgresql://localhost:5432/delphi";
    final String driver  = "org.postgresql.Driver";

    try {
      Class.forName(driver);  // Register the PostgreSQL driver
    } catch (ClassNotFoundException ex) {
      System.out.println("ClassNotFoundException => " + driver);
      System.exit(0);
    }

    // Reading <username> and <password> from System.in
    Scanner reader = new Scanner(System.in);
    System.out.print("username: ");
    String username = reader.next();

    System.out.print("password: ");
    String password = reader.next();
    reader.close();

    Connection connection = getConnection(jdbcUrl, username, password);
    if (connection == null) {
      System.out.println("getConnection("+ jdbcUrl +", " + username +", x)");
      System.exit(0);
    }

    try {
      // Perform desired database operations
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM blog");

      while (resultSet.next()) {
        String stamp = resultSet.getString("created");
        String story = resultSet.getString("story");
        System.out.println(stamp +" "+ story);
      }
      connection.close();  // Close the connection
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  } //</main>
} //</PostgreSQLExample>

