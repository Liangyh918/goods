package cn.itcast.goods.order.web.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.cart.domain.CartItem;
import cn.itcast.goods.cart.service.CartItemService;
import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.order.domain.OrderItem;
import cn.itcast.goods.order.service.OrderService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.user.domain.User;
import cn.itcast.servlet.BaseServlet;

public class OrderServlet extends BaseServlet {
	OrderService orderService = new OrderService();
	CartItemService cartItemService = new CartItemService();

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
	public String myOrders(HttpServletRequest req, HttpServletResponse res) {

		System.out.println("order");
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
		User user = (User) req.getSession().getAttribute("sessionUser");

		/*
		 * 4.使用pc和cid调用service#findPageBean<T>得到PageBean
		 */
		PageBean<Order> pagebean = orderService.myOrders(user.getUid(), pc);
		/*
		 * 5.给PageBean设置url，保存PageBean,转发到/jsp/book/list.jsp
		 */
		pagebean.setUrl(url);
		req.setAttribute("pagebean", pagebean);
		return "f:/jsps/order/list.jsp";
	}

	public String createOrder(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		/*
		 * 1.获取所有购物车条目的id，查询之
		 */
		String cartItemIds = req.getParameter("cartItemIds");
		List<CartItem> cartItemList = cartItemService.loadCartItems(cartItemIds);
		/*
		 * 2.创建Order
		 */
		Order order = new Order();
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(String.format("%tF %<tT", new Date()));// 下单时间
		order.setStatus(1);
		order.setAddress(req.getParameter("address"));
		User owner = (User) req.getSession().getAttribute("sessionUser");
		order.setOwner(owner);

		BigDecimal total = new BigDecimal("0");
		for (CartItem cartItem : cartItemList) {
			total = total.add(new BigDecimal(cartItem.getSubTotal() + ""));
		}
		order.setTotal(total.doubleValue());

		/*
		 * 3.创建List<OrderItem> 一个CartItem对应一个OrderItem
		 */
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for (CartItem cartItem : cartItemList) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderItemId(CommonUtils.uuid());
			orderItem.setBook(cartItem.getBook());
			orderItem.setCurrPrice(cartItem.getBook().getCurrPrice());
			orderItem.setOrder(order);
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setSubtotal(cartItem.getSubTotal());
			orderItemList.add(orderItem);
		}
		order.setOrderItemList(orderItemList);

		/*
		 * 4.调用service完成添加
		 */
		orderService.createOrder(order);
		cartItemService.batchDelete(cartItemIds);
		/*
		 * 5.保存订单，转发到ordersucc.jsp
		 */
		req.setAttribute("order", order);
		return "f:/jsps/order/ordersucc.jsp";

	}
	
	public String load(HttpServletRequest req,HttpServletResponse res) throws ServletException
	,IOException{
		String oid = req.getParameter("oid");
		Order order = orderService.load(oid);
		req.setAttribute("order", order);
		String btn = req.getParameter("btn");
		req.setAttribute("btn", btn);
		return "f:/jsps/order/desc.jsp";
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
			return "f:/jsps/msg.jsp";
		}
		orderService.updateStatus(oid, 5);
		req.setAttribute("code", "success");
		req.setAttribute("msg", "您的订单已取消！");
		return "f:/jsps/msg.jsp";
	}
	
	public String confirm(HttpServletRequest req,HttpServletResponse res) throws ServletException
	,IOException{
		String oid = req.getParameter("oid");
		/*
		 * 校验订单状态
		 */
		
		int status = orderService.findStatus(oid);
		if(status!=3){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，不能确认收货！");
			return "f:/jsps/msg.jsp";
		}
		orderService.updateStatus(oid, 4);
		req.setAttribute("code", "success");
		req.setAttribute("msg", "交易成功！");
		return "f:/jsps/msg.jsp";
	}

	public String paymentPre(HttpServletRequest req,HttpServletResponse res) throws ServletException
	,IOException{
		req.setAttribute("order", orderService.load(req.getParameter("oid")));
		return "f:/jsps/order/pay.jsp";
	}
	
	public String payment(HttpServletRequest req,HttpServletResponse res) throws ServletException
	,IOException{
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));
		
		/*
		 * 1.准备13个参数
		 */
		String p0_Cmd = "Buy";//业务类型，固定值
		String p1_MerId = props.getProperty("p1_MerId");//商品编码，在易宝的唯一标识
		String p2_Order = req.getParameter("oid");//订单编号
		String p3_Amt = "0.01";//支付金额
		String p4_Cur = "CNY";//交易币种，固定值CNY
		String p5_Pid = "";//商品名称
		String p6_Pcat = "";//商品种类
		String p7_Pdesc = "";//商品描述
		String p8_Url = props.getProperty("p8_Url");//在支付成功后，一包会访问这个地址。
		String p9_SAF = "";//送货地址
		String pa_MP = "";//扩展信息
		String pd_FrpId = req.getParameter("yh");//支付通道
		String pr_NeedResponse = "1";//应答机制，固定值1
		
		
		/*
		 * 2.计算hmac
		 * 需要13个参数
		 * 需要keyValue
		 * 需要加密算法
		 */
		
		String keyValue = props.getProperty("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc,
				p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
		
		/*
		 * 3.重定向到易宝的支付网关
		 */
		
		StringBuilder sb = new StringBuilder("https://www.yeepay.com/app-merchant-proxy/node");
		sb.append("?").append("p0_Cmd=").append(p0_Cmd);
		sb.append("&").append("p1_MerId").append(p1_MerId);
		sb.append("&").append("p2_Order").append(p2_Order);
		sb.append("&").append("p3_Amt").append(p3_Amt);
		sb.append("&").append("p4_Cur").append(p4_Cur);
		sb.append("&").append("p5_Pid").append(p5_Pid);
		sb.append("&").append("p6_Pcat").append(p6_Pcat);
		sb.append("&").append("p7_Pdesc").append(p7_Pdesc);
		sb.append("&").append("p8_Url").append(p8_Url);
		sb.append("&").append("p9_SAF").append(p9_SAF);
		sb.append("&").append("pa_MP").append(pa_MP);
		sb.append("&").append("pd_FrpId").append(pd_FrpId);
		sb.append("&").append("pr_NeedResponse").append(pr_NeedResponse);
		sb.append("&").append("hmac").append(hmac);
		
		res.sendRedirect(sb.toString());
		
		return null;
	}
	
	/**
	 * 回馈方法
	 * 当支付成功时，易宝会访问这里
	 * 用两种方法访问：
	 * 1.引导用户的浏览器重定向（如果用户关闭了浏览器，就不能访问这里）
	 * 2.易宝的服务器会使用点对点通讯的方法访问这个方法。（必须回馈success,不然易宝服务器会一直调用这个方法）
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String back(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException{
		/*
		 * 1.获取12个参数
		 */
		String p1_MerId = req.getParameter("p1_MerId");
		String r0_Cmd = req.getParameter("r0_Cmd");
		String r1_Code = req.getParameter("r1_Code");
		String r2_TrxId = req.getParameter("r2_TrxId");
		String r3_Amt = req.getParameter("r3_Amt");
		String r4_Cur = req.getParameter("r4_Cur");
		String r5_Pid = req.getParameter("r5_Pid");
		String r6_Order = req.getParameter("r6_Order");
		String r7_Uid = req.getParameter("r7_Uid");
		String r8_MP = req.getParameter("r8_MP");
		String r9_BType = req.getParameter("r9_BType");
		String hmac = req.getParameter("hmac");
		/*
		 * 2.获取keyValue
		 */
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));
		String keyValue = props.getProperty("keyValue");
		
		/*
		 * 3.调用PaymentUtil的校验方法来校验调用者的身份
		 *    >如果校验失败：保存错误信息，转发到msg.jsp
		 *    >如果校验通过： 
		 *         * 判断访问的方法是重定向还是点对点，如果要是重定向
		 *         修改该订单状态，保存成功信息，转发到msg.jsp
		 *         *如果是点对点：修改该订单状态，返回success
		 */
		
		boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, keyValue);
				
		if(!bool){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "支付失败");
		}
		if(r1_Code.equals("1")){
			orderService.updateStatus(r6_Order,2);
			
			if(r1_Code.equals("1")){
				req.setAttribute("code", "success");
				req.setAttribute("msg", "支付成功");
				return "f:/jsps/msg,jsp";	
			}else if(r9_BType.equals("2")){
				res.getWriter().print("success");
			}
			
		}
		return null;
	
	}
	
}
