package kcert.framework.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import com.ibatis.sqlmap.client.SqlMapClient;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

public class OtpDaoSupport  extends EgovAbstractDAO {
	
	@Resource(name="otpSqlMapClient")
	public void setSuperSqlMapClient(SqlMapClient sqlMapClient){
		super.setSqlMapClient(sqlMapClient);
	}
	/**
	 * 조회된 컬럼명이 소문자화된것을 대문자로 변경
	 * */
	private List<Map<String,Object>> upperKey(List<Map<String,Object>> list ,boolean upperCase){
		if(!upperCase)
			return list;
		List<Map<String,Object>> rstList	= new ArrayList<Map<String,Object>>();
		Map<String,Object>	copy	= null;
		
		for(int i=0;i<list.size();i++){
			copy	= upperKey(list.get(i),upperCase);
			rstList.add(copy);
		}
		list.clear();
		return rstList;
	}
	/**
	 * 조회된 컬럼명이 소문자화된것을 대문자로 변경
	 * */
	private Map<String,Object> upperKey(Map<String,Object> map ,boolean upperCase){
		if(!upperCase)
			return map;
		Map<String,Object>	copy	= new HashMap<String,Object>();;
		Iterator<String> 	iter		= map.keySet().iterator();
		String					key	= "";
		while(iter.hasNext()){
			key	= iter.next();
			copy.put(upperCase?key.toUpperCase(Locale.KOREA):key.toLowerCase(Locale.KOREA), map.get(key));
		}
		map.clear();
		return copy;
	}
	/**
	 * 컬럼명을 대.소문자화 시킬수 있게 boolean 설정
	 * */
    @SuppressWarnings("unchecked")
	public Object selectByPk(String queryId, Object parameterObject,boolean upperCase){
    	Object obj	= super.selectByPk(queryId,parameterObject);
    	if(!upperCase)
    		return obj;
    	if(obj instanceof Map)
    		return upperKey((Map<String,Object>)obj,upperCase);
    	
    	return obj;
    }

	/**
	 * 컬럼명을 대.소문자화 시킬수 있게 boolean 설정
	 * */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> list(String queryId, Object parameterObject,boolean upperCase){
    	return upperKey((List<Map<String,Object>>)(super.getSqlMapClientTemplate().queryForList(queryId, parameterObject)),upperCase);
    }
	/**
	 * 컬럼명을 대.소문자화 시킬수 있게 boolean 설정
	 * */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> listWithPaging(String queryId, Object parameterObject, int pageIndex, int pageSize,boolean upperCase){
        int skipResults = pageIndex * pageSize;
        int maxResults = pageIndex * pageSize + pageSize;
    	return upperKey((List<Map<String,Object>>)(super.getSqlMapClientTemplate().queryForList(queryId, parameterObject, skipResults, maxResults)),upperCase);
    }
	
}
