<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="kcert.admin.vo.LoginVO"%>
<%try{%>
<%
	LoginVO loginVO	= (LoginVO)request.getSession().getAttribute("loginVO");
%>
<body>
<div id="header">
	<h1><img src="/images/logo.png" alt="근로복지공단" /></h1>
	<p class="login">
		<span class="t_skyblue strong"><%=loginVO.getAdminf_nm() %></span>님께서 로그인하셨습니다. 
		<a href="/SEL_LGN_001002.do" class="loginBtn"><span>로그아웃</span></a>
	</p>
</div>
</body>
</html>
<%}catch(Exception e){e.printStackTrace();}%>