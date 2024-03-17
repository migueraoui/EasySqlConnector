package easysqlconnector;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyDataBaseManager {
    private static Connection connection;

    private MyDataBaseManager() {}

    public static Connection getMyConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String urlDatabase = "jdbc:mysql://localhost:3306/databasename"; // Change database name
                String usernameDatabase = "username"; //Change user name
                String passwordDatabase = "password"; // Change user password
                connection = DriverManager.getConnection(urlDatabase, usernameDatabase, passwordDatabase);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(MyDataBaseManager.class.getName()).log(Level.SEVERE, "Error establishing connection", ex);
            }
        }
        return connection;
    }

    public static ResultSet executeMyQuery(String query) {
        ResultSet resultSet = null;
        try {
            Statement statement = getMyConnection().createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(MyDataBaseManager.class.getName()).log(Level.SEVERE, "Error executing query", ex);
        } finally {
            closeConnection();
        }
        return resultSet;
    }

    public static int updateMyQuery(String query) {
        int rows = -1;
        try {
            PreparedStatement preparedStatement = getMyConnection().prepareStatement(query);
            rows = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MyDataBaseManager.class.getName()).log(Level.SEVERE, "Error executing update", ex);
        } finally {
            closeConnection();
        }
        return rows;
    }
    
    public static ResultSet searchMyRecords(String searchQuery) {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = getMyConnection().prepareStatement(searchQuery);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(MyDataBaseManager.class.getName()).log(Level.SEVERE, "Error searching records", ex);
        }
        return resultSet;
    }


    private static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(MyDataBaseManager.class.getName()).log(Level.SEVERE, "Error closing connection", ex);
            } finally {
                connection = null; // Reset the connection reference
            }
        }
    }
}
