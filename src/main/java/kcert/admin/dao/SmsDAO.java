package kcert.admin.dao;

import java.sql.SQLException;
import java.util.Map;

import kcert.framework.service.SmsDaoSupport;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("SmsDAO")
@Transactional
public class SmsDAO extends SmsDaoSupport {
	/**
	 * SMS 보내기
	 * @param param
	 * @return
	 */
	public int INS_SMS_001000(Map<String, String> param) throws SQLException {
		return update("INS_SMS_001000", param);
	}
}
