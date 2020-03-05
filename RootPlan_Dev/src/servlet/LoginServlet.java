package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ConnectDB;
import dto.CustomerInfo;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   static ConnectDB db = new ConnectDB();
   static int customerCnt = 0;
   static int customerSize = 0;
   static int[] log = new int[20]; //우선 20명만 수용
   static Map<String,Integer> logCheck = new HashMap<String,Integer>();   
    public LoginServlet() {    
       super();
   }
       
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       response.getWriter().append("Served at: ").append(request.getContextPath());
       this.doPost(request, response);
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html;charset=UTF-8");         
      PrintWriter out = response.getWriter();
      //System.out.println("사용자주소할당");      
      String ID=request.getParameter("customerID");   
      int menuIndex = Integer.parseInt(request.getParameter("menuIndex"));
      
      switch(menuIndex){
      case 0: //주소할당 받기
         String email = request.getParameter("email");
         String cid = request.getParameter("cID");
         String gender = request.getParameter("gender");
         String age = request.getParameter("age");
         //System.out.println("이메일= "+email+", cid= "+cid+" , gender= "+gender+" , age=" + age);
         CustomerInfo tmp = new CustomerInfo(cid, email, gender, age);
         
         if(logCheck.containsKey(cid)) {
        	 //System.out.println("이미 해시에 아이디 있음 ");
        	 out.print(logCheck.get(cid));
         }else{
        	 //System.out.println("해시에 아이디 없음 ");
            for(int i =0;i<20;i++) { //빈곳을 찾아 할당 해준다.
               if(log[i] == 0) {
                  log[i] = 1;
                  customerSize++;
                  logCheck.put(cid, i);
                  out.print(i);
                  break;
               }
            }
         }
         db.CheckID(tmp);   //아이디 있으면 pass; 있으면 생성   
         break;
         
      case 1: //주소 
         String cID = request.getParameter("cID");
         //System.out.println("cID : " + cID);
         int customerCnt =  logCheck.get(cID);
         log[customerCnt] = 0;
         if(customerSize == customerCnt)
            customerCnt--;
         customerSize--;
         //해쉬 해제도 해줘야함
         logCheck.remove(cID);
         break;      
      }
   }
}