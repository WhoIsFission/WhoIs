package org.e8.whois.parser;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.e8.whois.client.WhoIsClient;
import org.e8.whois.model.WhoIsNode;

public class WhoIsParser {

	
	public static WhoIsNode<Long> parseWhoIsResponse(String ipAddress) throws IOException{
		String response=WhoIsClient.callCommandRestClient(ipAddress);
		WhoIsNode<Long> responseNode=null;
		Parser parser=ParserBuilder.getParser(response);
		if(parser!=null)
		responseNode=parser.parse(response);		
		return responseNode;
	}
	
	
	public static void main(String... args) throws IOException{
		Scanner scn=new Scanner(new File("Arin.txt"));
		StringBuffer buf=new StringBuffer();
		while(scn.hasNextLine()){
			buf.append(scn.nextLine());
			buf.append(System.getProperty("line.separator"));
		}
		scn.close();
		System.out.println(buf.toString());
	WhoIsNode<Long> node=parseWhoIsResponse(buf.toString());
		System.out.println(node.getNetHandle());
		System.out.println(node.getNetName());
		System.out.println(node.getNetType());
		System.out.println(node.getOriginAS());
		System.out.println(node.getParent());
		System.out.println(node.getDescription());
		System.out.println(node.getDataSource());
		System.out.println(node.getLow());
		System.out.println(node.getHigh());
		System.out.println(node.getUpdatedDate());
		System.out.println(node.getOrg().getOrgName());
		System.out.println(node.getOrg().getOrgId());

		System.out.println(node.getOrg().getCountry());
		System.out.println(node.getOrg().getAddress());
		System.out.println(node.getOrg().getPhoneNo());
		System.out.println(node.getOrg().getUpdatedDate());
		System.out.println(node.getOrg().getEmail());
		System.out.println(node.getOrg().getFaxNo());

		for(int i=0;i<node.getOrgTech().size();i++){
		System.out.println(node.getOrgTech().get(i).getOrgTechHandle());
		System.out.println(node.getOrgTech().get(i).getOrgTechName());
		System.out.println(node.getOrgTech().get(i).getOrgAdrress());
		System.out.println(node.getOrgTech().get(i).getOrgTechPhone());
		System.out.println(node.getOrgTech().get(i).getOrgTechEmail());
		System.out.println(node.getOrgTech().get(i).getOrgTechFax());

		}
		for(int i=0;i<node.getOrgAbuse().size();i++){
		System.out.println(node.getOrgAbuse().get(i).getOrgAbuseHandle());
		System.out.println(node.getOrgAbuse().get(i).getOrgAbuseRef());}
		System.out.println("size of abuse is "+node.getOrgAbuse().size());
	
	}
	
}
