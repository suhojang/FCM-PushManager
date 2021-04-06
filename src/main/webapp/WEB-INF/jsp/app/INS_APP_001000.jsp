<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/include/declare.jspf" %>
<script src="<%=JS_PATH%>modal.js"></script>
<link rel="stylesheet" type="text/css" href="<%=CSS_PATH %>modal.css" />
<script type="text/javaScript">
function fn_init(){
}

function fn_search(){
	var frm	= document.frm;
	frm.action	= "/SEL_APP_001000.do";
	frm.submit();
}

function fn_reg(){
	if(!validate())
		return;
	if(!confirm("등록 하시겠습니까?"))
		return;
	
	fn_loadingModal_show();
	
	var frm	= document.frm;
	frm.action	= "/INS_APP_001001.do";
	frm.submit();
}

function validate(){
	if($("#APPINF_APPNM").val()==""){
		alert("앱명을 입력하세요.");
		$("#APPINF_APPNM").select();
		return false;
	}
	if($("#APPINF_PKNM").val()==""){
		alert("패키지명을 입력하세요.");
		$("#APPINF_PKNM").select();
		return false;
	}
	if($("#APPINF_ID").val()==""){
		alert("발신자ID를 입력하세요.");
		$("#APPINF_ID").select();
		return false;
	}
	if($("#APPINF_KEY").val()==""){
		alert("서버키를 입력하세요.");
		$("#APPINF_KEY").select();
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
			<h2>앱 정보 등록</h2>
			<div class="bbs_info">
				<div class="bbs_list">
					<table class="list_form">
						<tbody>
							<tr>
								<th scope="row" class="w100">앱 명</th>
								<td>
									<input type="text" id="APPINF_APPNM" name="APPINF_APPNM" size="120" maxlength="33" onfocus="this.select();" value="${result.APPINFAPPNM }" /> 
								</td>
							</tr>
							<tr>
								<th scope="row">패키지 명</th>
								<td>
									<input type="text" id="APPINF_PKNM" name="APPINF_PKNM" size="120" maxlength="33" onfocus="this.select();" value="${result.APPINFPKNM }" /> 
								</td>
							</tr>
							<tr>
								<th scope="row">발신자 ID</th>
								<td>
									<input type="text" id="APPINF_ID" name="APPINF_ID" size="120" maxlength="25" onfocus="this.select();" style="ime-mode:disable;" value="${result.APPINFID }"/> 
								</td>
							</tr>
							<tr>
								<th scope="row">서버키</th>
								<td>
									<input type="text" id="APPINF_KEY" name="APPINF_KEY" size="120" maxlength="300" onfocus="this.select();" style="ime-mode:disable;" value="${result.APPINFKEY }" /> 
								</td>
							</tr>
							<tr>
								<th scope="row">사용여부</th>
								<td>
									<select class="TypeSelect" id="APPINF_USEYN" name="APPINF_USEYN">
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
									<a class="btn kcw" href="javascript:void(0);" onclick="javascript:fn_reg();"><strong style="padding: 15px;">등록</strong></a>	
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
