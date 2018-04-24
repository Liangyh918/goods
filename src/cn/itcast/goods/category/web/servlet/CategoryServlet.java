package cn.itcast.goods.category.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.category.service.CategoryService;
import cn.itcast.servlet.BaseServlet;
import net.sf.json.JSONArray;

public class CategoryServlet extends BaseServlet {
	private CategoryService categoryService = new CategoryService();

	public String findAll(HttpServletRequest req, HttpServletResponse res) {

		List<Category> categoryList = categoryService.findAllCategory();
		/*
		 * for (Category category : categoryList) {
		 * System.out.println(category.getCname()); for(Category child
		 * :category.getChildren()){ System.out.println(child.getCname()); } }
		 */
		req.setAttribute("categoryList", categoryList);
		System.out.println(req.getAttribute("categoryList"));
		return "f:/jsps/left.jsp";
	}
}
