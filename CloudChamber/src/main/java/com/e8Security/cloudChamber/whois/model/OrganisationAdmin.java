package com.e8Security.cloudChamber.whois.model;

import java.util.Date;

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
public class OrganisationAdmin {

	@XmlElement(name="adminHandle")
	private String orgAdminHandle;
	@XmlElement(name="adminName")
	private String orgAdminName;
	@XmlElement(name="adminPhone")
	private String orgAdminPhone;
	@XmlElement(name="adminEmail")
	private String orgAdminEmail;
	@XmlElement(name="adminReference")
	private String orgAdminRef;
	@XmlElement(name="adminAddress")
	private String orgAdminAddress;
	@XmlElement(name="adminFax")
	private String orgAdminFax;
	private boolean isCurrentdata=true;
	private String contactType;
	@XmlElement(name="registrationDate")
	private Date regDate;
	@XmlElement(name="updatedDate")
	private Date updatedDate;


	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
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
	public String getOrgAdminHandle() {
		return orgAdminHandle;
	}
	public void setOrgAdminHandle(String orgAdminHandle) {
		this.orgAdminHandle = orgAdminHandle;
	}
	public String getOrgAdminName() {
		return orgAdminName;
	}
	public void setOrgAdminName(String orgAdminName) {
		this.orgAdminName = orgAdminName;
	}
	public String getOrgAdminPhone() {
		return orgAdminPhone;
	}
	public void setOrgAdminPhone(String orgAdminPhone) {
		this.orgAdminPhone = orgAdminPhone;
	}
	public String getOrgAdminEmail() {
		return orgAdminEmail;
	}
	public void setOrgAdminEmail(String orgAdminEmail) {
		this.orgAdminEmail = orgAdminEmail;
	}
	public String getOrgAdminRef() {
		return orgAdminRef;
	}
	public void setOrgAdminRef(String orgAdminRef) {
		this.orgAdminRef = orgAdminRef;
	}
	public String getOrgAdminAddress() {
		return orgAdminAddress;
	}
	public void setOrgAdminAddress(String orgAdminAddress) {
		this.orgAdminAddress = orgAdminAddress;
	}
	public String getOrgAdminFax() {
		return orgAdminFax;
	}
	public void setOrgAdminFax(String orgAdminFax) {
		this.orgAdminFax = orgAdminFax;
	}	
}