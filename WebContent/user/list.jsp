<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
function userDelete(id){
	if(confirm("确定要删除这条记录吗?")){
		$.post("${pageContext.request.contextPath}/user/delete.do",{id:id},
			function(result){
				var result=eval(result);
				if(result.errorInfo){
					alert(result.errorInfo)
				}else{
					alert("删除成功！！");
					window.location.href="${pageContext.request.contextPath}/user/list.do";
				}
			}
		);
	}
}

</script>
<div class="col-md-6">
</div>
<div class="col-md-6">
	<button type="button" class="btn btn-primary" style="float: right;"
	 onclick="javascript:window.location.href='${pageContext.request.contextPath}/user/preSave.do'">添加用户</button>
</div>

<div>
	<table class="table table-hover  table-bordered table-striped" style="margin-bottom: 0px;">
	  <tr>
	  	<th>序号</th>
	  	<th>用户名</th>
	  	<th>密码</th>
	  	<th>地址</th>
	  	<th>电话号码</th>
	  	<th>角色</th>
	  	<th>操作</th>
	  </tr>
	  <c:forEach var="user" items="${userList }" varStatus="status">
	  	<tr>
	  		<td>${status.index+1 }</td>
	  		<td>${user.userName }</td>
	  		<td>${user.passWord }</td>
	  		<td>${user.address }</td>
	  		<td>${user.tellPhone }</td>
	  		<td>${user.role }</td>
	  		<td>
	  			<button type="button" class="btn btn-info btn-xs" onclick="javascript:window.location.href='${pageContext.request.contextPath}/user/preSave.do?id=${user.id }'">修改</button>
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



