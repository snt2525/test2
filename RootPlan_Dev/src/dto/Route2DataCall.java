package dto;

public class Route2DataCall {
	int[] pt_order;
	int[] car_order;
	int start;
	int last;
	int size;
	public Route2DataCall() {
		
	}
	public void pushData(String pt_order_str, String car_order_str, int size, int start, int last) {
		String[] tmpPT = pt_order_str.split(",");
		String[] tmpCar = car_order_str.split(",");
		
		pt_order = new int[size];
		car_order = new int[size];
		
		for(int i =0;i<size;i++) {
			this.pt_order[i] = Integer.parseInt(tmpPT[i]);
			this.car_order[i] = Integer.parseInt(tmpCar[i]);			
		}	
		this.size = size;
		this.start = start;
		this.last = last;
	}
	public int getPt_order(int i) {
		return pt_order[i];
	}
	public int getCar_order(int i) {
		return car_order[i];
	}
	public int getStart() {
		return start;
	}
	public int getLast() {
		return last;
	}
	public int getSize() {
		return size;
	}
	
}
