package kcert.client.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PushClientService {
	
	/**<PRE>
	 *	로그저장
	 * @param param
	 * @return
	 * @throws Exception
	 * </PRE>
	 */
	public int INS_CLA_LOG_I00000(Map<String,String> param) throws Exception;
	
	/**<PRE>
	 *  채번
	 * @param param
	 * @return
	 * @throws Exception
	 * </PRE>
	 */
	public long NEXT_VAL (Map<String,String> param) throws SQLException;
	
	/**
	 * 앱 별 토큰 아이디 저장
	 * @param param
	 */
	public int INS_CLA_TKN_SND(Map<String, String> param) throws SQLException;

	/**
	 * 앱 일련번호 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public String SEL_CLA_APP_SEQ(Map<String, String> param) throws SQLException;

	/**
	 * 앱 서버키 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public String SEL_CLA_APP_KEY(Map<String, String> param) throws SQLException;

	/**
	 * 안드로이드 사용자 토큰아이디 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_CLA_TKN_S0001(Map<String, String> param) throws SQLException;

	/**
	 * 푸시 발송 이력 저장
	 * @param param
	 * @throws SQLException
	 */
	public int INS_CLA_FCM_SND(Map<String, String> param) throws SQLException;

	/**
	 * 응답 이력 저장
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public int INS_CLA_SND_RES(Map<String, String> param) throws SQLException;
	
	/**
	 * SMS 사용자 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_CLA_SMS_S0001(Map<String, String> param) throws SQLException;

	/**
	 * SMS 전송
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public int INS_SMS_001000(Map<String, String> param) throws SQLException;

	/**
	 * 긴급공지사항 조회(앱)
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_BRD_A01001(Map<String, Object> param) throws SQLException;

	/**
	 * 긴급공지사항 상세조회(앱)
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Map<String, String> SEL_BRD_A01002(Map<String, String> param) throws SQLException;

	/**
	 * 긴급공지사항 팝업안내 조회(앱)
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_BRD_P01000() throws SQLException;

	/**
	 * 수신여부 변경
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public int UPT_BRD_U01001(Map<String, String> param) throws SQLException;

	/**
	 * push 수신 이력 변경
	 * @param param
	 * @throws SQLException
	 */
	public int UPT_PSH_U01001(Map<String, String> param) throws SQLException;

	/**
	 * 테스트 토큰 아이디 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Map<String,String> SEL_CLA_TKNID_TEST(Map<String, String> param) throws SQLException;
}
