<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div style="padding-top: 0px;">
<div class="jumbotron" style="padding-top:5px">
	<h3>商品信息系统</h3>
	<div>
		
		<p>1. 该商品信息系统是基于 github 上 webmagic 快速构建的实时商品抓取系统;</p>
		<p>定义：定向抓取，定向抓取是一种特定的抓取技术，目标站点是已知的，站点的页面是已知的，站点发现等等特性。</p>
		<p>本商品系统主要用到  mysql + webmagic + 线程池  </p>
		<p>名词解析：</p>
		<p>
		<ul>
			<li>数据抓取环：抓取环指的是spider在配置文件中获取url，从互联网上下载网页，再从网页里面获取下一个URL的一个流程。</li>
			<li>Linkbase：链接库的存储模块，包含一般的链接信息；是抓取系统的核心，本系统使用的是  webmagic 实现。</li>
			<li>XPATH：一门在 XML 文档中查找信息的语言，XPath 可用来在 XML 文档中对元素和属性进行遍历， 是 W3C XSLT 标准的主要元素。使用XPATH以及相关工具lib进行链接抽取和信息抽取。</li>
			<li>Firebug：一个Firefox的插件，支持点击页面元素，获取XPATH路径，用于编辑配置模板和获取网络请求。</li>
			<li>JAVA EE：用于数据的web页面展示，，系统监控反馈。在这里主要是用来管理一个数据库，从而使商品信息系统管理更方便</li>
			<li>webmaigc ：github 开源的基于 java 语言的单机爬虫，该爬虫实际上包含大多数网页抓取的工具包，用于爬虫下载端以及抽取端。</li>
			<li>列表页：指的商品信息所有页面</li>
			<li>详情页：比如商品B2C的抓取中，特指商品页面，比如：<a href="http://item.jd.com/1378538.html">http://item.jd.com/1378538.html</a></li>
		</ul>
		</p>
		
		<p>一个完整的抓取数据流：</p>
		<ul>
			<li>1 用户提供种子URL</li>
			<li>2 种子URL进入linkbase中新URL队列中</li>
			<li>3 调度模块选取url进入到抓取模块的待抓取队列中</li>
			<li>4 抓取模块读取站点的配置文件，按照执行的频率进行抓取</li>
			<li>5 抓取的结果返回到pipeline接口中，并完成连接的抽取</li>
			<li>6 新发现的连接在linkbase里面进行dedup，并push到linkbase的新URL模块里面</li>
			<li>7 调度模块选取url进入抓取模块的待抓取队列，goto 4</li>
			<li>8 end</li>
		</ul>
		
	</div>
	
	
</div>
</div>

<script type="text/javascript">
function userDelete(id){
	if(confirm("确定要删除这条记录吗?")){
		$.post("${pageContext.request.contextPath}/inter/register.do",{id:id},
			function(result){
				var result=eval(result);
				if(result.errorInfo){
					alert(result.errorInfo)
				}else{
					alert("删除成功！！");
				}
			}
		);
	}
}

</script>