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
	public String myOrders(HttpServletRequest req, HttpServletResponse res) {

		System.out.println("order");
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
		User user = (User) req.getSession().getAttribute("sessionUser");

		/*
		 * 4.ʹ��pc��cid����service#findPageBean<T>�õ�PageBean
		 */
		PageBean<Order> pagebean = orderService.myOrders(user.getUid(), pc);
		/*
		 * 5.��PageBean����url������PageBean,ת����/jsp/book/list.jsp
		 */
		pagebean.setUrl(url);
		req.setAttribute("pagebean", pagebean);
		return "f:/jsps/order/list.jsp";
	}

	public String createOrder(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		/*
		 * 1.��ȡ���й��ﳵ��Ŀ��id����ѯ֮
		 */
		String cartItemIds = req.getParameter("cartItemIds");
		List<CartItem> cartItemList = cartItemService.loadCartItems(cartItemIds);
		/*
		 * 2.����Order
		 */
		Order order = new Order();
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(String.format("%tF %<tT", new Date()));// �µ�ʱ��
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
		 * 3.����List<OrderItem> һ��CartItem��Ӧһ��OrderItem
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
		 * 4.����service������
		 */
		orderService.createOrder(order);
		cartItemService.batchDelete(cartItemIds);
		/*
		 * 5.���涩����ת����ordersucc.jsp
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
		 * У�鶩��״̬
		 */
		
		int status = orderService.findStatus(oid);
		if(status!=1){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "״̬���ԣ�����ȡ����");
			return "f:/jsps/msg.jsp";
		}
		orderService.updateStatus(oid, 5);
		req.setAttribute("code", "success");
		req.setAttribute("msg", "���Ķ�����ȡ����");
		return "f:/jsps/msg.jsp";
	}
	
	public String confirm(HttpServletRequest req,HttpServletResponse res) throws ServletException
	,IOException{
		String oid = req.getParameter("oid");
		/*
		 * У�鶩��״̬
		 */
		
		int status = orderService.findStatus(oid);
		if(status!=3){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "״̬���ԣ�����ȷ���ջ���");
			return "f:/jsps/msg.jsp";
		}
		orderService.updateStatus(oid, 4);
		req.setAttribute("code", "success");
		req.setAttribute("msg", "���׳ɹ���");
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
		 * 1.׼��13������
		 */
		String p0_Cmd = "Buy";//ҵ�����ͣ��̶�ֵ
		String p1_MerId = props.getProperty("p1_MerId");//��Ʒ���룬���ױ���Ψһ��ʶ
		String p2_Order = req.getParameter("oid");//�������
		String p3_Amt = "0.01";//֧�����
		String p4_Cur = "CNY";//���ױ��֣��̶�ֵCNY
		String p5_Pid = "";//��Ʒ����
		String p6_Pcat = "";//��Ʒ����
		String p7_Pdesc = "";//��Ʒ����
		String p8_Url = props.getProperty("p8_Url");//��֧���ɹ���һ������������ַ��
		String p9_SAF = "";//�ͻ���ַ
		String pa_MP = "";//��չ��Ϣ
		String pd_FrpId = req.getParameter("yh");//֧��ͨ��
		String pr_NeedResponse = "1";//Ӧ����ƣ��̶�ֵ1
		
		
		/*
		 * 2.����hmac
		 * ��Ҫ13������
		 * ��ҪkeyValue
		 * ��Ҫ�����㷨
		 */
		
		String keyValue = props.getProperty("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc,
				p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
		
		/*
		 * 3.�ض����ױ���֧������
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
	 * ��������
	 * ��֧���ɹ�ʱ���ױ����������
	 * �����ַ������ʣ�
	 * 1.�����û���������ض�������û��ر�����������Ͳ��ܷ������
	 * 2.�ױ��ķ�������ʹ�õ�Ե�ͨѶ�ķ�������������������������success,��Ȼ�ױ���������һֱ�������������
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
		 * 1.��ȡ12������
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
		 * 2.��ȡkeyValue
		 */
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));
		String keyValue = props.getProperty("keyValue");
		
		/*
		 * 3.����PaymentUtil��У�鷽����У������ߵ����
		 *    >���У��ʧ�ܣ����������Ϣ��ת����msg.jsp
		 *    >���У��ͨ���� 
		 *         * �жϷ��ʵķ������ض����ǵ�Ե㣬���Ҫ���ض���
		 *         �޸ĸö���״̬������ɹ���Ϣ��ת����msg.jsp
		 *         *����ǵ�Ե㣺�޸ĸö���״̬������success
		 */
		
		boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, keyValue);
				
		if(!bool){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "֧��ʧ��");
		}
		if(r1_Code.equals("1")){
			orderService.updateStatus(r6_Order,2);
			
			if(r1_Code.equals("1")){
				req.setAttribute("code", "success");
				req.setAttribute("msg", "֧���ɹ�");
				return "f:/jsps/msg,jsp";	
			}else if(r9_BType.equals("2")){
				res.getWriter().print("success");
			}
			
		}
		return null;
	
	}
	
}
