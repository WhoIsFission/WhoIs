package org.e8.whois.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OrganisationTech {

	@XmlElement(name="techHandle")
	private String orgTechHandle;
	@XmlElement(name="techName")
	private String orgTechName;
	@XmlElement(name="techPhone")
	private String orgTechPhone;
	@XmlElement(name="techEmail")
	private String orgTechEmail;
	@XmlElement(name="techReference")
	private String orgTechRef;
	@XmlElement(name="techFax")
	private String orgTechFax;
	@XmlElement(name="techAddress")
	private String orgTechAdrress;
	private boolean isCurrentdata=true;

	
	public boolean isCurrentdata() {
		return isCurrentdata;
	}
	public void setCurrentdata(boolean isCurrentdata) {
		this.isCurrentdata = isCurrentdata;
	}
	public String getOrgTechAdrress() {
		return orgTechAdrress;
	}
	public void setOrgTechAdrress(String orgTechAdrress) {
		this.orgTechAdrress = orgTechAdrress;
	}
	public String getOrgTechHandle() {
		return orgTechHandle;
	}
	public void setOrgTechHandle(String orgTechHandle) {
		this.orgTechHandle = orgTechHandle;
	}
	public String getOrgTechName() {
		return orgTechName;
	}
	public void setOrgTechName(String orgTechName) {
		this.orgTechName = orgTechName;
	}
	public String getOrgTechPhone() {
		return orgTechPhone;
	}
	public void setOrgTechPhone(String orgTechPhone) {
		this.orgTechPhone = orgTechPhone;
	}
	public String getOrgTechEmail() {
		return orgTechEmail;
	}
	public void setOrgTechEmail(String orgTechEmail) {
		this.orgTechEmail = orgTechEmail;
	}
	public String getOrgTechRef() {
		return orgTechRef;
	}
	public void setOrgTechRef(String orgTechRef) {
		this.orgTechRef = orgTechRef;
	}
	public String getOrgTechFax() {
		return orgTechFax;
	}
	public void setOrgTechFax(String orgTechFax) {
		this.orgTechFax = orgTechFax;
	}
		
	
}
