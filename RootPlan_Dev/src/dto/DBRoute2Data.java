package dto;

public class DBRoute2Data {
	String cid;
	String rid;
	String pt_order;
	String car_order;
	int start;
	int last;
	int size;
	public DBRoute2Data(String cid, String rid) {
		this.cid = cid;
		this.rid = rid;
	}
	public String getCid() {
		return cid;
	}	
	public String getRid() {
		return rid;
	}
	public String getPt_order() {
		return pt_order;
	}
	public void setPt_order(String pt_order) {
		this.pt_order = pt_order;
	}
	public String getCar_order() {
		return car_order;
	}
	public void setCar_order(String car_order) {
		this.car_order = car_order;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLast() {
		return last;
	}
	public void setLast(int last) {
		this.last = last;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
}
