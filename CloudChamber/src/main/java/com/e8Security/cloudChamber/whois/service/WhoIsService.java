package com.e8Security.cloudChamber.whois.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.e8Security.cloudChamber.whois.client.RegistryLoader;
import com.e8Security.cloudChamber.whois.configuration.WhoIsConfiguration;
import com.e8Security.cloudChamber.whois.resource.IPResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class WhoIsService extends Service<WhoIsConfiguration>{

	private final static Logger logger=LoggerFactory.getLogger(WhoIsService.class);

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled())
			logger.debug("Start running services.");

		new WhoIsService().run(args);
	}

	@Override
	public void initialize(Bootstrap<WhoIsConfiguration> aBootStrap) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled())
			logger.debug("Initializing WhoIs service.");

		aBootStrap.setName("WhoIs Service for IP");
	}

	@Override
	public void run(WhoIsConfiguration conf, Environment env) throws Exception {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled())
			logger.debug("Build cache and adding resources to environment varibale.");
//build cache
		BuildCache.cacheBuild(conf);
		// Load RIR registry data
		RegistryLoader.getInstance();
		
		env.addResource(new IPResource(conf));


	}

}
