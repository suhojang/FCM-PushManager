<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/include/declare.jspf" %>
<script src="<%=JS_PATH%>modal.js"></script>
<link rel="stylesheet" type="text/css" href="<%=CSS_PATH %>modal.css" />
<script type="text/javaScript">
var obj = new Object();
var groupCnt = 0; 
function fn_init(){
	$.ajax({
		type: "POST"
		,url: "/UPT_GRP_001002.do"
		,timeout:"10000"
		,data:{
			"MYGRP_NO":$("#MYGRP_NO").val()
		}
		,success: function(data){
			s	= data['RESULT'];
			for(var i=0;i<s.length;i++){
				obj[s[i].UID] 	= s[i].UID;
				groupCnt++;
			}
		}
		,error: function(request,status,error){
			alert(request+", "+status+", "+error);
		}
	});

	$("#MYGRP_NM").focus();
	fn_init_set();
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
				fn_loadingModal_hide();
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

			fn_loadingModal_hide();
		}
		,error: function(request,status,error){
			alert(request+", "+status+", "+error);
			fn_loadingModal_hide();
		}
	});
}

function fn_jisa_search() {
	fn_loadingModal_show();

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
		
		fn_loadingModal_hide();
		return;
	}

	$("#JSCD").append(option01);
	$("#BSCD").append(option02);

	if(JYCD==""){
		$("#JSCD").attr("disabled",true);
		$("#BSCD").attr("disabled",true);
		
		fn_loadingModal_hide();
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
				
				fn_loadingModal_hide();
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

			fn_loadingModal_hide();
		}
		,error: function(request,status,error){
			$("#JSCD").attr("disabled",true);
			$("#BSCD").attr("disabled",true);
			
			alert(request+", "+status+", "+error);
			fn_loadingModal_hide();
		}
	});
}


function fn_buseo_search() {
	fn_loadingModal_show();

	var JSCD	= $("#JSCD option:selected").val();
	var JSNM	= $("#JSCD option:selected").text();

	$("#BSCD").empty();
	
	var option 	= "";
	option += "<option selected=\"selected\" value=\"\">부서를 선택하세요</option>";

	$("#BSCD").append(option);

	if(JSCD==""){
		$("#BSCD").attr("disabled",true);
		
		fn_loadingModal_hide();
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
				
				fn_loadingModal_hide();
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

			fn_loadingModal_hide();
		}
		,error: function(request,status,error){
			$("#BSCD").attr("disabled",true);
			alert(request+", "+status+", "+error);
			fn_loadingModal_hide();
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
	defualt_txt += "<th scope=\"col\">추가/삭제</th>";
	defualt_txt += "</tr>";
	$("#result").append(defualt_txt);
	
	var EMPTY_TXT = "";
	EMPTY_TXT += "<tr>";
	EMPTY_TXT += "<td scope=\"row\" colspan=\"4\">검색결과가 없습니다.</td>";
	EMPTY_TXT += "</tr>";
	
	if(BSCD==""){
		$("#result").append(EMPTY_TXT);
		
		fn_loadingModal_hide();
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
				
				fn_loadingModal_hide();
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
				
				temp += "<tr id=\"result_"+UID+"\">";
				temp += "<td scope=\"row\">"+UID+"</td>";
				temp += "<td scope=\"row\">"+UNM+"</td>";
				temp += "<td scope=\"row\">"+JSNM+" / "+BSNM+" / "+JWNM+"</td>";
				temp += "<td scope=\"row\"><span class=\"btn blue\" onclick=\"javascript:fn_group_add('"+UID+"','"+UNM+"','"+(JSNM+" / "+BSNM+" / "+JWNM)+"');\"><strong>추가</strong></span></td>";
				temp += "</tr>";
			}
			$("#result").append(temp);
			fn_loadingModal_hide();
		}
		,error: function(request,status,error){
			alert(request+", "+status+", "+error);
			$("#result").append(EMPTY_TXT);
			fn_loadingModal_hide();
		}
	});
}

//지역, 지사로 사용자 조회
function fn_common_search() {
	fn_loadingModal_show();
	
	var BSCD	= $("#BSCD option:selected").val();
	if(BSCD == ""){
		var JYCD	= $("#JYCD option:selected").val();
		var JSCD	= $("#JSCD option:selected").val();
		var searchCondition = $("#searchCondition option:selected").val();
		var searchKeyword 	= $("#searchKeyword").val();
		if(JYCD=="" && searchKeyword==""){
			alert("지역을 선택 하시거나 검색어를 입력하여 주세요.");
			fn_loadingModal_hide();
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
					fn_loadingModal_hide();
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
				fn_loadingModal_hide();
			}
			,error: function(request,status,error){
				alert(request+", "+status+", "+error);
				$("#result").append(EMPTY_TXT);
				fn_loadingModal_hide();
			}
		});
	}else{
		fn_user_search();
	}
}

var all_add_group_cnt	= 0;
function fn_group_all_add(){
	if(ALL_SEACH_USER==null || ALL_SEACH_USER==undefined){
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
		
		fn_group_add(UID, UNM, (JSNM+" / "+BSNM+" / "+JWNM), "ALL");
	}
	alert("그룹원("+all_add_group_cnt+"명) 등록 완료(이미 추가 된 사용자 제외) 하였습니다.");
}

function fn_group_add(no, nm, ss, type){
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
		temp += "<th scope=\"col\">추가/삭제</th>";
		temp += "</tr>";
		$("#userList").append(temp);
	}
	
	obj[no] 	= no;
	var temp	= "";
	temp 	+= "<tr id=\""+no+"\">";
	temp 	+= "<td scope=\"row\">"+no+"</td>";
	temp 	+= "<td scope=\"row\">"+nm+"</td>";
	temp 	+= "<td scope=\"row\">"+ss+"</td>";
	temp 	+= "<td scope=\"row\"><span class=\"btn gray\" onclick=\"javascript:fn_group_remove('"+no+"');\"><strong>삭제</strong></span></td>";
	temp 	+= "</tr>";
	
	$("#userList").append(temp);
	groupCnt++;
	if(type=="ALL") {
		all_add_group_cnt++;
	}
}
function fn_group_remove(no){
	$("#"+no).remove();
	delete obj[no];
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


function fn_search(){
	var frm	= document.frm;
	frm.action	= "/SEL_GRP_001000.do";
	frm.submit();
}

function fn_modify(){
	if(!validate())
		return;
	
	fn_loadingModal_show();
	var data = "";
	var idx	 = 0;
	for(var key in obj){
		data += idx==0?obj[key]:","+obj[key];
		idx++;
	}
	$("#PARAMS").val(data);
	
	if(!confirm("수정 하시겠습니까?"))
		return;
	var frm	= document.frm;
	frm.action	= "/UPT_GRP_001001.do";
	frm.submit();
}

function validate(){
	if($("#MYGRP_NM").val()==""){
		alert("그룹명을 입력하세요.");
		$("#MYGRP_NM").select();
		return false;
	}
	if(Object.keys(obj).length==0){
		alert("추가 된 그룹원 목록이 없습니다.");
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
<input type="hidden" name="MYGRP_NO" id="MYGRP_NO" value="${MNO }">
<input type="hidden" name="PARAMS" id="PARAMS" value="" />
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
			<h2>My Group 수정</h2>
			<!-- 폼테이블 -->
			<div class="bbs_list">
				<table class="list_form">
					<tbody>
						<tr>
							<th scope="row" class="w80">그룹명</th>
							<td>
								<input type="text" id="MYGRP_NM" name="MYGRP_NM" style="width: 90%;" maxlength="33" onfocus="this.select();" value="${MNM }">
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<h3>그룹원 목록</h3>
								<div class="grp_list" style="height: 200px;">
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
											<c:if test="${empty result}">
												<tr>
													<td scope="row" colspan="4">하단의 검색을 통하여 등록할 대상자를 추가해주세요.</td>
												</tr>
											</c:if>
											<c:if test="${!empty result}">
												<c:forEach items="${result}" var="result">
													<tr id="${result.UID }">
														<td scope="row">${result.UID }</td>
														<td scope="row">${result.UNM }</td>
														<td scope="row">${result.JSNM } / ${result.BSNM } / ${result.JWNM }</td>
														<td scope="row"><span class="btn gray" onclick="javascript:fn_group_remove('${result.UID}');"><strong>삭제</strong></span></td>
													</tr>
												</c:forEach>
											</c:if>
										</tbody>
									</table>
									<!-- 그룹원목록 테이블 -->
								</div>
								<h3 class="alist">그룹원 대상자 목록</h3>
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
								<div class="grp_list" style="height: 200px;">
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
												<th scope="col">추가/삭제</th>
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
								<a class="btn black" href="javascript:void(0);" onclick="javascript:fn_group_all_add();"><strong style="padding: 15px;">검색 대상자 전체등록</strong></a>
								<a class="btn kcw" href="javascript:void(0);" onclick="javascript:fn_modify();"><strong style="padding: 15px;">수정</strong></a>	
							</td>
						</tr>
					</table>
				</div>
			</div>							
			<!-- //폼테이블 -->				
		</div>
		<!-- //contents -->
	</div>
	<!-- //container -->
</div>
</form>
</body>
</html>
