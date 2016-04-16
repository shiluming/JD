<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap3/js/highcharts.js"></script>
<script type="text/javascript" src="http://cdn.hcharts.cn/highcharts/modules/exporting.js"></script>
<script type="text/javascript">

var province = new Array();
var provinceNum = new Array();
 $(function () {
	 $(function () {
		 var id = "10000233";
		 $.get("${pageContext.request.contextPath}/comments/anyComments.do",{itemid:id},function(data,status){
			 if(data=="") {
				 alert("请先抓取该商品评论信息！");
			 }
			 alert("省份"+data[0].PROVINCES[0]);
			 alert("人数" + data[0].PROVINCES[1].length);
			 province = data[0].PROVINCES[0];
			 provinceNum = data[0].PROVINCES[1];
			 alert(province);
			 initCharts();
		 });
		 
	});
	    
});
 
function initCharts() {
	$('#container').highcharts({
        chart: {
            type: 'column',
            margin: [ 50, 50, 100, 80]
        },
        title: {
            text: '用户省份信息分析图'
        },
        xAxis: {
            categories: 
				province
            ,
            labels: {
                rotation: -45,
                align: 'right',
                style: {
                    fontSize: '13px',
                    fontFamily: 'Verdana, sans-serif'
                }
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Population (millions)'
            }
        },
        legend: {
            enabled: false
        },
        tooltip: {
            pointFormat: '人数 : <b>{point.y:.1f} </b>',
        },
        series: [{
            name: 'Population',
            data: provinceNum,
            dataLabels: {
                enabled: true,
                rotation: -90,
                color: '#FFFFFF',
                align: 'right',
                x: 4,
                y: 10,
                style: {
                    fontSize: '13px',
                    fontFamily: 'Verdana, sans-serif',
                    textShadow: '0 0 3px black'
                }
            }
        }]
    });
}
</script>
 
<div id="container" style="min-width:700px;height:400px">


</div>



