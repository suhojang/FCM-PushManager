package kcert.client.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kcert.admin.service.AdminService;
import kcert.client.service.PushClientService;
import kcert.framework.controller.Controllable;
import kcert.framework.util.FCMResultCode;
import kcert.framework.util.JSONRequest;
import kcert.framework.util.JSONResponse;
import kcert.framework.util.MakeUtil;
import kcert.framework.util.SupportUtil;
import keos.FWebDoc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
 
/**
 * 푸시 클라이언트 컨트롤러
 * @author 장수호
 * @since 2018.03.06
 * @version 1.0
 * @see 
 * <pre>
 *    
 *   수정일      	수정자                 수정내용
 *  -------    --------     ---------------------------
 * 
 * </pre>
 */
@Controller
public class PushClientController extends Controllable{
	/** AdminService */
    @Resource(name = "AdminService")
    private AdminService adminService;
    
	/**pushClientService */
	@Resource(name="PushClientService")
	private PushClientService pushClientService;
	
	/**
	 * 클라이언트 모바일 작업
	 * @param request
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value="/CLA_MBL_001000.do")
	public void CLA_MBL_001000(HttpServletRequest request,HttpServletResponse response,Model model) {
		String command		= request.getParameter("H_CMD_ID");
		
		try {
			Method method = this.getClass().getDeclaredMethod(command,HttpServletRequest.class,HttpServletResponse.class,Model.class);
			method.invoke(this, new Object[]{request,response,model});
		} catch (NoSuchMethodException | SecurityException e) {
			logger.error(MakeUtil.stackTrace(e));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error(MakeUtil.stackTrace(e));
		}
	}
	
	/**
	 * 앱 별 토큰정보 저장
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException
	 */
	public void CLA_TKN_SND(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception {
		String rStr		= "Y";
		
		String TOKEN_ID		= request.getParameter("TOKEN_ID");
		String PACKAGE_NM	= request.getParameter("PACKAGE");
		
		Map<String,String> param	= new HashMap<String,String>();
		param.put("TOKEN", 		TOKEN_ID);
		param.put("PACKAGE", 	PACKAGE_NM);
		try{
			param.put("CD", 	"TKNMGR_NO");
			param.put("SQ", 	"0");
			param.put("TKNO", 	String.valueOf(pushClientService.NEXT_VAL(param)));	//토큰관리일련번호 채번
			param.put("APNO", 	pushClientService.SEL_CLA_APP_SEQ(param));			//앱 일련번호 조회
			
			pushClientService.INS_CLA_TKN_SND(param);
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
			rStr	= "N";
		}
		response.getWriter().print(rStr);
	}
	
	/**
	 * 인사/경조사 푸시 발송
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException
	 */
	public void CLA_FCM_PTL(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception {
		boolean useYN		= false;
		String ipAddr		= SupportUtil.getRemoteIP(request);
		//허용 서버 조회
		List<Map<String,Object>> svrList	= adminService.SEL_SVR_S001009();
		for (int i = 0; i < svrList.size(); i++) {
			if(ipAddr.equals(svrList.get(i).get("IPMGRIP")) || "0.0.0.0".equals(svrList.get(i).get("IPMGRIP"))) {
				useYN	= true;
				break;
			}
		}
		
		if (useYN) {
			/* 전체사용자 푸시전송
			 * 한번에 보낼 수 있는 푸시 건수 : 1000건
			 * [MKN(발송구분) : 02(포털)], [SKN(전송구분) : 01(즉시발송)], [PKN(포털전송구분) : 01(인사공지사항), 02(경조사), 999(테스트)], [MSG : 푸시전송내용]
			 */
			//MakeUtil.nvl(request.getParameter("PKN")) 999 : 테스트 메시징
			String MSG	= "";
			if("01".equals(MakeUtil.nvl(request.getParameter("PKN")))){
				MSG	= TITLE_PERSONNEL + MakeUtil.nvl(request.getParameter("MSG"));
			}else if("02".equals(MakeUtil.nvl(request.getParameter("PKN")))){
				MSG	= TITLE_FAMILYEVENT + MakeUtil.nvl(request.getParameter("MSG"));
			}else if("999".equals(MakeUtil.nvl(request.getParameter("PKN")))){
				MSG	= "[테스트] " + MakeUtil.nvl(request.getParameter("MSG"));
			}
			
			System.out.println("msg : " + MSG);
			
			Map<String,String> param	= new HashMap<String,String>();
			param.put("PACKAGE", 	OTP_PACKAGE);
			param.put("MKN",  		"02");
			param.put("SKN",  		"01");
			param.put("PKN",  		MakeUtil.nvl(request.getParameter("PKN")));
			param.put("MSG",  		MSG);
			param.put("RVDT",  		"");
			param.put("IMGLNK",  	"");
			param.put("LNK",  		"");
			param.put("SNDYN",  	"Y");	//발송여부
			
			//테스트 메시징 발송
			if("999".equals(MakeUtil.nvl(request.getParameter("PKN")))){
				//88892 사번으로 푸시/SMS 전송
				String server_key				= pushClientService.SEL_CLA_APP_KEY(param);		//FCM 서버키 조회
				param.put("USRID", "88892");
				Map<String,String> result		= pushClientService.SEL_CLA_TKNID_TEST(param);
				//String TKNID	= pushClientService.SEL_CLA_TKNID_TEST(param);
				
				List<Map<String,Object>> list	= new ArrayList<Map<String,Object>>();
				Map<String, Object> map	= new HashMap<String,Object>();
				map.put("TKNID", result.get("TKNID"));
				list.add(map);
				
				if ("A".equals(result.get("OS"))) {
					sender.send("", OTP_PACKAGE_NM, param.get("MSG"), list, server_key, 1);
				} else if ("I".equals(result.get("OS"))) {
					sender.send("", OTP_PACKAGE_NM, param.get("MSG"), list, server_key, 2);
				} else {
					response.getWriter().write("[88892] 사원의 디바이스 정보가 SMS로 설정되어 푸시메시지를 전송 할 수 없습니다.");
				}
				
				//sender.send("", OTP_PACKAGE_NM, param.get("MSG"), list, server_key, "A".equals(result.get("OS"))?1:"I".equals(result.get("OS"))?2:1);
				
			}else{
				try{
					param.put("CD", 	"PSHG_SNO");
					param.put("SQ", 	"0");
					param.put("PSSNO", 	String.valueOf(pushClientService.NEXT_VAL(param)));	//푸시 발송일련번호 채번
					
					param.put("CD", 	"PSHG_NO");
					param.put("SQ", 	"0");
					param.put("PSNO", 	String.valueOf(pushClientService.NEXT_VAL(param)));	//푸시 일련번호 채번
					param.put("RUSR", 	"SYSTEM");
					
					//발송정보이력 DB 저장
					//pushClientService.INS_CLA_FCM_SND(param);
					
					/*********************************************** 앱 사용자 푸시 전송 ************************************************/
					String server_key				= pushClientService.SEL_CLA_APP_KEY(param);		//FCM 서버키 조회
					
					param.put("OS", "A");
					List<Map<String,Object>> usrListA	= pushClientService.SEL_CLA_TKN_S0001(param);	//앱 사용자 조회(안드로이드)
					
					param.put("OS", "I");
					List<Map<String,Object>> usrListI	= pushClientService.SEL_CLA_TKN_S0001(param);	//앱 사용자 조회(IOS)
					
					if(usrListA!=null && usrListA.size() > 0){
						Map<String, List<Map<String,Object>>> listMap	= MakeUtil.makeList(usrListA);
						Iterator<String> iter	= listMap.keySet().iterator();
						while (iter.hasNext()) {
							List<Map<String,Object>> alist	= listMap.get(iter.next());
							Map<String,String> result	= sender.send(param.get("PSSNO"), OTP_PACKAGE_NM, param.get("MSG"), alist, server_key, 1);
							
							FCMResultCode message	= new FCMResultCode();
							String code				= result.get("RESCODE");
							
							for (int i = 0; i < alist.size(); i++) {
								String yn		= result.get(alist.get(i).get("TKNID"));
								String error	= MakeUtil.makeString(yn, "_", null, false);
								
								param.put("RSNO", 	MakeUtil.getRESKey());	//응답 일련번호 생성
								param.put("USRID", 	String.valueOf(alist.get(i).get("USRID")));
								param.put("TKNID", 	String.valueOf(alist.get(i).get("TKNID")));
								param.put("STS", 	error==null?"01":"99");
								param.put("RST", 	message.getMessage(code, error));
								
								//전송정보이력 DB 저장
								//pushClientService.INS_CLA_SND_RES(param);
							}
						}
					}
					
					if(usrListI!=null && usrListI.size() > 0){
						Map<String, List<Map<String,Object>>> listMap	= MakeUtil.makeList(usrListI);
						Iterator<String> iter	= listMap.keySet().iterator();
						while (iter.hasNext()) {
							List<Map<String,Object>> ilist	= listMap.get(iter.next());
							Map<String,String> result	= sender.send(param.get("PSSNO"), OTP_PACKAGE_NM, param.get("MSG"), ilist, server_key, 2);
							
							FCMResultCode message	= new FCMResultCode();
							String code				= result.get("RESCODE");
							
							for (int i = 0; i < ilist.size(); i++) {
								String yn		= result.get(ilist.get(i).get("TKNID"));
								String error	= MakeUtil.makeString(yn, "_", null, false);
								
								param.put("RSNO", 	MakeUtil.getRESKey());	//응답 일련번호 생성
								param.put("USRID", 	String.valueOf(ilist.get(i).get("USRID")));
								param.put("TKNID", 	String.valueOf(ilist.get(i).get("TKNID")));
								param.put("STS", 	error==null?"01":"99");
								param.put("RST", 	message.getMessage(code, error));
								
								//전송정보이력 DB 저장
								//pushClientService.INS_CLA_SND_RES(param);
							}
						}
					}
					/**************************************************************************************************************/
					
					/*********************************************** SMS사용자 전송 ***************************************************/
					/*List<Map<String,Object>> smsList	= pushClientService.SEL_CLA_SMS_S0001(param);
				if(smsList!=null && smsList.size() > 0){
					for (int i = 0; i < smsList.size(); i++) {
						param.put("CMID", 		SMS_CMID + String.valueOf(smsList.get(i).get("USRID")));
						param.put("CINFO", 		SMS_CINFO);
						param.put("MSGTYPE", 	SMS_MSGTYPE);
						param.put("STATUS", 	SMS_STATUS);
						param.put("DESTPHONE", 	String.valueOf(smsList.get(i).get("PHONE")));
						param.put("SENDPHONE", 	SMS_SENDPHONE);
						
						if (smsList.get(i).get("PHONE")==null || "".equals(String.valueOf(smsList.get(i).get("PHONE"))) || "NULL".equals(String.valueOf(smsList.get(i).get("PHONE")).toUpperCase(Locale.KOREA))) {
							param.put("RSNO", 	MakeUtil.getRESKey());	//응답 일련번호 생성
							param.put("USRID", 	String.valueOf(smsList.get(i).get("USRID")));
							param.put("TKNID", 	"SMS");
							param.put("STS", 	"99");
							param.put("RST", 	"전송 실패 : [핸드폰번호가 등록되지 않은 사용자 입니다.]");
							//전송정보이력 DB 저장
							pushClientService.INS_CLA_SND_RES(param);
							continue;
						}
						
						int rst	= pushClientService.INS_SMS_001000(param);
						
						param.put("RSNO", 	MakeUtil.getRESKey());	//응답 일련번호 생성
						param.put("USRID", 	String.valueOf(smsList.get(i).get("USRID")));
						param.put("TKNID", 	"SMS");
						param.put("STS", 	rst==1?"04":"99");
						param.put("RST", 	rst==1?"전송 성공":"전송 실패");
						
						//전송정보이력 DB 저장
						pushClientService.INS_CLA_SND_RES(param);
					}
				}*/
					/**************************************************************************************************************/
				}catch(SQLException e){
					logger.error(MakeUtil.stackTrace(e));
				}
			}
		} else {
			response.getWriter().write("허용되지 않은 서버 입니다.\n관리자에게 문의 하시기 바랍니다.");
		}
	}
	
	/**
	 * 긴급공지사항 조회
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException 
	 */
	public void SEL_BRD_A01001(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException {
		StringBuffer sb	= new StringBuffer();
		try{
			String pageNo	= FWebDoc.getReq(request, "page", langByte, langMode, langModeAnsi);
			String rowCount = FWebDoc.getReq(request, "rowCount", langByte, langMode, langModeAnsi);
			String setText	= FWebDoc.getReq(request, "setText", langByte, langMode, langModeAnsi);
			
			if(rowCount == null || rowCount.equals("")){rowCount = "15";}
			if(pageNo == null || pageNo.equals("")){pageNo = "1";}
		
			Map<String,Object> param	= new HashMap<String,Object>();
			
			int firstIndex	= (Integer.parseInt(pageNo) - 1) * Integer.parseInt(rowCount);
			
			param.put("ROWCOUNT", 	Integer.parseInt(rowCount));
			param.put("FIRSTINDEX", firstIndex);
			param.put("SETTEXT", 	setText);
			
			List<Map<String,Object>> list	= pushClientService.SEL_BRD_A01001(param);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					if(i != 0)
						sb.append("\n");
					sb.append(String.valueOf(list.get(i).get("BOARDIDX"))+"\r");
					sb.append(list.get(i).get("BOARDTITLE")+"\r");
					sb.append(list.get(i).get("BOARDDAT"));
				}
			}
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
		response.getWriter().print(FWebDoc.FEncode64Utf8(sb.toString()));
	}
	
	/**
	 * 게시판 상세내역 조회
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	public void SEL_BRD_A01002(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException {
		StringBuffer sb	= new StringBuffer();
		try{
			String BOARD_IDX = FWebDoc.getReq(request, "BOARD_IDX", langByte, langMode, langModeAnsi);
			
			Map<String,String> param	= new HashMap<String,String>();
			param.put("BOARD_IDX", BOARD_IDX);
			
			Map<String,String> map	= pushClientService.SEL_BRD_A01002(param);
			if(map!=null){
				sb.append(String.valueOf(map.get("BOARDIDX"))+"\r");
				sb.append(map.get("BOARDTITLE")+"\r");
				sb.append(map.get("BOARDCNTNT")+"\r");
				sb.append(map.get("BOARDDAT"));
			}
			
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
		response.getWriter().print(FWebDoc.FEncode64Utf8(sb.toString()));
	}
	
	/**
	 * 긴급공지사항 팝업 안내 조회
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException
	 */
	public void SEL_BRD_P01000(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException {
		StringBuffer sb	= new StringBuffer();
		try {
			List<Map<String,Object>> list = pushClientService.SEL_BRD_P01000();
			if(list!=null){
				for(int i=0;i<list.size();i++){
					if(i != 0)
						sb.append("\n");
					sb.append(String.valueOf(list.get(i).get("ENO"))+"\r");
					sb.append(list.get(i).get("TTL")+"\r");
					sb.append(list.get(i).get("CNTNT")+"\r");
					sb.append(list.get(i).get("STDT")+"\r");
					sb.append(list.get(i).get("EDDT")+"\r");
					sb.append(list.get(i).get("ORD"));
				}
			}
		} catch (SQLException e) {
			logger.error(MakeUtil.stackTrace(e));
		} catch(Exception e) {
			logger.error(MakeUtil.stackTrace(e));
		}
		response.getWriter().print(FWebDoc.FEncode64Utf8(sb.toString()));
	}
	
	
	/**
	 * 수신여부 변경
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException
	 */
	public void SEL_BRD_U01001(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException {
		String rStr		= "Y";
		
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("USER_ID", 	request.getParameter("USER_ID"));
			param.put("RCV1", 		MakeUtil.nvl(request.getParameter("RCV1")));
			param.put("RCV2", 		MakeUtil.nvl(request.getParameter("RCV2")));
			
			pushClientService.UPT_BRD_U01001(param);
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
			rStr	= "N";
		}
		response.getWriter().print(rStr);
	}
	
	/**
	 * PUSH 수신 이력 변경
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException
	 */
	public void UPT_PSH_U01001(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException {
		String rStr		= "Y";
		
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("USERID", 	request.getParameter("USER_ID"));
			param.put("PSSNO", 		request.getParameter("PSSNO"));
			param.put("STS", 		request.getParameter("STS"));
			
			System.out.println("param : "+param);
			
			pushClientService.UPT_PSH_U01001(param);
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
			rStr	= "N";
		}
		response.getWriter().print(rStr);
	}
	
	/**
	 * 로그 정보 저장
	 * @param jRequest
	 * @param jResponse
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	public void CLA_LOG_I00000(JSONRequest jRequest,JSONResponse jResponse,HttpServletRequest request,HttpServletResponse response,Model model) throws Exception {
		pushClientService.INS_CLA_LOG_I00000(CLA_SET_LOG_PARAM(request, jRequest.getBodyString("CLNO"), jRequest.getBodyString("CNTNT")));
	}
	
	/**
	 * 로그 정보 설정
	 * @param request
	 * @param fcno
	 * @param cntnt
	 * @return
	 */
	public Map<String,String> CLA_SET_LOG_PARAM(HttpServletRequest request, String clno, String cntnt){
		Map<String,String> param	= new HashMap<String,String>();
		param.put("CLNO", 	clno);
		param.put("IP", 	request.getRemoteAddr());
		param.put("CNTNT", 	cntnt);
		return param;
	}
}

