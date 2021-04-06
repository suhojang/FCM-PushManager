<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="kcert.admin.vo.LoginVO"%>
<%try{%>
<%
	LoginVO loginVO	= (LoginVO)request.getSession().getAttribute("loginVO");

	if(loginVO==null || loginVO.getAdminf_id()==null || "".equals(loginVO.getAdminf_id())){
%>
	<script>
		document.location.href = "/SEL_LGN_001000.do";
	</script>
<%}%>
<body>
<div id="Smenu">
	<ul class="dt1">
		<li id="MN_001"><a href="/SEL_BRD_001000.do">긴급공지 게시판</a></li>
		<li id="MN_002"><a href="/INS_MSG_001000.do">메시지 전송</a></li>
		<li id="MN_003"><a href="/SEL_STA_001000.do">통계</a></li>
		<li id="MN_004"><a href="/SEL_APP_001000.do">APP 정보등록</a></li>
		<li id="MN_005">
			<a href="javascript:void(0);">관리</a>
			<ul class="dt2">
				<li id="MN_005_001"><a href="/SEL_GRP_001000.do">My그룹</a></li>
				<!-- <li id="MN_005_002"><a href="/UPT_ADM_001000.do">비밀번호 변경</a></li> -->
				<li id="MN_005_002"><a href="/SEL_ADM_001000.do">관리자</a></li>
				<li id="MN_005_003"><a href="/SEL_IPM_001000.do">IP관리</a></li>
				<li id="MN_005_004"><a href="/SEL_SVR_001000.do">서버관리</a></li>
			</ul>
		</li>
	</ul>
</div>
</body>
</html>
<script type="text/javaScript">
try{
	var MN_LIST	= [
					"MN_001",
					"MN_002",
					"MN_003",
					"MN_004",
					"MN_005_001",
					"MN_005_002",
					"MN_005_003",
					"MN_005_004"
	           	   ];
	var MN_CODE	= "${MN_CODE}";
	
	for(var i=0;i<MN_LIST.length;i++){
		if(MN_CODE==MN_LIST[i] && MN_LIST[i].indexOf("MN_005")>=0)
			document.getElementById("MN_005").setAttribute("class", "on");
		document.getElementById(MN_LIST[i]).setAttribute("class", MN_CODE==MN_LIST[i]?"on":"");
	}
}catch(e){alert(e);}
</script>
<%}catch(Exception e){e.printStackTrace();}%>