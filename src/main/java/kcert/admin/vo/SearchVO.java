package kcert.admin.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SearchVO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String id ="";
	
	private String GUBUN = "";
	
	/** 검색조건 */
    private String searchCondition = "";
    
    /** 검색조건2 */
    private String searchConditionState = "";
    
    /** 검색Keyword */
    private String searchKeyword = "";
    
    /** 검색사용여부 */
    private String searchUseYn = "";
    
    /** 현재페이지 */
    private int pageNo = 1;
    
    /** 페이지갯수 */
    private int pageUnit = 10;
    
    /** 페이지사이즈 */
    private int pageSize = 10;

    /** firstIndex */
    private int firstIndex = 1;

    /** lastIndex */
    private int lastIndex = 1;

    /** recordCountPerPage */
    private int recordCountPerPage = 10;
    
    /** 시작일*/
    private String stDate = "";
    
    /** 종료일*/
    private String etDate = "";
    
    /** 메시지 들록 아이디*/
    private String arrRegId = "";
    
    /** 보험회사이름*/
    private String insuranceName = "";
    
    /** 모두 체크 */
    private String chkall = "";
    /** 모두 체크  YN hidden 값 넘겨주기 */
    private String chkallYN = "";
    
        
	public String getArrRegId() {
		return arrRegId;
	}

	public void setArrRegId(String arrRegId) {
		this.arrRegId = arrRegId;
	}

	public int getFirstIndex() {
		return firstIndex;
	}

	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public int getRecordCountPerPage() {
		return recordCountPerPage;
	}

	public void setRecordCountPerPage(int recordCountPerPage) {
		this.recordCountPerPage = recordCountPerPage;
	}

	public String getSearchCondition() {
        return searchCondition;
    }

    public void setSearchCondition(String searchCondition) {
        this.searchCondition = searchCondition;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getSearchUseYn() {
        return searchUseYn;
    }

    public void setSearchUseYn(String searchUseYn) {
        this.searchUseYn = searchUseYn;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageUnit() {
        return pageUnit;
    }

    public void setPageUnit(int pageUnit) {
        this.pageUnit = pageUnit;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

	public String getStDate() {
		return stDate;
	}

	public void setStDate(String stDate) {
		this.stDate = stDate;
	}

	public String getEtDate() {
		return etDate;
	}

	public void setEtDate(String etDate) {
		this.etDate = etDate;
	}

	public String getChkall() {
		return chkall;
	}

	public void setChkall(String chkall) {
		this.chkall = chkall;
	}

	public String getChkallYN() {
		return chkallYN;
	}

	public void setChkallYN(String chkallYN) {
		this.chkallYN = chkallYN;
	}

	public String getInsuranceName() {
		return insuranceName;
	}

	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSearchConditionState() {
		return searchConditionState;
	}

	public void setSearchConditionState(String searchConditionState) {
		this.searchConditionState = searchConditionState;
	}

	public String getGUBUN() {
		return GUBUN;
	}

	public void setGUBUN(String gUBUN) {
		GUBUN = gUBUN;
	}

}
