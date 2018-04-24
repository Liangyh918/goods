package cn.itcast.goods.order.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.order.domain.OrderItem;
import cn.itcast.goods.pager.Expression;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.pager.PageConstants;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {
	TxQueryRunner qr = new TxQueryRunner();
	
	public void add(Order order) throws SQLException{
		/*
		 * 1.���붩��
		 */
		String sql = "insert into t_order values(?,?,?,?,?,?)";
		Object[] params = {order.getOid(),order.getOrdertime(),order.getTotal()
				,order.getStatus(),order.getAddress(),order.getOwner().getUid()};
		qr.update(sql,params);
		System.out.println(sql);
		
		/*
		 * 2.ѭ�����������е�������Ŀ,��ÿ����Ŀ����һ��Object[]
		 * �����Ŀ�Ͷ�ӦObject[][]
		 * ִ�д���������ɲ��붩����Ŀ
		 *  
		 */
		sql = "insert into t_orderitem values(?,?,?,?,?,?,?,?)";
		int len = order.getOrderItemList().size();
		Object[][] objs = new Object[len][];
		for(int i=0;i<len;i++){
			OrderItem item = order.getOrderItemList().get(i);
			objs[i] = new Object[]{item.getOrderItemId(),item.getQuantity()
					,item.getSubtotal(),item.getBook().getBid(),
					item.getBook().getBname(),item.getBook().getCurrPrice(),
					item.getBook().getImage_b(),order.getOid()};
			
		}
		qr.batch(sql,objs);
		
	}
	
	
	
	
	/**
	 * ��ѯ��ǰ�û��µĶ���
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	
	public PageBean<Order> findByUid(String uid, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("uid", "=", uid));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * ��״̬��ѯ
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	
	public PageBean<Order> findByStatus(int status, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("status", "=", String.valueOf(status)));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * ��ѯ���ж���
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findAll(int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		return findByCriteria(exprList, pc);
	}
	
	private PageBean<Order> findByCriteria(List<Expression> exprList, int pc) throws SQLException {
		/*
		 * 1.�õ�ps 2.�õ�tr 3.�õ�beanlist 4.����PageBean,����
		 */
		/*
		 * 1.�õ�ps��ҳ��С
		 */
		int ps = PageConstants.ORDER_PAGE_SIZE;
		/*
		 * 2.ʹ��exprList����where�Ӿ�
		 */
		StringBuilder whereSql = new StringBuilder(" where 1=1");
		List<Object> params = new ArrayList<Object>();//
		for (Expression expr : exprList) {
			whereSql.append(" and ").append(expr.getName()).append(" ").append(expr.getOperator());
			if (!expr.getOperator().equals("is null")) {
				whereSql.append("?");
				params.add(expr.getValue());
			}
		}
		/*
		 * 3.�õ�tr.�ܼ�¼�� select count(1) from t_book where
		 */
		String sql = "select count(1) from t_order" + whereSql.toString();
		Number number = (Number) qr.query(sql, new ScalarHandler(), params.toArray());
		int tr = number.intValue();
		
		
		/*
		 * 4.�õ�beanlist
		 */
		
		params.add((pc - 1) * ps);
		params.add(pc * ps - 1);
		sql = "select * from t_order " + whereSql.toString()+" order by ordertime limit ?,?";
		List<Order> orderList = (List<Order>) qr.query(sql, new BeanListHandler<Order>(Order.class), params.toArray());

		//��Ȼ��ȡ�����ж�������������û�ж�����Ŀ
		//�������ж�����Ϊ��������ж�����Ŀ
		for(Order order : orderList){
			loadOrderItem(order);
		}
		
		/*
		 * pagebean ������url��url��servlet����
		 */
		PageBean<Order> pagebean = new PageBean<Order>();
		pagebean.setPc(pc);
		pagebean.setPs(ps);
		pagebean.setTr(tr);
		pagebean.setBeanList(orderList);
		return pagebean;
	}


	private void loadOrderItem(Order order) throws SQLException {
		/*
		 * 1. sql��䣺select * from t_orderitem where oid=?
		 * 2.ִ��sql���õ�List<OrderItem>
		 * 3.���ø�Order����
		 */
		String sql = "select * from t_orderitem where oid=?";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(),order.getOid());
		List<OrderItem> orderItemList = toOrderItemList(mapList);
		order.setOrderItemList(orderItemList);
	}


	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(Map map :mapList){
			OrderItem orderItem = toOrderItem(map);
			orderItemList.add(orderItem);
		}
		return orderItemList;
	}


	private OrderItem toOrderItem(Map<String,Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}
	
	/**
	 * ���ض���
	 * @param oid
	 * @return
	 * @throws SQLException
	 */
	public Order load(String oid) throws SQLException{
		String sql = "select * from t_order where oid=?";
		Order order = qr.query(sql, new BeanHandler<Order>(Order.class),oid);
		loadOrderItem(order);
		return order;
	}
	
	/**
	 * ��ѯ����״̬
	 * @param oid
	 * @return
	 * @throws SQLException
	 */
	public int findStatus(String oid) throws SQLException{
		String sql = "select status from t_order where oid=?";
		Number number = (Number) qr.query(sql, new ScalarHandler(),oid);
		return number.intValue();		
	}
	
	/**
	 * ���¶���״̬
	 * @param oid
	 * @param status
	 * @throws SQLException
	 */
	public void updateStatus(String oid,int status) throws SQLException{
		String sql = "update t_order set status = ? where oid = ?";
		qr.update(sql,status,oid);
	}
	
	
}
