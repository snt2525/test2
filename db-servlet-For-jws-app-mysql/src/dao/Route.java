package dao;

import callApi.ApiCarSearch;
import callApi.ApiPTSearch;
import dto.DBRoute2Data;
import dto.DataPair;
import dto.DataTotal;
import dto.InfoCar;
import dto.InfoPT;
import dto.InfoSectionPT;
import dto.Route2DataCall;
import dto.SetData;

public class Route {
	ApiPTSearch pt;
    ApiCarSearch cs;
    Shortpath sp;
    int listSize;

    public int carFlag = 0;
    public int ptFlag = 0;
    public int size = 0;
    public DataTotal dataTotal;
    
    public Route(int listSize){
    	this.listSize = listSize;
    	dataTotal = new DataTotal(listSize);
        sp = new Shortpath();
    }
    
    public void Clear() { 
    	dataTotal.carList.clear();
    	dataTotal.ptList.clear();
        carFlag = 0;
        ptFlag = 0;
        size = 0;     
    }
   //DB에 저장 할때 데이터를 DBRoute2Data안에 넣어준다
   public DBRoute2Data putRoute2Dto(DBRoute2Data tmp,int start,int last) {
	   String car = "";
	   String pt = "";
	   int carSize = dataTotal.carAns.length;
	   if(start!=last)carSize--;
	   for(int i = 0;i < carSize;i++) {
		   car += Integer.toString(dataTotal.carAns[i])+",";
		   pt += Integer.toString(dataTotal.ptAns[i])+",";
	   }
	   System.out.println("db에 들어가는 car_order: "+car+"  db에 들어가는 pt_order: "+pt);
	   tmp.setCar_order(car);
	   tmp.setPt_order(pt);
	   tmp.setSize(carSize);
	   tmp.setStart(start);
	   tmp.setLast(last);	
	   return tmp;
   }
   
/*   public void printAns(int size) {
	   System.out.print("dataTotal.carAns: ");
	   for(int i =0; i<size; i++) {
		   System.out.print(dataTotal.carAns[i]+", ");
	   }
	   System.out.println();
	   System.out.print("dataTotal.ptAns: ");
	   for(int i =0; i<size; i++) {
		   System.out.print(dataTotal.ptAns[i]+", ");
	   }
	   System.out.println();
   }*/
   
   //여기서 dataTotal에 데이터도 넣고, recall도 해준다
   public void putDTO_AND_reCall(Route2DataCall tmp, AddressDataManager ad) {
	   int size = tmp.getSize();
	      int start = tmp.getStart();
	      int last = tmp.getLast();
	         for(int i = 0;i < size; i++) {
	            dataTotal.carAns[i] = tmp.getCar_order(i);
	            dataTotal.ptAns[i] = tmp.getPt_order(i); 
	         }
	    //     printAns(size); //출력
	         System.out.println("ssssssssssss"+size);
	      
	      //recallApiData();얘를 호출 102번째 줄에 있음.   
	       if(start == last) size--;
	      pt = new ApiPTSearch(ad.getList(), dataTotal, size);
	      cs = new ApiCarSearch(ad.getList(), dataTotal, size);
	      
	      //재호출
	      recallApiData(0, start, last);
	      recallApiData(1, start, last);
   }
    
   public boolean callAPIData(int a, int b, String car, AddressDataManager ad, SetData sd) {     
	   //size = ad.addressData.size();
	   if(sd.GetStartData() == sd.GetLastData()) {
		   listSize++;
	   }
	   System.out.println("listSize : " + listSize);
       pt = new ApiPTSearch(ad.getList(), dataTotal, listSize);
	   size = ad.addressData.size();
      //대중교통  API 호출 & 동시에 걷기도 호출해서 이차원배열 채우기
        System.out.println("대중교통 호출");
        pt.callTransportApi(a, b);    
        //System.out.println("car : " + car);
        if(car.equals("0")) {      
           sp.init(ad.addressData.size(),dataTotal);
        	//sp = new Shortpath(ad.addressData.size(),dataTotal);
           //자동차 api호출
            System.out.println("자동차호출");           
            ptFlag = 1; //대중됴통 호출 끌
            cs = new ApiCarSearch(ad.getList(), dataTotal, listSize);
            cs.carApi(); //자동차 API call 
 		    carFlag = 1; //자동차 호출 끌 
            return false;
        }
        return true;
	}	
   
 /*  void print(int size) {
      System.out.print("자동차 : ");
      for(int i=0; i<size; i++) {
         System.out.print(dataTotal.carAns[i]+" ");
      }
      System.out.println();
      System.out.print("대중교통 : ");
      for(int i=0; i<size; i++) {
         System.out.print(dataTotal.ptAns[i]+" ");
      }
      System.out.println();
   }
*/
   public void recallApiData(int how, int start, int end) {
	   if(how == 0) {
		   pt.resultOrderCall(dataTotal.ptAns, start, end);			
	   }
	   else {
		  cs.resultOrderCall(dataTotal.carAns, start, end);
	   }
	   
   }
   
   public void callShortestPath(int start, int last, int isSame, int how) { 
	   if(how == 1) {
         sp.callDFS(start, last, 1, isSame);
         recallApiData(1, start, last);  // 자동차 재호출
      	 //cs.resultOrderCall(sp.carAns); //결과 순서로 api 다시 호출, 자동차
      }else { 
         sp.callDFS(start, last, 0, isSame);
         recallApiData(0, start, last);  // 대중교통 재호출
        // pt.resultOrderCall(sp.ptAns); //결과 순서로 api 다시 호출, 대중교통
      }
   } 
   
   public String orderResult(int how, AddressDataManager ad) { //결과 데이터 보내기 
	   String  result = "";
	   size =  ad.addressData.size();
	   if(how == 0) { 
		   result += "<ResultData>";
		   for(int i = 0;i<size;i++) {
			  result += "<Data>"; 
		      result += "<lat>"+ Double.toString(ad.addressData.get(dataTotal.ptAns[i]).getLat()) +"</lat>";
		      result += "<lng>"+ Double.toString(ad.addressData.get(dataTotal.ptAns[i]).getLng()) +"</lng>";
		      result += "</Data>"; 
		   }
		   result += "</ResultData>";
	   }else if(how == 1) {
		   result += "<ResultData>";
		   for(int i = 0;i<size;i++) {
			  result += "<Data>"; 
		      result += "<lat>"+ Double.toString(ad.addressData.get(dataTotal.carAns[i]).getLat()) +"</lat>";
		      result += "<lng>"+ Double.toString(ad.addressData.get(dataTotal.carAns[i]).getLng()) +"</lng>";
		      result += "</Data>"; 
		   }
		   result += "</ResultData>";		   
	   }
	   System.out.println("사이즈: "+size+"마크 : "+result);
	   return result;
   }
   
   public String resultPoly(int how) { // 0:pt, 1:car
	   
	   // 그릴떄 0:도보, 1:그외
	   String result ="";
	   if(how==0) {
		   result += "<ptData>";
		   for(int i=0; i<dataTotal.ptList.size();i++) {
			   int InfoPTSize = dataTotal.ptList.get(i).getLineListSize();
			   boolean isWalk = dataTotal.ptList.get(i).isWalk();
			   
			   result += "<Data>";
			   result += "<no>"+i+"</no>";
			   result += "<lno>0</lno>";
			   result += "<end>0</end>";
			   if(isWalk)  result += "<walk>0</walk>";
			   else    result += "<walk>1</walk>";
			   result += "<lat>" +Double.toString(dataTotal.ptList.get(i).getSx())+ "</lat>";
			   result += "<lng>" +Double.toString(dataTotal.ptList.get(i).getSy())+ "</lng>";
			   result += "</Data>";
			   
			   for(int j=0; j<InfoPTSize; j++) {
				   DataPair pair = dataTotal.ptList.get(i).getLineList(j);
				   result += "<Data>";
				   result += "<no>"+i+"</no>";
				   result += "<end>0</end>";
				   if(isWalk)  result += "<walk>0</walk>";
				   else    result += "<walk>1</walk>";
				   result += "<lat>" + Double.toString(pair.getX()) + "</lat>";
				   result += "<lng>" + Double.toString(pair.getY()) + "</lng>";
				   result += "</Data>";
			   }
			   result += "<Data>";
			   result += "<no>"+i+"</no>";
			   result += "<end>1</end>";
			   if(isWalk)  result += "<walk>0</walk>";
			   else    result += "<walk>1</walk>";
			   result += "<lat>" +Double.toString(dataTotal.ptList.get(i).getEx())+ "</lat>";
			   result += "<lng>" +Double.toString(dataTotal.ptList.get(i).getEy())+ "</lng>";
			   result += "</Data>";
		   }
		   result += "</ptData>";
		   
	   }else if(how==1) {
		   result += "<carData>";
		   for(int i=0; i<dataTotal.carList.size(); i++) {
			   int lineListSize = dataTotal.carList.get(i).getLineListSize();
			   result += "<Data>";
			   result +=  "<no>" + i + "</no>";
			   result += "<end>1</end>";
			   result += "<lat>" + Double.toString(dataTotal.carList.get(i).getSx())+"</lat>";
			   result += "<lng>" + Double.toString(dataTotal.carList.get(i).getSy())+"</lat>";
			   result += "</Data>";
			   
			   for(int j=0; j<lineListSize; j++) {
				   DataPair pair = dataTotal.carList.get(i).getLineList(j);
				   result += "<Data>";
				   result +=  "<no>" + i + "</no>";
				   result += "<end>0</end>";
				   if(dataTotal.carList.get(i).isWalk())  result += "<walk>0</walk>";
				   else    result += "<walk>1</walk>";
				   result += "<lat>" + Double.toString(pair.getX()) + "</lat>";
				   result += "<lng>" + Double.toString(pair.getY()) + "</lng>";
				   result += "</Data>";
			   }
		   }
		   result += "</carData>";
	   }
	   return result;
   }   
   
/*   void print(AddressDataManager ad, SetData sd) {
	   System.out.println("처음, 끝 : " + sd.GetStartData() + " , " + sd.GetLastData());
	   int listSize = ad.addressData.size();
	   System.out.print("리스트 보여주기 : ");
	   for(int i=0; i<listSize; i++) {
		   System.out.print(ad.addressData.get(i).getAddress() + " , ");
	   }
	   System.out.println();
   }*/
   
   public String resultList(int how, AddressDataManager ad, SetData sd) { // 0:pt, 1:car
	   String result="";
	   int adSize = ad.addressData.size();
	   if(sd.GetStartData() == sd.GetLastData()) {
		   adSize++;
	   }
	   
	  // print(ad, sd);
	   if(how==0) {
		   result += "<resultPTList>";
		   int sectionSize=0;
		   
		   // -1번 지점 // adSize 보내기 
		   result += "<Data>";
		   result += "<check>-1</check>";
		   result += "<wayCount>"+adSize+"</wayCount>";
		   if(sd.GetLastData()==sd.GetStartData()) result += "<cycle>1</cycle>";
		   else result += "<cycle>0</cycle>";
		   result += "</Data>";
		   
		   // 0번 지점
		   for(int k=0; k<ad.addressData.size(); k++) {
			   result += "<Data>";
			   result += "<check>0</check>";
			   result += "<title>" + ad.addressData.get(dataTotal.ptAns[k]).getAddress() + "</title>";
			   result += "</Data>";
		   }
		   
		   if(sd.GetStartData()==sd.GetLastData()) {
			   result += "<Data>";
			   result += "<check>0</check>";
			   result += "<title>" + ad.addressData.get(sd.GetLastData()).getAddress() + "</title>";
			   result += "</Data>";
		   }
		   
		   System.out.println("dataTotal.ptList.size : " +dataTotal.ptList.size());
		   
		   for(int i=0; i<dataTotal.ptList.size(); i++) {
			   InfoPT info = dataTotal.ptList.get(i);
			   // 처음에 버스번호 여러개(시작 역이름)->지하철 번호(시작 역이름) -> 버스번호 여러개(시작 역이름)
			   
			   // 1번 지점에서 보일 내용 : 경로 1->2 지점에서 띄어줄 내용 
			   result += "<Data>";
			   result += "<check>1</check>";
			   if(info.isWalk()) result += "<walk>true</walk>";
			   else result += "<walk>false</walk>";
			   result += "<totalTime>" + info.getTotalTime() + "</totalTime>";
			   result += "<totalDistance>" + info.getTotalDistance() + "</totalDistance>";
			   result += "<totalFare>" + info.getFare() + "</totalFare>";
			   result += "<totalStationCount>" + info.getStationCount() + "</totalStationCount>";
			   // 각 세부의 정보 
			   sectionSize = info.getSectionSize();
			   result += "<sectionSize>"+Integer.toString(info.getSectionSize()) + "</sectionSize>";
			   result += "</Data>";
			   
			   // 2번 지점에서 보일 내용
			   for(int j=0; j<info.getSectionSize(); j++) {
				   InfoSectionPT tmpSec = info.getSection(j);
				   result += "<Data>";
				   result += "<check>2</check>";
				   //System.out.println("trafficType은 뭐지 : " + tmpSec.getTrafficType());
				   if(tmpSec.getTrafficType()==1) result += "<trafficType>지하철</trafficType>";
				   else result += "<trafficType>버스</trafficType>";
				   result += "<bus>";
				   for(int k=0; k<tmpSec.getBusNoListSize(); k++) {
					   result += tmpSec.getBusNoList(k);
					   if(k!=tmpSec.getBusNoListSize()-1) result += ", ";
				   }
				   result += "</bus>";
				   result += "<subwayLine>" + tmpSec.getSubwayLine()+"</subwayLine>";
				   result +="<stationName>"+tmpSec.getStartStation()+"</stationName>"; // 처음 역 이름
				   result += "</Data>";
			   }
			   
			   if(info.isWalk()) {
				   result += "<Data>";
				   result += "<check>3</check>";
				   result += "<walk>true</walk>";
				   result += "</Data>";
			   }else {
				   for(int j=0; j<sectionSize; j++) {
					   result += "<Data>";
					   result += "<check>3</check>";
					   result += "<walk>false</walk>";
					   
					   InfoSectionPT sec = info.getSection(j);
					   if(sec.getTrafficType()==1) result += "<trafficType>지하철</trafficType>";
					   else result += "<trafficType>버스</trafficType>";
					   result += "<startStation>"+sec.getStartStation() + "</startStation>";
					   result += "<endStation>" + sec.getEndStation() + "</endStation>";
					   result += "<sectionDistance>" +Integer.toString(sec.getSectionDistance()) + "</sectionDistance>";
					   result += "<sectionTime>" + Integer.toString(sec.getSectionTime()) +"</sectionTime>";
					   if(sec.getTrafficType()==1) {
						   result += "<line>" +sec.getSubwayLine() + "</line>";
					   }else {
						   result += "<line>";
						   int busSize = sec.getBusNoListSize();
						   for(int a=0; a<busSize; a++) {
							   result += sec.getBusNoList(a);
							   if(a!=busSize-1) result += ", ";
						   }
						   result += "</line>";
					   }
					   
					   result += "</Data>";
				   }
			   }
		   }
		   result += "</resultPTList>";
	   }else {
		   result += "<resultCarList>";
		   
		   // -1번 지점 // adSize 보내기 
		   result += "<Data>";
		   result += "<check>-1</check>";
		   result += "<wayCount>"+adSize+"</wayCount>";
		   if(sd.GetStartData()==sd.GetLastData()) result += "<cycle>1</cycle>";
		   else result += "<cycle>0</cycle>";
		   result += "</Data>";
		   
		   // 0번 지점
		   for(int k=0; k<ad.addressData.size(); k++) {
			   result += "<Data>";
			   result += "<check>0</check>";
			   result += "<title>" + ad.addressData.get(dataTotal.carAns[k]).getAddress() + "</title>";
			   result += "</Data>";
		   }
		   
		   if(sd.GetStartData() == sd.GetLastData()) {
			   result += "<Data>";
			   result += "<check>0</check>";
			   result += "<title>" + ad.addressData.get(sd.GetLastData()).getAddress() + "</title>";
			   result += "</Data>";
		   }
		   
		   for(int i=0; i<dataTotal.carList.size(); i++) {
			   result += "<Data>";
			   InfoCar info = dataTotal.carList.get(i);
			   result += "<check>1</check>";
			   result += "<distance>" + Integer.toString(info.getDistance())+"</distance>";
			   result += "<walk>" + info.isWalk() + "</walk>";
			   result += "<time>" + Integer.toString(info.getTime()) + "</time>";
			   if(!info.isWalk()) result += "<fare>" + Integer.toString(info.getFare()) + "</fare>";
			   result += "</Data>";
		   }
		   result += "</resultCarList>";
	   }
	   return result;
   }
   
   
   
}