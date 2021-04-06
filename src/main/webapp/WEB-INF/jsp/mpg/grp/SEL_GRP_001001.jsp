<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/include/declare.jspf" %>
<script type="text/javaScript">
function fn_init(){
}

function fn_search(){
	var frm	= document.frm;
	frm.action	= "/SEL_GRP_001000.do";
	frm.submit();
}

function fn_modify(){
	var frm	= document.frm;
	frm.action	= "/UPT_GRP_001000.do";
	frm.submit();
}

function fn_delete(){
	if(!confirm("삭제 하시겠습니까?"))
		return;
	var frm	= document.frm;
	frm.action	= "/DEL_GRP_001000.do";
	frm.submit();
}

</script>
<body onload="javascript:fn_init();">

<form name="frm" method="post" onsubmit="return false;" style="height: 100%;">
<input type="hidden" name="MYGRP_NO" id="MYGRP_NO" value="${MNO }">
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
			<h2>My Group 상세보기</h2>
			<div class="bbs_list">
				<table class="list_form">
					<tbody>
						<tr>
							<th scope="row" class="w80">그룹명</th>
							<td>
								${MNM }
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<h3>그룹원 목록</h3>
								<div class="grp_list2" style="margin-bottom:0px;">
									<!-- //그룹원목록 테이블 -->
									<table class="list_member grp" style="margin-bottom:0px;">
										<colgroup>
											<col width="160px" />
											<col width="160px" />
											<col width="*" />
										</colgroup>
										<tbody>
											<tr>
												<th scope="col">사번</th>
												<th scope="col">이름</th>
												<th scope="col">소속</th>
											</tr>
										</tbody>
									</table>
									<!-- 그룹원목록 테이블 -->
								</div>
								<div class="grp_list2" style="height: 400px;overflow-x:hidden;overflow-y: auto;">
									<table class="list_member grp" style="margin-top:0px;">
										<colgroup>
											<col width="160px" />
											<col width="160px" />
											<col width="*" />
										</colgroup>
										<tbody>
											<c:if test="${!empty result}">
												<c:forEach items="${result}" var="result">
													<tr>
														<td scope="row">${result.UID }</td>
														<td scope="row">${result.UNM }</td>
														<td scope="row">${result.JSNM } / ${result.BSNM } / ${result.JWNM }</td>
													</tr>
												</c:forEach>
											</c:if>
										</tbody>
									</table>
									<!-- 그룹원목록 테이블 -->
								</div>
							</td>
						</tr>
					</tbody>
				</table>
				<div style="margin-top: 10px;">
					<table style="border: 0px;width: 100%">
						<colgroup>
							<col width="50%" />
							<col width="*" />
						</colgroup>
						<tr>
							<td>
								<a class="btn black" href="javascript:void(0);" onclick="javascript:fn_search();"><strong style="padding: 15px;">목록</strong></a>
							</td>
							<td align="right">
								<a class="btn black" href="javascript:void(0);" onclick="javascript:fn_delete();"><strong style="padding: 15px;">삭제</strong></a>&nbsp;
								<a class="btn kcw" href="javascript:void(0);" onclick="javascript:fn_modify();"><strong style="padding: 15px;">수정</strong></a>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<!-- //contents -->
	</div>
	<!-- //container -->
</div>
</form>
</body>
</html>
