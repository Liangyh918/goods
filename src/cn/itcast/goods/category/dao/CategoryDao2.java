package cn.itcast.goods.category.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.jdbc.TxQueryRunner;

public class CategoryDao2 {
	QueryRunner qr = new TxQueryRunner();

	public List<Category> findAllCategory() throws SQLException {
		List<Category> categorys;
		String sql = "select * from t_category where pid is null or pid = ''";

			List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
			categorys = toCategoryList(mapList);
			// TODO Auto-generated catch block
		return categorys;
	}

	public Category toCategory(Map map) throws SQLException {
		Category category = CommonUtils.toBean(map, Category.class);
		Category parent = new Category();
		String pid = (String)map.get("pid");
		parent.setCid(pid);
		List<Category> children = findChildrenCategorys(category.getCid());
		category.setParent(parent);
		category.setChildren(children);
		return category;
	}

	List<Category> toCategoryList(List<Map<String, Object>> mapList) throws SQLException {
		List<Category> categoryList = null;
		Category category;
		for (Map map : mapList) {
			category = toCategory(map);
			categoryList.add(category);
		}
		return categoryList;
	}

	List<Category> findChildrenCategorys(String ParentId) throws SQLException {
		List<Category> childrenCategorys = null;
		Category category = null;
		String sql = "select * from t_category where pid=?";
			List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), ParentId);
			childrenCategorys=toCategoryList(mapList);
			
		return childrenCategorys;
	}

	public void traverse(List<Category> list) {
		for (Category category : list) {
			System.out.println(category.getCname());
			if (!category.getChildren().isEmpty()) {
				traverse(list);
			}
		}
	}

	/*public static void main(String[] args) {
		CategoryDao categoryDao = new CategoryDao();
	List<Category> allCategory = categoryDao.findAllCategory();
	categoryDao.traverse(allCategory);
	}*/
}
