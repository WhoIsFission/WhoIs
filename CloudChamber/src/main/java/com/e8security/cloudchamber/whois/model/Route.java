package com.e8security.cloudchamber.whois.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/***
 * 
 * Route is used to capture route information for ORIGINASN
 * 
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Route {
	@XmlElement
	private String description;
	@XmlElement
	private String originASN;
	@XmlElement
	private String country;
	@XmlElement
	private Date updatedDate;
	@XmlElement
	private Date regDate;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOriginASN() {
		return originASN;
	}
	public void setOriginASN(String originASN) {
		this.originASN = originASN;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	
	

}
