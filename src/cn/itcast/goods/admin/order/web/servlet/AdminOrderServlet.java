package cn.itcast.goods.admin.order.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.order.service.OrderService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.user.domain.User;
import cn.itcast.servlet.BaseServlet;

public class AdminOrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();
	

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
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String allOrders(HttpServletRequest req, HttpServletResponse res) {

		/*
		 * 1.�õ�pc�����ҳ�洫�ݣ�ʹ��ҳ��ģ�������Ϊ1
		 */
		int pc = getPc(req);
		/*
		 * 2.�õ�url:
		 */
		String url = getUrl(req);
		/*
		 * 3.ʹ��pc��cid����service#findPageBean<T>�õ�PageBean
		 */
		PageBean<Order> pagebean = orderService.allOrders(pc);
		/*
		 * 4.��PageBean����url������PageBean,ת����/jsp/book/list.jsp
		 */
		pagebean.setUrl(url);
		req.setAttribute("pagebean", pagebean);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	
	/**
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String findByStatus(HttpServletRequest req, HttpServletResponse res) {

		/*
		 * 1.�õ�pc�����ҳ�洫�ݣ�ʹ��ҳ��ģ�������Ϊ1
		 */
		int pc = getPc(req);
		/*
		 * 2.�õ�url:
		 */
		String url = getUrl(req);
		/*
		 * 3.��ȡstatus����
		 */
		int status  = Integer.parseInt(req.getParameter("status"));
		
		/*
		 * 4.ʹ��pc��cid����service#findPageBean<T>�õ�PageBean
		 */
		PageBean<Order> pagebean = orderService.findByStatus(status,pc);
		/*
		 * 5.��PageBean����url������PageBean,ת����/jsp/book/list.jsp
		 */
		pagebean.setUrl(url);
		req.setAttribute("pagebean", pagebean);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	/**
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req,HttpServletResponse res) throws ServletException
	,IOException{
		String oid = req.getParameter("oid");
		Order order = orderService.load(oid);
		req.setAttribute("order", order);
		String btn = req.getParameter("btn");
		req.setAttribute("btn", btn);
		return "f:/adminjsps/admin/order/desc.jsp";
	}
	
	public String cancel(HttpServletRequest req,HttpServletResponse res) throws ServletException
	,IOException{
		String oid = req.getParameter("oid");
		/*
		 * У�鶩��״̬
		 */
		
		int status = orderService.findStatus(oid);
		if(status!=1){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "״̬���ԣ�����ȡ����");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 5);
		req.setAttribute("code", "success");
		req.setAttribute("msg", "���Ķ�����ȡ����");
		return "f:/adminjsps/msg.jsp";
	}
	
	public String deliver(HttpServletRequest req,HttpServletResponse res) throws ServletException
	,IOException{
		String oid = req.getParameter("oid");
		/*
		 * У�鶩��״̬
		 */
		
		int status = orderService.findStatus(oid);
		if(status!=2){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "״̬���ԣ����ܷ�����");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 3);
		req.setAttribute("code", "success");
		req.setAttribute("msg", "���Ķ����ѷ�����");
		return "f:/adminjsps/msg.jsp";
	}
	
	
}
