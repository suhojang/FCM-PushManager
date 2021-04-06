package kcert.framework.util;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;

import kcert.framework.exception.DefinedException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class JSONResponse  extends JSONProcess{
	private transient Logger logger = Logger.getLogger(this.getClass());
	
	private HttpServletResponse response;
	private SimpleDateFormat sf	= new SimpleDateFormat("yyyyMMddHHmmss");
	
	public JSONResponse(HttpServletResponse response) throws Exception{
		this.response	= response;
		init(JSON_REQUEST_HEADER,JSON_REQUEST_BODY);
	}
	public JSONResponse(String headerName,String bodyName,HttpServletResponse response) throws Exception{
		this.response	= response;
		init(headerName,bodyName);
	}
	
	private void init(String headerName,String bodyName){
		this.headerName	= headerName;
		this.bodyName		= bodyName;
		
		json		= new JSONObject();
		header	= new JSONObject();
		body		= new JSONObject();
		json.put(headerName, header);
		json.put(bodyName, body);
		setHeaderValue("H_RCV_TM",sf.format(Calendar.getInstance().getTime()));
	}
	
	public void response() throws Exception{
		response(null);
	}
	public void response(JSONRequest jRequest) throws Exception{
		setHeaderValue("H_RES_TM",sf.format(Calendar.getInstance().getTime()));
		
		if(jRequest!=null){
			setHeaderValue("H_CN_TP",jRequest.getHeaderString("H_CN_TP"));
			setHeaderValue("H_CMD_ID",jRequest.getHeaderString("H_CMD_ID"));
			setHeaderValue("H_REQ_TM",jRequest.getHeaderString("H_REQ_TM"));
			setHeaderValue("H_REQ_ZN",jRequest.getHeaderString("H_REQ_ZN"));
		}
		
		if(getHeaderString(JSON_RESULT_CODE)==null || "".equals(getHeaderString(JSON_RESULT_CODE)))
			success();
		
		json.put(headerName, header);
		json.put(bodyName, body);
		
    	response.setStatus(HttpServletResponse.SC_OK);
    	response.setHeader("Content-Type", CONTENT_TYPE_JSON);
    	logger.debug("---------- response json -------------");
    	logger.debug(json.toString());
		
    	if(response==null)
    		throw new Exception("HttpServletResponse is null.");
   		write(json.toString(),response.getWriter());
	}
	public void err() throws Exception{
		err("");
	}
	public void err(Throwable e) throws Exception{
		logger.error("", e);
    	if(e instanceof DefinedException)
    		err(e.getMessage());
    	else
    		err(JSON_RESULT_MESSAGE_DEFAULT);
	}
	public void err(String message) throws Exception{
		setHeaderValue(JSON_RESULT_CODE, JSON_RESULT_CODE_ERR);
		setHeaderValue(JSON_RESULT_MESSAGE, message);
	}
	
	public void success() throws Exception{
		success("");
	}
	public void success(String message) throws Exception{
		setHeaderValue(JSON_RESULT_CODE, JSON_RESULT_CODE_SUCC);
		setHeaderValue(JSON_RESULT_MESSAGE, message);
	}
	
	public void warn() throws Exception{
		warn("");
	}
	public void warn(String message) throws Exception{
		setHeaderValue(JSON_RESULT_CODE, JSON_RESULT_CODE_WARN);
		setHeaderValue(JSON_RESULT_MESSAGE, message);
	}
	
    /**
	 * Writer 으로 write.
	 * 
	 * @param value 출력 string
	 * @param writer Writer
	 * @exception IOException
	 */
    private static void write( String value , Writer writer ) throws Exception{
    	try{
    		if(writer==null)
    			throw new Exception("Writer is null.");
        	writer.write(value);
        	writer.flush();
    	}catch(Exception e){
    		throw e;
    	}finally{
    		try{if(writer!=null){writer.close();}}catch(IOException ie){}
    	}
    }    
}
