package org.e8.whois.parser;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.e8.whois.model.Organisation;
import org.e8.whois.model.OrganisationAbuse;
import org.e8.whois.model.OrganisationAdmin;
import org.e8.whois.model.OrganisationTech;
import org.e8.whois.model.Route;
import org.e8.whois.model.WhoIsNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * Arin Parser is used to parse response text from Arin Registry
 * 
 *
 */
public class ArinParser extends IPParser{
	private final static Logger logger=LoggerFactory.getLogger(ArinParser.class);
private final static Map<String,ParsingPattern> ARIN_PARSE_MAP=new HashMap<String,ParsingPattern>();
static{
	ARIN_PARSE_MAP.put("NetRange", ParsingPattern.WHOIS_NETRANGE_PATTERN);
	ARIN_PARSE_MAP.put("OriginAS", ParsingPattern.WHOIS_ORIGINASN_PATTERN);
	ARIN_PARSE_MAP.put("NetName", ParsingPattern.WHOIS_NETNAME_PATTERN);
	ARIN_PARSE_MAP.put("NetHandle", ParsingPattern.WHOIS_NETHANDLE_PATTERN);
	ARIN_PARSE_MAP.put("Parent", ParsingPattern.WHOIS_PARENT_PATTERN);
	ARIN_PARSE_MAP.put("NetType", ParsingPattern.WHOIS_NETTYPE_PATTERN);
	ARIN_PARSE_MAP.put("RegDate", ParsingPattern.WHOIS_REGDATE_PATTERN);
	ARIN_PARSE_MAP.put("Updated", ParsingPattern.WHOIS_UPDATEDATE_PATTERN);
	ARIN_PARSE_MAP.put("Ref", ParsingPattern.WHOIS_REF_PATTERN);
	ARIN_PARSE_MAP.put("OrgName", ParsingPattern.WHOIS_ORGNAME_PATTERN);
	ARIN_PARSE_MAP.put("OrgId", ParsingPattern.WHOIS_ORGID_PATTERN);
	ARIN_PARSE_MAP.put("Address", ParsingPattern.WHOIS_ORGADDRESS_PATTERN);
	ARIN_PARSE_MAP.put("City", ParsingPattern.WHOIS_ORGCITY_PATTERN);
	ARIN_PARSE_MAP.put("StateProv", ParsingPattern.WHOIS_ORGSTATE_PATTERN);
	ARIN_PARSE_MAP.put("PostalCode", ParsingPattern.WHOIS_ORGPOSTAL_PATTERN);
	ARIN_PARSE_MAP.put("Country", ParsingPattern.WHOIS_ORGCOUNTRY_PATTERN);
	ARIN_PARSE_MAP.put("OrgTechHandle", ParsingPattern.WHOIS_ORGTECHHANDLE_PATTERN);
	ARIN_PARSE_MAP.put("OrgTechName", ParsingPattern.WHOIS_ORGTECHNAME_PATTERN);
	ARIN_PARSE_MAP.put("OrgTechPhone", ParsingPattern.WHOIS_ORGTECHPHONE_PATTERN);
	ARIN_PARSE_MAP.put("OrgTechAddress", ParsingPattern.WHOIS_ORGTECHADDRESS_PATTERN);
	ARIN_PARSE_MAP.put("OrgTechEmail", ParsingPattern.WHOIS_ORGTECHEMAIL_PATTERN);
	ARIN_PARSE_MAP.put("OrgTechRef", ParsingPattern.WHOIS_ORGTECHREF_PATTERN);
	ARIN_PARSE_MAP.put("OrgAbuseHandle", ParsingPattern.WHOIS_ORGABUSEHANDLE_PATTERN);
	ARIN_PARSE_MAP.put("OrgAbuseName", ParsingPattern.WHOIS_ORGABUSENAME_PATTERN);
	ARIN_PARSE_MAP.put("OrgAbusePhone", ParsingPattern.WHOIS_ORGABUSEPHONE_PATTERN);
	ARIN_PARSE_MAP.put("OrgAbuseAddress", ParsingPattern.WHOIS_ORGABUSEADDRESS_PATTERN);
	ARIN_PARSE_MAP.put("OrgAbuseEmail", ParsingPattern.WHOIS_ORGABUSEEMAIL_PATTERN);
	ARIN_PARSE_MAP.put("OrgAbuseRef", ParsingPattern.WHOIS_ORGABUSEREF_PATTERN);
	ARIN_PARSE_MAP.put("OrgAdminHandle", ParsingPattern.WHOIS_ORGADMINHANDLE_PATTERN);
	ARIN_PARSE_MAP.put("OrgAdminName", ParsingPattern.WHOIS_ORGADMINNAME_PATTERN);
	ARIN_PARSE_MAP.put("OrgAdminPhone", ParsingPattern.WHOIS_ORGADMINPHONE_PATTERN);
	ARIN_PARSE_MAP.put("OrgAdminAddress", ParsingPattern.WHOIS_ORGADMINADDRESS_PATTERN);
	ARIN_PARSE_MAP.put("OrgAdminEmail", ParsingPattern.WHOIS_ORGADMINEMAIL_PATTERN);
	ARIN_PARSE_MAP.put("OrgAdminRef", ParsingPattern.WHOIS_ORGADMINREF_PATTERN);
	
	
	
	
	
}


/**
 * parse logic for Arin.
 * 
 */
	public WhoIsNode<Long> parse(String buf) throws IOException {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled())
			logger.debug("Started parsing by Arin parser");
		WhoIsNode<Long> responseNode=new WhoIsNode<Long>();
		Organisation org=new Organisation();
		  List<OrganisationTech> listOrgTech=new ArrayList<OrganisationTech>();
		  List<OrganisationAbuse> listOrgAbuse=new ArrayList<OrganisationAbuse>();
		  List<OrganisationAdmin> listOrgAdmin=new ArrayList<OrganisationAdmin>();
			responseNode.setOrg(org);
			responseNode.setOrgTech(listOrgTech);
			responseNode.setOrgAbuse(listOrgAbuse);
			responseNode.setOrgAdmin(listOrgAdmin);
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
		ParsingPattern pattern=ARIN_PARSE_MAP.get(aPattern);
		SimpleDateFormat datePattern=new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		OrganisationTech orgTech;
		OrganisationAbuse orgAbuse;
		OrganisationAdmin orgAdmin;
		Route route;
		int len;
		if(pattern!=null){
			if(logger.isDebugEnabled())
				logger.debug("Started Arin parsing for pattern: "+pattern.toString());
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
			  
		  case WHOIS_ORIGINASN_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("ASN pattern encountered");
			  
			  node.setOriginAS(aValue);break;			  
		  case WHOIS_NETNAME_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("NET Name pattern encountered");
			  node.setNetName(aValue);break;
		  case WHOIS_NETHANDLE_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("NET Handle pattern encountered");
			  node.setNetHandle(aValue);break;
		  case WHOIS_PARENT_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("Parent pattern encountered");
			  
			  node.setParent(aValue);break;
		  case WHOIS_NETTYPE_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("NET Type pattern encountered");
			  node.setNetType(aValue);break;
		  case WHOIS_REGDATE_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("Registration Date pattern encountered");
			  if(!(aValue==null||"".equals(aValue))){
			try {
			 date = datePattern.parse(aValue);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException("Date parse exception",e);
			}	  
			if(node.getOrg().getOrgName()==null||"".equals(node.getOrg().getOrgName()))
			  node.setRegDate(date);
			else
				node.getOrg().setRegDate(date);
			  }
			break;
		  case WHOIS_UPDATEDATE_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("Updated Date pattern encountered"); 
			  if(!(aValue==null||"".equals(aValue))){
			  try {
				date=datePattern.parse(aValue);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException("Date parse exception",e);
			}	
			   if(node.getOrg().getOrgName()==null||"".equals(node.getOrg().getOrgName()))
			   node.setUpdatedDate(date);
			   else
				   node.getOrg().setUpdatedDate(date);
			  }
			   break;
		  case WHOIS_REF_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("Reference pattern encountered");
			  
			  if(node.getOrg().getOrgName()==null||"".equals(node.getOrg().getOrgName()))
			  node.setRef(aValue);
			  else
				  node.getOrg().setRef(aValue);
			  
			  break;
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
				  logger.debug("Address pattern encountered");
			  
			  node.getOrg().setAddress((node.getOrg().getAddress()==null?"":node.getOrg().getAddress()+", ")+aValue);break; 
		  case WHOIS_ORGCITY_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("City pattern encountered");
			  
			  node.getOrg().setCity(aValue);break;
		  case WHOIS_ORGSTATE_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("State pattern encountered");
			  
			  node.getOrg().setState(aValue);break;
		  case WHOIS_ORGPOSTAL_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("Postal code pattern encountered");
			  
			  node.getOrg().setPostalCode(aValue);break;
		  case WHOIS_ORGCOUNTRY_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("Country pattern encountered");
			  
			  node.getOrg().setCountry(aValue);break;
		  case WHOIS_ORGTECHHANDLE_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("ORG Tech Handle pattern encountered");
			   orgTech=new OrganisationTech();
			  orgTech.setOrgTechHandle(aValue);
			  node.getOrgTech().add(orgTech);
			  break;
		  case WHOIS_ORGTECHNAME_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("ORG Tech Name pattern encountered");
			  
			   len=node.getOrgTech().size();
			   orgTech=node.getOrgTech().get(len-1);
			   orgTech.setOrgTechName(aValue);
			   break;
		  case WHOIS_ORGTECHPHONE_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("ORG Tech Phone pattern encountered");
			  
			  len=node.getOrgTech().size();
			   orgTech=node.getOrgTech().get(len-1);
			   orgTech.setOrgTechPhone(aValue);
			   break;
		  case WHOIS_ORGTECHADDRESS_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("ORG Tech Address pattern encountered");
			  
			  len=node.getOrgTech().size();
			  orgTech=node.getOrgTech().get(len-1);
			  orgTech.setOrgTechAdrress((orgTech.getOrgTechAdrress()==null?"":orgTech.getOrgTechAdrress()+", ")+aValue);
			  break;
		  case WHOIS_ORGTECHEMAIL_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("ORG Tech Email pattern encountered");
			  
			  len=node.getOrgTech().size();
			   orgTech=node.getOrgTech().get(len-1);
			   orgTech.setOrgTechEmail(aValue);
			   break;
		  case WHOIS_ORGTECHREF_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("ORG Tech Reference pattern encountered");
			  len=node.getOrgTech().size();
			   orgTech=node.getOrgTech().get(len-1);
			   orgTech.setOrgTechRef(aValue);
			   break;
		  case WHOIS_ORGABUSEHANDLE_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("ORG Abuse Handle pattern encountered");
			  
			  orgAbuse=new OrganisationAbuse();
			  orgAbuse.setOrgAbuseHandle(aValue);
			  node.getOrgAbuse().add(orgAbuse);
			  break;
		  case WHOIS_ORGABUSENAME_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("Org Abuse Name pattern encountered");
			  
			  len=node.getOrgAbuse().size();
			  orgAbuse=node.getOrgAbuse().get(len-1);
			  orgAbuse.setOrgAbuseName(aValue);
			  break;
		  case WHOIS_ORGABUSEPHONE_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("Org Abuse Phone pattern encountered");
			  
			  len=node.getOrgAbuse().size();
			  orgAbuse=node.getOrgAbuse().get(len-1);
			  orgAbuse.setOrgAbusePhone(aValue);
			  break;
		  case WHOIS_ORGABUSEADDRESS_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("Org Abuse Address pattern encountered");
			  
			  len=node.getOrgAbuse().size();
			  orgAbuse=node.getOrgAbuse().get(len-1);
			  orgAbuse.setOrgAbuseAddress((orgAbuse.getOrgAbuseAddress()==null?"":orgAbuse.getOrgAbuseAddress()+", ")+aValue);
			  break;
		  case WHOIS_ORGABUSEEMAIL_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("Org Abuse Email pattern encountered");
			  
			  len=node.getOrgAbuse().size();
			  orgAbuse=node.getOrgAbuse().get(len-1);
			  orgAbuse.setOrgAbuseEmail(aValue);
			  break;
		  case WHOIS_ORGABUSEREF_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("Org Abuse Reference pattern encountered");
			  
			  len=node.getOrgAbuse().size();
			  orgAbuse=node.getOrgAbuse().get(len-1);
			  orgAbuse.setOrgAbuseRef(aValue);
			  break;
			  // Admin pattern
			  
		  case WHOIS_ORGADMINHANDLE_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("ORG Abuse Handle pattern encountered");
			  
			  orgAdmin=new OrganisationAdmin();
			  orgAdmin.setOrgAdminHandle(aValue);
			  node.getOrgAdmin().add(orgAdmin);
			  break;
		  case WHOIS_ORGADMINNAME_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("Org Abuse Name pattern encountered");
			  
			  len=node.getOrgAdmin().size();
			  orgAdmin=node.getOrgAdmin().get(len-1);
			  orgAdmin.setOrgAdminName(aValue);
			  break;
		  case WHOIS_ORGADMINPHONE_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("Org Abuse Phone pattern encountered");
			  
			  len=node.getOrgAdmin().size();
			  orgAdmin=node.getOrgAdmin().get(len-1);
			  orgAdmin.setOrgAdminPhone(aValue);
			  break;
		  case WHOIS_ORGADMINADDRESS_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("Org Abuse Address pattern encountered");
			  
			  len=node.getOrgAdmin().size();
			  orgAdmin=node.getOrgAdmin().get(len-1);
			  orgAdmin.setOrgAdminAddress((orgAdmin.getOrgAdminAddress()==null?"":orgAdmin.getOrgAdminAddress()+", ")+aValue);
			  break;
		  case WHOIS_ORGADMINEMAIL_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("Org Abuse Email pattern encountered");
			  
			  len=node.getOrgAdmin().size();
			  orgAdmin=node.getOrgAdmin().get(len-1);
			  orgAdmin.setOrgAdminEmail(aValue);
			  break;
		  case WHOIS_ORGADMINREF_PATTERN:
			  if(logger.isDebugEnabled())
				  logger.debug("Org Abuse Reference pattern encountered");
			  
			  len=node.getOrgAbuse().size();
			  orgAbuse=node.getOrgAbuse().get(len-1);
			  orgAbuse.setOrgAbuseRef(aValue);
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


