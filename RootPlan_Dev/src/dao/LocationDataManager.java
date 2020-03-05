package dao;

import java.util.LinkedList;

import callApi.ShowLocalSearch;
import dto.Location;


public class LocationDataManager {
   LinkedList<Location> locationData;
   int size=0;
   
   public LocationDataManager() {
      locationData = new LinkedList<Location>();
   }
   
   public String getLocation(String si, String keywordVal) {
      ShowLocalSearch s = new ShowLocalSearch(si, keywordVal);      
      locationData.clear();
      locationData = s.getRecommendData(); //크롤링하기
      
      String result = "";      //크롤링한 데이터를 xml형태로 만들어준다.
      result += "<LocationData>";
      result += "<CityData><LocationCity>" + si + "</LocationCity></CityData>";
      
      for(int i =0;i<locationData.size();i++) {
         Location lo = locationData.get(i);
         result += "<Data>";
         result += "<no>" + Integer.toString(i) + " </no>";
         result += "<LocationTitle>" + lo.getTitle() + " </LocationTitle>";
         result += "<LocationLink>" + lo.getLink() + "</LocationLink>";
         result += "<LocationDescription>" + lo.getDescription() + "</LocationDescription>";
         result += "<LocationTP>" + lo.getTp() + "</LocationTP>";
         result += "<LocationCategory >" + lo.getCategory() + "</LocationCategory>";
         result += "<LocationRoadaddress>" + lo.getRoadaddress () + "</LocationRoadaddress>";        
         result += "<LocationAddress>" + lo.getAddress() + "</LocationAddress>";
         result += "<LocationMapx>" + lo.getMapx() + "</LocationMapx>";
         result += "<LocationMapy>" + lo.getMapy() + "</LocationMapy>";
         if(lo.getImgUrl() == null) {
            result += "<LocationImage>" + "img/logo_gray.png" + "</LocationImage>";
         }else {
            result += "<LocationImage>" + lo.getImgUrl() + "</LocationImage>";
         }
         result += "</Data>";
      }
      result += "</LocationData>";
      return result;  //result를 내보낸다.
   }
}
