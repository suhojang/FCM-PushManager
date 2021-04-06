<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/include/declare.jspf" %>
<script type="text/javaScript">
function fn_init(){
	$("#adminf_id").focus();
}

function fn_login(){
	if(!validate())
		return;
	var frm	= document.frm;
	frm.action = "/SEL_LGN_001001.do";
	frm.submit();
}

function validate(){
	if($("#adminf_id").val()==""){
		alert("아이디를 입력하세요.");
		$("#adminf_id").select();
		return false;
	}
	if($("#adminf_pwd").val()==""){
		alert("비밀번호를 입력하세요.");
		$("#adminf_pwd").select();
		return false;
	}
	return true;
}
</script>

<body onload="javascript:fn_init();" id="login">
<script type="text/javascript">
   var message = "${message}";
   if(message != "") {
      alert(message);
   }
</script>
<form name="frm" method="post" onsubmit="return false;">
	<div id="wrapper">
		<div id="lBox">
			<h1>
				<img src="<%=IMG_PATH%>/logo.png" alt="근로복지공단" />
			</h1>
			<div id="box"> 
				<p class="ip_l">
					<input type="text" id="adminf_id" name="adminf_id" maxlength="20" onfocus="this.select();" />
				</p>
				<p class="ip_r">
					<input type="password" id="adminf_pwd" name="adminf_pwd" maxlength="30" onfocus="this.select();" onkeydown="if(event.keyCode==13){fn_login();}" />
				</p>
				<p class="access">
					<input type="button" class="login" value="Login" onclick="javascript:fn_login();" />
				</p>
			</div>
		</div>
	</div>
</form>
</body>
</html>
