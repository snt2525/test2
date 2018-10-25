$.ajaxSetup({
	contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
	type : "post"
});

var ptFlag = 0;
var carFlag = 0;
function start() {
	$.ajax({
		url : "/AddressDataServlet",
		dataType : "html",
		data : $("#finish1").serialize()+"&customerID="+customerID, //0
		success : function(data) {
			if (data == 1) {
				callResultPT();
			} else {
				console.log("실패");
			}
		}
	});
	$.ajax({
		url : "/AddressDataServlet",
		dataType : "html",
		data : $("#finish2").serialize()+"&customerID="+customerID, //1
		success : function(data) {
			if (data == 1) {
				callResultCar();
			}
		}
	});
}

function callResultPT() { //대중교통
	$.ajax({ //dfs, 결과 순서 다시 재 호출
		type : "POST",
		url : "/AddressDataServlet",
		dataType : "text",
		data : $("#finishCallDFS1").serialize()+"&customerID="+customerID, //0
		success : function(data) {
			if(data == "1"){
				alert("경로를 제공하지 않습니다.")
				location.href="Second.html";
			}
			ptFlag = 1;
		}
	});
}
function callResultCar() { //자동차   
	$.ajax({ //dfs, 결과 순서 다시 재 호출
		type : "POST",
		url : "/AddressDataServlet",
		dataType : "html",
		data : $("#finishCallDFS2").serialize()+"&customerID="+customerID, //1
		success : function() {
			carFlag = 1;
		}
	});

	location.replace("Last.html");
}