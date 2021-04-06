package kcert.client.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kcert.admin.dao.SmsDAO;
import kcert.client.dao.PushClientDAO;
import kcert.client.service.PushClientService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("PushClientService")
@Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
public class PushClientServiceImpl extends AbstractServiceImpl implements PushClientService {

	
	/** pushClientDAO */
    @Resource(name="PushClientDAO")
	private PushClientDAO pushClientDAO; //데이터베이스 접근 클래스
    
    /** SmsDAO */
    @Resource(name="SmsDAO")
	private SmsDAO smsDAO;
    
	/**<PRE>
	 *	로그저장
	 * @param map
	 * @return
	 * @throws Exception
	 * </PRE>
	 */
	@Override
	public int INS_CLA_LOG_I00000(Map<String, String> param) throws Exception {
		HashMap<String,String> param2	= new HashMap<String,String>();
		param2.put("CD"		, "LOG_NO");
		param2.put("SQ"		, "0");
		param.put("LOGNO"	, String.valueOf(NEXT_VAL(param2)));	//영수증번호 채번
		
		return pushClientDAO.INS_CLA_LOG_I00000(param);
	}
	
	/**<PRE>
	 *  채번
	 * @param map
	 * @return
	 * @throws Exception
	 * </PRE>
	 */
	@Override
	public synchronized long NEXT_VAL (Map<String,String> param) throws SQLException{
		return pushClientDAO.NEXT_VAL(param);
	}

	/**
	 * 앱 별 토큰 아이디 저장
	 * @param param
	 */
	@Override
	public int INS_CLA_TKN_SND(Map<String, String> param) throws SQLException {
		return pushClientDAO.INS_CLA_TKN_SND(param);
	}

	/**
	 * 앱 일련번호 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String SEL_CLA_APP_SEQ(Map<String, String> param) throws SQLException {
		return pushClientDAO.SEL_CLA_APP_SEQ(param);
	}

	/**
	 * 앱 서버키 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String SEL_CLA_APP_KEY(Map<String, String> param) throws SQLException {
		return pushClientDAO.SEL_CLA_APP_KEY(param);
	}

	/**
	 *안드로이드 사용자 토큰아이디 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> SEL_CLA_TKN_S0001(Map<String, String> param) throws SQLException {
		return pushClientDAO.SEL_CLA_TKN_S0001(param);
	}

	/**
	 * 푸시 발송 이력 저장
	 * @param param
	 * @throws SQLException
	 */
	@Override
	public int INS_CLA_FCM_SND(Map<String, String> param) throws SQLException {
		return pushClientDAO.INS_CLA_FCM_SND(param);
	}

	/**
	 * 응답 이력 저장
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int INS_CLA_SND_RES(Map<String, String> param) throws SQLException {
		return pushClientDAO.INS_CLA_SND_RES(param);
	}
	
	/**
	 * SMS 사용자 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> SEL_CLA_SMS_S0001(Map<String, String> param) throws SQLException {
		return pushClientDAO.SEL_CLA_SMS_S0001(param);
	}

	/**
	 * SMS 전송
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int INS_SMS_001000(Map<String, String> param) throws SQLException {
		return smsDAO.INS_SMS_001000(param);
	}

	/**
	 * 긴급공지사항 조회(앱)
	 */
	@Override
	public List<Map<String, Object>> SEL_BRD_A01001(Map<String, Object> param) throws SQLException {
		return pushClientDAO.SEL_BRD_A01001(param);
	}

	/**
	 * 긴급공지사항 상세조회(앱)
	 */
	@Override
	public Map<String, String> SEL_BRD_A01002(Map<String, String> param) throws SQLException {
		return pushClientDAO.SEL_BRD_A01002(param);
	}

	/**
	 * 긴급공지사항 팝업안내 조회(앱)
	 */
	@Override
	public List<Map<String, Object>> SEL_BRD_P01000() throws SQLException {
		return pushClientDAO.SEL_BRD_P01000();
	}

	/**
	 * 수신여부 변경
	 */
	@Override
	public int UPT_BRD_U01001(Map<String, String> param) throws SQLException {
		return pushClientDAO.UPT_BRD_U01001(param);
	}

	/**
	 * 푸시 수신 이력 변경
	 */
	@Override
	public int UPT_PSH_U01001(Map<String, String> param) throws SQLException {
		return pushClientDAO.UPT_PSH_U01001(param);
	}

	/**
	 * 테스트 토큰 아이디 조회
	 */
	@Override
	public Map<String,String> SEL_CLA_TKNID_TEST(Map<String, String> param) throws SQLException {
		return pushClientDAO.SEL_CLA_TKNID_TEST(param);
	}
}
