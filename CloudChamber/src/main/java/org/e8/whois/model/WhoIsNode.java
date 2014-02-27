package org.e8.whois.model;

import java.util.Date;
import java.util.List;


public class WhoIsNode<T> implements Comparable<WhoIsNode<T>> {


	private String originAS;
	private String netName;
	private String netHandle;
	private String parent;
	private String netType;
	private String description;
	private String dataSource;
	private Date regDate;
	private Date updatedDate;
	private String ref;
	private int isCurrentData =1;//1 implies current data 0 - historic data// whynot boolean
	
	public int getIsCurrentData() {
		return isCurrentData;
	}

	public void setIsCurrentData(int isCurrentData) {
		this.isCurrentData = isCurrentData;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public T low;
    public T high;
    public void setLow(T low) {
		this.low = low;
	}

	public void setHigh(T high) {
		this.high = high;
		this.max=high;
		this.height=1;
	}

	private Organisation org;
    private List<OrganisationAbuse> orgAbuse;
    private List<OrganisationTech> orgTech;
    
    public Organisation getOrg() {
		return org;
	}

	public void setOrg(Organisation org) {
		this.org = org;
	}

	public List<OrganisationAbuse> getOrgAbuse() {
		return orgAbuse;
	}

	public void setOrgAbuse(List<OrganisationAbuse> orgAbuse) {
		this.orgAbuse = orgAbuse;
	}

	public List<OrganisationTech> getOrgTech() {
		return orgTech;
	}

	public void setOrgTech(List<OrganisationTech> orgTech) {
		this.orgTech = orgTech;
	}

	public T getLow(){
    	return this.low;
}

    public T getHigh(){
    	return this.high;
}
	public WhoIsNode<T> left;
    public WhoIsNode<T> right;
    public T max;
    public int height;
    public WhoIsNode(){
    
    }
    public WhoIsNode(T l, T h){
        this.low=l;
        this.high=h;
        this.max=high;
        this.height=1;
    }
    
    public int compareTo(WhoIsNode<T> nod){
    Comparable cLow=(Comparable) low;
    Comparable cHigh=(Comparable) high;
    Comparable nLow=(Comparable) nod.low;
    Comparable nHigh=(Comparable) nod.high; 
    if((cLow.compareTo(nLow)<0&&nHigh.compareTo(cHigh)<0)||(cLow.compareTo(nHigh))>0){
    		return 1;
    }
    
    if((cLow.compareTo(nLow)>0&&nHigh.compareTo(cHigh)>0)||(nLow.compareTo(cHigh))>0){
    		return -1;
    }
    
    return 0;
}

	
	public String getOriginAS() {
		return originAS;
	}
	public void setOriginAS(String originAS) {
		this.originAS = originAS;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getNetHandle() {
		return netHandle;
	}
	public void setNetHandle(String netHandle) {
		this.netHandle = netHandle;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getNetType() {
		return netType;
	}
	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	
	

}
