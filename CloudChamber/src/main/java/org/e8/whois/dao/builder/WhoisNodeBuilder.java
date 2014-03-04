package org.e8.whois.dao.builder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.e8.whois.model.Organisation;
import org.e8.whois.model.OrganisationAbuse;
import org.e8.whois.model.OrganisationAdmin;
import org.e8.whois.model.OrganisationTech;
import org.e8.whois.model.WhoIsNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * WhoisNodeBuilder is used to build elements of whoisNode
 * 
 */
public class WhoisNodeBuilder {
	private final static Logger logger = LoggerFactory.getLogger(WhoisNodeBuilder.class);

	/**
	 * set whois node values from the result set passed.
	 * 
	 * @param resultSet
	 * @return WhoIsNode<Long>
	 * @throws SQLException
	 */
	public static WhoIsNode<Long> setWhoIsNodeValues(ResultSet resultSet) throws SQLException {
		if(logger.isDebugEnabled())
			logger.debug("Setting whoIs node values");

		WhoIsNode<Long> whoisNode = new WhoIsNode<Long>();

		whoisNode.setLow(resultSet.getLong(WhoisConstants.IP_START_ADDRESS_COLUMNNAME));

		whoisNode.setHigh(resultSet.getLong(WhoisConstants.IP_END_ADDRESS_COLUMNNAME));

		whoisNode.setOriginAS(resultSet.getString(WhoisConstants.ORIGIN_AS_COLUMNNAME));

		whoisNode.setNetName(resultSet.getString(WhoisConstants.NET_NAME_COLUMNNAME));

		whoisNode.setNetHandle(resultSet.getString(WhoisConstants.NET_HANDLE_COLUMNNAME));

		whoisNode.setParent(resultSet.getString(WhoisConstants.PARENT_COLUMNNAME));		

		whoisNode.setNetType(resultSet.getString(WhoisConstants.NET_TYPE_COLUMNNAME));

		whoisNode.setRef(resultSet.getString(WhoisConstants.NET_REF_COLUMNNAME));

		whoisNode.setDataSource(resultSet.getString(WhoisConstants.DATA_SOURCE_COLUMNNAME));

		whoisNode.setDescription(resultSet.getString(WhoisConstants.DESCRIPTION_COLUMNNAME));

		java.sql.Date regDate = resultSet.getDate(WhoisConstants.REG_DATE_COLUMNNAME);
		if(regDate !=null){
			Date registereddate = new Date(regDate.getTime());
			whoisNode.setRegDate(registereddate);
		}

		java.sql.Date updatedDate = resultSet.getDate(WhoisConstants.UPDATED_DATE_COLUMNNAME);
		if(updatedDate !=null){
			Date updateddate = new Date(updatedDate.getTime());
			whoisNode.setUpdatedDate(updateddate);
		}

		whoisNode.setCurrentData(resultSet.getBoolean(WhoisConstants.IS_CURRENT_DATA_COLUMNNAME));
		whoisNode.setRawResponse(resultSet.getString(WhoisConstants.RAW_RESPONSE_COLUMNNAME));
		return whoisNode;
	}

	/**
	 * Set Organisation values from the result set passed. 
	 * 
	 * @param resultSet
	 * @return Organisation
	 * @throws SQLException
	 */

	public static Organisation setOrganisationValues(ResultSet resultSet) throws SQLException {
		if(logger.isDebugEnabled())
			logger.debug("Setting Organisation values");

		Organisation org = new Organisation();

		org.setOrgName(resultSet.getString(WhoisConstants.ORG_NAME_COLUMNNAME));

		org.setOrgId(resultSet.getString(WhoisConstants.ORG_ID_COLUMNNAME));

		org.setPhoneNo(resultSet.getString(WhoisConstants.ORG_PHONE_COLUMNNAME));	

		org.setFaxNo(resultSet.getString(WhoisConstants.ORG_FAX_COLUMNNAME));

		org.setCity(resultSet.getString(WhoisConstants.CITY_COLUMNNAME));		

		org.setState(resultSet.getString(WhoisConstants.STATE_COLUMNNAME));

		org.setCountry(resultSet.getString(WhoisConstants.COUNTRY_COLUMNNAME));

		org.setPostalCode(resultSet.getString(WhoisConstants.POSTAL_CODE_COLUMNNAME));

		java.sql.Date orgRegDate =resultSet.getDate(WhoisConstants.ORG_REG_DATE_COLUMNNAME);
		if(orgRegDate !=null){
			Date orgRegisteredDate = new Date(orgRegDate.getTime());
			org.setRegDate(orgRegisteredDate);
		}

		Date orgUpdatedDate = resultSet.getDate(WhoisConstants.ORG_UPDATED_DATE_COLUMNNAME);
		if(orgUpdatedDate !=null){
			Date orgUpdDate = new Date(orgUpdatedDate.getTime());
			org.setUpdatedDate(orgUpdDate);
		}

		org.setRef(resultSet.getString(WhoisConstants.ORG_REF_COLUMNNAME));

		return org;
	}



	/**
	 * Set abuse object values
	 * 
	 * @param resultSet
	 * @return Organisation Abuse
	 * @throws SQLException
	 */
	public static OrganisationAbuse setAbuseContactListValues(ResultSet resultSet) throws SQLException {
		if(logger.isDebugEnabled())
			logger.debug("Setting abuse object values");

		OrganisationAbuse orgAbuse = new OrganisationAbuse();

		orgAbuse.setOrgAbuseHandle(resultSet.getString(WhoisConstants.HANDLE_COLUMNNAME));
		orgAbuse.setOrgAbuseName(resultSet.getString(WhoisConstants.NAME_COLUMNNAME));
		orgAbuse.setOrgAbuseAddress(resultSet.getString(WhoisConstants.ADDRESS_COLUMNNAME));
		orgAbuse.setOrgAbusePhone(resultSet.getString(WhoisConstants.PHONE_COLUMNNAME));
		orgAbuse.setOrgAbuseEmail(resultSet.getString(WhoisConstants.EMAIL_COLUMNNAME));
		orgAbuse.setOrgAbuseFax(resultSet.getString(WhoisConstants.FAX_COLUMNNAME));
		orgAbuse.setOrgAbuseRef(resultSet.getString(WhoisConstants.REF_COLUMNNAME));
		orgAbuse.setContactType(resultSet.getString(WhoisConstants.CONTACT_TYPE_COLUMNNAME));
		return orgAbuse;
	}
	
	/**
	 * Set tech object values
	 * 
	 * @param resultSet
	 * @return Organisation Tech
	 * @throws SQLException
	 */
	public static OrganisationTech setTechContactListValues(ResultSet resultSet) throws SQLException {
		if(logger.isDebugEnabled())
			logger.debug("Setting tech object values");

		OrganisationTech orgTech = new OrganisationTech();

		orgTech.setOrgTechHandle(resultSet.getString(WhoisConstants.HANDLE_COLUMNNAME));
		orgTech.setOrgTechName(resultSet.getString(WhoisConstants.NAME_COLUMNNAME));
		orgTech.setOrgTechAdrress(resultSet.getString(WhoisConstants.ADDRESS_COLUMNNAME));
		orgTech.setOrgTechPhone(resultSet.getString(WhoisConstants.PHONE_COLUMNNAME));
		orgTech.setOrgTechEmail(resultSet.getString(WhoisConstants.EMAIL_COLUMNNAME));
		orgTech.setOrgTechFax(resultSet.getString(WhoisConstants.FAX_COLUMNNAME));
		orgTech.setOrgTechRef(resultSet.getString(WhoisConstants.REF_COLUMNNAME));
		orgTech.setContactType(resultSet.getString(WhoisConstants.CONTACT_TYPE_COLUMNNAME));
		return orgTech;
	}
	
	/**
	 * Set admin object values
	 * 
	 * @param resultSet
	 * @return Organisation Admin
	 * @throws SQLException
	 */
	public static OrganisationAdmin setAdminContactListValues(ResultSet resultSet) throws SQLException {
		if(logger.isDebugEnabled())
			logger.debug("Setting admin object values");

		OrganisationAdmin orgAdmin = new OrganisationAdmin();

		orgAdmin.setOrgAdminHandle(resultSet.getString(WhoisConstants.HANDLE_COLUMNNAME));
		orgAdmin.setOrgAdminName(resultSet.getString(WhoisConstants.NAME_COLUMNNAME));
		orgAdmin.setOrgAdminAddress(resultSet.getString(WhoisConstants.ADDRESS_COLUMNNAME));
		orgAdmin.setOrgAdminPhone(resultSet.getString(WhoisConstants.PHONE_COLUMNNAME));
		orgAdmin.setOrgAdminEmail(resultSet.getString(WhoisConstants.EMAIL_COLUMNNAME));
		orgAdmin.setOrgAdminFax(resultSet.getString(WhoisConstants.FAX_COLUMNNAME));
		orgAdmin.setOrgAdminRef(resultSet.getString(WhoisConstants.REF_COLUMNNAME));
		orgAdmin.setContactType(resultSet.getString(WhoisConstants.CONTACT_TYPE_COLUMNNAME));
		return orgAdmin;
	}

	/**
	 * Set contact list for all contact types
	 * 
	 * @param resultSet
	 * @param whoisNode
	 * @return WhoIsNode with all contacts
	 * @throws SQLException
	 */
	public static WhoIsNode<Long> setContactListValues(ResultSet resultSet,WhoIsNode<Long> whoisNode) throws SQLException {
		if(logger.isDebugEnabled())
			logger.debug("Setting abuse contacts list");

		List<OrganisationAbuse> abuseContactList = Collections.emptyList();
		List<OrganisationTech> techContactList = Collections.emptyList();
		List<OrganisationAdmin> adminContactList = Collections.emptyList();

		while(resultSet!=null&&resultSet.next()){
			String contactType = resultSet.getString("contact_type");
			if(contactType !=null && contactType.equalsIgnoreCase("ADMIN")){
				OrganisationAdmin orgAdmin = setAdminContactListValues(resultSet);
				adminContactList.add(orgAdmin);
			}

			if(contactType !=null && contactType.equalsIgnoreCase("TECH")){
				OrganisationTech orgTech = setTechContactListValues(resultSet);
				techContactList.add(orgTech);
			}			

			if(contactType !=null && contactType.equalsIgnoreCase("ABUSE")){
				OrganisationAbuse orgAbuse = setAbuseContactListValues(resultSet);
				abuseContactList.add(orgAbuse);
			}

		}
		whoisNode.setOrgAbuse(abuseContactList);
		whoisNode.setOrgAdmin(adminContactList);
		whoisNode.setOrgTech(techContactList);
		return whoisNode;
	}

	/**
	 * Insert whois node information to DB tables.
	 * 
	 * @param whoisNode
	 * @param preparedStatement
	 * @throws SQLException
	 */

	public static void insertWhoisToDbValues(WhoIsNode<Long> whoisNode,PreparedStatement preparedStatement) throws SQLException {
		if(logger.isDebugEnabled())
			logger.debug("Inserts whois node information to DB tables.");

		preparedStatement.setLong(1, whoisNode.getLow());
		preparedStatement.setLong(2, whoisNode.getHigh());
		preparedStatement.setString(3,whoisNode.getOriginAS());
		preparedStatement.setString(4,whoisNode.getNetName());
		preparedStatement.setString(5, whoisNode.getNetHandle());
		preparedStatement.setString(6,whoisNode.getParent());
		preparedStatement.setString(7,whoisNode.getNetType());
		preparedStatement.setString(8,whoisNode.getRef());
		preparedStatement.setString(9,whoisNode.getDataSource());
		preparedStatement.setString(10,whoisNode.getDescription());

		if(whoisNode.getRegDate() !=null){
			java.sql.Timestamp regTimestamp = new java.sql.Timestamp(whoisNode.getRegDate().getTime());
			preparedStatement.setTimestamp(11,regTimestamp);
		}
		else{ 
			preparedStatement.setTimestamp(11, null);
		}		

		if(whoisNode.getUpdatedDate() !=null){
			java.sql.Timestamp updTimestamp = new java.sql.Timestamp(whoisNode.getUpdatedDate().getTime());
			preparedStatement.setTimestamp(12,updTimestamp);
		}
		else{ 
			preparedStatement.setTimestamp(12, null);
		}
		Organisation org = whoisNode.getOrg();
		if(org !=null){
			preparedStatement.setString(13,org.getOrgName());
			preparedStatement.setString(14,org.getOrgId());
			preparedStatement.setString(15,org.getPhoneNo());
			preparedStatement.setString(16,org.getFaxNo());
			preparedStatement.setString(17,org.getCity());
			preparedStatement.setString(18,org.getState());
			preparedStatement.setString(19,org.getCountry());
			preparedStatement.setString(20,org.getPostalCode());
			if(org.getRegDate() !=null){
				java.sql.Timestamp regTimestamp = new java.sql.Timestamp(org.getRegDate().getTime());
				preparedStatement.setTimestamp(21,regTimestamp);
			}
			else{
				preparedStatement.setTimestamp(21, null);
			}
			if(org.getUpdatedDate() !=null){
				java.sql.Timestamp regTimestamp = new java.sql.Timestamp(org.getUpdatedDate().getTime());
				preparedStatement.setTimestamp(22,regTimestamp);
			}
			else{
				preparedStatement.setTimestamp(22, null);
			}
			preparedStatement.setString(23,org.getRef());			
		}
		else{
			preparedStatement.setString(13,null);
			preparedStatement.setString(14,null);
			preparedStatement.setString(15,null);
			preparedStatement.setString(16,null);
			preparedStatement.setString(17,null);
			preparedStatement.setString(18,null);
			preparedStatement.setString(19,null);
			preparedStatement.setString(20,null);
			preparedStatement.setTimestamp(21,null);
			preparedStatement.setTimestamp(22,null);			
			preparedStatement.setString(23,null);
		}
		preparedStatement.setBoolean(24,whoisNode.isCurrentData());
		preparedStatement.setTimestamp(25,new java.sql.Timestamp(new Date().getTime()));
		preparedStatement.setString(26,whoisNode.getRawResponse());
	}

	/**
	 * Inserting technical contacts to DB tables.
	 * 
	 * @param whoisNode
	 * @param preparedStatement
	 * @throws SQLException
	 */

	public static void insertTechContactToDbValues(WhoIsNode<Long> whoisNode,PreparedStatement preparedStatement) throws SQLException {
		if(logger.isDebugEnabled())
			logger.debug("Inserts Technical contact information to DB tables.");

		List<OrganisationTech> techList = whoisNode.getOrgTech();		
		for(OrganisationTech tech:techList){
			preparedStatement.setLong(1, whoisNode.getLow());
			preparedStatement.setLong(2, whoisNode.getHigh());
			preparedStatement.setString(3, tech.getContactType());
			preparedStatement.setString(4,tech.getOrgTechHandle());
			preparedStatement.setString(5,tech.getOrgTechName());
			preparedStatement.setString(6, tech.getOrgTechAdrress());
			preparedStatement.setString(7,tech.getOrgTechPhone());
			preparedStatement.setString(8,tech.getOrgTechEmail());
			preparedStatement.setString(9,tech.getOrgTechFax());
			preparedStatement.setString(10,tech.getOrgTechRef());
			preparedStatement.setBoolean(11,tech.isCurrentdata());
			preparedStatement.setTimestamp(12,new java.sql.Timestamp(new Date().getTime()));
			preparedStatement.addBatch();
		}
	}


	/**
	 * Inserts abuse contact information to DB.
	 * 
	 * @param whoisNode
	 * @param preparedStatement
	 * @throws SQLException
	 */

	public static void insertAbuseContactToDbValues(WhoIsNode<Long> whoisNode,PreparedStatement preparedStatement) throws SQLException {
		if(logger.isDebugEnabled())
			logger.debug("Inserts Abuse contact information to DB tables.");

		List<OrganisationAbuse> abuseList = whoisNode.getOrgAbuse();
		for(OrganisationAbuse abuse:abuseList){
			preparedStatement.setLong(1, whoisNode.getLow());
			preparedStatement.setLong(2, whoisNode.getHigh());
			preparedStatement.setString(3,abuse.getContactType());
			preparedStatement.setString(4,abuse.getOrgAbuseHandle());
			preparedStatement.setString(5,abuse.getOrgAbuseName());
			preparedStatement.setString(6, abuse.getOrgAbuseAddress());
			preparedStatement.setString(7,abuse.getOrgAbusePhone());
			preparedStatement.setString(8,abuse.getOrgAbuseEmail());
			preparedStatement.setString(9,abuse.getOrgAbuseFax());
			preparedStatement.setString(10,abuse.getOrgAbuseRef());
			preparedStatement.setBoolean(11,abuse.isCurrentdata());
			preparedStatement.setTimestamp(12,new java.sql.Timestamp(new Date().getTime()));
			preparedStatement.addBatch();
		}
	}
	
	/**
	 * Inserts admin contact information to DB.
	 * 
	 * @param whoisNode
	 * @param preparedStatement
	 * @throws SQLException
	 */

	public static void insertAdminContactToDbValues(WhoIsNode<Long> whoisNode,PreparedStatement preparedStatement) throws SQLException {
		if(logger.isDebugEnabled())
			logger.debug("Inserts Admin contact information to DB tables.");

		List<OrganisationAdmin> adminList = whoisNode.getOrgAdmin();
		for(OrganisationAdmin admin:adminList){
			preparedStatement.setLong(1, whoisNode.getLow());
			preparedStatement.setLong(2, whoisNode.getHigh());
			preparedStatement.setString(3,admin.getContactType());
			preparedStatement.setString(4,admin.getOrgAdminHandle());
			preparedStatement.setString(5,admin.getOrgAdminName());
			preparedStatement.setString(6, admin.getOrgAdminAddress());
			preparedStatement.setString(7,admin.getOrgAdminPhone());
			preparedStatement.setString(8,admin.getOrgAdminEmail());
			preparedStatement.setString(9,admin.getOrgAdminFax());
			preparedStatement.setString(10,admin.getOrgAdminRef());
			preparedStatement.setBoolean(11,admin.isCurrentdata());
			preparedStatement.setTimestamp(12,new java.sql.Timestamp(new Date().getTime()));
			preparedStatement.addBatch();
		}
	}
}
