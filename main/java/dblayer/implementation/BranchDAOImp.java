package dblayer.implementation;

import dblayer.connect.DBConnection;
import dblayer.dao.BranchDAO;
import dblayer.model.Branch;
import util.CustomException;
import util.Helper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchDAOImp implements BranchDAO {

    @Override
    public void insert(Branch branch) throws CustomException {
        Helper.checkNullValues(branch);
        String insertSql = "INSERT INTO branch (ifsc_code, contact_number, name, address, created_at, performed_by) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, branch.getIfscCode());
            preparedStatement.setString(2, branch.getContactNumber());
            preparedStatement.setString(3, branch.getName());
            preparedStatement.setString(4, branch.getAddress());
            preparedStatement.setLong(5, branch.getCreatedAt());
            preparedStatement.setLong(6, branch.getPerformedBy());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                branch.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public Branch get(Long id) throws CustomException {
        Helper.checkNullValues(id);
        String selectSql = "SELECT * FROM branch WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return convertRStoBranch(resultSet);
            } else {
                throw new CustomException("Branch not found with ID: " + id);
            }
        } catch (SQLException e) {
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public <T> void update(Long id, Long performerID, String column, T value) throws CustomException {
        Helper.checkNullValues(id);
        Helper.checkNullValues(column);
        Helper.checkNullValues(value);
        String updateSql = "UPDATE branch SET " + column + " = ? WHERE id = ?";
        String updateTimeSql = "UPDATE branch SET modified_at = ?, performed_by = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSql);
             PreparedStatement preparedStatement2 = connection.prepareStatement(updateTimeSql)) {
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
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();

            preparedStatement2.setLong(1, System.currentTimeMillis());
            preparedStatement2.setLong(2, performerID);
            preparedStatement2.setLong(3, id);
            preparedStatement2.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws CustomException {
        Helper.checkNullValues(id);
        String deleteSql = "DELETE FROM branch WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public List<Branch> findAll() throws CustomException {
        List<Branch> branches = new ArrayList<>();
        String selectAllSql = "SELECT * FROM branch";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectAllSql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                branches.add(Helper.mapResultSetToObject(resultSet, Branch.class));
            }
        } catch (SQLException e) {
            throw new CustomException(e.getMessage());
        }
        return branches;
    }
    
}