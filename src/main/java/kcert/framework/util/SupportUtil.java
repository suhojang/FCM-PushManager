package kcert.framework.util;

import javax.servlet.http.HttpServletRequest;

public class SupportUtil {
	public static String pureNumber(String val){
		if(val==null || "".equals(val))
			return val;
		StringBuffer sb	= new StringBuffer();
		
		for(int i=0;i<val.length();i++){
			if(val.charAt(i)>=48 && val.charAt(i)<=57)
				sb.append(val.charAt(i));
		}
		return sb.toString();
	}
	
	public static final String convertHP(String hpno){
		hpno	= pureNumber(hpno);
		if(hpno==null || hpno.length()<10)
			return hpno;
		if(hpno.length()==10){
			hpno	= hpno.substring(0,3)+"-"+hpno.substring(3,6)+"-"+hpno.substring(6);
		}else if(hpno.length()==11){
			hpno	= hpno.substring(0,3)+"-"+hpno.substring(3,7)+"-"+hpno.substring(7);
		}else if(hpno.length()>11){
			hpno	= hpno.substring(0,3)+"-"+hpno.substring(3,7)+"-"+hpno.substring(7,11);
		}
		return hpno;
	}
	
	public static final String getRemoteIP(HttpServletRequest request){
		String ip	= request.getHeader("X-FORWARDED-FOR");
		if(ip==null || ip.length()==0)
			ip = request.getRemoteAddr();
		
		return ip;
	}
}
