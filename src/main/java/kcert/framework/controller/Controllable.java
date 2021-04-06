package kcert.framework.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kcert.framework.util.FCMsender;
import kcert.framework.util.JXL;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.kcert.io.JOutputStream;

public abstract class Controllable {
	protected transient Logger logger = Logger.getLogger(this.getClass());
	//seed 128 암복호화 키
	public static final String CRYPT_KEY				= "$____KCERT_#PUSH_KEY____$";
	
	protected static String langByte = "8859_1";
	protected static String langMode = "utf-8";
	protected static String langModeAnsi = "euc-kr";
	
	public static final Map<String,String> CONTENT_TYPE_IMAGE	= new HashMap<String,String>();
	static{
		CONTENT_TYPE_IMAGE.put("3ds","image/x-3ds");
		CONTENT_TYPE_IMAGE.put("art","image/x-jg");
		CONTENT_TYPE_IMAGE.put("bmp","image/bmp");
		CONTENT_TYPE_IMAGE.put("btif","image/prs.btif");
		CONTENT_TYPE_IMAGE.put("cgm","image/cgm");
		CONTENT_TYPE_IMAGE.put("cmx","image/x-cmx");
		CONTENT_TYPE_IMAGE.put("dib","image/bmp");
		CONTENT_TYPE_IMAGE.put("djv","image/vnd.djvu");
		CONTENT_TYPE_IMAGE.put("djvu","image/vnd.djvu");
		CONTENT_TYPE_IMAGE.put("dmg","application/x-apple-diskimage");
		CONTENT_TYPE_IMAGE.put("dwg","image/vnd.dwg");
		CONTENT_TYPE_IMAGE.put("dxf","image/vnd.dxf");
		CONTENT_TYPE_IMAGE.put("fbs","image/vnd.fastbidsheet");
		CONTENT_TYPE_IMAGE.put("fh","image/x-freehand");
		CONTENT_TYPE_IMAGE.put("fh4","image/x-freehand");
		CONTENT_TYPE_IMAGE.put("fh5","image/x-freehand");
		CONTENT_TYPE_IMAGE.put("fh7","image/x-freehand");
		CONTENT_TYPE_IMAGE.put("fhc","image/x-freehand");
		CONTENT_TYPE_IMAGE.put("fpx","image/vnd.fpx");
		CONTENT_TYPE_IMAGE.put("fst","image/vnd.fst");
		CONTENT_TYPE_IMAGE.put("g3","image/g3fax");
		CONTENT_TYPE_IMAGE.put("gif","image/gif");
		CONTENT_TYPE_IMAGE.put("ico","image/x-icon");
		CONTENT_TYPE_IMAGE.put("ief","image/ief");
		CONTENT_TYPE_IMAGE.put("iso","application/x-iso9660-image");
		CONTENT_TYPE_IMAGE.put("jpe","image/jpeg");
		CONTENT_TYPE_IMAGE.put("jpeg","image/jpeg");
		CONTENT_TYPE_IMAGE.put("jpg","image/jpeg");
		CONTENT_TYPE_IMAGE.put("ktx","image/ktx");
		CONTENT_TYPE_IMAGE.put("mac","image/x-macpaint");
		CONTENT_TYPE_IMAGE.put("mdi","image/vnd.ms-modi");
		CONTENT_TYPE_IMAGE.put("mmr","image/vnd.fujixerox.edmics-mmr");
		CONTENT_TYPE_IMAGE.put("npx","image/vnd.net-fpx");
		CONTENT_TYPE_IMAGE.put("odi","application/vnd.oasis.opendocument.image");
		CONTENT_TYPE_IMAGE.put("oti","application/vnd.oasis.opendocument.image-template");
		CONTENT_TYPE_IMAGE.put("pbm","image/x-portable-bitmap");
		CONTENT_TYPE_IMAGE.put("pct","image/pict");
		CONTENT_TYPE_IMAGE.put("pcx","image/x-pcx");
		CONTENT_TYPE_IMAGE.put("pgm","image/x-portable-graymap");
		CONTENT_TYPE_IMAGE.put("pic","image/pict");
		CONTENT_TYPE_IMAGE.put("pict","image/pict");
		CONTENT_TYPE_IMAGE.put("png","image/png");
		CONTENT_TYPE_IMAGE.put("pnm","image/x-portable-anymap");
		CONTENT_TYPE_IMAGE.put("pnt","image/x-macpaint");
		CONTENT_TYPE_IMAGE.put("ppm","image/x-portable-pixmap");
		CONTENT_TYPE_IMAGE.put("psd","image/vnd.adobe.photoshop");
		CONTENT_TYPE_IMAGE.put("qti","image/x-quicktime");
		CONTENT_TYPE_IMAGE.put("qtif","image/x-quicktime");
		CONTENT_TYPE_IMAGE.put("ras","image/x-cmu-raster");
		CONTENT_TYPE_IMAGE.put("rgb","image/x-rgb");
		CONTENT_TYPE_IMAGE.put("rlc","image/vnd.fujixerox.edmics-rlc");
		CONTENT_TYPE_IMAGE.put("sgi","image/sgi");
		CONTENT_TYPE_IMAGE.put("sid","image/x-mrsid-image");
		CONTENT_TYPE_IMAGE.put("svg","image/svg+xml");
		CONTENT_TYPE_IMAGE.put("svgz","image/svg+xml");
		CONTENT_TYPE_IMAGE.put("t3","application/x-t3vm-image");
		CONTENT_TYPE_IMAGE.put("tga","image/x-tga");
		CONTENT_TYPE_IMAGE.put("tif","image/tiff");
		CONTENT_TYPE_IMAGE.put("tiff","image/tiff");
		CONTENT_TYPE_IMAGE.put("uvg","image/vnd.dece.graphic");
		CONTENT_TYPE_IMAGE.put("uvi","image/vnd.dece.graphic");
		CONTENT_TYPE_IMAGE.put("uvvg","image/vnd.dece.graphic");
		CONTENT_TYPE_IMAGE.put("uvvi","image/vnd.dece.graphic");
		CONTENT_TYPE_IMAGE.put("wbmp","image/vnd.wap.wbmp");
		CONTENT_TYPE_IMAGE.put("wdp","image/vnd.ms-photo");
		CONTENT_TYPE_IMAGE.put("webp","image/webp");
		CONTENT_TYPE_IMAGE.put("xbm","image/x-xbitmap");
		CONTENT_TYPE_IMAGE.put("xif","image/vnd.xiff");
		CONTENT_TYPE_IMAGE.put("xpm","image/x-xpixmap");
		CONTENT_TYPE_IMAGE.put("xwd","image/x-xwindowdump");
		CONTENT_TYPE_IMAGE.put("pdf","application/pdf");
		CONTENT_TYPE_IMAGE.put("insfimg","image/png");
	}
	
	public static final Map<String,String> CONTENT_MENU_CODE	= new HashMap<String,String>();
	static{
		CONTENT_MENU_CODE.put("BRD", 	"MN_001");
		CONTENT_MENU_CODE.put("MSG", 	"MN_002");
		CONTENT_MENU_CODE.put("STA", 	"MN_003");
		CONTENT_MENU_CODE.put("APP", 	"MN_004");
		CONTENT_MENU_CODE.put("GRP", 	"MN_005_001");
		CONTENT_MENU_CODE.put("ADM", 	"MN_005_002");
		CONTENT_MENU_CODE.put("IPM", 	"MN_005_003");
		CONTENT_MENU_CODE.put("SVR", 	"MN_005_004");
	}
	
	protected static final String OTP_PACKAGE		= "kr.or.kcomwel.officeapp";
	protected static final String OTP_PACKAGE_NM	= "스마트포털 모바일앱";
	
	protected static final String SMS_CMID			= new java.text.SimpleDateFormat("yyyyMMddHHmmssS").format(new java.util.Date());
	protected static final String SMS_CINFO			= "push";
	protected static final String SMS_MSGTYPE		= "0";
	protected static final String SMS_STATUS		= "0";
	protected static final String SMS_SENDPHONE		= "02-2670-0355";
	
	protected static final String TITLE_EMERGENCY	= "[긴급]";
	protected static final String TITLE_PERSONNEL	= "[인사]";
	protected static final String TITLE_FAMILYEVENT	= "[경조사]";
	
	protected static final FCMsender sender	= FCMsender.getInstance();
	
    /**
	 * ServletOutputStream 으로 이미지를 직접 write 하는 방식을 취한다.
	 * 이미지의 경로가 보안사항일 경우 사용한다.
	 * 
	 * @param imagePathP 다운로드할 이미지경로
	 * @param response HttpServletResponse
	 * @exception IOException
	 */
    public void imageView( String imagePathP , HttpServletResponse response ) throws Exception{
    	String imagePath	= StringUtils.replace(imagePathP, "\\", File.separator);
    	imagePath			= StringUtils.replace(imagePath, "/", File.separator);
    	
    	String extType		= "$%^&*()!@#$";
    	if(imagePath.lastIndexOf(".")>=0){
    		extType	= imagePath.substring(imagePath.lastIndexOf(".")+1);
    	}

    	String contentType	= "";
    	response.setStatus(HttpServletResponse.SC_OK);
    	contentType	= CONTENT_TYPE_IMAGE.get(extType.toLowerCase());
    	if(contentType==null)
    		throw new Exception("The file is not an image.");
    	
    	File imgFile	= new File(imagePath);
		if(!imgFile.exists() || imgFile.isDirectory())
			throw new Exception("["+imagePath+"] 파일을 찾을 수 없습니다.");

    	imageView(new FileInputStream(imgFile),response,contentType);
    }
    
    /**
	 * ServletOutputStream 으로 이미지를 직접 write 하는 방식을 취한다.
	 * 이미지의 경로가 보안사항일 경우 사용한다.
	 * 
	 * @param is 다운로드할 이미지 stream
	 * @param response 
	 * @param imageContentType 이미지 타입
	 * @exception IOException
	 */
    public void imageView( InputStream is , HttpServletResponse response, String imageContentType ) throws Exception{
    	response.setContentType(imageContentType);
    	write(is , response.getOutputStream());
	}


    /**
	 * 파일다운로드를 담당한다.
	 * @param filePath 다운로드할 파일경로
	 * @param fileName 저장될 파일명
	 * @param response HttpServletResponse
	 * @exception IOException
	 */
    public void fileDown( String filePath , String fileName, HttpServletRequest request, HttpServletResponse response ) throws Exception{
        String browser = getBrowser(request);
        String encodedFilename = "";
        
        if (browser.equals("MSIE")) {
            encodedFilename = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Firefox")) {
            encodedFilename = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Opera")) {
            encodedFilename = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < fileName.length(); i++) {
                char c = fileName.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        } else {
            encodedFilename = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        }
        response.setStatus(HttpServletResponse.SC_OK);
    	response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename="+encodedFilename);
        response.setHeader("Content-Description", "JSP Generated Data");
    	
		File fFile	= new File(filePath);
		if(!fFile.exists() || fFile.isDirectory()){
			return;
		}
		write(new FileInputStream(fFile),response.getOutputStream());
	}

    /**
	 * OutputStream 으로 write.
	 * 
	 * @param is 입력 stream
	 * @param os 출력 stream
	 * @exception IOException
	 */
    public static void write( InputStream is , OutputStream os ) throws IOException{
    	if(is==null || os==null){
    		return;
    	}
    	byte[] buf	= new byte[1024];
    	int iReadSize			= 0;
    	try{
    		while( (iReadSize=is.read(buf))!=-1 ){
    			os.write(buf,0,iReadSize);
    		}
    		os.flush();
    	}catch(IOException ie){
    		throw ie;
    	}finally{
    		try{if(is!=null){is.close();}}catch(IOException ie){}
    		try{if(os!=null){os.close();}}catch(IOException ie){}
    	}
    }    
    
    /**
     * 브라우저 구분 얻기.
     * @param request
     * @return
     */
    private static String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if(header==null){
            return "MSIE";
        }else if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        }
        return "MSIE";
    }
    
    /**
     * 엑셀다운로드
     * @param request
     * @param response
     * @param jxl
     * @param fileNm
     * @return
     */
    public String excelDown(HttpServletRequest request,HttpServletResponse response,JXL jxl, String fileNm){
    	FileInputStream fis	= null;
    	try{
    		String fileName	= fileNm+"_"+new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date())+".xls";
    		byte[] bytes	= jxl.getBytes();
			response.setHeader("Content-Disposition","attachment;filename="+URLEncoder.encode(fileName,"UTF-8")+";");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
			response.getOutputStream().flush();
    	}catch(Exception e){
    		
    	}finally{
    		try{if(fis!=null)fis.close();}catch(Exception ex){}
    	}
    	return null;
    }

    /**
     * csv다운로드
     * @param request
     * @param response
     * @param out
     * @param fileName
     * @return
     */
    public String csvDown(HttpServletRequest request,HttpServletResponse response,JOutputStream out, String fileNm){
    	try{
    		String fileName	= fileNm+"_"+new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date())+".csv";
    		byte[] bytes	= out.getBytes();
			response.setHeader("Content-Disposition","attachment;filename="+URLEncoder.encode(fileName,"UTF-8")+";");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
			response.getOutputStream().flush();
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    	}
    	return null;
	}

}
