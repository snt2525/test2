package callApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LocalSearchImg {
	 public StringBuilder sb;
	 public String clientId = "QUyHkL9SA1c0aTQjz197";
	 public String clientSecret = "d4nYetPHwT";
	 
	 public String getImage(String imgTitle,int num) {
	      ////System.out.println("이미지 불러오기");
	      try {
	         String text = URLEncoder.encode(imgTitle, "utf-8");
	         // 여기에 있는 display 값을 조정함에 따라 사진을 긁어오는게 달라진다. 
	         String apiURL = "https://openapi.naver.com/v1/search/image?query=" + text + "&display=" + 100 + "&";
	         URL url = new URL(apiURL);
	         HttpURLConnection con = (HttpURLConnection) url.openConnection();
	         con.setRequestMethod("GET");
	         con.setRequestProperty("X-Naver-Client-Id", clientId);
	         con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
	         ////System.out.println("URL : " + apiURL);
	         int responseCode = con.getResponseCode();
	         BufferedReader br;
	         if (responseCode == 200) {
	            br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
	         } else {
	            br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
	         }
	         sb = new StringBuilder();
	         String line;

	         while ((line = br.readLine()) != null) {
	            sb.append(line + "\n");
	         }

	         br.close();
	         con.disconnect();
	         
	         ////System.out.println(sb);
	         String data = sb.toString();	         
	         String[] array;
	         array = data.split("\"");         
	       //  //System.out.println("이미지 개수 : " + array.length);
	         if(num == 0) {  //데이터 추천의 이미지를 보여주는 경우
		         for (int i = 0; i < array.length; i++) {
		            if (array[i].equals("thumbnail") && array[i+2]!=null) 
		               return array[i+2];
		         }
	         }else if(num == 1) { //추천 데이터에서 하나를 선택해서 데이터를 보내는 경우
	        	 String result = "";
	        	 result += "<UrlData>";
	        	 int cnt = 0;	        
	        	 for (int i = 0; i < array.length; i++) {
			            if (array[i].equals("thumbnail")) { 
			            	if( ++cnt > 10) break; //url은 10개만 받는다.
			            	result += "<Data>";
			            	result += "<imgUrl>" + array[i+2] + "</imgUrl>";
			            	result += "</Data>";
			            }
	         	}	 
	        	 result += "</UrlData>";         	
	         	//System.out.println(result);
	         	return result;
	         }
	      }catch(Exception e) {
	         //System.out.println(e);
	      }
	      return null;
	   }
}
