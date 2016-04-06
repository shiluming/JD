<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap3/css/bootstrap-table.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap3/css/dataTables.bootstra.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap3/css/bootstrap.min.css">
<script src="${pageContext.request.contextPath}/bootstrap3/js/bootstrap-table.js"></script>
<div>
	<div class="panel">
		<div class="col-lg-3">
		</div>
		<div class="col-lg-6">
		<form action="${pageContext.request.contextPath}/index/showsearch.do" method="post">
		<div class="input-group input-group-lg" align="center">
	  		<input id="queryStr" name="queryStr" type="text" class="form-control" placeholder="${query}">
	  		<span class="input-group-btn">
	  			<button id="searchBtn" class="btn btn-default" type="submit">Go!</button>
			</span>
		</div>
		</form>
		</div>
		<div class="col-lg-3">
		
		</div>
	</div>
	
	<div class="panel">
	<table data-toggle="table"
       data-card-view="true">
    <thead>
    <tr>
        <th >序号</th>
        <th >商品名字</th>
        <th >价格</th>
        <th >评论数</th>
        <th>商店名字</th>
        <th>URL</th>
    </tr>
    </thead>
    <c:forEach var="search" items="${list }" varStatus="status">
    	<tr>
	  		<td>${status.index+1 }</td>
	  		<td>${search.others }</td>
	  		<td>${search.itemprice }</td>
	  		<td>${search.commentCounts }</td>
	  		<td>${search.itemShop }</td>
	  		<td><a href="${search.comments }" target="_blank">${search.comments }</a></td>
	  	</tr>
    </c:forEach>
</table>
	</div>
</div>

<script type="text/javascript">





</script>
