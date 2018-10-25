// 모든 페이지에서 처음에 접근하는 페이지
$.ajaxSetup({
   contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
   type:"post"
});
var checkflag = sessionStorage.getItem("checkflag");
var customerID = sessionStorage.getItem("customerID");

function print(){
   console.log("email : " + sessionStorage.getItem("email"));
   //console.log("customerName : " + sessionStorage.getItem("name"));
   console.log("id : " + sessionStorage.getItem("id"));
   console.log("gender : " + sessionStorage.getItem("gender"));
   console.log("age : " + sessionStorage.getItem("age"));
}

var naviHtml = "<a href='Second.html'><img class='navImg' src='img/logo_color.png'/></a>"
	+sessionStorage.getItem("email")+" 님";
$("#bar").html(naviHtml);
function sessionCheck(i){
   // 만약 로그인 안되어있으면 로그인 페이지로 무조건 가기
   if(sessionStorage.getItem('id')==null){
      alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
      location.href="index.html";
      return 0;
   }else{
      print();     
      if(i == 0){ // index 페이지에서 호출
         sendCustomerInfo();
      }
      // customerID 계속 들고다니면서 모든 폼에 추가로 전송하기
      customerID = sessionStorage.getItem("customerID"); 
      return 1;
   }   
}

var email = sessionStorage.getItem("email");
var customerName = sessionStorage.getItem("name");
var id = sessionStorage.getItem("id");
var gender = sessionStorage.getItem("gender");
var age = sessionStorage.getItem("age");

function sendCustomerInfo(){
	sessionStorage.setItem("checkflag", 0);
	email = sessionStorage.getItem("email");
	customerName = sessionStorage.getItem("name");
	id = sessionStorage.getItem("id");
	gender = sessionStorage.getItem("gender");
	age = sessionStorage.getItem("age");
	
	$.ajax({
		url:"/LoginServlet",
		dataType: "text",
		async: false,
		data:"menuIndex=0&email="+email+"&cID="+id+"&gender="+gender+"&age="+age,
		success: function(data){
			console.log("customerID 번호 : " + data);
			sessionStorage.setItem("customerID", data); // customerID 입력
			customerID = data;
			ListInit();
		},error:function(data){
			console.log("customerID 값 받아오기 실패"); 
		}
	});
}

function ListInit(){ 
	$.ajax({
		url:"/AddressDataServlet",
		dataType: "text",
		async: false,
		data:"menuIndex=24&customerID="+customerID
	});
}

function killSession(){ 
   sessionStorage.clear();
   console.log("killSession");
   $.ajax({
      url:"/LoginServlet",
      dataType: "text",
      data: "menuIndex=1&cID="+id
   });
   $.ajax({
	      url:"/AddressDataServlet",
	      dataType: "text",
	      data: "menuIndex=23&cID="+id+"&customerID="+customerID
	   });
}