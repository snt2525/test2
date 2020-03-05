package dto;

import java.util.Arrays;

public class DBRouteData {
	private String rid; //몇번째 사이즈 인지
	private String cid; //사용자 id;
	private int datasize; //경로가 몇개 저장되어 있는지
	private String name; 

	private String[] address = new String[8];
	private double[] lat = new double[8];
	private double[] lng = new double[8];
	
	public DBRouteData(String rid,String cid){
	     Arrays.fill(address,"");
	     Arrays.fill(lat, 0);
	     Arrays.fill(lng, 0);
	     this.rid = rid;
	     this.cid = cid;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDataSize(int size) {
		this.datasize = size;
	}
	
	public void setAddress(int i,String a) {
		this.address[i] = a;
	}
	
	public void setLat(int i,double a) {
		this.lat[i] = a;
	}
	
	public void setLng(int i,double a) { 
		this.lng[i] = a;
	}
	
	public String getRid() {
		return rid;
	}

	public String getCid() {
		return cid;
	}

	public int getDatasize() {
		return datasize;
	}

	public String getAddress(int i) {
		return address[i];
	}

	public double getLat(int i) {
		return lat[i];
	}

	public double getLng(int i) {
		return lng[i];
	}
}
