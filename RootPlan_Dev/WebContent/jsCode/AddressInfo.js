var marker = new Array(10);
var cnt = 0;
var flag = 0;
var imgUrl = [
	'img/mark1.png',
	'img/mark2.png',
	'img/mark3.png',
	'img/mark4.png',
	'img/mark5.png',
	'img/mark6.png',
	'img/mark7.png',
	'img/mark8.png',
	'img/mark9.png',
	'img/mark10.png'
]

$.ajaxSetup({
	contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
	type:"post"
});

function test(){
	$.ajax({
	      url : "/CallSearchLocalApi", //인터넷망
	      type : "post",
	      data :"name="+"짜증난다 그만좀하자"+"&menuIndex="+4,
	      dataType : "text",
	      success : function(data) {
	    	  alert(data);
	      },
	      error : function(xhr, status, error) {
	         alert("에러발생");
	      }
	   });
}


//$("infoBtn").on("click", '#btn' , function(){
function clickADDBtn(){
	if(flag == 1){
		$.ajax({
			url:"/AddressDataServlet",
			dataType: "html",
			data: $("#saveAddress").serialize()+"&customerID="+customerID,
			success: function(data){
				infoWindow.close();
				document.saveAddress.lat.value = "";
				document.saveAddress.lng.value = "";
				document.saveAddress.address.value = "";	
				if(data == 7){
					alert("목적지를 7개 이상 선택하실 수 없습니다.")
				}else{										
					getData(); 	
					flag = 0;
				}
			}, 		
		    error: function (data) {alert("목적지를 지정해 주세요")}				
		});
	}else
		alert("목적지를 지정해 주세요");
}


//모든 데이터 호출 
function getData(){
	clean();
	getDataSize(1);
	$.ajax({
		url:"/AddressDataServlet",
		dataType: "xml",
		data: $("#getAddressData").serialize()+"&customerID="+customerID,
		success: function(data){
			var htmlStr = "";
			if(data!= null){
				$(prev).css("background-color","rgb(255, 153, 000)");
			    prev = "#btnMainList";
				$(prev).css("background-color","#FC7A00");
				jQuery('#resetBtn').show();   //초기화 버튼 활성화
				htmlStr = "<h5 class='headline'>여행지 선택</h5>";
				$(data).find("Address").each(function(){
					htmlStr += "<div style='display:flex;'>";
					htmlStr += "<div class='left-content'><img src='img/mark" + $(this).find('no').text()+".png'/></div>";
					htmlStr += "<div class='middle-content'>" + $(this).find('data').text() + "</div>";
					htmlStr += "<div class='right-content'><a href='#' class='delete-factor no-uline noul' id='del', name='"+ $(this).find("no").text() +"'>X</a></div>";
					htmlStr += "</div>";
					
					//마크 표시
					var latlngPoint = new naver.maps.Point($(this).find('lat').text(),$(this).find('lng').text());
					
					var latlng = new naver.maps.LatLng(latlngPoint.y,latlngPoint.x);
					var greenMarker = {
					    position: latlng,
					    map: map,
					    icon: {
					    	 url: imgUrl[cnt],
				             size: new naver.maps.Size(38, 58),
				             anchor: new naver.maps.Point(19, 58),
					    }
					};
					marker[cnt] = new naver.maps.Marker(greenMarker);
					marker[cnt++].setMap(map); // 추가	
				})
				$("#list").html(htmlStr);
			}
			else{
				jQuery('#resetBtn').hide();  // 값이 없으면 버튼 감추기
				showMessage();
			}
		}, error: function(data){
				console.log("실패");
		}
		
	});
}

$("#addressBasket").on("click", '#resetBtn' , function(){
	if(confirm("데이터를 초기화 하시겠습니까?") == true){
		$.ajax({
			url:"/AddressDataServlet",
			dataType: "html",
			data: $("#initData").serialize()+"&customerID="+customerID,
			success: function(data){
				if(data>0){
					clean();
					//$("#resetBtn").attr("type","hidden");  //초기화 버튼 hidden
					jQuery('#resetBtn').hide();  
					showMessage();
					getDataSize(1);
				}
			}
		});	
	}
})

function clean(){
	for(var i = 0;i< cnt;i++){
		marker[i].setMap(null); 
	}	
	marker = new Array(10);
	cnt = 0;		
}


//삭제 모듈
$("#list").on("click", '#del' , function(){
	if(confirm("삭제하시겠습니까?") == true){
		document.deleteAddress.deleteIndex.value = $(this).attr("name");
		$.ajax({
			url:"/AddressDataServlet",
			dataType: "text",
			data: $("#deleteAddress").serialize()+"&customerID="+customerID,
			success:function(data){				
				if(data>0){
					//clean(); 
					getData();
				}else{
					alert("삭제를 실패하였습니다.");
				}			
			},
			error: function(data){
				console.log(data);
			}
		});
	}
})
	

function getDataSize(num){	
	$.ajax({
		url:"/AddressDataServlet",
		dataType: "text",
		data: $("#getSize").serialize()+"&customerID="+customerID,
		success:function(data){				
			if(data<3){
				if(num==0) 
					alert("목적지를 3개 이상 선택해 주세요.");
				$('#nextBtn1').attr({'href':'#'});
			}else{
				$('#nextBtn1').attr({'href':'Third.html'});
			}
		},
		error: function(data){
			console.log(data);
		}			
	});	
}


function showMessage(){
	// 튜토리얼이랑 메세지 보여주기
	var htmlStr =""; 
	htmlStr ="<div><img style='text-align:center; margin-left:30px;width:80%;' src='img/messageImg2.png'/></div>";
	//htmlStr += "<div><br><h5><b>루트플랜이란?</b></h5></div>";
	/*htmlStr += "<div><p>여행지의 핵심 방문 장소와 사용자가 선택한 방문지를 기준으로<br> 효율적인 여행 동선을 제공해드립니다. </p>" +
			"<p>1. 최단 시간 이동 경로 및 교통 정보 제공</p>" +
			"<p>2. 사이클이 생성되는 경로 정보 제공</p>" +
			"<p>3. 여행지의 정보 제공</p>" +
			"<br></div>";*/
	
	//htmlStr += "<div><p>★경유지 성택하기</p>" +
		//	"<p>여행지는 최소 3개에서 최대 7개까지 선택할 수 있습니다. 시작지점과 마지막 지점을 선택하셔야 결과를 보실 수 있습니다.</p></div>";
	
	htmlStr += "<div><h5><b>[경로 선택방법 3가지]</b></h5>" +
			"<p>1. 지도를 직접 클릭합니다.</p>" +
			"<p>2. 검색 버튼을 눌러 목적지를 검색합니다.</p>" +
			"<p>3. 지도 아래에 있는 해당 지역 대표 여행지의 위치보기 버튼을 클릭합니다. (가이드 키워드를 사용해 관련 지역의 다양한 여행지 정보를 추가로 얻을 수 있습니다.)</p>" +
			"<p><br>위와 같은 방법을 통하면 지도위에 정보창이 뜨게 되고, +버튼을 클릭하여 여행지를 선택합니다. </p></div>";
	
	$("#list").html(htmlStr); 
}


















