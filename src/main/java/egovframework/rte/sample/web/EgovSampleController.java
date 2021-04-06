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
package egovframework.rte.sample.web;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import egovframework.rte.sample.service.EgovSampleService;
import egovframework.rte.sample.service.SampleDefaultVO;
import egovframework.rte.sample.service.SampleVO;


/**  
 * @Class Name : EgovSampleController.java
 * @Description : EgovSample Controller Class
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

@Controller
@SessionAttributes(types=SampleVO.class)
public class EgovSampleController {
	
	/** EgovSampleService */
    @Resource(name = "sampleService")
    private EgovSampleService sampleService;
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /** Validator */
    @Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
    /**
	 * 글 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "/sample/egovSampleList"
	 * @exception Exception
	 */
    @RequestMapping(value="/sample/egovSampleList.do")
    public String selectSampleList(@ModelAttribute("searchVO") SampleDefaultVO searchVO, 
    		ModelMap model)
            throws Exception {
    	
    	/** EgovPropertyService.sample */
    	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
    	searchVO.setPageSize(propertiesService.getInt("pageSize"));
    	
    	/** pageing setting */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());
		
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
        List sampleList = sampleService.selectSampleList(searchVO);
        model.addAttribute("resultList", sampleList);
        
        int totCnt = sampleService.selectSampleListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        
        return "/sample/egovSampleList";
    } 

    /**
	 * 글을 조회한다.
	 * @param sampleVO - 조회할 정보가 담긴 VO
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return @ModelAttribute("sampleVO") - 조회한 정보
	 * @exception Exception
	 */
    @RequestMapping("/sample/selectSample.do")
    public @ModelAttribute("sampleVO")
    SampleVO selectSample(
            SampleVO sampleVO,
            @ModelAttribute("searchVO") SampleDefaultVO searchVO) throws Exception {
        return sampleService.selectSample(sampleVO);
    }
		
    /**
	 * 글 등록 화면을 조회한다.
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "/sample/egovSampleRegister"
	 * @exception Exception
	 */
    @RequestMapping("/sample/addSampleView.do")
    public String addSampleView(
            @ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model)
            throws Exception {
        model.addAttribute("sampleVO", new SampleVO());
        return "/sample/egovSampleRegister";
    }
    
    /**
	 * 글을 등록한다.
	 * @param sampleVO - 등록할 정보가 담긴 VO
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return "forward:/sample/egovSampleList.do"
	 * @exception Exception
	 */
    @RequestMapping("/sample/addSample.do")
    public String addSample(
    		@ModelAttribute("searchVO") SampleDefaultVO searchVO,
       	 	SampleVO sampleVO,
            BindingResult bindingResult, Model model, SessionStatus status) 
    throws Exception {
    	
    	// Server-Side Validation
    	beanValidator.validate(sampleVO, bindingResult);
    	
    	if (bindingResult.hasErrors()) {
    		model.addAttribute("sampleVO", sampleVO);
			return "/sample/egovSampleRegister";
    	}
    	
        sampleService.insertSample(sampleVO);
        status.setComplete();
        return "forward:/sample/egovSampleList.do";
    }
    
    /**
	 * 글 수정화면을 조회한다.
	 * @param id - 수정할 글 id
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "/sample/egovSampleRegister"
	 * @exception Exception
	 */
    @RequestMapping("/sample/updateSampleView.do")
    public String updateSampleView(
            @RequestParam("selectedId") String id ,
            @ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model)
            throws Exception {
        SampleVO sampleVO = new SampleVO();
        sampleVO.setId(id);
        // 변수명은 CoC 에 따라 sampleVO
        model.addAttribute(selectSample(sampleVO, searchVO));
        return "/sample/egovSampleRegister";
    }
    /**
     * 
     * 글 상세보기 (내용만 나타내기)
     * @return "/sample/view"
     * @exception Exception
     */
    @RequestMapping(value="/notice/detail/view/{key}", method=RequestMethod.GET)
    public String view(
    		@PathVariable String key //	키값
    		,@ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model)
    				throws Exception {
    	SampleVO sampleVO = new SampleVO();
    	sampleVO.setId(key);
    	// 변수명은 CoC 에 따라 sampleVO
    	model.addAttribute(selectSample(sampleVO, searchVO));
    	return "/sample/view";
    }
    /**
     * 
     * 글 상세보기 (내용만 나타내기)
     * @return "/sample/view"
     * @exception Exception
     */
    @RequestMapping(value="/notice/detail/view", method=RequestMethod.POST)
    public String view2(
    		 @RequestParam String key //	키값
    		,@ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model)
    				throws Exception {
    	SampleVO sampleVO = new SampleVO();
    	sampleVO.setId(key);
    	// 변수명은 CoC 에 따라 sampleVO
    	model.addAttribute(selectSample(sampleVO, searchVO));
    	return "/sample/view";
    }

    /**
	 * 글을 수정한다.
	 * @param sampleVO - 수정할 정보가 담긴 VO
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return "forward:/sample/egovSampleList.do"
	 * @exception Exception
	 */
    @RequestMapping("/sample/updateSample.do")
    public String updateSample(
            @ModelAttribute("searchVO") SampleDefaultVO searchVO, 
            SampleVO sampleVO, 
            BindingResult bindingResult, Model model, SessionStatus status)
            throws Exception {

    	beanValidator.validate(sampleVO, bindingResult);
    	
    	if (bindingResult.hasErrors()) {
    		model.addAttribute("sampleVO", sampleVO);
			return "/sample/egovSampleRegister";
    	}
    	
        sampleService.updateSample(sampleVO);
        status.setComplete();
        return "forward:/sample/egovSampleList.do";
    }
    
    /**
	 * 글을 삭제한다.
	 * @param sampleVO - 삭제할 정보가 담긴 VO
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return "forward:/sample/egovSampleList.do"
	 * @exception Exception
	 */
    @RequestMapping("/sample/deleteSample.do")
    public String deleteSample(
            SampleVO sampleVO,
            @ModelAttribute("searchVO") SampleDefaultVO searchVO, SessionStatus status)
            throws Exception {
        sampleService.deleteSample(sampleVO);
        status.setComplete();
        return "forward:/sample/egovSampleList.do";
    }
    
    /**
	 * 글 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @param model
	 * @return "/sample/egovSampleList"
	 * @exception Exception
	 */
    @RequestMapping(value="/notice/list/{current_page}/{request_page}", method=RequestMethod.GET)
    public @ResponseBody String selNotice(@ModelAttribute("searchVO") SampleDefaultVO searchVO
    		,HttpServletRequest request
    		,HttpServletResponse response
			,@PathVariable int current_page		       //	현재페이지    
			,@PathVariable int request_page            //	요청페이지
    		,ModelMap model)
            throws Exception {
    	String result = "";
    	
    	/** EgovPropertyService.sample */
    	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
    	searchVO.setPageSize(propertiesService.getInt("pageSize"));
    	
    	/** pageing setting */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(current_page);
		paginationInfo.setRecordCountPerPage(request_page);
		paginationInfo.setPageSize(searchVO.getPageSize());
		
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		int totCnt = sampleService.selectSampleListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		
        List sampleList = sampleService.selectSampleList(searchVO);
        
		
		HashMap hmLogInfo = new HashMap();
		ArrayList alstLogInfo = new ArrayList();
        for(int i=0; i < sampleList.size();i++){
        	hmLogInfo = new HashMap<String, String>();
        	JSONObject jsObj2 = JSONObject.fromObject(sampleList.get(i));
    		hmLogInfo.put("key",jsObj2.get("id"));
    		hmLogInfo.put("title",jsObj2.get("name"));
    		hmLogInfo.put("date",jsObj2.get("regdate"));
    		alstLogInfo.add(hmLogInfo);
        }
        
		JSONArray jaryLogInfo = JSONArray.fromObject(alstLogInfo);

		HashMap hmJsonLogInfo = new HashMap();

        hmJsonLogInfo.put("list", jaryLogInfo);
		hmJsonLogInfo.put("total",Integer.toString(totCnt));
		
		JSONObject jsObj = JSONObject.fromObject(hmJsonLogInfo);

		//box.put("jobjSrchBalance", jsObj);
		System.out.println(jsObj.toString());	
		result = jsObj.toString();
		
		return result;
    } 
    /**
     * 글 목록을 조회한다. (pageing)
     * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
     * @param model
     * @return "/sample/egovSampleList"
     * @exception Exception
     */
    @RequestMapping(value="/notice/list/{current_page}/{request_page}", method=RequestMethod.POST)
    public @ResponseBody String selNotice2(@ModelAttribute("searchVO") SampleDefaultVO searchVO
    		,HttpServletRequest request
    		,HttpServletResponse response
    		,@RequestParam int current_page          //	현재페이지
    		,@RequestParam int request_page          //	요청페이지
    		,ModelMap model)
    				throws Exception {
    	String result = "";
    	
    	/** EgovPropertyService.sample */
    	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
    	searchVO.setPageSize(propertiesService.getInt("pageSize"));
    	
    	/** pageing setting */
    	PaginationInfo paginationInfo = new PaginationInfo();
    	paginationInfo.setCurrentPageNo(current_page);
    	paginationInfo.setRecordCountPerPage(request_page);
    	paginationInfo.setPageSize(searchVO.getPageSize());
    	
    	searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
    	searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
    	searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
    	int totCnt = sampleService.selectSampleListTotCnt(searchVO);
    	paginationInfo.setTotalRecordCount(totCnt);
    	
    	List sampleList = sampleService.selectSampleList(searchVO);
    	
    	
    	HashMap hmLogInfo = new HashMap();
    	ArrayList alstLogInfo = new ArrayList();
    	for(int i=0; i < sampleList.size();i++){
    		hmLogInfo = new HashMap<String, String>();
    		JSONObject jsObj2 = JSONObject.fromObject(sampleList.get(i));
    		hmLogInfo.put("key",jsObj2.get("id"));
    		hmLogInfo.put("title",jsObj2.get("name"));
    		hmLogInfo.put("date",jsObj2.get("regdate"));
    		alstLogInfo.add(hmLogInfo);
    	}
    	
    	JSONArray jaryLogInfo = JSONArray.fromObject(alstLogInfo);
    	
    	HashMap hmJsonLogInfo = new HashMap();
    	
    	hmJsonLogInfo.put("list", jaryLogInfo);
    	hmJsonLogInfo.put("total",Integer.toString(totCnt));
    	
    	JSONObject jsObj = JSONObject.fromObject(hmJsonLogInfo);
    	
    	//box.put("jobjSrchBalance", jsObj);
    	System.out.println(jsObj.toString());	
    	result = jsObj.toString();
    	
    	return result;
    } 

    /**
	 * 글을 조회한다.
	 * @param sampleVO - 조회할 정보가 담긴 VO
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return @ModelAttribute("sampleVO") - 조회한 정보
	 * @exception Exception
	 */
    @RequestMapping(value="/notice/detail/{key}", method=RequestMethod.GET)
    public @ResponseBody String selNoticeDetail(
            HttpServletRequest request
    		,HttpServletResponse response
			,@PathVariable String key //	키값
			) throws Exception {
    	String result = "";
    	
    	SampleVO sampleVO = new SampleVO();
    	sampleVO.setId(key);
    	try{
    		sampleVO = sampleService.selectSample(sampleVO);

    	System.out.println(sampleVO.getName());
    	HashMap hmLogInfo = new HashMap();
    	hmLogInfo.put("key", sampleVO.getId());
    	hmLogInfo.put("title", sampleVO.getName());
    	hmLogInfo.put("content", "/notice/detail/view/"+sampleVO.getId() );
    	hmLogInfo.put("date", sampleVO.getRegDate());
    	
    	JSONObject jobj = JSONObject.fromObject(hmLogInfo);
    		result = jobj.toString();
    	}catch (NullPointerException ex) {
    		result = ex.getMessage();
    	} catch (IndexOutOfBoundsException ex) {
    		result = ex.getMessage();
    	} catch (ClassCastException ex) {
    		result = ex.getMessage();
    	} catch(Exception ex){ 
    		result = ex.getMessage();
    	}
    	return result;
		
    }
    /**
     * 글을 조회한다.
     * @param sampleVO - 조회할 정보가 담긴 VO
     * @param searchVO - 목록 조회조건 정보가 담긴 VO
     * @param status
     * @return @ModelAttribute("sampleVO") - 조회한 정보
     * @exception Exception
     */
    @RequestMapping(value="/notice/detail/{key}", method=RequestMethod.POST)
    public @ResponseBody String selNoticeDetail2(
    		HttpServletRequest request
    		,HttpServletResponse response
    		,@RequestParam String key //	키값
    		) throws Exception {
    	String result = "";
    	
    	SampleVO sampleVO = new SampleVO();
    	sampleVO.setId(key);
    	try{
    		sampleVO = sampleService.selectSample(sampleVO);
    		
    		System.out.println(sampleVO.getName());
    		HashMap hmLogInfo = new HashMap();
    		hmLogInfo.put("key", sampleVO.getId());
    		hmLogInfo.put("title", sampleVO.getName());
    		hmLogInfo.put("content", "/notice/detail/view/"+sampleVO.getId() );
    		hmLogInfo.put("date", sampleVO.getRegDate());
    		
    		JSONObject jobj = JSONObject.fromObject(hmLogInfo);
    		result = jobj.toString();
    	}catch (NullPointerException ex) {
    		result = ex.getMessage();
    	} catch (IndexOutOfBoundsException ex) {
    		result = ex.getMessage();
    	} catch (ClassCastException ex) {
    		result = ex.getMessage();
    	} catch(Exception ex){ 
    		result = ex.getMessage();
    	}
    	
    	return result;
    	
    }
}
