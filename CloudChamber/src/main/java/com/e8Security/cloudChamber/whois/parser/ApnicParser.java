package com.e8Security.cloudChamber.whois.parser;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.e8Security.cloudChamber.whois.model.Organisation;
import com.e8Security.cloudChamber.whois.model.OrganisationAbuse;
import com.e8Security.cloudChamber.whois.model.OrganisationAdmin;
import com.e8Security.cloudChamber.whois.model.OrganisationTech;
import com.e8Security.cloudChamber.whois.model.Route;
import com.e8Security.cloudChamber.whois.model.WhoIsNode;

/***
 * Apnic Parser is used to parse response text from Apnic Registry
 * 
 *
 */
public class ApnicParser extends IPParser{

	private final static Logger logger=LoggerFactory.getLogger(ApnicParser.class);
	private static Map<String,ParsingPattern> APNIC_PARSE_MAP=new HashMap<String,ParsingPattern>();
	 private Map<String,List<String>> contactsMap=new HashMap<String,List<String>>();
	static{
		
		  APNIC_PARSE_MAP.put("inetnum",ParsingPattern.WHOIS_NETRANGE_PATTERN);
		  APNIC_PARSE_MAP.put("origin",ParsingPattern.WHOIS_ORIGINASN_PATTERN);
		  APNIC_PARSE_MAP.put("netname",ParsingPattern.WHOIS_NETNAME_PATTERN);
		  APNIC_PARSE_MAP.put("source",ParsingPattern.WHOIS_DATASOURCE_PATTERN);
		  APNIC_PARSE_MAP.put("parent",ParsingPattern.WHOIS_PARENT_PATTERN);
		  APNIC_PARSE_MAP.put("status",ParsingPattern.WHOIS_NETTYPE_PATTERN);
		  APNIC_PARSE_MAP.put("org-name",ParsingPattern.WHOIS_ORGNAME_PATTERN);
		  APNIC_PARSE_MAP.put("irt",ParsingPattern.WHOIS_ORGNAME_PATTERN);
	      APNIC_PARSE_MAP.put("org",ParsingPattern.WHOIS_ORGID_PATTERN);
		  APNIC_PARSE_MAP.put("address",ParsingPattern.WHOIS_ORGADDRESS_PATTERN);
		  APNIC_PARSE_MAP.put("phone",ParsingPattern.WHOIS_ORGPHONE_PATTERN);
		  APNIC_PARSE_MAP.put("fax-no",ParsingPattern.WHOIS_ORGFAX_PATTERN);
		  APNIC_PARSE_MAP.put("nic-hdl",ParsingPattern.WHOIS_ORGTECHHANDLE_PATTERN);
		  APNIC_PARSE_MAP.put("person",ParsingPattern.WHOIS_ORGTECHNAME_PATTERN);
		  APNIC_PARSE_MAP.put("role",ParsingPattern.WHOIS_ORGTECHNAME_PATTERN);
		  APNIC_PARSE_MAP.put("descr",ParsingPattern.WHOIS_DESCR_PATTERN);
		  APNIC_PARSE_MAP.put("country",ParsingPattern.WHOIS_ORGCOUNTRY_PATTERN);
		  APNIC_PARSE_MAP.put("e-mail",ParsingPattern.WHOIS_ORGTECHEMAIL_PATTERN);
		  APNIC_PARSE_MAP.put("changed",ParsingPattern.WHOIS_UPDATEDATE_PATTERN);
		  APNIC_PARSE_MAP.put("created",ParsingPattern.WHOIS_REGDATE_PATTERN);
		
		
	}
	

/*
 * Getting list of contacts given contactname and value net
 * 
 * 
 */
private List<String> getListOfContacts(String aContactName,String aValueInet){

	 List<String> lstOfContacts=contactsMap.get(aValueInet);
	 if(lstOfContacts==null||lstOfContacts.isEmpty()){
		 	 lstOfContacts=new ArrayList<String>();
		}
	 lstOfContacts.add(aContactName);
	 return lstOfContacts;

}

	
	/**
	 * parse logic for Apnic.
	 * @throws ParseException 
	 * 
	 */
		public WhoIsNode<Long> parse(String buf,String aIpAddress) throws IOException, ParseException {
			// TODO Auto-generated method stub
			if(logger.isDebugEnabled())
				logger.debug("Started parsing by Ripe parser");
		
			WhoIsNode<Long> responseNode=new WhoIsNode<Long>();
			Organisation org=new Organisation();
			  List<OrganisationTech> listOrgTech=new ArrayList<OrganisationTech>();
			  List<OrganisationAbuse> listOrgAbuse=new ArrayList<OrganisationAbuse>();
			  List<OrganisationAdmin> listOrgAdmin=new ArrayList<OrganisationAdmin>();
				responseNode.setOrg(org);
				responseNode.setOrgTech(listOrgTech);
				responseNode.setOrgAbuse(listOrgAbuse);
				responseNode.setOrgAdmin(listOrgAdmin);
				responseNode.setRoute(new Route());
			BufferedReader bufRead=new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf.getBytes())));
		String str;	
		//Pattern pattern =Pattern.compile(regex)
		while((str=bufRead.readLine())!=null){
			int index=str.indexOf(":");
			if(index>0){
			 String prop=str.substring(0, index).trim();
			 String value=str.substring(index+1).trim();
			 // for inetnum
			 if("inetnum".equalsIgnoreCase(prop)){
				 responseNode=caseParsing(prop,value,responseNode,"NETBLOCK",aIpAddress);
				 String strInet="";
					while((strInet=bufRead.readLine())!=null&&!"".equals((strInet))){
						int indexInet=strInet.indexOf(":");
						if(indexInet>0){
						 String propInet=strInet.substring(0, indexInet).trim();
						 String valueInet=strInet.substring(indexInet+1).trim();
						 if("tech-c".equalsIgnoreCase(propInet)){
							 contactsMap.put(valueInet,getListOfContacts("tech-c",valueInet));
						 }else if("abuse-c".equalsIgnoreCase(propInet)){
							 contactsMap.put(valueInet,getListOfContacts("abuse-c",valueInet));
						 }else if("admin-c".equalsIgnoreCase(propInet)){
							 contactsMap.put(valueInet, getListOfContacts("admin-c", valueInet));
						 }
						 responseNode=caseParsing(propInet,valueInet,responseNode,"NETBLOCK",aIpAddress);
					}
				}
			 } else if("organisation".equalsIgnoreCase(prop)||"irt".equalsIgnoreCase(prop)){

				 responseNode=caseParsing(prop,value,responseNode,"ORGANISATION",aIpAddress);
				 String strOrg="";
					while((strOrg=bufRead.readLine())!=null&&!"".equals(strOrg)){
						int indexOrg=strOrg.indexOf(":");
						if(indexOrg>0){
						 String propOrg=strOrg.substring(0, indexOrg).trim();
						 String valueOrg=strOrg.substring(indexOrg+1).trim();
						 responseNode=caseParsing(propOrg,valueOrg,responseNode,"ORGANISATION",aIpAddress);
						}
				   }			 
			 }else if("person".equalsIgnoreCase(prop)||"role".equalsIgnoreCase(prop)||"nic-hdl".equalsIgnoreCase(prop)){

				 String valueNicHandle="";
				 if("nic-hdl".equalsIgnoreCase(prop)){
						valueNicHandle=value;
						    }
				 StringBuffer buffer=new StringBuffer();
				 buffer.append(str);
				 buffer.append(System.getProperty("line.separator"));
				 String strCont="";						 
					while((strCont=bufRead.readLine())!=null&&!"".equals(strCont)){
						int indexCont=strCont.indexOf(":");
						if(indexCont>0){
						 String propCont=strCont.substring(0, indexCont).trim();
						 String valueCont=strCont.substring(indexCont+1).trim();
						 if("nic-hdl".equalsIgnoreCase(propCont)){
							valueNicHandle=valueCont;
							    }
						 	 buffer.append(strCont);
							 buffer.append(System.getProperty("line.separator"));	 
						    }						
						}
			
			 
			 List<String> lstContacts=contactsMap.get(valueNicHandle);
			 if(lstContacts!=null){
			 for(String contactName:lstContacts){
				 if("tech-c".equalsIgnoreCase(contactName)){
					 OrganisationTech orgTech=new OrganisationTech();
					 responseNode.getOrgTech().add(orgTech);
					 BufferedReader bufReadOrgDetails=new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer.toString().getBytes())));
					 String strOrgTech="";
							 while((strOrgTech=bufReadOrgDetails.readLine())!=null&&!"".equals(strOrgTech)){
								 int indColon=strOrgTech.indexOf(":");
									if(indColon>0){
									 String propTech=strOrgTech.substring(0, indColon);
									 String valueTech=strOrgTech.substring(indColon+1).trim();
							 responseNode=caseParsing(propTech,valueTech,responseNode,"TECH",aIpAddress);
									}
				     	  }
				 }else if("abuse-c".equalsIgnoreCase(contactName)){
					 OrganisationAbuse orgAbuse=new OrganisationAbuse();
					 responseNode.getOrgAbuse().add(orgAbuse);
					 BufferedReader bufReadOrgDetails=new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer.toString().getBytes())));
					 String strOrgAbuse="";
							 while((strOrgAbuse=bufReadOrgDetails.readLine())!=null&&!"".equals(strOrgAbuse)){
								 int indColon=strOrgAbuse.indexOf(":");
									if(indColon>0){
									 String propAbuse=strOrgAbuse.substring(0, indColon);
									 String valueAbuse=strOrgAbuse.substring(indColon+1).trim();
							 responseNode=caseParsing(propAbuse,valueAbuse,responseNode,"ABUSE",aIpAddress);
									}
					  }
		       }else if("admin-c".equalsIgnoreCase(contactName)){
							 OrganisationAdmin orgAdmin=new OrganisationAdmin();
							 responseNode.getOrgAdmin().add(orgAdmin);
							 BufferedReader bufReadOrgDetails=new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer.toString().getBytes())));
							 String strOrgAdmin="";
							  while((strOrgAdmin=bufReadOrgDetails.readLine())!=null&&!"".equals(strOrgAdmin)){
										 int indColon=strOrgAdmin.indexOf(":");
											if(indColon>0){
											 String propTech=strOrgAdmin.substring(0, indColon);
											 String valueTech=strOrgAdmin.substring(indColon+1).trim();
									 responseNode=caseParsing(propTech,valueTech,responseNode,"ADMIN",aIpAddress);
											}
								  }
					  	  }
			        }
			 }
			 }else if("route".equalsIgnoreCase(prop)){
				 String strAsn="";
					while((strAsn=bufRead.readLine())!=null&&!"".equals(strAsn)){
						int indexASN=strAsn.indexOf(":");
						if(indexASN>0){
						 String propAsn=strAsn.substring(0, indexASN);
						 String valueAsn=strAsn.substring(indexASN+1);
						 responseNode=caseParsing(propAsn,valueAsn,responseNode,"ASN",aIpAddress);
						}
				}
			 }
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
		private WhoIsNode<Long> caseParsing(String aPattern,String aValue,WhoIsNode<Long> node,String aType,String aIpAddress) throws ParseException {
			ParsingPattern pattern=APNIC_PARSE_MAP.get(aPattern);
			OrganisationTech orgTech;		
			OrganisationAbuse orgAbuse;
			OrganisationAdmin orgAdmin;
			SimpleDateFormat parser=new SimpleDateFormat("yyyyMMdd");
			Date date;
			int len;
			if(pattern!=null&&aType!=null){
				if(logger.isDebugEnabled())
					logger.debug("Started Ripe parsing for pattern: "+pattern.toString());
			switch(pattern){
			  case WHOIS_NETRANGE_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("NETRANGE pattern encountered for type: "+aType);
				  if("NETBLOCK".equalsIgnoreCase(aType)){
				  if(isCidrNotation(aValue)){
					  aValue=cidrToRange(aValue);
				  }
				  String netrange[]=aValue.split("-");
				  if(netrange!=null&&netrange.length>1){

					  String netAddr1[]=netrange[0].trim().split("\\.");
					  String netAddr2[]=netrange[1].trim().split("\\.");
					  int addr00=Integer.valueOf(netAddr1[0]);
					  int addr01=Integer.valueOf(netAddr1[1]);
					  int addr10=Integer.valueOf(netAddr2[0]);
					  int addr11=Integer.valueOf(netAddr2[1]);
					  // To check for mask >=16 for IP ranges to be set
					  if((addr00^addr10)==0&&(addr01^addr11)==0){
					  node.setStartAddress(netrange[0].trim());
					  node.setEndAddress(netrange[1].trim());
				  node.setLow(this.ipToLong(netrange[0].trim()));
				  node.setHigh(this.ipToLong(netrange[1].trim()));
					  }else{
						  node.setStartAddress(aIpAddress.trim());
						  node.setEndAddress(aIpAddress.trim());
						  node.setLow(this.ipToLong(aIpAddress.trim()));
						  node.setHigh(this.ipToLong(aIpAddress.trim()));  
					  }
				   
				  }
				  }
				  break;
			  case  WHOIS_DESCR_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("DESCRIPTION pattern encountered for type "+aType);
				  if("NETBLOCK".equalsIgnoreCase(aType))
				  node.setDescription(aValue);
				  else if("ASN".equalsIgnoreCase(aType))
					  node.getRoute().setDescription(aValue);
				  break;
			  case  WHOIS_ORGCOUNTRY_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Organisation country pattern encountered for type: "+aType);
				  if("ORGANISATION".equalsIgnoreCase(aType))
				  node.getOrg().setCountry(aValue);
				  break;
			  case WHOIS_ORIGINASN_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("ASN pattern encountered");
				//  node.setOriginAS(aValue);TODO route info
				  if("ASN".equalsIgnoreCase(aType)){
					  node.setOriginAS(aValue);
					  node.getRoute().setOriginASN(aValue);
				  }
				  break;
			  case  WHOIS_PARENT_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Parent pattern encountered for type: "+aType);
				  if("NETBLOCK".equalsIgnoreCase(aType))
				  node.setParent(aValue);
				  break;
			  case WHOIS_NETNAME_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Net Name pattern encountered for type: "+aType);
				  if("NETBLOCK".equalsIgnoreCase(aType))
				  node.setNetName(aValue);
				  break;
			  case WHOIS_DATASOURCE_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("DataSource pattern encountered for type: "+aType);
				  
				  if("NETBLOCK".equalsIgnoreCase(aType))
				  node.setDataSource(aValue);
				  break;
			  case WHOIS_NETTYPE_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("NetType pattern encountered for type: "+aType);
				  if("NETBLOCK".equalsIgnoreCase(aType))
				  node.setNetType(aValue);
				  
				  break;
			  case WHOIS_ORGNAME_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("ORG Name pattern encountered for type: "+aType);
				  if("ORGANISATION".equalsIgnoreCase(aType))
				    node.getOrg().setOrgName(aValue);
				  break;	  
			  case WHOIS_ORGID_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("OrgId pattern encountered for type: "+aType);
				  if("ORGANISATION".equalsIgnoreCase(aType))
				  node.getOrg().setOrgId(aValue);
				  break;
			  case WHOIS_ORGADDRESS_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Org Address pattern encountered for type: "+aType);
				  
				  if("TECH".equalsIgnoreCase(aType)){
				   len=node.getOrgTech().size();
				  if(len>0){
					   orgTech=node.getOrgTech().get(len-1);
					   orgTech.setOrgTechAdrress((orgTech.getOrgTechAdrress()==null?"":orgTech.getOrgTechAdrress()+", ")+aValue);
				   }
				  }else if("ABUSE".equalsIgnoreCase(aType)){
					   len=node.getOrgAbuse().size();
						  if(len>0){
							  orgAbuse=node.getOrgAbuse().get(len-1);
							  orgAbuse.setOrgAbuseAddress((orgAbuse.getOrgAbuseAddress()==null?"":orgAbuse.getOrgAbuseAddress()+", ")+aValue);
						   }
						  }else if("ADMIN".equalsIgnoreCase(aType)){
							   len=node.getOrgAdmin().size();
								  if(len>0){
									  orgAdmin=node.getOrgAdmin().get(len-1);
									  orgAdmin.setOrgAdminAddress((orgAdmin.getOrgAdminAddress()==null?"":orgAdmin.getOrgAdminAddress()+", ")+aValue);
								   }
								  }			  
				  else if("ORGANISATION".equalsIgnoreCase(aType)){
					  node.getOrg().setAddress((node.getOrg().getAddress()==null?"":node.getOrg().getAddress()+", ")+aValue);
				  }
				  
				  break;
			  case WHOIS_ORGPHONE_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Org Phone pattern encountered for type: "+aType);
				  
				  if("TECH".equalsIgnoreCase(aType)){
					   len=node.getOrgTech().size();
					  if(len>0){
						   orgTech=node.getOrgTech().get(len-1);
						   orgTech.setOrgTechPhone((orgTech.getOrgTechPhone()==null?"":orgTech.getOrgTechPhone()+", ")+aValue);
					   }
					  }else if("ABUSE".equalsIgnoreCase(aType)){
						   len=node.getOrgAbuse().size();
							  if(len>0){
								  orgAbuse=node.getOrgAbuse().get(len-1);
								  orgAbuse.setOrgAbusePhone((orgAbuse.getOrgAbusePhone()==null?"":orgAbuse.getOrgAbusePhone()+", ")+aValue);
							   }
							  }else if("ADMIN".equalsIgnoreCase(aType)){
								   len=node.getOrgAdmin().size();
									  if(len>0){
										  orgAdmin=node.getOrgAdmin().get(len-1);
										  orgAdmin.setOrgAdminPhone((orgAdmin.getOrgAdminPhone()==null?"":orgAdmin.getOrgAdminPhone()+", ")+aValue);
									   }
									  }			  
					  else if("ORGANISATION".equalsIgnoreCase(aType)){
						  node.getOrg().setPhoneNo((node.getOrg().getPhoneNo()==null?"":node.getOrg().getPhoneNo()+", ")+aValue);
					  }
				  
				  break;
			  case WHOIS_ORGFAX_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Org Fax pattern encountered for type: "+aType);
				  
				  if("TECH".equalsIgnoreCase(aType)){
					   len=node.getOrgTech().size();
					  if(len>0){
						   orgTech=node.getOrgTech().get(len-1);
						   orgTech.setOrgTechFax((orgTech.getOrgTechFax()==null?"":orgTech.getOrgTechFax()+", ")+aValue);
					   }
					  }else if("ABUSE".equalsIgnoreCase(aType)){
						   len=node.getOrgAbuse().size();
							  if(len>0){
								  orgAbuse=node.getOrgAbuse().get(len-1);
								  orgAbuse.setOrgAbuseFax((orgAbuse.getOrgAbuseFax()==null?"":orgAbuse.getOrgAbuseFax()+", ")+aValue);
							   }
							  }else if("ADMIN".equalsIgnoreCase(aType)){
								   len=node.getOrgAdmin().size();
									  if(len>0){
										  orgAdmin=node.getOrgAdmin().get(len-1);
										  orgAdmin.setOrgAdminFax((orgAdmin.getOrgAdminFax()==null?"":orgAdmin.getOrgAdminFax()+", ")+aValue);
									   }
									  }else if("ORGANISATION".equalsIgnoreCase(aType)){
										  node.getOrg().setFaxNo((node.getOrg().getFaxNo()==null?"":node.getOrg().getFaxNo()+", ")+aValue);
					  }
				  
				  
				  break;
			  case WHOIS_ORGTECHHANDLE_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Org Tech Handle pattern encountered for type: "+aType);
                         
				  if("TECH".equalsIgnoreCase(aType)){
					   len=node.getOrgTech().size();
					  if(len>0){
						   orgTech=node.getOrgTech().get(len-1);
						   orgTech.setOrgTechHandle((orgTech.getOrgTechHandle()==null?"":orgTech.getOrgTechHandle()+", ")+aValue);
					   }
					  }else if("ABUSE".equalsIgnoreCase(aType)){
						   len=node.getOrgAbuse().size();
							  if(len>0){
								  orgAbuse=node.getOrgAbuse().get(len-1);
								  orgAbuse.setOrgAbuseHandle((orgAbuse.getOrgAbuseHandle()==null?"":orgAbuse.getOrgAbuseHandle()+", ")+aValue);
							   }
							  }else if("ADMIN".equalsIgnoreCase(aType)){
								   len=node.getOrgAdmin().size();
									  if(len>0){
										  orgAdmin=node.getOrgAdmin().get(len-1);
										  orgAdmin.setOrgAdminHandle((orgAdmin.getOrgAdminHandle()==null?"":orgAdmin.getOrgAdminHandle()+", ")+aValue);
									   }
									  }
				  
				  break;
			  case WHOIS_ORGTECHNAME_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Org Tech Name pattern encountered");
				  
				  if("TECH".equalsIgnoreCase(aType)){
					   len=node.getOrgTech().size();
					  if(len>0){
						   orgTech=node.getOrgTech().get(len-1);
						   orgTech.setOrgTechName((orgTech.getOrgTechName()==null?"":orgTech.getOrgTechName()+", ")+aValue);
					   }
					  }else if("ABUSE".equalsIgnoreCase(aType)){
						   len=node.getOrgAbuse().size();
							  if(len>0){
								  orgAbuse=node.getOrgAbuse().get(len-1);
								  orgAbuse.setOrgAbuseName((orgAbuse.getOrgAbuseName()==null?"":orgAbuse.getOrgAbuseName()+", ")+aValue);
							   }
							  }else if("ADMIN".equalsIgnoreCase(aType)){
								   len=node.getOrgAdmin().size();
									  if(len>0){
										  orgAdmin=node.getOrgAdmin().get(len-1);
										  orgAdmin.setOrgAdminName((orgAdmin.getOrgAdminName()==null?"":orgAdmin.getOrgAdminName()+", ")+aValue);
									   }
									  }
				  
				  
			        break;
			  case WHOIS_ORGTECHEMAIL_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Org Tech Email pattern encountered for type: "+aType);
				  
				  if("TECH".equalsIgnoreCase(aType)){
					   len=node.getOrgTech().size();
					  if(len>0){
						   orgTech=node.getOrgTech().get(len-1);
						   orgTech.setOrgTechEmail((orgTech.getOrgTechEmail()==null?"":orgTech.getOrgTechEmail()+", ")+aValue);
					   }
					  }else if("ABUSE".equalsIgnoreCase(aType)){
						   len=node.getOrgAbuse().size();
							  if(len>0){
								  orgAbuse=node.getOrgAbuse().get(len-1);
								  orgAbuse.setOrgAbuseEmail((orgAbuse.getOrgAbuseEmail()==null?"":orgAbuse.getOrgAbuseEmail()+", ")+aValue);
							   }
							  }else if("ADMIN".equalsIgnoreCase(aType)){
								   len=node.getOrgAdmin().size();
									  if(len>0){
										  orgAdmin=node.getOrgAdmin().get(len-1);
										  orgAdmin.setOrgAdminEmail((orgAdmin.getOrgAdminEmail()==null?"":orgAdmin.getOrgAdminEmail()+", ")+aValue);
									   }
									  }else if("ORGANISATION".equalsIgnoreCase(aType)){
										  node.getOrg().setEmail((node.getOrg().getEmail()==null?"":node.getOrg().getEmail()+", ")+aValue);
					  }
				  
				  
				  break;
				  
			  case WHOIS_UPDATEDATE_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Updated Date pattern encountered for type: "+aType);
				
				  
					  int ind=aValue.lastIndexOf(" ");
					  aValue=aValue.substring(ind+1).trim();
					  if(aValue!=null&&!"".equals(aValue)){
					date=parser.parse(aValue);
					  if("TECH".equalsIgnoreCase(aType)){
						   len=node.getOrgTech().size();
						  if(len>0){
							   orgTech=node.getOrgTech().get(len-1);
							  orgTech.setUpdatedDate(date);
						   }
						  }else if("ABUSE".equalsIgnoreCase(aType)){
							   len=node.getOrgAbuse().size();
								  if(len>0){
									  orgAbuse=node.getOrgAbuse().get(len-1);
									  orgAbuse.setUpdatedDate(date);
								   }
								  }else if("ADMIN".equalsIgnoreCase(aType)){
									   len=node.getOrgAdmin().size();
										  if(len>0){
											  orgAdmin=node.getOrgAdmin().get(len-1);
											  orgAdmin.setUpdatedDate(date);
										   }
										  }else if("ORGANISATION".equalsIgnoreCase(aType)){
											  node.getOrg().setUpdatedDate(date);
										  		}else if("ASN".equalsIgnoreCase(aType)){
										  			node.getRoute().setUpdatedDate(date);
										  		}
			}
				  break;
			
			  case WHOIS_REGDATE_PATTERN:
				  if(logger.isDebugEnabled())
					  logger.debug("Reg Date pattern encountered for type: "+aType);
				
				  
					  ind=aValue.lastIndexOf(" ");
					  aValue=aValue.substring(ind+1).trim();
					  if(aValue!=null&&!"".equals(aValue)){
					date=parser.parse(aValue);
					  if("TECH".equalsIgnoreCase(aType)){
						   len=node.getOrgTech().size();
						  if(len>0){
							   orgTech=node.getOrgTech().get(len-1);
							   orgTech.setRegDate(date);
						   }
						  }else if("ABUSE".equalsIgnoreCase(aType)){
							   len=node.getOrgAbuse().size();
								  if(len>0){
									  orgAbuse=node.getOrgAbuse().get(len-1);
									  orgAbuse.setRegDate(date);
								   }
								  }else if("ADMIN".equalsIgnoreCase(aType)){
									   len=node.getOrgAdmin().size();
										  if(len>0){
											  orgAdmin=node.getOrgAdmin().get(len-1);
											  orgAdmin.setRegDate(date);//TODO UPADTE
										   }
										  }else if("ORGANISATION".equalsIgnoreCase(aType)){
											  node.getOrg().setRegDate(date);
										  		}else if("ASN".equalsIgnoreCase(aType)){
										  			node.getRoute().setRegDate(date);
										  		}
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


