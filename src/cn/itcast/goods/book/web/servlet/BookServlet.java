package cn.itcast.goods.book.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.servlet.BaseServlet;

public class BookServlet extends BaseServlet {

	private BookService bookService = new BookService();

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
		return "f:/jsps/book/list.jsp";
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
		return "f:/jsps/book/list.jsp";
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
		return "f:/jsps/book/list.jsp";
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
		return "f:/jsps/book/list.jsp";
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
		return "f:/jsps/book/list.jsp";
	}

	public String load(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		req.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}

}
