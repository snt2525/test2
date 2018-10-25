$.ajaxSetup({
	contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
	type : "post"
});

var rID = sessionStorage.getItem("rID");

function start() {
	//여기서 db불러와서 데이터를 넣어놓는다.
	$.ajax({ //dfs, 결과 순서 다시 재 호출
		type : "POST",
		url : "/AddressDataServlet",
		dataType : "html",
		data : $("#finish1").serialize()+"&customerID="+customerID+"&rID="+rID+"&cID="+id, //0
		success : function() {
			location.replace("Last.html");
		}
	});
}
