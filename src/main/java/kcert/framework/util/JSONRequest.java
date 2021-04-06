package kcert.framework.util;

import javax.servlet.http.HttpServletRequest;
import kcert.framework.exception.DefinedException;
import org.apache.log4j.Logger;
import net.sf.json.JSONObject;

public class JSONRequest extends JSONProcess{
	private transient Logger logger = Logger.getLogger(this.getClass());

	public JSONRequest(String jsonParamName, HttpServletRequest request) throws Exception{
		String jsonString	= request.getParameter(jsonParamName);
    	logger.debug("---------- request json -------------");
    	logger.debug(jsonString);

    	if(jsonString==null || "".equals(jsonString))
			throw new DefinedException("Could not find JSON parameter.");
		try{
			json		= JSONObject.fromObject(jsonString);
		}catch(Exception e){
			throw new DefinedException(e.getMessage());
		}
		headerName	= JSON_REQUEST_HEADER;
		bodyName	= JSON_REQUEST_BODY;
		
		header	= json.getJSONObject(headerName);
		body		= json.getJSONObject(bodyName);
	}
	
	public JSONRequest(String jsonData) throws Exception{
    	logger.debug("---------- request json -------------");
    	logger.debug(jsonData);
		if(jsonData==null || "".equals(jsonData))
			throw new DefinedException("Invalid json data.["+jsonData+"]");
		try{
			json		= JSONObject.fromObject(jsonData);
		}catch(Exception e){
			throw new DefinedException(e.getMessage());
		}
		
		headerName	= JSON_REQUEST_HEADER;
		bodyName	= JSON_REQUEST_BODY;
		
		header	= json.getJSONObject(headerName);
		body		= json.getJSONObject(bodyName);
	}

	public JSONRequest(HttpServletRequest request) throws Exception{
    	String jsonString	= request.getParameter(JSON_REQUEST_PARAM);
    	logger.debug("---------- request json -------------");
    	logger.debug(jsonString);
		if(jsonString==null || "".equals(jsonString))
			throw new DefinedException("Invalid json data.["+jsonString+"]");
		try{
			json		= JSONObject.fromObject(jsonString);
		}catch(Exception e){
			throw new DefinedException(e.getMessage());
		}
		
		headerName	= JSON_REQUEST_HEADER;
		bodyName	= JSON_REQUEST_BODY;
		
		header	= json.getJSONObject(headerName);
		body		= json.getJSONObject(bodyName);
	}
	
	public JSONRequest(String jsonParamName, String headerName,String bodyName,HttpServletRequest request) throws Exception{
    	String jsonString	= request.getParameter(jsonParamName);
    	logger.debug("---------- request json -------------");
    	logger.debug(jsonString);

    	if(jsonString==null || "".equals(jsonString))
			throw new DefinedException("Could not find JSON parameter.");
		try{
			json		= JSONObject.fromObject(jsonString);
		}catch(Exception e){
			throw new DefinedException(e.getMessage());
		}
		
		this.headerName	= headerName;
		this.bodyName	= bodyName;
		
		header	= json.getJSONObject(headerName);
		body		= json.getJSONObject(bodyName);
	}
	
	public JSONRequest(String jsonData, String headerName,String bodyName) throws Exception{
    	logger.debug("---------- request json -------------");
    	logger.debug(jsonData);
		if(jsonData==null || "".equals(jsonData))
			throw new DefinedException("Invalid json data.["+jsonData+"]");
		try{
			json		= JSONObject.fromObject(jsonData);
		}catch(Exception e){
			throw new DefinedException(e.getMessage());
		}
		
		this.headerName	= headerName;
		this.bodyName	= bodyName;
		
		header	= json.getJSONObject(headerName);
		body		= json.getJSONObject(bodyName);
	}

	public JSONRequest(String headerName,String bodyName,HttpServletRequest request) throws Exception{
    	String jsonString	= request.getParameter(JSON_REQUEST_PARAM);
    	logger.debug("---------- request json -------------");
    	logger.debug(jsonString);
		if(jsonString==null || "".equals(jsonString))
			throw new DefinedException("Invalid json data.["+jsonString+"]");
		try{
			json		= JSONObject.fromObject(jsonString);
		}catch(Exception e){
			throw new DefinedException(e.getMessage());
		}
		this.headerName	= headerName;
		this.bodyName	= bodyName;
		
		header	= json.getJSONObject(headerName);
		body		= json.getJSONObject(bodyName);
	}
	
}
