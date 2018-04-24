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
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String allOrders(HttpServletRequest req, HttpServletResponse res) {

		/*
		 * 1.得到pc：如果页面传递，使用页面的，否则置为1
		 */
		int pc = getPc(req);
		/*
		 * 2.得到url:
		 */
		String url = getUrl(req);
		/*
		 * 3.使用pc和cid调用service#findPageBean<T>得到PageBean
		 */
		PageBean<Order> pagebean = orderService.allOrders(pc);
		/*
		 * 4.给PageBean设置url，保存PageBean,转发到/jsp/book/list.jsp
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
		 * 1.得到pc：如果页面传递，使用页面的，否则置为1
		 */
		int pc = getPc(req);
		/*
		 * 2.得到url:
		 */
		String url = getUrl(req);
		/*
		 * 3.获取status参数
		 */
		int status  = Integer.parseInt(req.getParameter("status"));
		
		/*
		 * 4.使用pc和cid调用service#findPageBean<T>得到PageBean
		 */
		PageBean<Order> pagebean = orderService.findByStatus(status,pc);
		/*
		 * 5.给PageBean设置url，保存PageBean,转发到/jsp/book/list.jsp
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
		 * 校验订单状态
		 */
		
		int status = orderService.findStatus(oid);
		if(status!=1){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，不能取消！");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 5);
		req.setAttribute("code", "success");
		req.setAttribute("msg", "您的订单已取消！");
		return "f:/adminjsps/msg.jsp";
	}
	
	public String deliver(HttpServletRequest req,HttpServletResponse res) throws ServletException
	,IOException{
		String oid = req.getParameter("oid");
		/*
		 * 校验订单状态
		 */
		
		int status = orderService.findStatus(oid);
		if(status!=2){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，不能发货！");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 3);
		req.setAttribute("code", "success");
		req.setAttribute("msg", "您的订单已发货！");
		return "f:/adminjsps/msg.jsp";
	}
	
	
}
