<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/include/declare.jspf" %>
<%
	String TODAY	= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()); 
	String HH		= new java.text.SimpleDateFormat("HH").format(new java.util.Date());
%>
<script src="<%=JS_PATH%>modal.js"></script>
<link rel="stylesheet" type="text/css" href="<%=CSS_PATH %>modal.css" />
<script type="text/javaScript">
$(document).ready(function(){
 	var fileTarget = $("#PSHG_IMGLNK");
 	var filename = "";
 	
    fileTarget.on("change", function(){
        if(window.FileReader){
            filename = $(this)[0].files[0].name;
        } else {
            filename = $(this).val().split("/").pop().split("\\").pop();
        }
    	if(!fn_is_imageFile(filename)){
    		$(this).siblings("#PSHG_IMGLNK_NM").val("");
    		if ("<%=_Browser%>"=="MSIE"){
    			$(this).replaceWith($(this).clone(true));
    		}else{
    			$(this).val("");
    		}
    		return;
    	}
        $(this).siblings("#PSHG_IMGLNK_NM").val(filename);
    });
}); 

var all_pushCnt	= 0;	//전체 push사용자 갯수
var all_smsCnt	= 0;	//전체 sms사용자 갯수

var ALL_TXT	= "";
function fn_init(){
	$("#PSHG_GRP_TXT").html("");
	all_pushCnt	= "${PUSHCNT}";
	all_smsCnt	= "${SMSCNT}";
	
	ALL_TXT	+= "전체 " + fn_comma(String(parseInt(all_pushCnt)+parseInt(all_smsCnt))) + " 명<br/>";
	ALL_TXT	+= "Push : " + fn_comma(all_pushCnt) + "명" + " / " + "SMS : " + fn_comma(all_smsCnt) + "명";
	
	fn_group_choice("01");
	
	var hh		= 24;
	var hh_obj	= document.getElementById("PSHG_RVDT_HH");
	for(var i = 0; i < hh; i++){
		hh_obj.innerHTML 	+= "<option value='"+(i < 10 ? ("0"+i) : i)+"' "+(i=="<%=HH%>"?'selected':'')+">"+(i < 10 ? ("0"+i) : i)+"</option>";
	}
	
	var mm		= 60;
	var splitMM	= 5;
	var mm_obj	= document.getElementById("PSHG_RVDT_MM");
	for (var i = 0; i < mm; i++) {
		if(i % splitMM == 0){
			mm_obj.innerHTML	+= "<option value='"+(i < 10 ? ("0"+i) : i)+"' "+(i==55?'selected':'')+">"+(i < 10 ? ("0"+i) : i)+"</option>";
		}
	}
	$("#PSHG_MSG").val("");
	$("#PSHG_MSG").focus();
}

function fn_txt_handler(obj, ref, len){
	if (fn_bytes_length(obj.value) > len) {
		alert("허용된 글자수가 초과되었습니다.\r\n초과된 부분은 자동으로 삭제됩니다.");
		obj.value	= fn_cut_bytes_length(obj.value, len);
	}
	ref.innerHTML	= "( <b>"+fn_bytes_length(obj.value)+"</b> / "+len+" Bytes )";
}

function fn_date_show(obj){
	$("#"+obj).datepicker("show");
}

function fn_rvdt_show(display){
	$("#PSHG_RVDT_TR").css("display",display);
}

var first_group	= true;
var prev_choice	= "01";
//수신 그룹 선택
function fn_group_choice(type){
	fn_popup_display("show",type);
	fn_set_reciever(type);
	if(type=="02"){
		fn_init_set();
	}else if(type=="03" && first_group){
		fn_group_search();
		first_group	= false;
	}else if(type=="04"){
		$("#USER_ID").val("");
	}
}

//수신자명단 변경
function fn_set_reciever(type){
	//이전에 선택 되어진 수신그룹과 동일할 경우 수신자명단을 바꾸지 않는다.
	var temp = "";
	if(type=="01"){
		temp	= ALL_TXT;
	}else if(type=="02"){
		temp	= "Push : " + pushCnt + "명" + " / " + "SMS : " + smsCnt + "명";
	}else if(type=="03"){
		temp	= "Push : " + grp_pushCnt + "명" + " / " + "SMS : " + grp_smsCnt + "명";
	}else if(type=="04"){
		temp	= "관리자 전송";
	}
	$("#PSHG_GRP_TXT").html(temp);
}

//레이어 닫기
function fn_close(type){
	fn_popup_display();
	fn_set_reciever(type);
}

//선택 된 레이어 팝업 보여주기
function fn_popup_display(act, type){
	$("#popup_bg").css(			"display",	act=="show"&&type=="02"?"block":"none");
	$("#popup_div_01").css(		"display",	act=="show"&&type=="02"?"block":"none");
	$("#popup_div_sub_01").css(	"display",	act=="show"&&type=="02"?"block":"none");
	
	$("#popup_bg2").css(		"display",	act=="show"&&type=="03"?"block":"none");
	$("#popup_div_02").css(		"display",	act=="show"&&type=="03"?"block":"none");
	$("#popup_div_sub_02").css(	"display",	act=="show"&&type=="03"?"block":"none");
	
	$("#popup_bg3").css(		"display",	act=="show"&&type=="04"?"block":"none");
	$("#popup_div_03").css(		"display",	act=="show"&&type=="04"?"block":"none");
	$("#popup_div_sub_03").css(	"display",	act=="show"&&type=="04"?"block":"none");
}

function fn_empty(){
	obj		= new Object();
	plc 	= new Object();
	chk_grp = new Object();
	groupCnt	= 0;
	pushCnt 	= 0; 
	smsCnt 		= 0;
	
	$("#userList").empty();
	var temp	= "";
	temp += "<tr>";
	temp += "<th scope=\"col\">사번</th>";
	temp += "<th scope=\"col\">이름</th>";
	temp += "<th scope=\"col\">소속</th>";
	temp += "<th scope=\"col\">삭제</th>";
	temp += "</tr>";
	temp += "<tr>";
	temp += "<td scope=\"row\" colspan=\"4\">검색결과가 없습니다.</td>";
	temp += "</tr>";
	$("#userList").append(temp);
	
	$("#result").empty();
	temp	= "";
	temp += "<tr id=\"resultHeader\">";
	temp += "<th scope=\"col\">사번</th>";
	temp += "<th scope=\"col\">이름</th>";
	temp += "<th scope=\"col\">소속</th>";
	temp += "<th scope=\"col\">추가</th>";
	temp += "</tr>";
	temp += "<tr>";
	temp += "<td scope=\"row\" colspan=\"4\">검색결과가 없습니다.</td>";
	temp += "</tr>";
	$("#result").append(temp);
	
	$("#result_grp").empty();
	$("#result_grp_user").empty();
}

//선택완료
function fn_choice_complete(type){
	fn_popup_display();
	
	var temp = "";
	if(type=="02"){
		temp	+= "Push : " + pushCnt + "명" + " / " + "SMS : " + smsCnt + "명";
	}else if(type=="03"){
		temp	+= "Push : " + grp_pushCnt + "명" + " / " + "SMS : " + grp_smsCnt + "명";
	}else if(type=="04"){
		temp	+= "관리자 전송 : " + $("#USER_ID").val();
	}else{
		temp	+= ALL_TXT;
	}
	$("#PSHG_GRP_TXT").html(temp);
}

function fn_init_set(){
	$("#JYCD").empty();

	$.ajax({
		type: "POST"
		,url: "/SEL_ORG_001000.do"
		,timeout:"10000"
		,success: function(data){
			s	= data['RESULT'];
			if(s==""){
				alert("검색 된 지역이 없습니다.");
				return;
			}
			var ss = s.split("\n");
			var option = "";
			option += "<option selected=\"selected\" value=\"\">지역을 선택하세요</option>";
			for(var i=0;i<ss.length;i++){
				var sss = ss[i].split("\r");

				var cd 	= sss[0];
				var nm 	= sss[1];

				option += "	<option value=\""+cd+"\">"+nm+"</option>";
			}
			$("#JYCD").html(option);
			$("#JSCD").html("<option selected=\"selected\" value=\"\">지사를 선택하세요</option>");
			$("#BSCD").html("<option selected=\"selected\" value=\"\">부서를 선택하세요</option>");
		}
		,error: function(request,status,error){
			alert(request+", "+status+", "+error);
		}
	});
}

function fn_jisa_search() {
	var JYCD	= $("#JYCD option:selected").val();

	$("#JSCD").empty();
	$("#BSCD").empty();

	var option01 	= "";
	option01 += "<option selected=\"selected\" value=\"\">지사를 선택하세요</option>";
	var option02 	= "";
	option02 += "<option selected=\"selected\" value=\"\">부서를 선택하세요</option>";

	if(JYCD=="CDFFT"){
		option01 += "<option value=\"FFT\">인천고객지원센터</option>";
		$("#JSCD").append(option01);
		$("#BSCD").append(option02);
		
		$("#JSCD").attr("disabled",false);
		$("#BSCD").attr("disabled",true);
		
		return;
	}

	$("#JSCD").append(option01);
	$("#BSCD").append(option02);

	if(JYCD==""){
		$("#JSCD").attr("disabled",true);
		$("#BSCD").attr("disabled",true);
		
		return;
	}

	$.ajax({
		type: "POST"
		,url: "/SEL_ORG_001001.do"
		,dataType: "json"
		,data: {
			"JYCD":JYCD
		}
		,timeout:"10000"
		,success: function(data){
			s	= data['RESULT'];
			if(s==""){
				$("#JSCD").attr("disabled",true);
				$("#BSCD").attr("disabled",true);
				
				alert("검색 된 지사가 없습니다.");
				return;
			}
			var ss = s.split("\n");
			var option = "";
			for(var i=0;i<ss.length;i++){
				var sss = ss[i].split("\r");

				var cd 	= sss[0];
				var nm 	= sss[1];

				option += "	<option value=\""+cd+"\">"+nm+"</option>";
			}
			$("#JSCD").append(option);
			
			$("#JSCD").attr("disabled",false);
			$("#BSCD").attr("disabled",true);
		}
		,error: function(request,status,error){
			$("#JSCD").attr("disabled",true);
			$("#BSCD").attr("disabled",true);
			
			alert(request+", "+status+", "+error);
		}
	});
}

function fn_buseo_search() {
	var JSCD	= $("#JSCD option:selected").val();
	var JSNM	= $("#JSCD option:selected").text();

	$("#BSCD").empty();
	
	var option 	= "";
	option += "<option selected=\"selected\" value=\"\">부서를 선택하세요</option>";

	$("#BSCD").append(option);

	if(JSCD==""){
		$("#BSCD").attr("disabled",true);
		
		return;
	}
	
	$.ajax({
		type: "POST"
		,url: "/SEL_ORG_001002.do"
		,dataType: "json"
		,data: {
			"JSCD":JSCD
			,"JSNM":JSNM
		}
		,timeout:"10000"
		,success: function(data){
			s	= data['RESULT'];
			if(s==""){
				$("#BSCD").attr("disabled",true);
				
				alert("검색 된  부서가 없습니다.");
				return;
			}
			var ss = s.split("\n");
			var option = "";
			for(var i=0;i<ss.length;i++){
				var sss = ss[i].split("\r");

				var cd 	= sss[2];
				var nm 	= sss[3];

				option += "	<option value=\""+cd+"\">"+nm+"</option>";
			}
			$("#BSCD").append(option);
			$("#BSCD").attr("disabled",false);

		}
		,error: function(request,status,error){
			$("#BSCD").attr("disabled",true);
			alert(request+", "+status+", "+error);
		}
	});
}

var ALL_SEACH_USER;
function fn_user_search() {
	ALL_SEACH_USER = null;
	var JSCD	= $("#JSCD option:selected").val();
	var JSNM	= $("#JSCD option:selected").text();
	var BSCD	= $("#BSCD option:selected").val();
	var searchCondition = $("#searchCondition option:selected").val();
	var searchKeyword 	= $("#searchKeyword").val();

	$("#result").empty();
	
	var defualt_txt	= "";
	defualt_txt += "<tr id=\"resultHeader\">";
	defualt_txt += "<th scope=\"col\">사번</th>";
	defualt_txt += "<th scope=\"col\">이름</th>";
	defualt_txt += "<th scope=\"col\">소속</th>";
	defualt_txt += "<th scope=\"col\">추가</th>";
	defualt_txt += "</tr>";
	$("#result").append(defualt_txt);
	
	var EMPTY_TXT = "";
	EMPTY_TXT += "<tr>";
	EMPTY_TXT += "<td scope=\"row\" colspan=\"4\">검색결과가 없습니다.</td>";
	EMPTY_TXT += "</tr>";
	
	if(BSCD==""){
		$("#result").append(EMPTY_TXT);
		return;
	}
	
	$.ajax({
		type: "POST"
		,url: "/SEL_ORG_001003.do"
		,dataType: "json"
		,data: {
			"JSCD":JSCD
			,"JSNM":JSNM
			,"BSCD":BSCD
			,"searchCondition":searchCondition
			,"searchKeyword":searchKeyword
		}
		,timeout:"10000"
		,success: function(data){
			s	= data['RESULT'];
			if(s==""){
				$("#result").append(EMPTY_TXT);
				return;
			}
			var ss = s.split("\n");
			ALL_SEACH_USER	= ss;
			var temp = "";
			for(var i=0;i<ss.length;i++){
				var sss = ss[i].split("\r");
				
				var UID 	= sss[0];
				var UNM 	= sss[1];
				var OFF1 	= (sss[2]=="null"?"":sss[2]);
				var OFF2 	= (sss[3]=="null"?"":sss[3]);
				var PHONE 	= (sss[4]=="null"?"":sss[4]);
				var EMAIL 	= (sss[5]=="null"?"":sss[5]);
				var JSNM 	= (sss[6]=="null"?"":sss[6]);
				var BSNM 	= (sss[7]=="null"?"":sss[7]);
				var JWNM 	= (sss[8]=="null"?"":sss[8]);
				var POLICY 	= (sss[9]=="null"?"":sss[9]);
				
				temp += "<tr id=\"result_"+UID+"\">";
				temp += "<td scope=\"row\">"+UID+"</td>";
				temp += "<td scope=\"row\">"+UNM+"</td>";
				temp += "<td scope=\"row\">"+JSNM+" / "+BSNM+" / "+JWNM+"</td>";
				temp += "<td scope=\"row\"><span class=\"btn blue\" onclick=\"javascript:fn_group_add('"+UID+"','"+UNM+"','"+(JSNM+" / "+BSNM+" / "+JWNM)+"', '"+POLICY+"');\"><strong>추가</strong></span></td>";
				temp += "</tr>";
			}
			$("#result").append(temp);
		}
		,error: function(request,status,error){
			alert(request+", "+status+", "+error);
			$("#result").append(EMPTY_TXT);
		}
	});
}

//지역, 지사로 사용자 조회
function fn_common_search() {
	var BSCD	= $("#BSCD option:selected").val();
	if(BSCD == ""){
		var JYCD	= $("#JYCD option:selected").val();
		var JSCD	= $("#JSCD option:selected").val();
		var searchCondition = $("#searchCondition option:selected").val();
		var searchKeyword 	= $("#searchKeyword").val();
		if(JYCD=="" && searchKeyword==""){
			alert("지역을 선택 하시거나 검색어를 입력하여 주세요.");
			return;
		}
		ALL_SEACH_USER = null;
		
		$("#result").empty();
		
		var defualt_txt	= "";
		defualt_txt += "<tr id=\"resultHeader\">";
		defualt_txt += "<th scope=\"col\">사번</th>";
		defualt_txt += "<th scope=\"col\">이름</th>";
		defualt_txt += "<th scope=\"col\">소속</th>";
		defualt_txt += "<th scope=\"col\">추가</th>";
		defualt_txt += "</tr>";
		$("#result").append(defualt_txt);
		
		var EMPTY_TXT = "";
		EMPTY_TXT += "<tr>";
		EMPTY_TXT += "<td scope=\"row\" colspan=\"4\">검색결과가 없습니다.</td>";
		EMPTY_TXT += "</tr>";
		
		$.ajax({
			type: "POST"
			,url: "/SEL_ORG_001004.do"
			,dataType: "json"
			,data: {
				"JYCD":JYCD
				,"JSCD":JSCD
				,"searchCondition":searchCondition
				,"searchKeyword":searchKeyword
			}
			,timeout:"10000"
			,success: function(data){
				s	= data['RESULT'];
				if(s==""){
					$("#result").append(EMPTY_TXT);
					return;
				}
				var ss = s.split("\n");
				ALL_SEACH_USER	= ss;
				var temp = "";
				for(var i=0;i<ss.length;i++){
					var sss = ss[i].split("\r");
					
					var UID 	= sss[0];
					var UNM 	= sss[1];
					var OFF1 	= (sss[2]=="null"?"":sss[2]);
					var OFF2 	= (sss[3]=="null"?"":sss[3]);
					var PHONE 	= (sss[4]=="null"?"":sss[4]);
					var EMAIL 	= (sss[5]=="null"?"":sss[5]);
					var JSNM 	= (sss[6]=="null"?"":sss[6]);
					var BSNM 	= (sss[7]=="null"?"":sss[7]);
					var JWNM 	= (sss[8]=="null"?"":sss[8]);
					var POLICY 	= (sss[9]=="null"?"":sss[9]);
					
					temp += "<tr id=\"result_"+UID+"\">";
					temp += "<td scope=\"row\">"+UID+"</td>";
					temp += "<td scope=\"row\">"+UNM+"</td>";
					temp += "<td scope=\"row\">"+JSNM+" / "+BSNM+" / "+JWNM+"</td>";
					temp += "<td scope=\"row\"><span class=\"btn blue\" onclick=\"javascript:fn_group_add('"+UID+"','"+UNM+"','"+(JSNM+" / "+BSNM+" / "+JWNM)+"', '"+POLICY+"');\"><strong>추가</strong></span></td>";
					temp += "</tr>";
				}
				$("#result").append(temp);
			}
			,error: function(request,status,error){
				alert(request+", "+status+", "+error);
				$("#result").append(EMPTY_TXT);
			}
		});
	}else{
		fn_user_search();
	}
}

var all_add_group_cnt	= 0;
function fn_group_all_add(){
	if(ALL_SEACH_USER==null || ALL_SEACH_USER==undefined || ALL_SEACH_USER.length==0){
		alert("조회 된 사용자가 없습니다.");
		return;
	}
	
	all_add_group_cnt	= 0;
	var ss	= ALL_SEACH_USER;
	for(var i=0;i<ss.length;i++){
		var sss = ss[i].split("\r");
		
		var UID 	= sss[0];
		var UNM 	= sss[1];
		var JSNM 	= (sss[6]=="null"?"":sss[6]);
		var BSNM 	= (sss[7]=="null"?"":sss[7]);
		var JWNM 	= (sss[8]=="null"?"":sss[8]);
		var POLICY 	= (sss[9]=="null"?"":sss[9]);
		
		fn_group_add(UID, UNM, (JSNM+" / "+BSNM+" / "+JWNM), POLICY, "ALL");
	}
	alert("그룹원("+all_add_group_cnt+"명) 등록 완료(이미 추가 된 사용자 제외) 하였습니다.");
}

var groupCnt 	= 0; 		//선택 된 사용자 갯수
var pushCnt 	= 0; 		//푸시사용자 갯수
var smsCnt 		= 0; 		//SMS사용자 갯수
var obj = new Object();		//사용자 ID 정보
var plc = new Object();		//사용자 정책 정보

//사용자 추가
function fn_group_add(no, nm, ss, policy, type){
	var yn	= true;
	for(var key in obj){
		if(obj[key]==no){
			if(type!="ALL") {
				alert("이미 추가 된 사용자 입니다.");
			}
			yn	= false;
			break;
		}
	}
	if(!yn)
		return;
	
	$("#result_"+no).css("display","none");
	if(groupCnt==0){
		$("#userList").empty();
		
		var temp	= "";
		temp += "<tr>";
		temp += "<th scope=\"col\">사번</th>";
		temp += "<th scope=\"col\">이름</th>";
		temp += "<th scope=\"col\">소속</th>";
		temp += "<th scope=\"col\">삭제</th>";
		temp += "</tr>";
		$("#userList").append(temp);
	}
	if(policy=="P000000000"){
		smsCnt++;
	}else if(policy=="P000000001"){
		pushCnt++;
	}
	
	obj[no] 	= no;
	plc[no]		= policy;
	var temp	= "";
	temp 	+= "<tr id=\""+no+"\">";
	temp 	+= "<td scope=\"row\">"+no+"</td>";
	temp 	+= "<td scope=\"row\">"+nm+"</td>";
	temp 	+= "<td scope=\"row\">"+ss+"</td>";
	temp 	+= "<td scope=\"row\"><span class=\"btn gray\" onclick=\"javascript:fn_group_remove('"+no+"','"+policy+"');\"><strong>삭제</strong></span></td>";
	temp 	+= "</tr>";
	
	$("#userList").append(temp);
	groupCnt++;
	if(type=="ALL") {
		all_add_group_cnt++;
	}
}

//사용자 삭제
function fn_group_remove(no, policy){
	$("#"+no).remove();
	delete obj[no];
	delete plc[no];
	
	if(policy == "P000000000"){
		smsCnt--;		
	}else if(policy == "P000000001"){
		pushCnt--;
	}
	groupCnt--;
	
	if(groupCnt==0){
		var temp	= "";
		temp += "<tr>";
		temp += "<td scope=\"row\" colspan=\"4\">검색결과가 없습니다.</td>";
		temp += "</tr>";
		
		$("#userList").append(temp);
	}
	
	$("#result_"+no).css("display","");
}

var grp_pushCnt 	= 0; 	//My Group 푸시사용자 갯수
var grp_smsCnt 		= 0; 	//My Group SMS사용자 갯수
var grp_obj = new Object();	//My Group 사용자 정보
var grp_plc = new Object();	//My Group 그룹 정보
var chk_grp = new Object();	//My Group 체크 사용자 정보

//My Group 조회
function fn_group_search(){
	$.ajax({
		type: "POST"
		,url: "/SEL_MSG_001001.do"
		,timeout:"10000"
		,success: function(data){
			var grpHTML	= "";
			s	= data['result'];
			for(var i=0;i<s.length;i++){
				grpHTML	+= "<li><input type=\"checkbox\" onchange=\"javascript:fn_set_group(this, "+s[i].MYGRPNO+");\"><h3>"+s[i].MYGRPNM+"</h3><a href=\"javascript:void(0);\" onclick=\"javascript:fn_member_search("+s[i].MYGRPNO+");\"><font color='blue'>[그룹원 보기]</font></a></li>";
				
				fn_group_user_search(s[i].MYGRPNO);
			}
			$("#result_grp").html(grpHTML);
			
		}
		,error: function(request,status,error){
			alert(request+", "+status+", "+error);
		}
	});
}

//My Group 사용자 조회
function fn_group_user_search(no){
	$.ajax({
		type: "POST"
		,url: "/SEL_MSG_001002.do"
		,dataType: "json"
		,data: {
			"MYGRP_NO":no
		}
		,timeout:"10000"
		,success: function(data){
			s	= data['result'];
			var grp_nObj	= new Object();
			var grp_nPlc	= new Object();
			for(var i=0;i<s.length;i++){
				grp_nObj[s[i].UID]	= s[i].UNM + "<span>(" + s[i].UID + ")</span>";
				grp_nPlc[s[i].UID]	= s[i].POLICY;
				
			}
			grp_obj[no]	= grp_nObj;
			grp_plc[no]	= grp_nPlc;
		}
		,error: function(request,status,error){
			alert(request+", "+status+", "+error);
		}
	});
}

//My Group 체크 선택
function fn_set_group(obj, no){
	if($(obj).is(":checked")){
		for(var key in grp_plc[no]){
			if(grp_plc[no][key]=="P000000000"){
				grp_smsCnt++;
			}else if(grp_plc[no][key]=="P000000001"){
				grp_pushCnt++;
			}
		}
		chk_grp[no]	= grp_obj[no];
		
	}else{
		for(var key in grp_plc[no]){
			if(grp_plc[no][key]=="P000000000"){
				grp_smsCnt--;
			}else if(grp_plc[no][key]=="P000000001"){
				grp_pushCnt--;
			}
		}
		delete chk_grp[no];
	}
}

//My Group 맴버보기
function fn_member_search(no){
	var grpHTML	= "";
	for(var key in grp_obj[no]){
		grpHTML	+= "<li>"+grp_obj[no][key]+"</span></li>";
	}
	$("#result_grp_user").html(grpHTML);
}

//메시지 전송
function fn_send(){
	if(!validate())
		return;
	if(!confirm($("#PSHG_SKN").val()=="02"?"메시지를 예약 전송 하시겠습니까?":"메시지를 전송 하시겠습니까?"))
		return;
	
	var frm	= document.frm;
	
	var tmp	= "";
	if($("#PSHG_SKN").val()=="02"){
		tmp	+= $("#PSHG_RVDT_DD").val().replaceAll("-","") + $("#PSHG_RVDT_HH").val() + $("#PSHG_RVDT_MM").val();
		$("#PSHG_RVDT").val(tmp);
	}
	tmp	= "";
	if($("#PSHG_GRP").val()=="01"){
		tmp += "ALL";
	}else if($("#PSHG_GRP").val()=="02"){
		var len	= Object.keys(obj).length;
		for(var key in obj){
			tmp += (len-1)==0?obj[key]:obj[key]+",";
			len--;
		}
	}else if($("#PSHG_GRP").val()=="03"){
		var change	= false;
		for(var k in chk_grp){
			var len	= Object.keys(chk_grp[k]).length;
			for(var key in chk_grp[k]){
				if(change){
					change	= false;
					tmp += ",";
				}
				tmp += (len-1)==0?key:key+",";
				len--;
			}
			change	= true;
		}
	}else if($("#PSHG_GRP").val()=="04"){
		tmp += $("#USER_ID").val();
	}
	
	$("#GRPUSR_IDS").val(tmp);
	$("#APPNM").val($("#PSHG_PACKNM option:selected").text());
	
	frm.action	= "/INS_MSG_001001.do";
	frm.submit();
	
	fn_loadingModal_show();
	setTimeout(function(){
		fn_loadingModal_hide();
		document.location.href	= "/SEL_STA_001000.do";	
	}, 500);
}

function validate(){
	if($("#PSHG_MSG").val()==""){
		alert("내용을 입력하세요.");
		$("#PSHG_MSG").select();
		return false;
	}
	
	if($("#PSHG_GRP").val()=="02"){
		if(Object.keys(obj).length==0){
			alert("선택 된 수신자가 없습니다.");
			return false;
		}
	}else if($("#PSHG_GRP").val()=="03"){
		if(Object.keys(chk_grp).length==0){
			alert("선택 된 수신자가 없습니다.");
			return false;
		}
	}else if($("#PSHG_GRP").val()=="04"){
		if($("#USER_ID").val()==""){
			alert("관리자 사번을 입력해 주세요.");
			return;
		}
	}
	return true;
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
	$( "#PSHG_RVDT_DD" ).datepicker({
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
.filebox input[type="file"] {
    position: absolute;
    width: 1px;
    height: 1px;
    padding: 0;
    margin: -1px;
    overflow: hidden;
    clip:rect(0,0,0,0);
    border: 0;
}

.filebox label {
    display: inline-block;
    padding: .5em .75em;
    color: #999;
    font-size: inherit;
    line-height: normal;
    vertical-align: middle;
    background-color: #fdfdfd;
    cursor: pointer;
    border: 1px solid #ebebeb;
    border-bottom-color: #e2e2e2;
    border-radius: .25em;
}

/* named upload */
.filebox .upload-name {
  display: inline-block;
  padding: .5em .75em;
  font-size: inherit;
  font-family: inherit;
  line-height: normal;
  vertical-align: middle;
  background-color: #f5f5f5;
  border: 1px solid #ebebeb;
  border-bottom-color: #e2e2e2;
  border-radius: .25em;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
}

.filebox.bs3-primary label {
  	color: #fff;
    background-color: #0090B7;
    border-color: #006C93;
}
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
<form name="frm" method="post" onsubmit="return false;" style="height: 100%;" enctype="multipart/form-data">
<input type="hidden" id="GRPUSR_IDS" name="GRPUSR_IDS" />
<input type="hidden" id="APPNM" name="APPNM" />
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
			<h2>메시지 전송</h2>
			<div class="bbs_info">
				<div class="bbs_list">
					<table class="list_form">
						<tbody>
							<tr>
								<th scope="row" class="w100">앱 명</th>
								<td>
									<select class="TypeSelect" id="PSHG_PACKNM" name="PSHG_PACKNM">
										<c:if test="${!empty result}">
											<c:forEach items="${result}" var="result">
												<option value="${result.APPINFPKNM}">${result.APPINFAPPNM}</option>
											</c:forEach>
										</c:if>
									</select>
								</td>
							</tr>
							<tr>
								<th scope="row">내 용</th>
								<td>
									<textarea id="PSHG_MSG" name="PSHG_MSG" rows="20" cols="84" onkeyup="javascript:fn_txt_handler(this, document.getElementById('PSHG_MSG_BYTE'), 2000);">
									</textarea><br/>
									<span id="PSHG_MSG_BYTE">( <b>0</b> / 2000 Bytes )</span><br/>
									<font color="red"><b>* 푸시 메시지 내용을 입력하여 주십시오.</b></font>
								</td>
							</tr>
							<tr>
								<th scope="row">발송 구분</th>
								<td>
									<input type="radio" name="SKN" checked="checked" value="01" onclick="fn_radio_click(document.getElementsByName('SKN')[0]);document.getElementById('PSHG_SKN').value='01';fn_rvdt_show('none');"/> 
									<label style="cursor: pointer;" onclick="fn_radio_click(document.getElementsByName('SKN')[0]);document.getElementById('PSHG_SKN').value='01';fn_rvdt_show('none');">즉시</label>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="SKN" value="02" onclick="fn_radio_click(document.getElementsByName('SKN')[1]);document.getElementById('PSHG_SKN').value='02';fn_rvdt_show('');"/> 
									<label style="cursor: pointer;" onclick="fn_radio_click(document.getElementsByName('SKN')[1]);document.getElementById('PSHG_SKN').value='02';fn_rvdt_show('');">예약</label>
									
									<input type="hidden" id="PSHG_SKN" name="PSHG_SKN" value="01"/>
								</td>
							</tr>
							<tr id="PSHG_RVDT_TR" style="display: none;">
								<th scope="row">발송시각</th>
								<td>
									<input type="text" id="PSHG_RVDT_DD" name="PSHG_RVDT_DD" size="10" readonly="readonly" style="cursor: pointer;" value="<%=TODAY%>"/><input type="button" class="img_button" onclick="javascript:fn_date_show('PSHG_RVDT_DD');" />
									<select class="TypeSelect" id="PSHG_RVDT_HH" name="PSHG_RVDT_HH">
									</select> 시 
									<select class="TypeSelect" id="PSHG_RVDT_MM" name="PSHG_RVDT_MM">
									</select> 분 
									
									<input type="hidden" id="PSHG_RVDT" name="PSHG_RVDT"/>
								</td>
							</tr>
							<tr>
								<th scope="row">이미지</th>
								<td>
									<div class="filebox bs3-primary">
			                            <input class="upload-name" id="PSHG_IMGLNK_NM" name="PSHG_IMGLNK_NM" style="width: 400px;" disabled="disabled">
			
			                            <label for="PSHG_IMGLNK">파일선택</label><br/>
			                          	<input type="file" id="PSHG_IMGLNK" name="PSHG_IMGLNK" class="upload-hidden"> 
										<font>최소 : [512 x 256] , <b>권장 : [1024 x 512]</b> , 최대 : [2048 x 1024]</font>
			                        </div>
								</td>
							</tr>
							<tr>
								<th scope="row">Link URL</th>
								<td>
									<input type="text" id="PSHG_LNK" name="PSHG_LNK" size="120" maxlength="100" onfocus="this.select();"/><br/>
									<font color="red"><b>* 푸시 메시지 클릭 시 입력하신 주소로 링크 됩니다.</b></font>&nbsp;(미입력 시 앱이 구동 됩니다.)
								</td>
							</tr>
							<tr>
								<th scope="row">수신 그룹</th>
								<td>
									<input type="radio" name="GRP" checked="checked" value="01" onclick="fn_radio_click(document.getElementsByName('GRP')[0]);document.getElementById('PSHG_GRP').value='01';fn_group_choice('01');"/> 
									<label style="cursor: pointer;" onclick="fn_radio_click(document.getElementsByName('GRP')[0]);document.getElementById('PSHG_GRP').value='01';fn_group_choice('01');">전체</label>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
									<input type="radio" name="GRP" value="02" onclick="fn_radio_click(document.getElementsByName('GRP')[1]);document.getElementById('PSHG_GRP').value='02';fn_group_choice('02');"/> 
									<label style="cursor: pointer;" onclick="fn_radio_click(document.getElementsByName('GRP')[1]);document.getElementById('PSHG_GRP').value='02';fn_group_choice('02');">직접 선택</label>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="GRP" value="03" onclick="fn_radio_click(document.getElementsByName('GRP')[2]);document.getElementById('PSHG_GRP').value='03';fn_group_choice('03');"/> 
									<label style="cursor: pointer;" onclick="fn_radio_click(document.getElementsByName('GRP')[2]);document.getElementById('PSHG_GRP').value='03';fn_group_choice('03');">My Group 선택</label>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="GRP" value="04" onclick="fn_radio_click(document.getElementsByName('GRP')[3]);document.getElementById('PSHG_GRP').value='04';fn_group_choice('04');"/> 
									<label style="cursor: pointer;" onclick="fn_radio_click(document.getElementsByName('GRP')[3]);document.getElementById('PSHG_GRP').value='04';fn_group_choice('04');">관리자 발송</label>
									<input type="hidden" name="PSHG_GRP" id="PSHG_GRP" value="01"/>
								</td>
							</tr>
							<tr>
								<th scope="row">수신자 명단</th>
								<td id="PSHG_GRP_TXT">
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
					<div class="btn_box2" style="margin-top: 10px;">
						<a class="btn black" href="javascript:void(0);" onclick="javascript:fn_send();">
							<strong style="padding: 15px;">발송</strong>
						</a>
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

<!-- 직접선택 팝업 -->
<div id="popup_bg" style="display: none;"></div>
<div id="popup_div_01" class="statsPop sns" style="top:180px; display: none;">
	<div id="popup_div_sub_01" class="statsPop_wrap" style="display: none;">
		<!-- 폼테이블 -->
		<div class="bbs_list">
			<div style="margin-top: 5px;margin-bottom: 5px;">
				<table style="border: 0px;width: 100%">
					<tr>
						<td align="right">
							<a class="btn black" href="javascript:void(0);" onclick="javascript:fn_popup_display();"><strong style="padding: 15px;">닫기</strong></a>	
						</td>
					</tr>
				</table>
			</div>
			<table class="list_form">
				<tbody>
					<tr>
						<td colspan="2">
							<h3>수신 대상자</h3>
							<div class="grp_list" style="max-height: 150px;">
								<!-- //그룹원목록 테이블 -->
								<table class="list_member grp">
									<colgroup>
										<col width="120px" />
										<col width="120px" />
										<col width="*" />
										<col width="100px" />
									</colgroup>
									<tbody id="userList">
										<tr>
											<th scope="col">사번</th>
											<th scope="col">이름</th>
											<th scope="col">소속</th>
											<th scope="col">삭제</th>
										</tr>
										<tr>
											<td scope="row" colspan="4">하단의 검색을 통하여 등록할 대상자를 추가해주세요.</td>
										</tr>
									</tbody>
								</table>
								<!-- 그룹원목록 테이블 -->
							</div>
							<h3 class="alist">수신 대상자 조회</h3>
							<div class="search">	
								<select class="TypeSelect" id="JYCD" name="JYCD" onchange="javascript:fn_jisa_search();">
								</select>
								<select class="TypeSelect" id="JSCD" name="JSCD" onchange="javascript:fn_buseo_search();" disabled>
								</select>
								<select class="TypeSelect" id="BSCD" name="BSCD" disabled>
								</select>
								<select class="TypeSelect" id="searchCondition" name="searchCondition">
									<option value="0">사번</option>
									<option value="1">이름</option>
								</select>
								<input type="text" id="searchKeyword" name="searchKeyword" size="15" onkeydown="if(event.keyCode==13){javascript:fn_common_search();}">
								<p class="btnbox">
									<a class="gray" href="javascript:void(0);" onclick="javascript:fn_common_search();">검색</a>
								</p>
							</div>
							<div class="grp_list" style="max-height: 150px;">
								<table class="list_member">
									<colgroup>
										<col width="120px" />
										<col width="120px" />
										<col width="*" />
										<col width="100px" />
									</colgroup>
									<tbody id="result">
										<tr>
											<th scope="col">사번</th>
											<th scope="col">이름</th>
											<th scope="col">소속</th>
											<th scope="col">추가</th>
										</tr>
										<tr>
											<td scope="row" colspan="4">검색결과가 없습니다.</td>
										</tr>
									</tbody>
								</table>
								<!-- 등록대상자 테이블 -->
							</div>
						</td>
					</tr>
					
				</tbody>
			</table>
			
			<!-- <div class="btn_box" style="margin-top: 10px;"> -->
			<div style="margin-top: 10px;">
				<table style="border: 0px;width: 100%">
					<colgroup>
						<col width="50%" />
						<col width="*" />
					</colgroup>
					<tr>
						<td>
							<a class="btn black" href="javascript:void(0);" onclick="javascript:fn_group_all_add();"><strong style="padding: 15px;">검색 대상자 전체등록</strong></a>
						</td>
						<td align="right">
							<a class="btn kcw" href="javascript:void(0);" onclick="javascript:fn_choice_complete('02');"><strong style="padding: 15px;">선택완료</strong></a>	
						</td>
					</tr>
				</table>
			</div>
		</div>							
		<!-- //폼테이블 -->			
	</div>
</div>
<!-- //직접선택 팝업 -->

<!-- My Group 팝업 -->
<div id="popup_bg2" style="display: none;"></div>
<div id="popup_div_02" class="statsPop myGrp" style="max-height:400px; display: none;">
	<div id="popup_div_sub_02" class="statsPop_wrap" style="display: none;">
		<!-- 폼테이블 -->
		<div class="mGrp">
			<div style="margin-top: 5px;margin-bottom: 5px;margin-right: 10px;">
				<table style="border: 0px;width: 100%">
					<tr>
						<td align="right">
							<a class="btn black" href="javascript:void(0);" onclick="javascript:fn_popup_display();"><strong style="padding: 15px;">닫기</strong></a>	
						</td>
					</tr>
				</table>
			</div>
			<div class="gList">
				<h3>My Group</h3>
				<ul id="result_grp" style="height: 175px;">
				</ul>
				<div class="btn_box" style="margin-top: 15px;margin-left: 15px;margin-bottom:15px;">
					<table style="border: 0px;width: 100%">
						<colgroup>
						<tr>
							<td align="center">
								<a class="btn kcw" href="javascript:void(0);" onclick="javascript:fn_choice_complete('03');"><strong style="padding: 15px;">선택완료</strong></a>	
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="gName">
				<h3>그룹명</h3>
				<ul id="result_grp_user">
				</ul>
			</div>
		</div>							
		<!-- //폼테이블 -->			
	</div>
</div>
<!-- //My Group  팝업 -->

<!-- 관리자 전송 팝업 -->
<div id="popup_bg3" style="display: none;overflow: auto;"></div>
<div id="popup_div_03" class="statsPop" style="display: none;top: 300px; left: 550px; right: 0px; width: 600px;">
	<div id="popup_div_sub_03" class="statsPop_wrap" style="display: none;">
		<div>
			<table style="border: 0px;width: 100%">
				<tr>
					<td align="right">
						<a class="btn black" href="javascript:void(0);" onclick="javascript:fn_popup_display();"><strong style="padding: 15px;">닫기</strong></a>	
					</td>
				</tr>
			</table>
		</div>
		<table style="border:0;width:100%;">
			<tr>
				<td>
					<h3>관리자에게 전송</h3>
					<table class="list_member" style="margin-bottom:0px;">
						<colgroup>
							<col width="120px;" />
							<col width="*" />
						</colgroup>
						<tbody>
							<tr>
								<th scope="row">관리자 사번</th>
								<td style="text-align:left;">
									<input type="text" id="USER_ID" name="USER_ID" size="50" maxlength="10" onfocus="this.select();" style="padding:0px;"/>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<font color="red"><b>* 푸시 토큰이 없는 경우에는 SMS로 발송됩니다.</b></font>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="btn_box" style="margin-top: 15px;margin-left: 15px;margin-bottom:15px;">
					<table style="border: 0px;width: 100%">
						<colgroup>
						<tr>
							<td align="center">
								<a class="btn kcw" href="javascript:void(0);" onclick="javascript:fn_choice_complete('04');"><strong style="padding: 15px;">입력완료</strong></a>	
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