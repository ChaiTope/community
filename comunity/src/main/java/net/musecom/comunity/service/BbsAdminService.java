package net.musecom.comunity.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.musecom.comunity.mapper.BbsAdminMapper;
import net.musecom.comunity.mapper.BbsCategoryMapper;
import net.musecom.comunity.model.BbsAdmin;
import net.musecom.comunity.model.BbsCategory;

@Service
public class BbsAdminService {

	@Autowired
	private BbsAdminMapper bbsAdminMapper;
	
	@Autowired
	private BbsCategoryMapper bbsCategoryMapper;
	
	public int editBbsAdmin(BbsAdmin bbsAdmin) {
		return bbsAdminMapper.updateBbsAdmin(bbsAdmin);
	}
	
	public List<BbsAdmin> getAllBbsList(){
		return bbsAdminMapper.selectList();
	}
	
	public List<BbsCategory> getBbsCategoryById(int id) {
		return bbsAdminMapper.selectCategoryByBbsId(id);
	}
	
	public int bbsCategoryDelete(int categoryId) {
		return bbsCategoryMapper.deleteCategory(categoryId);
	}
	
	public int bbsCategoryUpdate(BbsCategory category) {
	    return bbsCategoryMapper.updateCategory(category);	
	}
	
	public int bbsCategoryInsert(BbsCategory category) {
	    return bbsCategoryMapper.insertCategory(category);	
	}
}
