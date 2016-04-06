<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap3/css/bootstrap-table.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap3/css/dataTables.bootstra.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap3/css/bootstrap.min.css">
<script src="${pageContext.request.contextPath}/bootstrap3/js/bootstrap-table.js"></script>
<div>
	<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title"><b>调度控制台</b></h3>
  </div>
  <div class="panel-body">
	  <div class="form-group">
	    <label class="col-sm-6 control-label"><h4>商品信息爬虫控制</h4> <font color="red">${info}</font></label>
		 <button onclick="javascript:window.location.href='${pageContext.request.contextPath}/spider/start.do'" class="btn btn-primary">start</button>
	  </div>
	  <div class="form-group">
	  	<div class="col-sm-6"> 
	  	<label class="col-sm-5 control-label">概览</label>
	  	<label class="col-sm-5 control-label">无效URL：</label>
	  	<div id="useurl" class="col-sm-2"></div>
	  	</div>
	  	
	  	<div class="col-sm-6">
	  	<label class="col-sm-7 control-label">有效URL：</label>
	  	<div id="unusefullurl" class="col-sm-2"></div>
	  	</div>
	  	
	  </div>
	  
	  <br></br>
	  <ul class="nav nav-list"> 
     	<li class="divider"></li>  
	  </ul>
	  
	  <div class="form-group">
	    <label class="col-sm-3 control-label">商品评论爬虫控制</label>
	    <br/>
	    <div class="col-sm-9" align="left">
	    <table align="left">
	    	<tr>输入要获取评论的商品id</tr>
	    	<tr><input type="text" class="form-control" id="id" name="id" style="width: 200px"></tr>
	    	<tr><button type="button" class="btn btn-primary" onclick="comments()">开始执行</button>&nbsp;&nbsp;</tr>
	    </table>
	      
	    </div>
	  </div>
	  
	  <div class="form-group">
	    <div class="col-sm-offset-2 col-sm-10">
	    </div>
	  </div>
  </div>
</div>
	
</div>


<script type="text/javascript">

function comments() {
	var id = $("#id").val();
	if(id=="") {
		alert("请输入id");
		return ;
	}
	$.get("",function(data,status){
		
	});
}


$(document).ready(function(){
    $.get("${pageContext.request.contextPath}/url/usefullURL.do",function(data,status){
      $("#unusefullurl").append(data.count);
    });
    $.get("${pageContext.request.contextPath}/url/unusefullURL.do",function(data,status){
        $("#useurl").append(data.count);  
    });
});



</script>
<!-- 客户端分页demo -->