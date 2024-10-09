package dblayer.implementation;

import dblayer.connect.DBConnection;
import dblayer.dao.BranchDAO;
import dblayer.model.Branch;
import util.CustomException;
import util.Helper;
import util.SQLHelper;

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
    public Branch get(String[] selectColumns, String[] conditionalColumns, Object[] values) throws CustomException {
    	return SQLHelper.get("branch", selectColumns, conditionalColumns, values, Branch.class);
    }

    @Override
    public void update(String[] columns, Object[] values) throws CustomException {
    	SQLHelper.update("branch", columns, values);
    }

    @Override
    public void delete(String[] columns, Object[] values) throws CustomException {
    	SQLHelper.delete("branch", columns, values);
    }

    @Override
    public List<Branch> findAll() throws CustomException {
        List<Branch> branches = new ArrayList<>();
        String selectAllSql = "SELECT * FROM branch";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectAllSql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                branches.add(SQLHelper.mapResultSetToObject(resultSet, Branch.class, "branch"));
            }
        } catch (SQLException e) {
            throw new CustomException(e.getMessage());
        }
        return branches;
    }
    
}