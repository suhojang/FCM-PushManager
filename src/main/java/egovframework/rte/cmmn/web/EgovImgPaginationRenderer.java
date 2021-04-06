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
package egovframework.rte.cmmn.web;

import java.text.MessageFormat;

import egovframework.rte.ptl.mvc.tags.ui.pagination.AbstractPaginationRenderer;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**  
 * @Class Name : ImagePaginationRenderer.java
 * @Description : ImagePaginationRenderer Class
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
public class EgovImgPaginationRenderer extends AbstractPaginationRenderer {
	
    /**
    * PaginationRenderer
	* 
    * @see 개발프레임웍크 실행환경 개발팀
    */
	@SuppressWarnings("unused")
	public EgovImgPaginationRenderer() {

		//String strWebDir = "/egovframework.guideprogram.basic/images/egovframework/cmmn/"; // localhost
		String strWebDir = "/###ARTIFACT_ID###/images/egovframework/cmmn/";
		
		String Tag_UL_START = "<ul class=\"pagination\">";
		String Tag_UL_END = "</ul>";

		firstPageLabel = Tag_UL_START+"<li class=\"\"><a href=\"#\" onclick=\"{0}({1}); return false;\" title=\"처음\">" +
							"&laquo;</a></li>"; 
        previousPageLabel = "<li class=\"\"><a href=\"#\" onclick=\"{0}({1}); return false;\" title=\"이전\">" +
        						"&lsaquo;</a></li>";
        currentPageLabel = "<li class=\"active\"><a href=\"#\">{0}<span class=\"sr-only\">(current)</span></a></li>";
        otherPageLabel = "<li class=\"\"><a href=\"#\" onclick=\"{0}({1}); return false;\">{2}</a></li>";
        nextPageLabel = "<li class=\"\"><a href=\"#\" onclick=\"{0}({1}); return false;\" title=\"다음\">" +
        					"&rsaquo;</a></li>";
        lastPageLabel = "<li class=\"\"><a href=\"#\" onclick=\"{0}({1}); return false;\" title=\"마지막\">" +
        					"&raquo;</a></li>"
        					+Tag_UL_END;
        
	}
	public String renderPagination(PaginationInfo paginationInfo,String jsFunction){
		
		StringBuffer strBuff = new StringBuffer();
		
		int firstPageNo = paginationInfo.getFirstPageNo();
		int firstPageNoOnPageList = paginationInfo.getFirstPageNoOnPageList();
		int totalPageCount = paginationInfo.getTotalPageCount();
		int pageSize = paginationInfo.getPageSize();
		int lastPageNoOnPageList = paginationInfo.getLastPageNoOnPageList();
		int currentPageNo = paginationInfo.getCurrentPageNo();
		int lastPageNo = paginationInfo.getLastPageNo();
		
		//if(totalPageCount > pageSize){
			if(firstPageNoOnPageList > pageSize){
				strBuff.append(MessageFormat.format(firstPageLabel,new Object[]{jsFunction,Integer.toString(firstPageNo)}));
				strBuff.append(MessageFormat.format(previousPageLabel,new Object[]{jsFunction,Integer.toString(firstPageNoOnPageList-1)}));
			}else{
				strBuff.append(MessageFormat.format(firstPageLabel,new Object[]{jsFunction,Integer.toString(firstPageNo)}));
				strBuff.append(MessageFormat.format(previousPageLabel,new Object[]{jsFunction,Integer.toString(firstPageNo)}));
			}
		//}
		
		for(int i=firstPageNoOnPageList;i<=lastPageNoOnPageList;i++){
			if(i==currentPageNo){
				strBuff.append(MessageFormat.format(currentPageLabel,new Object[]{Integer.toString(i)}));
			}else{
				strBuff.append(MessageFormat.format(otherPageLabel,new Object[]{jsFunction,Integer.toString(i),Integer.toString(i)}));
			}
		}
		
		//if(totalPageCount > pageSize){
			if(lastPageNoOnPageList < totalPageCount){
				strBuff.append(MessageFormat.format(nextPageLabel,new Object[]{jsFunction,Integer.toString(firstPageNoOnPageList+pageSize)}));
				strBuff.append(MessageFormat.format(lastPageLabel,new Object[]{jsFunction,Integer.toString(lastPageNo)}));
			}else{
				strBuff.append(MessageFormat.format(nextPageLabel,new Object[]{jsFunction,Integer.toString(lastPageNo)}));
				strBuff.append(MessageFormat.format(lastPageLabel,new Object[]{jsFunction,Integer.toString(lastPageNo)}));
			}
		//}
		return strBuff.toString();
	}
}
