<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation"  >
  <div class="container">
    <div class="navbar-header">
      <a class="navbar-brand " href="#">商品信息系统</a>
    </div>
    <div id="navbar" class="navbar-right">
      <a class="navbar-brand" href="#">当前用户：${currentUser.userName }『<font color="red" size="2px">${currentUser.role}</font>』</a>
    </div>
  </div>
</nav>