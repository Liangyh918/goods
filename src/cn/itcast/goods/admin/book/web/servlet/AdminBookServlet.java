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
    * ɾ��ͼ��
    * 
    */
   public String delete(HttpServletRequest req, HttpServletResponse res) 
		   throws ServletException,IOException{
	   String bid = req.getParameter("bid");
	   /*
	    * ɾ��ͼ��
	    */
	   Book book = bookService.load(bid);
	   String savepath = this.getServletContext().getRealPath("/");//��ȡ��ʵ��·��
	   new File(savepath,book.getImage_w()).delete();//ɾ���ļ�
	   new File(savepath,book.getImage_b()).delete();//ɾ���ļ�
	   
	   bookService.delete(bid);//ɾ�����ݿ�ļ�¼
	   
	   req.setAttribute("msg", "ɾ��ͼ��ɹ�");
	   return "f:/adminjsps/msg.jsp";
	   
   }
   
   /**
    * �༭ͼ�� 
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
	   req.setAttribute("msg", "�޸�ͼ��ɹ���");
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
	 * ��ȡ��ǰҳ��ҳ��
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
	 * ��ȡurl,ҳ���еķ�ҳ��������Ҫʹ������Ϊ�����ӵ�Ŀ�꣡
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
	 * �������ѯ
	 * @param req
	 * @param res
	 * @return
	 */
	public String findByCategory(HttpServletRequest req, HttpServletResponse res) {
		/*
		 * 1.�õ�pc�����ҳ�洫�ݣ�ʹ��ҳ��ģ�������Ϊ1
		 */
		int pc = getPc(req);
		/*
		 * 2.�õ�url:
		 */
		String url = getUrl(req);
		/*
		 * 3.��ȡ��ѯ����������������cid���������id
		 */
		String cid = req.getParameter("cid");// �����post������Ҳ������������
		/*
		 * 4.ʹ��pc��cid����service#findPageBean<T>ry�õ�PageBean
		 */
		PageBean<Book> pagebean = bookService.findByCategory(cid, pc);
		/*
		 * 5.��PageBean����url������PageBean,ת����/jsp/book/list.jsp
		 */
		for (Book book : pagebean.getBeanList()) {
			System.out.println(book.getBname());
		}
		pagebean.setUrl(url);
		req.setAttribute("pagebean", pagebean);
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * �����߲�ѯ
	 * @param req
	 * @param res
	 * @return
	 */
	public String findByAuthor(HttpServletRequest req, HttpServletResponse res) {
		/*
		 * 1.�õ�pc�����ҳ�洫�ݣ�ʹ��ҳ��ģ�������Ϊ1
		 */
		int pc = getPc(req);
		/*
		 * 2.�õ�url:
		 */
		String url = getUrl(req);
		/*
		 * 3.��ȡ��ѯ����������������cid���������id
		 */
		String author = req.getParameter("author");// �����post������Ҳ������������
		System.out.println("author" + author);
		/*
		 * 4.ʹ��pc��cid����service#findPageBean<T>ry�õ�PageBean
		 */
		PageBean<Book> pagebean = bookService.findByAuthor(author, pc);
		/*
		 * 5.��PageBean����url������PageBean,ת����/jsp/book/list.jsp
		 */
		for (Book book : pagebean.getBeanList()) {
			System.out.println(book.getBname());
		}
		pagebean.setUrl(url);
		req.setAttribute("pagebean", pagebean);
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * ��������ѯ
	 * @param req
	 * @param res
	 * @return
	 */
	public String findByBname(HttpServletRequest req, HttpServletResponse res) {
		/*
		 * 1.�õ�pc�����ҳ�洫�ݣ�ʹ��ҳ��ģ�������Ϊ1
		 */
		int pc = getPc(req);
		/*
		 * 2.�õ�url:
		 */
		String url = getUrl(req);
		/*
		 * 3.��ȡ��ѯ����������������bname����ͼ����
		 */
		String bname = req.getParameter("bname");// �����post������Ҳ������������
		/*
		 * 4.ʹ��pc��bname����service#findPageBean<T>ry�õ�PageBean
		 */
		PageBean<Book> pagebean = bookService.findByBname(bname, pc);
		/*
		 * 5.��PageBean����url������PageBean,ת����/jsp/book/list.jsp
		 */
		for (Book book : pagebean.getBeanList()) {
			System.out.println(book.getBname());
		}
		pagebean.setUrl(url);
		req.setAttribute("pagebean", pagebean);
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * ���������ѯ
	 * @param req
	 * @param res
	 * @return
	 */
	public String findByPress(HttpServletRequest req, HttpServletResponse res) {
		/*
		 * 1.�õ�pc�����ҳ�洫�ݣ�ʹ��ҳ��ģ�������Ϊ1
		 */
		int pc = getPc(req);
		/*
		 * 2.�õ�url:
		 */
		String url = getUrl(req);
		/*
		 * 3.��ȡ��ѯ����������������press����������
		 */
		String press = req.getParameter("press");// �����post������Ҳ������������
		System.out.println("press" + press);
		/*
		 * 4.ʹ��pc��bname����service#findPageBean<T>ry�õ�PageBean
		 */
		PageBean<Book> pagebean = bookService.findByPress(press, pc);
		/*
		 * 5.��PageBean����url������PageBean,ת����/jsp/book/list.jsp
		 */
		for (Book book : pagebean.getBeanList()) {
			System.out.println(book.getBname());
		}
		pagebean.setUrl(url);
		req.setAttribute("pagebean", pagebean);
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * ��ϲ�ѯ
	 * @param req
	 * @param res
	 * @return
	 */
	public String findByCombination(HttpServletRequest req, HttpServletResponse res) {
		/*
		 * 1.�õ�pc�����ҳ�洫�ݣ�ʹ��ҳ��ģ�������Ϊ1
		 */
		int pc = getPc(req);
		/*
		 * 2.�õ�url:
		 */
		String url = getUrl(req);
		/*
		 * 3.��ȡ��ѯ����������������press����������
		 */
		Book criteria = (Book) CommonUtils.toBean(req.getParameterMap(), Book.class);// �����post������Ҳ������������
		System.out.println("criteria.bname" + criteria.getBname());
		System.out.println("criteria.Author" + criteria.getAuthor());
		System.out.println("criteria.Press" + criteria.getPress());
		/*
		 * 4.ʹ��pc��bname����service#findPageBean<T>ry�õ�PageBean
		 */
		PageBean<Book> pagebean = bookService.findByCombination(criteria, pc);
		/*
		 * 5.��PageBean����url������PageBean,ת����/jsp/book/list.jsp
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
		 * 1.��ȡbid,�õ�Book���󣬱���֮
		 */
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		req.setAttribute("book", book);
		/*
		 * 2.��ȡ�����Լ����࣬����֮
		 */
		req.setAttribute("parents", categoryService.findParents());
		/*
		 * 3.��ȡ��ǰͼ��������һ������������2������
		 */
		String pid = book.getCategory().getParent().getCid();
		req.setAttribute("children", categoryService.findChildren(pid));
		/*
		 * ת����desc.jsp
		 */
		return "f:/adminjsps/admin/book/desc.jsp";
	}
   
	/**
	 * ���ͼ�飺��һ��
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
		 * 1.��ȡpid
		 * 2.ͨ��pid��ѯ����2������
		 * 3.��List<Category> ת����json����������ͻ���
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
			System.out.println("��"+i+"��");
		}
		sb.append("]");
		System.out.println("json:"+sb.toString());
		return sb.toString();
	}
	
   
	
}
