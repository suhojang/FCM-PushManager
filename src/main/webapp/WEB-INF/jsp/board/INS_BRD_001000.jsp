<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/include/declare.jspf" %>
<%
	String TODAY	= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()); 
%>
<script src="<%=JS_PATH%>modal.js"></script>
<link rel="stylesheet" type="text/css" href="<%=CSS_PATH %>modal.css" />
<script type="text/javaScript">
function fn_init(){
	var hh		= 24;
	var hh_obj	= document.getElementById("EMGBRD_STDT_HH");
	var hh_obj2	= document.getElementById("EMGBRD_EDDT_HH");
	for(var i = 0; i < hh; i++){
		hh_obj.innerHTML 	+= "<option value='"+(i < 10 ? ("0"+i) : i)+"' "+(i==1?'selected':'')+">"+(i < 10 ? ("0"+i) : i)+"</option>";
		hh_obj2.innerHTML 	+= "<option value='"+(i < 10 ? ("0"+i) : i)+"' "+(i==23?'selected':'')+">"+(i < 10 ? ("0"+i) : i)+"</option>";
	}
	
	var mm		= 60;
	var splitMM	= 5;
	var mm_obj	= document.getElementById("EMGBRD_STDT_MM");
	var mm_obj2	= document.getElementById("EMGBRD_EDDT_MM");
	for (var i = 0; i < mm; i++) {
		if(i % splitMM == 0){
			mm_obj.innerHTML	+= "<option value='"+(i < 10 ? ("0"+i) : i)+"' "+(i==0?'selected':'')+">"+(i < 10 ? ("0"+i) : i)+"</option>";
			mm_obj2.innerHTML	+= "<option value='"+(i < 10 ? ("0"+i) : i)+"' "+(i==55?'selected':'')+">"+(i < 10 ? ("0"+i) : i)+"</option>";
		}
	}
	
	$("#EMGBRD_TTL").focus();
	
}

function fn_search(){
	var frm	= document.frm;
	frm.action	= "/SEL_BRD_001000.do";
	frm.submit();
}

function fn_reg(){
	if(!validate())
		return;
	if(!confirm("등록 하시겠습니까?"))
		return;
	
	var frm	= document.frm;
	
	var rstStr	= "";
	var lines	= frm.EMGBRD_CNTNT.value.split("\n");
	for(var i=0;i<lines.length;i++){
		if(i!=lines.length-1)
			rstStr	+= lines[i] + "<br>";
		else
			rstStr	+= lines[i];
	}
	
	frm.EMGBRD_STDT.value	= frm.EMGBRD_STDT_DD.value.replaceAll("-","") +  frm.EMGBRD_STDT_HH.value + frm.EMGBRD_STDT_MM.value;
	frm.EMGBRD_EDDT.value	= frm.EMGBRD_EDDT_DD.value.replaceAll("-","") +  frm.EMGBRD_EDDT_HH.value + frm.EMGBRD_EDDT_MM.value;
	frm.EMGBRD_CNTNT.value	= rstStr;
	frm.action	= "/INS_BRD_001001.do";
	frm.submit();
	
	fn_loadingModal_show();
	setTimeout(function(){
		fn_loadingModal_hide();
		document.location.href	= "/SEL_BRD_001000.do";	
	}, 500);
}

function validate(){
	if($("#EMGBRD_TTL").val()==""){
		alert("제목을 입력하세요.");
		$("#EMGBRD_TTL").select();
		return false;
	}
	if($("#EMGBRD_CNTNT").val()==""){
		alert("내용을 입력하세요.");
		$("#EMGBRD_CNTNT").select();
		return false;
	}
	return true;
}

function fn_viewer(){
	var len	= Number($("#ORD_CNT").val())!=5?Number($("#ORD_CNT").val())+1:5;
	len	= Number($("#EMGBRD_ORD").val()) > len ? Number($("#EMGBRD_ORD").val()) : len;
	
	var rolling = "";
	for(var i=0;i<len;i++){
		rolling += "<li class=\""+((i+1)==$("#EMGBRD_ORD").val()?"on":"")+"\">"+(i+1)+"</li>";
	}
	$("#rolling").html(rolling);
	
	var rstStr	= "";
	if($("#EMGBRD_CNTNT").val()!="") {
		var lines	= $("#EMGBRD_CNTNT").val().split("\n");
		for(var i=0;i<lines.length;i++){
			if(i!=lines.length-1)
				rstStr	+= lines[i] + "<br>";
			else
				rstStr	+= lines[i];
		}
	}
	
	$("#EMGBRD_TTL_VIEW").html($("#EMGBRD_TTL").val()==""?"제목없음":$("#EMGBRD_TTL").val());
	$("#EMGBRD_CNTNT_VIEW").html(rstStr==""?"내용없음":rstStr);
	
	$("#popup_bg2").css("display", "block");
	$("#statsPop2").css("display", "block");
	$("#preview").css("display", "block");
}

function fn_txt_handler(obj, ref, len){
	if (fn_bytes_length(obj.value) > len) {
		alert("허용된 글자수가 초과되었습니다.\r\n초과된 부분은 자동으로 삭제됩니다.");
		obj.value	= fn_cut_bytes_length(obj.value, len);
	}
	ref.innerHTML	= "( <b>"+fn_bytes_length(obj.value)+"</b> / "+len+" Bytes )";
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

function fn_btn_preview_yn(val){
	$("#btn_preview").css("visibility",val!="OFF"?"visible":"hidden");
}

function fn_view_check(obj){
	var clazz	= obj.getAttribute("class");
	obj.setAttribute("class",clazz=="chk1 on"?"chk1":"chk1 on");
}

function fn_view_close(){
	$("#popup_bg2").css("display","none");
	$("#statsPop2").css("display", "none");
	$("#preview").css("display","none");
}

function fn_date_show(obj){
	$("#"+obj).datepicker("show");
}

function fn_trs_display(choice){
	if(choice=="01"){
		$("#TRS1").css("display","");
		$("#TRS2").css("display","none");
	}else if(choice=="02"){
		$("#TRS1").css("display","none");
		$("#TRS2").css("display","");
	}
}
</script>

<script>
$(function() {
	$( "#EMGBRD_STDT_DD, #EMGBRD_EDDT_DD" ).datepicker({
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
<input type="hidden" id="ORD_CNT" name="ORD_CNT" value="${ORD_CNT }"/>
<input type="hidden" id="EMGBRD_STDT" name="EMGBRD_STDT"/>
<input type="hidden" id="EMGBRD_EDDT" name="EMGBRD_EDDT"/>
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
			<h2>긴급공지사항 등록</h2>
			<div class="bbs_info">
				<div class="bbs_list">
					<table class="list_form">
						<tbody>
							<tr>
								<th scope="row" class="w100">제 목</th>
								<td>
									<input type="text" id="EMGBRD_TTL" name="EMGBRD_TTL" size="120" onfocus="this.select();" onkeyup="javascript:fn_txt_handler(this, document.getElementById('EMGBRD_TTL_BYTE'), 300);" /> 
									<span id="EMGBRD_TTL_BYTE">( <b>0</b> / 300 Bytes )</span>
								</td>
							</tr>
							<tr>
								<th scope="row">팝업시작</th>
								<td>
									<input type="text" id="EMGBRD_STDT_DD" name="EMGBRD_STDT_DD" size="10" readonly="readonly" style="cursor: pointer;" value="<%=TODAY%>"/><input type="button" class="img_button" onclick="javascript:fn_date_show('EMGBRD_STDT_DD');" />
									<select class="TypeSelect" id="EMGBRD_STDT_HH" name="EMGBRD_STDT_HH">
									</select> 시 
									<select class="TypeSelect" id="EMGBRD_STDT_MM" name="EMGBRD_STDT_MM">
									</select> 분 
								</td>
							</tr>
							<tr>
								<th scope="row">팝업종료</th>
								<td>
									<input type="text" id="EMGBRD_EDDT_DD" name="EMGBRD_EDDT_DD" size="10" readonly="readonly" style="cursor: pointer;" value="<%=TODAY%>"/><input type="button" class="img_button" onclick="javascript:fn_date_show('EMGBRD_EDDT_DD');" />
									<select class="TypeSelect" id="EMGBRD_EDDT_HH" name="EMGBRD_EDDT_HH">
									</select> 시 
									<select class="TypeSelect" id="EMGBRD_EDDT_MM" name="EMGBRD_EDDT_MM">
									</select> 분 
								</td>
							</tr>
							<tr>
								<th scope="row">팝업순서</th>
								<td>
									<select class="TypeSelect" id="EMGBRD_ORD" name="EMGBRD_ORD" onchange="javascript:fn_btn_preview_yn(this.value);">
										<option value="OFF">OFF</option>
										<option value="1">1</option>
										<option value="2">2</option>
										<option value="3">3</option>
										<option value="4">4</option>
										<option value="5">5</option>
									</select> 
								</td>
							</tr>
							<tr>
								<th scope="row">내용</th>
								<td>
									<textarea id="EMGBRD_CNTNT" name="EMGBRD_CNTNT" rows="20" cols="84" onkeyup="javascript:fn_txt_handler(this, document.getElementById('EMGBRD_CNTNT_BYTE'), 1000);"></textarea><br/>
									<span id="EMGBRD_CNTNT_BYTE">( <b>0</b> / 1000 Bytes )</span>
								</td>
							</tr>
							<tr>
								<th scope="row">발송 대상</th>
								<td>
									<input type="radio" name="TRS" checked="checked" value="01" onclick="fn_radio_click(document.getElementsByName('TRS')[0]);document.getElementById('PSHG_TRS').value='01';fn_trs_display('01');"/> 
									<label style="cursor: pointer;" onclick="fn_radio_click(document.getElementsByName('TRS')[0]);document.getElementById('PSHG_TRS').value='01';fn_trs_display('01');">전체(<b>푸시/SMS</b>)</label>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="TRS" value="02" onclick="fn_radio_click(document.getElementsByName('TRS')[1]);document.getElementById('PSHG_TRS').value='02';fn_trs_display('02');"/> 
									<label style="cursor: pointer;" onclick="fn_radio_click(document.getElementsByName('TRS')[1]);document.getElementById('PSHG_TRS').value='02';fn_trs_display('02');">앱 사용자(<b>푸시</b>)</label>
									<br/>
									<font color="red" id="TRS1"><b>* 푸시 토큰이 없는 사용자는 SMS로 발송 됩니다.</b></font>
									<font color="red" id="TRS2" style="display: none;"><b>* 푸시 토큰이 없는 사용자는 발송 되지 않습니다.</b></font>
									<input type="hidden" id="PSHG_TRS" name="PSHG_TRS" value="01"/>
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
									<a class="btn black" id="btn_preview" href="javascript:void(0);" onclick="javascript:fn_viewer();" style="visibility: hidden;"><strong style="padding: 15px;">미리보기</strong></a>&nbsp;
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

<!-- 전송결과 팝업 -->
<div id="popup_bg2" style="display: none;"></div>
<div class="statsPop2" id="statsPop2" style="display: none;">
	<div id="preview" class="statsPop_wrap2" style="display: none;">
		<div class="dotBox">
			<ul id="rolling">
				<li class="on">1</li>
				<li>2</li>
				<li>3</li>
				<li>4</li>
				<li>5</li>
			</ul>
		</div>
		<h3 class="tiBox" id="EMGBRD_TTL_VIEW"></h3>
		<div class="txtBox" id="EMGBRD_CNTNT_VIEW">
		</div>
		<div class="btnBox">
			<a class="chk1 on" href="javascript:void(0);" onclick="javascript:fn_view_check(this);"><span>다시보지 않음</span></a>
			<a class="chk2" href="javascript:void(0);" onclick="javascript:fn_view_close();"><span>창닫기</span></a>
		</div>
	</div>
</div>
