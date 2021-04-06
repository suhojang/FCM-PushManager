<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/include/declare.jspf" %>
<script src="<%=JS_PATH%>Chart.bundle.js"></script>
<script src="<%=JS_PATH%>utils.js"></script>
<script src="<%=JS_PATH%>modal.js"></script>
<link rel="stylesheet" type="text/css" href="<%=CSS_PATH %>modal.css" />
<script type="text/javaScript">
function fn_init(){
	var PS_CNT		= "${data.PSCNT}";		//푸시 발송 성공 건
	var PF_CNT		= "${data.PFCNT}";		//푸시 발송 실패 건
	var SMS_CNT		= "${data.SMSCNT}";		//SMS 발송 건
	var PR_CNT		= "${data.PRCNT}";		//수신 성공 건
	var PRF_CNT		= "${data.PRFCNT}";		//수신 실패 건
	var PRD_CNT		= "${data.PRDCNT}";		//읽음 성공 건
	var PRDF_CNT	= "${data.PRDFCNT}";	//읽음 실패 건
	
	var config = {type:'pie', data:{datasets:[{data:[],backgroundColor:[],label:'Dataset 1'}],labels:[]}, options:{responsive:true}};
	var ctx = document.getElementById('chart-area').getContext('2d');
	config.data.datasets[0].data = [PS_CNT, PF_CNT, SMS_CNT];
	config.data.datasets[0].backgroundColor = [window.chartColors.green, window.chartColors.red, window.chartColors.yellow];
	config.data.labels = ['Push성공','Push실패','SMS'];
	window.myPie = new Chart(ctx, config);
 
	config = {type:'pie', data:{datasets:[{data:[],backgroundColor:[],label:'Dataset 1'}],labels:[]}, options:{responsive:true}};
	var ctx2 = document.getElementById('chart-area2').getContext('2d');
	config.data.datasets[0].data = [PS_CNT, PF_CNT];
	config.data.datasets[0].backgroundColor = [window.chartColors.blue, window.chartColors.red];
	config.data.labels = ['성공','실패'];
	window.myPie = new Chart(ctx2, config);

	config = {type:'pie', data:{datasets:[{data:[],backgroundColor:[],label:'Dataset 1'}],labels:[]}, options:{responsive:true}};
	var ctx3 = document.getElementById('chart-area3').getContext('2d');
	config.data.datasets[0].data = [PR_CNT, PRF_CNT];
	config.data.datasets[0].backgroundColor = [window.chartColors.blue, window.chartColors.red];
	config.data.labels = ['성공','실패'];
	window.myPie = new Chart(ctx3, config);

	config = {type:'pie', data:{datasets:[{data:[],backgroundColor:[],label:'Dataset 1'}],labels:[]}, options:{responsive:true}};
	var ctx4 = document.getElementById('chart-area4').getContext('2d');
	config.data.datasets[0].data = [PRD_CNT, PRDF_CNT];
	config.data.datasets[0].backgroundColor = [window.chartColors.blue, window.chartColors.red];
	config.data.labels = ['읽음','읽지않음'];
	window.myPie = new Chart(ctx4, config);
}

//페이지 이동
function fn_page(pageNo){
	if($("#stDate").val==""){
		alert("조회 시작일을 선택하여 주십시오.");
		return;
	}
	if($("#etDate").val==""){
		alert("조회 종료일을 선택하여 주십시오.");
		return;
	}
	
	fn_loadingModal_show();
	
	var frm	= document.frm;
	
	frm.pageNo.value = pageNo;
	frm.action = "/SEL_STA_001000.do";
	frm.submit();	
}

//상세보기
function fn_detail(no){
	fn_loadingModal_show();
	$("#resend").html("<a class=\"btn kcw\" href=\"javascript:void(0);\" onclick=\"javascript:fn_resend("+no+");\"><strong style=\"padding: 15px;\">실패건 재전송</strong></a>");
	
	$.ajax({
		type: "POST"
		,url: "/SEL_STA_001001.do"
		,dataType: "json"
		,data: {
			"PSHG_SNO":no
		}
		,timeout:"10000"
		,success: function(data){
			fn_loadingModal_hide();
			
			s	= data['RESULT'];
			
			var temp	= "";
			if(s==""){
				temp += "<tr>";
				temp += "<td scope=\"row\" colspan=\"3\">전송결과가 없습니다</td>";
				temp += "</tr>";
				
				$("#result").html(temp);
				fn_popup_display("show");
				
				return;
			}
			
			for(var i=0;i<s.length;i++){
				var rsno	= s[i].RSNO;
				var psssno	= s[i].PSSNO;
				var rsid	= s[i].RSID;
				var usrnm	= s[i].USRNM;
				var jsnm	= s[i].JSNM==null?"":s[i].JSNM;
				var bsnm	= s[i].BSNM==null?"":s[i].BSNM;
				var tknid	= s[i].TKNID;
				var sts		= s[i].STS;
				var rst		= s[i].RST;
				var useyn	= s[i].USEYN;
				var rdt		= s[i].RDT;
				var udt		= s[i].UDT;
				
				var clazz	= sts=="99"?"t_red":"t_blue";
				var style	= (i==0)?"border-top-width:0px;":"";
				
				temp	+= "<tr>";
				temp	+= "<td scope=\"row\" style="+style+">"+rsid+"</td>";
				temp	+= "<td scope=\"row\" style="+style+">"+usrnm+"</td>";
				temp	+= "<td scope=\"row\" style="+style+">"+jsnm+"/"+bsnm+"</td>";
				temp	+= "<td scope=\"row\" class="+clazz+" style="+style+">"+(sts==99?"실패 : "+rst.substring(0,rst.indexOf("[")):"전송성공")+"</td>";
				temp	+= "</tr>";
			}
			$("#result").html(temp);
			
			fn_popup_display("show");
		}
		,error: function(request,status,error){
			alert(request+", "+status+", "+error);
			fn_loadingModal_hide();
		}
	});
}

function fn_close(){
	fn_popup_display("close");
}

function fn_resend(no){
	var frm	= document.frm;
	
	frm.PSHG_SNO.value	= no;
	frm.action	= "/SEL_STA_001002.do";
	frm.submit();
}

//csv다운로드
function fn_csv_down(){
	var searchCondition	= $("#searchCondition").val();
	var stDate	= $("#stDate").val();
	var etDate	= $("#etDate").val();
	
	document.location.href = "/STA_CSV_DOWN.do?searchCondition="+searchCondition+"&stDate="+stDate+"&etDate="+etDate;
}

function fn_date_show(obj){
	$("#"+obj).datepicker("show");
}

function fn_popup_display(act, type){
	$("#popup_bg").css(			"display",	act=="show"?"block":"none");
	$("#popup_div_01").css(		"display",	act=="show"?"block":"none");
	$("#popup_div_sub_01").css(	"display",	act=="show"?"block":"none");
}

function fn_loadingModal_show(){
    $("#loadingModal").modal({
    	  keyboard : false
    	 ,backdrop : 'static'
    });
}
function fn_loadingModal_hide(){
    $("#loadingModal").modal('hide');
}
</script>

<script>
$(function() {
	$( "#stDate, #etDate" ).datepicker({
		dateFormat: 'yy-mm-dd',
		prevText: '이전 달',
		nextText: '다음 달',
		monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		dayNames: ['일','월','화','수','목','금','토'],
		dayNamesShort: ['일','월','화','수','목','금','토'],
		dayNamesMin: ['일','월','화','수','목','금','토'],
		showMonthAfterYear: true,
		yearSuffix: '년',
		changeMonth: true,
		changeYear: true,
		yearRange: 'c-10:c+10'
	});
});
</script>
<style>
tr.boldrow {font-weight: bold;}
tr.boldrow > td {color:#960707; font-size: 13px;}
tr.boldrow > td > a {color:#960707; font-size: 13px;}
</style>

<body onload="javascript:fn_init();">
<!-- Loading Modal -->
<div class="modal fade" id="loadingModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2" aria-hidden="true">
	<div class="modal-dialog" style="background-color: rgba(0,0,0,0.0)">
		<div class="isp-container2" style="margin: auto;margin-top:170px;background: #fff" >
		        <div class="loading-area2">
		        	<p><img src="<%=IMG_PATH%>bx_loader.gif" border="0"></p>
		            <p><img src="<%=IMG_PATH%>loading-txt.gif" border="0"></p>
		        </div>
		</div>		
	</div>
</div>
<form name="frm" method="post" onsubmit="return false;" style="height: 100%;">
<input type="hidden" name="pageNo" id="pageNo" value="">
<input type="hidden" name="PSHG_SNO" id="PSHG_SNO" value="">

<div id="wrapper">
	<!-- header -->
	<jsp:include page="/top.do"/>
	<!-- //header -->
	
	<!-- container -->
	<div id="container">
		<!-- sidemenu -->
		<jsp:include page="/left.do"/>
		<!-- //sidemenu -->
		
		<!-- contents -->
		<div id="contents">
			<h2>통계</h2>
			<div class="bbs_info">
				<div class="search">
					<select class="TypeSelect" id="searchCondition" name="searchCondition">
						<c:if test="${!empty appInfo}">
							<c:forEach items="${appInfo}" var="appInfo">
								<option value="${appInfo.APPINFPKNM}">${appInfo.APPINFAPPNM}</option>
							</c:forEach>
						</c:if>
					</select>
					<input type="text" class="w80" id="stDate" name="stDate" value="${stDate }" size="10" readonly="readonly" style="cursor: pointer;"/><input type="button" class="img_button" onclick="javascript:fn_date_show('stDate');" /> ~  
					<input type="text" class="w80" id="etDate" name="etDate" value="${etDate }" size="10" readonly="readonly" style="cursor: pointer;"/><input type="button" class="img_button" onclick="javascript:fn_date_show('etDate');" />
					<p class="btnbox"><a href="javascript:void(0);" onclick="javascript:fn_page(1);" style="padding: 10px;">검색</a></p>
				</div>
			</div>
			
			<div class="pieChart">
				<table>
					<colgroup>
						<col width="25%">
						<col width="25%">
						<col width="25%">
						<col width="25%">
					</colgroup>
					<tbody>
						<tr>
							<td>
								<div id="canvas-holder" style="width:100%">
									<canvas id="chart-area"></canvas>
								</div>
								<p>총 발송</p>
							</td>								
							<td>
								<div id="canvas-holder" style="width:100%">
									<canvas id="chart-area2"></canvas>
								</div>
								<p>Push 발송</p>
							</td>								
							<td>
								<div id="canvas-holder" style="width:100%">
									<canvas id="chart-area3"></canvas>
								</div>
								<p>Push 수신</p>
							</td>								
							<td>
								<div id="canvas-holder" style="width:100%">
									<canvas id="chart-area4"></canvas>
								</div>
								<p>Push 읽음</p>
							</td>								
						</tr>
					</tbody>
				</table>
			</div>
			<div class="page">
				Total : ${totCnt }개 / Page : <em style="color:#0498b6; font-weight: bold;">${paginationInfo.currentPageNo }</em> / ${paginationInfo.totalPageCount }
			</div>
			<div class="bbs_list">
				<table class="list_form totalSub">
					<tbody>
						<tr>
							<th scope="row">No</th>
							<th scope="row">앱명</th>
							<th scope="row">메세지</th>
							<th scope="row">총발송건</th>
							<th scope="row">성공</th>
							<th scope="row">수신</th>
							<th scope="row">읽음</th>
							<th scope="row">실패</th>
							<th scope="row">SMS</th>
							<th scope="row">발송일시</th>
						</tr>
						<c:if test="${empty result}">
							<tr>
								<td colspan="10">조회 된 목록이 없습니다.</td>
							</tr>
						</c:if>
						<c:if test="${!empty result}">
							<c:set var="listSid" value="0"/>
							<c:forEach items="${result}" var="result">
								<c:set var="listSid" value="${listSid+1}"/>
								<c:set var="cntnt" value="${result.MSG }" />
								<tr onclick="javascript:fn_detail('${result.PSSNO }');" style="cursor: pointer;">
									<td><c:out value="${paginationInfo.totalRecordCount - (listSid-1+(paginationInfo.currentPageNo-1)*paginationInfo.recordCountPerPage)}"/></td>
									<td>${result.APPNM }</td>
									<td>${fn:substring(cntnt, 0, 25)}<span>...</span></td>
									<td>${result.TOTCNT }</td>
									<td>${result.SCNT }</td>
									<td>${result.RCNT }</td>
									<td>${result.RDCNT }</td>
									<td>${result.FCNT }</td>
									<td>${result.SMSCNT }</td>
									<td>${result.SNDDAT }</td>
								</tr>		
							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>
			
			<!-- paging -->
			<div class="bbs_paging">
				<jsp:include page="/pagination.do" />
			</div>
			
			<div class="btn_box"><a class="btn kcw" href="javascript:void(0);" onclick="javascript:fn_csv_down();"><strong style="padding: 15px;">CSV 다운로드</strong></a>	
			</div>
			<!-- //paging -->
		</div>
		<!-- //contents -->
	</div>
	<!-- //container -->
</div>
</form>
</body>
</html>

<!-- 전송결과 팝업 -->
<div id="popup_bg" style="display: none;overflow: auto;"></div>
<div id="popup_div_01" class="statsPop" style="display: none;top: 200px; left: 550px; right: 0px; width: 760px;">
	<div id="popup_div_sub_01" class="statsPop_wrap" style="display: none;">
		<table style="border:0;width:100%;">
			<tr>
				<td>
					<h3>전송결과</h3>
					<table class="list_member" style="margin-bottom:0px;">
						<colgroup>
							<col width="80px;" />
							<col width="80px;" />
							<col width="250px;" />
							<col width="*" />
						</colgroup>
						<tbody>
							<tr>
								<th scope="col">사번</th>
								<th scope="col">이름</th>
								<th scope="col">소속</th>
								<th scope="col">전송결과</th>
							</tr>
						</tbody>
					</table>
					<div style="height:200px;overflow-y:auto;margin-bottom:20px;">
					<table class="list_member" style="margin-top:0px;">
						<colgroup>
							<col width="80px;" />
							<col width="80px;" />
							<col width="250px;" />
							<col width="*" />
						</colgroup>
						<tbody id="result">
						</tbody>
					</table>
					</div>
					<div style="margin-top: 10px;">
						<table style="border: 0px;width: 100%">
							<colgroup>
								<col width="50%" />
								<col width="*" />
							</colgroup>
							<tr>
								<td>
									<a class="btn black" href="javascript:void(0);" onclick="javascript:fn_close();"><strong style="padding: 15px;">닫기</strong></a>
								</td>
								<td align="right" id="resend">
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>
		
	</div>
</div>
<!-- //전송결과 팝업 -->