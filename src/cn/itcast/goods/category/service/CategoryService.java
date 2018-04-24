package cn.itcast.goods.category.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.goods.category.dao.CategoryDao;
import cn.itcast.goods.category.domain.Category;
import net.sf.json.JSONArray;

/**
 * 分类模块的业务层
 * 
 * @author liangyh
 *
 */
public class CategoryService {
	private CategoryDao categoryDao =  new CategoryDao();

	public List<Category> findAllCategory() {
		List<Category> allCategorys = null;
		try {
			allCategorys = categoryDao.findAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return allCategorys;
	}
	
	public JSONArray listToJsonArray(){
		JSONArray categoryList = JSONArray.fromObject(findAllCategory());
		return categoryList;
	}
	
	/**
	 * 添加分类
	 * @param category
	 */
	public void add(Category category){
		try{
			categoryDao.add(category);
		} catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<Category> findParents() {
		List<Category> allCategorys = null;
		try {
			return categoryDao.findParents();
			 
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}		
	}
	
	
	/**
	 * 修改分类
	 * @param cid
	 * @return
	 */
	public Category load(String cid){
		try {
			return categoryDao.load(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	/**
	 * 修改分类
	 * @param category
	 */
	public void edit(Category category){
		try {
			categoryDao.edit(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 查询一级分类下有多少个二级分类
	 * @param pid
	 */
	public int findChildrenCountByParent(String pid){
		try {
			return categoryDao.findChildrenCountByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 删除二级分类
	 * @param cid
	 */
	public void delete(String cid){
		try {
			categoryDao.delete(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 通过父分类查找子分类
	 * @param pid
	 * @return
	 */
	public List<Category> findChildren(String pid){
		try {
			return categoryDao.findByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	} 
}
