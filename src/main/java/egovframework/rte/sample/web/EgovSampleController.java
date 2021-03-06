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
 * @  ?????????      ?????????              ????????????
 * @ ---------   ---------   -------------------------------
 * @ 2009.03.16           ????????????
 * 
 * @author ????????????????????? ???????????? ?????????
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
	 * ??? ????????? ????????????. (pageing)
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
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
	 * ?????? ????????????.
	 * @param sampleVO - ????????? ????????? ?????? VO
	 * @param searchVO - ?????? ???????????? ????????? ?????? VO
	 * @param status
	 * @return @ModelAttribute("sampleVO") - ????????? ??????
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
	 * ??? ?????? ????????? ????????????.
	 * @param searchVO - ?????? ???????????? ????????? ?????? VO
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
	 * ?????? ????????????.
	 * @param sampleVO - ????????? ????????? ?????? VO
	 * @param searchVO - ?????? ???????????? ????????? ?????? VO
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
	 * ??? ??????????????? ????????????.
	 * @param id - ????????? ??? id
	 * @param searchVO - ?????? ???????????? ????????? ?????? VO
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
        // ???????????? CoC ??? ?????? sampleVO
        model.addAttribute(selectSample(sampleVO, searchVO));
        return "/sample/egovSampleRegister";
    }
    /**
     * 
     * ??? ???????????? (????????? ????????????)
     * @return "/sample/view"
     * @exception Exception
     */
    @RequestMapping(value="/notice/detail/view/{key}", method=RequestMethod.GET)
    public String view(
    		@PathVariable String key //	??????
    		,@ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model)
    				throws Exception {
    	SampleVO sampleVO = new SampleVO();
    	sampleVO.setId(key);
    	// ???????????? CoC ??? ?????? sampleVO
    	model.addAttribute(selectSample(sampleVO, searchVO));
    	return "/sample/view";
    }
    /**
     * 
     * ??? ???????????? (????????? ????????????)
     * @return "/sample/view"
     * @exception Exception
     */
    @RequestMapping(value="/notice/detail/view", method=RequestMethod.POST)
    public String view2(
    		 @RequestParam String key //	??????
    		,@ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model)
    				throws Exception {
    	SampleVO sampleVO = new SampleVO();
    	sampleVO.setId(key);
    	// ???????????? CoC ??? ?????? sampleVO
    	model.addAttribute(selectSample(sampleVO, searchVO));
    	return "/sample/view";
    }

    /**
	 * ?????? ????????????.
	 * @param sampleVO - ????????? ????????? ?????? VO
	 * @param searchVO - ?????? ???????????? ????????? ?????? VO
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
	 * ?????? ????????????.
	 * @param sampleVO - ????????? ????????? ?????? VO
	 * @param searchVO - ?????? ???????????? ????????? ?????? VO
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
	 * ??? ????????? ????????????. (pageing)
	 * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
	 * @param model
	 * @return "/sample/egovSampleList"
	 * @exception Exception
	 */
    @RequestMapping(value="/notice/list/{current_page}/{request_page}", method=RequestMethod.GET)
    public @ResponseBody String selNotice(@ModelAttribute("searchVO") SampleDefaultVO searchVO
    		,HttpServletRequest request
    		,HttpServletResponse response
			,@PathVariable int current_page		       //	???????????????    
			,@PathVariable int request_page            //	???????????????
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
     * ??? ????????? ????????????. (pageing)
     * @param searchVO - ????????? ????????? ?????? SampleDefaultVO
     * @param model
     * @return "/sample/egovSampleList"
     * @exception Exception
     */
    @RequestMapping(value="/notice/list/{current_page}/{request_page}", method=RequestMethod.POST)
    public @ResponseBody String selNotice2(@ModelAttribute("searchVO") SampleDefaultVO searchVO
    		,HttpServletRequest request
    		,HttpServletResponse response
    		,@RequestParam int current_page          //	???????????????
    		,@RequestParam int request_page          //	???????????????
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
	 * ?????? ????????????.
	 * @param sampleVO - ????????? ????????? ?????? VO
	 * @param searchVO - ?????? ???????????? ????????? ?????? VO
	 * @param status
	 * @return @ModelAttribute("sampleVO") - ????????? ??????
	 * @exception Exception
	 */
    @RequestMapping(value="/notice/detail/{key}", method=RequestMethod.GET)
    public @ResponseBody String selNoticeDetail(
            HttpServletRequest request
    		,HttpServletResponse response
			,@PathVariable String key //	??????
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
     * ?????? ????????????.
     * @param sampleVO - ????????? ????????? ?????? VO
     * @param searchVO - ?????? ???????????? ????????? ?????? VO
     * @param status
     * @return @ModelAttribute("sampleVO") - ????????? ??????
     * @exception Exception
     */
    @RequestMapping(value="/notice/detail/{key}", method=RequestMethod.POST)
    public @ResponseBody String selNoticeDetail2(
    		HttpServletRequest request
    		,HttpServletResponse response
    		,@RequestParam String key //	??????
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
