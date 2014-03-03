package org.e8.whois.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.e8.whois.model.Organisation;
import org.e8.whois.model.OrganisationAbuse;
import org.e8.whois.model.OrganisationTech;
import org.e8.whois.model.WhoIsNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * Ripe Parser is used to parse response text from Ripe Registry
 * 
 *
 */
public class RipeParser extends IPParser{

	private final static Logger logger=LoggerFactory.getLogger(AfrinicParser.class);
	private static Map<String,ParsingPattern> RIPE_PARSE_MAP=new HashMap<String,ParsingPattern>();

static{
	
	  RIPE_PARSE_MAP.put("inetnum",ParsingPattern.WHOIS_NETRANGE_PATTERN);
	  RIPE_PARSE_MAP.put("origin",ParsingPattern.WHOIS_ORIGINASN_PATTERN);
	  RIPE_PARSE_MAP.put("netname",ParsingPattern.WHOIS_NETNAME_PATTERN);
	  RIPE_PARSE_MAP.put("source",ParsingPattern.WHOIS_DATASOURCE_PATTERN);
	  RIPE_PARSE_MAP.put("parent",ParsingPattern.WHOIS_PARENT_PATTERN);
	  RIPE_PARSE_MAP.put("status",ParsingPattern.WHOIS_NETTYPE_PATTERN);
	  RIPE_PARSE_MAP.put("org-name",ParsingPattern.WHOIS_ORGNAME_PATTERN);
	  RIPE_PARSE_MAP.put("org",ParsingPattern.WHOIS_ORGID_PATTERN);
	  RIPE_PARSE_MAP.put("address",ParsingPattern.WHOIS_ORGADDRESS_PATTERN);
	  RIPE_PARSE_MAP.put("phone",ParsingPattern.WHOIS_ORGPHONE_PATTERN);
	  RIPE_PARSE_MAP.put("fax-no",ParsingPattern.WHOIS_ORGFAX_PATTERN);
	  RIPE_PARSE_MAP.put("nic-hdl",ParsingPattern.WHOIS_ORGTECHHANDLE_PATTERN);
	  RIPE_PARSE_MAP.put("person",ParsingPattern.WHOIS_ORGTECHNAME_PATTERN);
	  RIPE_PARSE_MAP.put("descr",ParsingPattern.WHOIS_DESCR_PATTERN);
	  RIPE_PARSE_MAP.put("country",ParsingPattern.WHOIS_ORGCOUNTRY_PATTERN);
	  RIPE_PARSE_MAP.put("e-mail",ParsingPattern.WHOIS_ORGTECHEMAIL_PATTERN);
	  RIPE_PARSE_MAP.put("changed",ParsingPattern.WHOIS_UPDATEDATE_PATTERN);
	
	
}
	
	/**
	 * parse logic for Ripe.
	 * 
	 */
		public WhoIsNode<Long> parse(String buf) throws IOException {
			// TODO Auto-generated method stub
			if(logger.isDebugEnabled())
				logger.debug("Started parsing by Ripe parser");
		
			WhoIsNode<Long> responseNode=new WhoIsNode<Long>();
			Organisation org=new Organisation();
			  List<OrganisationTech> listOrgTech=new ArrayList<OrganisationTech>();
			  List<OrganisationAbuse> listOrgAbuse=new ArrayList<OrganisationAbuse>();
				responseNode.setOrg(org);
				responseNode.setOrgTech(listOrgTech);
				responseNode.setOrgAbuse(listOrgAbuse);
			BufferedReader bufRead=new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf.getBytes())));
		String str;	
		//Pattern pattern =Pattern.compile(regex)
		while((str=bufRead.readLine())!=null){
			int index=str.indexOf(":");
			if(index>0){
			 String prop=str.substring(0, index);
			 String value=str.substring(index+1);
			 responseNode=caseParsing(prop,value.trim(),responseNode);
				}
		
			}
		if(logger.isDebugEnabled())
			logger.debug("Parsed response being returned");
			return responseNode;
		}
		
		
		/*
		 * 
		 * Case based parsing
		 */
		private WhoIsNode<Long> caseParsing(String aPattern,String aValue,WhoIsNode<Long> node) {
			ParsingPattern pattern=RIPE_PARSE_MAP.get(aPattern);
			OrganisationTech orgTech;			
			int len;
			if(pattern!=null){
				if(logger.isDebugEnabled())
					logger.debug("Started Ripe parsing for pattern: "+pattern.toString());
			switch(pattern){
			  case WHOIS_NETRANGE_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("NETRANGE pattern encountered");
				  if(isCidrNotation(aValue)){
					  aValue=cidrToRange(aValue);
				  }
				  String netrange[]=aValue.split("-");
				  if(netrange!=null&&netrange.length>1){
					  node.setStartAddress(netrange[0].trim());
					  node.setEndAddress(netrange[1].trim());
				  node.setLow(this.ipToLong(netrange[0].trim()));
				  node.setHigh(this.ipToLong(netrange[1].trim()));
				  }
				  break;
			  case  WHOIS_DESCR_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("DESCRIPTION pattern encountered");
				  node.setDescription(aValue);
				  break;
			  case  WHOIS_ORGCOUNTRY_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Organisation country pattern encountered");
				  node.getOrg().setCountry(aValue);
				  break;
			  case WHOIS_ORIGINASN_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("ASN pattern encountered");
				  node.setOriginAS(aValue);break;
			  case  WHOIS_PARENT_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Parent pattern encountered");
				  
				  node.setParent(aValue);break;
			  case WHOIS_NETNAME_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Net Name pattern encountered");
				  
				  node.setNetName(aValue);break;
			  case WHOIS_DATASOURCE_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("DataSource pattern encountered");
				  
				  node.setDataSource(aValue);break;
			  case WHOIS_NETTYPE_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("NetType pattern encountered");
				  
				  node.setNetType(aValue);break;
			  case WHOIS_ORGNAME_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("ORG Name pattern encountered");
				  
				    node.getOrg().setOrgName(aValue);break;	  
			  case WHOIS_ORGID_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("OrgId pattern encountered");
				  
				  node.getOrg().setOrgId(aValue);break;
			  case WHOIS_ORGADDRESS_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Org Address pattern encountered");
				  
				  len=node.getOrgTech().size();
				  if(len>0){
					   orgTech=node.getOrgTech().get(len-1);
					   orgTech.setOrgTechAdrress((orgTech.getOrgTechAdrress()==null?"":orgTech.getOrgTechAdrress()+", ")+aValue);
				  }else{
					  node.getOrg().setAddress((node.getOrg().getAddress()==null?"":node.getOrg().getAddress()+", ")+aValue);
				  }
				  
				  break;
			  case WHOIS_ORGPHONE_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Org Phone pattern encountered");
				  
				  len=node.getOrgTech().size();
				  if(len>0){
					   orgTech=node.getOrgTech().get(len-1);
					   orgTech.setOrgTechPhone(aValue);
				  }else{
					  node.getOrg().setPhoneNo(aValue);
				  }
				  
				  break;
			  case WHOIS_ORGFAX_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Org Fax pattern encountered");
				  
				  len=node.getOrgTech().size();
				  if(len>0){
					   orgTech=node.getOrgTech().get(len-1);
					   orgTech.setOrgTechFax(aValue);
				  }else{
					  node.getOrg().setFaxNo(aValue);
				  }				  
				  break;
			  case WHOIS_ORGTECHHANDLE_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Org Tech Handle pattern encountered");
				  
				  len=node.getOrgTech().size();
				  if(len>0){
					  orgTech=node.getOrgTech().get(len-1);
					   orgTech.setOrgTechHandle(aValue); 
				  }
				  break;
			  case WHOIS_ORGTECHNAME_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Org Tech Name pattern encountered");
				  
			        orgTech=new OrganisationTech();
			        orgTech.setOrgTechName(aValue);
			        node.getOrgTech().add(orgTech);
			        break;
			  case WHOIS_ORGTECHEMAIL_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Org Tech Email pattern encountered");
				  
				  len=node.getOrgTech().size();
				  if(len>0){
					  orgTech=node.getOrgTech().get(len-1);
					   orgTech.setOrgTechEmail(aValue); 
				  }
				  break;
			default:
				break;			 
			     }
			if(logger.isDebugEnabled())
				  logger.debug("Returning node after setting pattern:"+ pattern.toString());
			}
			
			return node;
		}
}

