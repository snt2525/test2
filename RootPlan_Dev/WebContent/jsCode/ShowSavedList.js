$.ajaxSetup({
	contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
	type:"post"
});


var prev;
// 즐겨찾기 버튼 누르면 리스트 보여주는 함수로 가기
$(function(){
	prev = "#btnMainList";
   $('#btnSaveList').click(function(){
	   // 즐겨찾기 버튼 감추고 리스트 버튼 보이게 하기 
	   $(prev).css("background-color","rgb(255, 153, 000)");
	   prev = "#btnSaveList";
	   $(prev).css("background-color","#FC7A00");
	   showList();
   });
   $('#btnMainList').click(function(){
	   // 리스트 버튼 감추고 즐겨찾기 버튼 보여주기
	   $(prev).css("background-color","rgb(255, 153, 000)");
	   prev = "#btnMainList";
	   $(prev).css("background-color","#FC7A00");
	   getData(); // main으로 돌아가면 모든 리스트 다시 보여주기
   });
   $('#btnSearch').click(function(){
	   $(prev).css("background-color","rgb(255, 153, 000)");
	   prev = "#btnSearch";
	   $(prev).css("background-color","#FC7A00");
   });
 }); 

function toggleLayer(layer)
{
    var l = document.getElementById(layer);
    if(l.style.display == "")
        l.style.display = "none";
    else if(l.style.display == "none")
        l.style.display = "";
}

var rID = "";
function showList(){ // 저장된 데이터 리스트 보여주는 함수
	$("#resetBtn").attr("type","hidden");
	//alert("showList");
	// addressServlet 6번으로 가기
	$.ajax({
		url:"/AddressDataServlet",
		dataType: "xml", 
		data: "menuIndex=6&customerID="+customerID+"&cID="+sessionStorage.getItem("id"),
		success: function(data){
			var htmlStr ="<h3 class='headline'>저장된 경로</h3>";
			var size=0, tmpId=0, check=0;
			document.getElementById("resetBtn").style.display="block";
			document.getElementById("resetBtn").style.display="none";
			
			$(data).find("Data").each(function(){
				check=1;
				htmlStr += "<div><hr class='two'></div>"
				htmlStr += "<div>";
				size = $(this).find('size').text();
				htmlStr += "<div class='save_subtitle'><a class='no-uline noul' href=\"javascript:toggleLayer('"+tmpId+"');\">" +$(this).find('name').text()+"</a></div>";
				htmlStr += "<div id='"+tmpId+"' style='display:none'><hr class='three'><div class='detail-save'>";  
				for(var idx=0; idx<size; idx++){
					htmlStr += (idx+1) +". " + $(this).find('address'+idx).text();
					if(idx==size-1) htmlStr += "</div>";
					else htmlStr += " <img src=img/arrow_right.png /> "; 
				}
				htmlStr += "<div id='containerBox'>"; 
				htmlStr += "<a id='box-left' class='btn btn-skin' onclick=\"goLoading('"+$(this).find('rID').text()+"')\">바로경로보기</a>"; // css 적용하기 
				htmlStr += "<a id='box-center' class='btn btn-skin' onclick=\"goMain('"+$(this).find('rID').text()+"')\">선택</a>";
				htmlStr += "<a id='box-right' class='btn btn-skin' onclick=\"goDelete('"+$(this).find('name').text() +"','"+$(this).find('rID').text()+"')\">삭제</a>";
				htmlStr += "</div>";	
				htmlStr += "</div></div>";
				tmpId++;
			}) 
			if(check==0) htmlStr += "<div><h5>저장된 경로가 없습니다.</h5></div>";
			$("#list").html(htmlStr);
		}, 		
	    error: function (data) {
	    	console.log("저장한 리스트 가져오기 실패");
    	}				
	});
}

function goLoading(rID){
	alert("경로 보는 페이지로 넘어갑니다.");
	$.ajax({
		url:"/AddressDataServlet",
		dataType: "text", 
		data: "menuIndex=19&customerID="+customerID+"&cID="+id+"&rID="+rID,
		success: function(data){
			sessionStorage.setItem("rID", rID);
			location.href = "LoadingSaved.html";
		}, 		
	    error: function (data) {
	    	console.log("경로보기 넘어가기 실패");
    	}				
	});
}

function goMain(rID){
	$.ajax({
		url:"/AddressDataServlet",
		dataType: "text", 
		data: "menuIndex=19&customerID="+customerID+"&cID="+id+"&rID="+rID,
		success: function(data){
			//document.getElementById("btnMainList").style.display="none";
		   //document.getElementById("btnSaveList").style.display="block";
			getData();
		}, 		
	    error: function (data) {
	    	console.log("선택하기로 가는거 넘어가기 실패");
    	}				
	});
}

function goDelete(name, rID){
	$.ajax({
		url:"/AddressDataServlet",
		dataType: "text", 
		data: "menuIndex=22&customerID="+customerID+"&cID="+id+"&rID="+rID,
		success: function(data){
			console.log(data);
			if(data==1){
				alert(name+"을 삭제하셨습니다.");
				showList();
			}
		}, 		
	    error: function (data) {
	    	console.log("삭제 실패");
    	}				
	});
}