package org.e8.whois.service;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.e8.whois.cache.WhoisCacheTree;
import org.e8.whois.configuration.WhoIsConfiguration;
import org.e8.whois.model.WhoIsNode;
import org.e8.whois.parser.Parser;
import org.e8.whois.parser.ParserBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BuildCache {
	
	
private static Logger cacheBuildLog=LoggerFactory.getLogger(BuildCache.class);

	public static void cacheBuild(WhoIsConfiguration conf) throws IOException{
		//TODO : query DAO to get list of WhoIs nodes and call 
		
	/*	List<WhoIsNode<Long>> lstNodes=new ArrayList<WhoIsNode<Long>>();
		WhoisCacheTree<Long> cacheTree=WhoisCacheTree.getCacheInstance();
			for(WhoIsNode<Long> node:lstNodes){
			cacheTree.insert(node);
		}*/
		//remove
		String files[]={"Arin.txt","Lacnic.txt","Afrinic.txt","Ripe.txt","Apnic.txt"};
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
		if(parser!=null)
		responseNode=parser.parse(response);		
		cacheTree.insert(responseNode);
		}
		
		
	}

}
