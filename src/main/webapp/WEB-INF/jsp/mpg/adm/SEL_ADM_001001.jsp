<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/include/declare.jspf" %>
<script type="text/javaScript">
function fn_init(){
}

function fn_search(){
	var frm	= document.frm;
	frm.action	= "/SEL_ADM_001000.do";
	frm.submit();
}

function fn_modify(){
	var frm	= document.frm;
	frm.action	= "/UPT_ADM_001000.do";
	frm.submit();
}

function fn_delete(){
	if("${result.ADMINFID }"=="admin"){
		alert("admin은 삭제 할 수 없습니다.");
		return;
	}
	if(!confirm("삭제 하시겠습니까?"))
		return;
	var frm	= document.frm;
	frm.action	= "/DEL_ADM_001000.do";
	frm.submit();
}

</script>
<body onload="javascript:fn_init();">

<form name="frm" method="post" onsubmit="return false;" style="height: 100%;">
<input type="hidden" name=ADMINF_ID id="ADMINF_ID" value="${result.ADMINFID }">
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
			<h2>서버정보 상세보기</h2>
			<div class="bbs_info">
				<div class="bbs_list">
					<table class="list_form">
						<tbody>
							<tr>
								<th scope="row" class="w100">아이디</th>
								<td>
									${result.ADMINFID }
								</td>
							</tr>
							<tr>
								<th scope="row">관리자명</th>
								<td>
									${result.ADMINFNM }
								</td>
							</tr>
							<tr>
								<th scope="row">사용여부</th>
								<td>
									${result.ADMINFDELYN }
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
		</div>
		<!-- //contents -->
	</div>
	<!-- //container -->
</div>
</form>
</body>
</html>
