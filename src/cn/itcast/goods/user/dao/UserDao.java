package cn.itcast.goods.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.goods.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

public class UserDao {
	private QueryRunner qr = new TxQueryRunner();

	/*
	 * 注册校验
	 */
	public boolean ajaxValidateLoginname(String loginname) throws SQLException {
		String sql = "select count(1) from t_user where 1=1 and loginname = ?";
		Number number = (Number) qr.query(sql, new ScalarHandler(), loginname);
		return number.intValue() == 0;
	}

	public boolean ajaxValidateEmail(String email) throws SQLException {
		String sql = "select count(1) from t_user where 1=1 and email = ?";
		Number number = (Number) qr.query(sql, new ScalarHandler(), email);
		return number.intValue() == 0;
	}

	/**
	 * 添加用户
	 * 
	 * @param user
	 * @throws SQLException
	 */
	public void addUser(User user) throws SQLException {
		String sql = "insert into t_user values(?,?,?,?,?,?)";
		Object[] params = { user.getUid(), user.getLoginname(), user.getLoginpass(), user.getEmail(), user.isStatus(),
				user.getActivationCode() };
		qr.update(sql, params);
	}

	/**
	 * 根据激活码查找用户
	 * 
	 * @param ActivationCode
	 * @return
	 * @throws SQLException
	 */
	public User findUserByActivationCode(String ActivationCode) throws SQLException {
		String sql = "select * from t_user where 1=1 and activationCode = ?";
		User user = (User) qr.query(sql, new BeanHandler<User>(User.class), ActivationCode);
		return user;
	}

	public void activateUser(User user, boolean status) throws SQLException {
		String sql = "update t_user set status=? where 1=1 and uid= ?";
		qr.update(sql, status, user.getUid());
	}

	public User findUser(User user) throws SQLException {
		String sql = "select * from t_user where 1=1 and loginname=?";
		User LoginUser = (User) qr.query(sql, new BeanHandler<User>(User.class), user.getLoginname());
		return LoginUser;
	}

	public User findUserByLoginnameAndLoginpass(String loginname, String loginpass) throws SQLException {
		String sql = "select * from t_user where loginname=? and loginpass = ?";
		return (User)qr.query(sql, new BeanHandler<User>(User.class), loginname, loginpass);
	}
}