package callApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;

import dao.CalculateDist;
import dto.DataPair;
import dto.DataTotal;
import dto.InfoPT;
import dto.InfoSectionPT;
import dto.TimeMethod;

public class ApiPTSearch {
   CalculateDist cd = new CalculateDist();
   StringBuilder sb;
   String key = "	0bI9mEI/hPhH2I+rU7TjMdZOXex005hL4poBNf0DHWA";
   LinkedList<dto.Address> ad;
   DataTotal dataTotal;
   // 이차원 배열을 Route.java에다가 넣어주기
   ApiWalkSearch ws;
   int listSize;
   boolean flag = false; // 대중교통에서 걷기 api호출에 쿨타임을 주기 위해서 만들었다.
   boolean isSame=false;
   int adSize;
   //boolean errorExist = false;

   // 생성자, 이차원 배열 초기화
   public ApiPTSearch(LinkedList<dto.Address> ad, DataTotal dataTotal, int listSize) {
	  this.listSize = listSize;
      adSize = ad.size();
      this.ad = ad;
      this.ws = new ApiWalkSearch();
      this.dataTotal = dataTotal;
      // 배열 초기화
      dataTotal.initPtDist();
   }   

   // 출력 함수 : 대중교통 거리 출력
   void ptPrint(int size) {
      System.out.println("pt dist print");
      for(int i=0; i<listSize; i++) {
         for(int j=0; j<listSize; j++) {
            System.out.print(dataTotal.ptDist[i][j].getTime() + " "); 
         }
         System.out.println();
      }
   }
   
   // 이차원 배열 돌면서 callPTApi 호출
   public void callTransportApi(int a, int b) {
      for (int i = a; i < b; i++) {
         for (int j = 0; j < listSize; j++) {
            if (dataTotal.ptDist[i][j].getMethod()) {
            	continue; // 걷기데이터가 호출되었었기 때문에
            }else if (i == j)
            	dataTotal.ptDist[i][j] = new TimeMethod(Integer.MAX_VALUE, false);
            else {
               callPTApi( ad.get(i).getLat(),  ad.get(i).getLng(), ad.get(j).getLat(), ad.get(j).getLng(),i ,j );
            }
         }
      }
      ptPrint(listSize);
      System.out.println("pt end"+", list size: "+ listSize);
   }
 
   // callTransportApi 호출당해, 대중교통 호출
  public void callPTApi(double sx, double sy, double ex , double ey, int i, int j) {
      CalculateDist calDist = new CalculateDist();
	  double distanceMeter = calDist.distance(sx, sy, ex, ey, "meter");
      if (distanceMeter <= 800) {
         // 이전에 있던 애가 걷기 호출을 했었는지
         int tmpTime = 0;
         if (flag == true) {
            try {
               Thread.sleep(550);
               tmpTime = ws.walkApi(i, j, sx, sy, ex, ey)/60;  // 60 없애고 분으로 해야할듯
            } catch (Exception e) {}
         } else {
            tmpTime = ws.walkApi(i, j, sx, sy, ex, ey) / 60;
         }
         
         // 걷기일 경우 양방향 같으니 같은 데이터 넣어주기
         dataTotal.ptDist[i][j] = new TimeMethod(tmpTime, true);
         dataTotal.ptDist[j][i] = new TimeMethod(tmpTime, true);
         flag = true;
      } else {
         flag = false;
         try {
            String apiURL = "https://api.odsay.com/v1/api/searchPubTransPath?SX=" + Double.toString(sx) + "&SY="
                  + Double.toString(sy) + "&EX=" + Double.toString(ex) + "&EY=" + Double.toString(ey) + "&apiKey="
                  + key + "";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {         
               br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
               //에러가 발생하면 걷기로 대체
               int tmpTime = 0;
               if (flag == true) {
                  try {
                     Thread.sleep(550);
                     tmpTime = ws.walkApi(i, j, sx, sy, ex, ey)/60;  // 60 없애고 분으로 해야할듯
                  } catch (Exception e) {}
               } else {
                  tmpTime = ws.walkApi(i, j, sx, sy, ex, ey) / 60;
               }
               
               // 걷기일 경우 양방향 같으니 같은 데이터 넣어주기
               dataTotal.ptDist[i][j] = new TimeMethod(tmpTime, true);
               dataTotal.ptDist[j][i] = new TimeMethod(tmpTime, true);
               flag = true;

               return ;
            }
            sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
               sb.append(line + "\n");
            }         

            String data = sb.toString();
            String[] array;
            array = data.split("\"");
            for (int k = 0; k < array.length; k++) {
               if(array[k].equals("code")) {     //700m 이하로 문제 발생
            	   System.out.println("문제있음=============="+ sb); 
                   //에러가 발생하면 걷기로 대체 
                   int tmpTime = 0;
                   if (flag == true) {
                      try {
                         Thread.sleep(550);
                         tmpTime = ws.walkApi(i, j, sx, sy, ex, ey)/60;  // 60 없애고 분으로 해야할듯
                      } catch (Exception e) {}
                   } else {
                      tmpTime = ws.walkApi(i, j, sx, sy, ex, ey) / 60;
                   }
                   
                   // 걷기일 경우 양방향 같으니 같은 데이터 넣어주기
                   dataTotal.ptDist[i][j] = new TimeMethod(tmpTime, true);
                   dataTotal.ptDist[j][i] = new TimeMethod(tmpTime, true);
                   flag = true;

                   return ;
               }
               if (array[k].equals("totalTime")) {
            	   dataTotal.ptDist[i][j] = new TimeMethod(Integer.parseInt(array[k + 1].substring(1, array[k + 1].length() - 1)), false);
                  break;
               }              
            }
            br.close();
            con.disconnect();
         } catch (Exception e) { }
      }
   }

  public void resultOrderCall(int[] result, int start, int end) {  //결과대로 호출
	  dataTotal.setError(false);
	  if(start==end) {
		  for(int i =0; i < listSize ; i++) {
			  if(dataTotal.isError()) return;
	    	  if(i == listSize - 1) {
	    		  dataTotal.ptList.add(callResultPT( ad.get(result[i]).getLat(), ad.get(result[i]).getLng(),
			               ad.get(result[0]).getLat(), ad.get(result[0]).getLng(), result[i], result[0]));
	    	  }else {
		    	  dataTotal.ptList.add(callResultPT( ad.get(result[i]).getLat(), ad.get(result[i]).getLng(),
		               ad.get(result[i+1]).getLat(), ad.get(result[i+1]).getLng(), result[i],result[i+1]));
	    	  }
	      }
	  }else {
		  if(dataTotal.isError()) return;
		  //System.out.println("문제!!!!!!!!!!!!!!!!!!!!!!"+ listSize);
	      for(int i =0; i < listSize -1; i++) {
	    	  //System.out.println("i:"+i);
	    	  dataTotal.ptList.add(callResultPT( ad.get(result[i]).getLat(), ad.get(result[i]).getLng(),
	               ad.get(result[i+1]).getLat(), ad.get(result[i+1]).getLng(), result[i],result[i+1]));
	      }
	  }
  }
  
  // 대중교통 재호출할 때, 마지막에 결과 한노드에서 한 노드로 총 정보 가져오기 
  public InfoPT callResultPT(double sx, double sy, double ex, double ey,int a, int b) {
      InfoPT infopt = new InfoPT(); // 1-2 지점 이동시
      InfoSectionPT infoSec = new InfoSectionPT();
      CalculateDist calDist = new CalculateDist();
      double distanceMeter = calDist.distance(sx, sy, ex, ey, "meter");
      if (dataTotal.ptDist[a][b].getMethod() || distanceMeter <= 800) {
    	  //System.out.println("걷기 호출" + dataTotal.ptDist[a][b].getMethod());
         // 이전에 있던 애가 걷기 호출을 했었는지
         if (flag == true) {
            try {
               Thread.sleep(1500);
               infopt = ws.resultWalkPTApi(sx, sy, ex, ey); 
               if(infopt.isError()) {
            	   dataTotal.setError(true);
            	   return infopt;
               }
            } catch (Exception e) {}
         } else {
            infopt = ws.resultWalkPTApi(sx, sy, ex, ey);
         }        
         // 걷기일 경우 양방향 같으니 같은 데이터 넣어주기
         flag = true;
      } else {
         flag=false;
         try {
            String apiURL = "https://api.odsay.com/v1/api/searchPubTransPath?SX=" + Double.toString(sx) + "&SY="
                  + Double.toString(sy) + "&EX=" + Double.toString(ex) + "&EY=" + Double.toString(ey) + "&apiKey="
                  + key + "";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
               br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
               br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
               
               //System.out.println("대중교통 실패");
            }
            sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
               sb.append(line + "\n");
            }

            br.close();
            con.disconnect();

            String data = sb.toString();
            String[] array;
            array = data.split("\\{|\\}|\"|\\,|\\:");

            int trafficType = 0;
            double x = 0, y = 0;
            int cnt=0;
            for (int i = 0; i < array.length; i++) {
               if (array[i].equals("result")) {
                  infopt.setSx(sx);
                  infopt.setSy(sy);
                  infopt.setEx(ex);
                  infopt.setEy(ey);
                  infopt.setWalk(false); // 여기까지 온건 walk 아니라 pt
               } else if (array[i].equals("trafficType")) {
                  trafficType = Integer.parseInt(array[i + 2]);
               } else if (array[i].equals("lane")) {
                  if(cnt!=0) infopt.addSection(infoSec); // 처음이 아니면 넣어주기
                  cnt++;
                  infoSec = new InfoSectionPT();
                  infoSec.setTrafficType(trafficType); // lane이 trafficType보다 나중에 나오니까
               } else if (array[i].equals("busNo")) { // 버스일 경우
                  if (trafficType != 2)   continue;
                  infoSec.addBusNoList(array[i + 3]);
               } else if (array[i].equals("name")) { // 지하철일 경우
                  if (trafficType != 1)   continue;
                  infoSec.setSubwayLine(array[i + 3]);
               } else if(array[i].equals("stationCount")){ // section별 정류장 개수
            	   infoSec.setSectionStationCount(Integer.parseInt(array[i + 2]));
               }else if (array[i].equals("x")) {
                  x = Double.parseDouble(array[i + 3]);
               } else if (array[i].equals("y")) {
                  y = Double.parseDouble(array[i + 3]);
                  infopt.addLineList(new DataPair(x, y));
               } else if (array[i].equals("distance")) {
            	   if(trafficType==3) continue;
                  infoSec.setSectionDistance(Integer.parseInt(array[i + 2]));
               } else if (array[i].equals("sectionTime")) {
            	   if(trafficType==3) continue;
                  infoSec.setSectionTime(Integer.parseInt(array[i + 2]));
               } else if (array[i].equals("startName")) {
                  infoSec.setStartStation(array[i + 3]);
               } else if (array[i].equals("endName")) {
                  infoSec.setEndStation(array[i + 3]);
               } else if (array[i].equals("payment")) {
                  infopt.setFare(Integer.parseInt(array[i + 2]));
               } else if (array[i].equals("totalTime")) {
                  infopt.setTotalTime(Integer.parseInt(array[i + 2]));
               }else if(array[i].equals("totalStationCount")) { // 총 정류장 개수
            	   infopt.setStationCount(Integer.parseInt(array[i + 2]));
               } else if (array[i].equals("totalDistance")) {
                  infopt.setTotalDistance(Integer.parseInt(array[i + 2]));
               } else if (array[i].equals("firstStartStation")) {
                  infopt.setFirstStartStation(array[i + 3]);
               } else if (array[i].equals("lastEndStation")) {
                  infopt.setLastEndStation(array[i + 3]);
                  break; // 하나 다 불러온 거니 for문 나감
               }
            }
            infopt.addSection(infoSec); 
         } catch (Exception e) {}
      }
      return infopt;
  }
  
}