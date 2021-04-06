package kcert.framework.tag;

import egovframework.rte.ptl.mvc.tags.ui.pagination.AbstractPaginationRenderer;

public class ImagePaginationRenderer extends AbstractPaginationRenderer {
	
	public ImagePaginationRenderer() {
		firstPageLabel 		= "<a href=\"#\" class=\"first\" onclick=\"{0}({1});\"><img src=\"/images/btn_paging_1.png\" alt=\"처음\" /></a>"; 
		previousPageLabel 	= "<a href=\"#\" class=\"prev\" onclick=\"{0}({1});\"><img src=\"/images/btn_paging_2.png\" alt=\"이전\" /></a>";
		currentPageLabel	= "<a href=\"#\" class=\"on\"><b>{0}</b></a>";
		otherPageLabel		= "<a href=\"#\" onclick=\"{0}({1});\">{2}</a>";
		nextPageLabel 		= "<a href=\"#\" class=\"next\" onclick=\"{0}({1});\"><img src=\"/images/btn_paging_3.png\" alt=\"다음\" /></a>";
		lastPageLabel 		= "<a href=\"#\" class=\"last\" onclick=\"{0}({1});\"><img src=\"/images/btn_paging_4.png\" alt=\"마지막\" /></a>";
	}
}
