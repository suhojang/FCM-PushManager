package kcert.framework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.kcert.util.StringUtil;

public class FCMsender {
	private transient Logger logger = Logger.getLogger(this.getClass());
	
	private final int timeout		= 30 * 1000;
	
	private static FCMsender sender	= null;
	private JSONObject json			= null;
	
	private FCMsender(){}
	
	public static FCMsender getInstance(){
		synchronized (FCMsender.class){
			if (sender==null) {
				sender	= new FCMsender();
			}
			return sender;
		}
	}
	
	/**
	 * @param pssno	 	: 응답일련번호
	 * @param title 	: 제목
	 * @param msg		: 메시지
	 * @param list		: 사용자 목록
	 * @param key		: fcm key
	 * @param info		: 1: 안드로이드, 2:IOS
	 * @return
	 */
	public Map<String,String> send(String pssno, String title, String msg, List<Map<String,Object>> list, String key, int info) {
		return send(pssno, title, msg, null, null, list, key, info);
	}
	
	/**
	 * @param pssno	 	: 응답일련번호
	 * @param title : 제목
	 * @param msg	: 메시지
	 * @param lnk	: 링크url
	 * @param list	: 사용자 목록
	 * @param key	: fcm key
	 * @param info	: 1: 안드로이드, 2:IOS
	 * @return
	 */
	public Map<String,String> send(String pssno, String title, String msg, String lnk, List<Map<String,Object>> list, String key, int info) {
		return send(pssno, title, msg, lnk, null, list, key, info);
	}
	
	/**
	 * @param rsno	 	: 응답일련번호
	 * @param title	: 제목
	 * @param msg	: 메시지
	 * @param lnk	: 링크 url
	 * @param imgUrl: 이미지 url
	 * @param list	: 사용자 목록
	 * @param key	: fcm key
	 * @param info	: 1: 안드로이드, 2:IOS
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> send(String pssno, String title, String msg, String lnk, String imgUrl, List<Map<String,Object>> list, String key, int info) {
		Map<String,String> result	= new HashMap<String,String>();
		StringBuffer sb	= new StringBuffer();
		
		URL u 					= null;
		HttpURLConnection conn 	= null;
		OutputStream os			= null;
		BufferedReader in 		= null;

		JSONObject obj	= new JSONObject();
		try{
			u = new URL(new JProperties().getFcmUrl());
			
	        conn = (HttpURLConnection) u.openConnection();
	        conn.setConnectTimeout(timeout);
	        conn.setUseCaches(false);
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Authorization", "key=" + key);
	        conn.setRequestProperty("Content-Type", "application/json");
	        
	        JSONObject data		= new JSONObject();
	        data.put(FCMResultCode.TITLE, 	StringUtil.replaceNull(title));
	        data.put(FCMResultCode.BODY, 	StringUtil.replaceNull(msg));
	        data.put("lnk", 	StringUtil.replaceNull(lnk));
	        data.put("imgUrl", 	StringUtil.replaceNull(imgUrl));
	        data.put("pssno", 	StringUtil.replaceNull(pssno));
	        
	        JSONArray arr	= new JSONArray();
	        
	        for (int i = 0; i < list.size(); i++)
	        	arr.add(String.valueOf(list.get(i).get("TKNID")));
	        
	        if(info==2){
	        	JSONObject noti	= new JSONObject();
		        noti.put(FCMResultCode.TITLE, StringUtil.replaceNull(title));
		        noti.put(FCMResultCode.BODY, StringUtil.replaceNull(msg));
		        noti.put("sound", "default");
		        
		        obj	= getSendJsonData(noti, data, arr);
	        }else{
	        	obj	= getSendJsonData(data, arr);
	        }
	        
	        System.out.println(obj.toString());
			
	        os	= conn.getOutputStream();
	        os.write(obj.toString().getBytes("UTF-8"));
	        os.flush();
	        os.close();
	        
	        result.put(FCMResultCode.RESCODE, String.valueOf(conn.getResponseCode()));
	        
	        in	= new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String line;
	        while ((line = in.readLine()) != null)
				sb.append(line);
	        
	        JSONParser parser	= new JSONParser();
	        JSONObject out		= (JSONObject) parser.parse(sb.toString());
	        
	        JSONArray array	= (JSONArray)out.get(FCMResultCode.RESULTS);
	        for (int i = 0; i < array.size(); i++) {
	        	String error	= String.valueOf(((JSONObject) array.get(i)).get(FCMResultCode.ERROR));
	        	
				result.put(String.valueOf(list.get(i).get("TKNID")), error==null || "null".equals(error)?"Y":"N_"+error);
			}
		}catch(IOException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch (ParseException e) {
			logger.error(MakeUtil.stackTrace(e));
		}finally{
			if(conn!=null){conn.disconnect();}
			try{if(os!=null){os.close();}}catch(IOException e){}
			try{if(in!=null){in.close();}}catch(IOException e){}
		}
		return result;
	}
	
	/**
	 * fcm 전송 데이터 생성
	 * @param noti
	 * @param data
	 * @param arr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private JSONObject getSendJsonData(JSONObject data, JSONArray arr) {
		json		= new JSONObject();
		
		json.put(FCMResultCode.DATA, 			data);
		json.put(FCMResultCode.REGISTRATION_IDS, arr);
			
		return json;
	}
	
	/**
	 * fcm 전송 데이터 생성
	 * @param noti
	 * @param data
	 * @param arr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private JSONObject getSendJsonData(JSONObject noti, JSONObject data, JSONArray arr) {
		json		= new JSONObject();
		
		json.put(FCMResultCode.NOTIFICATION, 	noti);
		json.put(FCMResultCode.DATA, 			data);
		json.put(FCMResultCode.REGISTRATION_IDS, arr);
		
		json.put("mutable_content", true);
			
		return json;
	}
	
	public static void main(String[] args) throws Exception {
		String title	= "스마트포털 모바일앱";
		//String msg		= "금일 13:00 ~ 15:00까지 긴급점검이 있습니다.\n스마트포털 모바일 앱 사용에 이용제한이 발생 할 수 있으니 양해 부탁드립니다.";
		String msg		= "소장님 테스트 발송";
		String lnk		= "";
		//String lnk		= "http://192.168.0.139:8080/officeapp/index.do";
		String imgUrl	= "";
		//String imgUrl	= "http://192.168.0.139:8080/officeapp/noti_img2.png";
		List<Map<String,Object>> list	= new ArrayList<Map<String,Object>>();
		//String API_KEY	= "AAAAToKfvjQ:APA91bFCdVLCr_WLo7HqyzdlHIZ_zvRKo6Zs84m2aOcKxhMNr2-pOeONoitCt-squmppAbjciKmuL-lB0dDruJByG7mSpsUCpLdTB8ZxuxhxvwWnwfudS7Zo3v66y1gVP9c7z1UYIqew";
		String API_KEY	= "AAAAXmAQQmI:APA91bHunXiD7GX7oMHjlnm-8XRnUhg69_NfusMKLTY51UT7fQ4M3ffz8Qr8QxX3XO4PyFJmiKxHuYNvTYynQRQ2RNgyE1-c8ywFJkyr6Z83fr_bT_9K-yVuylawu2b7zxoAeZrvo6Tt";
		
		//fVwkOtF251I:APA91bH6R9qr9iLE9NQkBITwDT84O1XYp-O5UzsUR2zxa7gEUHlJzvxA0ctLIncbq9wjFOWgHOAr94B7KJsF7-SuZUGt7wMnPEKfi-zunTV7eO0lkarq5ndAmCxti67vpE5dTYexLsMX
		Map<String, Object> token	= new HashMap<String, Object>();
//		token.put("TKNID", "fmAZMpRO1oA:APA91bErayBKfc2fSf-pRvocaTrVgAbSIjfb5eZzXTHEOhOUW2DBvLKrBSc10FZuco_7LhkRPaubKp-iOsuzAJiHF-KH6izwxjtrzXCH1RMUQYtGwyvxOT8utpqKxSK-dtDrPjiP1mNLSj_GXVqTxDEq6ZrOKIYEWA");
		//token.put("TKNID", "fi7KZOnCslU:APA91bGtWdrVk2j2XRcGSPD2Tt5XEtGVljdHjbmZN2-w_MV2HbEUfM4Coam8b7QnU1uJddV14vPHwyQl6ZcebjSQ-XlWCvp303CMgcdlN-3gbpbiFl6lWKIPtI_CsPIrrKSKJamwb3WGgg2RAttKc9a3AvgPD6GKkA");
		//token.put("TKNID", "fxvBiiXACKQ:APA91bEApFiNA-7mYqRMhCrlsFP6NjeHk8ALqs2RFLOOd41He9sKoFHamyKRCNhK4uw8Z_PWL5DsSz5myOQXJLoWzpQVqm2JNcIZyNJfxx6wBtEITuzgLkR6SUWWFc7Z9I-C46XJ3gb_KHTzKd_YsT9KAIpzLe0CTg");
		//token.put("TKNID", "eyEbT_2IDV8:APA91bH0ok7k0MQ0TzD6KqdHTghqOR0nRy7fVR1QHuZ9cnMxQPj2Rd3urZGEla-VVq_fC5MC6I9zzARecuJyUG987IVE2bxMEpN84d4xN7TxLkagH6fEOk0CZgZL_ExbDUY03F1bizfecKmeY7JhgFUCkQkHN8Vjqw");
		token.put("TKNID", "d0d2aM56fvE:APA91bHK9FrSdw5Fo8A6QU32qkd5BofNlgAtErXPbdMCbVQ8zArFx19qr6F8QsCSGIaVgfK5UUOyQEhUeeQMDyDEXdkX0FGUOeUxyOSTuA0AXkR6zq9Dj6JacUocMAKeKmhFpskzR8kntQbDVfEbfocDss4hpoBJVQ");
		list.add(token);
		
		/*for (int i = 0; i < 500; i++) {
			token	= new HashMap<String, Object>();
			token.put("TKNID", "d1oY7i0cKbI:APA91bF23dL4iNR1KkPg_7bgtcHZ9pLCWKh_hrd23tlu4q75kWnMJ-SjzKtAXfcMonKngBd22-YpzLvO98gTJ9PhNbDUlG4LFXN_7cvoavSant93ybpdk4J6jl9XXkSlQB2Wk7Ge-viB"+i);
			list.add(token);
		}*/
		/*
		token	= new HashMap<String,String>();
		token.put("TKNID", "11enzq277x67U:APA91bGUuvFmJ_GbPLJq6ZQonsgvQQMI1ILpT3noPqZpdDEzV5cHLDo6l77Y14FXxY3VafT1W8q1FJzpSJ_O1ts7nkclOdXMLxvLBGDDTONscGQ7NDyccvIt2qtg5EsAUm7N1X9wFybV");
		list.add(token);
		
		token	= new HashMap<String,String>();
		token.put("TKNID", "22enzq277x67U:APA91bGUuvFmJ_GbPLJq6ZQonsgvQQMI1ILpT3noPqZpdDEzV5cHLDo6l77Y14FXxY3VafT1W8q1FJzpSJ_O1ts7nkclOdXMLxvLBGDDTONscGQ7NDyccvIt2qtg5EsAUm7N1X9wFybV");
		list.add(token);
		
		token	= new HashMap<String,String>();
		token.put("TKNID", "33fGb8dcF0H5o:APA91bGW5IqcnjRponywn_u7tXytqTeMjGieox8qRSeoPnYvD4ptzL25B41HX5lP7PHsaX266oJ9UeetfPLP8vbPH2uZXt4L9yDBgodwpfWpV-qrUnZ7TwZgOtMdnBYhoGzMUaMVcYib");
		list.add(token);*/
        
		FCMsender sender	= FCMsender.getInstance();
		Map<String,String> result	= sender.send(title, msg, lnk, imgUrl, list, API_KEY, 2);

		FCMResultCode message	= new FCMResultCode();
		
		String code	= result.get("RESCODE");
		for (int i = 0; i < list.size(); i++) {
			String yn		= result.get(list.get(i).get("TKNID"));
			
			String error	= MakeUtil.makeString(yn, "_", null, false);
			yn				= MakeUtil.makeString(yn, "_", yn, true);
			
			System.out.println(error);
			System.out.println(yn);
			
			System.out.println(message.getMessage(code, error));
		}
	}
}
