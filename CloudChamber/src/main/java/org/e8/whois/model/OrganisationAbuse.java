package org.e8.whois.model;

public class OrganisationAbuse {

	private String orgAbuseHandle;
	private String orgAbuseName;
	private String orgAbusePhone;
	private String orgAbuseEmail;
	private String orgAbuseRef;
	private String orgAbuseAddress;
	private boolean isCurrentdata=true;

	
	public boolean isCurrentdata() {
		return isCurrentdata;
	}
	public void setCurrentdata(boolean isCurrentdata) {
		this.isCurrentdata = isCurrentdata;
	}
	public String getOrgAbuseAddress() {
		return orgAbuseAddress;
	}
	public void setOrgAbuseAddress(String orgAbuseAddress) {
		this.orgAbuseAddress = orgAbuseAddress;
	}
	public String getOrgAbuseHandle() {
		return orgAbuseHandle;
	}
	public void setOrgAbuseHandle(String orgAbuseHandle) {
		this.orgAbuseHandle = orgAbuseHandle;
	}
	public String getOrgAbuseName() {
		return orgAbuseName;
	}
	public void setOrgAbuseName(String orgAbuseName) {
		this.orgAbuseName = orgAbuseName;
	}
	public String getOrgAbusePhone() {
		return orgAbusePhone;
	}
	public void setOrgAbusePhone(String orgAbusePhone) {
		this.orgAbusePhone = orgAbusePhone;
	}
	public String getOrgAbuseEmail() {
		return orgAbuseEmail;
	}
	public void setOrgAbuseEmail(String orgAbuseEmail) {
		this.orgAbuseEmail = orgAbuseEmail;
	}
	public String getOrgAbuseRef() {
		return orgAbuseRef;
	}
	public void setOrgAbuseRef(String orgAbuseRef) {
		this.orgAbuseRef = orgAbuseRef;
	}
		
	
	
}
