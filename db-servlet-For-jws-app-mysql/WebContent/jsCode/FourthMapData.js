$.ajaxSetup({
	contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
	type:"post"
});

function isChecked_L(index){ //0:START,1:FINAL , i: 넘버
	//marker을 다시 전부 초기화 한다.
	cleanMap();
	$.ajax({
		url:"/AddressDataServlet",
		dataType: "xml",
		data: $("#callLatLng").serialize()+"&customerID="+customerID,
		success: function(data){
			var i = 0;
			$(data).find("LatLng").each(function(){
				var tmp = i;
				var latlngPoint = new naver.maps.Point($(this).find('lat').text(),$(this).find('lng').text());
				var latlng = new naver.maps.LatLng(latlngPoint.y,latlngPoint.x);		
				
				if(i == parseInt(index)){
					//시작이랑 끝이랑 같으면 12번 그림 , 그게 아니면 11번 그림
					if(parseInt($(this).find("start").text()) == parseInt(index))
						tmp = 12;
					else 
						tmp = 11;
				}else if(parseInt($(this).find("start").text()) == i)
					tmp = 10; //start를 초기화 한다.
				
				var greenMarker = {
					    position: latlng,
					    map: map2,
					    icon: {
					    	 url:imgUrl[tmp],
				             size: new naver.maps.Size(38, 58),
				             anchor: new naver.maps.Point(19, 58),
					    }
					};
				marker[i] = new naver.maps.Marker(greenMarker);
				marker[i++].setMap(map2); // 추가	
			})
		}, error: function(data){
			console.log("실패");
		}
	});
	$("#index").val(index);	
	saveIndex();
	//서버에 데이터를 넘겨준다.
}


function saveIndex(){  //start, last 데이터를 저장해 둔다.
	$.ajax({
		url:"/AddressDataServlet",
		dataType: "html",
		data: $("#setIndexData").serialize()+"&customerID="+customerID,
		success: function(data){
		}
	});
}

function cleanMap(){ //마커를 초기화
	for(var i = 0;i< cnt;i++){
		marker[i].setMap(null); 
	}	
	marker = new Array(10);
}

function cleanSaveData(){
	$("#index").val("-1");
	saveIndex();
}

function possibleNext(){
	if(document.setIndexData.index.value == "-1"){
		$('#nextBtn').attr({'href':'#'});
		alert("마지막 목적지를 선정해주세요")
	}else{
		$('#nextBtn').attr({'href':'Loading.html'});
	}
}


function getDataFourth(){ 
	$.ajax({
		url:"/AddressDataServlet",
		dataType: "xml",
		data: $("#mData").serialize()+"&customerID="+customerID,
		success: function(data){
			var htmlStr = "";
			var tmpCnt = 0;
			var markerCnt = 0;
			$(data).find("Address").each(function(){
				htmlStr += "<div>";
				htmlStr += "<div class='left-content'>";
				htmlStr += "<input type='radio' name='finalPosition' onClick='isChecked_L("
					+ $(this).find("num").text()+");' value='"
					+ $(this).find("num").text() + "'/> </div>";
				htmlStr += "<div class='middle-content2'><img src='img/mark";
				htmlStr += $(this).find('no').text() + ".png' />" + "&nbsp;"+  $(this).find('data').text();			
				htmlStr += "</div></div>";
				
				//마크 표시
				var latlngPoint = new naver.maps.Point($(this).find('lat').text(),$(this).find('lng').text());
				var latlng = new naver.maps.LatLng(latlngPoint.y,latlngPoint.x);
				if(parseInt($(this).find("start").text()) == cnt)
					markerCnt = 10; //start를 초기화 한다.
				else
					markerCnt = cnt;
				
				var greenMarker = {
				    position: latlng,
				    map: map2,
				    icon: {
				    	 url:imgUrl[markerCnt],
			             size: new naver.maps.Size(38, 58),
			             anchor: new naver.maps.Point(19, 58),
				    }
				};
				marker[cnt] = new naver.maps.Marker(greenMarker);
				marker[cnt++].setMap(map2); // 추가
				tmpCnt++; //start를 찾기위해
			}) 
			htmlStr += "</table>";	
			$("#list3").html(htmlStr);	
		}, error: function(data){
			console.log("실패");
		}
	});
}

