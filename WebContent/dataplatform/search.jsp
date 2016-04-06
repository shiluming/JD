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
	  		<input id="queryStr" name="queryStr" type="text" class="form-control" placeholder="Search for...">
	  		<span class="input-group-btn">
	  			<button id="searchBtn" class="btn btn-default" type="submit">Go!</button>
			</span>
		</div>
		</form>
		</div>
		<div class="col-lg-3">
		
		</div>
	</div>
	
</div>

<script type="text/javascript">

$(document).ready(function(){
	$("#searchBtn").click(function(){
		var query = $("#queryStr").val();
		$("#table").attr("data-url","${pageContext.request.contextPath}/index/search.do?query="+query);
		$.get("${pageContext.request.contextPath}/index/search.do?query="+query,function(data,status){
		   
		    $('#table').bootstrapTable({
		        data: data
		    });
		    
		  });
		
	  });

});

function queryParams() {
    return {
        type: 'owner',
        sort: 'updated',
        direction: 'desc',
        per_page: 10,
        page: 1
    };
}
function search() {
	var query = $("#queryStr").val();
	alert(query)
	$("input").submit();
	
};

</script>
