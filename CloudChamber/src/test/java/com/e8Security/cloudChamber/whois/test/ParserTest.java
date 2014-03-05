package org.e8.whois.client;

import org.e8.whois.exceptionHandling.WhoIsException;
import org.e8.whois.model.WhoIsNode;
import org.e8.whois.parser.WhoIsParser;

public class ParserTest {

	public static void main(String[] args) throws WhoIsException {
		// TODO Auto-generated method stub
		WhoIsNode<Long> node=WhoIsParser.parseWhoIsResponse("200.7.84.50");
		
		System.out.println("node address range: "+node.getStartAddress()+" and end address "+node.getEndAddress());
		System.out.println("node net name: "+node.getNetName());
		System.out.println("node net decr: "+node.getDescription());
		System.out.println("node net data source: "+node.getDataSource());
		System.out.println("node net status: "+node.getNetType());
		System.out.println("node autonum: "+node.getOriginAS());

		
		System.out.println("node org address: "+node.getOrg().getAddress());
		System.out.println("node org name: "+node.getOrg().getOrgName());
		System.out.println("node org Id: "+node.getOrg().getOrgId());
		System.out.println("node org fax: "+node.getOrg().getFaxNo());
		
		System.out.println("node tech name: "+node.getOrgTech().get(0).getOrgTechName());
		System.out.println("node tech address: "+node.getOrgTech().get(0).getOrgTechAdrress());
		System.out.println("node tech phone: "+node.getOrgTech().get(0).getOrgTechPhone());


		System.out.println("node admin name: "+node.getOrgAbuse().get(0).getOrgAbuseName());
		System.out.println("node admin phone: "+node.getOrgAbuse().get(0).getOrgAbusePhone());
		System.out.println("node tech address: "+node.getOrgAbuse().get(0).getOrgAbuseAddress());
		
		System.out.println("node route descr: "+node.getRoute().getDescription());
		System.out.println("node route Orgin asn: "+node.getRoute().getOriginASN());
		
		/**
		 * 
node address range: 5.10.16.0 and end address 5.10.19.255
node net name: BBS-CLOUDVMS
node net handle: null
node org address: null
node org name: null
node org Id: null
node org fax: null
node tech: BBS Tech-c
node admin: BBS Admin-c
node admin phone: +44 1582 227927
node route descr:           BBS Commerce Ltd
node route Orgin asn:          AS57168
		 * 
		 */
		
		
	}

}
