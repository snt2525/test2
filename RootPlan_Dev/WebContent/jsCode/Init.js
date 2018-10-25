//3번째 4번째 페이지 초기화 
var marker = new Array(10);
var cnt = 0;

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
	'img/mark10.png',
	'img/mark_start.png', //시작  (10)
	'img/mark_end.png', //끝     (11)
	'img/mark_start_end.png' //시작과 끝이 같다   (12)
]


$.ajaxSetup({
	contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
	type : "post"
});
var latlngPoint;
var map2;

$.ajax({
	contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
	type : "post",
	url:"/AddressDataServlet",
	dataType: "xml",
	data:  $("#Init").serialize()+"&customerID="+customerID,
	success: function(data){
		$(data).find("Data").each(function(){
			latlngPoint = new naver.maps.Point($(this).find('lat').text(),$(this).find('lng').text());	
			map2 = new naver.maps.Map("map2", {
		           center: new naver.maps.LatLng(latlngPoint.y, latlngPoint.x),
		           zoom: 11,
		           size : new  naver.maps.Size($(window).width() - $("#aside").width(), $(window).height()), 
		           mapTypeControl: true,
		          
		       });
			
		})
	}, error: function(data){
		console.log("실패");
	}
});

