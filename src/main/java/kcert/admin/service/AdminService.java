package kcert.admin.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import kcert.admin.vo.SearchVO;
import kcert.admin.vo.LoginVO;

public interface AdminService {
	
	/**
	 * 채번
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public long NEXT_VAL (Map<String,String> param) throws SQLException;
	
	/**
	 * 로그인 처리
	 * @param loginVO
	 * @return
	 * @throws Exception
	 */
	public LoginVO SEL_AMDIN_LOGIN(LoginVO loginVO) throws SQLException;
	/**
	 * 긴급공지사항 조회
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> SEL_BRD_001000 (SearchVO searchVO) throws SQLException;

	/**
	 * 긴급공지사항 수 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	public int SEL_BRD_001000_CNT(SearchVO searchVO) throws SQLException;

	/**
	 * 긴급공지사항 등록
	 * @param param
	 * @throws SQLException
	 */
	public int INS_BRD_001001(Map<String, String> param) throws SQLException;

	/**
	 * 팝업순서가 존재하는 목록 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_BRD_001009(Map<String, String> param) throws SQLException;

	/**
	 * 기존팝업 순서 변경
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public int UPT_BRD_001009(Map<String, String> param) throws SQLException;

	/**
	 * 긴급공지사항 상세보기
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> SEL_BRD_001001(Map<String, String> param) throws SQLException;

	/**
	 * 긴급공지사항 삭제
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public int DEL_BRD_001000(Map<String, String> param) throws SQLException;

	/**
	 * 긴급공지사항 수정
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public int UPT_BRD_001001(Map<String, String> param) throws SQLException;

	/**
	 * 관리자 비밀번호 변경
	 * @param loginVO
	 * @return
	 * @throws SQLException
	 */
	public int UPT_ADM_001001(LoginVO loginVO) throws SQLException;

	/**
	 * IP관리 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_IPM_001000(SearchVO searchVO) throws SQLException;

	/**
	 * IP관리 리스트 갯수 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public int SEL_IPM_001000_CNT(SearchVO searchVO) throws SQLException;

	/**
	 * IP 등록
	 * @param param
	 * @throws SQLException
	 */
	public int INS_IPM_001001(Map<String, String> param) throws SQLException;

	/**
	 * IP 정보 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> SEL_IPM_001001(Map<String, String> param) throws SQLException;

	/**
	 * IP삭제
	 * @param param
	 * @throws SQLException
	 */
	public int DEL_IPM_001000(Map<String, String> param) throws SQLException;

	/**
	 *  IP 수정
	 * @param param
	 * @throws SQLException
	 */
	public int UPT_IPM_001001(Map<String, String> param) throws SQLException;

	/**
	 * 허용 IP조회
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_IPM_S001009() throws SQLException;

	/**
	 * 앱정보 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_APP_001000(SearchVO searchVO) throws SQLException;

	/**
	 * 앱정보 리스트 갯수 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public int SEL_APP_001000_CNT(SearchVO searchVO) throws SQLException;

	/**
	 * 앱정보 등록
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public int INS_APP_001001(Map<String, String> param) throws SQLException;

	/**
	 * 앱정보 상세조회
	 * @param param
	 * @return
	 */
	public Map<String, Object> SEL_APP_001001(Map<String, String> param) throws SQLException;

	/**
	 * 앱정보 수정
	 * @param param
	 * @return
	 */
	public int UPT_APP_001001(Map<String, String> param) throws SQLException;

	/**
	 * 앱정보 삭제
	 * @param param
	 * @return
	 */
	public int DEL_APP_001000(Map<String, String> param) throws SQLException;

	/**
	 * My 그룹 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_GRP_001000(SearchVO searchVO) throws SQLException;

	/**
	 * My 그룹 리스트 갯수 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public int SEL_GRP_001000_CNT(SearchVO searchVO) throws SQLException;

	/**
	 * 지역정보 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_ORG_001000(Map<String, String> param) throws SQLException;

	/**
	 * 지사정보 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> SEL_ORG_001001(Map<String, String> param) throws SQLException;

	/**
	 * 부서정보 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_ORG_001002(Map<String, String> param) throws SQLException;

	/**
	 * 사용자정보 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_ORG_001003(Map<String, String> param) throws SQLException;

	/**
	 * 사용자 정보 조회(지역선택, 지사선택)
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_ORG_001004(Map<String, String> param) throws SQLException;
	
	/**
	 * My Group 정보 등록
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public int INS_GRP_001001(Map<String, String> param) throws SQLException;

	/**
	 * 사용자정보 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_GRP_001009(Map<String, String> param) throws SQLException;

	/**
	 * My Group 사용자 등록
	 * @param param
	 * @throws SQLException
	 */
	public int INS_GRP_001009(Map<String, String> param) throws SQLException;

	/**
	 * My Group 상세보기
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_GRP_001001(Map<String, String> param) throws SQLException;

	/**
	 * My Group 삭제
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public int DEL_GRP_001000(Map<String, String> param) throws SQLException;

	/**
	 * My Group 수정
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public int UPT_GRP_001001(Map<String, String> param) throws SQLException;

	/**
	 * SMS, Push 사용자 수 조회 
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> SEL_MSG_S01000() throws SQLException;

	/**
	 * My Group 조회
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_MSG_S01001() throws SQLException;

	/**
	 * 앱 정보 조회
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_MSG_S01011() throws SQLException;

	/**
	 * 사용자 토큰 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_ADM_TKN_S0001(Map<String, String> param) throws SQLException;

	/**
	 * 예약전송 사용자 추가
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public int INS_RSV_001000(Map<String, String> param) throws SQLException;

	/**
	 * 예약전송 건 확인 조회
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_MSG_RSV_001(Map<String, String> param) throws SQLException;

	/**
	 * 메시지 발송여부 변경
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public int UPT_MSG_001000(Map<String, String> param) throws SQLException;

	/**
	 * 통계 조회
	 * @return
	 * @throws SQLException
	 */
	public Map<String, String> SEL_STA_001009(SearchVO searchVO) throws SQLException;

	/**
	 * 통계 리스트 조회
	 * @param searchVO
	 * @return
	 */
	public List<Map<String, Object>> SEL_STA_001000(SearchVO searchVO) throws SQLException;

	/**
	 * 통계 리스트 갯수 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public int SEL_STA_001000_CNT(SearchVO searchVO) throws SQLException;

	/**
	 * 통계 CSV다운로드 데이터 조회
	 * @param searchVO
	 * @return
	 */
	public List<Map<String, Object>> SEL_STA_CSV_DOWN(Map<String,String> param) throws SQLException;

	/**
	 * 전송결과 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_STA_001001(Map<String, String> param) throws SQLException;

	/**
	 * fcm정보조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Map<String, String> SEL_STA_001002(Map<String, String> param) throws SQLException;

	/**
	 * 전송실패 목록 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_STA_001003(Map<String, String> param) throws SQLException;

	/**
	 * 전송이력 수정
	 * @param param
	 * @return
	 */
	public int UPT_SND_001001(Map<String, String> param) throws SQLException;

	/**
	 * 서버관리 리스트 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_SVR_001000(SearchVO searchVO) throws SQLException;

	/**
	 * 서버관리 리스트 갯수 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public int SEL_SVR_001000_CNT(SearchVO searchVO) throws SQLException;

	/**
	 * 서버 등록
	 * @param param
	 * @throws SQLException
	 */
	public int INS_SVR_001001(Map<String, String> param) throws SQLException;

	/**
	 * 서버 정보 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> SEL_SVR_001001(Map<String, String> param) throws SQLException;

	/**
	 * 서버삭제
	 * @param param
	 * @throws SQLException
	 */
	public int DEL_SVR_001000(Map<String, String> param) throws SQLException;

	/**
	 *  서버 수정
	 * @param param
	 * @throws SQLException
	 */
	public int UPT_SVR_001001(Map<String, String> param) throws SQLException;

	/**
	 * 허용 서버조회
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_SVR_S001009() throws SQLException;

	/**
	 * 푸시 발송 성공 건 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public int SEL_STA_001009_01(SearchVO searchVO) throws SQLException;
	
	/**
	 * 푸시 발송 실패 건 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public int SEL_STA_001009_02(SearchVO searchVO) throws SQLException;
	
	/**
	 * SMS 발송 건 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public int SEL_STA_001009_03(SearchVO searchVO) throws SQLException;
	
	/**
	 * 수신 성공 건 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public int SEL_STA_001009_04(SearchVO searchVO) throws SQLException;
	
	/**
	 * 읽음 성공 건 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public int SEL_STA_001009_05(SearchVO searchVO) throws SQLException;

	/**
	 * 관리자 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> SEL_ADM_001000(SearchVO searchVO) throws SQLException;

	/**
	 * 관리자 갯수 조회
	 * @param searchVO
	 * @return
	 */
	public int SEL_ADM_001000_CNT(SearchVO searchVO) throws SQLException;

	/**
	 * 관리자 등록
	 * @param param
	 */
	public int INS_ADM_001001(Map<String, String> param) throws SQLException;

	/**
	 * 관리자 상세 조회
	 * @param param
	 * @return
	 */
	public Map<String, Object> SEL_ADM_001001(Map<String, String> param) throws SQLException;

	/**
	 * 관리자 수정
	 * @param param
	 * @throws SQLException
	 */
	public int UPT_ADM_001001(Map<String, String> param) throws SQLException;

	/**
	 * 관리자 삭제
	 * @param param
	 * @throws SQLException
	 */
	public int DEL_ADM_001000(Map<String, String> param) throws SQLException;
}
