<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
function userDelete(id){
	if(confirm("确定要删除这条记录吗?")){
		$.post("${pageContext.request.contextPath}/system/delete.do",{id:id},
			function(result){
				var result=eval(result);
				if(result.errorInfo){
					alert(result.errorInfo)
				}else{
					alert("删除成功！！");
					window.location.href="${pageContext.request.contextPath}/system/list.do";
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
	  	<th>用户名</th>
	  	<th>ip</th>
	  	<th>地点</th>
	  	<th>时间</th>
	  	<th>类型</th>
	  	<th>登录结果</th>
	  	<th>操作</th>
	  </tr>
	  <c:forEach var="user" items="${list }" varStatus="status">
	  	<tr>
	  		<td>${status.index+1 }</td>
	  		<td>${user.username }</td>
	  		<td>${user.ip }</td>
	  		<td>${user.address }</td>
	  		<td>${user.date }</td>
	  		<td>${user.type}</td>
	  		<td>${user.others }</td>
	  		<td>
	  			<button type="button" class="btn btn-danger btn-xs" onclick="userDelete(${user.id })">删除</button>
	  		</td>
	  	</tr>
	  </c:forEach>
	</table>
	<nav >
		<ul class="pagination">
			${pageCode }
		</ul>
	</nav>
</div>



