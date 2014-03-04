package org.e8.whois.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * Model class for Organisation Abuse.
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class OrganisationAbuse {

	@XmlElement(name="abuseHandle")
	private String orgAbuseHandle;
	@XmlElement(name="abuseName")
	private String orgAbuseName;
	@XmlElement(name="abusePhone")
	private String orgAbusePhone;
	@XmlElement(name="abuseEmail")
	private String orgAbuseEmail;
	@XmlElement(name="abuseReference")
	private String orgAbuseRef;
	@XmlElement(name="abuseFax")
	private String orgAbuseFax;
	@XmlElement(name="abuseAddress")
	private String orgAbuseAddress;
	private boolean isCurrentdata=true;
	private String contactType;
	
	
	public String getContactType() {
		return contactType;
	}	
	public void setContactType(String contactType) {
		this.contactType = contactType;
	}
	
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
	public String getOrgAbuseFax() {
		return orgAbuseFax;
	}
	public void setOrgAbuseFax(String orgAbuseFax) {
		this.orgAbuseFax = orgAbuseFax;
	}	
}
