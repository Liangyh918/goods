<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>cartlist.jsp</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script src="<c:url value='/js/round.js'/>"></script>

<link rel="stylesheet" type="text/css"
	href="<c:url value='/jsps/css/cart/list.css'/>">
<script type="text/javascript">
	$(function() {
		showTotal();

		/*  
		设置全选按钮的click事件
		 */
		$("#selectAll").click(function() {
			var bool = $("#selectAll").attr("checked");
			setItemCheckBox(bool);
			setJieSuan(bool);
		});

		/*
		设置每一个条目的checkbox的click事件
		 */
		$(":checkbox[name=checkboxBtn]")
				.click(
						function() {
							var all = $(":checkbox[name=checkboxBtn]").length;
							var selected = $(":checkbox[name=checkboxBtn][checked=true]").length;
							alert("all" + all);
							alert("selected" + selected);
							if (all == selected) {
								$("#selectAll").attr("checked", true);
								setJieSuan(true);
							}
							if (all > selected) {
								$("#selectAll").attr("checked", false);
								setJieSuan(true);
							}
							if (selected == 0) {
								$("#selectAll").attr("checked", false);
								setJieSuan(false);
							}

						});

		
		$(".jian").click(function(){
			// 获取cartItemId
			var id = $(this).attr("id").subString(0,32);
			//获取文本框中的数量
			var quantity = $("#"+id+"Quantity").val();
			//判断当前数量是否为1，如果为1，那就不是修改数量了,而是要删除了
			if(quantity == 1){
				if(confirm("您是否要删除该商品？")){
					location = "CartItemServlet?method=batchDelete&cartItemIds="+id;
				} else{
					sendUpdateQuantity(id,Number(quantity)-1);
				}
			}
		});
		
		$(".jia").click(function(){
			// 获取cartItemId
			var id = $(this).attr("id").substring(0,32);
			//获取文本框中的数量
			var quantity = $("#"+id+"Quantity").val();
			alert(quantity);
			sendUpdateQuantity(id,Number(quantity)+1);
		});
	});

	//计算总计
	function showTotal() {
		var total = 0;
		$(":checkbox[name=checkboxBtn][checked=true]").each(function() {
			var id = $(this).val();

			var text = $("#" + id + "Subtotal").text();
			total += Number(text);
		});
		$("#total").text(round(total));
	}

	/*
	 * 统一设置所有条目的复选按钮
	 */
	function setItemCheckBox(bool) {
		$(":checkbox[name=checkboxBtn]").attr("checked", bool);
	}

	/*
	 * 设置结算按钮样式和响应效果
	 */
	function setJieSuan(bool) {
		if (bool) {
			$("#jiesuan").removeClass("kill").addClass("jiesuan");
			$("#jiesuan").unbind("click");
		} else {
			$("#jiesuan").removeClass("jiesuan").addClass("kill");
			$("#jiesuan").click(function() {
				return false;
			})
		}
	}

	/*
	 * 批量删除
	 */
	function batchDelete() {
		//1.得到所有勾选的item
		//2.将被勾选的item的cartItemId加入到数组中
		//3.指定location为CartItemServlet,method=batchDelete,参数为cartItemIds=数组的toString
		var cartItemIds = new Array();
		$(":checkbox[name=checkboxBtn][checked=true]").each(function() {
			cartItemIds.push($(this).val());
		})
		location = "CartItemServlet?method=batchDelete&cartItemIds="+cartItemIds;
	}
	
	/*
	* 发送异步请求，更新商品数目
	*/
	function sendUpdateQuantity(id,quantity){
		$.ajax({
			async:false,
			cache:false,
			data:{method:"updateQuantity",cartItemId:id,quantity:quantity},
			datatype:"json",
			url:"/goods/CartItemServlet",
			type:"POST",
			success:function(result){
				//1.修改数量
				alert("result.quantity:"+result);
				var jsonResult = eval('('+result+')');
				$("#"+id+"Quantity").val(jsonResult.quantity);
				//2.修改小计
				alert("result.subtotal:"+jsonResult.subtotal);
				$("#"+id+"Subtotal").text(jsonResult.subtotal);
				//3.重新计算总计
				showTotal();
			}
		}
				)
		
	}
	
	function jiesuan(){
		/*
		1.将被勾选的条目的cartItemId加入数组cartItemIds
		2.将cartItemIds.toString()和总计total作为表单的两个hidden (cartItemIds和total)的值，提交到后台
		*/
		var cartItemIds =  new Array();
		$(":checkbox[name=checkboxBtn][checked=true]").each(
				
				function(){
					alert(1);
				cartItemIds.push($(this).val());
	}
				);
		alert(cartItemIds.toString());
		$("#cartItemIds").val(cartItemIds.toString());
		$("#hiddenTotal").val($("#total").text());
		//3.提交表单
		$("#jieSuanForm").submit();
	}
</script>
</head>
<body>

<c:choose>
<c:when test="${empty cartItemList }" >
<table width="95%" align="center" cellpadding="0" cellspacing="0" >
		<tr>
			<td align="right"><img align="top"
				src="<c:url value='/images/icon_empty.png'/>" /></td>
			<td><span class="spanEmpty">您的购物车中暂时没有商品</span></td>
		</tr>
	</table>
</c:when>
<c:otherwise>
<table width="95%" align="center" cellpadding="0" cellspacing="0">
		<tr align="center" bgcolor="#efeae5">
			<td align="left" width="50px"><input type="checkbox"
				id="selectAll" checked="checked" /><label for="selectAll">全选</label>
			</td>
			<td colspan="2">商品名称</td>
			<td>单价</td>
			<td>数量</td>
			<td>小计</td>
			<td>操作</td>
		</tr>



		<c:forEach items="${cartItemList}" var="cartItem">
			<tr align="center">
				<td align="left"><input value="${cartItem.cartItemId }"
					type="checkbox" name="checkboxBtn" checked="checked" /></td>
				<td align="left" width="70px"><a class="linkImage"
					href="<c:url value='/BookServlet?method=findByBid&bid=${cartItem.book.bid}'/>"><img
						border="0" width="54" align="top"
						src="<c:url value='/${cartItem.book.image_b}'/>" /></a></td>
				<td align="left" width="400px"><a
					href="<c:url value='/BookServlet?method=findByBid&bid=${cartItem.book.bid}'/>"><span>${cartItem.book.bname}</span></a>
				</td>
				<td><span>&yen;<span class="currPrice"
						id="${cartItem.cartItemId }CurrPrice">${cartItem.book.currPrice}</span></span></td>
				<td><a class="jian" id="${cartItem.cartItemId }Jian"></a><input
					class="quantity" readonly="readonly"
					id="${cartItem.cartItemId }Quantity" type="text"
					value="${cartItem.quantity}" /><a class="jia"
					id="${cartItem.cartItemId }Jia"></a></td>
				<td width="100px"><span class="price_n">&yen;<span
						class="subTotal" id="${cartItem.cartItemId }Subtotal">${cartItem.subTotal}</span></span>
				</td>
				<td><a
					href="<c:url value='/CartItemServlet?method=batchDelete&cartItemIds=${cartItem.cartItemId}'/>">删除</a></td>
			</tr>
		</c:forEach>





		<tr>
			<td colspan="4" class="tdBatchDelete"><a
				href="javascript:batchDelete();">批量删除</a></td>
			<td colspan="3" align="right" class="tdTotal"><span>总计：</span><span
				class="price_t">&yen;<span id="total">${cartItem.total}</span></span>
			</td>
		</tr>
		<tr>
			<td colspan="7" align="right"><a
				href="javascript:jiesuan();" id="jiesuan"
				class="jiesuan"></a></td>
		</tr>
	</table>
	<form id="jieSuanForm" action="/goods/CartItemServlet"
		method="post">
		<input type="hidden" name="cartItemIds" id="cartItemIds" /> 
		<input type="hidden" name="total" id="hiddenTotal" /> 
		<input type="hidden" name="method" value="loadCartItems" />
	</form>
</c:otherwise>
</c:choose>

	


</body>
</html>
