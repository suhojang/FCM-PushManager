<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/include/declare.jspf" %>

<script type="text/javaScript">
function fn_init(){
	if("${searchCondition}"!="")
		document.getElementById("searchCondition").value	= "${searchCondition}";	
}

//페이지 이동
function fn_page(pageNo){
	var frm	= document.frm;
	
	frm.pageNo.value = pageNo;
	frm.action = "/SEL_BRD_001000.do";
	frm.submit();	
}

//등록페이지 이동
function fn_reg(){
	var frm	= document.frm;
	frm.action	= "/INS_BRD_001000.do";
	frm.submit();
}

//상세보기
function fn_detail(no){
	var frm	= document.frm;
	frm.EMGBRD_NO.value	= no;
	frm.action	= "/SEL_BRD_001001.do";
	frm.submit();
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
<form name="frm" method="post" onsubmit="return false;" style="height: 100%;">
<input type="hidden" name="pageNo" id="pageNo" value="">
<input type="hidden" name="EMGBRD_NO" id="EMGBRD_NO" value="">

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
			<h2>긴급공지사항 조회</h2>
			<div class="bbs_info">
				<div class="page">
					Total : ${totCnt }개 / Page : <em>${paginationInfo.currentPageNo }</em> / ${paginationInfo.totalPageCount }
				</div>
				<div class="search">
					등록일
					<input type="text" id="stDate" name="stDate" value="${stDate }" size="10" readonly="readonly" style="cursor: pointer;"/><input type="button" class="img_button" onclick="javascript:fn_date_show('stDate');" /> ~  
					<input type="text" id="etDate" name="etDate" value="${etDate }" size="10" readonly="readonly" style="cursor: pointer;"/><input type="button" class="img_button" onclick="javascript:fn_date_show('etDate');" />
					
					<select class="TypeSelect" id="searchCondition" name="searchCondition" style="margin-left: 10px;">
						<option value="0">제목</option>
						<option value="1">내용</option>
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
							<th scope="col">제목</th>
							<th scope="col">순서</th>
							<th scope="col">팝업시작</th>
							<th scope="col">팝업종료</th>
							<th scope="col">등록일</th>
							<th scope="col">등록자</th>
						</tr>				
					</thead>
					<tbody>
					<c:if test="${empty result}">
						<tr>
							<td colspan="7">조회 된 목록이 없습니다.</td>
						</tr>
					</c:if>
					<c:if test="${!empty result}">
						<c:set var="listSid" value="0"/>
						<c:forEach items="${result}" var="result">
						<c:set var="listSid" value="${listSid+1}"/>
						<c:if test="${result.EMGBRDORD != 'OFF'}">
							<tr class="boldrow" onclick="javascript:fn_detail('${result.EMGBRDNO }');" style="cursor: pointer;">
								<td style="border-right-color: transparent;"><img src="<%=IMG_PATH %>popup_icon.png" style="width: 15px;height: 15px;"/></td>
								<td>${result.EMGBRDTTL }</td>
								<td>${result.EMGBRDORD }</td>
								<td>${result.EMGBRDSTDT }</td>
								<td>${result.EMGBRDEDDT }</td>
								<td>${result.EMGBRDRDT }</td>
								<td>${result.ADMINFGRD }</td>
							</tr>
						</c:if>
						<c:if test="${result.EMGBRDORD == 'OFF'}">
							<tr onclick="javascript:fn_detail('${result.EMGBRDNO }');" style="cursor: pointer;">
								<td><c:out value="${paginationInfo.totalRecordCount - (listSid-1+(paginationInfo.currentPageNo-1)*paginationInfo.recordCountPerPage)}"/></td>
								<%-- <td>${result.EMGBRDNO }</td> --%>
								<td>${result.EMGBRDTTL }</td>
								<td>${result.EMGBRDORD }</td>
								<td>${result.EMGBRDSTDT }</td>
								<td>${result.EMGBRDEDDT }</td>
								<td>${result.EMGBRDRDT }</td>
								<td>${result.ADMINFGRD }</td>
							</tr>
						</c:if>
						</c:forEach>
					</c:if>
					</tbody>
				</table>
			</div>
			
			<!-- paging -->
			<div class="bbs_paging">
				<jsp:include page="/pagination.do" />
			</div>
			
			<div class="btn_box"><a class="btn kcw" href="javascript:void(0);" onclick="javascript:fn_reg();"><strong style="padding: 15px;">등록</strong></a>	
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
