package kcert.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kcert.framework.controller.Controllable;
import kcert.framework.util.MakeUtil;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController extends Controllable {
	@RequestMapping(value="/SND_FCM_001.do")
	public void SND_FCM_001(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,Object> usrMap	= new HashMap<String,Object>();
		List<Map<String,Object>> usrList	= new ArrayList<Map<String,Object>>();
		if("I".equals(request.getParameter("OS"))){
			usrMap.put("USRID", "88892");
			usrMap.put("TKNID", "c4m4tkONPb0:APA91bHZpoChrFvf5Vyjn3_-_fxqGJYCkwl5wsrDofJKwUiWXV3eZ4m4pYLQ3_dWM97O3B1nYBrbQdcLzhgCjOSLolV76MYcPdeSpC6vUUwJm52SYPzb0EMCgQ9-xe9K4-carVw9ocrG");
			usrMap.put("OS", "I");
			
		}else{
			usrMap.put("USRID", "88888");
			usrMap.put("TKNID", "dJEuFyhFsWM:APA91bGfPHx_DNLW5aKb5desQmuMCFAHuwZ3_zz37Z3uLNL7L9IkaSzUxi17-5Lp8ZnY7u52gBxrKH0hdl4UKfnpBDT8lSXkp0ZRb3_0pkbpkvzmfO_uet6xvGRJzfFUZyE6Gyh7vvrp");
			usrMap.put("OS", "A");
		}
		
		usrList.add(usrMap);
		Map<String, List<Map<String,Object>>> listMap	= MakeUtil.makeList(usrList);
		Iterator<String> iter	= listMap.keySet().iterator();
		while (iter.hasNext()) {
			String IMGLNK	= "http://192.168.0.139:8081/upload/images/test.png";
			sender.send(OTP_PACKAGE_NM, "테스트 푸시 메시지", "http://m.naver.com", IMGLNK, listMap.get(iter.next()), "AAAAXmAQQmI:APA91bHunXiD7GX7oMHjlnm-8XRnUhg69_NfusMKLTY51UT7fQ4M3ffz8Qr8QxX3XO4PyFJmiKxHuYNvTYynQRQ2RNgyE1-c8ywFJkyr6Z83fr_bT_9K-yVuylawu2b7zxoAeZrvo6Tt", 2);
		}
	}
}
