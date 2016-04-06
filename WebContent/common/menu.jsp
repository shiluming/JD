<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
	function logout(){
		if(confirm('您确定要退出系统吗?')){
			window.location.href="${pageContext.request.contextPath}/user/logout.do";
		}
	}
</script>
<div class="list-group">
  <a href="#" class="list-group-item active">
    系统菜单
  </a>
  	<c:if test="${currentUser.role=='管理员'}">
	  <a href="${pageContext.request.contextPath}/system/list.do?page=1" class="list-group-item">系统记录</a>
	  <a href="${pageContext.request.contextPath}/user/list.do?" class="list-group-item">用户管理</a>
	</c:if>
	<c:if test="${currentUser.role=='使用者' || currentUser.role=='管理员' }">
	  <a href="${pageContext.request.contextPath}/inter/list.do" class="list-group-item">接口管理</a>
	  <a href="${pageContext.request.contextPath}/index/list.do" class="list-group-item">JD索引</a>
	  <a href="${pageContext.request.contextPath}/spider/list.do" class="list-group-item">控制台</a>
	</c:if>
	  <a href="${pageContext.request.contextPath}/data/list.do?page=1" class="list-group-item">数据平台</a>
	  <a href="${pageContext.request.contextPath}/data/search.do" class="list-group-item">商品搜索</a>
	  <a href="${pageContext.request.contextPath}/data/priceAnaly.do" class="list-group-item">数据分析</a>
	  <a href="javascript:logout()" class="list-group-item">安全退出</a>
  
</div>