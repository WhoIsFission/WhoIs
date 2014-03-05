package com.e8security.cloudchamber.whois.configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

/**
 * WhoIsConfiguration mapping for configuration.yml
 *
 */
public class WhoIsConfiguration extends Configuration{

	@Valid
	@NotNull
	@JsonProperty
	private String hello;
	
    @Valid
@NotNull
@JsonProperty
	private Database database;
    

    @DefaultValue("2")
    @JsonProperty
    private Integer connectionPool;

    @DefaultValue("4")
    @JsonProperty
    private Integer threads;

	public String getHello() {
		return hello;
	}

	public void setHello(String hello) {
		this.hello = hello;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public Integer getConnectionPool() {
		return connectionPool;
	}

	public void setConnectionPool(Integer connectionPool) {
		this.connectionPool = connectionPool;
	}

	public Integer getThreads() {
		return threads;
	}

	public void setThreads(Integer threads) {
		this.threads = threads;
	}
    
    
    
	
	
	
}
