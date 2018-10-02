package dto;

import java.util.LinkedList;

public class InfoPT {
	private double sx; // 요청시
	private double sy; // 요청시
	private double ex; // 요청시
	private double ey; // 요청시
	private String firstStartStation; 
	private String lastEndStation;
	private int stationCount;
	private int totalDistance; // // 응답 : totalDistance
	private boolean walk; // 걷기면 true, 버스나 지하철이면 false
	private int totalTime; // 응답 : totalTime
	private int fare; // 응답 : payment
	//private int stationCount; // stationCount로  DataPair 정보 가짐
	private LinkedList<InfoSectionPT> section; // 환승 정보, cntTransfer만큼 만들기
	private LinkedList<DataPair> lineList; // 환승에 상관없이 좌표는 연속적으로 저장
	private String errorMsg;  // 에러 메세지
	private boolean error; // error 존재시 1, 없으면 0
	
	public InfoPT(){
		section = new LinkedList<InfoSectionPT>(); // 환승 없을 수도 있음
		lineList = new LinkedList<DataPair>();
		error = false;
	}

	
	public int getStationCount() {
		return stationCount;
	}


	public void setStationCount(int stationCount) {
		this.stationCount = stationCount;
	}


	public String getErrorMsg() {
		return errorMsg;
	}


	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	public boolean isError() {
		return error;
	}


	public void setError(boolean error) {
		this.error = error;
	}


	public int getSectionSize() {
		return section.size();
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

	public int getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(int totalDistance) {
		this.totalDistance = totalDistance;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	public boolean isWalk() {
		return walk;
	}

	public void setWalk(boolean walk) {
		this.walk = walk;
	}
	

	public String getFirstStartStation() {
		return firstStartStation;
	}

	public void setFirstStartStation(String firstStartStation) {
		this.firstStartStation = firstStartStation;
	}

	public String getLastEndStation() {
		return lastEndStation;
	}

	public void setLastEndStation(String lastEndStation) {
		this.lastEndStation = lastEndStation;
	}

	public int getFare() {
		return fare;
	}

	public void setFare(int fare) {
		this.fare = fare;
	}
	
	public int getLineListSize() {
	      return lineList.size();
	}
	
	public InfoSectionPT getSection(int idx) {
		return section.get(idx);
	}
	public void addSection(InfoSectionPT item) {
		section.add(item);
	}
	public DataPair getLineList(int idx) {
		return lineList.get(idx);
	}
	public void addLineList(DataPair pair) {
		lineList.add(pair);
	}

	/*public void print() {
		System.out.println("sx, sy= "+this.getSx()+","+this.getSy());
		System.out.println("ex, ey= "+this.getEx()+","+this.getEy());
		System.out.println("첫 정거장, 마지막 정거장 : " + this.getFirstStartStation() + " , " + this.getLastEndStation());
		System.out.println("총거리 , 총시간 : " + this.getTotalDistance() + ", " + this.getTotalTime());
		System.out.println("총비용 : " + this.getFare());
		System.out.println("총환승 횟수 : " + this.getSectionSize());
		
		for(int i=0; i<this.getSectionSize(); i++) {
			InfoSectionPT b = this.getSection(i);
			System.out.println("1:지하철, 2:버스 = " + b.getTrafficType());
			System.out.println("시작, 끝 = " + b.getStartStation() + " , " + b.getEndStation());
			System.out.println("거리, 시간 = " + b.getSectionDistance() + ", " + b.getSectionTime());
			if(b.getTrafficType()==1) {
				System.out.println("지하철 노선 : " + b.getSubwayLine());	
			}else {
				for(int j=0; j<b.getBusNoListSize(); j++) 
					System.out.println("버스 : " + b.getBusNoList(j));
			}
		}
	}*/
}
