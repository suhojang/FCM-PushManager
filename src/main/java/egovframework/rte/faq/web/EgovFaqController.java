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
package egovframework.rte.faq.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import egovframework.rte.faq.service.EgovFaqService;
import egovframework.rte.faq.service.FaqDefaultVO;
import egovframework.rte.faq.service.FaqVO;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;


/**  
 * @Class Name : EgovFaqController.java
 * @Description : EgovFaq Controller Class
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
@SessionAttributes(types=FaqVO.class)
public class EgovFaqController {
	
	/** EgovFaqService */
    @Resource(name = "FaqService")
    private EgovFaqService FaqService;
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /** Validator */
    @Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
    /**
	 * ??? ????????? ????????????. (pageing)
	 * @param faqVO - ????????? ????????? ?????? FaqDefaultVO
	 * @param model
	 * @return "/faq/egovFaqList"
	 * @exception Exception
	 */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value="/faq/egovFaqList.do")
    public String selectFaqList(@ModelAttribute("faqDefaultVO") FaqDefaultVO faqDefaultVO, 
    		ModelMap model)
            throws Exception {
    	
    	/** EgovPropertyService.Faq */
    	faqDefaultVO.setPageUnit(propertiesService.getInt("pageUnit"));
    	faqDefaultVO.setPageSize(propertiesService.getInt("pageSize"));
    	
    	/** pageing setting */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(faqDefaultVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(faqDefaultVO.getPageUnit());
		paginationInfo.setPageSize(faqDefaultVO.getPageSize());
		
		faqDefaultVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		faqDefaultVO.setLastIndex(paginationInfo.getLastRecordIndex());
		faqDefaultVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
        List FaqList = FaqService.selectFaqList(faqDefaultVO);
        model.addAttribute("resultList", FaqList);
        
        int totCnt = FaqService.selectFaqListTotCnt(faqDefaultVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        
        return "/faq/egovFaqList";
    } 

    /**
	 * ?????? ????????????.
	 * @param FaqVO - ????????? ????????? ?????? VO
	 * @param faqVO - ?????? ???????????? ????????? ?????? VO
	 * @param status
	 * @return @ModelAttribute("FaqVO") - ????????? ??????
	 * @exception Exception
	 */
    @RequestMapping("/faq/selectFaq.do")
    public @ModelAttribute("FaqVO")
    FaqVO selectFaq(
            FaqVO faqVO,
            @ModelAttribute("faqDefaultVO") FaqDefaultVO faqDefaultVO) throws Exception {
        return FaqService.selectFaq(faqVO);
    }
		
    /**
	 * ??? ?????? ????????? ????????????.
	 * @param faqVO - ?????? ???????????? ????????? ?????? VO
	 * @param model
	 * @return "/faq/egovFaqRegister"
	 * @exception Exception
	 */
    @RequestMapping("/faq/addFaqView.do")
    public String addFaqView(
            @ModelAttribute("faqDefaultVO") FaqDefaultVO faqDefaultVO, Model model)
            throws Exception {
        model.addAttribute("faqVO", new FaqVO());
        return "/faq/egovFaqRegister";
    }
    
    /**
	 * ?????? ????????????.
	 * @param FaqVO - ????????? ????????? ?????? VO
	 * @param faqVO - ?????? ???????????? ????????? ?????? VO
	 * @param status
	 * @return "forward:/faq/egovFaqList.do"
	 * @exception Exception
	 */
    @RequestMapping("/faq/addFaq.do")
    public String addFaq(
    		@ModelAttribute("faqDefaultVO") FaqDefaultVO faqDefaultVO,
       	 	FaqVO faqVO,
            BindingResult bindingResult, Model model, SessionStatus status) 
    throws Exception {
    	
    	// Server-Side Validation
    	beanValidator.validate(faqVO, bindingResult);
    	
    	if (bindingResult.hasErrors()) {
    		model.addAttribute("FaqVO", faqVO);
			return "/faq/egovFaqRegister";
    	}
    	
        FaqService.insertFaq(faqVO);
        status.setComplete();
        return "forward:/faq/egovFaqList.do";
    }
    
    /**
	 * ??? ??????????????? ????????????.
	 * @param id - ????????? ??? id
	 * @param faqVO - ?????? ???????????? ????????? ?????? VO
	 * @param model
	 * @return "/faq/egovFaqRegister"
	 * @exception Exception
	 */
    @RequestMapping("/faq/updateFaqView.do")
    public String updateFaqView(
            @RequestParam("selectedId") String id ,
            @ModelAttribute("faqDefaultVO") FaqDefaultVO faqDefaultVO, Model model)
            throws Exception {
        FaqVO faqVO = new FaqVO();
        faqVO.setId(id);
        // ???????????? CoC ??? ?????? FaqVO
        model.addAttribute(selectFaq(faqVO, faqDefaultVO));
        return "/faq/egovFaqRegister";
    }
    /**
     * 
     * ??? ???????????? (????????? ????????????)
     * @return "/faq/view"
     * @exception Exception
     */
    @RequestMapping(value="/faq/detail/view/{key}", method=RequestMethod.GET)
    public String view(
    		@PathVariable String key //	??????
    		,@ModelAttribute("faqDefaultVO") FaqDefaultVO faqDefaultVO, Model model)
    				throws Exception {
    	FaqVO faqVO = new FaqVO();
    	faqVO.setId(key);
    	// ???????????? CoC ??? ?????? FaqVO
    	model.addAttribute(selectFaq(faqVO, faqDefaultVO));
    	return "/faq/view";
    }
    /**
     * 
     * ??? ???????????? (????????? ????????????)
     * @return "/faq/view"
     * @exception Exception
     */
    @RequestMapping(value="/faq/detail/view", method=RequestMethod.POST)
    public String view2(
    		 @RequestParam String key //	??????
    		,@ModelAttribute("faqDefaultVO") FaqDefaultVO faqDefaultVO, Model model)
    				throws Exception {
    	FaqVO faqVO = new FaqVO();
    	faqVO.setId(key);
    	// ???????????? CoC ??? ?????? FaqVO
    	model.addAttribute(selectFaq(faqVO, faqDefaultVO));
    	return "/faq/view";
    }

    /**
	 * ?????? ????????????.
	 * @param FaqVO - ????????? ????????? ?????? VO
	 * @param faqVO - ?????? ???????????? ????????? ?????? VO
	 * @param status
	 * @return "forward:/faq/egovFaqList.do"
	 * @exception Exception
	 */
    @RequestMapping("/faq/updateFaq.do")
    public String updateFaq(
            @ModelAttribute("faqDefaultVO") FaqDefaultVO faqDefaultVO, 
            FaqVO faqVO, 
            BindingResult bindingResult, Model model, SessionStatus status)
            throws Exception {

    	beanValidator.validate(faqVO, bindingResult);
    	
    	if (bindingResult.hasErrors()) {
    		model.addAttribute("FaqVO", faqVO);
			return "/faq/egovFaqRegister";
    	}
    	
        FaqService.updateFaq(faqVO);
        status.setComplete();
        return "forward:/faq/egovFaqList.do";
    }
    
    /**
	 * ?????? ????????????.
	 * @param FaqVO - ????????? ????????? ?????? VO
	 * @param faqVO - ?????? ???????????? ????????? ?????? VO
	 * @param status
	 * @return "forward:/faq/egovFaqList.do"
	 * @exception Exception
	 */
    @RequestMapping("/faq/deleteFaq.do")
    public String deleteFaq(
            FaqVO faqVO,
            @ModelAttribute("faqDefaultVO") FaqDefaultVO faqDefaultVO, SessionStatus status)
            throws Exception {
        FaqService.deleteFaq(faqVO);
        status.setComplete();
        return "forward:/faq/egovFaqList.do";
    }
    
    /**
	 * ??? ????????? ????????????. (pageing)
	 * @param faqVO - ????????? ????????? ?????? FaqDefaultVO
	 * @param model
	 * @return "/faq/egovFaqList"
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/faq/list/{current_page}/{request_page}", method=RequestMethod.GET)
    public @ResponseBody String selfaq(@ModelAttribute("faqDefaultVO") FaqDefaultVO faqDefaultVO
    		,HttpServletRequest request
    		,HttpServletResponse response
			,@PathVariable int current_page		       //	???????????????    
			,@PathVariable int request_page            //	???????????????
    		,ModelMap model)
            throws Exception {
    	String result = "";
    	
    	/** EgovPropertyService.Faq */
    	faqDefaultVO.setPageUnit(propertiesService.getInt("pageUnit"));
    	faqDefaultVO.setPageSize(propertiesService.getInt("pageSize"));
    	
    	/** pageing setting */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(current_page);
		paginationInfo.setRecordCountPerPage(request_page);
		paginationInfo.setPageSize(faqDefaultVO.getPageSize());
		
		faqDefaultVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		faqDefaultVO.setLastIndex(paginationInfo.getLastRecordIndex());
		faqDefaultVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		int totCnt = FaqService.selectFaqListTotCnt(faqDefaultVO);
		paginationInfo.setTotalRecordCount(totCnt);
		
        List FaqList = FaqService.selectFaqList(faqDefaultVO);
        
		
        HashMap groupValue = new HashMap<String, String>();
		
        for(int i=0; i < FaqList.size();i++){
        	
        	JSONObject jsObj2 = JSONObject.fromObject(FaqList.get(i));
        	
        	HashMap childValue = new HashMap<String, String>();
        	HashMap itemValue = new HashMap<String, String>();
        	ArrayList itemList = new ArrayList();
        	
        	childValue.put("key",jsObj2.get("id"));			//??????
        	childValue.put("q",jsObj2.get("name"));			//??????
        	childValue.put("date",jsObj2.get("regdate"));	//?????????
        	itemValue.put("a",jsObj2.get("description"));	//??????
        	itemList.add(itemValue);
        	JSONArray itemArray = JSONArray.fromObject(itemList);
        	childValue.put("items",itemArray);//??????
    		
    		groupValue.put(childValue.get("key"),childValue);//???????????????
    		
        }
        
		//JSONArray jaryLogInfo = JSONArray.fromObject(groupValue);
		
		HashMap hmJsonLogInfo = new HashMap();

        hmJsonLogInfo.put("groupList", groupValue);
		hmJsonLogInfo.put("total",Integer.toString(totCnt));
		
		JSONObject jsObj = JSONObject.fromObject(hmJsonLogInfo);

		//box.put("jobjSrchBalance", jsObj);
		System.out.println(jsObj.toString());	
		result = jsObj.toString();
		
		return result;
    } 
    /**
     * ??? ????????? ????????????. (pageing)
     * @param faqVO - ????????? ????????? ?????? FaqDefaultVO
     * @param model
     * @return "/faq/egovFaqList"
     * @exception Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/faq/list/{current_page}/{request_page}", method=RequestMethod.POST)
    public @ResponseBody String selfaq2(@ModelAttribute("faqDefaultVO") FaqDefaultVO faqDefaultVO
    		,HttpServletRequest request
    		,HttpServletResponse response
    		,@RequestParam int current_page          //	???????????????
    		,@RequestParam int request_page          //	???????????????
    		,ModelMap model)
    				throws Exception {
    	String result = "";
    	
    	/** EgovPropertyService.Faq */
    	faqDefaultVO.setPageUnit(propertiesService.getInt("pageUnit"));
    	faqDefaultVO.setPageSize(propertiesService.getInt("pageSize"));
    	
    	/** pageing setting */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(current_page);
		paginationInfo.setRecordCountPerPage(request_page);
		paginationInfo.setPageSize(faqDefaultVO.getPageSize());
		
		faqDefaultVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		faqDefaultVO.setLastIndex(paginationInfo.getLastRecordIndex());
		faqDefaultVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		int totCnt = FaqService.selectFaqListTotCnt(faqDefaultVO);
		paginationInfo.setTotalRecordCount(totCnt);
		
        List FaqList = FaqService.selectFaqList(faqDefaultVO);
        
		
        HashMap groupValue = new HashMap<String, String>();
		
        for(int i=0; i < FaqList.size();i++){
        	
        	JSONObject jsObj2 = JSONObject.fromObject(FaqList.get(i));
        	
        	HashMap childValue = new HashMap<String, String>();
        	HashMap itemValue = new HashMap<String, String>();
        	ArrayList itemList = new ArrayList();
        	
        	childValue.put("key",jsObj2.get("id"));			//??????
        	childValue.put("q",jsObj2.get("name"));			//??????
        	childValue.put("date",jsObj2.get("regdate"));	//?????????
        	itemValue.put("a",jsObj2.get("description"));	//??????
        	itemList.add(itemValue);
        	JSONArray itemArray = JSONArray.fromObject(itemList);
        	childValue.put("items",itemArray);//??????
    		
    		groupValue.put(childValue.get("key"),childValue);//???????????????
    		
        }
        
		//JSONArray jaryLogInfo = JSONArray.fromObject(groupValue);
		
		HashMap hmJsonLogInfo = new HashMap();

        hmJsonLogInfo.put("groupList", groupValue);
		hmJsonLogInfo.put("total",Integer.toString(totCnt));
		
		JSONObject jsObj = JSONObject.fromObject(hmJsonLogInfo);

		//box.put("jobjSrchBalance", jsObj);
		System.out.println(jsObj.toString());	
		result = jsObj.toString();
		
		return result;
    } 

    /**
	 * ?????? ????????????.
	 * @param FaqVO - ????????? ????????? ?????? VO
	 * @param faqVO - ?????? ???????????? ????????? ?????? VO
	 * @param status
	 * @return @ModelAttribute("FaqVO") - ????????? ??????
	 * @exception Exception
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/faq/detail/{key}", method=RequestMethod.GET)
    public @ResponseBody String selfaqDetail(
            HttpServletRequest request
    		,HttpServletResponse response
			,@PathVariable String key //	??????
			) throws Exception {
    	String result = "";
    	
    	FaqVO faqVO = new FaqVO();
    	faqVO.setId(key);
    	try{
    		faqVO = FaqService.selectFaq(faqVO);

    	System.out.println(faqVO.getName());
    	HashMap hmLogInfo = new HashMap();
    	hmLogInfo.put("key", faqVO.getId());
    	hmLogInfo.put("title", faqVO.getName());
    	hmLogInfo.put("content", "/faq/detail/view/"+faqVO.getId() );
    	hmLogInfo.put("date", faqVO.getRegDate());
    	
    	JSONObject jobj = JSONObject.fromObject(hmLogInfo);
    		result = jobj.toString();
    	} catch (NullPointerException ex) {
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
     * @param FaqVO - ????????? ????????? ?????? VO
     * @param faqVO - ?????? ???????????? ????????? ?????? VO
     * @param status
     * @return @ModelAttribute("FaqVO") - ????????? ??????
     * @exception Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/faq/detail/{key}", method=RequestMethod.POST)
    public @ResponseBody String selfaqDetail2(
    		HttpServletRequest request
    		,HttpServletResponse response
    		,@RequestParam String key //	??????
    		) throws Exception {
    	String result = "";
    	
    	FaqVO faqVO = new FaqVO();
    	faqVO.setId(key);
    	try{
    		faqVO = FaqService.selectFaq(faqVO);
    		
    		System.out.println(faqVO.getName());
    		HashMap hmLogInfo = new HashMap();
    		hmLogInfo.put("key", faqVO.getId());
    		hmLogInfo.put("title", faqVO.getName());
    		hmLogInfo.put("content", "/faq/detail/view/"+faqVO.getId() );
    		hmLogInfo.put("date", faqVO.getRegDate());
    		
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
