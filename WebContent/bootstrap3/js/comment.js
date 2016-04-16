
var province = new Array();
var provinceNum = new Array();
var level = new Array();
var levelNum = new Array();
function btn_comment(id) {
	
	/*<div id="container" style="min-width:700px;height:400px">


		</div>
	 * 
	 */
	var div = "<div id='container1' style='min-width:700px;height:400px'>"
			+"</div>";
	 
	$(function () {
		 
		 $.get("../comments/anyComments.do",{itemid:id},function(data,status){
			 if(data=="") {
				 alert("请先抓取该商品评论信息！");
			 }
			 province = data[0].PROVINCES[0];
			 provinceNum = data[0].PROVINCES[1];
			 level = data[0].USERLEVEL[0];
			 levelNum = data[0].USERLEVEL[1];
			 $("#title").html("");
			 $("#title").append(data[2].USER[0]);
			 initCharts();
			 $("#container1").remove();
			 $("#content").append(div);
			 levelCharts();
		 });
		 
	});
	 
	
	    
}

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

function levelCharts() {
	$('#container1').highcharts({
        chart: {
            type: 'column',
            margin: [ 50, 50, 100, 80]
        },
        title: {
            text: '用户会员等级分析图'
        },
        xAxis: {
            categories: 
				level
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
            pointFormat: '数量: <b>{point.y:.1f} </b>',
        },
        series: [{
            name: 'Population',
            data: levelNum,
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