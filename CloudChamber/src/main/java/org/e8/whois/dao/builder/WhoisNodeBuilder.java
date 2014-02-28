package org.e8.whois.dao.builder;

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
			orgTech.setOrgAdrress(resultSet.getString(5));
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

}
