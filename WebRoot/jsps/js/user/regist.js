$(function() {
	// 1.刷新页面时，有错误消息显示，无错误消息不显示label列
	$(".errorClass").each(function() {
		showError($(this));
	});

	// 2.光标移入移出按钮效果
	$("#submitBtn").hover(function() {
		$("#submitBtn").attr("src", "/goods/images/regist2.jpg");
	}, function() {
		$("#submitBtn").attr("src", "/goods/images/regist1.jpg");
	});

	// 3.输入框得到焦点时删除校验信息
	$(".inputClass").focus(function() {
		var lable = $(this).attr("id") + "Error";
		$("#" + lable).text("");
		showError($("#" + lable));
	});

	// 4.输入框失去焦点时进行校验
	$(".inputClass").blur(
			function() {
				var id = $(this).attr("id")
				var funcName = "validate" + id.substring(0, 1).toUpperCase()
						+ id.substring(1) + "()";
				eval(funcName);
			});
	// 5.提交表单时，校验所有域
	$("#registForm").submit(function() {
		var bool = true;
		if (!validateLoginname()) {
			bool = false;
		}
		if (!validateloginpass()) {
			bool = false;
		}
		if (!validateReloginpass()) {
			bool = false;
		}
		if (!validateEmail()) {
			bool = false;
		}
		if (!validateVerifycode()) {
			bool = false;
		}
		return bool;
	})
});

function showError(ele) {
	var text = ele.text();
	if (!text) {
		ele.css("display", "none");
	} else {
		ele.css("display", "");
	}
}

function _hyz() {
	$("#verifycodeimg").attr("src",
			"/goods/VerifyCodeServlet?a=" + new Date().getTime());
}

function validateLoginname() {
	var id = "loginname";
	var value = $("#" + id).val();

	// 1.校验用户名是否为空
	if (!value) {
		$("#" + id + "Error").text("用户名不能为空")
		showError($("#" + id + "Error"));
		return false;
	}
	// 2.校验用户名长度
	if (value.length < 3 || value.length > 20) {
		$("#" + id + "Error").text("用户名长度非法")
		showError($("#" + id + "Error"));
		return false;
	}

	// 3.校验用户名是否已注册
	$.ajax({
		url : "/goods/UserServlet",
		data : {
			method : "ajaxValidateLoginname",
			loginname : value
		},
		type : "POST",
		datatype : "json",
		cache : false,
		success : function(result) {
			if (!result) {
				$("#" + id + "Error").text("用户名已注册");
				showError("#" + id + "Error");
				false;
			}
		}
	});
	return true;
}

function validateloginpass() {
	var id = "loginpass";
	var value = $("#" + id).val();

	// 1.校验密码是否为空
	if (!value) {
		$("#" + id + "Error").text("密码不能为空");
		showError($("#" + id + "Error"));
		return false;
	}
	// 2.校验密码长度
	if (value.length < 3 || value.length > 20) {
		$("#" + id + "Error").text("密码长度非法");
		showError($("#" + id + "Error"));
		return false;
	}
	return true;
}

function validateReloginpass() {
	var id = "reloginpass";
	var value = $("#" + id).val();

	// 1.校验确认密码是否为空
	if (!value) {
		$("#" + id + "Error").text("确认密码不能为空")
		showError($("#" + id + "Error"));
		return false;
	}
	// 2.校验确认密码长度
	if (value.length < 3 || value.length > 20) {
		$("#" + id + "Error").text("确认密码长度非法");
		showError($("#" + id + "Error"));
		return false;
	}
	// 3.校验确认密码和密码是否一致
	if (value != $("#loginpass").val()) {
		$("#" + id + "Error").text("确认密码和密码不一致");
		showError($("#" + id + "Error"));
		return false;
	}
	return true;
}

function validateEmail() {
	var id = "email";
	var value = $("#" + id).val();
	// 1.校验邮箱是否为空
	if (!value) {
		$("#" + id + "Error").text("邮箱地址不能为空");
		showError($("#" + id + "Error"));
		return false;
	}
	// 2.校验邮箱格式是否合法
	if (!/^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/
			.test(value)) {
		$("#" + id + "Error").text("邮箱格式不正确");
		showError($("#" + id + "Error"));
		return false;
	}
	// 3.校验邮箱是否已经被注册
	$.ajax({
		url : "/goods/UserServlet",
		data : {
			method : "ajaxValidateEmail",
			email : value
		},
		type : "POST",
		datatype : "json",
		cache : false,
		success : function(result) {
			if (!result) {
				$("#" + id + "Error").text("邮箱已注册");
				showError("#" + id + "Error");
				false;
			}
		}
	});
	return true;
}

function validateVerifycode() {
	var id = "verifycode";
	var value = $("#" + id).val();
	// 1.校验验证码是否为空
	if (!value) {
		$("#" + id + "Error").text("验证码不能为空");
		showError($("#" + id + "Error"));
		return false;
	}
	// 2.校验验证码长度是否正确
	if (value.length != 4) {
		$("#" + id + "Error").text("验证码长度不符");
		showError($("#" + id + "Error"))
		return false;
	}
	// 3.校验验证码是否与图片一致
	$.ajax({
		url : "/goods/UserServlet",
		data : {
			method : "ajaxValidateVerifyCode",
			verifycode : value
		},
		type : "POST",
		datatype : "json",
		async : true,
		cache : false,
		success : function(result) {
			if (result != "true") {
				$("#" + id + "Error").text("验证码不正确");
				showError($("#" + id + "Error"));
				false;
			}
		}
	});
	return true;
}