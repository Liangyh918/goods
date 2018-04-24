package cn.itcast.goods.order.service;

import java.sql.SQLException;

import cn.itcast.goods.order.dao.OrderDao;
import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.jdbc.JdbcUtils;

public class OrderService {
	OrderDao orderDao = new OrderDao();

	public void createOrder(Order order) {
		try {
			JdbcUtils.beginTransaction();
			orderDao.add(order);

			JdbcUtils.commitTransaction();
		} catch (SQLException e) {
			try {
				e.printStackTrace();
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e2) {
				throw new RuntimeException(e);
			}
		}

	}

	public PageBean<Order> myOrders(String uid, int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findByUid(uid, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (Exception e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new RuntimeException(e);

		}

	}
	
	/**
	 * 查询所有
	 * @param pc
	 * @return
	 */
	public PageBean<Order> allOrders(int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findAll(pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (Exception e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new RuntimeException(e);

		}

	}
	
	
	/**
	 * 查询所有
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findByStatus(int status ,int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findByStatus(status,pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (Exception e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new RuntimeException(e);

		}

	}

	public Order load(String oid) {
		try {
			JdbcUtils.beginTransaction();
			Order order = orderDao.load(oid);
			JdbcUtils.commitTransaction();
			return order;
		} catch (Exception e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (Exception e2) {

			}
			throw new RuntimeException(e);
		}

	}
	
	/**
	 * 查询订单状态
	 * @param oid
	 * @return
	 */
	
	public int findStatus(String oid) {
		try {
			return orderDao.findStatus(oid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 修改订单状态
	 * @param oid
	 */
	public void updateStatus(String oid,int status){
		try {
			orderDao.updateStatus( oid, status);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}
