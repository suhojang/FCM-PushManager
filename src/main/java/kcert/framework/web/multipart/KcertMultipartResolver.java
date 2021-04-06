package kcert.framework.web.multipart;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class KcertMultipartResolver extends CommonsMultipartResolver{
	public KcertMultipartResolver() {
		super();
	}

	public KcertMultipartResolver(ServletContext servletContext) {
		super(servletContext);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
		String encoding			= determineEncoding(request);
		FileUpload fileUpload	= prepareFileUpload(encoding);
		try {
			List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(new ServletRequestContext(request));
			return parseFileItems(fileItems, encoding);
		}catch (FileUploadBase.SizeLimitExceededException ex) {
			throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
		}catch (Exception ex) {
			ex.printStackTrace();
			throw new MultipartException("Could not parse multipart servlet request", ex);
		}
	}
}
