$.ajaxSetup({
	contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
	type:"post"
});

function isChecked_S(index){ //0:START,1:FINAL , i: 넘버
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
					tmp = 10;		
				}
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
		alert("시작 위치를 선정해주세요")
	}else{
		$.ajax({
			contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
			url:"/AddressDataServlet",
			dataType: "text",
			data:  $("#possible").serialize()+"&customerID="+customerID,
			success:function(data){	
				var size = data;
				if(size>4){ // 대중교통 반으로 나눠서 돌리기
					document.apiAB.a.value = "0";
					document.apiAB.b.value = "4";
					document.apiAB.carBlock.value = "0";
					$.ajax({
						url:"/AddressDataServlet",
						data: $("#apiAB").serialize()+"&customerID="+customerID
					});	
					document.apiAB.a.value = "4";
					document.apiAB.b.value = String(size);
					document.apiAB.carBlock.value = "1";
					$.ajax({
						url:"/AddressDataServlet",
						data: $("#apiAB").serialize()+"&customerID="+customerID
					});	
				}else{ // 자동차 
					document.apiAB.a.value = "0";
					document.apiAB.b.value = String(size);
					document.apiAB.carBlock.value = "0";
					$.ajax({
						url:"/AddressDataServlet",
						data: $("#apiAB").serialize()+"&customerID="+customerID
					});
				}	
			},
			error: function(data){
				console.log(data);
			}			
		});	
		$('#nextBtn').attr({'href':'Fourth.html'});
	}
}


//모든 데이터 호출 
function getDataThird(){
	$.ajax({
		url:"/AddressDataServlet",
		dataType: "xml",
		data: $("#getAddressData").serialize()+"&customerID="+customerID,
		success: function(data){
			var htmlStr = "";
			$(data).find("Address").each(function(){
				htmlStr += "<div>";
				htmlStr += "<div class='left-content'>";
				htmlStr += "<input type='radio' name='startPosition' onClick='isChecked_S("
					+ $(this).find("num").text()+");' value='"
					+ $(this).find("num").text() + "'/> </div>";
				htmlStr += "<div class='middle-content2'> <img src='img/mark";
				htmlStr += $(this).find('no').text() + ".png' />" + "&nbsp;"+  $(this).find('data').text();	
				htmlStr += "</div></div>";
				//마크 표시
				var latlngPoint = new naver.maps.Point($(this).find('lat').text(),$(this).find('lng').text());
				var latlng = new naver.maps.LatLng(latlngPoint.y,latlngPoint.x);
				var greenMarker = {
				    position: latlng,
				    map: map2,
				    icon: {
				    	 url:imgUrl[cnt],
			             size: new naver.maps.Size(38, 58),
			             anchor: new naver.maps.Point(19, 58),
				    }
				};
				marker[cnt] = new naver.maps.Marker(greenMarker);
				marker[cnt++].setMap(map2); // 추가	
			})
			htmlStr += "</table>";	
			$("#list2").html(htmlStr);	
		}, error: function(data){
			console.log("실패");
		}
	});
}

