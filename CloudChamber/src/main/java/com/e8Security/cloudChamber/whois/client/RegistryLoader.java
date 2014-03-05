package com.e8security.cloudchamber.whois.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistryLoader {
	
	private static RegistryLoader REGISTRY_INSTANCE;
	private static String[] ADDRESS_PREFIX_WHOIS=new String[256];
	private static String patternPrefixWhois=".*(([0-9][0-9][0-9])/[0-9][0-9]?).*(whois\\..*\\.net).*";
	
	private RegistryLoader(){
		
	}
	
	public static RegistryLoader getInstance() throws FileNotFoundException{
		if(REGISTRY_INSTANCE==null){
			synchronized (RegistryLoader.class) {
				if(REGISTRY_INSTANCE==null){
					Scanner scanner=new Scanner(new File("ipv4-address-space.txt"));
					Pattern pattern=Pattern.compile(patternPrefixWhois);
					while(scanner.hasNextLine()){
						String line=scanner.nextLine();
						Matcher matcher=pattern.matcher(line);
						if(matcher.find()){
							String whoisserver=matcher.group(3);
							String pref=matcher.group(2);
							if(whoisserver!=null&&pref!=null&&!"".equals(whoisserver)&&!"".equals(pref)){
								int prefix=Integer.parseInt(pref);	
								ADDRESS_PREFIX_WHOIS[prefix]=whoisserver;
							}
						}
						
					}
					scanner.close();
					REGISTRY_INSTANCE=new RegistryLoader();
				}
			}
		}
		
		return REGISTRY_INSTANCE;
	}
	
	public String getWhoIsServer(int prefix){
		if(prefix>=0&&prefix<=255)
		return ADDRESS_PREFIX_WHOIS[prefix];
		else 
			return null;
	}

}
