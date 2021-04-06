package kcert.admin.vo;

import java.io.Serializable;

public class LoginVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String adminf_id;
	private String adminf_pwd;
	private String adminf_nm;
	private String adminf_grd;
	private String adminf_delyn;
	private String adminf_rusr;
	private String adminf_rdt;
	private String adminf_udt;
	
	public String getAdminf_id() {
		return adminf_id;
	}

	public void setAdminf_id(String adminf_id) {
		this.adminf_id = adminf_id;
	}

	public String getAdminf_pwd() {
		return adminf_pwd;
	}

	public void setAdminf_pwd(String adminf_pwd) {
		this.adminf_pwd = adminf_pwd;
	}

	public String getAdminf_nm() {
		return adminf_nm;
	}

	public void setAdminf_nm(String adminf_nm) {
		this.adminf_nm = adminf_nm;
	}


	public String getAdminf_grd() {
		return adminf_grd;
	}

	public void setAdminf_grd(String adminf_grd) {
		this.adminf_grd = adminf_grd;
	}

	public String getAdminf_delyn() {
		return adminf_delyn;
	}

	public void setAdminf_delyn(String adminf_delyn) {
		this.adminf_delyn = adminf_delyn;
	}

	public String getAdminf_rusr() {
		return adminf_rusr;
	}

	public void setAdminf_rusr(String adminf_rusr) {
		this.adminf_rusr = adminf_rusr;
	}

	public String getAdminf_rdt() {
		return adminf_rdt;
	}

	public void setAdminf_rdt(String adminf_rdt) {
		this.adminf_rdt = adminf_rdt;
	}

	public String getAdminf_udt() {
		return adminf_udt;
	}

	public void setAdminf_udt(String adminf_udt) {
		this.adminf_udt = adminf_udt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
