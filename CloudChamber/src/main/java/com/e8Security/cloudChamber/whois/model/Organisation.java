package org.e8.whois.model;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model class for Organisation.
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Organisation {

	@XmlElement(name="name")
	private String orgName;
	@XmlElement(name="Id")
	private String orgId;
	@XmlElement(name="address")
	private String address;
	@XmlElement(name="city")
	private String city;
	@XmlElement(name="state")
	private String state;
	@XmlElement(name="postalcode")
	private String postalCode;
	@XmlElement(name="country")
	private String country;
	@XmlElement(name="registrationDate")
	private Date regDate;
	@XmlElement(name="updatedDate")
	private Date updatedDate;
	@XmlElement(name="phone")
	private String phoneNo;
	@XmlElement(name="fax")
	private String faxNo;
	@XmlElement(name="reference")
	private String ref;
	@XmlElement(name="email")
	private String email;

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
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
	public String getFaxNo() {
		return faxNo;
	}
	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	
}
