package kcert.admin.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import kcert.admin.vo.LoginVO;
import kcert.admin.vo.SearchVO;
import kcert.framework.service.PushDaoSupport;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("AdminDAO")
@Transactional
public class AdminDAO extends PushDaoSupport {
	
	/**<PRE>
	 *  채번
	 *  sequence채번
	 * @param map
	 * @return
	 * @throws Exception
	 * </PRE>
	 */
	public synchronized long NEXT_VAL(Map<String,String> param) {
		selectByPk("admin_nextval", param, true);
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
		selectByPk("admin_currentval", param, true);
		return Long.parseLong(String.valueOf(param.get("SQ")));
	}
	
	/**
	 * 관리자 로그인 처리
	 * @param loginVO
	 * @return
	 * @throws SQLException
	 */
	public LoginVO SEL_ADM_LOGIN(LoginVO vo) throws SQLException {
		return (LoginVO)getSqlMapClientTemplate().queryForObject("SEL_AMDIN_LOGIN", vo);
	}

	/**<PRE>
	 * 긴급공지사항 조회
	 * @param map
	 * @return
	 * @throws SQLException
	 * </PRE>
	 */
	public List<Map<String, Object>> SEL_BRD_001000(SearchVO searchVO) throws SQLException {
		return list("SEL_BRD_001000", searchVO, true);
	}

	/**
	 * 긴급공지사항 개수 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public int SEL_BRD_001000_CNT(SearchVO searchVO) throws SQLException {
		return (Integer)getSqlMapClientTemplate().queryForObject("SEL_BRD_001000_CNT", searchVO);
	}
	
	/**
	 * 긴급공지사항 등록
	 * @param param
	 * @return
	 */
	public int INS_BRD_001001(Map<String, String> param) throws SQLException {
		return update("INS_BRD_001001", param);
	}
	
	/**
	 * 팝업순서가 존재하는 목록 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_BRD_001009(Map<String, String> param) throws SQLException {
		return list("SEL_BRD_001009", param, true);
	}
	
	/**
	 * 기존 팝업 순서 변경
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public int UPT_BRD_001009(Map<String, String> param) throws SQLException {
		return update("UPT_BRD_001009", param);
	}
	
	/**
	 * 긴급공지사항 상세보기
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> SEL_BRD_001001(Map<String, String> param) {
		return (Map<String, Object>)selectByPk("SEL_BRD_001001", param, true);
	}
	
	/**
	 * 긴급공지사항 삭제
	 * @param param
	 * @return
	 */
	public int DEL_BRD_001000(Map<String, String> param) {
		return delete("DEL_BRD_001000", param);
	}
	
	/**
	 * 긴급공지사항 수정
	 * @param param
	 * @return
	 */
	public int UPT_BRD_001001(Map<String, String> param) {
		return update("UPT_BRD_001001", param);
	}
	
	/**
	 * 관리자 비밀번호 변경
	 * @param loginVO
	 * @return
	 */
	public int UPT_ADM_001001(LoginVO loginVO) {
		return update("UPT_ADM_001001", loginVO);
	}
	
	/**
	 * IP관리 리스트 조회
	 * @param searchVO
	 * @return
	 */
	public List<Map<String, Object>> SEL_IPM_001000(SearchVO searchVO) {
		return list("SEL_IPM_001000", searchVO, true);
	}
	
	/**
	 * IP관리 리스트 갯수 조회
	 * @param searchVO
	 * @return
	 */
	public int SEL_IPM_001000_CNT(SearchVO searchVO) {
		return (Integer)getSqlMapClientTemplate().queryForObject("SEL_IPM_001000_CNT", searchVO);
	}
	
	/**
	 * IP 등록
	 * @param param
	 * @return
	 */
	public int INS_IPM_001001(Map<String, String> param) {
		return update("INS_IPM_001001", param);
	}
	
	/**
	 * IP정보 조회
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> SEL_IPM_001001(Map<String, String> param) {
		return (Map<String, Object>)selectByPk("SEL_IPM_001001", param, true);
	}
	
	/**
	 * IP삭제
	 * @param param
	 * @return
	 */
	public int DEL_IPM_001000(Map<String, String> param) {
		return delete("DEL_IPM_001000", param);
	}
	
	/**
	 * IP수정
	 * @param param
	 * @return
	 */
	public int UPT_IPM_001001(Map<String, String> param) {
		return update("UPT_IPM_001001", param);
	}
	
	/**
	 * 허용 아이피 조회
	 * @return
	 */
	public List<Map<String, Object>> SEL_IPM_S001009() {
		return list("SEL_IPM_S001009", null, true);
	}
	
	/**
	 * 앱정보 리스트 조회
	 * @param searchVO
	 * @return
	 */
	public List<Map<String, Object>> SEL_APP_001000(SearchVO searchVO) {
		return list("SEL_APP_001000", searchVO, true);
	}
	
	/**
	 * 앱정보 리스트 갯수 조회
	 * @param searchVO
	 * @return
	 */
	public int SEL_APP_001000_CNT(SearchVO searchVO) {
		return (Integer)getSqlMapClientTemplate().queryForObject("SEL_APP_001000_CNT", searchVO);
	}
	
	/**
	 * 앱정보 등록
	 * @param param
	 * @return
	 */
	public int INS_APP_001001(Map<String, String> param) {
		return update("INS_APP_001001", param);
	}
	
	/**
	 * 앱정보 상세 조회
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> SEL_APP_001001(Map<String, String> param) {
		return (Map<String, Object>)selectByPk("SEL_APP_001001", param, true);
	}
	
	/**
	 * 앱정보 수정
	 * @param param
	 * @return
	 */
	public int UPT_APP_001001(Map<String, String> param) {
		return update("UPT_APP_001001", param);
	}
	
	/**
	 * 앱정보 삭제
	 * @param param
	 * @return
	 */
	public int DEL_APP_001000(Map<String, String> param) {
		return delete("DEL_APP_001000", param);
	}
	
	/**
	 * My 그룹 리스트 조회
	 * @param searchVO
	 * @return
	 */
	public List<Map<String, Object>> SEL_GRP_001000(SearchVO searchVO) {
		return list("SEL_GRP_001000", searchVO, true);
	}
	
	/**
	 * My 그룹 리스트 갯수 조회
	 * @param searchVO
	 * @return
	 */
	public int SEL_GRP_001000_CNT(SearchVO searchVO) {
		return (Integer)getSqlMapClientTemplate().queryForObject("SEL_GRP_001000_CNT", searchVO);
	}
	
	/**
	 * 지역정보 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> SEL_ORG_001000(Map<String, String> param) {
		return list("SEL_ORG_001000", param, true);
	}
	
	/**
	 * 지사정보 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> SEL_ORG_001001(Map<String, String> param) {
		return list("SEL_ORG_001001", param, true);
	}
	
	/**
	 * 부서정보 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> SEL_ORG_001002(Map<String, String> param) {
		return list("SEL_ORG_001002", param, true);
	}
	
	/**
	 * 사용자정보 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> SEL_ORG_001003(Map<String, String> param) {
		return list("SEL_ORG_001003", param, true);
	}
	
	/**
	 * 사용자 정보 조회(지역선택, 지사선택)
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> SEL_ORG_001004(Map<String, String> param) {
		return list("SEL_ORG_001004", param, true);
	}
	
	/**
	 * My Group 정보 등록
	 * @param param
	 * @return
	 */
	public int INS_GRP_001001(Map<String, String> param) {
		return update("INS_GRP_001001", param);
	}
	
	/**
	 * 사용자 정보 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> SEL_GRP_001009(Map<String, String> param) {
		return list("SEL_GRP_001009", param, true);
	}
	
	/**
	 * My Group 사용자 등록
	 * @param param
	 * @return
	 */
	public int INS_GRP_001009(Map<String, String> param) {
		return update("INS_GRP_001009", param);
	}
	
	/**
	 * My Group 상세보기
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> SEL_GRP_001001(Map<String, String> param) {
		return list("SEL_GRP_001001", param, true);
	}
	
	/**
	 * My Group 삭제
	 * @param param
	 * @return
	 */
	public int DEL_GRP_001000(Map<String, String> param) {
		return delete("DEL_GRP_001000", param);
	}
	
	/**
	 * My Group 수정 
	 * @param param
	 * @return
	 */
	public int UPT_GRP_001001(Map<String, String> param) {
		return update("UPT_GRP_001001", param);
	}
	
	/**
	 * My Group 사용자 삭제
	 * @param param
	 * @return
	 */
	public int UPT_GRP_001009(Map<String, String> param) {
		return delete("UPT_GRP_001009", param);
	}
	
	/**
	 * SMS, Push 사용자 수 조회 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> SEL_MSG_S01000() {
		return (Map<String, Object>)selectByPk("SEL_MSG_S01000", null, true);
	}
	
	/**
	 * My Group 조회
	 * @return
	 */
	public List<Map<String, Object>> SEL_MSG_S01001() {
		return list("SEL_MSG_S01001", null, true);
	}
	
	/**
	 * 앱 정보 조회
	 * @return
	 */
	public List<Map<String, Object>> SEL_MSG_S01011() {
		return list("SEL_MSG_S01011", null, true);
	}
	
	/**
	 * 사용자 토큰 조회
	 * @return
	 */
	public List<Map<String, Object>> SEL_ADM_TKN_S0001(Map<String, String> param) {
		return list("SEL_ADM_TKN_S0001", param, true);
	}
	
	/**
	 * 예약전송 사용자 추가
	 * @param param
	 * @return
	 */
	public int INS_RSV_001000(Map<String, String> param) {
		return update("INS_RSV_001000", param);
	}
	
	/**
	 * 예약전송 건 확인 조회
	 * @return
	 */
	public List<Map<String, Object>> SEL_MSG_RSV_001(Map<String, String> param) {
		return list("SEL_MSG_RSV_001", param, true);
	}
	
	/**
	 * 메시지 발송여부 변경
	 * @param param
	 * @return
	 */
	public int UPT_MSG_001000(Map<String, String> param) {
		return update("UPT_MSG_001000", param);
	}
	
	/**
	 * 통계 조회
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> SEL_STA_001009(SearchVO searchVO) {
		return (Map<String, String>)selectByPk("SEL_STA_001009", searchVO, true);
	}
	
	/**
	 * 통계 리스트 조회
	 * @param searchVO
	 * @return
	 */
	public List<Map<String, Object>> SEL_STA_001000(SearchVO searchVO) {
		return list("SEL_STA_001000", searchVO, true);
	}
	
	/**
	 * 통계 리스트 갯수 조회
	 * @param searchVO
	 * @return
	 */
	public int SEL_STA_001000_CNT(SearchVO searchVO) {
		return (Integer)getSqlMapClientTemplate().queryForObject("SEL_STA_001000_CNT", searchVO);
	}
	
	/**
	 * 전송결과 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> SEL_STA_001001(Map<String, String> param) {
		return list("SEL_STA_001001", param, true);
	}
	
	/**
	 * 통계 CSV다운로드 데이터 조회
	 * @param searchVO
	 * @return
	 */
	public List<Map<String, Object>> SEL_STA_CSV_DOWN(Map<String,String> param) {
		return list("SEL_STA_CSV_DOWN", param, true);
	}
	
	/**
	 * fcm정보조회
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> SEL_STA_001002(Map<String, String> param) {
		return (Map<String, String>)selectByPk("SEL_STA_001002", param, true);
	}
	
	/**
	 * 전송실패 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> SEL_STA_001003(Map<String, String> param) {
		return list("SEL_STA_001003", param, true);
	}
	
	/**
	 * 전송이력 수정
	 * @param param
	 * @return
	 */
	public int UPT_SND_001001(Map<String, String> param) {
		return update("UPT_SND_001001", param);
	}
	
	/**
	 * 서버관리 리스트 조회
	 * @param searchVO
	 * @return
	 */
	public List<Map<String, Object>> SEL_SVR_001000(SearchVO searchVO) {
		return list("SEL_SVR_001000", searchVO, true);
	}
	
	/**
	 * 서버관리 리스트 갯수 조회
	 * @param searchVO
	 * @return
	 */
	public int SEL_SVR_001000_CNT(SearchVO searchVO) {
		return (Integer)getSqlMapClientTemplate().queryForObject("SEL_SVR_001000_CNT", searchVO);
	}
	
	/**
	 * 서버 등록
	 * @param param
	 * @return
	 */
	public int INS_SVR_001001(Map<String, String> param) {
		return update("INS_SVR_001001", param);
	}
	
	/**
	 * 서버정보 조회
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> SEL_SVR_001001(Map<String, String> param) {
		return (Map<String, Object>)selectByPk("SEL_SVR_001001", param, true);
	}
	
	/**
	 * 서버삭제
	 * @param param
	 * @return
	 */
	public int DEL_SVR_001000(Map<String, String> param) {
		return delete("DEL_SVR_001000", param);
	}
	
	/**
	 * 서버수정
	 * @param param
	 * @return
	 */
	public int UPT_SVR_001001(Map<String, String> param) {
		return update("UPT_SVR_001001", param);
	}
	
	/**
	 * 허용 서버 조회
	 * @return
	 */
	public List<Map<String, Object>> SEL_SVR_S001009() {
		return list("SEL_SVR_S001009", null, true);
	}
	
	/**
	 * 푸시 발송 성공 건 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public int SEL_STA_001009_01(SearchVO searchVO) {
		return (Integer)getSqlMapClientTemplate().queryForObject("SEL_STA_001009_01", searchVO);
	}
	
	/**
	 * 푸시 발송 실패 건 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public int SEL_STA_001009_02(SearchVO searchVO) {
		return (Integer)getSqlMapClientTemplate().queryForObject("SEL_STA_001009_02", searchVO);
	}
	
	/**
	 * SMS 발송 건 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public int SEL_STA_001009_03(SearchVO searchVO) {
		return (Integer)getSqlMapClientTemplate().queryForObject("SEL_STA_001009_03", searchVO);
	}
	
	/**
	 * 수신 성공 건 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public int SEL_STA_001009_04(SearchVO searchVO) {
		return (Integer)getSqlMapClientTemplate().queryForObject("SEL_STA_001009_04", searchVO);
	}
	
	/**
	 * 읽음 성공 건 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public int SEL_STA_001009_05(SearchVO searchVO) {
		return (Integer)getSqlMapClientTemplate().queryForObject("SEL_STA_001009_05", searchVO);
	}
	
	/**
	 * 관리자 조회
	 * @param searchVO
	 * @return
	 */
	public List<Map<String, Object>> SEL_ADM_001000(SearchVO searchVO) {
		return list("SEL_ADM_001000", searchVO, true);
	}
	
	/**
	 * 관리자 갯수 조회
	 * @param searchVO
	 * @return
	 */
	public int SEL_ADM_001000_CNT(SearchVO searchVO) {
		return (Integer)getSqlMapClientTemplate().queryForObject("SEL_ADM_001000_CNT", searchVO);
	}
	
	/**
	 * 관리자 등록
	 * @param param
	 * @return
	 */
	public int INS_ADM_001001(Map<String, String> param) {
		return update("INS_ADM_001001", param);
	}
	
	/**
	 * 관리자 상세 조회
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> SEL_ADM_001001(Map<String, String> param) {
		return (Map<String, Object>)selectByPk("SEL_ADM_001001", param, true);
	}
	
	/**
	 * 관리자 수정
	 * @param param
	 * @return
	 */
	public int UPT_ADM_001001(Map<String, String> param) {
		return update("UPT_ADM_001001", param);
	}
	
	/**
	 * 관리자 삭제
	 * @param param
	 * @return
	 */
	public int DEL_ADM_001000(Map<String, String> param) {
		return delete("DEL_ADM_001000", param);
	}
}
