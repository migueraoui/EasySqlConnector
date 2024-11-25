package easysqlconnector;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    private final ConnectionProvider connectionProvider;

    public DatabaseManager(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }
    
    public ResultSet executeQuery(String query) {
        try (Connection connection = connectionProvider.getConnection();
             Statement statement = connection.createStatement()) {
            return statement.executeQuery(query);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error executing query: " + query, ex);
            return null;
        }
    }
    
    public int executeUpdate(String query, Object... params) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = prepareStatement(connection, query, params)) {
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error executing update: " + query, ex);
            return -1;
        }
    }

    private PreparedStatement prepareStatement(Connection connection, String query, Object... params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement;
    }
}
