package dto;

import java.util.LinkedList;

public class InfoCar {
	private double sx;
	private double sy;
	private double ex;
	private double ey;
	private int distance;
	private boolean walk; // 걷기면 true
	private int time;
	private int fare;
	private LinkedList<DataPair> lineList; // line에 x,y 정보
	
	public InfoCar(){
		lineList = new LinkedList<DataPair>();
	}
	
	public int getLineListSize() {
		return lineList.size();
	}
	
	public boolean isWalk() {
		return walk;
	}

	public void setWalk(boolean walk) {
		this.walk = walk;
	}

	public double getSx() {
		return sx;
	}
	public void setSx(double sx) {
		this.sx = sx;
	}
	public double getSy() {
		return sy;
	}
	public void setSy(double sy) {
		this.sy = sy;
	}
	public double getEx() {
		return ex;
	}
	public void setEx(double ex) {
		this.ex = ex;
	}
	public double getEy() {
		return ey;
	}
	public void setEy(double ey) {
		this.ey = ey;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getFare() {
		return fare;
	}
	public void setFare(int fare) {
		this.fare = fare;
	}
	public DataPair getLineList(int idx) {
		return lineList.get(idx);
	}
	public void addLineList(DataPair pair) {
		lineList.add(pair);
	}
	
	/*public void print() { // 출력 용
		//System.out.println("출력하기");
		//System.out.println("거리 : " + this.getDistance());
		//System.out.println("시간 : " + this.getTime());
		for(int i=0; i<this.lineList.size(); i++) {
			//System.out.println(this.lineList.get(i).getX() + " , " + this.lineList.get(i).getY());
		}
	}*/
}
