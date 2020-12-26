package chat;

import java.sql.*;

public class DBHandler {
    private String dbHost = "localhost";
    private String dbPort = "3306";
    private String dbUser = "root";
    private String dbPass = "1234";
    private String dbName = "chat";

    public static final String TABLE = "users";
    public static final String ID = "ID";
    public static final String LOGIN = "login";
    public static final String PASS = "password";

    Connection connection;
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        String connectionStr = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName +
                "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(connectionStr, dbUser, dbPass);
        return connection;
    }

    public void addUser(String login, String passWord) throws SQLException, ClassNotFoundException {
        String sqlInsert = "INSERT INTO " + TABLE + "(" + LOGIN + "," + PASS + ")" + "VALUES(?,?)";
        PreparedStatement statement = getConnection().prepareStatement(sqlInsert);
        statement.setString(1, login);
        statement.setString(2, passWord);
        statement.executeUpdate();

    }

    public ResultSet getUser(String login, String pass) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = null;
        String select = "SELECT * FROM " + TABLE + " WHERE " + LOGIN + "=? AND " + PASS + "=?";
        PreparedStatement statement = getConnection().prepareStatement(select);
        statement.setString(1, login);
        statement.setString(2, pass);
        resultSet = statement.executeQuery();
        return resultSet;
    }
    
    public void changeNick(String oldLog, String newLog) throws SQLException, ClassNotFoundException {
        String sqlUpdate = "UPDATE " + TABLE + " SET " + LOGIN + " = ?"
                + "WHERE " + LOGIN + " = ?";
        PreparedStatement statement = getConnection().prepareStatement(sqlUpdate);
        statement.setString(1, newLog);
        statement.setString(2, oldLog);
        statement.executeUpdate();
        // TODO: 21.12.2020
    }
}
