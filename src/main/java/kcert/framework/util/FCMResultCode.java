package kcert.framework.util;

import java.lang.reflect.Field;
import java.util.Locale;

import org.apache.log4j.Logger;

@SuppressWarnings("unused")
public class FCMResultCode {
	private transient Logger logger = Logger.getLogger(this.getClass());
	
	private String CODE_200	= "메시지가 성공적으로 전송 되었습니다."; 
	private String CODE_400	= "잘못된 JSON 데이터 [JSON 메시지의 형식이 올바르게 지정되지 않았거나 올바른 필드가 포함되어 있는지 확인하세요.]";
	private String CODE_401	= "발신자 인증오류 [승인헤더가 누락되었거나 FCM서비스가 사용중지 되었습니다.]";
	private String CODE_5xx	= "요청 시간 초과 [서버에서 시간 내에 요청을 처리하지 못했습니다.]";
	private String CODE_500	= "FCM 서버 오류 [요청을 처리하려고 시도하는 중에 FCM서버에 오류가 발생했습니다.오류가 계속되면 Firebase 지원팀에 문의하시기 바랍니다.https://firebase.google.com/support/?hl=ko]";
	
	private String CODE_200_MISSINGREGISTRATION	= "누락 된 등록 토큰 [registration_id나 JSON 데이터에 to 또는 registration_ids 필드를 확인하세요.]"; 
	private String CODE_200_INVALIDREGISTRATION	= "잘못 된 등록 토큰 [사용자의 등록 토큰이 값이 잘못 되었습니다.일부분을 자르거나 다른 문자를 추가해서는 안 됩니다.]"; 
	private String CODE_200_NOTREGISTERED		= "등록 되지 않은 기기 [사용자가 어플리케이션을 삭제하였거나, 사용자의 토큰이 만료 혹은 등록해제 되었습니다.]"; 
	private String CODE_200_INVALIDPACKAGENAME	= "잘못 된 패키지 이름 [요청한 사용자 토큰이 등록 되어진 패키지명으로 메시지를 보내도록 지정했는지 확인 하시기 바랍니다.]"; 
	private String CODE_200_MISMATCHSENDERID	= "일치하지 않는 발신자 [패키지명과 발신자ID/API키를 확인하십시오.]";
	private String CODE_200_MESSAGETOOBIG		= "너무 큰 메시지 [메시지의 내용이 4,096Byte를 초과 되었습니다.]";
	private String CODE_200_INVALIDDATAKEY		= "잘못 된 데이터 키 [ex) from, gcm 또는 모든값에 google 포함되었는지 확인하세요.]";
	private String CODE_200_INVALIDTTL			= "잘못 된 수명 [time_to_live에서 사용한 값이 0초에서 2,419,200초(4주) 사이의 기간을 나타내는 정수인지 확인하세요.]";
	private String CODE_200_UNAVAILABLE			= "요청 시간 초과 [서버에서 시간 내에 요청을 처리하지 못했습니다.]";
	private String CODE_200_INTERNALSERVERERROR	= "FCM 서버 오류 [요청을 처리하려고 시도하는 중에 FCM서버에 오류가 발생했습니다.오류가 계속되면 Firebase 지원팀에 문의하시기 바랍니다.https://firebase.google.com/support/?hl=ko]";
	private String CODE_200_DEVICEMESSAGERATEEXCEEDED	= "기기 메시지 비율 초과 [이 기기로 전달되는 메시지 비율이 너무 높습니다.이 기기로 보내는 메시지 수를 줄이십시오.]";
	private String CODE_200_TOPICSMESSAGERATEEXCEEDED	= "주제 메시지 비율 초과 [특정 주제의 구독자에게 전달되는 메시지 비율이 너무 높습니다.]";
	private String CODE_200_INVALIDAPNSCREDENTIAL	= "잘못된 APN 인증 정보 [필수 APN SSL 인증서가 업로드되지 않았거나 만료되어 iOS 기기를 타겟팅한 메시지를 보내지 못했습니다. 개발 및 프로덕션 인증서의 유효성을 확인하세요.]";
	
	private String CODE_400_INVALIDPARAMETERS	= "잘못된 매개변수 [전송 되어지는 매개변수의 이름과 유형이 올바른지 확인하세요.]";
	
	public static final String TITLE		= "title";
	public static final String BODY			= "body";
	
	public static final String NOTIFICATION		= "notification";
	public static final String DATA				= "data";
	public static final String REGISTRATION_IDS	= "registration_ids";
	
	public static final String RESCODE		= "RESCODE";
	public static final String RESULTS		= "results";
	public static final String ERROR		= "error";
	
	public String getMessage(String code){
		return getMessage(code, null);
	}
	
	public String getMessage(String code, String errCode){
		String message	= "";
		Field field		= null;
		try {
			field		= this.getClass().getDeclaredField("CODE_"+code+(errCode==null?"":"_"+errCode.toUpperCase(Locale.KOREA)));
			message		= (String) field.get(this);
		} catch (NoSuchFieldException e) {
			logger.error(MakeUtil.stackTrace(e));
		} catch (SecurityException e) {
			logger.error(MakeUtil.stackTrace(e));
		}catch (IllegalArgumentException e) {
			logger.error(MakeUtil.stackTrace(e));
		} catch (IllegalAccessException e) {
			logger.error(MakeUtil.stackTrace(e));
		}
		return message;
	}
}
