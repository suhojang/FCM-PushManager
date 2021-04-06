package kcert.framework.vo;

public class SmsVo {
	private String cmid;		//yyyyMMddHHmmssS + UserID
	private String cinfo;		//push
	private int msgType;		//0
	private int status;			//0
	private String destPhone;	//받는사람
	private String sendPhone;	//보내는사람 (02-2670-0355)
	private String msgBody;		//메시지
  
	public String getCmid() {
		return cmid;
	}
	public void setCmid(String cmid) {
		this.cmid = cmid;
	}
	public String getCinfo() {
		return cinfo;
	}
	public void setCinfo(String cinfo) {
		this.cinfo = cinfo;
	}
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDestPhone() {
		return destPhone;
	}
	public void setDestPhone(String destPhone) {
		this.destPhone = destPhone;
	}
	public String getSendPhone() {
		return sendPhone;
	}
	public void setSendPhone(String sendPhone) {
		this.sendPhone = sendPhone;
	}
	public String getMsgBody() {
		return msgBody;
	}
	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
}
