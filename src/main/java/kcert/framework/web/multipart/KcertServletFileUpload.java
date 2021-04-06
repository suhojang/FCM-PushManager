package kcert.framework.web.multipart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.FileItemHeadersSupport;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

public class KcertServletFileUpload extends ServletFileUpload{
	@Override
    public List parseRequest(RequestContext ctx)throws FileUploadException {
        try {
            FileItemIterator iter = getItemIterator(ctx);
            List items = new ArrayList();
            FileItemFactory fac = getFileItemFactory();
            if (fac == null) {
                throw new NullPointerException(
                    "No FileItemFactory has been set.");
            }
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                FileItem fileItem = fac.createItem(item.getFieldName(),
                        item.getContentType(), item.isFormField(),
                        item.getName());
                try {
                    Streams.copy(item.openStream(), fileItem.getOutputStream(),true);
                } catch (FileUploadIOException e) {
                    throw (FileUploadException) e.getCause();
                } catch (IOException e) {
                    throw new IOFileUploadException(
                            "Processing of " + MULTIPART_FORM_DATA
                            + " request failed. " + e.getMessage(), e);
                }
                if (fileItem instanceof FileItemHeadersSupport) {
                    final FileItemHeaders fih = item.getHeaders();
                    ((FileItemHeadersSupport) fileItem).setHeaders(fih);
                }
                items.add(fileItem);
            }
            return items;
        } catch (FileUploadIOException e) {
            throw (FileUploadException) e.getCause();
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage(), e);
        }
    }
}
