package cn.itcast.goods.cart.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.cart.dao.CartItemDao;
import cn.itcast.goods.cart.domain.CartItem;

public class CartItemService {
	private CartItemDao cartItemDao = new CartItemDao();

	/**
	 * 查询用户购物车条目
	 * 
	 * @param uid
	 * @return
	 */
	public List<CartItem> findCartItemByUser(String uid) {
		List<CartItem> cartItemList = new ArrayList<CartItem>();
		try {
			cartItemList = cartItemDao.findByUser(uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return cartItemList;
	}

	/**
	 * 更新或插入条目
	 * 
	 * @param cartItem
	 */
	public void add(CartItem cartItem) {
		try {
			if (cartItemDao == null) {
				System.out.println("cartItemDao is null");
			}
			if (cartItem == null) {
				System.out.println("cartItem is null");
			}
			if (cartItem.getUser() == null) {
				System.out.println("cartItem.getUser() is null");
			}
			if (cartItem.getBook() == null) {
				System.out.println("cartItem.getBook() is null");
			}
			System.out.println("cartItem.getUser().getUid():" + cartItem.getUser().getUid());
			CartItem _cartItem = cartItemDao.findByUidAndBid(cartItem.getUser().getUid(), cartItem.getBook().getBid());
			// 1.购物车条目已经存在，只需更新数量
			if (_cartItem != null) {
				cartItemDao.updateQuantity(_cartItem.getCartItemId(), cartItem.getQuantity());
			} else {
				// 2.如果不存在需要插入
				cartItem.setCartItemId(CommonUtils.uuid());
				cartItemDao.addCartItem(cartItem);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 批量删除
	 * 
	 * @param cartItemIds
	 */
	public void batchDelete(String cartItemIds) {
		try {
			System.out.println(cartItemIds);
			cartItemDao.batchDelete(cartItemIds);
		} catch (Exception e) {
			System.out.println("message" + e.getMessage());
			throw new RuntimeException();
		}
	}

	/**
	 * 查找购物车条目
	 * 
	 * @param cartItemId
	 * @return
	 */
	public CartItem findByCartItemId(String cartItemId) {
		CartItem cartItem = null;
		try {
			cartItem = cartItemDao.findByCartItemId(cartItemId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return cartItem;
	}

	/**
	 * 更新购物车商品数量
	 * @param cartItemId
	 * @param quantity
	 */
	public CartItem updateQuantity(String cartItemId, int quantity) {
		try {
			cartItemDao.updateQuantity(cartItemId, quantity);
			return cartItemDao.findByCartItemId(cartItemId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 在提交订单界面加载在购物车界面勾选的item
	 * @param cartItemIds
	 * @return
	 */
	public List<CartItem> loadCartItems(String cartItemIds){
		try {
			return cartItemDao.loadCartItems(cartItemIds);
		} catch (SQLException e) {
		throw new RuntimeException(e);
		}
		
	}

}
