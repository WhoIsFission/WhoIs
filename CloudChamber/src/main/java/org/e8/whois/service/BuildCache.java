package org.e8.whois.service;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import org.e8.whois.cache.WhoisCacheTree;
import org.e8.whois.configuration.WhoIsConfiguration;
import org.e8.whois.dao.DAOFactory;
import org.e8.whois.dao.IpWhoisDAO;
import org.e8.whois.dao.impl.IpWhoisDAOImpl;
import org.e8.whois.model.WhoIsNode;
import org.e8.whois.parser.Parser;
import org.e8.whois.parser.ParserBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BuildCache {
	
	
private static Logger cacheBuildLog=LoggerFactory.getLogger(BuildCache.class);

public static void cacheBuild(WhoIsConfiguration conf) throws Exception{
	//TODO : query DAO to get list of WhoIs nodes and call 
	IpWhoisDAO daoInstance=DAOFactory.getDAO(conf);
	if(daoInstance!=null){
		List<WhoIsNode<Long>> lstNodes=daoInstance.findAllWhoisByIpData();
		WhoisCacheTree<Long> cacheTree=WhoisCacheTree.getCacheInstance();
		for(WhoIsNode<Long> node:lstNodes){
			cacheTree.insert(node);
		}
		cacheBuildLog.info("Heihgt of tree : "+cacheTree.getHeight());
	}


	/*String files[]={"Arin.txt","Lacnic.txt","Afrinic.txt","Ripe.txt","Apnic.txt"};
	WhoisCacheTree<Long> cacheTree=WhoisCacheTree.getCacheInstance();
	for(String file:files){
		Scanner scn=new Scanner(new File(file));
		StringBuffer buf=new StringBuffer();
		while(scn.hasNextLine()){
			buf.append(scn.nextLine());
			buf.append(System.getProperty("line.separator"));
		}
		scn.close();
		String response=buf.toString();


		WhoIsNode<Long> responseNode=null;
		Parser parser=ParserBuilder.getParser(response);
		if(parser!=null){
			responseNode=parser.parse(response);
			responseNode.setRawResponse(response);
		}
		cacheTree.insert(responseNode);
	}*/

}

}
