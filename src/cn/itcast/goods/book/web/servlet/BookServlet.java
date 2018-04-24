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
		return "f:/jsps/book/list.jsp";
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
		return "f:/jsps/book/list.jsp";
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
		return "f:/jsps/book/list.jsp";
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
		return "f:/jsps/book/list.jsp";
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
		return "f:/jsps/book/list.jsp";
	}

	public String load(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		req.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}

}
