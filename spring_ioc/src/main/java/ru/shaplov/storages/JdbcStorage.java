package ru.shaplov.storages;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * @author shaplov
 * @since 30.07.2019
 */
public class JdbcStorage implements Storage {

    private static final Logger LOG = LogManager.getLogger(JdbcStorage.class);
    private static final BasicDataSource SOURCE = new BasicDataSource();

    public JdbcStorage(String url, String username, String password,
                       String driver, String minIdle, String maxIdle, String maxPs) {
        SOURCE.setUrl(url);
        SOURCE.setUsername(username);
        SOURCE.setPassword(password);
        SOURCE.setDriverClassName(driver);
        SOURCE.setMinIdle(Integer.parseInt(minIdle));
        SOURCE.setMaxIdle(Integer.parseInt(maxIdle));
        SOURCE.setMaxOpenPreparedStatements(Integer.parseInt(maxPs));
    }

    @Override
    public User add(User user) {
        try (Connection connection = SOURCE.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO demo_users (name) VALUES (?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
                return user;
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        throw new IllegalStateException("Could not add user to database");
    }

    @Override
    public User findById(int id) {
        User result = null;
        try (Connection connection = SOURCE.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM demo_users where id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = new User();
                result.setId(rs.getInt("id"));
                result.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }
}
