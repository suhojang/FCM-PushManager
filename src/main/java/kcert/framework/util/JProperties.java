package kcert.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class JProperties {
	private transient Logger logger = Logger.getLogger(this.getClass());
	
	public JProperties(){}
	
	/**
	 * FCM url을 읽어온다.
	 * @return
	 * @throws Exception
	 */
	public String getFcmUrl() throws IOException {
		return getLoadProperties().getProperty("fcm.url");
	}
	
	/**
	 * 업로드 url을 읽어온다.
	 * @return
	 * @throws IOException
	 */
	public String getUploadUrl() throws IOException {
		return getLoadProperties().getProperty("upload.url");
	}
	
	/**
	 * 배치 url를 읽어온다.
	 * @return
	 * @throws IOException
	 */
	public String getBatchUrl() throws IOException {
		return getLoadProperties().getProperty("batch.url");
	}
	
	/**
	 * properties 파일 로드
	 * @return
	 * @throws Exception
	 */
	private Properties getLoadProperties() {
		Properties properties	= null;
		try {
			InputStream is	= this.getClass().getClassLoader().getResourceAsStream("properties/config.properties");
			properties = new Properties();
			properties.load(is);
		} catch (IOException e) {
			logger.error(MakeUtil.stackTrace(e));
		}
		return properties;
	}
}
