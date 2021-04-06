package kcert.client.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import kcert.framework.service.PushDaoSupport;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository("PushClientDAO")
@Transactional
public class PushClientDAO extends PushDaoSupport{
	/**<PRE>
	 *  채번
	 *  sequence채번
	 * @param map
	 * @return
	 * @throws Exception
	 * </PRE>
	 */
	public synchronized long NEXT_VAL(Map<String,String> param) {
		selectByPk("client_nextval", param, true);
		return Long.parseLong(String.valueOf(param.get("SQ")));
	}
	/**<PRE>
	 *  현재 sequence 조회
	 * @param map
	 * @return
	 * @throws Exception
	 * </PRE>
	 */
	public synchronized long CURR_VAL(Map<String,String> param) {
		selectByPk("client_currentval", param,true);
		return Long.parseLong(String.valueOf(param.get("SQ")));
	}
	
	/**<PRE>
	 *  로그저장
	 * @param map
	 * @return
	 * @throws Exception
	 * </PRE>
	 */
	public int INS_CLA_LOG_I00000(Map<String,String> param) {
		return update("INS_CLA_LOG_I00000", param);
	}
	
	/**
	 * 앱 일련번호 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public String SEL_CLA_APP_SEQ(Map<String, String> param) {
		return (String) selectByPk("SEL_CLA_APP_SEQ", param, true);
	}
	
	/**
	 * 앱 별 토큰 아이디 저장
	 * @param param
	 */
	public int INS_CLA_TKN_SND(Map<String, String> param) {
		return update("INS_CLA_TKN_SND", param);
	}
	
	/**
	 * 앱 서버키 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public String SEL_CLA_APP_KEY(Map<String, String> param) {
		return (String) selectByPk("SEL_CLA_APP_KEY", param, true);
	}
	
	/**
	 * 전체사용자 토큰아이디 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_CLA_TKN_S0001(Map<String, String> param) {
		return list("SEL_CLA_TKN_S0001", param , true);
	}
	
	/**
	 * 푸시 발송 이력 저장
	 * @param param
	 * @throws SQLException
	 */
	public int INS_CLA_FCM_SND(Map<String, String> param) {
		return update("INS_CLA_FCM_SND", param);
	}
	
	/**
	 * 푸시 발송 응답 이력 저장
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public int INS_CLA_SND_RES(Map<String, String> param) {
		return update("INS_CLA_SND_RES", param);
	}
	
	/**
	 * SMS 사용자 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_CLA_SMS_S0001(Map<String, String> param) {
		return list("SEL_CLA_SMS_S0001", param , true);
	}
	
	/**
	 * 긴급공지사항 조회(앱)
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> SEL_BRD_A01001(Map<String, Object> param) {
		return list("SEL_BRD_A01001", param , true);
	}
	
	/**
	 * 긴급공지사항 상세조회(앱)
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> SEL_BRD_A01002(Map<String, String> param) {
		return (Map<String,String>) selectByPk("SEL_BRD_A01002", param,true);
	}
	
	/**
	 * 긴급공지사항 팝업안내 조회(앱)
	 * @return
	 */
	public List<Map<String, Object>> SEL_BRD_P01000() {
		return list("SEL_BRD_P01000", null , true);
	}
	
	/**
	 * 수신여부 변경
	 * @param param
	 * @return
	 */
	public int UPT_BRD_U01001(Map<String, String> param) {
		return update("UPT_BRD_U01001", param);
	}
	
	/**
	 * 푸시 수신이력 변경
	 * @param param
	 * @return
	 */
	public int UPT_PSH_U01001(Map<String, String> param) {
		return update("UPT_PSH_U01001", param);
	}
	
	/**
	 * 테스트 토큰 아이디 조회
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> SEL_CLA_TKNID_TEST(Map<String, String> param) {
		return (Map<String,String>) selectByPk("SEL_CLA_TKNID_TEST", param, true);
	}
}