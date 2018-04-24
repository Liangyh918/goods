package cn.itcast.goods.cart.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.cart.domain.CartItem;
import cn.itcast.goods.cart.service.CartItemService;
import cn.itcast.goods.user.domain.User;
import cn.itcast.servlet.BaseServlet;


public class CartItemServlet extends BaseServlet {
	private CartItemService cartItemService = new CartItemService();

	public String myCart(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
		List<CartItem> cartItemList = new ArrayList<CartItem>();
		User user = (User) req.getSession().getAttribute("sessionUser");
		cartItemList = cartItemService.findCartItemByUser(user.getUid());
		req.setAttribute("cartItemList", cartItemList);
		return "f:/jsps/cart/list.jsp";
	}

	public String add(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
		/*
		 * 封装表单书据到CartItem对象
		 */
		Map map = req.getParameterMap();
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		User user = (User) req.getSession().getAttribute("sessionUser");
		if (req.getSession().getAttribute("sessionUser") == null) {
			System.out.println("sessuibUser is null");

		}
		cartItem.setBook(book);
		cartItem.setUser(user);

		// 添加条目
		cartItemService.add(cartItem);

		// 转发到购物车页面
		return myCart(req, res);
	}

	/**
	 * 批量删除
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public String batchDelete(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
		String cartItemIds = req.getParameter("cartItemIds");
		cartItemService.batchDelete(cartItemIds);
		return myCart(req, res);
	}

	public String findByCartItemId(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
		String cartItemId = req.getParameter("cartItemId");
		cartItemService.findByCartItemId(cartItemId);
		return myCart(req, res);
	}

	public String updateQuantity(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
		String cartItemId = req.getParameter("cartItemId");
		System.out.println("quantity:"+req.getParameter("quantity"));
		int quantity = Integer.parseInt(req.getParameter("quantity"));
		CartItem cartItem = cartItemService.updateQuantity(cartItemId, quantity);
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
		sb.append(",");
		sb.append("\"subtotal\"").append(":").append(cartItem.getSubTotal());
		sb.append("}");

		res.getWriter().print(sb);
		return null;
	}
	
	/**
	 * 在提交订单界面加载在购物车界面勾选的item
	 * @param cartItemIds
	 * @return
	 */
	public String loadCartItems(HttpServletRequest req, HttpServletResponse res){
		
		String cartItemIds = req.getParameter("cartItemIds");
		System.out.println("cartItemIds:"+cartItemIds);
		String total = req.getParameter("total");
		List<CartItem> cartItemList = new ArrayList<CartItem>();
		cartItemList = cartItemService.loadCartItems(cartItemIds);
		for (Iterator iterator = cartItemList.iterator(); iterator.hasNext();) {
			CartItem cartItem = (CartItem) iterator.next();
			System.out.println(1);
			
		}
		req.setAttribute("cartItemIds", cartItemIds);
		req.setAttribute("cartItemList",cartItemList);
		req.setAttribute("total",total);
		return "f:/jsps/cart/showitem.jsp";
	}

}
