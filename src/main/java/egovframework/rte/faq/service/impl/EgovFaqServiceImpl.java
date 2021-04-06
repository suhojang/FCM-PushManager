/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.rte.faq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.faq.service.EgovFaqService;
import egovframework.rte.faq.service.FaqDefaultVO;
import egovframework.rte.faq.service.FaqVO;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;

/**  
 * @Class Name : EgovFaqServiceImpl.java
 * @Description : Faq Business Implement Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2009.03.16           최초생성
 * 
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2009. 03.16
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by MOPAS All right reserved.
 */

@Service("FaqService")
public class EgovFaqServiceImpl extends AbstractServiceImpl implements
        EgovFaqService {
	
	/** FaqDAO */
    @Resource(name="FaqDAO")
    private FaqDAO FaqDAO;
    
    /** ID Generation */
    @Resource(name="egovIdGnrService2")    
    private EgovIdGnrService egovIdGnrService2;

	/**
	 * 글을 등록한다.
	 * @param vo - 등록할 정보가 담긴 FaqVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertFaq(FaqVO vo) throws Exception {
    	log.debug(vo.toString());
    	
    	/** ID Generation Service */
    	String id = egovIdGnrService2.getNextStringId();
    	vo.setId(id);
    	log.debug(vo.toString());
    	
    	FaqDAO.insertFaq(vo);    	
        return id;
    }

    /**
	 * 글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 FaqVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateFaq(FaqVO vo) throws Exception {
        FaqDAO.updateFaq(vo);
    }

    /**
	 * 글을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 FaqVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteFaq(FaqVO vo) throws Exception {
        FaqDAO.deleteFaq(vo);
    }

    /**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 FaqVO
	 * @return 조회한 글
	 * @exception Exception
	 */
    public FaqVO selectFaq(FaqVO vo) throws Exception {
        FaqVO resultVO = FaqDAO.selectFaq(vo);
        if (resultVO == null)
            throw processException("info.nodata.msg");
        return resultVO;
    }

    /**
	 * 글 목록을 조회한다.
	 * @param faqVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
    @SuppressWarnings("rawtypes")
	public List selectFaqList(FaqDefaultVO faqVO) throws Exception {
        return FaqDAO.selectFaqList(faqVO);
    }

    /**
	 * 글 총 갯수를 조회한다.
	 * @param faqVO - 조회할 정보가 담긴 VO
	 * @return 글 총 갯수
	 * @exception
	 */
    public int selectFaqListTotCnt(FaqDefaultVO faqVO) {
		return FaqDAO.selectFaqListTotCnt(faqVO);
	}
    
}
