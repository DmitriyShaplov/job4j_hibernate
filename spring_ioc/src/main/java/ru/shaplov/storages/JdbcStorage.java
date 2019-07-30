package ru.shaplov.storages;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author shaplov
 * @since 30.07.2019
 */
public class JdbcStorage implements Storage {

    private static final Logger LOG = LogManager.getLogger(JdbcStorage.class);
    private static final BasicDataSource SOURCE = new BasicDataSource();

    public JdbcStorage() {
        try (InputStream in = JdbcStorage.class.getClassLoader().getResourceAsStream("app.properties")) {
            if (in == null) {
                throw new IllegalStateException("properties are null");
            }
            Properties config = new Properties();
            config.load(in);
            SOURCE.setUrl(config.getProperty("url"));
            SOURCE.setUsername(config.getProperty("username"));
            SOURCE.setPassword(config.getProperty("password"));
            SOURCE.setDriverClassName(config.getProperty("driver"));
            SOURCE.setMinIdle(Integer.parseInt(config.getProperty("min_idle")));
            SOURCE.setMaxIdle(Integer.parseInt(config.getProperty("max_idle")));
            SOURCE.setMaxOpenPreparedStatements(Integer.parseInt(config.getProperty("max_ps")));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new IllegalStateException("could not get properties");
        }
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
