package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import callApi.LocalSearch;
import callApi.LocalSearchImg;
import callApi.ShowLocalSearch;
import dao.LocationDataManager;


@WebServlet("/CallSearchLocalApi")
public class SearchAPIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public SearchAPIServlet() {     
        super();
    }
    
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.getWriter().append("Served at: ").append(request.getContextPath());
      this.doPost(request, response);
   }
   
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	      response.setContentType("text/html;charset=UTF-8");	      
	      PrintWriter out = response.getWriter();
	      //System.out.println("검색api호출");
	      int menuIndex = Integer.parseInt(request.getParameter("menuIndex"));
	      
		  switch(menuIndex) {
			  case 1:  //지도 클릭, 검색
				  String result = "";
				  LocalSearch ls = new LocalSearch();
				  int num = Integer.parseInt(request.getParameter("num")); //0: 검색, 1: 클릭
				  String findLocation = request.getParameter("findLocation"); //타이틀
				  String address = request.getParameter("address"); // 도로명 주소
				  result = ls.mapLocalSearch(num, findLocation, address);
				  out.print(result);
				  break;
			  case 2:
				  String Si = request.getParameter("Si");
		          String clickSi = request.getParameter("clickSi");
		          String keywordVal = request.getParameter("keywordVal");
	               if(!Si.equals(clickSi)) {
	                  LocationDataManager l = new LocationDataManager();	                  
	                  String  result1 = l.getLocation(clickSi, keywordVal);
	                  out.print(result1);
	               }else {
	                  String  result1 = "0";
	                  out.print(result1);
	               }
				  break;	
			  case 3:
				  String result2 = "";
				  LocalSearchImg img = new LocalSearchImg();
				  String localName = request.getParameter("localName");
				  result2 = img.getImage(localName, 1);
				  ////System.out.println(result2);
				  out.print(result2);
				  break;
				  
			  case 4: 
				  String name = request.getParameter("name");
				  ShowLocalSearch ls2 = new ShowLocalSearch("서울시","테마파크");
				  out.print(ls2.getRecommendData2());
				  break;
		  }
   }
   
   
 
}