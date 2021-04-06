package kcert.scheduler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import kcert.admin.service.AdminService;
import kcert.client.service.PushClientService;
import kcert.framework.util.FCMResultCode;
import kcert.framework.util.FCMsender;
import kcert.framework.util.JProperties;
import kcert.framework.util.MakeUtil;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
	private transient Logger logger = Logger.getLogger(this.getClass());
	
	/** AdminService */
    @Resource(name = "AdminService")
    private AdminService adminService;
    
    /**pushClientService */
	@Resource(name="PushClientService")
	private PushClientService pushClientService;
	
	private final FCMsender sender		= FCMsender.getInstance();
	private final String SMS_CMID		= new java.text.SimpleDateFormat("yyyyMMddHHmmssS").format(new java.util.Date());
	private final String SMS_CINFO		= "push";
	private final String SMS_MSGTYPE	= "0";
	private final String SMS_STATUS		= "0";
	private final String SMS_SENDPHONE	= "02-2670-0355";
	
	private final String OTP_PACKAGE_NM	= "스마트포털 모바일앱";
	
    //1분마다 수행
	@Scheduled(fixedDelay=1000 * 60)
	public void scheduler() throws Exception {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			String time	= new java.text.SimpleDateFormat("yyyyMMddHHmm").format(new java.util.Date());
			param.put("TIME", time);
			
			List<Map<String,Object>> usrListA	= new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> usrListI	= new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> smsList	= new ArrayList<Map<String,Object>>();
			
			List<Map<String,Object>> list	= adminService.SEL_MSG_RSV_001(param);		//예약 건 조회
			for(int i=0;i<list.size();i++){
				param.put("PACKAGE", String.valueOf(list.get(i).get("PACKNM")));	//패키지명
				param.put("PSSNO", 	String.valueOf(list.get(i).get("PSSNO")));	//발송일련번호
				param.put("MSG",  	String.valueOf(list.get(i).get("MSG")));
				
				String server_key				= pushClientService.SEL_CLA_APP_KEY(param);	//FCM 서버키 조회
				if("ALL".equals(String.valueOf(list.get(i).get("IDS")))){
					//전체사용자 전송
					param.put("OS", "A");
					usrListA	= pushClientService.SEL_CLA_TKN_S0001(param);	//앱 사용자 조회(안드로이드)
					
					param.put("OS", "I");
					usrListI	= pushClientService.SEL_CLA_TKN_S0001(param);	//앱 사용자 조회(IOS)
					smsList		= pushClientService.SEL_CLA_SMS_S0001(param);	//sms 전체 사용자 조회
				}else{
					//선택 되어진 사용자 토큰 조회
					param.put("PARAMS", MakeUtil.makeConditionParams(String.valueOf(list.get(i).get("IDS")), ","));
					param.put("OS", "A");
					usrListA	= adminService.SEL_ADM_TKN_S0001(param);	//앱 사용자 조회(안드로이드)
					
					param.put("OS", "I");
					usrListI	= adminService.SEL_ADM_TKN_S0001(param);	//앱 사용자 조회(IOS)
					
					for(int j=usrListA.size()-1;j>=0;j--){
						if("SMS".equals(String.valueOf(usrListA.get(j).get("TKNID")))){
							smsList.add(usrListA.get(j));
							usrListA.remove(j);
						}
					}
					
					for(int j=usrListI.size()-1;j>=0;j--){
						if("SMS".equals(String.valueOf(usrListI.get(j).get("TKNID")))){
							smsList.add(usrListI.get(j));
							usrListI.remove(j);
						}
					}
				}
				
				//링크 url
				String LNK	= "";
				if (!"".equals(MakeUtil.isNull(String.valueOf(list.get(i).get("LNK"))))) {
					LNK	= new JProperties().getUploadUrl() + String.valueOf(list.get(i).get("LNK"));
				}
				//이미지 url
				String IMGLNK	= "";
				if (!"".equals(MakeUtil.isNull(String.valueOf(list.get(i).get("IMGLNK"))))) {
					IMGLNK	= new JProperties().getUploadUrl() + String.valueOf(list.get(i).get("IMGLNK"));
				}
				
				/*********************************************** 앱 사용자 푸시 전송 ************************************************/
				if (usrListA!=null && usrListA.size() > 0) {
					Map<String, List<Map<String,Object>>> listMap	= MakeUtil.makeList(usrListA);
					Iterator<String> iter	= listMap.keySet().iterator();
					while (iter.hasNext()) {
						List<Map<String,Object>> alist	= listMap.get(iter.next());
						Map<String,String> result	= sender.send(param.get("PSSNO"), OTP_PACKAGE_NM, param.get("MSG"), LNK, IMGLNK, alist, server_key, 1);
						
						FCMResultCode message	= new FCMResultCode();
						String code				= result.get("RESCODE");
						
						for (int k = 0; k < alist.size(); k++) {
							String yn		= result.get(alist.get(k).get("TKNID"));
							String error	= MakeUtil.makeString(yn, "_", null, false);
							
							param.put("RSNO", 	MakeUtil.getRESKey());	//응답 일련번호 생성
							param.put("USRID", 	String.valueOf(alist.get(k).get("USRID")));
							param.put("TKNID", 	String.valueOf(alist.get(k).get("TKNID")));
							param.put("STS", 	error==null?"01":"99");
							param.put("RST", 	message.getMessage(code, error));
							
							//전송정보이력 DB 저장
							pushClientService.INS_CLA_SND_RES(param);
							
							System.out.println("푸시 전송 응답 메시지 ["+k+"]: " + message.getMessage(code, error));
						}
					}
				}
				
				if (usrListI!=null && usrListI.size() > 0) {
					Map<String, List<Map<String,Object>>> listMap	= MakeUtil.makeList(usrListI);
					Iterator<String> iter	= listMap.keySet().iterator();
					while (iter.hasNext()) {
						List<Map<String,Object>> ilist	= listMap.get(iter.next());
						Map<String,String> result	= sender.send(param.get("PSSNO"), OTP_PACKAGE_NM, param.get("MSG"), LNK, IMGLNK, ilist, server_key, 2);
						
						FCMResultCode message	= new FCMResultCode();
						String code				= result.get("RESCODE");
						
						for (int k = 0; k < ilist.size(); k++) {
							String yn		= result.get(ilist.get(k).get("TKNID"));
							String error	= MakeUtil.makeString(yn, "_", null, false);
							
							param.put("RSNO", 	MakeUtil.getRESKey());	//응답 일련번호 생성
							param.put("USRID", 	String.valueOf(ilist.get(k).get("USRID")));
							param.put("TKNID", 	String.valueOf(ilist.get(k).get("TKNID")));
							param.put("STS", 	error==null?"01":"99");
							param.put("RST", 	message.getMessage(code, error));
							
							//전송정보이력 DB 저장
							pushClientService.INS_CLA_SND_RES(param);
							
							System.out.println("푸시 전송 응답 메시지 ["+k+"]: " + message.getMessage(code, error));
						}
					}
				}
				/**************************************************************************************************************/
				
				/*********************************************** SMS사용자 전송 ***************************************************/
				if (smsList != null && smsList.size() > 0) {
					for (int k = 0; k < smsList.size(); k++) {
						param.put("CMID", 		SMS_CMID + String.valueOf(smsList.get(k).get("USRID")));
						param.put("CINFO", 		SMS_CINFO);
						param.put("MSGTYPE", 	SMS_MSGTYPE);
						param.put("STATUS", 	SMS_STATUS);
						param.put("DESTPHONE", 	String.valueOf(smsList.get(k).get("PHONE")));
						param.put("SENDPHONE", 	SMS_SENDPHONE);
						
						if (smsList.get(k).get("PHONE")==null || "".equals(String.valueOf(smsList.get(k).get("PHONE"))) || "NULL".equals(String.valueOf(smsList.get(k).get("PHONE")).toUpperCase(Locale.KOREA))) {
							param.put("RSNO", 	MakeUtil.getRESKey());	//응답 일련번호 생성
							param.put("USRID", 	String.valueOf(smsList.get(k).get("USRID")));
							param.put("TKNID", 	"SMS");
							param.put("STS", 	"99");
							param.put("RST", 	"전송 실패 : [핸드폰번호가 등록되지 않은 사용자 입니다.]");
							//전송정보이력 DB 저장
							pushClientService.INS_CLA_SND_RES(param);
							
							System.out.println("SMS 전송 응답 메시지 ["+k+"]: " +"전송 실패 : [핸드폰번호가 등록되지 않은 사용자 입니다.]");
							
							continue;
						}
						
						int rst	= pushClientService.INS_SMS_001000(param);
						
						param.put("RSNO", 	MakeUtil.getRESKey());	//응답 일련번호 생성
						param.put("USRID", 	String.valueOf(smsList.get(k).get("USRID")));
						param.put("TKNID", 	"SMS");
						param.put("STS", 	rst==1?"04":"99");
						param.put("RST", 	rst==1?"전송 성공":"전송 실패");
						
						//전송정보이력 DB 저장
						pushClientService.INS_CLA_SND_RES(param);
						
						System.out.println("SMS 전송 응답 메시지 ["+k+"]: " + (rst==1?"전송 성공":"전송 실패"));
					}
				}
				/**************************************************************************************************************/
				param.put("PSNO", 	String.valueOf(list.get(i).get("PSNO")));	//메시지 일련번호
				//메시지 발송여부 변경
				adminService.UPT_MSG_001000(param);
			}
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
	}
}
