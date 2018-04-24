<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>book_desc.jsp</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link rel="stylesheet" type="text/css" href="<c:url value='/adminjsps/admin/css/book/desc.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/jquery/jquery.datepick.css'/>">
<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jquery/jquery.datepick.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jquery/jquery.datepick-zh-CN.js'/>"></script>

<script type="text/javascript" src="<c:url value='/adminjsps/admin/js/book/desc.js'/>"></script>

<script type="text/javascript">

$(function() {
	$("#box").attr("checked", false);
	$("#formDiv").css("display", "none");
	$("#show").css("display", "");	
	
	// 操作和显示切换
	$("#box").click(function() {
		if($(this).attr("checked")) {
			$("#show").css("display", "none");
			$("#formDiv").css("display", "");
		} else {
			$("#formDiv").css("display", "none");
			$("#show").css("display", "");		
		}
	});
});


function loadChildren(){
	/*
	*1.获取pid
	*2.发出异步请求，功能之：
	    3.得到一个数组
	    4.获取cid元素（<select>）把内部的<option>全部删除
	    5.添加一个头（<option>请选择2级分类</option>）
	    6.循环数组，把数组中每个对象转换成一个<option>添加到cid中
	*/
	//1.获取pid
	var pid = $("#pid").val();
	    $.ajax({
	    	async:true,
	    	cache:false,
	    	url:"/goods/admin/AdminBookServlet",
	    	data:{method:"ajaxFindChildren",pid:pid},
	    	type:"POST",
	    	dataType:"json",
	    	success:function(arr){
	    		//3.得到cid,删除他的内容
	    		$("#cid").empty();//删除元素的子元素
	    		//4.添加头
	    	    $("#cid").append($("<option>======请选择2级分类======</option>"));
	    		//循环遍历数组，把每个对象转换成<option>添加cid中
	    		for(var i=0;i<arr.length;i++){
	    			var option = $("<option>").val(arr[i].cid).text(arr[i].cname);
	    			$("#cid").append(option);
	    		}
	    	}
	    })
}

function editForm(){
	$("#method").val("edit");
	$("#form").submit();
}

function deleteForm(){
	$("#method").val("delete");
	$("#form").submit();
}



</script>
  </head>
  
  <body>
    <input type="checkbox" id="box"><label for="box">编辑或删除</label>
    <br/>
    <br/>
  <div id="show">
    <div class="sm">Spring实战(第3版)（In Action系列中最畅销的Spring图书，近十万读者学习Spring的共同选择）</div>
    <img align="top" src="<c:url value='/book_img/23254532-1_w.jpg'/>" class="tp"/>
    <div id="book" style="float:left;">
    <table>
	    <tr>
				<td colspan="3">
					作者：${book.author} 著
				</td>
			</tr>
			<tr>
				<td colspan="3">
					出版社：${book.press }
				</td>
			</tr>
			<tr>
				<td colspan="3">出版时间：${book.publishtime }</td>
			</tr>
			<tr>
				<td>版次：${book.edition}</td>
				<td>页数：${book.pageNum}</td>
				<td>字数：${book.wordNum}</td>
			</tr>
			<tr>
				<td width="180">印刷时间：${book.printtime }</td>
				<td>开本：${book.booksize }开</td>
				<td>纸张：${book.paper }</td>
			</tr>
		</table>
	</div>
  </div>
  
  
  <div id='formDiv'>
   <div class="sm">&nbsp;</div>
   <form action="<c:url value='/admin/AdminBookServlet'/>" method="post" id="form">
   <input type="hidden" name="method" id="method">
   <input type="hidden" name="method" value="edit"/>
   	<input type="hidden" name="bid" value="${book.bid }"/>
    <img align="top" src="<c:url value='/book_img/23254532-1_w.jpg'/>" class="tp"/>
    <div style="float:left;">
	    <ul>
	    	<li>商品编号：${book.bid}</li>
	    	<li>传智价：<span class="price_n">&yen;${book.currPrice}</span></li>
	    	<li>定价：<span class="spanPrice">&yen;${book.price}</span>　折扣：<span style="color: #c30;">${book.discount}</span>折</li>
	    </ul>
		<hr style="margin-left: 50px; height: 1px; color: #dcdcdc"/>
		<table class="tab">
			<tr>
				<td colspan="3">
					作者：${book.author} 著
				</td>
			</tr>
			<tr>
				<td colspan="3">
					出版社：${book.press }
				</td>
			</tr>
			<tr>
				<td colspan="3">出版时间：${book.publishtime }</td>
			</tr>
			<tr>
				<td>版次：${book.edition}</td>
				<td>页数：${book.pageNum}</td>
				<td>字数：${book.wordNum}</td>
			</tr>
			<tr>
				<td width="180">印刷时间：${book.printtime }</td>
				<td>开本：${book.booksize }开</td>
				<td>纸张：${book.paper }</td>
			</tr>
			<tr>
				<td>
					一级分类：<select name="pid" id="pid" onchange="loadChildren()">
						<option value="">==请选择1级分类==</option>
						<c:forEach items="${parents }" var="parent">
			    		<option value="${parent.cid }" <c:if test="${parent.cid eq book.category.parent.cid}">selected="selected"</c:if> >${parent.cname}</option>
			    		</c:forEach>
					</select>
				</td>
				<td>
					二级分类：<select name="cid" id="cid">
						<option value="">==请选择2级分类==</option>
						<c:forEach items="${children }" var="child">
			    		<option value="${child.cid }" <c:if test="${book.category.cid eq child.cid }"> selected="selected"</c:if> >${child.cname }</option>
			    		</c:forEach>
					</select>
				</td>
				<td></td>
			</tr>
			<tr>
				<td colspan="2">
					<input onclick="editForm()" type="button" name="method" id="editBtn" class="btn" value="编　　辑">
					<input onclick="delForm()" type="button" name="method" id="delBtn" class="btn" value="删　　除">
				</td>
				<td></td>
			</tr>
		</table>
	</div>
   </form>
  </div>

  </body>
</html>
