package cn.itcast.goods.admin.category.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.category.service.CategoryService;
import cn.itcast.servlet.BaseServlet;

public class AdminCategoryServlet extends BaseServlet{
CategoryService categoryService  = new CategoryService();
BookService bookService = new BookService();


public String findAll(HttpServletRequest req,HttpServletResponse res) throws 
ServletException,IOException{
	req.setAttribute("parents", categoryService.findAllCategory());
	return "f:/adminjsps/admin/category/list.jsp";
}

/**
 * 
 * @param req
 * @param res
 * @return
 * @throws ServletException
 * @throws IOException
 */
public String addParent(HttpServletRequest req,HttpServletResponse res) 
		throws ServletException,IOException{
	/*
	 * 1.封装表单数据到Category中
	 * 2.调用service的add()方法完成添加
	 * 3.调用findAll();返回list.jsp显示所有分类
	 */
	Category parent = CommonUtils.toBean(req.getParameterMap(), Category.class);
	parent.setCid(CommonUtils.uuid());//设置cid
	categoryService.add(parent);
	return findAll(req,res);	
}

 /**
  * 
  * @param req
  * @param res
  * @return
  * @throws ServletException
  * @throws IOException
  */
  public String addChildPre(HttpServletRequest req,HttpServletResponse res)
  throws ServletException,IOException{
	  String pid = req.getParameter("pid");//当前点击的父分类id
	  List<Category> parents = categoryService.findParents();
	  req.setAttribute("pid", pid);
	  req.setAttribute("parents", parents);
	  
	  return "f:/adminjsps/admin/category/add2.jsp";
  }
  
  
  /**
   * 添加二级分类第二步
   * @param req
   * @param res
   * @return
   * @throws ServletException
   * @throws IOException
   */
  public String addChild(HttpServletRequest req,HttpServletResponse res) 
  		throws ServletException,IOException{
  	/*
  	 * 1.封装表单数据到Category中
  	 * 2.调用service的add()方法完成添加
  	 * 3.调用findAll();返回list.jsp显示所有分类
  	 */
  	Category child = CommonUtils.toBean(req.getParameterMap(), Category.class);
  	child.setCid(CommonUtils.uuid());//设置cid
  	Category parent = new Category();
  	String pid = req.getParameter("pid");
  	parent.setCid(pid);
  	child.setParent(parent);
  	categoryService.add(child);
  	return findAll(req,res);	
  }
  
  /**
   * 修改一级分类第一步
   * @param req
   * @param res
   * @return
   * @throws ServletException
   * @throws IOException
   */
  public String editParentPre(HttpServletRequest req,HttpServletResponse res) 
	  		throws ServletException,IOException{
	  /*
	   * 1.获取链接中的cid
	   * 2.使用cid加载category
	   * 3.保存Category
	   * 4.转发到edit.jsp页面显示Category
	   */
	  String cid = req.getParameter("pid");
	  Category parent = categoryService.load(cid);
	  req.setAttribute("parent", parent);
	  return "f:/adminjsps/admin/category/edit.jsp";
	  
  } 
  
  
  /**
   * 修改一级分类第二步
   * @param req
   * @param res
   * @return
   * @throws ServletException
   * @throws IOException
   */
  public String editParent(HttpServletRequest req,HttpServletResponse res) 
	  		throws ServletException,IOException{
	  /*
	   * 1.封装表单书据到Category中
	   * 2.调用service方法完成修改
	   * 3.转发到list.jsp显示所有分类（return findAll();）
	   */
	  Category parent = CommonUtils.toBean(req.getParameterMap(), Category.class);
	  categoryService.edit(parent);
	  return findAll(req, res); 
}
  
  /**
   * 修改二级分类第一步
   * @param req
   * @param res
   * @return
   * @throws ServletException
   * @throws IOException
   */
  public String editChildPre(HttpServletRequest req,HttpServletResponse res) 
	  		throws ServletException,IOException{
	  /*
	   * 1.获取链接中的cid
	   * 2.使用cid加载category
	   * 3.保存Category
	   * 4.转发到edit.jsp页面显示Category
	   */
	  String cid = req.getParameter("cid");
	  Category child = categoryService.load(cid);
	  req.setAttribute("child", child);
	  req.setAttribute("parents", categoryService.findParents());
	  return "f:/adminjsps/admin/category/edit2.jsp";
	  
  } 
  
  
  /**
   * 修改二级分类第二步
   * @param req
   * @param res
   * @return
   * @throws ServletException
   * @throws IOException
   */
  public String editChild(HttpServletRequest req,HttpServletResponse res) 
	  		throws ServletException,IOException{
	  /*
	   * 1.封装表单参数到Category child
	   * 2.把表单中的pid封装到child,...
	   * 3.调用service.edit()完成修改
	   * 4.返回到list.jsp
	   */
	  Category child = CommonUtils.toBean(req.getParameterMap(), Category.class);
	  String pid = req.getParameter("pid");
	  Category parent = new Category();
	  parent.setCid(pid);
	  child.setParent(parent);
	  categoryService.edit(child);
	  return findAll(req, res); 
}
  
  public String deleteParent(HttpServletRequest req,HttpServletResponse res) 
	  		throws ServletException,IOException{
	  /*
	   * 1.获取链接参数cid,他是一个1级分类的id
	   * 2.通过cid，查看该付分类下子分类的个数
	   * 3.如果大于零，说明还有子分类，不能删除。保存错误信息，转发到msg.jsp
	   * 4.如果等于零，删除之，返回到list.jsp
	   */
	  String cid = req.getParameter("cid");
	  int cnt= categoryService.findChildrenCountByParent(cid);
	  if(cnt>0){
		  req.setAttribute("msg", "该分类下还有子类，不能删除");
		  return "f:/adminjsps/msg.jsp";
	  }else{
		  categoryService.delete(cid);
		  return findAll(req, res);
	  }
	  
  }
  
  
  public String deleteChild(HttpServletRequest req,HttpServletResponse res) 
	  		throws ServletException,IOException{
	  /*
	   * 1.获取cid，即二级分类的id
	   * 2.获取二级分类下图书的个数
	   * 3.如果大于零，保存错误信息，转发到msg.jsp
	   * 4.如果等于零，删除之，返回到list.jsp
	   */
	  String cid = req.getParameter("cid");
	  int cnt = bookService.findBookCountByCategory(cid);
	  if(cnt > 0){
		  req.setAttribute("msg", "该分类下还存在图书，不能删除");
		  return "f:/adminjsps/msg.jsp";
	  }
	  else{
		  categoryService.delete(cid);
		  return findAll(req, res);
	  }
  }

}
