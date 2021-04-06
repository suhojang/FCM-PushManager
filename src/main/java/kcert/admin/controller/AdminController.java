package kcert.admin.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kcert.admin.service.AdminService;
import kcert.admin.vo.LoginVO;
import kcert.admin.vo.SearchVO;
import kcert.client.service.PushClientService;
import kcert.framework.controller.Controllable;
import kcert.framework.service.FileUploadService;
import kcert.framework.util.FCMResultCode;
import kcert.framework.util.JProperties;
import kcert.framework.util.JRunTimeExec;
import kcert.framework.util.MakeUtil;
import kcert.framework.util.SupportUtil;
import kcert.framework.vo.FileVO;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import au.com.bytecode.opencsv.CSVWriter;

import com.kcert.io.JOutputStream;
import com.kcert.support.Crypto;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
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
public class AdminController extends Controllable {
	private transient Logger logger = Logger.getLogger(this.getClass());
	
	/** fileUploadService */  
	@Resource(name="fileUploadService")
	private FileUploadService fileUploadService;
	
	/** AdminService */
    @Resource(name = "AdminService")
    private AdminService adminService;
    
    /**pushClientService */
	@Resource(name="PushClientService")
	private PushClientService pushClientService;
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * top menu include
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/top.do")
	public String top(HttpServletRequest request, HttpServletResponse response,Model model) {
		return "/menu/top";
	}
	
	/**
	 * top menu include
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/left.do")
	public String left(HttpServletRequest request, HttpServletResponse response,Model model) {
		return "/menu/left";
	}
	
	/**
	 * pagination include
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/pagination.do")
	public String pagination(HttpServletRequest request, HttpServletResponse response,Model model) {
		return "/page/pagination";
	}
	
	/**
	 * 관리자 로그인
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/SEL_LGN_001000.do")
	public String SEL_LGN_001000(HttpServletRequest request, HttpServletResponse response,Model model) {
		String returnStr	= "/login";
		boolean useYN		= false;
		try{
			
			LoginVO loginVO = (LoginVO) request.getSession().getAttribute("loginVO");
			if (loginVO!=null && !"".equals(loginVO.getAdminf_id())) {
				returnStr	= "redirect:/SEL_BRD_001000.do";
			}
			
			String ipAddr	= SupportUtil.getRemoteIP(request);
			
			logger.info("===================== 로그인 시도 =============================");
			logger.info("===========================================================");
			logger.info("===========================================================");
			logger.info("로그인 시도 IP 주소 : " + ipAddr);
			logger.info("로그인 시도 IP 주소 : " + ipAddr);
			
			//허용 아이피 조회
			List<Map<String,Object>> list	= adminService.SEL_IPM_S001009();
			for (int i = 0; i < list.size(); i++) {
				if(ipAddr.equals(list.get(i).get("IPMGRIP")) || "0.0.0.0".equals(list.get(i).get("IPMGRIP"))) {
					useYN	= true;
					break;
				}
			}
			
			
			
		}catch(SQLException e){
			model.addAttribute("ERRMSG", MakeUtil.stackTrace(e));
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			model.addAttribute("ERRMSG", MakeUtil.stackTrace(e));
			logger.error(MakeUtil.stackTrace(e));
		}
		return useYN?returnStr:"/unknown";
	}
	
	/**
	 * 관리자 로그인 처리
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/SEL_LGN_001001.do")
	public String SEL_LGN_001001(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, Model model) {
		String returnStr	= "";
		boolean useYN		= false;
		try{
			String ipAddr	= SupportUtil.getRemoteIP(request);
			
			logger.info("===================== 로그인 시도 =============================");
			logger.info("===========================================================");
			logger.info("===========================================================");
			logger.info("로그인 시도 IP 주소 : " + ipAddr);
			logger.info("로그인 시도 IP 주소 : " + ipAddr);
			
			//허용 아이피 조회
			List<Map<String,Object>> list	= adminService.SEL_IPM_S001009();
			for (int i = 0; i < list.size(); i++) {
				if(ipAddr.equals(list.get(i).get("IPMGRIP")) || "0.0.0.0".equals(list.get(i).get("IPMGRIP"))) {
					useYN	= true;
					break;
				}
			}
			
			if (!useYN) {
				returnStr	= "redirect:/SEL_LGN_001000.do";
			} else {
				String pwd	= Crypto.encrypt(loginVO.getAdminf_pwd(), Controllable.CRYPT_KEY, "UTF-8");
				loginVO.setAdminf_pwd(pwd);
				LoginVO resultVO = adminService.SEL_AMDIN_LOGIN(loginVO);
				
				if (resultVO != null && resultVO.getAdminf_id() != null && !"".equals(resultVO.getAdminf_id())) {
					request.getSession().setAttribute("loginVO", resultVO);
					returnStr	= "redirect:/SEL_BRD_001000.do";
				} else {
					model.addAttribute("message", "암호가 올바르지 않습니다.");
					returnStr	= "/login";
				}
			}
			
			logger.info("===========================================================");
			logger.info("===========================================================");
			logger.info("===================== 로그인 끝 =============================");
			
		}catch(SQLException e){
			model.addAttribute("ERRMSG", MakeUtil.stackTrace(e));
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			model.addAttribute("ERRMSG", MakeUtil.stackTrace(e));
			logger.error(MakeUtil.stackTrace(e));
		}
		return returnStr;
	}
	
	/**
	 * 관리자 로그아웃 처리
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/SEL_LGN_001002.do")
	public String SEL_LGN_001002(HttpServletRequest request, Model model) {
		request.getSession().removeAttribute("loginVO");
		
		return "redirect:/SEL_LGN_001000.do";
	}
	
	/**
	 * 긴급공지사항 조회
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/SEL_BRD_001000.do")
	public String SEL_BRD_001000(@ModelAttribute("searchVO") SearchVO searchVO, ModelMap model) {
		try{
        	//날짜 설정 1개월
        	String day01 = new SimpleDateFormat("yyyy-MM-01").format(new Date());
        	String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        	if("".equals(searchVO.getStDate()) && "".equals(searchVO.getEtDate())){
        		//searchVO.setStDate(day01);
        		searchVO.setEtDate(today);
        	}
        	
        	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        	searchVO.setPageSize(propertiesService.getInt("pageSize"));
        	
        	PaginationInfo paginationInfo = new PaginationInfo();
        	paginationInfo.setCurrentPageNo(searchVO.getPageNo());     
        	paginationInfo.setRecordCountPerPage(searchVO.getPageUnit()); 
        	paginationInfo.setPageSize(searchVO.getPageSize());
        	
        	searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        	searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        	searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        	
        	List<Map<String,Object>> list = adminService.SEL_BRD_001000(searchVO);
        	int totCnt = adminService.SEL_BRD_001000_CNT(searchVO);
        	
        	paginationInfo.setTotalRecordCount(totCnt);
        	model.addAttribute("stDate", searchVO.getStDate());
        	model.addAttribute("etDate", searchVO.getEtDate());
        	model.addAttribute("searchCondition", searchVO.getSearchCondition());
        	model.addAttribute("searchKeyword", searchVO.getSearchKeyword());
        	model.addAttribute("result", list);
        	model.addAttribute("paginationInfo", paginationInfo);
        	model.addAttribute("totCnt", totCnt);
        	model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("BRD"));
        	
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/board/SEL_BRD_001000";
	}
	
	/**
	 * 긴급공지사항 등록페이지 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/INS_BRD_001000.do")
	public String INS_BRD_001000(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, String> param	= new HashMap<String,String>();
		try {
			List<Map<String,Object>> list = adminService.SEL_BRD_001009(param);
			
			model.addAttribute("ORD_CNT", list.size());
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("BRD"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "/board/INS_BRD_001000";
	}
	
	
	/**<PRE>
	 * 긴급공지사항 등록
	 * @param param
	 * @return
	 * @throws Exception
	 * </PRE>
	 */
	@RequestMapping(value="/INS_BRD_001001.do")
	public void INS_BRD_001001(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		
		try {
			param.put("EMGBRD_TTL", 	request.getParameter("EMGBRD_TTL")); 	//제목
			param.put("EMGBRD_CNTNT", 	request.getParameter("EMGBRD_CNTNT"));	//내용
			param.put("EMGBRD_STDT", 	request.getParameter("EMGBRD_STDT"));	//팝업시작일시
			param.put("EMGBRD_EDDT", 	request.getParameter("EMGBRD_EDDT"));	//팝업종료일시
			param.put("EMGBRD_ORD", 	request.getParameter("EMGBRD_ORD"));	//팝업순서
			param.put("ADMINF_ID", 		((LoginVO)request.getSession().getAttribute("loginVO")).getAdminf_id());		//등록자
			
			param.put("PACKAGE", 	OTP_PACKAGE);
			param.put("MKN",  		"03");
			param.put("SKN",  		"01");
			param.put("MSG", 		request.getParameter("EMGBRD_CNTNT"));	//메시지
			
			param.put("PKN",  		"");
			param.put("RVDT",  		"");
			param.put("IMGLNK",  	"");
			param.put("LNK",  		"");
			param.put("SNDYN", 		"Y");
			
			param.put("CD", 	"PSHG_SNO");
			param.put("SQ", 	"0");
			param.put("PSSNO", 	String.valueOf(adminService.NEXT_VAL(param)));	//푸시 발송일련번호 채번
			
			/*********************************************** 긴급공지게시판 저장 ************************************************/
			param.put("CD",		"EMGBRD_NO");
			param.put("SQ",		"0");
			param.put("EMGBRD_NO",	String.valueOf(adminService.NEXT_VAL(param)));	//긴급공지사항 채번
			
			Map<String,String> map	= new HashMap<String,String>();
			String prev_ord	= param.get("EMGBRD_ORD");
			List<Map<String,Object>> list = adminService.SEL_BRD_001009(param);
			for (int i = 0; i < list.size(); i++) {
				if(prev_ord.equals(String.valueOf(list.get(i).get("EMGBRDORD")))){
					int ord		= Integer.parseInt((String) list.get(i).get("EMGBRDORD"));
					//동일한 팝업순번이 존재 할때
					prev_ord	= String.valueOf(Integer.parseInt((String) list.get(i).get("EMGBRDORD")) + 1);
					
					map.put("EMGBRD_NO", 	String.valueOf(list.get(i).get("EMGBRDNO")));
					map.put("EMGBRD_ORD", 	ord!=5?String.valueOf(ord + 1):"OFF");
					
					//기존 팝업 순서 변경
					adminService.UPT_BRD_001009(map);
				}
			}
			//긴급공지사항 등록
			adminService.INS_BRD_001001(param);
			/**************************************************************************************************************/
			
			param.put("CD", 	"PSHG_NO");
			param.put("SQ", 	"0");
			param.put("PSNO", 	String.valueOf(pushClientService.NEXT_VAL(param)));	//푸시 일련번호 채번
			param.put("RUSR", 	param.get("ADMINF_ID"));
			
			//발송정보이력 DB 저장
			pushClientService.INS_CLA_FCM_SND(param);
			
			/*********************************************** 앱 사용자 푸시 전송 ************************************************/
			String server_key					= pushClientService.SEL_CLA_APP_KEY(param);		//FCM 서버키 조회
			
			param.put("OS", "A");
			List<Map<String,Object>> usrListA	= pushClientService.SEL_CLA_TKN_S0001(param);	//앱 사용자 조회(안드로이드)
			
			param.put("OS", "I");
			List<Map<String,Object>> usrListI	= pushClientService.SEL_CLA_TKN_S0001(param);	//앱 사용자 조회(IOS)
			
			String MSG	= param.get("MSG").replaceAll("<br>", "\n");
			String trans	= request.getParameter("PSHG_TRS");
			//안드로이드 전송
			if (usrListA != null && usrListA.size() > 0) {
				Map<String, List<Map<String,Object>>> listMap	= MakeUtil.makeList(usrListA);
				Iterator<String> iter	= listMap.keySet().iterator();
				while (iter.hasNext()) {
					List<Map<String,Object>> alist	= listMap.get(iter.next());
					Map<String,String> result	= sender.send(param.get("PSSNO"), OTP_PACKAGE_NM, TITLE_EMERGENCY + MSG, alist, server_key, 1);
					
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
						pushClientService.INS_CLA_SND_RES(param);
					}
				}
			}
			
			//iOS 전송
			if (usrListI != null && usrListI.size() > 0) {
				Map<String, List<Map<String,Object>>> listMap	= MakeUtil.makeList(usrListI);
				Iterator<String> iter	= listMap.keySet().iterator();
				while (iter.hasNext()) {
					List<Map<String,Object>> ilist	= listMap.get(iter.next());
					Map<String,String> result	= sender.send(param.get("PSSNO"), OTP_PACKAGE_NM, TITLE_EMERGENCY + MSG, ilist, server_key, 2);
					
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
						pushClientService.INS_CLA_SND_RES(param);
					}
				}
			}
			/**************************************************************************************************************/
			
			/*********************************************** SMS사용자 전송 ***************************************************/
			if ("01".equals(trans)) {
				List<Map<String,Object>> smsList	= pushClientService.SEL_CLA_SMS_S0001(param);
				if(smsList!=null && smsList.size() > 0){
					for (int i = 0; i < smsList.size(); i++) {
						param.put("CMID", 		SMS_CMID + String.valueOf(smsList.get(i).get("USRID")));
						param.put("CINFO", 		SMS_CINFO);
						param.put("MSGTYPE", 	SMS_MSGTYPE);
						param.put("STATUS", 	SMS_STATUS);
						param.put("MSG", 		MSG);
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
						
						param.put("MSG", TITLE_EMERGENCY + MSG);
						int rst	= pushClientService.INS_SMS_001000(param);
						
						param.put("MSG", 	MSG);
						param.put("RSNO", 	MakeUtil.getRESKey());	//응답 일련번호 생성
						param.put("USRID", 	String.valueOf(smsList.get(i).get("USRID")));
						param.put("TKNID", 	"SMS");
						param.put("STS", 	rst==1?"04":"99");
						param.put("RST", 	rst==1?"전송 성공":"전송 실패");
						
						//전송정보이력 DB 저장
						pushClientService.INS_CLA_SND_RES(param);
					}
				}
			}
			/**************************************************************************************************************/
		
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 긴급공지사항 상세 조회
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/SEL_BRD_001001.do")
	public String SEL_BRD_001001(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("EMGBRD_NO", request.getParameter("EMGBRD_NO"));
			Map<String,Object> rs = adminService.SEL_BRD_001001(param);
			
			model.addAttribute("result", rs);
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("BRD"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/board/SEL_BRD_001001";
	}
	
	/**
	 * 긴급공지사항 수정 페이지 이동
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/UPT_BRD_001000.do")
	public String UPT_BRD_001000(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			List<Map<String,Object>> list = adminService.SEL_BRD_001009(param);
			model.addAttribute("ORD_CNT", list.size());
			
			param.put("EMGBRD_NO", request.getParameter("EMGBRD_NO"));
			Map<String,Object> rs = adminService.SEL_BRD_001001(param);
			
			model.addAttribute("result", rs);
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("BRD"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/board/UPT_BRD_001000";
	}
	
	/**
	 * 긴급공지사항 수정
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/UPT_BRD_001001.do")
	public String UPT_BRD_001001(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		
		try {
			param.put("EMGBRD_NO", 		request.getParameter("EMGBRD_NO")); 	//게시판 일련번호
			param.put("EMGBRD_TTL", 	request.getParameter("EMGBRD_TTL")); 	//제목
			param.put("EMGBRD_CNTNT", 	request.getParameter("EMGBRD_CNTNT"));	//내용
			param.put("EMGBRD_STDT", 	request.getParameter("EMGBRD_STDT"));	//팝업시작일시
			param.put("EMGBRD_EDDT", 	request.getParameter("EMGBRD_EDDT"));	//팝업종료일시
			param.put("EMGBRD_ORD", 	request.getParameter("EMGBRD_ORD"));	//팝업순서
			param.put("ADMINF_ID", 		((LoginVO)request.getSession().getAttribute("loginVO")).getAdminf_id());		//등록자
			
			/*********************************************** 긴급공지게시판 저장 ************************************************/
			Map<String,String> map	= new HashMap<String,String>();
			//팝업순서가 존재하는 목록 조회
			String prev_ord	= param.get("EMGBRD_ORD");
			List<Map<String,Object>> list = adminService.SEL_BRD_001009(param);
			for (int i = 0; i < list.size(); i++) {
				if(prev_ord.equals(String.valueOf(list.get(i).get("EMGBRDORD")))){
					int ord		= Integer.parseInt((String) list.get(i).get("EMGBRDORD"));
					//동일한 팝업순번이 존재 할때
					prev_ord	= String.valueOf(Integer.parseInt((String) list.get(i).get("EMGBRDORD")) + 1);
					
					map.put("EMGBRD_NO", 	String.valueOf(list.get(i).get("EMGBRDNO")));
					map.put("EMGBRD_ORD", 	ord!=5?String.valueOf(ord + 1):"OFF");
					
					//기존 팝업 순서 변경
					adminService.UPT_BRD_001009(map);
				}
			}
			//긴급공지사항 수정
			adminService.UPT_BRD_001001(param);
		/**************************************************************************************************************/
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
		
    	return "redirect:/SEL_BRD_001000.do";
	}
	
	/**
	 * 긴급공지사항 삭제
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/DEL_BRD_001000.do")
	public String DEL_BRD_001000(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("EMGBRD_NO", request.getParameter("EMGBRD_NO"));
			
			adminService.DEL_BRD_001000(param);
			
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("BRD"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "redirect:/SEL_BRD_001000.do";
	}
	
	/**
	 * 비밀번호 변경 페이지 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	/*
	@RequestMapping(value="/UPT_ADM_001000.do")
	public String UPT_ADM_001000(HttpServletRequest request,HttpServletResponse response,Model model) {
		try{
			LoginVO loginVO			= (LoginVO)request.getSession().getAttribute("loginVO");
			
			String ADMINF_ID		= loginVO.getAdminf_id();
			String ADMINF_CURR_PWD	= new String(Crypto.decryptBytes(loginVO.getAdminf_pwd(), Controllable.CRYPT_KEY, "UTF-8"),"UTF-8");
			
			model.addAttribute("ADMINF_ID", ADMINF_ID);
			model.addAttribute("ADMINF_CURR_PWD", ADMINF_CURR_PWD);
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("PWD"));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
			e.printStackTrace();
		}
		return "/mpg/adm/UPT_ADM_001000";
	}
	*/
	
	/**
	 * 비밀번호 변경
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value="/UPT_ADM_001001.do")
	public String UPT_ADM_001001(@ModelAttribute("loginVO") LoginVO loginVO,HttpServletRequest request,HttpServletResponse response,Model model) {
		try{
			loginVO.setAdminf_pwd(Crypto.encrypt(loginVO.getAdminf_pwd(), Controllable.CRYPT_KEY, "UTF-8"));
			
			int result	= adminService.UPT_ADM_001001(loginVO);
			if (result == 1) {
				LoginVO lv	= (LoginVO)request.getSession().getAttribute("loginVO");
				lv.setAdminf_pwd(loginVO.getAdminf_pwd());
				
				request.getSession().setAttribute("loginVO", lv);
			}
				
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
		return "redirect:/SEL_BRD_001000.do";
	}*/
	
	/**
	 * 아이피관리 조회
	 * @param searchVO
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/SEL_IPM_001000.do")
	public String SEL_IPM_001000(@ModelAttribute("searchVO") SearchVO searchVO, HttpServletRequest request,HttpServletResponse response,Model model) {
		try{
        	//날짜 설정 1개월
        	String day01 = new SimpleDateFormat("yyyy-MM-01").format(new Date());
        	String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        	if("".equals(searchVO.getStDate()) && "".equals(searchVO.getEtDate())){
        		//searchVO.setStDate(day01);
        		searchVO.setEtDate(today);
        	}
        	
        	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        	searchVO.setPageSize(propertiesService.getInt("pageSize"));
        	
        	PaginationInfo paginationInfo = new PaginationInfo();
        	paginationInfo.setCurrentPageNo(searchVO.getPageNo());     
        	paginationInfo.setRecordCountPerPage(searchVO.getPageUnit()); 
        	paginationInfo.setPageSize(searchVO.getPageSize());
        	
        	searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        	searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        	searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        	
        	List<Map<String,Object>> list = adminService.SEL_IPM_001000(searchVO);
        	int totCnt = adminService.SEL_IPM_001000_CNT(searchVO);
        	
        	paginationInfo.setTotalRecordCount(totCnt);
        	model.addAttribute("stDate", searchVO.getStDate());
        	model.addAttribute("etDate", searchVO.getEtDate());
        	model.addAttribute("searchCondition", searchVO.getSearchCondition());
        	model.addAttribute("searchKeyword", searchVO.getSearchKeyword());
        	model.addAttribute("result", list);
        	model.addAttribute("paginationInfo", paginationInfo);
        	model.addAttribute("totCnt", totCnt);
        	model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("IPM"));
        	
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/mpg/ipm/SEL_IPM_001000";
	}
	
	/**
	 * IP관리 등록페이지 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/INS_IPM_001000.do")
	public String INS_IPM_001000(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("IPM"));
		
		return "/mpg/ipm/INS_IPM_001000";
	}
	
	/**
	 * IP관리 등록
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/INS_IPM_001001.do")
	public String INS_IPM_001001(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try {
			param.put("IPMGR_NM",		request.getParameter("IPMGR_NM"));
			param.put("IPMGR_IP", 		request.getParameter("IPMGR_IP"));
			param.put("IPMGR_USEYN", 	request.getParameter("IPMGR_USEYN"));
			
			param.put("CD",		"IPMGR_NO");
			param.put("SQ",		"0");
			param.put("IPMGR_NO",	String.valueOf(adminService.NEXT_VAL(param)));	//IP관리 채번
			
			//IP관리 등록
			adminService.INS_IPM_001001(param);
			
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "redirect:/SEL_IPM_001000.do";
	}
	
	/**
	 * IP관리 상세
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/SEL_IPM_001001.do")
	public String SEL_IPM_001001(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("IPMGR_NO", request.getParameter("IPMGR_NO"));
			Map<String,Object> rs = adminService.SEL_IPM_001001(param);
			
			model.addAttribute("result", rs);
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("IPM"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/mpg/ipm/SEL_IPM_001001";
	}
	
	/**
	 * IP관리 수정페이지 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/UPT_IPM_001000.do")
	public String UPT_IPM_001000(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("IPMGR_NO", request.getParameter("IPMGR_NO"));
			Map<String,Object> rs = adminService.SEL_IPM_001001(param);
			
			String[] ip	= String.valueOf(rs.get("IPMGRIP")).split("\\.");
			rs.put("IPMGRIP1", ip[0]);
			rs.put("IPMGRIP2", ip[1]);
			rs.put("IPMGRIP3", ip[2]);
			rs.put("IPMGRIP4", ip[3]);
			
			model.addAttribute("result", rs);
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("IPM"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/mpg/ipm/UPT_IPM_001000";
	}
	
	/**
	 * IP 수정
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/UPT_IPM_001001.do")
	public String UPT_IPM_001001(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try {
			param.put("IPMGR_NO", 		request.getParameter("IPMGR_NO")); 	//IP 일련번호
			param.put("IPMGR_NM", 		request.getParameter("IPMGR_NM")); 	//사용자명
			param.put("IPMGR_IP", 		request.getParameter("IPMGR_IP"));	//등록IP
			param.put("IPMGR_USEYN", 	request.getParameter("IPMGR_USEYN"));	//사용여부
			
			//IP 수정
			adminService.UPT_IPM_001001(param);
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
		
    	return "redirect:/SEL_IPM_001000.do";
	}
	
	/**
	 * IP삭제
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/DEL_IPM_001000.do")
	public String DEL_IPM_001000(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("IPMGR_NO", request.getParameter("IPMGR_NO"));
			
			adminService.DEL_IPM_001000(param);
			
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("IPM"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "redirect:/SEL_IPM_001000.do";
	}
	

	/**
	 * 앱정보 조회
	 * @param searchVO
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/SEL_APP_001000.do")
	public String SEL_APP_001000(@ModelAttribute("searchVO") SearchVO searchVO, HttpServletRequest request,HttpServletResponse response,Model model) {
		try{
        	//날짜 설정 1개월
        	String day01 = new SimpleDateFormat("yyyy-MM-01").format(new Date());
        	String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        	if("".equals(searchVO.getStDate()) && "".equals(searchVO.getEtDate())){
        		//searchVO.setStDate(day01);
        		searchVO.setEtDate(today);
        	}
        	
        	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        	searchVO.setPageSize(propertiesService.getInt("pageSize"));
        	
        	PaginationInfo paginationInfo = new PaginationInfo();
        	paginationInfo.setCurrentPageNo(searchVO.getPageNo());     
        	paginationInfo.setRecordCountPerPage(searchVO.getPageUnit()); 
        	paginationInfo.setPageSize(searchVO.getPageSize());
        	
        	searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        	searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        	searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        	
        	List<Map<String,Object>> list = adminService.SEL_APP_001000(searchVO);
        	int totCnt = adminService.SEL_APP_001000_CNT(searchVO);
        	
        	paginationInfo.setTotalRecordCount(totCnt);
        	model.addAttribute("stDate", searchVO.getStDate());
        	model.addAttribute("etDate", searchVO.getEtDate());
        	model.addAttribute("searchCondition", searchVO.getSearchCondition());
        	model.addAttribute("searchKeyword", searchVO.getSearchKeyword());
        	model.addAttribute("result", list);
        	model.addAttribute("paginationInfo", paginationInfo);
        	model.addAttribute("totCnt", totCnt);
        	model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("APP"));
        	
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/app/SEL_APP_001000";
	}
	
	/**
	 * 앱정보 등록페이지 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/INS_APP_001000.do")
	public String INS_APP_001000(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("APP"));
		
		return "/app/INS_APP_001000";
	}
	
	/**
	 * APP정보 등록
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/INS_APP_001001.do")
	public String INS_APP_001001(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try {
			param.put("APPINF_PKNM",	request.getParameter("APPINF_PKNM"));
			param.put("APPINF_APPNM", 	request.getParameter("APPINF_APPNM"));
			param.put("APPINF_ID", 		request.getParameter("APPINF_ID"));
			param.put("APPINF_KEY", 	request.getParameter("APPINF_KEY"));
			param.put("APPINF_USEYN", 	request.getParameter("APPINF_USEYN"));
			
			param.put("CD",		"APPINF_NO");
			param.put("SQ",		"0");
			param.put("APPINF_NO",	String.valueOf(adminService.NEXT_VAL(param)));	//APP정보관리 채번
			
			//APP정보 등록
			adminService.INS_APP_001001(param);
			
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "redirect:/SEL_APP_001000.do";
	}
	
	/**
	 * APP정보 상세
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/SEL_APP_001001.do")
	public String SEL_APP_001001(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("APPINF_NO", request.getParameter("APPINF_NO"));
			Map<String,Object> rs = adminService.SEL_APP_001001(param);
			
			model.addAttribute("result", rs);
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("APP"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/app/SEL_APP_001001";
	}
	
	/**
	 * APP정보 수정페이지 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/UPT_APP_001000.do")
	public String UPT_APP_001000(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("APPINF_NO", request.getParameter("APPINF_NO"));
			Map<String,Object> rs = adminService.SEL_APP_001001(param);
			
			model.addAttribute("result", rs);
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("APP"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/app/UPT_APP_001000";
	}
	
	/**
	 * APP정보 수정
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/UPT_APP_001001.do")
	public String UPT_APP_001001(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try {
			param.put("APPINF_NO", 		request.getParameter("APPINF_NO")); 	//앱정보 일련번호
			param.put("APPINF_PKNM", 	request.getParameter("APPINF_PKNM")); 	//패키지명
			param.put("APPINF_APPNM", 	request.getParameter("APPINF_APPNM"));	//앱이름
			param.put("APPINF_ID", 		request.getParameter("APPINF_ID"));		//발신자ID
			param.put("APPINF_KEY", 	request.getParameter("APPINF_KEY"));	//서버키
			param.put("APPINF_USEYN", 	request.getParameter("APPINF_USEYN"));	//사용여부
			
			//APP정보 수정
			adminService.UPT_APP_001001(param);
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
		
    	return "redirect:/SEL_APP_001000.do";
	}
	
	/**
	 * APP정보 삭제
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/DEL_APP_001000.do")
	public String DEL_APP_001000(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("APPINF_NO", request.getParameter("APPINF_NO"));
			
			adminService.DEL_APP_001000(param);
			
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("APP"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "redirect:/SEL_APP_001000.do";
	}
	
	/**
	 * My 그룹 리스트 조회
	 * @param searchVO
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/SEL_GRP_001000.do")
	public String SEL_GRP_001000(@ModelAttribute("searchVO") SearchVO searchVO, HttpServletRequest request,HttpServletResponse response,Model model) {
		try{
        	//날짜 설정 1개월
        	String day01 = new SimpleDateFormat("yyyy-MM-01").format(new Date());
        	String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        	if("".equals(searchVO.getStDate()) && "".equals(searchVO.getEtDate())){
        		//searchVO.setStDate(day01);
        		searchVO.setEtDate(today);
        	}
        	
        	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        	searchVO.setPageSize(propertiesService.getInt("pageSize"));
        	
        	PaginationInfo paginationInfo = new PaginationInfo();
        	paginationInfo.setCurrentPageNo(searchVO.getPageNo());     
        	paginationInfo.setRecordCountPerPage(searchVO.getPageUnit()); 
        	paginationInfo.setPageSize(searchVO.getPageSize());
        	
        	searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        	searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        	searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        	
        	List<Map<String,Object>> list = adminService.SEL_GRP_001000(searchVO);
        	int totCnt = adminService.SEL_GRP_001000_CNT(searchVO);
        	
        	paginationInfo.setTotalRecordCount(totCnt);
        	model.addAttribute("stDate", searchVO.getStDate());
        	model.addAttribute("etDate", searchVO.getEtDate());
        	model.addAttribute("searchCondition", searchVO.getSearchCondition());
        	model.addAttribute("searchKeyword", searchVO.getSearchKeyword());
        	model.addAttribute("result", list);
        	model.addAttribute("paginationInfo", paginationInfo);
        	model.addAttribute("totCnt", totCnt);
        	model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("GRP"));
        	
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/mpg/grp/SEL_GRP_001000";
	}
	
	/**
	 * My Group 등록페이지 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/INS_GRP_001000.do")
	public String INS_GRP_001000(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("GRP"));
		
		return "/mpg/grp/INS_GRP_001000";
	}
	
	/**
	 * 지역조회
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value="/SEL_ORG_001000.do")
	public ModelAndView SEL_ORG_001000(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException, SQLException, Exception {
		ModelAndView mv	= new ModelAndView(); 
		
		/*******************************      [[ JIYUK_CODE  ]]       *********************************************
		근로복지공단(CD00A) : A00
		공단본부(CD01A): A53, A0B, A32, A47, A43, A44, A22, A26, A57, A13, A54, A45, A46, A40, A21, A55, A29, A48, A49, A53, ITF
		서울지역(CD00C) : C0R, C2S, C3S, C4S, C5S, C6S, C7S, CFT, CGT, C9S, CAS, CCT, CDT, CBT, CET, CDD
		부산지역(CD00D) : D0R, D9S, D3S, DAT, D4S, D5S, D6S, D7S, D8T, DDD
		대구지역(CD00E) : E0R, E9S, E8S, E3S, E4S, EAT, E5T, E6T, EDD
		경인지역(CD00F) : F0R, F2S, F3S, FAS, F4S, F5S, F6S, FBS, F8S, FDD
		광주지역(CD00G) : G0R, G2S, G3S, G4S, G5S, G6S, G7S, GAT, G8S, GDD
		대전지역(CD00H) : H0R, H2S, H3S, H4T, H5T, H6T, HDD
		인재개발원(CD00B) : B0R
		병원(CD00T) : T1R, U1R, M1R, R1R, Q1R, P1R, J2R, S1R, L1R, K1R
		재활공학연구소(CDV0R) : V0R
		콜센터(CDIOR) : IOR
		**********************************************************************************************************/
		StringBuffer sb	= new StringBuffer();
		try{
			Map<String,String> param	= new HashMap<String,String>();
			List<Map<String,Object>> list	= adminService.SEL_ORG_001000(param);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					if(i != 0)
						sb.append("\n");
					sb.append(list.get(i).get("CD")+"\r");
					sb.append(list.get(i).get("NM"));
				}
			}
			mv.addObject("RESULT", sb.toString());
			
		}catch(SQLException e){
			logger.error(e);
		}catch(Exception e){
			logger.error(e);
		}
		mv.setViewName("jsonView");
		
		return mv;
	}
	
	/**
	 * 지역선택 => 지사 조회
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value="/SEL_ORG_001001.do")
	public ModelAndView SEL_ORG_001001(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException, SQLException, Exception {
		ModelAndView mv	= new ModelAndView();
		
		/*******************************      [[ JIYUK_CODE  ]]       *********************************************
		근로복지공단(CD00A) : A00
		공단본부(CD01A): A53, A0B, A32, A47, A43, A44, A22, A26, A57, A13, A54, A45, A46, A40, A21, A55, A29, A48, A49, A53, ITF
		서울지역(CD00C) : C0R, C2S, C3S, C4S, C5S, C6S, C7S, CFT, CGT, C9S, CAS, CCT, CDT, CBT, CET, CDD
		부산지역(CD00D) : D0R, D9S, D3S, DAT, D4S, D5S, D6S, D7S, D8T, DDD
		대구지역(CD00E) : E0R, E9S, E8S, E3S, E4S, EAT, E5T, E6T, EDD
		경인지역(CD00F) : F0R, F2S, F3S, FAS, F4S, F5S, F6S, FBS, F8S, FDD
		광주지역(CD00G) : G0R, G2S, G3S, G4S, G5S, G6S, G7S, GAT, G8S, GDD
		대전지역(CD00H) : H0R, H2S, H3S, H4T, H5T, H6T, HDD
		인재개발원(CD00B) : B0R
		병원(CD00T) : T1R, U1R, M1R, R1R, Q1R, P1R, J2R, S1R, L1R, K1R
		재활공학연구소(CDV0R) : V0R
		**********************************************************************************************************/
		
		StringBuffer sb	= new StringBuffer();
		try{
			String JYCD	= request.getParameter("JYCD");

			Map<String,String> param	= new HashMap<String,String>();
			param.put("JYCD", JYCD);
			
			List<Map<String,Object>> list	= adminService.SEL_ORG_001001(param);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					if(i != 0)
						sb.append("\n");
					sb.append(list.get(i).get("CD")+"\r");
					sb.append(list.get(i).get("NM"));
				}
			}
			mv.addObject("RESULT", sb.toString());
			
		}catch(SQLException e){
			logger.error(e);
		}catch(Exception e){
			logger.error(e);
		}
		mv.setViewName("jsonView");
		
		return mv;
	}
	
	/**
	 * 지사선택 => 부서 조회
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value="/SEL_ORG_001002.do")
	public ModelAndView SEL_ORG_001002(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException, SQLException, Exception {
		ModelAndView mv	= new ModelAndView();
		
		/*********************************************************
		인천고객지원센터 : FFT
		인천고객지원 : FFT
		광주고객지원센터 : GGU
		울산고객지원센터 : DDE
		직업성폐질환연구소 : X0R
		근로복지연구원 : W0R
		재활공학연구소 : V0R
		
		공단본부(A53), 산재모병원 건립추진단(A53) 코드번호가 동일하므로 지사이름을 받아 검색한다.
		*********************************************************/
		StringBuffer sb	= new StringBuffer();
		try{
			Map<String,String> param	= new HashMap<String,String>();
			param.put("JSCD", 	request.getParameter("JSCD"));
			param.put("JSNM", 	request.getParameter("JSNM"));
			
			List<Map<String,Object>> list	= adminService.SEL_ORG_001002(param);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					if(i != 0)
						sb.append("\n");
					sb.append(list.get(i).get("JSCD")+"\r");
					sb.append(list.get(i).get("JSNM")+"\r");
					sb.append(list.get(i).get("BSCD")+"\r");
					sb.append(list.get(i).get("BSNM"));
				}
			}
			mv.addObject("RESULT", sb.toString());
			
		}catch(SQLException e){
			logger.error(e);
		}catch(Exception e){
			logger.error(e);
		}
		mv.setViewName("jsonView");
		
		return mv;
	}
	
	/**
	 * 부서선택 => 직원 조회
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value="/SEL_ORG_001003.do")
	public ModelAndView SEL_ORG_001003(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException, SQLException, Exception {
		ModelAndView mv	= new ModelAndView();
		StringBuffer sb	= new StringBuffer();
		try{
			Map<String,String> param	= new HashMap<String,String>();
			param.put("JYCD", 	request.getParameter("JYCD"));
			param.put("JSCD", 	request.getParameter("JSCD"));
			param.put("JSNM", 	request.getParameter("JSNM"));
			param.put("BSCD", 	request.getParameter("BSCD"));
			param.put("searchCondition", 	request.getParameter("searchCondition"));
			param.put("searchKeyword", 		request.getParameter("searchKeyword"));

			List<Map<String,Object>> list	= adminService.SEL_ORG_001003(param);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					if(i != 0)
						sb.append("\n");
					sb.append(list.get(i).get("UID")+"\r");
					sb.append(list.get(i).get("UNM")+"\r");
					sb.append(list.get(i).get("OFF1")+"\r");
					sb.append(list.get(i).get("OFF2")+"\r");
					sb.append(list.get(i).get("PHONE")+"\r");
					sb.append(list.get(i).get("EMAIL")+"\r");
					sb.append(list.get(i).get("JSNM")+"\r");
					sb.append(list.get(i).get("BSNM")+"\r");
					sb.append(list.get(i).get("JWNM")+"\r");
					sb.append(list.get(i).get("POLICY"));
				}
			}
			mv.addObject("RESULT", sb.toString());
			
		}catch(SQLException e){
			logger.error(e);
		}catch(Exception e){
			logger.error(e);
		}
		mv.setViewName("jsonView");
		
		return mv;
	}
	
	/**
	 * 사용자 정보 조회(지역선택, 지사선택)
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value="/SEL_ORG_001004.do")
	public ModelAndView SEL_ORG_001004(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException, SQLException, Exception {
		ModelAndView mv	= new ModelAndView();
		StringBuffer sb	= new StringBuffer();
		try{
			Map<String,String> param	= new HashMap<String,String>();
			param.put("JYCD", 	request.getParameter("JYCD"));
			param.put("JSCD", 	request.getParameter("JSCD"));
			param.put("searchCondition", 	request.getParameter("searchCondition"));
			param.put("searchKeyword", 		request.getParameter("searchKeyword"));

			List<Map<String,Object>> list	= adminService.SEL_ORG_001004(param);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					if(i != 0)
						sb.append("\n");
					sb.append(list.get(i).get("UID")+"\r");
					sb.append(list.get(i).get("UNM")+"\r");
					sb.append(list.get(i).get("OFF1")+"\r");
					sb.append(list.get(i).get("OFF2")+"\r");
					sb.append(list.get(i).get("PHONE")+"\r");
					sb.append(list.get(i).get("EMAIL")+"\r");
					sb.append(list.get(i).get("JSNM")+"\r");
					sb.append(list.get(i).get("BSNM")+"\r");
					sb.append(list.get(i).get("JWNM")+"\r");
					sb.append(list.get(i).get("POLICY"));
				}
			}
			mv.addObject("RESULT", sb.toString());
			
		}catch(SQLException e){
			logger.error(e);
		}catch(Exception e){
			logger.error(e);
		}
		mv.setViewName("jsonView");
		
		return mv;
	}
	
	/**
	 * My Group 정보 등록
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/INS_GRP_001001.do")
	public String INS_GRP_001001(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try {
			param.put("MYGRP_NM",	request.getParameter("MYGRP_NM"));
			param.put("CD",		"MYGRP_NO");
			param.put("SQ",		"0");
			param.put("MYGRP_NO",	String.valueOf(adminService.NEXT_VAL(param)));	//My Group 정보 채번
			
			//그룹 정보 등록
			adminService.INS_GRP_001001(param);
			String[] arr	= MakeUtil.makeStringArray(request.getParameter("PARAMS"), ",");
			for(int i=0;i<arr.length;i++){
				param.put("CD",		"GRPUSR_NO");
				param.put("SQ",		"0");
				param.put("GRPUSR_NO",	String.valueOf(adminService.NEXT_VAL(param)));	//My Group 사용자정보 채번
				param.put("GRPUSR_ID", 	arr[i]);
				
				adminService.INS_GRP_001009(param);
			}
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "redirect:/SEL_GRP_001000.do";
	}
	
	/**
	 * My Group 상세
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/SEL_GRP_001001.do")
	public String SEL_GRP_001001(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("MYGRP_NO", request.getParameter("MYGRP_NO"));
			List<Map<String,Object>> list = adminService.SEL_GRP_001001(param);
			
			model.addAttribute("MNO", String.valueOf(list.get(0).get("MNO")));
			model.addAttribute("MNM", String.valueOf(list.get(0).get("MNM")));
			model.addAttribute("result", list);
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("GRP"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/mpg/grp/SEL_GRP_001001";
	}
	
	/**
	 * My Group 삭제
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/DEL_GRP_001000.do")
	public String DEL_GRP_001000(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("MYGRP_NO", request.getParameter("MYGRP_NO"));
			
			adminService.DEL_GRP_001000(param);
			
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("GRP"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "redirect:/SEL_GRP_001000.do";
	}
	
	/**
	 * My Group 수정페이지 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/UPT_GRP_001000.do")
	public String UPT_GRP_001000(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("MYGRP_NO", request.getParameter("MYGRP_NO"));
			List<Map<String,Object>> list = adminService.SEL_GRP_001001(param);
			
			model.addAttribute("MNO", String.valueOf(list.get(0).get("MNO")));
			model.addAttribute("MNM", String.valueOf(list.get(0).get("MNM")));
			model.addAttribute("result", list);
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("GRP"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/mpg/grp/UPT_GRP_001000";
	}
	
	/**
	 * My Group 사용자목록 조회
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/UPT_GRP_001002.do")
	public ModelAndView UPT_GRP_001002(HttpServletRequest request, HttpServletResponse response, Model model) {
		ModelAndView mv	= new ModelAndView();
		
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("MYGRP_NO", request.getParameter("MYGRP_NO"));
			List<Map<String,Object>> list = adminService.SEL_GRP_001001(param);
			
			mv.addObject("RESULT", list);
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
		mv.setViewName("jsonView");
    	return mv;
	}
	
	/**
	 * My Group 수정
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/UPT_GRP_001001.do")
	public String UPT_GRP_001001(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try {
			request.setCharacterEncoding("UTF-8");
			
			param.put("MYGRP_NO", request.getParameter("MYGRP_NO"));
			param.put("MYGRP_NM", request.getParameter("MYGRP_NM"));
			//My Group 수정
			adminService.UPT_GRP_001001(param);
			
			String[] arr	= MakeUtil.makeStringArray(request.getParameter("PARAMS"), ",");
			for(int i=0;i<arr.length;i++){
				param.put("CD",		"GRPUSR_NO");
				param.put("SQ",		"0");
				param.put("GRPUSR_NO",	String.valueOf(adminService.NEXT_VAL(param)));	//My Group 사용자정보 채번
				param.put("GRPUSR_ID", 	arr[i]);
				
				adminService.INS_GRP_001009(param);
			}
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
		
    	return "redirect:/SEL_GRP_001000.do";
	}
	
	/**
	 * 메시지 전송 화면이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/INS_MSG_001000.do")
	public String INS_MSG_001000(HttpServletRequest request,HttpServletResponse response,Model model) {
		try{
			//전체 sms,push 사용자 수 조회
			Map<String,Object> result = adminService.SEL_MSG_S01000();
			//앱 정보 조회
			List<Map<String,Object>> list = adminService.SEL_MSG_S01011();
			
			model.addAttribute("SMSCNT", String.valueOf(result.get("SMSCNT")));
			model.addAttribute("PUSHCNT", String.valueOf(result.get("PUSHCNT")));
			model.addAttribute("result", list);
			
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("MSG"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
		return "/msg/INS_MSG_001000";
	}
	
	/**
	 * My 그룹 정보 조회
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/SEL_MSG_001001.do")
	public ModelAndView SEL_MSG_001001(HttpServletRequest request,HttpServletResponse response,Model model) {
		ModelAndView mv				= new ModelAndView();
		try{
			List<Map<String,Object>> list	= adminService.SEL_MSG_S01001();
			
			mv.addObject("result", list);
		}catch(Exception e){
			
			e.printStackTrace();
		}
		mv.setViewName("jsonView");
		
		return mv;
	}
	
	/**
	 * 그룹 사용자 정보 조회
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/SEL_MSG_001002.do")
	public ModelAndView SEL_MSG_001002(HttpServletRequest request,HttpServletResponse response,Model model) {
		ModelAndView mv				= new ModelAndView();
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("MYGRP_NO", request.getParameter("MYGRP_NO"));
			List<Map<String,Object>> list = adminService.SEL_GRP_001001(param);
			mv.addObject("result", list);
		}catch(Exception e){
			e.printStackTrace();
		}
		mv.setViewName("jsonView");
		
		return mv;
	}
	
	/**
	 * 메시지 전송
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/INS_MSG_001001.do")
	public void INS_MSG_001001(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,List<FileVO>>	files		= null;
		FileVO						imgVo		= null;
		Map<String,String> param	= new HashMap<String,String>();
		
		try{
			if(request instanceof MultipartHttpServletRequest)
				files	= fileUploadService.fileUpload(request,"INS_MSG_I01001");	//파일저장 및 파일정보 반환
			
			if(files!=null && files.get("PSHG_IMGLNK")!=null && files.get("PSHG_IMGLNK").size()>0)
				imgVo	= files.get("PSHG_IMGLNK").get(0);
			else
				imgVo	= new FileVO();
			
			param.put("IMGLNK", 	imgVo.getNewName());//이미지파일명
			
			param.put("APPNM", 		request.getParameter("APPNM"));	//앱 패키지이름
			param.put("PACKAGE", 	request.getParameter("PSHG_PACKNM"));	//앱 패키지명
			param.put("MSG", 		request.getParameter("PSHG_MSG"));	//메시지내용
			param.put("MKN", 		"01");	//발송구분
			param.put("SKN", 		request.getParameter("PSHG_SKN"));	//전송구분
			param.put("PKN", 		"");	//포탈전송구분
			param.put("RVDT", 		request.getParameter("PSHG_RVDT"));	//예약발송일시
			param.put("LNK", 		request.getParameter("PSHG_LNK"));	//링크URL
			param.put("SNDYN", 		"02".equals(param.get("SKN"))?"N":"Y");	//발송여부
			
			param.put("CD", 	"PSHG_SNO");
			param.put("SQ", 	"0");
			param.put("PSSNO", 	String.valueOf(pushClientService.NEXT_VAL(param)));	//푸시 발송일련번호 채번
			
			param.put("CD", 	"PSHG_NO");
			param.put("SQ", 	"0");
			param.put("PSNO", 	String.valueOf(pushClientService.NEXT_VAL(param)));	//푸시 일련번호 채번
			param.put("RUSR", 	((LoginVO)request.getSession().getAttribute("loginVO")).getAdminf_id());
			
			//발송정보이력 DB 저장
			pushClientService.INS_CLA_FCM_SND(param);
			
			String server_key	= pushClientService.SEL_CLA_APP_KEY(param);		//FCM 서버키 조회
			
			List<Map<String,Object>> usrListA	= new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> usrListI	= new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> smsList	= new ArrayList<Map<String,Object>>();
			
			String ids		= request.getParameter("GRPUSR_IDS");
			String trans	= request.getParameter("PSHG_TRS");
			
			//예약전송
			if("02".equals(param.get("SKN"))){
				param.put("CD", 		"RSVMGR_NO");
				param.put("SQ", 		"0");
				param.put("RSNO", 		String.valueOf(adminService.NEXT_VAL(param)));	//예약전송관리 일련번호 채번
				param.put("RSVMGR_IDS",	ids);
				//예약전송 사용자 추가
				adminService.INS_RSV_001000(param);
				
			}else{
				if("ALL".equals(ids)){
					//전체사용자 전송
					param.put("OS", "A");
					usrListA	= pushClientService.SEL_CLA_TKN_S0001(param);	//앱 사용자 조회(안드로이드)
					
					param.put("OS", "I");
					usrListI	= pushClientService.SEL_CLA_TKN_S0001(param);	//앱 사용자 조회(IOS)
					
					smsList	= pushClientService.SEL_CLA_SMS_S0001(param);	//sms 전체 사용자 조회
				}else{
					//선택 되어진 사용자 토큰 조회
					param.put("PARAMS", MakeUtil.makeConditionParams(ids, ","));
					param.put("OS", "A");
					usrListA	= adminService.SEL_ADM_TKN_S0001(param);	//앱 사용자 조회(안드로이드)
					
					param.put("OS", "I");
					usrListI	= adminService.SEL_ADM_TKN_S0001(param);	//앱 사용자 조회(IOS)
					
					for(int i=usrListA.size()-1;i>=0;i--){
						if("SMS".equals(String.valueOf(usrListA.get(i).get("TKNID")))){
							smsList.add(usrListA.get(i));
							usrListA.remove(i);
						}
					}
					
					for(int i=usrListI.size()-1;i>=0;i--){
						if("SMS".equals(String.valueOf(usrListI.get(i).get("TKNID")))){
							smsList.add(usrListI.get(i));
							usrListI.remove(i);
						}
					}
				}
				
				//이미지 url
				String IMGLNK	= "";
				if (!"".equals(param.get("IMGLNK"))) {
					IMGLNK	= new JProperties().getUploadUrl() + param.get("IMGLNK");
				}
				/*********************************************** 앱 사용자 푸시 전송 ************************************************/
				if (usrListA!=null && usrListA.size() > 0) {
					Map<String, List<Map<String,Object>>> listMap	= MakeUtil.makeList(usrListA);
					Iterator<String> iter	= listMap.keySet().iterator();
					while (iter.hasNext()) {
						List<Map<String,Object>> alist	= listMap.get(iter.next());
						Map<String,String> result	= sender.send(param.get("PSSNO"), param.get("APPNM"), param.get("MSG"), param.get("LNK"), IMGLNK, alist, server_key, 1);
						
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
							pushClientService.INS_CLA_SND_RES(param);
						}
					}
				}
				
				if (usrListI!=null && usrListI.size() > 0) {
					Map<String, List<Map<String,Object>>> listMap	= MakeUtil.makeList(usrListI);
					Iterator<String> iter	= listMap.keySet().iterator();
					while (iter.hasNext()) {
						List<Map<String,Object>> ilist	= listMap.get(iter.next());
						Map<String,String> result	= sender.send(param.get("PSSNO"), param.get("APPNM"), param.get("MSG"), param.get("LNK"), IMGLNK, ilist, server_key, 2);
						
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
							pushClientService.INS_CLA_SND_RES(param);
						}
					}
				}
				/**************************************************************************************************************/
				
				if("01".equals(trans)){
					/*********************************************** SMS사용자 전송 ***************************************************/
					if (smsList != null && smsList.size() > 0) {
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
					}
					/**************************************************************************************************************/
				}
			}
				
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/SEL_STA_001000.do")
	public String SEL_STA_001000(@ModelAttribute("searchVO") SearchVO searchVO, Model model) {
		try{
        	//날짜 설정 1개월
        	String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        	if("".equals(searchVO.getStDate())){
        		searchVO.setStDate(today);
        	}
        	if("".equals(searchVO.getEtDate())){
        		searchVO.setEtDate(today);
        	}
        	
        	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        	searchVO.setPageSize(propertiesService.getInt("pageSize"));
        	
        	PaginationInfo paginationInfo = new PaginationInfo();
        	paginationInfo.setCurrentPageNo(searchVO.getPageNo());     
        	paginationInfo.setRecordCountPerPage(searchVO.getPageUnit()); 
        	paginationInfo.setPageSize(searchVO.getPageSize());
        	
        	searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        	searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        	searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        	
        	//통계 조회
        	//Map<String,String> rs 			= adminService.SEL_STA_001009(searchVO);
        	int PSCNT 			= adminService.SEL_STA_001009_01(searchVO);
        	int PFCNT 			= adminService.SEL_STA_001009_02(searchVO);
        	int SMSCNT 			= adminService.SEL_STA_001009_03(searchVO);
        	int PRCNT 			= adminService.SEL_STA_001009_04(searchVO);
        	int PRDCNT 			= adminService.SEL_STA_001009_05(searchVO);
        	
        	Map<String,String> rs	= new HashMap<String,String>();
        	rs.put("PSCNT", 	String.valueOf(PSCNT));		//푸시 발송 성공 건
        	rs.put("PFCNT", 	String.valueOf(PFCNT));		//푸시 발송 실패 건
        	rs.put("SMSCNT", 	String.valueOf(SMSCNT));	//SMS 발송 건
        	rs.put("PRCNT", 	String.valueOf(PRCNT));		//수신 성공 건
        	rs.put("PRFCNT", 	String.valueOf(PSCNT - PRCNT));	//수신 실패 건
        	rs.put("PRDCNT", 	String.valueOf(PRDCNT));	//읽음 성공 건
        	rs.put("PRDFCNT", 	String.valueOf(PRCNT - PRDCNT));	//읽음 실패 건
        	
        	//리스트 조회
        	List<Map<String,Object>> list 	= adminService.SEL_STA_001000(searchVO);
        	int totCnt = adminService.SEL_STA_001000_CNT(searchVO);
        	
        	//앱 정보 조회
        	List<Map<String,Object>> appInfo = adminService.SEL_MSG_S01011();
        	
        	paginationInfo.setTotalRecordCount(totCnt);
        	model.addAttribute("stDate", searchVO.getStDate());
        	model.addAttribute("etDate", searchVO.getEtDate());
        	model.addAttribute("searchCondition", searchVO.getSearchCondition());
        	model.addAttribute("appInfo", appInfo);
        	model.addAttribute("result", list);
        	model.addAttribute("data", rs);
        	model.addAttribute("paginationInfo", paginationInfo);
        	model.addAttribute("totCnt", totCnt);
        	model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("STA"));
        	
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
		return "/stat/SEL_STA_001000";
	}
	
	@RequestMapping(value="/SEL_STA_001001.do")
	public ModelAndView SEL_STA_001001(HttpServletRequest request,HttpServletResponse response,Model modell) {
		ModelAndView mv	= new ModelAndView();
		
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("PSHG_SNO", request.getParameter("PSHG_SNO"));
			List<Map<String,Object>> list = adminService.SEL_STA_001001(param);
			
			mv.addObject("RESULT", list);
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
		mv.setViewName("jsonView");
    	
		return mv;
	}
	
	@RequestMapping(value="/SEL_STA_001002.do")
	public String SEL_STA_001002(HttpServletRequest request,HttpServletResponse response,Model modell) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("PSHG_SNO", request.getParameter("PSHG_SNO"));
			
			Map<String,String> info	= adminService.SEL_STA_001002(param);
			String MSG			= info.get("MSG");
			String APPNM		= info.get("APPNM");
			String server_key	= info.get("APPKEY");
			String IMGLNK		= info.get("IMGLNK");
			String LNK			= info.get("LNK");
			
			param.put("OS", "A");
			List<Map<String,Object>> usrListA = adminService.SEL_STA_001003(param);
			param.put("OS", "I");
			List<Map<String,Object>> usrListI = adminService.SEL_STA_001003(param);
			
			if (usrListA != null && usrListA.size() > 0) {
				Map<String, List<Map<String,Object>>> listMap	= MakeUtil.makeList(usrListA);
				Iterator<String> iter	= listMap.keySet().iterator();
				while (iter.hasNext()) {
					List<Map<String,Object>> list	= listMap.get(iter.next());
					Map<String,String> result	= sender.send(param.get("PSHG_SNO"), APPNM, MSG, LNK, IMGLNK, list, server_key, 1);
					
					FCMResultCode message	= new FCMResultCode();
					String code				= result.get("RESCODE");
					
					for (int i = 0; i < list.size(); i++) {
						String yn		= result.get(list.get(i).get("TKNID"));
						String error	= MakeUtil.makeString(yn, "_", null, false);
						
						param.put("RSNO", 	String.valueOf(list.get(i).get("RSNO")));
						param.put("USRID", 	String.valueOf(list.get(i).get("USRID")));
						param.put("TKNID", 	String.valueOf(list.get(i).get("TKNID")));
						param.put("STS", 	error==null?"01":"99");
						param.put("RST", 	message.getMessage(code, error));
						
						//전송정보이력 DB 수정
						adminService.UPT_SND_001001(param);
					}
				}
			}
			
			//iOS 전송
			if (usrListI != null && usrListI.size() > 0) {
				Map<String, List<Map<String,Object>>> listMap	= MakeUtil.makeList(usrListI);
				Iterator<String> iter	= listMap.keySet().iterator();
				while (iter.hasNext()) {
					List<Map<String,Object>> list	= listMap.get(iter.next());
					Map<String,String> result	= sender.send(param.get("PSHG_SNO"), APPNM, MSG, LNK, IMGLNK, list, server_key, 2);
					
					FCMResultCode message	= new FCMResultCode();
					String code				= result.get("RESCODE");
					
					for (int i = 0; i < list.size(); i++) {
						String yn		= result.get(list.get(i).get("TKNID"));
						String error	= MakeUtil.makeString(yn, "_", null, false);
						
						param.put("RSNO", 	String.valueOf(list.get(i).get("RSNO")));
						param.put("USRID", 	String.valueOf(list.get(i).get("USRID")));
						param.put("TKNID", 	String.valueOf(list.get(i).get("TKNID")));
						param.put("STS", 	error==null?"01":"99");
						param.put("RST", 	message.getMessage(code, error));
						
						//전송정보이력 DB 수정
						adminService.UPT_SND_001001(param);
					}
				}
			}
			
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
		
		return "redirect:/SEL_STA_001000.do";
	}
	
	@RequestMapping(value="/STA_CSV_DOWN.do")
	public String STA_CSV_DOWN(HttpServletRequest request,HttpServletResponse response,Model model) {
		String fileName	= "통계";
		try{
			Map<String,String> param	= new HashMap<String,String>();
			param.put("searchCondition", MakeUtil.isNull(request.getParameter("searchCondition")));
			param.put("stDate", MakeUtil.isNull(request.getParameter("stDate")));
			param.put("etDate", MakeUtil.isNull(request.getParameter("etDate")));
			
			List<Map<String,Object>> list 	= adminService.SEL_STA_CSV_DOWN(param);
			//최상위 컬럼명 셋팅
			List<String[]> col	= new ArrayList<String[]>();
			col.add(new String[]{"번호", "앱명", "메시지", "총발송건", "성공", "수신", "읽음", "실패", "SMS", "발송일시"});
			
			CSVWriter cw		= null;
			JOutputStream out	= new JOutputStream();
			try{
				cw	= new CSVWriter(new OutputStreamWriter(out, "EUC-KR"),',','"');
				cw.writeAll(col);
				
				for(Map<String,Object> data:list){
					cw.writeNext(new String[]{
							String.valueOf(data.get("ROWNUM")),
							String.valueOf(data.get("APPNM")),
							String.valueOf(data.get("MSG")),
							String.valueOf(data.get("TOTCNT")),
							String.valueOf(data.get("SCNT")),
							String.valueOf(data.get("RCNT")),
							String.valueOf(data.get("RDCNT")),
							String.valueOf(data.get("FCNT")),
							String.valueOf(data.get("SMSCNT")),
							String.valueOf(data.get("SNDDAT"))
							});
				}
				
			}catch(Exception e){
				e.printStackTrace();
				logger.error(MakeUtil.stackTrace(e));
			}finally{
				try{if(cw!=null){cw.close();}}catch(Exception e){}
			}
			
			return csvDown(request,response,out,fileName);
		}catch(SQLException e){
			e.printStackTrace();
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			e.printStackTrace();
			logger.error(MakeUtil.stackTrace(e));
		}
		return null;
	}
	
	@RequestMapping(value="/GRP_USR_SYNC.do")
	public ModelAndView GRP_USR_SYNC(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException, SQLException, Exception {
		ModelAndView mv	= new ModelAndView();
		try{
			JRunTimeExec exec	= JRunTimeExec.getInstance();
			boolean yn	= exec.exec(new JProperties().getBatchUrl());
			mv.addObject("RESULT", (yn?"Y":"N"));
		}catch(Exception e){
			mv.addObject("RESULT", "동기화 오류 : " + e.getMessage());
			e.printStackTrace();
		}
		mv.setViewName("jsonView");
		
		return mv;
	}
	

	/**
	 * 서버관리 조회
	 * @param searchVO
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/SEL_SVR_001000.do")
	public String SEL_SVR_001000(@ModelAttribute("searchVO") SearchVO searchVO, HttpServletRequest request,HttpServletResponse response,Model model) {
		try{
        	//날짜 설정 1개월
        	String day01 = new SimpleDateFormat("yyyy-MM-01").format(new Date());
        	String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        	if("".equals(searchVO.getStDate()) && "".equals(searchVO.getEtDate())){
        		//searchVO.setStDate(day01);
        		searchVO.setEtDate(today);
        	}
        	
        	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        	searchVO.setPageSize(propertiesService.getInt("pageSize"));
        	
        	PaginationInfo paginationInfo = new PaginationInfo();
        	paginationInfo.setCurrentPageNo(searchVO.getPageNo());     
        	paginationInfo.setRecordCountPerPage(searchVO.getPageUnit()); 
        	paginationInfo.setPageSize(searchVO.getPageSize());
        	
        	searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        	searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        	searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        	
        	List<Map<String,Object>> list = adminService.SEL_SVR_001000(searchVO);
        	int totCnt = adminService.SEL_SVR_001000_CNT(searchVO);
        	
        	paginationInfo.setTotalRecordCount(totCnt);
        	model.addAttribute("stDate", searchVO.getStDate());
        	model.addAttribute("etDate", searchVO.getEtDate());
        	model.addAttribute("searchCondition", searchVO.getSearchCondition());
        	model.addAttribute("searchKeyword", searchVO.getSearchKeyword());
        	model.addAttribute("result", list);
        	model.addAttribute("paginationInfo", paginationInfo);
        	model.addAttribute("totCnt", totCnt);
        	model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("SVR"));
        	
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/mpg/svr/SEL_SVR_001000";
	}
	
	/**
	 * 서버관리 등록페이지 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/INS_SVR_001000.do")
	public String INS_SVR_001000(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("SVR"));
		
		return "/mpg/svr/INS_SVR_001000";
	}
	
	/**
	 * 서버관리 등록
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/INS_SVR_001001.do")
	public String INS_SVR_001001(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try {
			param.put("SVRMGR_NM",		request.getParameter("SVRMGR_NM"));
			param.put("SVRMGR_IP", 		request.getParameter("SVRMGR_IP"));
			param.put("SVRMGR_USEYN", 	request.getParameter("SVRMGR_USEYN"));
			
			param.put("CD",		"SVRMGR_NO");
			param.put("SQ",		"0");
			param.put("SVRMGR_NO",	String.valueOf(adminService.NEXT_VAL(param)));	//IP관리 채번
			
			//서버관리 등록
			adminService.INS_SVR_001001(param);
			
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "redirect:/SEL_SVR_001000.do";
	}
	
	/**
	 * 서버관리 상세
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/SEL_SVR_001001.do")
	public String SEL_SVR_001001(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("SVRMGR_NO", request.getParameter("SVRMGR_NO"));
			Map<String,Object> rs = adminService.SEL_SVR_001001(param);
			
			model.addAttribute("result", rs);
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("SVR"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/mpg/svr/SEL_SVR_001001";
	}
	
	/**
	 * 서버관리 수정페이지 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/UPT_SVR_001000.do")
	public String UPT_SVR_001000(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("SVRMGR_NO", request.getParameter("SVRMGR_NO"));
			Map<String,Object> rs = adminService.SEL_SVR_001001(param);
			
			String[] ip	= String.valueOf(rs.get("SVRMGRIP")).split("\\.");
			rs.put("SVRMGRIP1", ip[0]);
			rs.put("SVRMGRIP2", ip[1]);
			rs.put("SVRMGRIP3", ip[2]);
			rs.put("SVRMGRIP4", ip[3]);
			
			model.addAttribute("result", rs);
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("SVR"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/mpg/svr/UPT_SVR_001000";
	}
	
	/**
	 * 서버 수정
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/UPT_SVR_001001.do")
	public String UPT_SVR_001001(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try {
			param.put("SVRMGR_NO", 		request.getParameter("SVRMGR_NO")); 	//서버 일련번호
			param.put("SVRMGR_NM", 		request.getParameter("SVRMGR_NM")); 	//서버명
			param.put("SVRMGR_IP", 		request.getParameter("SVRMGR_IP"));		//등록IP
			param.put("SVRMGR_USEYN", 	request.getParameter("SVRMGR_USEYN"));	//사용여부
			
			//서버 수정
			adminService.UPT_SVR_001001(param);
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
		
    	return "redirect:/SEL_SVR_001000.do";
	}
	
	/**
	 * 서버삭제
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/DEL_SVR_001000.do")
	public String DEL_SVR_001000(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("SVRMGR_NO", request.getParameter("SVRMGR_NO"));
			
			adminService.DEL_SVR_001000(param);
			
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("SVR"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "redirect:/SEL_SVR_001000.do";
	}
	
	
	/**
	 * 관리자 조회
	 * @param searchVO
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/SEL_ADM_001000.do")
	public String SEL_ADM_001000(@ModelAttribute("searchVO") SearchVO searchVO, HttpServletRequest request,HttpServletResponse response,Model model) {
		try{
        	//날짜 설정 1개월
        	String day01 = new SimpleDateFormat("yyyy-MM-01").format(new Date());
        	String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        	if("".equals(searchVO.getStDate()) && "".equals(searchVO.getEtDate())){
        		//searchVO.setStDate(day01);
        		searchVO.setEtDate(today);
        	}
        	
        	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        	searchVO.setPageSize(propertiesService.getInt("pageSize"));
        	
        	PaginationInfo paginationInfo = new PaginationInfo();
        	paginationInfo.setCurrentPageNo(searchVO.getPageNo());     
        	paginationInfo.setRecordCountPerPage(searchVO.getPageUnit()); 
        	paginationInfo.setPageSize(searchVO.getPageSize());
        	
        	searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        	searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        	searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        	
        	List<Map<String,Object>> list = adminService.SEL_ADM_001000(searchVO);
        	int totCnt = adminService.SEL_ADM_001000_CNT(searchVO);
        	
        	paginationInfo.setTotalRecordCount(totCnt);
        	model.addAttribute("stDate", searchVO.getStDate());
        	model.addAttribute("etDate", searchVO.getEtDate());
        	model.addAttribute("searchCondition", searchVO.getSearchCondition());
        	model.addAttribute("searchKeyword", searchVO.getSearchKeyword());
        	model.addAttribute("result", list);
        	model.addAttribute("paginationInfo", paginationInfo);
        	model.addAttribute("totCnt", totCnt);
        	model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("ADM"));
        	
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/mpg/adm/SEL_ADM_001000";
	}
	
	/**
	 * 관리자 등록페이지 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/INS_ADM_001000.do")
	public String INS_ADM_001000(HttpServletRequest request, HttpServletResponse response, Model model) {
		LoginVO loginVO			= (LoginVO)request.getSession().getAttribute("loginVO");
		model.addAttribute("MGR_ID", loginVO.getAdminf_id());
		
		model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("ADM"));
		
		return "/mpg/adm/INS_ADM_001000";
	}
	
	/**
	 * 관리자 등록
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/INS_ADM_001001.do")
	public String INS_ADM_001001(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try {
			param.put("ADMINF_ID",		request.getParameter("ADMINF_ID"));
			param.put("ADMINF_PWD",		Crypto.encrypt(request.getParameter("ADMINF_PWD"), Controllable.CRYPT_KEY, "UTF-8"));
			param.put("ADMINF_NM", 		request.getParameter("ADMINF_NM"));
			param.put("ADMINF_DELYN", 	request.getParameter("ADMINF_DELYN"));
			param.put("MGR_ID", 		request.getParameter("MGR_ID"));
			
			//등록
			adminService.INS_ADM_001001(param);
			
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "redirect:/SEL_ADM_001000.do";
	}
	
	/**
	 * 관리자 상세
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/SEL_ADM_001001.do")
	public String SEL_ADM_001001(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("ADMINF_ID",		request.getParameter("ADMINF_ID"));
			Map<String,Object> rs = adminService.SEL_ADM_001001(param);
			
			model.addAttribute("result", rs);
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("ADM"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/mpg/adm/SEL_ADM_001001";
	}
	
	/**
	 * 관리자 수정페이지 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/UPT_ADM_001000.do")
	public String UPT_ADM_001000(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			LoginVO loginVO			= (LoginVO)request.getSession().getAttribute("loginVO");
			
			param.put("ADMINF_ID", request.getParameter("ADMINF_ID"));
			Map<String,Object> rs = adminService.SEL_ADM_001001(param);
			rs.put("ADMINFPWD", new String(Crypto.decryptBytes(String.valueOf(rs.get("ADMINFPWD")), Controllable.CRYPT_KEY, "UTF-8"),"UTF-8"));
			
			model.addAttribute("MGR_ID", loginVO.getAdminf_id());
			model.addAttribute("result", rs);
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("ADM"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "/mpg/adm/UPT_ADM_001000";
	}
	
	/**
	 * 관리자 수정
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/UPT_ADM_001001.do")
	public String UPT_ADM_001001(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try {
			param.put("ADMINF_ID", 		request.getParameter("ADMINF_ID"));
			param.put("ADMINF_PWD", 	Crypto.encrypt(request.getParameter("ADMINF_PWD"), Controllable.CRYPT_KEY, "UTF-8"));
			param.put("ADMINF_NM", 		request.getParameter("ADMINF_NM"));
			param.put("ADMINF_DELYN", 	request.getParameter("ADMINF_DELYN"));
			param.put("MGR_ID", 		request.getParameter("MGR_ID"));
			
			adminService.UPT_ADM_001001(param);
		}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
		
    	return "redirect:/SEL_ADM_001000.do";
	}
	
	/**
	 * 관리자삭제
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/DEL_ADM_001000.do")
	public String DEL_ADM_001000(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String,String> param	= new HashMap<String,String>();
		try{
			param.put("ADMINF_ID", request.getParameter("ADMINF_ID"));
			
			adminService.DEL_ADM_001000(param);
			
			model.addAttribute("MN_CODE", CONTENT_MENU_CODE.get("ADM"));
    	}catch(SQLException e){
			logger.error(MakeUtil.stackTrace(e));
		}catch(Exception e){
			logger.error(MakeUtil.stackTrace(e));
		}
    	return "redirect:/SEL_ADM_001000.do";
	}
}