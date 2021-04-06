<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/include/declare.jspf" %>
<script src="<%=JS_PATH%>modal.js"></script>
<link rel="stylesheet" type="text/css" href="<%=CSS_PATH %>modal.css" />
<script type="text/javaScript">
function fn_init(){
	document.getElementById("ADMINF_DELYN").value	= "${result.ADMINFDELYN}";
}

function fn_search(){
	var frm	= document.frm;
	frm.action	= "/SEL_ADM_001000.do";
	frm.submit();
}

function fn_modify(){
	if(!validate())
		return;
	if(!confirm("수정 하시겠습니까?"))
		return;
	
	fn_loadingModal_show();
	
	var frm	= document.frm;
	frm.action	= "/UPT_ADM_001001.do";
	frm.submit();
}

function validate(){
	if($("#ADMINF_PWD_CURR").val()!="${result.ADMINFPWD}"){
		alert("현재 비밀번호가 올바르지 않습니다.");
		$("#ADMINF_PWD_CURR").select();
		return false;
	}
	
	if($("#ADMINF_PWD").val()==""){
		alert("신규 비밀번호를 입력하세요.");
		$("#ADMINF_PWD").select();
		return false;
	}
	if($("#ADMINF_PWD2").val()==""){
		alert("신규 비밀번호 확인을 입력하세요.");
		$("#ADMINF_PWD2").select();
		return false;
	}
	if($("#ADMINF_PWD").val()!=$("#ADMINF_PWD2").val()){
		alert("비밀번호와 비밀번호 확인이 동일하지 않습니다.");
		$("#ADMINF_PWD2").select();
		return false;
	}
	if($("#ADMINF_NM").val()==""){
		alert("관리자명을 입력하세요.");
		$("#ADMINF_NM").select();
		return false;
	}
	if($("#ADMINF_NM").val().length > 20){
		alert("관리자명은 20자 내로 작성하여 주십시오.");
		$("#ADMINF_NM").select();
		return false;
	}
	return true;
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
<input type="hidden" name="MGR_ID" id="MGR_ID" value="${MGR_ID}">
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
			<h2>IP정보 수정</h2>
			<div class="bbs_info">
				<div class="bbs_list">
					<table class="list_form">
						<tbody>
							<tr>
								<th scope="row" class="w100">아이디</th>
								<td>
									<input type="text" id="ADMINF_ID" name="ADMINF_ID" size="120" maxlength="20" onfocus="this.select();" value="${result.ADMINFID }"/> 
								</td>
							</tr>
							<tr>
								<th scope="row" class="w150">현재 비밀번호</th>
								<td>
									<input type="password" id="ADMINF_PWD_CURR" name="ADMINF_PWD_CURR" size="30" maxlength="30" onfocus="this.select();" /> 
								</td>
							</tr>
							<tr>
								<th scope="row">신규 비밀번호</th>
								<td>
									<input type="password" id="ADMINF_PWD" name="ADMINF_PWD" size="30" maxlength="30" onfocus="this.select();"/>
								</td>
							</tr>
							<tr>
								<th scope="row">신규 비밀번호 확인</th>
								<td>
									<input type="password" id="ADMINF_PWD2" name="ADMINF_PWD2" size="30" maxlength="30" onfocus="this.select();"/>
								</td>
							</tr>
							<tr>
								<th scope="row" class="w100">관리자명</th>
								<td>
									<input type="text" id="ADMINF_NM" name="ADMINF_NM" size="120" maxlength="20" onfocus="this.select();" value="${result.ADMINFNM }"/> 
								</td>
							</tr>
							<tr>
								<th scope="row">사용여부</th>
								<td>
									<select class="TypeSelect" id="ADMINF_DELYN" name="ADMINF_DELYN">
										<option value="Y">Y</option>
										<option value="N">N</option>
									</select> 
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
