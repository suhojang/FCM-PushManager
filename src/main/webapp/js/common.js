/*------------------------------------------------------
 * function : fn_show_layer
 * description : layer 선택 시 선택된 객체를 나타나게하고 동일한 id를 가지는 다른 객체들을 사라지게한다.
 * 
 * [arguments]
 * 		arguments[0] object : obj - 나타날 layer 객체
 * [return]
 * 
 -------------------------------------------------------*/
function fn_show_layer(obj,pleft,ptop){
	try{
		var objArr	= document.all(obj.id); 
		if(objArr.length==undefined)
			objArr	= new Array(objArr);

		for(var i=0;i<objArr.length;i++){
			if(objArr[i]==obj){
				objArr[i].style.display	= (objArr[i].style.display!='none')?"none":"block";
			}else{
				objArr[i].style.display	= "none";
			}
			objArr[i].style.left	= new String((pleft==undefined)?10:pleft)+"px";
			objArr[i].style.top		= new String((ptop==undefined)?200:ptop)+"px";
		}
	}catch(e){
		throw e;
	}
}

function fn_date_format(obj,yn){
	var str	= obj.value;
	str	= str.trim().replaceAll("-","");
	str	= str.replaceAll(' ','').replace(/[\{\}\[\]\/\?\.\,\;\:\|\)\*\~\`\!\^\-\_\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
	str	= str.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
	str	= str.replace(/[\a-zA-Z]/g,'');

	obj.value	= str;
	
	if(str.length!=8 && str.length!=6)
		return;

	if(!yn){
		obj.value	= str;
		return;
	}

	if(str.length==8)
		str	= str.substring(0,4)+"-"+str.substring(4,6)+"-"+str.substring(6);
	else if(str.length==6)
		str	= str.substring(0,4)+"-"+str.substring(4);

	obj.value	= str;
}

/*------------------------------------------------------
 * function : fn_imemode
 * description : 현재값의 유효성을 체크한다.
 * example : fn_imemode(IME_MODE_YYYYMMDD,document.getElementById("DAT"),"가입일");
 * 
 -------------------------------------------------------*/
var IME_MODE_NOKOREAN		= 1;//한글불가
var IME_MODE_NUMBERONLY		= 2;//숫자만
var IME_MODE_NUMENGUBAR		= 3;//숫자+영문+_
var IME_MODE_NUMENG			= 20;//숫자+영문
var IME_MODE_NUMBENGUBAR	= 4;//숫자+대문자영문+_
var IME_MODE_NUMSENGUBAR	= 5;//숫자+소문자영문+_
var IME_MODE_ENGLISH		= 6;//영문
var IME_MODE_NUMUBAR		= 7;//숫자+'-'
var IME_MODE_NUMDOT			= 8;//숫자+'.'
var IME_MODE_EMAIL			= 9;//EMAIL
var IME_MODE_PHONE			= 10;//PHONE
var IME_MODE_MOBILE			= 11;//MOBILE PHONE
var IME_MODE_EMAIL1			= 12;//숫자+영문+_+-
var IME_MODE_EMAIL2			= 13;//숫자+영문+_+-+.
var IME_MODE_YYYYMMDD		= 14;//yyyymmdd날짜
var IME_MODE_NUMENGDOTUBARDS	= 15;//영문,숫자,'.','-','_'
var IME_MODE_NUMDOTCOMA		= 16;//숫자+'.'+','
var IME_MODE_AMT			= 17;//금액
var IME_MODE_YYYYMM			= 18;//yyyymmdd날짜
var IME_MODE_KORENGNUM		= 19;//한글,영문,숫자
var IME_MODE_FLOAT			= 21;//숫자,소숫점 가능

function fn_imemode(type,obj,name,str,showmsg){
	var msg		= "";
	var test	= false;
	
	str			= str==null?"":str;
	showmsg		= showmsg==null?true:false;
	
	var val		= obj==null?str:obj.value;
	var pattern	= null;
	
	if(type==IME_MODE_NOKOREAN){	//한글불가.
		pattern	= /[ㄱ-ㅎㅏ-ㅣ가-힣]/g;
		if(!pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]은 [한글]을 입력할 수 없습니다.";
			
			if(obj!=null)
				obj.value	= obj.value.replaceAll(' ','').replace(/[ㄱ-ㅎ가-힣]/g,'');
		}
	}else if(type==IME_MODE_NUMBERONLY){//숫자only
		pattern	= /^[0-9]*$/;
		if(pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]은 [숫자]만 입력할 수 있습니다.";

			if(obj!=null){
				obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\.\,\;\:\|\)\*\~\`\!\^\-\_\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
				obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
				obj.value	= obj.value.replace(/[\a-zA-Z]/g,'');
			}
		}
	}else if(type==IME_MODE_NUMENGUBAR){//숫자,영문,'_'
		pattern	= /^[a-zA-Z0-9\_]*$/;
		if(pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]은 [숫자,영문,'_']만 입력할 수 있습니다.";
			
			if(obj!=null){
				obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\.\,\;\:\|\)\*\~\`\!\^\-\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
				obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
			}
		}
	}else if(type==IME_MODE_NUMENG){//숫자,영문
		pattern	= /^[a-zA-Z0-9]*$/;
		if(pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]은 [숫자,영문]만 입력할 수 있습니다.";
			
			if(obj!=null){
				obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\.\_\,\;\:\|\)\*\~\`\!\^\-\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
				obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
			}
		}
	}else if(type==IME_MODE_NUMBENGUBAR){//대문자영문+숫자+'_'
		pattern	= /^[A-Z0-9\_]*$/;
		if(pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]은 [숫자,대문자영문,'_']만 입력할 수 있습니다.";
			
			if(obj!=null){
				obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\.\,\;\:\|\)\*\~\`\!\^\-\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
				obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
				obj.value	= obj.value.replace(/[\a-z]/g,'');
			}
		}
	}else if(type==IME_MODE_NUMBENGUBAR){//소문자영문+숫자+'_'
		pattern	= /^[a-z0-9\_]*$/;
		if(pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]은 [숫자,소문자영문,'_']만 입력할 수 있습니다.";
			
			if(obj!=null){
				obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\.\,\;\:\|\)\*\~\`\!\^\-\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
				obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
				obj.value	= obj.value.replace(/[\A-Z]/g,'');
			}
		}
	}else if(type==IME_MODE_ENGLISH){//소문자영문+숫자+'_'
		pattern	= /^[a-zA-Z]*$/;
		if(pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]은 [영문]만 입력할 수 있습니다.";
			
			if(obj!=null){
				obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\.\,\;\:\|\)\*\~\_`\!\^\-\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
				obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
				obj.value	= obj.value.replace(/[\0-9]/g,'');
			}
		}
	}else if(type==IME_MODE_NUMUBAR){//숫자+'-'
		pattern	= /^[0-9\-]*$/;
		if(pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]은 [숫자,'-']만 입력할 수 있습니다.";
			
			if(obj!=null){
				obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\.\,\;\:\|\)\*\~\_`\!\^\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
				obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
				obj.value	= obj.value.replace(/[\a-zA-Z]/g,'');
			}
		}
	}else if(type==IME_MODE_NUMDOT){//숫자+'.'
		pattern	= /^[0-9\.]*$/;
		if(pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]은 [숫자,'.']만 입력할 수 있습니다.";
			
			if(obj!=null){
				obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\,\;\:\|\)\*\~\_`\!\^\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
				obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
				obj.value	= obj.value.replace(/[\a-zA-Z]/g,'');
			}
		}
	}else if(type==IME_MODE_EMAIL){//EMAIL
		pattern	= /^[0-9a-zA-Z\_\-]{1,}@[0-9a-zA-Z\_\-]{1,}\.{1}[0-9a-zA-Z\_\-]{1,}\.{0,1}[0-9a-zA-Z\_\-]*$/;
		if(pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]이 [EMAIL] 형식에 맞지 않습니다.";
			
			if(obj!=null){
				obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\,\;\:\|\)\*\~`\!\^\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
				obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
			}
		}
	}else if(type==IME_MODE_PHONE){//PHONE
		pattern	= /^[0-9\-]{9,13}$/;
		if(pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]이 [전화번호/FAX] 형식에 맞지 않습니다.";
			
			if(obj!=null){
				obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\,\;\:\|\)\*\~\_`\!\^\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
				obj.value	= obj.value.replace(/[\a-zA-Z]/g,'');
				obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
			}
		}
	}else if(type==IME_MODE_MOBILE){//MOBILE PHONE
		pattern	= /^[0-9\-]{10,13}$/;
		if(pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]이 [MOBILE PHONE] 형식에 맞지 않습니다.";
			
			if(obj!=null){
				obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\,\;\:\|\)\*\~\_`\!\^\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
				obj.value	= obj.value.replace(/[\a-zA-Z]/g,'');
				obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
			}
		}
	}else if(type==IME_MODE_EMAIL1){//숫자+영문+_+-
		pattern	= /^[0-9a-zA-Z\_\-]*$/;
		if(pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]은 [영문,숫자,'_','-']만 입력할 수 있습니다.";
			
			if(obj!=null){
				obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\,\;\:\|\)\*\~\.\`\!\^\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
				obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
			}
		}
	}else if(type==IME_MODE_EMAIL2){//숫자+영문+_+-+.
		pattern	= /^[0-9a-zA-Z\_\-]{1,}\.{1}[0-9a-zA-Z\_\-]{1,}\.{0,1}[0-9a-zA-Z\_\-]*$/;
		if(pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]은 [영문,숫자,'_','-','.']만 입력할 수 있으며, 형식에 맞게 입력하여야 합니다.\n\n" + "형식 : ex)[ abc123.com ]";
			
			if(obj!=null){
				obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\,\;\:\|\)\*\~\`\!\^\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
				obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
			}
		}
	}else if(type==IME_MODE_YYYYMMDD){
		msg		= "["+name+"]의 날짜 형식이 올바르지 않습니다.";

		if(!fn_imemode(IME_MODE_NUMDOT,obj,name))
			return false;
		
		val		= obj.value.replace(/[\.]/g,'');
		test	= true;
		
		if(val.length!=8)
			test	= false;
		
		var yyyy	= val.substring(0,4);
		var mm		= val.substring(4,6);
		if(mm.startsWith("0"))
			mm		= mm.substring(1);
		var dd		= val.substring(6);
		if(dd.startsWith("0"))
			dd		= dd.substring(1);

		if(test && (yyyy=="0000" || mm=="0" || dd=="0"))
			test	= false;
		if( test && "/1/3/5/7/8/10/12/".indexOf("/"+mm+"/")>=0 && parseNumber(dd)>31)
			test	= false;
		else if( test && "/4/6/9/11/".indexOf("/"+mm+"/")>=0 && parseNumber(dd)>30)
			test	= false;
		else if(parseNumber(mm)==2 && test){
			if( ((parseNumber(yyyy) % 4 == 0) && (parseNumber(yyyy) % 100 != 0))|| (parseNumber(yyyy) % 400 == 0) ){
				if(parseNumber(dd)>29)
					test	= false;
			}else{
				if(parseNumber(dd)>28)
					test	= false;
			}
		}
		else if(test && "/1/2/3/4/5/6/7/8/9/10/11/12/".indexOf("/"+mm+"/")<0)
			test	= false;
	}else if(type==IME_MODE_YYYYMM){
		msg		= "["+name+"]의 년월 형식이 올바르지 않습니다.";

		if(!fn_imemode(IME_MODE_NUMDOT,obj,name))
			return false;
		
		val		= obj.value.replace(/[\.]/g,'');
		test	= true;
		
		if(val.length!=6)
			test	= false;
		
		var mm		= val.substring(4);
		
		if(test && "/01/02/03/04/05/06/07/08/09/10/11/12/".indexOf("/"+mm+"/")<0)
			test	= false;
	}else if(type==IME_MODE_NUMENGDOTUBARDS){//숫자,영문,'.','-','_','@'
		pattern	= /^[0-9a-zA-Z\_\-\.\@]*$/;
		if(pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]은 [영문,숫자,'_','-','.','@']만 입력할 수 있습니다.";
			
			if(obj!=null){
				obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\,\;\:\|\)\*\~`\!\^\+\<\>\#\$\%\&\\\=\(\'\"]/g,'');
				obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
			}
		}
	}else if(type==IME_MODE_NUMDOTCOMA){//숫자+'.'+','
		pattern	= /^[0-9\.\,]*$/;
		if(pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]은 [숫자,'.',',']만 입력할 수 있습니다.";
			
			if(obj!=null){
				obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\;\:\|\)\*\~\_`\!\^\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
				obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
				obj.value	= obj.value.replace(/[\a-zA-Z]/g,'');
			}
		}
	}else if(type==IME_MODE_AMT){//금액
		if(val.indexOf(".")>=0 && val.length-val.indexOf(".")>3){
			test	= false;
			msg		= "["+name+"]은 소숫점 두자리까지만 가능합니다.";
		}else{
			pattern	= /^[0-9\,]*[\.]{0,1}[0-9]{0,2}$/;
			if(pattern.test(val)){
				test	= true;
			}else{
				test	= false;
				msg		= "["+name+"]은 올바른 금액이 아닙니다.";
				
				if(obj!=null){
					obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\;\:\|\)\*\~\-\_`\!\^\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
					obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
					obj.value	= obj.value.replace(/[\a-zA-Z]/g,'');
				}
			}
		}
	}else if(type==IME_MODE_KORENGNUM){	//한글,영문,숫자가능
		pattern	= /^[ㄱ-ㅎㅏ-ㅣ가-힣0-9a-zA-Z|\s]*$/;
		if(pattern.test(val)){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]은 [특수문자]를 입력할 수 없습니다.";
			if(obj!=null){
				obj.value	= obj.value.replace(/[\{\}\[\]\/\?\;\:\|\)\*\~\_`\!\^\+\<\>\@\#\$\%\&\\\=\-\(\'\"]/g,'');
			}
		}
	}else if(type==IME_MODE_FLOAT){//숫자,소숫점 가능
		pattern	= /^[0-9\.]*$/;
		if(pattern.test(val) && val.indexOf(".")==val.lastIndexOf(".")){
			test	= true;
		}else{
			test	= false;
			msg		= "["+name+"]은 올바른 숫자가 아닙니다.";
			
			if(obj!=null){
				obj.value	= obj.value.replaceAll(' ','').replace(/[\{\}\[\]\/\?\,\;\:\|\)\*\~\_`\!\^\+\<\>\@\#\$\%\&\\\=\(\'\"]/g,'');
				obj.value	= obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
				obj.value	= obj.value.replace(/[\a-zA-Z]/g,'');
			}
		}
	}
	
	if(!test){
		if(obj!=null)
			obj.select();
		if(showmsg)
			alert(msg);
	}
	return test;
}

//사업자번호 유효성 체크
function fn_check_bizno(bizNo) {
	bizNo	= bizNo.replaceAll('-','');

	if(bizNo.length!=10)
		return false;

	if(!fn_imemode(IME_MODE_NUMBERONLY,null,null,bizNo,false)) 
		return false;

	var a = new Array;
    var b = new Array(1,3,7,1,3,7,1,3,5);
    var sum = 0;
    for (var i = 0; i < 10; i++) a[i] = bizNo.substring(i,i+1);
    for (var i = 0; i < 9;  i++) sum = sum + (a[i] * b[i]);
    sum = sum + ((a[8] * 5) / 10);
    y = (sum - (sum % 1)) % 10;
    if (y == 0)
        z = 0;
    else
        z = 10 - y;

    if (z != a[9])
        return false;

    return true;
}

//생일 유효성체크
function fn_check_birth(birth){
	birth	= birth.replaceAll(".","").replaceAll("-","");
	
	if(!fn_imemode(IME_MODE_NUMBERONLY,null,null,birth,false)) 
		return false;
	
	if(birth.length!=8 && birth.length!=4 && birth.length!=3)
		return false;

	if(birth.length>4)
		birth	= birth.substring(birth.length-4);
	else if(birth.length==3)
		birth	= "0"+new String(birth);
	
	var mm	= birth.substring(0,2);
	if(mm.startsWith("0"))
		mm	= mm.substring(1);
	var dd	= birth.substring(2);
	if(dd.startsWith("0"))
		dd	= dd.substring(1);
		
	if(parseNumber(mm)==2 && parseNumber(dd)>29)
		return false;
	else if("/1/3/5/7/8/10/12/".indexOf("/"+mm+"/")>=0 && parseNumber(dd)>31 )
		return false;
	else if("/4/6/9/11/".indexOf("/"+mm+"/")>=0 && parseNumber(dd)>30 )
		return false;

	if(parseNumber(mm)>12)
		return false;
	if(parseNumber(mm)<1)
		return false;
	if(parseNumber(dd)>31)
		return false;
	if(parseNumber(dd)<1)
		return false;
	
	return true;
}

//bytes변환 사이즈
function fn_bytes_length(str){
	var len	= 0;
	var chr	= '';
	for(var i=0;i<str.length;i++){
		chr	= str.charAt(i);
		if(escape(chr).length>4)
			len	+= 2;
		else
			len	+= 1;
	}
	return len;
}

//bytes수 까지 문자열 자르기
function fn_cut_bytes_length(str, len){
	var size	= 0;
	var idx		= 0;
	var chr		= '';
	
	for (var i=0;i<str.length;i++) {
		if(size >= len)
			break;
		
		chr	= str.charAt(i);
		if(escape(chr).length>4)
			size	+= 2;
		else
			size	+= 1;
		
		idx	= i+1;
	}
	return str.substring(0, idx);
}

//엘리먼트 포커스이동
function fn_focus_move(obj, size, rObj){
	if(obj.value.length==size)
		document.getElementById(rObj).select();
}

//이미지파일 형식 체크
function fn_is_imageFile(value){
	if(!/(\.gif|\.jpg|\.jpeg|\.bmp|\.png)$/i.test(value)) {
		alert("이미지 형식의 파일을 선택하십시오");
		return false;
	}
	return true;
}

function fn_checkbox(obj){
	if(obj.disabled)
		return;
	if(obj.getAttribute("type")=="radio"){
		var arr	= document.getElementsByName(obj.name); 
		for(var i=0;i<arr.length;i++){
			arr[i].checked	= false;
		}
		obj.checked	= true;
		return;
	}
	if(obj.checked)
		obj.checked	= false;
	else
		obj.checked	= true;
}

//radio 버튼 클릭
function fn_radio_click(obj){
	obj.checked	= true;
}

function fn_comma(num){
	if(num.indexOf(".")>=0)
		num	= num.substring(0, num.indexOf("."));
	
	return num.replace(/\B(?=(\d{3})+(?!\d))/g,",");
}