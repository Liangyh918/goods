package cn.itcast.goods.admin.book.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.category.service.CategoryService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.servlet.BaseServlet;

public class AdminBookServlet extends BaseServlet {
   private BookService bookService = new BookService();
   private CategoryService categoryService = new CategoryService();
   
   
   /**
    * 删除图书
    * 
    */
   public String delete(HttpServletRequest req, HttpServletResponse res) 
		   throws ServletException,IOException{
	   String bid = req.getParameter("bid");
	   /*
	    * 删除图书
	    */
	   Book book = bookService.load(bid);
	   String savepath = this.getServletContext().getRealPath("/");//获取真实的路径
	   new File(savepath,book.getImage_w()).delete();//删除文件
	   new File(savepath,book.getImage_b()).delete();//删除文件
	   
	   bookService.delete(bid);//删除数据库的记录
	   
	   req.setAttribute("msg", "删除图书成功");
	   return "f:/adminjsps/msg.jsp";
	   
   }
   
   /**
    * 编辑图书 
    * @param req
    * @param res
    * @return
    * @throws ServletException
    * @throws IOException
    */
   public String edit(HttpServletRequest req, HttpServletResponse res) 
   throws ServletException,IOException{
	   /*
	    * 
	    */
	   Map map = req.getParameterMap();
	   Book book = CommonUtils.toBean(map, Book.class);
	   Category category = CommonUtils.toBean(map, Category.class);
	   book.setCategory(category);
	   bookService.edit(book);
	   req.setAttribute("msg", "修改图书成功！");
	   return "f:/adminjsps/msg.jsp";
   }
   
   
	public String findCategoryAll(HttpServletRequest req, HttpServletResponse res) {

		List<Category> categoryList = categoryService.findAllCategory();
		/*
		 * for (Category category : categoryList) {
		 * System.out.println(category.getCname()); for(Category child
		 * :category.getChildren()){ System.out.println(child.getCname()); } }
		 */
		req.setAttribute("categoryList", categoryList);
		System.out.println(req.getAttribute("categoryList"));
		return "f:/adminjsps/admin/book/left.jsp";
	}
	
	/**
	 * 获取当前页的页码
	 * 
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req) {
		int pc = 1;
		String param = req.getParameter("pc");
		if (param != null && !param.trim().isEmpty()) {
			try {
				pc = Integer.parseInt(param);
			} catch (RuntimeException e) {

			}
		}
		return pc;
	}

	/**
	 * 截取url,页面中的分页导航中需要使用它作为超链接的目标！
	 * 
	 * @param req
	 * @return
	 */
	/*
	 * http://localhost:8080/goods/BookServlet?method=findByCategory&cid=xxx
	 * 
	 */
	private String getUrl(HttpServletRequest req) {
		String url = req.getRequestURI() + "?" + req.getQueryString();
		int index = url.indexOf("&pc");
		if (index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}

	/**
	 * 按分类查询
	 * @param req
	 * @param res
	 * @return
	 */
	public String findByCategory(HttpServletRequest req, HttpServletResponse res) {
		/*
		 * 1.得到pc：如果页面传递，使用页面的，否则置为1
		 */
		int pc = getPc(req);
		/*
		 * 2.得到url:
		 */
		String url = getUrl(req);
		/*
		 * 3.获取查询条件，本方法就是cid，即分类的id
		 */
		String cid = req.getParameter("cid");// 如果是post参数，也可以这样子吗？
		/*
		 * 4.使用pc和cid调用service#findPageBean<T>ry得到PageBean
		 */
		PageBean<Book> pagebean = bookService.findByCategory(cid, pc);
		/*
		 * 5.给PageBean设置url，保存PageBean,转发到/jsp/book/list.jsp
		 */
		for (Book book : pagebean.getBeanList()) {
			System.out.println(book.getBname());
		}
		pagebean.setUrl(url);
		req.setAttribute("pagebean", pagebean);
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * 按作者查询
	 * @param req
	 * @param res
	 * @return
	 */
	public String findByAuthor(HttpServletRequest req, HttpServletResponse res) {
		/*
		 * 1.得到pc：如果页面传递，使用页面的，否则置为1
		 */
		int pc = getPc(req);
		/*
		 * 2.得到url:
		 */
		String url = getUrl(req);
		/*
		 * 3.获取查询条件，本方法就是cid，即分类的id
		 */
		String author = req.getParameter("author");// 如果是post参数，也可以这样子吗？
		System.out.println("author" + author);
		/*
		 * 4.使用pc和cid调用service#findPageBean<T>ry得到PageBean
		 */
		PageBean<Book> pagebean = bookService.findByAuthor(author, pc);
		/*
		 * 5.给PageBean设置url，保存PageBean,转发到/jsp/book/list.jsp
		 */
		for (Book book : pagebean.getBeanList()) {
			System.out.println(book.getBname());
		}
		pagebean.setUrl(url);
		req.setAttribute("pagebean", pagebean);
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * 按书名查询
	 * @param req
	 * @param res
	 * @return
	 */
	public String findByBname(HttpServletRequest req, HttpServletResponse res) {
		/*
		 * 1.得到pc：如果页面传递，使用页面的，否则置为1
		 */
		int pc = getPc(req);
		/*
		 * 2.得到url:
		 */
		String url = getUrl(req);
		/*
		 * 3.获取查询条件，本方法就是bname，即图书名
		 */
		String bname = req.getParameter("bname");// 如果是post参数，也可以这样子吗？
		/*
		 * 4.使用pc和bname调用service#findPageBean<T>ry得到PageBean
		 */
		PageBean<Book> pagebean = bookService.findByBname(bname, pc);
		/*
		 * 5.给PageBean设置url，保存PageBean,转发到/jsp/book/list.jsp
		 */
		for (Book book : pagebean.getBeanList()) {
			System.out.println(book.getBname());
		}
		pagebean.setUrl(url);
		req.setAttribute("pagebean", pagebean);
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * 按出版社查询
	 * @param req
	 * @param res
	 * @return
	 */
	public String findByPress(HttpServletRequest req, HttpServletResponse res) {
		/*
		 * 1.得到pc：如果页面传递，使用页面的，否则置为1
		 */
		int pc = getPc(req);
		/*
		 * 2.得到url:
		 */
		String url = getUrl(req);
		/*
		 * 3.获取查询条件，本方法就是press，即出版社
		 */
		String press = req.getParameter("press");// 如果是post参数，也可以这样子吗？
		System.out.println("press" + press);
		/*
		 * 4.使用pc和bname调用service#findPageBean<T>ry得到PageBean
		 */
		PageBean<Book> pagebean = bookService.findByPress(press, pc);
		/*
		 * 5.给PageBean设置url，保存PageBean,转发到/jsp/book/list.jsp
		 */
		for (Book book : pagebean.getBeanList()) {
			System.out.println(book.getBname());
		}
		pagebean.setUrl(url);
		req.setAttribute("pagebean", pagebean);
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * 组合查询
	 * @param req
	 * @param res
	 * @return
	 */
	public String findByCombination(HttpServletRequest req, HttpServletResponse res) {
		/*
		 * 1.得到pc：如果页面传递，使用页面的，否则置为1
		 */
		int pc = getPc(req);
		/*
		 * 2.得到url:
		 */
		String url = getUrl(req);
		/*
		 * 3.获取查询条件，本方法就是press，即出版社
		 */
		Book criteria = (Book) CommonUtils.toBean(req.getParameterMap(), Book.class);// 如果是post参数，也可以这样子吗？
		System.out.println("criteria.bname" + criteria.getBname());
		System.out.println("criteria.Author" + criteria.getAuthor());
		System.out.println("criteria.Press" + criteria.getPress());
		/*
		 * 4.使用pc和bname调用service#findPageBean<T>ry得到PageBean
		 */
		PageBean<Book> pagebean = bookService.findByCombination(criteria, pc);
		/*
		 * 5.给PageBean设置url，保存PageBean,转发到/jsp/book/list.jsp
		 */
		for (Book book : pagebean.getBeanList()) {
			System.out.println(book.getBname());
		}
		pagebean.setUrl(url);
		req.setAttribute("pagebean", pagebean);
		return "f:/adminjsps/admin/book/list.jsp";
	}

	public String load(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		/*
		 * 1.获取bid,得到Book对象，保存之
		 */
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		req.setAttribute("book", book);
		/*
		 * 2.获取所有以及分类，保存之
		 */
		req.setAttribute("parents", categoryService.findParents());
		/*
		 * 3.获取当前图书所属的一级分类下所有2级分类
		 */
		String pid = book.getCategory().getParent().getCid();
		req.setAttribute("children", categoryService.findChildren(pid));
		/*
		 * 转发到desc.jsp
		 */
		return "f:/adminjsps/admin/book/desc.jsp";
	}
   
	/**
	 * 添加图书：第一步
	 * @param req
	 * @param res
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addPre(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		List<Category> parents = categoryService.findParents();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/add.jsp";
	}
	
	/**
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxFindChildren(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		/**
		 * 1.获取pid
		 * 2.通过pid查询所有2级分类
		 * 3.把List<Category> 转换成json对象，输出到客户端
		 */
		String pid = req.getParameter("pid");
		List<Category> children = categoryService.findChildren(pid);
		String json = toJson(children);
		res.getWriter().print(json);
		return null;
	}
	
	private String toJson(Category category){
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"cid\"").append(":").append("\"").
		append(category.getCid()).append("\"");
		sb.append(",");
		sb.append("\"cname\"").append(":").append("\"").
		append(category.getCname()).append("\"");
		sb.append("}");
		return sb.toString();
	}
	
	private String toJson(List<Category> categoryList){
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < categoryList.size(); i++) {
			sb.append(toJson(categoryList.get(i)));
			if(i<categoryList.size()-1){
				sb.append(",");
			}
			System.out.println("第"+i+"个");
		}
		sb.append("]");
		System.out.println("json:"+sb.toString());
		return sb.toString();
	}
	
   
	
}
