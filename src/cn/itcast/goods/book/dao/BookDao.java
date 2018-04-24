package cn.itcast.goods.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.pager.Expression;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.pager.PageConstants;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private TxQueryRunner qr = new TxQueryRunner();

	/**
	 * 删除图书
	 * @param bid
	 * @throws SQLException
	 */
	public void delete(String bid) throws SQLException{
		String sql = "delete form t_book where bid=?";
		qr.update(sql,bid);
	}
	
	
	
	/**
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByCategory(String cid, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("cid", "=", cid));
		PageBean pb = findByCriteria(exprList, pc);
		return pb;
	}

	public PageBean<Book> findByBname(String bname, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname", "like", bname));
		return findByCriteria(exprList, pc);
	}

	public PageBean<Book> findByAuthor(String author, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("author", "like", "%" + author + "%"));
		return findByCriteria(exprList, pc);
	}

	public PageBean<Book> findByPress(String press, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("press", "like", "%" + press + "%"));
		return findByCriteria(exprList, pc);
	}

	public PageBean<Book> findByCombination(Book criteria,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname","like","%"+criteria.getBname()+"%"));
		exprList.add(new Expression("author","like","%"+criteria.getAuthor()+"%"));
		exprList.add(new Expression("press","like","%"+criteria.getPress()+"%"));
		return findByCriteria(exprList, pc);
	}
	/**
	 * 通用查询方法
	 * 
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException
	 */

	private PageBean<Book> findByCriteria(List<Expression> exprList, int pc) throws SQLException {
		/*
		 * 1.得到ps 2.得到tr 3.得到beanlist 4.创建PageBean,返回
		 */
		/*
		 * 1.得到ps，页大小
		 */
		int ps = PageConstants.BOOK_PAGE_SIZE;
		/*
		 * 2.使用exprList生成where子句
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
		 * 3.得到tr.总记录数 select count(1) from t_book where
		 */
		String sql = "select count(1) from t_book" + whereSql.toString();
		Number number = (Number) qr.query(sql, new ScalarHandler(), params.toArray());
		int tr = number.intValue();
		
		
		/*
		 * 4.得到beanlist
		 */
		
		params.add((pc - 1) * ps);
		params.add(pc * ps - 1);
		sql = "select * from t_book" + whereSql.toString()+" order by orderby limit ?,?";
		List<Book> bookList = (List<Book>) qr.query(sql, new BeanListHandler<Book>(Book.class), params.toArray());

		/*
		 * pagebean 不设置url，url在servlet设置
		 */
		PageBean<Book> pagebean = new PageBean<Book>();
		pagebean.setPc(pc);
		pagebean.setPs(ps);
		pagebean.setTr(tr);
		pagebean.setBeanList(bookList);
		return pagebean;
	}
	
	public Book findByBid(String bid) throws SQLException{
		String sql = "select * from t_book b,t_category c where b.cid = c.cid and bid=?";
		//一行记录中，包含了很多book的属性，还有一个cid
		Map<String,Object> map = (Map) qr.query(sql, new MapHandler(),bid);
		//把Map中除了cid以外的其他属性映射到book对象中；
		Book book = CommonUtils.toBean(map, Book.class);
		//把Map中cid属性映射到category中，即这个Category只有cid
		Category category = CommonUtils.toBean(map, Category.class);
		//两者建立关系
		book.setCategory(category);
		
		//把pid获取出来，创建一个Category parent,把pid付给它，然后再把parent赋给category
		if(map.get("pid")!=null){
		Category parent = new Category();
		parent.setCid((String) map.get("pid"));
		category.setParent(parent);
		}
		return book;
	}
	
	
	/**
	 * 查询指定分类下图书的个数
	 * @param cid
	 * @return
	 * @throws SQLException
	 */
	public int findBookCountByCategory(String cid) throws SQLException{
		String sql = "select count(*) from t_book where cid = ?";
		Number cnt = (Number) qr.query(sql, new ScalarHandler(),cid);
		return cnt == null?0:cnt.intValue();
	}
	
	
	public void add(Book book) throws SQLException{
		String sql = "insert into t_book(bid,bname,author,price,currPrice,"+
						"discount,press,publishtime,edithion,pageNum,wordNum,printtime,"+
				"booksize,paper,cid,image_w,image_b"+
			    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = {book.getBid(),book.getBname(),book.getAuthor(),book.getPrice()
				,book.getCurrPrice(),book.getDiscount(),book.getPress(),book.getPublishtime()
				,book.getEdition(),book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(),book.getCategory().getCid(),
				book.getImage_w(),book.getImage_b()};
		qr.update(sql,params);
	}
	
	
	public void edit(Book book) throws SQLException{
		String sql = "update t_book set bname=?,author?,price=?,currPrice=?,"+
	"discount=?,press=?,publishtime=?,edition=?,pageNum=?,wordNum=?,"+
				"printtime=?,booksize=?,paper=?,cid=? where bid=?";
		Object[] params = {book.getBname(),book.getAuthor(),book.getPrice(),book.getCurrPrice(),
				book.getDiscount(),book.getPress(),book.getPublishtime(),book.getEdition(),book.getPageNum()
				,book.getWordNum(),book.getPrinttime(),book.getBooksize(),book.getPaper(),book.getCategory().getCid()
				,book.getBid()};
		qr.update(sql,params);
	}
	
}
