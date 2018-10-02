package callApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import dto.DataPair;
import dto.InfoCar;
import dto.InfoPT;

public class ApiWalkSearch{
   public StringBuilder sb;
   String key = "9974a775-4c3d-48f1-8df7-650b3f2debfc";
   String startName = "start";
   String endName = "end";

   // dfs를 위한 거리 값 가져올떄
   public int walkApi(int sno, int eno, double sx, double sy, double ex, double ey) {
	   //System.out.println("ddddddddddddd걷기");
      int findTime = 0;
      try {
          String apiURL = "https://api2.sktelecom.com/tmap/routes/pedestrian?version=1&format=xml&startX="
        		  	+Double.toString(sx)+"&startY="+Double.toString(sy)+"&endX="+Double.toString(ex)+"&endY="+Double.toString(ey) 
        		  	+ "&startName="+startName+"&endName="+endName;
          URL url = new URL(apiURL);
          HttpURLConnection con = (HttpURLConnection) url.openConnection();
          
          con.setRequestMethod("POST");
          con.setRequestProperty("appKey", key);
          con.setDoOutput(true);
          
          int responseCode = con.getResponseCode();
          BufferedReader br;
          if (responseCode == 200) {
              br = new BufferedReader(new InputStreamReader(con.getInputStream()));
          } else {
              br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
              //System.out.print("실패");
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
          array = data.split("<|>");
          
          for(int i=0; i<array.length; i++) {
        	  if(array[i].equals("tmap:totalTime")) {
        		  // 여기서 다른 클래스 이차원 배열에 넣어주기
        		  findTime =  Integer.parseInt(array[i+1]);
        		  break;
        	  }
          }
      }catch(Exception e) {}
	return findTime;
   }
   
   // 대중교통 걷기 재호출시
   public InfoPT resultWalkPTApi(double sx, double sy, double ex, double ey) { // 대중교통 걷기 전용
	   InfoPT infopt = new InfoPT();
	   
	   try {
			String apiURL = "https://api2.sktelecom.com/tmap/routes/pedestrian?version=1&format=xml&startX="
					+ Double.toString(sx) + "&startY=" + Double.toString(sy) + "&endX=" + Double.toString(ex) + "&endY="
					+ Double.toString(ey) + "&startName=start&endName=end";
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("appKey", key);
			con.setDoOutput(true);

			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				sb = new StringBuilder();
				String line;

				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}

				br.close();
				con.disconnect();
				String data = sb.toString();

				//System.out.println("d실패" + data);	
				infopt.setError(true);
				return infopt;
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
			array = data.split("<|>");

			
			//System.out.println("성공 : " + data);
			
			for (int i = 0; i < array.length; i++) {
				if (array[i].equals("id")){
					Thread.sleep(550);
					return resultWalkPTApi(sx, sy, ex, ey);
				}
				else if (array[i].equals("tmap:totalDistance")) {
					infopt.setSx(sx);
					infopt.setSy(sy);
					infopt.setEx(ex);
					infopt.setEy(ey);
					infopt.setTotalDistance(Integer.parseInt(array[i + 1]));
				} else if (array[i].equals("tmap:totalTime")) {
					infopt.setTotalTime(Integer.parseInt(array[i + 1]));
				} else if (array[i].equals("coordinates")) {
					if (array[i - 2].equals("Point")) continue;
					String[] temp = array[i + 1].split("\\s+|,");
					for (int j = 0; j < temp.length; j += 2) {
						infopt.addLineList(
								new DataPair(Double.parseDouble(temp[j]), Double.parseDouble(temp[j + 1])));
					}
				}
			}

			// 걷기일 경우 나머지 firstStation, endStation은 연결된 대중교통 값에서 가져오기
			infopt.setWalk(true);
			//infocar.print();
		} catch (Exception e) {}
		return infopt;
   }
   
   // 자동차 걷기 재호출시
   public InfoCar resultWalkCarApi(double sx, double sy, double ex, double ey) { // 자동차 걷기 전용
		InfoCar infocar = new InfoCar(); // car class 재활용
		try {
			String apiURL = "https://api2.sktelecom.com/tmap/routes/pedestrian?version=1&format=xml&startX="
					+ Double.toString(sx) + "&startY=" + Double.toString(sy) + "&endX=" + Double.toString(ex) + "&endY="
					+ Double.toString(ey) + "&startName=start&endName=end";
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("appKey", key);
			con.setDoOutput(true);

			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				//System.out.println("d실패");
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
			array = data.split("<|>");

			for (int i = 0; i < array.length; i++) {
				if (array[i].equals("tmap:totalDistance")) {
					infocar.setSx(sx);
					infocar.setSy(sy);
					infocar.setEx(ex);
					infocar.setEy(ey);
					infocar.setDistance(Integer.parseInt(array[i + 1]));
				} else if (array[i].equals("tmap:totalTime")) {
					infocar.setTime(Integer.parseInt(array[i + 1]));
				} else if (array[i].equals("coordinates")) {
					if (array[i - 2].equals("Point"))
						continue;
					String[] temp = array[i + 1].split("\\s+|,");
					for (int j = 0; j < temp.length; j += 2) {
						infocar.addLineList(
								new DataPair(Double.parseDouble(temp[j]), Double.parseDouble(temp[j + 1])));
					}
				}
			}

			infocar.setWalk(true);
		} catch (Exception e) {}
		return infocar;
	}
}
