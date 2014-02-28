package org.e8.whois.dao.builder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.e8.whois.model.Organisation;
import org.e8.whois.model.OrganisationAbuse;
import org.e8.whois.model.OrganisationTech;
import org.e8.whois.model.WhoIsNode;

public class WhoisNodeBuilder {

	public static WhoIsNode<Long> setWhoIsNodeValues(ResultSet resultSet) throws SQLException {

		WhoIsNode<Long> whoisNode = new WhoIsNode<Long>();

		whoisNode.setLow(resultSet.getLong(1));

		whoisNode.setHigh(resultSet.getLong(2));

		whoisNode.setOriginAS(resultSet.getString(3));

		whoisNode.setNetName(resultSet.getString(4));

		whoisNode.setNetHandle(resultSet.getString(5));

		whoisNode.setParent(resultSet.getString(6));		

		whoisNode.setNetType(resultSet.getString(7));

		whoisNode.setRef(resultSet.getString(8));

		whoisNode.setDataSource(resultSet.getString(9));

		whoisNode.setDescription(resultSet.getString(10));

		java.sql.Date regDate = resultSet.getDate(11);
		if(regDate !=null){
			Date registereddate = new Date(regDate.getTime());
			whoisNode.setRegDate(registereddate);
		}

		java.sql.Date updatedDate = resultSet.getDate(12);
		if(updatedDate !=null){
			Date updateddate = new Date(updatedDate.getTime());
			whoisNode.setUpdatedDate(updateddate);
		}

		whoisNode.setIsCurrentData(resultSet.getBoolean(24));
		whoisNode.setRawResponse(resultSet.getString(26));
		return whoisNode;
	}

	public static Organisation setOrganisationValues(ResultSet resultSet) throws SQLException {
		Organisation org = new Organisation();

		org.setOrgName(resultSet.getString(13));

		org.setOrgId(resultSet.getString(14));

		org.setPhoneNo(resultSet.getString(15));	

		org.setFaxNo(resultSet.getString(16));

		org.setCity(resultSet.getString(17));		

		org.setState(resultSet.getString(18));

		org.setCountry(resultSet.getString(19));

		org.setPostalCode(resultSet.getString(20));

		java.sql.Date orgRegDate =resultSet.getDate(21);
		if(orgRegDate !=null){
			Date orgRegisteredDate = new Date(orgRegDate.getTime());
			org.setRegDate(orgRegisteredDate);
		}

		Date orgUpdatedDate = resultSet.getDate(22);
		if(orgUpdatedDate !=null){
			Date orgUpdDate = new Date(orgUpdatedDate.getTime());
			org.setUpdatedDate(orgUpdDate);
		}

		org.setRef(resultSet.getString(23));

		return org;
	}

	public static List<OrganisationTech> setTechContactListValues(ResultSet resultSet) throws SQLException {
		List<OrganisationTech> techContactList = new ArrayList<OrganisationTech>();
		while(resultSet!=null&&resultSet.next()){
			OrganisationTech orgTech = new OrganisationTech();

			orgTech.setOrgTechHandle(resultSet.getString(3));
			orgTech.setOrgTechName(resultSet.getString(4));
			orgTech.setOrgTechAdrress(resultSet.getString(5));
			orgTech.setOrgTechPhone(resultSet.getString(6));
			orgTech.setOrgTechEmail(resultSet.getString(7));
			orgTech.setOrgTechFax(resultSet.getString(8));
			orgTech.setOrgTechRef(resultSet.getString(9));

			techContactList.add(orgTech);
		}
		return techContactList;
	}

	public static List<OrganisationAbuse> setAbuseContactListValues(ResultSet resultSet) throws SQLException {
		List<OrganisationAbuse> abuseContactList = new ArrayList<OrganisationAbuse>();
		while(resultSet!=null&&resultSet.next()){
			OrganisationAbuse orgAbuse = new OrganisationAbuse();

			orgAbuse.setOrgAbuseHandle(resultSet.getString(3));
			orgAbuse.setOrgAbuseName(resultSet.getString(4));
			orgAbuse.setOrgAbuseAddress(resultSet.getString(5));
			orgAbuse.setOrgAbusePhone(resultSet.getString(6));
			orgAbuse.setOrgAbuseEmail(resultSet.getString(7));
			orgAbuse.setOrgAbuseRef(resultSet.getString(8));

			abuseContactList.add(orgAbuse);
		}
		return abuseContactList;
	}

	public static void insertWhoisToDbValues(WhoIsNode<Long> whoisNode,PreparedStatement preparedStatement) throws SQLException {
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
		preparedStatement.setBoolean(24,whoisNode.getIsCurrentData());
		preparedStatement.setTimestamp(25,new java.sql.Timestamp(new Date().getTime()));
		preparedStatement.setString(26,whoisNode.getRawResponse());
	}

	public static void insertTechContactToDbValues(WhoIsNode<Long> whoisNode,PreparedStatement preparedStatement) throws SQLException {
		List<OrganisationTech> techList = whoisNode.getOrgTech();		
		for(OrganisationTech tech:techList){
			preparedStatement.setLong(1, whoisNode.getLow());
			preparedStatement.setLong(2, whoisNode.getHigh());
			preparedStatement.setString(3,tech.getOrgTechHandle());
			preparedStatement.setString(4,tech.getOrgTechName());
			preparedStatement.setString(5, tech.getOrgTechAdrress());
			preparedStatement.setString(6,tech.getOrgTechPhone());
			preparedStatement.setString(7,tech.getOrgTechEmail());
			preparedStatement.setString(8,tech.getOrgTechFax());
			preparedStatement.setString(9,tech.getOrgTechRef());
			preparedStatement.setBoolean(10,tech.isCurrentdata());
			preparedStatement.setTimestamp(11,new java.sql.Timestamp(new Date().getTime()));
			preparedStatement.addBatch();
		}
	}

	public static void insertAbuseContactToDbValues(WhoIsNode<Long> whoisNode,PreparedStatement preparedStatement) throws SQLException {
		List<OrganisationAbuse> abuseList = whoisNode.getOrgAbuse();
		for(OrganisationAbuse abuse:abuseList){
			preparedStatement.setLong(1, whoisNode.getLow());
			preparedStatement.setLong(2, whoisNode.getHigh());
			preparedStatement.setString(3,abuse.getOrgAbuseHandle());
			preparedStatement.setString(4,abuse.getOrgAbuseName());
			preparedStatement.setString(5, abuse.getOrgAbuseAddress());
			preparedStatement.setString(6,abuse.getOrgAbusePhone());
			preparedStatement.setString(7,abuse.getOrgAbuseEmail());
			preparedStatement.setString(8,abuse.getOrgAbuseRef());
			preparedStatement.setBoolean(9,abuse.isCurrentdata());
			preparedStatement.setTimestamp(10,new java.sql.Timestamp(new Date().getTime()));
			preparedStatement.addBatch();
		}
	}
}
