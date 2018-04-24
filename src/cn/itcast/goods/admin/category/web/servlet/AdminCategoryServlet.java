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
	 * 1.��װ�����ݵ�Category��
	 * 2.����service��add()����������
	 * 3.����findAll();����list.jsp��ʾ���з���
	 */
	Category parent = CommonUtils.toBean(req.getParameterMap(), Category.class);
	parent.setCid(CommonUtils.uuid());//����cid
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
	  String pid = req.getParameter("pid");//��ǰ����ĸ�����id
	  List<Category> parents = categoryService.findParents();
	  req.setAttribute("pid", pid);
	  req.setAttribute("parents", parents);
	  
	  return "f:/adminjsps/admin/category/add2.jsp";
  }
  
  
  /**
   * ��Ӷ�������ڶ���
   * @param req
   * @param res
   * @return
   * @throws ServletException
   * @throws IOException
   */
  public String addChild(HttpServletRequest req,HttpServletResponse res) 
  		throws ServletException,IOException{
  	/*
  	 * 1.��װ�����ݵ�Category��
  	 * 2.����service��add()����������
  	 * 3.����findAll();����list.jsp��ʾ���з���
  	 */
  	Category child = CommonUtils.toBean(req.getParameterMap(), Category.class);
  	child.setCid(CommonUtils.uuid());//����cid
  	Category parent = new Category();
  	String pid = req.getParameter("pid");
  	parent.setCid(pid);
  	child.setParent(parent);
  	categoryService.add(child);
  	return findAll(req,res);	
  }
  
  /**
   * �޸�һ�������һ��
   * @param req
   * @param res
   * @return
   * @throws ServletException
   * @throws IOException
   */
  public String editParentPre(HttpServletRequest req,HttpServletResponse res) 
	  		throws ServletException,IOException{
	  /*
	   * 1.��ȡ�����е�cid
	   * 2.ʹ��cid����category
	   * 3.����Category
	   * 4.ת����edit.jspҳ����ʾCategory
	   */
	  String cid = req.getParameter("pid");
	  Category parent = categoryService.load(cid);
	  req.setAttribute("parent", parent);
	  return "f:/adminjsps/admin/category/edit.jsp";
	  
  } 
  
  
  /**
   * �޸�һ������ڶ���
   * @param req
   * @param res
   * @return
   * @throws ServletException
   * @throws IOException
   */
  public String editParent(HttpServletRequest req,HttpServletResponse res) 
	  		throws ServletException,IOException{
	  /*
	   * 1.��װ����ݵ�Category��
	   * 2.����service��������޸�
	   * 3.ת����list.jsp��ʾ���з��ࣨreturn findAll();��
	   */
	  Category parent = CommonUtils.toBean(req.getParameterMap(), Category.class);
	  categoryService.edit(parent);
	  return findAll(req, res); 
}
  
  /**
   * �޸Ķ��������һ��
   * @param req
   * @param res
   * @return
   * @throws ServletException
   * @throws IOException
   */
  public String editChildPre(HttpServletRequest req,HttpServletResponse res) 
	  		throws ServletException,IOException{
	  /*
	   * 1.��ȡ�����е�cid
	   * 2.ʹ��cid����category
	   * 3.����Category
	   * 4.ת����edit.jspҳ����ʾCategory
	   */
	  String cid = req.getParameter("cid");
	  Category child = categoryService.load(cid);
	  req.setAttribute("child", child);
	  req.setAttribute("parents", categoryService.findParents());
	  return "f:/adminjsps/admin/category/edit2.jsp";
	  
  } 
  
  
  /**
   * �޸Ķ�������ڶ���
   * @param req
   * @param res
   * @return
   * @throws ServletException
   * @throws IOException
   */
  public String editChild(HttpServletRequest req,HttpServletResponse res) 
	  		throws ServletException,IOException{
	  /*
	   * 1.��װ��������Category child
	   * 2.�ѱ��е�pid��װ��child,...
	   * 3.����service.edit()����޸�
	   * 4.���ص�list.jsp
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
	   * 1.��ȡ���Ӳ���cid,����һ��1�������id
	   * 2.ͨ��cid���鿴�ø��������ӷ���ĸ���
	   * 3.��������㣬˵�������ӷ��࣬����ɾ�������������Ϣ��ת����msg.jsp
	   * 4.��������㣬ɾ��֮�����ص�list.jsp
	   */
	  String cid = req.getParameter("cid");
	  int cnt= categoryService.findChildrenCountByParent(cid);
	  if(cnt>0){
		  req.setAttribute("msg", "�÷����»������࣬����ɾ��");
		  return "f:/adminjsps/msg.jsp";
	  }else{
		  categoryService.delete(cid);
		  return findAll(req, res);
	  }
	  
  }
  
  
  public String deleteChild(HttpServletRequest req,HttpServletResponse res) 
	  		throws ServletException,IOException{
	  /*
	   * 1.��ȡcid�������������id
	   * 2.��ȡ����������ͼ��ĸ���
	   * 3.��������㣬���������Ϣ��ת����msg.jsp
	   * 4.��������㣬ɾ��֮�����ص�list.jsp
	   */
	  String cid = req.getParameter("cid");
	  int cnt = bookService.findBookCountByCategory(cid);
	  if(cnt > 0){
		  req.setAttribute("msg", "�÷����»�����ͼ�飬����ɾ��");
		  return "f:/adminjsps/msg.jsp";
	  }
	  else{
		  categoryService.delete(cid);
		  return findAll(req, res);
	  }
  }

}
