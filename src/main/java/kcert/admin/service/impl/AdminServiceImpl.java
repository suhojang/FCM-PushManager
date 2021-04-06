package kcert.admin.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kcert.admin.dao.AdminDAO;
import kcert.admin.dao.SmsDAO;
import kcert.admin.service.AdminService;
import kcert.admin.vo.LoginVO;
import kcert.admin.vo.SearchVO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("AdminService")
@Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
public class AdminServiceImpl extends AbstractServiceImpl implements AdminService{

	
	/** adminDAO */
    @Resource(name="AdminDAO")
	private AdminDAO adminDAO;
    
    /** SmsDAO */
    @Resource(name="SmsDAO")
	private SmsDAO smsDAO;

    /**<PRE>
	 *  채번
	 * @param map
	 * @return
	 * @throws Exception
	 * </PRE>
	 */
	@Override
	public synchronized long NEXT_VAL (Map<String,String> param) throws SQLException {
		return adminDAO.NEXT_VAL(param);
	}
	
    /**
     * 로그인 처리
     */
    @Override
    public LoginVO SEL_AMDIN_LOGIN(LoginVO loginVO) throws SQLException {
    	return adminDAO.SEL_ADM_LOGIN(loginVO);
    }
    /**
     * 긴급공지사항 조회
     */
	@Override
	public List<Map<String, Object>> SEL_BRD_001000(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_BRD_001000(searchVO);
	}

	/**
	 * 긴급공지사항 개수 조회
	 */
	@Override
	public int SEL_BRD_001000_CNT(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_BRD_001000_CNT(searchVO);
	}

	/**
	 * 긴급공지사항 등록
	 */
	@Override
	public int INS_BRD_001001(Map<String, String> param) throws SQLException {
		return adminDAO.INS_BRD_001001(param);
	}

	/**
	 * 팝업순서가 존재하는 목록 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_BRD_001009(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_BRD_001009(param);
	}

	/**
	 * 기존팝업순서 변경
	 */
	@Override
	public int UPT_BRD_001009(Map<String, String> param) throws SQLException {
		return adminDAO.UPT_BRD_001009(param);
	}

	/**
	 * 긴급공지사항 상세보기
	 */
	@Override
	public Map<String, Object> SEL_BRD_001001(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_BRD_001001(param);
	}

	/**
	 * 긴급공지사항 삭제
	 */
	@Override
	public int DEL_BRD_001000(Map<String, String> param) throws SQLException {
		return adminDAO.DEL_BRD_001000(param);
	}

	/**
	 * 긴급공지사항 수정
	 */
	@Override
	public int UPT_BRD_001001(Map<String, String> param) throws SQLException {
		return adminDAO.UPT_BRD_001001(param);
	}

	/**
	 * 관리자 비밀번호 변경
	 */
	@Override
	public int UPT_ADM_001001(LoginVO loginVO) throws SQLException {
		return adminDAO.UPT_ADM_001001(loginVO);
	}

	/**
	 * IP관리 리스트 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_IPM_001000(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_IPM_001000(searchVO);
	}

	/**
	 * IP관리 리스트 갯수 조회
	 */
	@Override
	public int SEL_IPM_001000_CNT(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_IPM_001000_CNT(searchVO);
	}

	/**
	 * IP등록
	 */
	@Override
	public int INS_IPM_001001(Map<String, String> param) throws SQLException {
		return adminDAO.INS_IPM_001001(param);
	}

	/**
	 * IP정보조회
	 */
	@Override
	public Map<String, Object> SEL_IPM_001001(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_IPM_001001(param);
	}

	/**
	 * IP삭제
	 */
	@Override
	public int DEL_IPM_001000(Map<String, String> param) throws SQLException {
		return adminDAO.DEL_IPM_001000(param);
	}

	/**
	 * IP수정
	 */
	@Override
	public int UPT_IPM_001001(Map<String, String> param) throws SQLException {
		return adminDAO.UPT_IPM_001001(param);
	}

	/**
	 * 허용 IP조회
	 */
	@Override
	public List<Map<String, Object>> SEL_IPM_S001009() throws SQLException {
		return adminDAO.SEL_IPM_S001009();
	}

	/**
	 * 앱정보 리스트 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_APP_001000(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_APP_001000(searchVO);
	}

	/**
	 * 앱정보 리스트 갯수 조회
	 */
	@Override
	public int SEL_APP_001000_CNT(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_APP_001000_CNT(searchVO);
	}

	/**
	 * 앱정보 등록
	 */
	@Override
	public int INS_APP_001001(Map<String, String> param) throws SQLException {
		return adminDAO.INS_APP_001001(param);
	}

	/**
	 * 앱정보 상세조회
	 */
	@Override
	public Map<String, Object> SEL_APP_001001(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_APP_001001(param);
	}

	/**
	 * 앱정보 수정
	 */
	@Override
	public int UPT_APP_001001(Map<String, String> param) throws SQLException {
		return adminDAO.UPT_APP_001001(param);
	}

	/**
	 * 앱정보 삭제
	 */
	@Override
	public int DEL_APP_001000(Map<String, String> param) throws SQLException {
		return adminDAO.DEL_APP_001000(param);
	}

	/**
	 * My 그룹 리스트 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_GRP_001000(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_GRP_001000(searchVO);
	}

	/**
	 * My 그룹 리스트 갯수 조회
	 */
	@Override
	public int SEL_GRP_001000_CNT(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_GRP_001000_CNT(searchVO);
	}

	/**
	 * 지역정보 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_ORG_001000(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_ORG_001000(param);
	}

	/**
	 * 지사정보 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_ORG_001001(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_ORG_001001(param);
	}

	/**
	 * 부서정보 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_ORG_001002(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_ORG_001002(param);
	}

	/**
	 * 사용자 정보 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_ORG_001003(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_ORG_001003(param);
	}
	
	/**
	 * 사용자 정보 조회(지역선택, 지사선택)
	 */
	@Override
	public List<Map<String, Object>> SEL_ORG_001004(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_ORG_001004(param);
	}

	/**
	 * My Group 정보 등록
	 */
	@Override
	public int INS_GRP_001001(Map<String, String> param) throws SQLException {
		return adminDAO.INS_GRP_001001(param);
	}

	/**
	 * 사용자 정보 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_GRP_001009(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_GRP_001009(param);
	}

	/**
	 * My Group 사용자 등록
	 */
	@Override
	public int INS_GRP_001009(Map<String, String> param) throws SQLException {
		return adminDAO.INS_GRP_001009(param);
	}

	/**
	 * My Group 상세보기
	 */
	@Override
	public List<Map<String, Object>> SEL_GRP_001001(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_GRP_001001(param);
	}

	/**
	 * My Group 삭제
	 */
	@Override
	public int DEL_GRP_001000(Map<String, String> param) throws SQLException {
		return adminDAO.DEL_GRP_001000(param);
	}

	/**
	 * My Group 수정
	 */
	@Override
	public int UPT_GRP_001001(Map<String, String> param) throws SQLException {
		int upt_grp	= adminDAO.UPT_GRP_001001(param);
		int upt_usr	= adminDAO.UPT_GRP_001009(param);
		
		return upt_grp==1 && upt_usr==1 ? 1 : 0;
	}

	/**
	 * SMS, Push 사용자 수 조회 
	 */
	@Override
	public Map<String, Object> SEL_MSG_S01000() throws SQLException {
		return adminDAO.SEL_MSG_S01000();
	}

	/**
	 * My Group 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_MSG_S01001() throws SQLException {
		return adminDAO.SEL_MSG_S01001();
	}

	/**
	 * 앱 정보 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_MSG_S01011() throws SQLException {
		return adminDAO.SEL_MSG_S01011();
	}

	/**
	 * 사용자 토큰 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_ADM_TKN_S0001(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_ADM_TKN_S0001(param);
	}

	/**
	 * 예약전송 사용자 추가
	 */
	@Override
	public int INS_RSV_001000(Map<String, String> param) throws SQLException {
		 return adminDAO.INS_RSV_001000(param);
	}

	/**
	 * 예약전송 건 확인 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_MSG_RSV_001(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_MSG_RSV_001(param);
	}

	/**
	 * 메시지 발송여부 변경
	 */
	@Override
	public int UPT_MSG_001000(Map<String, String> param) throws SQLException {
		return adminDAO.UPT_MSG_001000(param);
	}

	/**
	 * 통계 조회
	 */
	@Override
	public Map<String, String> SEL_STA_001009(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_STA_001009(searchVO);
	}

	/**
	 * 통계 리스트 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_STA_001000(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_STA_001000(searchVO);
	}

	/**
	 * 통계 리스트 갯수 조회
	 */
	@Override
	public int SEL_STA_001000_CNT(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_STA_001000_CNT(searchVO);
	}
	
	/**
	 * 전송결과 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_STA_001001(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_STA_001001(param);
	}

	/**
	 * 통계 CSV다운로드 데이터 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_STA_CSV_DOWN(Map<String,String> param) throws SQLException {
		return adminDAO.SEL_STA_CSV_DOWN(param);
	}

	/**
	 * fcm정보조회
	 */
	@Override
	public Map<String, String> SEL_STA_001002(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_STA_001002(param);
	}

	/**
	 * 전송실패 목록 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_STA_001003(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_STA_001003(param);
	}

	/**
	 * 전송이력 수정
	 */
	@Override
	public int UPT_SND_001001(Map<String, String> param) throws SQLException {
		return adminDAO.UPT_SND_001001(param);
	}

	/**
	 * 서버관리 리스트 조회
	 */
	@Override
	public List<Map<String, Object>> SEL_SVR_001000(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_SVR_001000(searchVO);
	}

	/**
	 * 서버관리 리스트 갯수 조회
	 */
	@Override
	public int SEL_SVR_001000_CNT(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_SVR_001000_CNT(searchVO);
	}

	/**
	 * 서버등록
	 */
	@Override
	public int INS_SVR_001001(Map<String, String> param) throws SQLException {
		return adminDAO.INS_SVR_001001(param);
	}

	/**
	 * 서버정보조회
	 */
	@Override
	public Map<String, Object> SEL_SVR_001001(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_SVR_001001(param);
	}

	/**
	 * 서버삭제
	 */
	@Override
	public int DEL_SVR_001000(Map<String, String> param) throws SQLException {
		return adminDAO.DEL_SVR_001000(param);
	}

	/**
	 * 서버수정
	 */
	@Override
	public int UPT_SVR_001001(Map<String, String> param) throws SQLException {
		return adminDAO.UPT_SVR_001001(param);
	}

	/**
	 * 허용 서버조회
	 */
	@Override
	public List<Map<String, Object>> SEL_SVR_S001009() throws SQLException {
		return adminDAO.SEL_SVR_S001009();
	}

	/**
	 * 푸시 발송 성공 건 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int SEL_STA_001009_01(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_STA_001009_01(searchVO);
	}

	/**
	 * 푸시 발송 실패 건 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int SEL_STA_001009_02(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_STA_001009_02(searchVO);
	}

	/**
	 * SMS 발송 건 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int SEL_STA_001009_03(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_STA_001009_03(searchVO);
	}

	/**
	 * 수신 성공 건 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int SEL_STA_001009_04(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_STA_001009_04(searchVO);
	}

	/**
	 * 읽음 성공 건 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int SEL_STA_001009_05(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_STA_001009_05(searchVO);
	}

	/**
	 * 관리자 조회
	 * @param searchVO
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> SEL_ADM_001000(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_ADM_001000(searchVO);
	}

	/**
	 * 관리자 갯수 조회
	 * @param searchVO
	 * @return
	 */
	@Override
	public int SEL_ADM_001000_CNT(SearchVO searchVO) throws SQLException {
		return adminDAO.SEL_ADM_001000_CNT(searchVO);
	}

	/**
	 * 관리자 등록
	 * @param param
	 */
	@Override
	public int INS_ADM_001001(Map<String, String> param) throws SQLException {
		return adminDAO.INS_ADM_001001(param);
		
	}

	/**
	 * 관리자 상세 조회
	 * @param param
	 * @return
	 */
	@Override
	public Map<String, Object> SEL_ADM_001001(Map<String, String> param) throws SQLException {
		return adminDAO.SEL_ADM_001001(param);
	}

	/**
	 * 관리자 수정
	 * @param param
	 * @throws SQLException
	 */
	@Override
	public int UPT_ADM_001001(Map<String, String> param) throws SQLException {
		return adminDAO.UPT_ADM_001001(param);
		
	}

	/**
	 * 관리자 삭제
	 * @param param
	 * @throws SQLException
	 */
	@Override
	public int DEL_ADM_001000(Map<String, String> param) throws SQLException {
		return adminDAO.DEL_ADM_001000(param);
	}
}
