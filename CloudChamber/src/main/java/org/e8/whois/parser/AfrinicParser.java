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

public class AfrinicParser extends IPParser{

	
	private static Map<String,ParsingPattern> AFRINIC_PARSE_MAP=new HashMap<String,ParsingPattern>();

	static{
		
		AFRINIC_PARSE_MAP.put("inetnum",ParsingPattern.WHOIS_NETRANGE_PATTERN);
		  AFRINIC_PARSE_MAP.put("origin",ParsingPattern.WHOIS_ORIGINASN_PATTERN);
		  AFRINIC_PARSE_MAP.put("netname",ParsingPattern.WHOIS_NETNAME_PATTERN);
		  AFRINIC_PARSE_MAP.put("source",ParsingPattern.WHOIS_DATASOURCE_PATTERN);
		  AFRINIC_PARSE_MAP.put("parent",ParsingPattern.WHOIS_PARENT_PATTERN);
		  AFRINIC_PARSE_MAP.put("status",ParsingPattern.WHOIS_NETTYPE_PATTERN);
		  AFRINIC_PARSE_MAP.put("org-name",ParsingPattern.WHOIS_ORGNAME_PATTERN);
		  AFRINIC_PARSE_MAP.put("org",ParsingPattern.WHOIS_ORGID_PATTERN);
		  AFRINIC_PARSE_MAP.put("address",ParsingPattern.WHOIS_ORGADDRESS_PATTERN);
		  AFRINIC_PARSE_MAP.put("phone",ParsingPattern.WHOIS_ORGPHONE_PATTERN);
		  AFRINIC_PARSE_MAP.put("fax-no",ParsingPattern.WHOIS_ORGFAX_PATTERN);
		  AFRINIC_PARSE_MAP.put("nic-hdl",ParsingPattern.WHOIS_ORGTECHHANDLE_PATTERN);
		  AFRINIC_PARSE_MAP.put("person",ParsingPattern.WHOIS_ORGTECHNAME_PATTERN);
		  AFRINIC_PARSE_MAP.put("descr",ParsingPattern.WHOIS_DESCR_PATTERN);
		  AFRINIC_PARSE_MAP.put("country",ParsingPattern.WHOIS_ORGCOUNTRY_PATTERN);
		  AFRINIC_PARSE_MAP.put("e-mail",ParsingPattern.WHOIS_ORGTECHEMAIL_PATTERN);
		  AFRINIC_PARSE_MAP.put("changed",ParsingPattern.WHOIS_UPDATEDATE_PATTERN);
		
		
	}
	
	/**
	 * parse logic for Ripe.
	 * @throws IOException 
	 * 
	 */
		public WhoIsNode<Long> parse(String buf) throws IOException {
			// TODO Auto-generated method stub
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
			return responseNode;
		}
	
	
	
	
	/**
	 * 
	 * 
	 */
	
		private WhoIsNode<Long> caseParsing(String aPattern,String aValue,WhoIsNode<Long> node){
			ParsingPattern pattern=AFRINIC_PARSE_MAP.get(aPattern);
		OrganisationTech orgTech;			
		int len;
		if(pattern!=null){
		switch(pattern){
		  case WHOIS_NETRANGE_PATTERN:
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
			  node.setDescription(aValue);
			  break;
		  case  WHOIS_ORGCOUNTRY_PATTERN:
			  node.getOrg().setCountry(aValue);
			  break;
		  case WHOIS_ORIGINASN_PATTERN:
			  node.setOriginAS(aValue);break;
		  case  WHOIS_PARENT_PATTERN:
			  node.setParent(aValue);break;
		  case WHOIS_NETNAME_PATTERN:
			  node.setNetName(aValue);break;
		  case WHOIS_DATASOURCE_PATTERN:
			  node.setDataSource(aValue);break;
		  case WHOIS_NETTYPE_PATTERN:
			  node.setNetType(aValue);break;
		  case WHOIS_ORGNAME_PATTERN:
			    node.getOrg().setOrgName(aValue);break;	  
		  case WHOIS_ORGID_PATTERN:
			  node.getOrg().setOrgId(aValue);break;
		  case WHOIS_ORGADDRESS_PATTERN:
			  len=node.getOrgTech().size();
			  if(len>0){
				   orgTech=node.getOrgTech().get(len-1);
				   orgTech.setOrgTechAdrress((orgTech.getOrgTechAdrress()==null?"":orgTech.getOrgTechAdrress()+", ")+aValue);
			  }else{
				  node.getOrg().setAddress((node.getOrg().getAddress()==null?"":node.getOrg().getAddress()+", ")+aValue);
					  }
			  
			  break;
		  case WHOIS_ORGPHONE_PATTERN:
			  len=node.getOrgTech().size();
			  if(len>0){
				   orgTech=node.getOrgTech().get(len-1);
				   String phone=null;
				   if(orgTech.getOrgTechPhone()!=null)
					   phone=orgTech.getOrgTechPhone();
				   orgTech.setOrgTechPhone((phone==null?"":phone+",")+aValue);
				   int i=orgTech.getOrgTechPhone().length();
				   orgTech.setOrgTechPhone(orgTech.getOrgTechPhone().substring(0, i-1));
			  }else{
				  node.getOrg().setPhoneNo(aValue);
			  }
			  
			  break;
		  case WHOIS_ORGFAX_PATTERN:
			  len=node.getOrgTech().size();
			  if(len>0){
				   orgTech=node.getOrgTech().get(len-1);
				   orgTech.setOrgTechFax(aValue);
			  }else{
				  node.getOrg().setFaxNo(aValue);
			  }
			  			  
			  break;
		  case WHOIS_ORGTECHHANDLE_PATTERN:
			  len=node.getOrgTech().size();
			  if(len>0){
				  orgTech=node.getOrgTech().get(len-1);
				   orgTech.setOrgTechHandle(aValue); 
			  }
			  break;
		  case WHOIS_ORGTECHNAME_PATTERN:
		        orgTech=new OrganisationTech();
		        orgTech.setOrgTechName(aValue);
		        node.getOrgTech().add(orgTech);
		        break;
		  case WHOIS_ORGTECHEMAIL_PATTERN:
			  len=node.getOrgTech().size();
			  if(len>0){
				  orgTech=node.getOrgTech().get(len-1);
				   orgTech.setOrgTechEmail(aValue); 
			  }
			  break;
			  default: break;
		   }
		}
		return node;
	}

}
