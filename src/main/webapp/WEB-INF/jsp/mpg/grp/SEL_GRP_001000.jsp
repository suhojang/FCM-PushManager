<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/include/declare.jspf" %>

<script src="<%=JS_PATH%>modal.js"></script>
<link rel="stylesheet" type="text/css" href="<%=CSS_PATH %>modal.css" />
<script type="text/javaScript">
function fn_init(){
	if("${searchCondition}"!="")
		document.getElementById("searchCondition").value	= "${searchCondition}";	
}

//페이지 이동
function fn_page(pageNo){
	var frm	= document.frm;
	
	frm.pageNo.value = pageNo;
	frm.action = "/SEL_GRP_001000.do";
	frm.submit();	
}

//등록페이지 이동
function fn_reg(){
	var frm	= document.frm;
	frm.action	= "/INS_GRP_001000.do";
	frm.submit();
}

//인사정보 동기화
function fn_usr_sync(){
	fn_loadingModal_show();
	$.ajax({
		type: "POST"
		,url: "/GRP_USR_SYNC.do"
		,timeout:"10000"
		,success: function(data){
			fn_loadingModal_hide();
			s	= data['RESULT'];
			if(s=="Y"){
				alert("동기화가 완료 되었습니다.");
			}else{
				alert(s);
			}
		}
		,error: function(request,status,error){
			fn_loadingModal_hide();
			alert(request+", "+status+", "+error);
		}
	});
}

//상세보기
function fn_detail(no){
	var frm	= document.frm;
	frm.MYGRP_NO.value	= no;
	frm.action	= "/SEL_GRP_001001.do";
	frm.submit();
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

function fn_date_show(obj){
	$("#"+obj).datepicker("show");
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
<input type="hidden" name="MYGRP_NO" id="MYGRP_NO" value="">

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
			<h2>My Group 조회</h2>
			<div class="bbs_info">
				<div class="page">
					Total : ${totCnt }개 / Page : <em>${paginationInfo.currentPageNo }</em> / ${paginationInfo.totalPageCount }
				</div>
				<div class="search">
					등록일
					<input type="text" id="stDate" name="stDate" value="${stDate }" size="10" readonly="readonly" style="cursor: pointer;"/><input type="button" class="img_button" onclick="javascript:fn_date_show('stDate');" /> ~  
					<input type="text" id="etDate" name="etDate" value="${etDate }" size="10" readonly="readonly" style="cursor: pointer;"/><input type="button" class="img_button" onclick="javascript:fn_date_show('etDate');" />
					
					<select class="TypeSelect" id="searchCondition" name="searchCondition">
						<option value="0">그룹명</option>
					</select>
					<input type="text" id="searchKeyword" name="searchKeyword" onkeydown="if(event.keyCode==13){javascript:fn_page(1);}" value="${searchKeyword }">
					<p class="btnbox"><a href="javascript:void(0);" onclick="javascript:fn_page(1);" style="padding: 10px;">검색</a></p>
				</div>
			</div>
			<div class="bbs_list">
				<table class="list_org">
				<caption>리스트</caption>
					<thead>					
						<tr>
							<th scope="col">No</th>
							<th scope="col">그룹명</th>
							<th scope="col">등록자수</th>
							<th scope="col">사용여부</th>
							<th scope="col">등록일자</th>
						</tr>				
					</thead>
					<tbody>
					<c:if test="${empty result}">
						<tr>
							<td colspan="5">조회 된 목록이 없습니다.</td>
						</tr>
					</c:if>
					<c:if test="${!empty result}">
						<c:set var="listSid" value="0"/>
						<c:forEach items="${result}" var="result">
							<c:set var="listSid" value="${listSid+1}"/>
							<tr onclick="javascript:fn_detail('${result.MYGRPNO }');" style="cursor: pointer;">
								<td><c:out value="${paginationInfo.totalRecordCount - (listSid-1+(paginationInfo.currentPageNo-1)*paginationInfo.recordCountPerPage)}"/></td>
								<td style="width: 30%;text-align: center;">${result.MYGRPNM }</td>
								<td>${result.MYGRPUCNT }</td>
								<td>${result.MYGRPUSEYN }</td>
								<td>${result.MYGRPRDT }</td>
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
			
			<div style="margin-top: 10px;">
				<table style="border: 0px;width: 100%">
					<colgroup>
						<col width="50%" />
						<col width="*" />
					</colgroup>
					<tr>
						<td>
							<a class="btn black" href="javascript:void(0);" onclick="javascript:fn_usr_sync();"><strong style="padding: 15px;">인사정보 동기화</strong></a>
						</td>
						<td align="right">
							<a class="btn kcw" href="javascript:void(0);" onclick="javascript:fn_reg();"><strong style="padding: 15px;">등록</strong></a>	
						</td>
					</tr>
				</table>
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
