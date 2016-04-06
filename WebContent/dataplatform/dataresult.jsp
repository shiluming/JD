<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap3/css/bootstrap-table.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap3/css/dataTables.bootstra.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap3/css/bootstrap.min.css">
<script src="${pageContext.request.contextPath}/bootstrap3/js/bootstrap-table.js"></script>
<div>
	<table id="example" 
		data-toggle="table"
		data-url="${pageContext.request.contextPath}/data/getProductData.do"
		data-search="true"
		data-pagination="true"
		data-query-params="queryParams"
		class="table table-striped table-bordered table-hover table-condensed" style="margin-bottom: 0px;">
	  <thead>
	  <tr>
	  	<th data-field="id">序号</th>
	  	<th data-field="itemname">商品名称</th>
	  	<th data-field="itemid">商品ID</th>
	  	<th data-field="itemprice">价格</th>
	  	<th data-field="commentCounts">评论数</th>
	  	<th data-field="itemShop">商店</th>
	  	<th >查看评论</th>
	  	<th >是否下架</th>
	  </tr>
	  </thead>
	  <tbody>
	  
	  </tbody>
	</table>
	
</div>


<script type="text/javascript">

function queryParams() {
    return {
        type: 'owner',
        sort: 'updated',
        direction: 'desc',
        per_page: 100,
        page: 1
    };
}


</script>
<!-- 客户端分页demo -->