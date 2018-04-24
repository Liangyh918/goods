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
		 * 1.��ȡ�û���
		 */
		String loginname = req.getParameter("loginname");
		/*
		 * 2.ͨ��service��ȡУ����
		 */
		boolean b = userService.ajaxValidateLoginname(loginname);
		/*
		 * 3.У�������͸��ͻ���
		 */
		res.getWriter().print(b);
		return null;
	}

	public String ajaxValidateEmail(HttpServletRequest req, HttpServletResponse res) throws IOException {
		/*
		 * 1.��ȡ�û���
		 */
		String email = req.getParameter("email");
		/*
		 * 2.ͨ��service��ȡУ����
		 */
		boolean b = userService.ajaxValidateLoginname(email);
		/*
		 * 3.У�������͸��ͻ���
		 */
		res.getWriter().print(b);
		return null;
	}

	public String ajaxValidateVerifyCode(HttpServletRequest req, HttpServletResponse res) throws IOException {
		/*
		 * 1.��ȡ�������֤��
		 */
		String verifycode = req.getParameter("verifycode");
		/*
		 * 2.��ȡͼƬ����ʵ����֤��
		 */
		String verifycode01 = (String) req.getSession().getAttribute("vCode");
		System.out.println("verifycode01:" + verifycode01);
		/*
		 * 3.���бȽϣ����Դ�Сд
		 */
		boolean b = verifycode.equalsIgnoreCase(verifycode01);
		/*
		 * 4.���ȽϽ�����͸��ͻ���
		 */
		res.getWriter().print(b);
		return null;
	}

	public String regist(HttpServletRequest req, HttpServletResponse res) {
		/*
		 * 1.��װ�����ݵ�user
		 */
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		/*
		 * 2.У��֮�����У��ʧ�ܣ����������Ϣ�����ص�regist.jsp��ʾ
		 */
		Map<String, String> errors = validateRegist(formUser, req.getSession());
		if (errors.size() > 0) {

			req.setAttribute("errors", errors);
			req.setAttribute("form", formUser);
			return "f:/jsps/user/regist.jsp";
		}

		/*
		 * 3.ʹ��service��user�������
		 */
		userService.regist(formUser);
		/*
		 * 4.��ע��ɹ���Ϣת����msg.jsp��ʾ
		 */
		req.setAttribute("code", "success");
		req.setAttribute("msg", "ע��ɹ��������ϵ����伤�");
		return "f:/jsps/msg.jsp";
	}

	public Map<String, String> validateRegist(User formUser, HttpSession httpSession) {
		Map<String, String> errors = new HashMap<String, String>();
		/*
		 * 1.��֤�û���loginname�Ƿ�Ϸ����Ƿ�Ϊ�գ�����3-20
		 */
		String loginname = formUser.getLoginname();
		if (loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "�û�������Ϊ��");
		} else if (loginname.length() < 3 || loginname.length() > 20) {
			errors.put("loginname", "�û���������3~20֮��");
		} else if (!userService.ajaxValidateLoginname(loginname)) {
			errors.put("loginname", "�û�����ע��,������û�");
		}
		/*
		 * 2.��֤����
		 */
		String loginpass = formUser.getLoginpass();
		if (loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "���벻��Ϊ��");
		} else if (loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "���볤�ȱ�����2~20֮��");
		}

		/*
		 * 3.��֤ȷ������
		 */
		String reloginpass = formUser.getReloginpass();
		if (reloginpass == null || reloginpass.trim().isEmpty()) {
			errors.put("reloginpass", "�û�������Ϊ��");
		} else if (!loginpass.equals(reloginpass)) {
			errors.put("reloginpass", "�������벻һ��");
		}
		System.out.println(loginpass + ";" + reloginpass);
		/*
		 * 4.��֤����
		 */
		String email = formUser.getEmail();
		String regex = "^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
		if (email == null || email.trim().isEmpty()) {
			errors.put("email", "���䲻��Ϊ��");
		} else if (!email.matches(regex)) {
			errors.put("email", "�����ʽ���Ϸ�");
		} else if (!userService.ajaxValidateEmail(email)) {
			errors.put("email", "�����ѱ�ע�ᣬ���������");
		}

		/*
		 * 5.��֤��֤��
		 */
		String verifycode = formUser.getVerifycode();
		if (verifycode == null || verifycode.trim().isEmpty()) {
			errors.put("verifycode", "��֤�벻��Ϊ��");
		} else if (verifycode.length() != 4) {
			errors.put("verifycode", "��֤�볤�Ȳ���ȷ");
		} else if (verifycode.equals((String) httpSession.getAttribute("vCode"))) {

			errors.put("verifycode", "��֤�벻��ȷ");
		}
		return errors;
	}

	public String activation(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			userService.activateUser(req.getParameter("activationCode"));
			req.setAttribute("code", "success");
			req.setAttribute("msg", "����ɹ������Ե�¼");
		} catch (UserException e) {
			req.setAttribute("msg", e.getMessage());
			req.setAttribute("code", "error");
		}
		return "f:/jsps/msg.jsp";
	}

	public String login(HttpServletRequest req, HttpServletResponse res) throws IOException {

		/**
		 * 1.��װ�����ݵ�User������ 2.У��user���� 3.����userservice.login()��ȡuser 4.�鿴
		 * ���user�����ڣ�����û�м���򱣴������Ϣ�ͱ����ݣ����ڱ���ͻ��Ե�login.jsp, ת����login.jsp
		 * 5.���user���ڣ���user���󱣴浽�Ự�У����û�����ӵ�cookie�У�ע���������Ϊutf-8����
		 */

		/*
		 * 1.��װ�����ݵ�User������
		 */
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		/*
		 * 2.У��user����
		 */
		Map<String, String> errors = validateLogin(formUser, req.getSession());
		if (errors.size() > 0) {
			req.setAttribute("errors", errors);
			req.setAttribute("form", formUser);
			return "f:/jsps/user/login.jsp";
		}
		/*
		 * 3.����userservice.login()��ȡuser
		 */

		try {
			User user = userService.login(formUser);
			/*
			 * 5.���user���ڣ���user���󱣴浽�Ự�У����û�����ӵ�cookie�У�ע���������Ϊutf-8����
			 */
			req.getSession().setAttribute("sessionUser", user);
			Cookie cookie = new Cookie("loginname", URLEncoder.encode(user.getLoginname(), "utf-8"));
			cookie.setMaxAge(60 * 60 * 24 * 10);
			res.addCookie(cookie);

			return "r:/index.jsp";
		} catch (UserException e) {
			/*
			 * 4.���user�����ڣ�����û�м���򱣴������Ϣ�ͱ����ݣ����ڱ���ͻ��Ե�login.jsp, ת����login.jsp
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
