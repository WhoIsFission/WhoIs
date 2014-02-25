package org.e8.whois.service;

import java.util.HashMap;
import java.util.Map;

import org.e8.whois.configuration.WhoIsConfiguration;
import org.e8.whois.resource.IPResource;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class WhoIsService extends Service<WhoIsConfiguration>{
	
	Map<String,String> cache=new HashMap<String,String>();

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
new WhoIsService().run(args);
	}

	@Override
	public void initialize(Bootstrap<WhoIsConfiguration> aBootStrap) {
		// TODO Auto-generated method stub
		aBootStrap.setName("WhoIs Service for IP");
	}

	@Override
	public void run(WhoIsConfiguration conf, Environment env) throws Exception {
		// TODO Auto-generated method stub
		env.addResource(new IPResource(cache));
	}

}
