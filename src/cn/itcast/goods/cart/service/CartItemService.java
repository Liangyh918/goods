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
	 * ��ѯ�û����ﳵ��Ŀ
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
	 * ���»������Ŀ
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
			// 1.���ﳵ��Ŀ�Ѿ����ڣ�ֻ���������
			if (_cartItem != null) {
				cartItemDao.updateQuantity(_cartItem.getCartItemId(), cartItem.getQuantity());
			} else {
				// 2.�����������Ҫ����
				cartItem.setCartItemId(CommonUtils.uuid());
				cartItemDao.addCartItem(cartItem);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ����ɾ��
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
	 * ���ҹ��ﳵ��Ŀ
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
	 * ���¹��ﳵ��Ʒ����
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
	 * ���ύ������������ڹ��ﳵ���湴ѡ��item
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
