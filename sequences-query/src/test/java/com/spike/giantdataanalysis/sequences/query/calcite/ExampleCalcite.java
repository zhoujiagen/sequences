package com.spike.giantdataanalysis.sequences.query.calcite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.apache.calcite.jdbc.CalciteConnection;

public class ExampleCalcite {

  public static void main(String[] args) throws Exception {
    // SQL: https://calcite.apache.org/docs/reference.html
    // JDBC connect string parameters: https://calcite.apache.org/docs/adapter.html

    Class.forName("org.apache.calcite.jdbc.Driver");
    Properties info = new Properties();
    info.setProperty("lex", "MYSQL");
    info.setProperty("schema", "tpc");
    info.setProperty("schemaType", "JDBC");
    info.setProperty("schema.jdbcUrl", "jdbc:mysql://localhost/tpc");
    info.setProperty("schema.jdbcUser", "root");
    info.setProperty("schema.jdbcPassword", "admin");

    Connection connection = DriverManager.getConnection("jdbc:calcite:", info);
    CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);

    Statement statement = calciteConnection.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT * FROM Customer LIMIT 10");
    while (resultSet.next()) {
      Customer customer =
          new Customer(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3),
              resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),
              resultSet.getString(7), resultSet.getString(8), resultSet.getString(9),
              resultSet.getString(10), resultSet.getString(11), resultSet.getString(12),
              resultSet.getDate(13), resultSet.getString(14), resultSet.getBigDecimal(15),
              resultSet.getBigDecimal(16), resultSet.getBigDecimal(17), resultSet.getBigDecimal(18),
              resultSet.getBigDecimal(19), resultSet.getBigDecimal(20), resultSet.getString(21));
      System.err.println(customer);
    }

    resultSet.close();
    statement.close();
    connection.close();
  }

}
