package cn.itcast.goods.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.management.RuntimeErrorException;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.dao.UserDao;
import cn.itcast.goods.user.domain.User;
import cn.itcast.goods.user.service.exception.UserException;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

public class UserService {
	private UserDao userDao = new UserDao();

	/**
	 * 校验用户名是否已注册
	 * 
	 * @param loginname
	 * @return
	 */
	public boolean ajaxValidateLoginname(String loginname) {
		try {
			return userDao.ajaxValidateLoginname(loginname);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 校验邮箱是否已注册
	 * 
	 * @param email
	 * @return
	 */
	public boolean ajaxValidateEmail(String email) {
		try {
			return userDao.ajaxValidateEmail(email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 注册用户
	 * 
	 * @param user
	 */
	public void regist(User user) {
		/*
		 * 数据补齐
		 */
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid() + CommonUtils.uuid());

		/*
		 * 向数据库插入数据
		 */
		try {
			userDao.addUser(user);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		/*
		 * 发送邮件
		 */
		Properties props = new Properties();
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		/*
		 * 1.创建mail session
		 */
		String host = props.getProperty("host");
		String loginUser = props.getProperty("user");
		;
		String password = props.getProperty("password");
		Session session = MailUtils.createSession(host, loginUser, password);
		/*
		 * 2.创建mail 对象
		 */
		String from = props.getProperty("from");
		String to = user.getEmail();
		String subject = props.getProperty("subject");
		//
		String content = MessageFormat.format(props.getProperty("content"), user.getActivationCode());
		Mail mail = new Mail(from, to, subject, content);
		/*
		 * 3.发送邮件
		 */
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException | IOException e) {
			throw new RuntimeException(e);
		}

	}

	public void activateUser(String ActivateCode) throws UserException {
		try {
			User user = userDao.findUserByActivationCode(ActivateCode);
			if (user == null) {
				throw new UserException("无效的激活码");
			}
			if (user.isStatus()) {
				throw new UserException("用户已激活，不能重复激活");
			}
			userDao.activateUser(user, true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public User login(User user) throws UserException {
		try {
			User TargetUser = userDao.findUserByLoginnameAndLoginpass(user.getLoginname(), user.getLoginpass());
			if (TargetUser == null) {
				throw new UserException("用户名或密码错误");
			} else if (!TargetUser.isStatus()) {
				throw new UserException("用户尚未激活");
			}
			return TargetUser;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

}
