package cn.itcast.goods.cart.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.cart.domain.CartItem;
import cn.itcast.goods.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

public class CartItemDao {
	private TxQueryRunner qr = new TxQueryRunner();

	// ��һ��map(Map<String,Object>)�û�ת����cartItem(CartItem)
	private CartItem toCartItem(Map map) {
		if(map==null || map.size()==0){
			return null;
		}
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		User user = CommonUtils.toBean(map, User.class);
		cartItem.setBook(book);
		cartItem.setUser(user);
		return cartItem;
	}

	// �����map(List<Map<String,Object>>)�û�ת���ɶ��cartItem(List<CartItem>)
	private List<CartItem> toCartItemList(List<Map<String, Object>> mapList) {
		List<CartItem> cartItemList = new ArrayList<CartItem>();
		for (Map map : mapList) {
			cartItemList.add(toCartItem(map));
		}
		return cartItemList;
	}

	/**
	 * ��ȡ�û��Ĺ��ﳵ��Ŀ
	 * 
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public List<CartItem> findByUser(String uid) throws SQLException {
		String sql = "select * from t_cartitem c,t_book b where 1=1 and c.bid=b.bid and c.uid=? order by c.orderby";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), uid);
		return toCartItemList(mapList);
	}

	/**
	 * 
	 * @param uid
	 * @param bid
	 * @return
	 * @throws SQLException
	 */
	public CartItem findByUidAndBid(String uid, String bid) throws SQLException {
		System.out.println("findByUidAndBid method");
		String sql = "select * from t_cartitem where 1=1 and uid = ? and bid = ?";
		Map<String, Object> map = qr.query(sql, new MapHandler(), uid, bid);
		CartItem cartItem = toCartItem(map);
		return cartItem;
	}

	/**
	 * ������Ʒ����
	 * @param cartItemId
	 * @param quantity
	 * @throws SQLException
	 */
	public void updateQuantity(String cartItemId, int quantity) throws SQLException {
		String sql = "update t_cartitem set quantity = ?  where 1=1 and cartItemId=?";
		qr.update(sql, quantity, cartItemId);
	}

	public void addCartItem(CartItem cartItem) throws SQLException {
		String sql = "insert into t_cartitem(cartitemid,quantity,uid,bid) values(?,?,?,?)";
		Object[] params = { cartItem.getCartItemId(), cartItem.getQuantity(), cartItem.getUser().getUid(),
				cartItem.getBook().getBid() };
		qr.update(sql,params);
	}

	/**
	 * ���������С����wheresql
	 * @param length
	 * @return
	 */
	private String toWhereSql(int length){ 
		StringBuilder sb = new StringBuilder(" cartItemId in (");
		for(int i=0;i<length;i++){
			sb.append("?");
			if(i<length-1){
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	
	/**
	 * ����ɾ��
	 * @param cartItemIds
	 * @throws SQLException
	 */
	public void batchDelete(String cartItemIds) throws SQLException{
		//��String����ת����Object���飬�Ա���Ϊ�ɱ�����б�
		Object[] cartItemIdArray =  cartItemIds.split(",");
		//����cartItemIdArray��ȡwheresql
		String sql = "delete from t_cartitem where "+toWhereSql(cartItemIdArray.length);
		qr.update(sql,cartItemIdArray);
	}
	
	public CartItem findByCartItemId(String cartItemId) throws SQLException{
		String sql = "select * from t_cartitem c,t_book b where c.bid = b.bid and c.cartitemid = ?";
		Map<String,Object> map = qr.query(sql, new MapHandler(), cartItemId);
		return toCartItem(map);
	}
	
	
	/**
	 * ���ύ������������ڹ��ﳵ���湴ѡ��item
	 * @param cartItemIds
	 * @return
	 * @throws SQLException
	 */
	public List<CartItem> loadCartItems(String cartItemIds) throws SQLException{
		/*
		 * 1.��id�ַ�����ֳ������Ա���Ϊ��ѯ����
		 */
		Object[] cartItemIdArray = cartItemIds.split(",") ;
		/*
		 * 2.����where�Ӿ�
		 */
		String whereSql = toWhereSql(cartItemIdArray.length);
		/*
		 * 3.����sql
		 */
		String sql =  "select * from t_cartitem c,t_book b where c.bid=b.bid and "+whereSql;
		/*
		 * 4.��ȡ�����˲�ѯ�������ݵ�List<map>����
		 */
		
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(), cartItemIdArray);
		
		/*
		 * 5.��mapList����ת����List<CartItem>����
		 */
		
		return  toCartItemList(mapList);
		
	}
	
}
