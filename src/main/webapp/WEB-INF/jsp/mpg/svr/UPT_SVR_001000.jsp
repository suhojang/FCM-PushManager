<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/include/declare.jspf" %>
<script src="<%=JS_PATH%>modal.js"></script>
<link rel="stylesheet" type="text/css" href="<%=CSS_PATH %>modal.css" />
<script type="text/javaScript">
function fn_init(){
	document.getElementById("SVRMGR_USEYN").value	= "${result.SVRMGRUSEYN}";
}

function fn_search(){
	var frm	= document.frm;
	frm.action	= "/SEL_SVR_001000.do";
	frm.submit();
}

function fn_modify(){
	if(!validate())
		return;
	if(!confirm("수정 하시겠습니까?"))
		return;
	
	fn_loadingModal_show();
	
	var frm	= document.frm;
	frm.SVRMGR_IP.value	= $("#SVRMGR_IP1").val() + "." + $("#SVRMGR_IP2").val() + "." + $("#SVRMGR_IP3").val() + "." + $("#SVRMGR_IP4").val();
	frm.action	= "/UPT_SVR_001001.do";
	frm.submit();
}

function validate(){
	if($("#SVRMGR_NM").val()==""){
		alert("사용자명을 입력하세요.");
		$("#SVRMGR_NM").select();
		return false;
	}
	if($("#SVRMGR_IP1").val()==""){
		alert("IP를 입력하세요.");
		$("#SVRMGR_IP1").select();
		return false;
	}
	if($("#SVRMGR_IP2").val()==""){
		alert("IP를 입력하세요.");
		$("#SVRMGR_IP2").select();
		return false;
	}
	if($("#SVRMGR_IP3").val()==""){
		alert("IP를 입력하세요.");
		$("#SVRMGR_IP3").select();
		return false;
	}
	if($("#SVRMGR_IP4").val()==""){
		alert("IP를 입력하세요.");
		$("#SVRMGR_IP4").select();
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
<input type="hidden" name=SVRMGR_NO id="SVRMGR_NO" value="${result.SVRMGRNO }">
<input type="hidden" id="SVRMGR_IP" name="SVRMGR_IP"/>
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
								<th scope="row" class="w100">사용자명</th>
								<td>
									<input type="text" id="SVRMGR_NM" name="SVRMGR_NM" size="120" maxlength="10" onfocus="this.select();" value="${result.SVRMGRNM }"/> 
								</td>
							</tr>
							<tr>
								<th scope="row">등록 IP</th>
								<td>
									<input type="text" id="SVRMGR_IP1" name="SVRMGR_IP1" size="5" maxlength="3" onfocus="this.select();" onkeyup="javascript:fn_focus_move(this,3,'SVRMGR_IP2')" value="${result.SVRMGRIP1 }"/> . 
									<input type="text" id="SVRMGR_IP2" name="SVRMGR_IP2" size="5" maxlength="3" onfocus="this.select();" onkeyup="javascript:fn_focus_move(this,3,'SVRMGR_IP3')" value="${result.SVRMGRIP2 }" /> . 
									<input type="text" id="SVRMGR_IP3" name="SVRMGR_IP3" size="5" maxlength="3" onfocus="this.select();" onkeyup="javascript:fn_focus_move(this,3,'SVRMGR_IP4')" value="${result.SVRMGRIP3 }" /> .
									<input type="text" id="SVRMGR_IP4" name="SVRMGR_IP4" size="5" maxlength="3" onfocus="this.select();" value="${result.SVRMGRIP4 }" /> 
								</td>
							</tr>
							<tr>
								<th scope="row">사용여부</th>
								<td>
									<select class="TypeSelect" id="SVRMGR_USEYN" name="SVRMGR_USEYN">
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
