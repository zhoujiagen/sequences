package com.spike.giantdataanalysis.sequences.query.calcite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.apache.calcite.jdbc.CalciteConnection;

public class ExampleCalciteCSV {
  public static void main(String[] args) throws Exception {

    // SQL: https://calcite.apache.org/docs/reference.html
    // JDBC connect string parameters: https://calcite.apache.org/docs/adapter.html

    // use model json.
    Class.forName("org.apache.calcite.jdbc.Driver");
    Properties info = new Properties();
    info.setProperty("model", "src/test/resources/model/tpc.json");
    info.setProperty("lex", "MYSQL");
    info.setProperty("schema", "tpc");
    info.setProperty("schemaType", "CUSTOM");

    Connection connection = DriverManager.getConnection("jdbc:calcite:", info);
    CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);

    Statement statement = calciteConnection.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT * FROM Customer LIMIT 10");
    StringBuilder sb = new StringBuilder();
    while (resultSet.next()) {
      sb.append(" ").append(resultSet.getString(1));
      sb.append(" ").append(resultSet.getString(2));
      sb.append(" ").append(resultSet.getString(3));
      sb.append(" ").append(resultSet.getString(4));
      sb.append(" ").append(resultSet.getString(5));
      sb.append(" ").append(resultSet.getString(6));
      sb.append(" ").append(resultSet.getString(7));
      sb.append(" ").append(resultSet.getString(8));
      sb.append(" ").append(resultSet.getString(9));
      sb.append(" ").append(resultSet.getString(10));
      sb.append(" ").append(resultSet.getString(11));
      sb.append(" ").append(resultSet.getString(12));
      sb.append(" ").append(resultSet.getString(13));
      sb.append(" ").append(resultSet.getString(14));
      sb.append(" ").append(resultSet.getString(15));
      sb.append(" ").append(resultSet.getString(16));
      sb.append(" ").append(resultSet.getString(17));
      sb.append(" ").append(resultSet.getString(18));
      sb.append(" ").append(resultSet.getString(19));
      sb.append(" ").append(resultSet.getString(20));
      sb.append(" ").append(resultSet.getString(21));
      sb.append("\n");
    }
    System.out.println(sb.toString());

    resultSet.close();
    statement.close();
    connection.close();

  }
}
