<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script type="text/javascript" src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="http://cdn.hcharts.cn/highcharts/highcharts.js"></script>
<script type="text/javascript" src="http://cdn.hcharts.cn/highcharts/exporting.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap3/js/comment.js"></script>
<script type="text/javascript" >
 
 $(function () {
	 var t1,t2,t3,t4,t5,t6,t7;
	 $.get("${pageContext.request.contextPath}/analyse/getpriceAnay.do",function(data,status){
		 t1 = data.s0;
		 t2 = data.s1;
		 t3 = data.s2;
		 t4 = data.s3;
		 t5 = data.s4;
		 t6 = data.s5;
		 t7 = data.s6;
		 
		 var all = Number(t1)+Number(t2)+Number(t3)+Number(t4)+Number(t5)+Number(t6)+Number(t7);
		t1 = t1/all;
		t2 = t2/all;
		t3 = t3/all;
		t4 = t4/all;
		t5 = t5/all;
		t6 = t6/all;
		t7 = t7/all;
		 
		 $('#container').highcharts({
		        chart: {
		            plotBackgroundColor: null,
		            plotBorderWidth: null,
		            plotShadow: false
		        },
		        title: {
		            text: '京东商城商品价格区间图 2016 3月份'
		        },
		        tooltip: {
		    	    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		        },
		        plotOptions: {
		            pie: {
		                allowPointSelect: true,
		                cursor: 'pointer',
		                dataLabels: {
		                    enabled: true,
		                    color: '#000000',
		                    connectorColor: '#000000',
		                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
		                }
		            }
		        },
		        series: [{
		            type: 'pie',
		            name: 'Browser share',
		            data: [
		                ['其他',   t1],
		                ['500元之内',       t2],
		                {
		                    name: '500-1000元之间',
		                    y: t3,
		                    sliced: true,
		                    selected: true
		                },
		                ['1000-3000之间',    t4],
		                ['3000-5000之间',     t5],
		                ['5000-10000之间',   t6],
		                ['10000以上',   t7]
		            ]
		        }]
		    });
	 });
	 
	 var all = t1+t2+t3+t4+t5+t6+t7;
	});
 
function btn_click() {
	var id = $("#id").val();
	btn_comment(id);
}
</script>
<div class="row">
	<div class="col-lg-4">
	<div class="input-group">
      <input type="text" id="id" class="form-control" placeholder="输入商品ID">
      <span class="input-group-btn">
        <button class="btn btn-default" type="button" onclick="btn_click()">评论分析</button>
      </span>
    </div><!-- /input-group -->
	</div>
	<div class="col-lg-8">
	
	</div>
</div>
<br>
 <div id="content" class="panel panel-default">
 <h4><div class="panel-heading" style="text-align: center" id="title"></div></h4	>
  <div class="panel-heading">
    <h3 class="panel-title"><b>数据分析展示台:</b><div style="text-align: center"><b id=""></b></div></h3>
  </div>
<div id="container" style="min-width:700px;height:400px">

</div>

</div>
