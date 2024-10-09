package dblayer.implementation;

import dblayer.dao.UserDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dblayer.connect.DBConnection;
import dblayer.model.User;
import util.CustomException;
import util.Helper;
import util.SQLHelper;

public class UserDAOImp implements UserDAO {
	
    @Override
    public void insert(User user) throws CustomException {
    	Helper.checkNullValues(user);
    	String INSERT_USER_SQL = "INSERT INTO user (fullname, email, phone, role, username, password, status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setString(1, user.getFullname());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setLong(3, user.getPhone());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.setString(5, user.getUsername());
            preparedStatement.setString(6, user.getPassword());
            preparedStatement.setString(7, user.getStatus());
            preparedStatement.setLong(8, user.getCreatedAt());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
        	throw new CustomException(e.getMessage());
        }
    }

    @Override
    public User get(Long id) throws CustomException {
    	Helper.checkNullValues(id);
        User user = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user = SQLHelper.mapResultSetToObject(rs, User.class, "user");
                user.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
        	throw new CustomException(e.getMessage());
        }
        return user;
    }

    @Override
    public <T> void update(Long id, String column, T value) throws CustomException {
    	Helper.checkNullValues(id);
    	Helper.checkNullValues(column);
    	Helper.checkNullValues(value);
        String updateSql = "UPDATE user SET " + column + " = ?, modified_at = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
            if (value instanceof String) {
                preparedStatement.setString(1, (String) value);
            } else if (value instanceof Long) {
                preparedStatement.setLong(1, (Long) value);
            } else if (value instanceof Integer) {
                preparedStatement.setInt(1, (Integer) value);
            } else if (value instanceof Double) {
                preparedStatement.setDouble(1, (Double) value);
            } else {
                throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
            }
            preparedStatement.setLong(2, System.currentTimeMillis());
            preparedStatement.setLong(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
        	throw new CustomException(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws CustomException {
    	Helper.checkNullValues(id);
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
        	throw new CustomException(e.getMessage());
        }
    }

    @Override
    public List<User> findAll() throws CustomException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user")) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                users.add(SQLHelper.mapResultSetToObject(rs, User.class, "user"));
            }
        } catch (SQLException e) {
        	throw new CustomException(e.getMessage());
        }
        return users;
    }

}
