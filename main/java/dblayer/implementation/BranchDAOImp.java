package dblayer.implementation;

import dblayer.dao.BranchDAO;
import dblayer.model.Branch;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import util.CustomException;
import util.SQLHelper;
import java.util.List;

public class BranchDAOImp implements BranchDAO {

	@Override
	public void insertBranch(Branch obj) throws CustomException {
		SQLHelper.insert("branch", obj);
	}

	@Override
	public List<Branch> getBranch(List<String> columns, List<Criteria> conditions)
			throws CustomException {
		return SQLHelper.get("branch", Branch.class, columns, conditions);
	}

	@Override
	public void updateBranch(List<ColumnCriteria> columnCriteriaList, List<Criteria> conditions)
			throws CustomException {
		SQLHelper.update("branch", columnCriteriaList, conditions);
	}

	@Override
	public void deleteBranch(List<Criteria> conditions) throws CustomException {
		SQLHelper.delete("branch", conditions);
	}
}