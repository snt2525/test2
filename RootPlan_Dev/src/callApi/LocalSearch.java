package callApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import dto.Location;

public class LocalSearch {  
	   public int display = 10;
	   public StringBuilder sb;
	   public String clientId = "QUyHkL9SA1c0aTQjz197";
	   public String clientSecret = "d4nYetPHwT";	   
	   
	   public String mapLocalSearch(int num,String findLocation, String address) { 		 
		   String result = "";
		  String[] tmp = address.trim().split(" "); //도로명 주소 분리
		  
		  if(num == 1) {
			  String[] splitRest = findLocation.split(" ");
			  int size = splitRest.length;
			  ////System.out.println(findLocation);
			  findLocation = "";
			  if(size == 1) {   
					result+= "<ResultData>";		
					result+= "<title>l.l</title>";  	
			        result+= "</ResultData>";
				  return result;
			  }
			  for(int i = 1;i < size;i++) {
				  findLocation += splitRest[i];
				  if( i + 1 <size)
					  findLocation += " ";
			  }	    	
		  }
		
		    try {
		       ////System.out.println("검색 호출");
		        String text = URLEncoder.encode(findLocation, "utf-8");
		        String apiURL = "https://openapi.naver.com/v1/search/local?query=" + text + "&display=" + display + "&";
		        URL url = new URL(apiURL);
		        HttpURLConnection con = (HttpURLConnection) url.openConnection();
		        con.setRequestMethod("GET");
		        con.setRequestProperty("X-Naver-Client-Id", clientId);
		        con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
		            int responseCode = con.getResponseCode();
		            BufferedReader br;
		            if (responseCode == 200) {
		                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		            } else {
		                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
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
		        Location location = new Location();
		        for (int i = 0; i < array.length; i++) {
		            if (array[i].equals("title")) {
		               location = new Location();
		               location.setTitle(array[i+2]);
		               //location.setImgUrl(getImage(findLocation)); // title로 이미지 검색해서 넣어주기
		            }
		            else if(array[i].equals("link")) 
		               location.setLink(array[i+2]);
		            else if(array[i].equals("category")) 
		               location.setCategory(array[i+2]);
		            else if(array[i].equals("description")) 
		               location.setDescription(array[i+2]);
		            else if(array[i].equals("telephone")) 
		               location.setTp(array[i+2]);
		            else if(array[i].equals("address")) 
		               location.setAddress(array[i+2]);
		            else if(array[i].equals("roadAddress")) 
		               location.setRoadaddress(array[i+2]);
		            else if (array[i].equals("mapx"))
		               location.setMapx(array[i+2]);
		            else if (array[i].equals("mapy")) {
		                location.setMapy(array[i+2]);
		                if(checkResult(location, tmp, findLocation)) {
		                   result = makeCheckList(location,findLocation);
		                   break;
		                }
		            }
		        }
		        if(result=="") {
		           result+= "<ResultData>";      
		           result+= "<title>"+findLocation+"</title>";     
		           result+= "</ResultData>";
		        }
		        ////System.out.println(result);
		    } catch (Exception e) {
		        //System.out.println(e);
		    } 
		    return result;
	   }
	   
	   protected Boolean checkResult(Location location, String[] tmp, String findLocation) {
		      ////System.out.println("왜니:"+ location.getTitle());
		      String addTmp = location.getRoadaddress(); //주소가 같은지
		      String[] addressTmp = addTmp.trim().split(" ");
		         for(int j=0;j<tmp.length;j++) {
		            ////System.out.println("왜니1:"+ tmp[j]+", "+ addressTmp[j] +",tmp:" +tmp.length+"addressTmp.length: "+addressTmp.length);
		            if(j>=addressTmp.length) break;
		            if(!tmp[j].equals(addressTmp[j])) { 
		               return false;
		            }
		         }
		         String tTmp = location.getTitle(); //타이틀이 같은지
		         String[] titleTmp = tTmp.split("<b>|</b>| ");  //api 찾은  타이틀
		         String[] fLTmp = findLocation.split(" "); //입력받은 타이틀
		         boolean flag = false;
		         for(int i = 0;i<titleTmp.length;i++) {
		            if(fLTmp[0].equals(titleTmp[i])){    //같으면 배열로 들어온다.
		               int start = 0;
		               for(int j = i;j <titleTmp.length; j++) {
		                  if(start<fLTmp.length) {
		                     if(!fLTmp[start++].equals(titleTmp[j]))
		                        return false;                        
		                  }
		               }
		               flag = true;
		               break;
		            }
		         }
		       ////System.out.println("왜니2:"+ location.getTitle());
		         if(!flag) 
		            return false;
		         else
		            return true;
		   }
		   
		   protected String makeCheckList(Location l, String title) {
		      ////System.out.println("@@@ss@@@@"+title);
		      String result = "";
		      result+= "<ResultData>";      
		      result+= "<title>" + title + "</title>";  
		        result+= "<img>" + l.getImgUrl() + "</img>";                     
		        result+= "<link>" + l.getLink() + "</link>";
		        result+= "<category>" + l.getCategory() + "</category>";
		        result+= "<description>" + l.getDescription() + "</description>";
		        result+= "<telephone>" + l.getTp() + "</telephone>";
		        result+= "<address>" + l.getAddress()+ "</address>";    
		        result+= "<roadAddress>"+ l.getRoadaddress() + "</roadAddress>";   
		        result+= "</ResultData>";
		      return result;
		   } 
}
