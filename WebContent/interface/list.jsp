<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
function userDelete(sw,id){
	if(confirm("确定要关闭该接口吗?")){
		$.post("${pageContext.request.contextPath}/inter/btn.do",{sw:sw,id:id},
			function(result){
				var result=eval(result);
				if(result.errorInfo){
					alert(result.errorInfo)
				}else{
					alert("关闭成功！！");
					window.location.href="${pageContext.request.contextPath}/inter/list.do";
				}
			}
		);
	}
}
function start(sw,id){
	if(confirm("确定要开启该接口吗?")){
		$.post("${pageContext.request.contextPath}/inter/btn.do",{sw:sw,id:id},
			function(result){
				var result=eval(result);
				if(result.errorInfo){
					alert(result.errorInfo)
				}else{
					alert("开启成功！！");
					window.location.href="${pageContext.request.contextPath}/inter/list.do";
				}
			}
		);
	}
}

</script>
<div class="col-md-6">
</div>
<div class="col-md-6">
</div>

<div>
	<table class="table table-hover  table-bordered table-striped" style="margin-bottom: 0px;">
	  <tr>
	  	<th>序号</th>
	  	<th>接口名称</th>
	  	<th>接口类型</th>
	  	<th>地址</th>
	  	<th>状态</th>
	  	<th>操作</th>
	  </tr>
	  <c:forEach var="user" items="${list }" varStatus="status">
	  	<tr>
	  		<td>${status.index+1 }</td>
	  		<td>${user.name }</td>
	  		<td>${user.type }</td>
	  		<td>${user.address }</td>
	  		<td>${user.flag }</td>
	  		<td>
	  			<button type="button" class="btn btn-info btn-xs" onclick="start(${1},${user.id})">启用</button>
	  			<button type="button" class="btn btn-danger btn-xs" onclick="userDelete(${0 },${user.id})">停止</button>
	  		</td>
	  	</tr>
	  </c:forEach>
	</table>
</div>



