<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ERROR Page</title>
</head>

<body>
<center><br>
프로그램 처리 중 오류가 발생하였습니다.<br><br>
증상이 지속되면 관리자에게 문의하십시요.<br><br>
<c:if test='${errMsg != "" && errMsg != null}'>
오류내용 : ${errMsg}
<br><br><br>
</c:if>
<a href="javascript:history.back();">뒤로가기</a>
</center>
<script>
	(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
	
	ga('create', 'UA-72259944-1', 'auto');
	ga('send', 'pageview');	
</script>
</body>
</html>