<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>用户注册</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link rel="stylesheet" type="text/css"
	href="<c:url value='/css/css.css'/>">
<link rel="stylesheet" type="text/css"
	href="<c:url value='/jsps/css/user/regist.css'/>">
<script type="text/javascript"
	src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/common.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/jsps/js/user/regist.js'/>"></script>
</head>
<body>
	<div id="divMain">
		<div id="divTitle">
			<span id="divSpan">新用户注册</span>
		</div>
		<div id="divBody">
			<form id="registForm" action="/goods/UserServlet" method="post">
				<!--"<c:url value='/UserServlet'/>"  -->
				<input type="hidden" name="method" value="regist" />
				<table id="tableform">
					<tr>
						<td class="tdText">用户名：</td>
						<td class="tdInput"><input type="text" name="loginname"
							id="loginname" class="inputClass" value="${form.loginname}" /></td>
						<td class="tdError"><label class="errorClass"
							id="loginnameError">${errors.loginname}</label></td>
					</tr>
					<tr>
						<td class="tdText">密码：</td>
						<td class="tdInput"><input type="password" name="loginpass"
							id="loginpass" class="inputClass" value="${form.loginpass}" /></td>
						<td class="tdError"><label class="errorClass"
							id="loginpassError">${errors.loginpass}</label></td>
					</tr>
					<tr>
						<td class="tdText">确认密码：</td>
						<td class="tdInput"><input type="password" name="reloginpass"
							id="reloginpass" class="inputClass" value="${form.reloginpass}" /></td>
						<td class="tdError"><label class="errorClass"
							id="reloginpassError">${errors.reloginpass}</label></td>
					</tr>
					<tr>
						<td class="tdText">Email：</td>
						<td class="tdInput"><input type="text" name="email"
							id="email" class="inputClass" value="${form.email}" /></td>
						<td class="tdError"><label class="errorClass" id="emailError">${errors.email}</label></td>
					</tr>
					<tr>
						<td class="tdText">图像验证码：</td>
						<td><input type="text" name="verifycode" id="verifycode"
							class="inputClass" value="${form.verifycode}" /></td>
						<td class="tdError"><label class="errorClass"
							id="verifycodeError">${errors.verifycode}</label></td>
					</tr>
					<tr>
						<td class="tdText"></td>
						<td class="tdInput"><div id="divVerifyCode">
								<img src="<c:url value='/VerifyCodeServlet'/>"
									name="verifycodeimg" id="verifycodeimg" />
							</div></td>
						<td><a id="switchVerifyCode" href="javascript:_hyz()">换一张</a></td>
					</tr>
					<tr>
						<td class="tdText"></td>
						<td class="tdInput"><input type="image"
							src="<c:url value='/images/regist1.jpg'/>" name="submitBtn"
							id="submitBtn" /></td>
						<td></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>
