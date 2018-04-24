package cn.itcast.goods.cart.domain;

import java.math.BigDecimal;

import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.user.domain.User;

public class CartItem {
	private String cartItemId;// 购物车条目id
	private int quantity;// 数量
	private Book book;//图书
	private User user;//用户
	
	public String getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	//添加小计方法
	public double getSubTotal(){
		BigDecimal price = new BigDecimal(book.getCurrPrice()+"");
		BigDecimal quantity = new BigDecimal(this.quantity+"");
		return price.multiply(quantity).doubleValue();
	}
}
