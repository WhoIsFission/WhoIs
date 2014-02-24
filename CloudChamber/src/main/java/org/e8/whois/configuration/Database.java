package org.e8.whois.configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Database {
	
	@Valid
	@NotNull
	@JsonProperty
	private String driverClass;
	
	@Valid
	@NotNull
	@JsonProperty
	private String user;
	
	@Valid
	@NotNull
	@JsonProperty
	private String password;
	
	
	@Valid
	@NotNull
	@JsonProperty
	private String url;


	public String getDriverClass() {
		return driverClass;
	}


	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}

}
