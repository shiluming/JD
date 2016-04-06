<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap3/css/bootstrap-table.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap3/css/dataTables.bootstra.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap3/css/bootstrap.min.css">
<script src="${pageContext.request.contextPath}/bootstrap3/js/bootstrap-table.js"></script>
<script type="text/javascript">
	function createIndex() {
		var id;
		$.get("${pageContext.request.contextPath}/index/indexLogInfo.do?current=1",function(data,status){
			var obj = eval(data);
			id = obj.rows[0].id;
			alert("id="+id);
			$.get("${pageContext.request.contextPath}/index/create.do?id="+id,function(data,status){
				alert(status);
				alert(data);
			});
		});
	}
	function buckupIndex() {
		$.get("${pageContext.request.contextPath}/index/buckup.do",function(data,status){
			alert(status);
			alert(data);
		});
	}
	
	function deleteIndex() {
		$.get("${pageContext.request.contextPath}/index/delete.do?id="+id,function(data,status){
			var obj = eval(data);
			alert(obj.result);
		});
		
	}
	$(document).ready(function(){
		$.get("${pageContext.request.contextPath}/index/indexLogInfo.do?current=1",function(data,status){
			var obj = eval(data);
			
			$("#time").append(obj.rows[0].createtime);
			$("#dir").append(obj.rows[0].indexdir);
			$("#number").append(obj.rows[0].indexnum);
			$("#sparetime").append(obj.rows[0].sparetime);
			$("#user").append(obj.rows[0].indexuser);
			$("#buckup").append(obj.rows[0].isbuckup);
			$("#buckupdir").append(obj.rows[0].buckupdir);
		});
	});
</script>
<div class="col-md-4">
	<button type="button" class="btn btn-primary" style="float: left;"
	 	onclick="createIndex()">创建索引</button>
</div>
<div class="col-md-4">
	<button type="button" class="btn btn-primary" style="float: left;"
	 	onclick="buckupIndex()">备份索引</button>&nbsp;&nbsp;&nbsp;&nbsp;
</div>

<div class="col-md-4">
	<button type="button" class="btn btn-primary" style="float: right;"
	 onclick="deleteIndex()">删除索引</button>
</div>
<div>
	<div class="col-lg-3">
		<div><h3>索引log</h3></div>
		<table id="tb_index"
		data-toggle="table"
		data-url="${pageContext.request.contextPath}/index/indexLogInfo.do"
		data-pagination="true"
		data-side-pagination="server"
		data-page-list="[10, 20]"
		class="table table-striped table-bordered table-hover table-condensed" style="margin-bottom: 0px;">
	  <thead>
	  <tr>
	  	<th data-field="id">ID</th>
	  	<th data-field="createtime">创建时间</th>
	  	<th >操作</th>
	  </tr>
	  </thead>
	  <tbody>
	  
	  </tbody>
	</table>
	</div>
	<div class="col-lg-6">
		<div><h3>索引信息：</h3></div>
		<table class="table">
  			
  			<tr>
  				<td>1. 创建时间：</td>
  				<td id="time"></td>
  			</tr>
  			<tr>
  				<td>2. 索引目录：</td>
  				<td id="dir"></td>
  			</tr>
  			<tr>
  				<td>3. 商品索引数量：</td>
  				<td id="number"></td>
  			</tr>
  			<tr>
  				<td>4. 创建索引花费时间：</td>
  				<td id="sparetime"></td>
  			</tr>
  			<tr>
  				<td>5. 创建索引用户：</td>
  				<td id="user"></td>
  			</tr>
  			<tr>
  				<td>6. 是否备份：</td>
  				<td id="buckup"></td>
  				
  			</tr>
  			<tr>
  				<td>7. 备份目录：</td>
  				<td id="buckupdir"></td>
  			</tr>
		</table>
	<div class="col-lg-3">
		
	</div>
	</div>
</div>



