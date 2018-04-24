package cn.itcast.goods.user.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.domain.User;
import cn.itcast.goods.user.service.UserService;
import cn.itcast.goods.user.service.exception.UserException;
import cn.itcast.servlet.BaseServlet;

public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();

	public String ajaxValidateLoginname(HttpServletRequest req, HttpServletResponse res) throws IOException {
		/*
		 * 1.获取用户名
		 */
		String loginname = req.getParameter("loginname");
		/*
		 * 2.通过service获取校验结果
		 */
		boolean b = userService.ajaxValidateLoginname(loginname);
		/*
		 * 3.校验结果发送给客户端
		 */
		res.getWriter().print(b);
		return null;
	}

	public String ajaxValidateEmail(HttpServletRequest req, HttpServletResponse res) throws IOException {
		/*
		 * 1.获取用户名
		 */
		String email = req.getParameter("email");
		/*
		 * 2.通过service获取校验结果
		 */
		boolean b = userService.ajaxValidateLoginname(email);
		/*
		 * 3.校验结果发送给客户端
		 */
		res.getWriter().print(b);
		return null;
	}

	public String ajaxValidateVerifyCode(HttpServletRequest req, HttpServletResponse res) throws IOException {
		/*
		 * 1.获取输入的验证码
		 */
		String verifycode = req.getParameter("verifycode");
		/*
		 * 2.获取图片上真实的验证码
		 */
		String verifycode01 = (String) req.getSession().getAttribute("vCode");
		System.out.println("verifycode01:" + verifycode01);
		/*
		 * 3.进行比较，忽略大小写
		 */
		boolean b = verifycode.equalsIgnoreCase(verifycode01);
		/*
		 * 4.将比较结果发送给客户端
		 */
		res.getWriter().print(b);
		return null;
	}

	public String regist(HttpServletRequest req, HttpServletResponse res) {
		/*
		 * 1.封装表单数据到user
		 */
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		/*
		 * 2.校验之，如果校验失败，保存错误信息，返回到regist.jsp显示
		 */
		Map<String, String> errors = validateRegist(formUser, req.getSession());
		if (errors.size() > 0) {

			req.setAttribute("errors", errors);
			req.setAttribute("form", formUser);
			return "f:/jsps/user/regist.jsp";
		}

		/*
		 * 3.使用service将user存入表中
		 */
		userService.regist(formUser);
		/*
		 * 4.将注册成功消息转发到msg.jsp显示
		 */
		req.setAttribute("code", "success");
		req.setAttribute("msg", "注册成功，请马上到邮箱激活！");
		return "f:/jsps/msg.jsp";
	}

	public Map<String, String> validateRegist(User formUser, HttpSession httpSession) {
		Map<String, String> errors = new HashMap<String, String>();
		/*
		 * 1.验证用户名loginname是否合法，是否不为空，长度3-20
		 */
		String loginname = formUser.getLoginname();
		if (loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "用户名不能为空");
		} else if (loginname.length() < 3 || loginname.length() > 20) {
			errors.put("loginname", "用户名长度在3~20之间");
		} else if (!userService.ajaxValidateLoginname(loginname)) {
			errors.put("loginname", "用户名已注册,请更换用户");
		}
		/*
		 * 2.验证密码
		 */
		String loginpass = formUser.getLoginpass();
		if (loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "密码不能为空");
		} else if (loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度必须在2~20之间");
		}

		/*
		 * 3.验证确认密码
		 */
		String reloginpass = formUser.getReloginpass();
		if (reloginpass == null || reloginpass.trim().isEmpty()) {
			errors.put("reloginpass", "用户名不能为空");
		} else if (!loginpass.equals(reloginpass)) {
			errors.put("reloginpass", "两个密码不一致");
		}
		System.out.println(loginpass + ";" + reloginpass);
		/*
		 * 4.验证邮箱
		 */
		String email = formUser.getEmail();
		String regex = "^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
		if (email == null || email.trim().isEmpty()) {
			errors.put("email", "邮箱不能为空");
		} else if (!email.matches(regex)) {
			errors.put("email", "邮箱格式不合法");
		} else if (!userService.ajaxValidateEmail(email)) {
			errors.put("email", "邮箱已被注册，请更换邮箱");
		}

		/*
		 * 5.验证验证码
		 */
		String verifycode = formUser.getVerifycode();
		if (verifycode == null || verifycode.trim().isEmpty()) {
			errors.put("verifycode", "验证码不能为空");
		} else if (verifycode.length() != 4) {
			errors.put("verifycode", "验证码长度不正确");
		} else if (verifycode.equals((String) httpSession.getAttribute("vCode"))) {

			errors.put("verifycode", "验证码不正确");
		}
		return errors;
	}

	public String activation(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			userService.activateUser(req.getParameter("activationCode"));
			req.setAttribute("code", "success");
			req.setAttribute("msg", "激活成功，可以登录");
		} catch (UserException e) {
			req.setAttribute("msg", e.getMessage());
			req.setAttribute("code", "error");
		}
		return "f:/jsps/msg.jsp";
	}

	public String login(HttpServletRequest req, HttpServletResponse res) throws IOException {

		/**
		 * 1.封装表单数据到User对象中 2.校验user对象 3.调用userservice.login()获取user 4.查看
		 * 如果user不存在，或者没有激活，则保存错误消息和表单数据，用于报错和回显到login.jsp, 转发到login.jsp
		 * 5.如果user存在，将user对象保存到会话中，将用户名添加到cookie中（注意编码设置为utf-8），
		 */

		/*
		 * 1.封装表单数据到User对象中
		 */
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		/*
		 * 2.校验user对象
		 */
		Map<String, String> errors = validateLogin(formUser, req.getSession());
		if (errors.size() > 0) {
			req.setAttribute("errors", errors);
			req.setAttribute("form", formUser);
			return "f:/jsps/user/login.jsp";
		}
		/*
		 * 3.调用userservice.login()获取user
		 */

		try {
			User user = userService.login(formUser);
			/*
			 * 5.如果user存在，将user对象保存到会话中，将用户名添加到cookie中（注意编码设置为utf-8），
			 */
			req.getSession().setAttribute("sessionUser", user);
			Cookie cookie = new Cookie("loginname", URLEncoder.encode(user.getLoginname(), "utf-8"));
			cookie.setMaxAge(60 * 60 * 24 * 10);
			res.addCookie(cookie);

			return "r:/index.jsp";
		} catch (UserException e) {
			/*
			 * 4.如果user不存在，或者没有激活，则保存错误消息和表单数据，用于报错和回显到login.jsp, 转发到login.jsp
			 */
			System.out.println("login01");
			req.setAttribute("msg", e.getMessage());
			req.setAttribute("form", formUser);
			return "f:/jsps/user/login.jsp";
		}

	}

	public Map<String, String> validateLogin(User formUser, HttpSession httpSession) {
		Map<String, String> errors = new HashMap<String, String>();

		return errors;
	}

	public String logout(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getSession().invalidate();
		return "r:/jsps/user/login.jsp";
	}
}
