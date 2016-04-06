<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap3/css/bootstrap-table.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap3/css/dataTables.bootstra.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap3/css/bootstrap.min.css">
<script src="${pageContext.request.contextPath}/bootstrap3/js/bootstrap-table.js"></script>
<div>
	<div class="btn-group btn-group-lg" role="group" aria-label="...">
  		<button type="button" class="btn btn-default" onclick="javascript:window.location.href='${pageContext.request.contextPath}/data/list.do?page=1'">商品信息</button>
  		<button type="button" class="btn btn-default" onclick="javascript:window.location.href='${pageContext.request.contextPath}/data/unuseURL.do?page=1'">无效URL信息</button>
  		<button type="button" class="btn btn-default" onclick="javascript:window.location.href='${pageContext.request.contextPath}/data/useURL.do?page=1'">有效URL信息</button>
	</div>

	<table id="example"
		data-toggle="table"
		data-url="${pageContext.request.contextPath}/url/usefull.do"
		data-pagination="true"
		data-side-pagination="server"
		data-page-list="[10, 20, 50, 100, 200]"
		class="table table-striped table-bordered table-hover table-condensed" style="margin-bottom: 0px;">
	  <thead>
	  <tr>
	  	<th data-field="id">序号1</th>
	  	<th data-field="url">URL</th>
	  	<th data-field="itemid">商品ID</th>
	  	<th data-field="time">时间</th>
	  	<th data-field="threadname">线程名称</th>
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
<!-- 服务端分页demo -->