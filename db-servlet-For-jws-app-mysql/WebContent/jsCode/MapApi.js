//지도 데이터 배열
var title2 = "";
var address2 = "";
var roadAddress2 = "";
var tp2 = "";
var category2 = "";
var link3 = "";
var link4 = "";
var description2 = "";
var latlngTmp;
$.ajaxSetup({
   contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
   type : "post"
});

var map = new naver.maps.Map("map", {
   center : new naver.maps.LatLng(37.5666103, 126.9783882),
   zoom : 9,
   size : new naver.maps.Size($(window).width(), $(window).height() - 300),
   mapTypeControl : true,

});

var infoWindow = new naver.maps.InfoWindow({
    anchorSkew: true
});

map.setCursor('pointer');

// map을 눌렀을때 크롤링 보여줌
function searchCoordinateToAddress(latlng) {
   latlngTmp = latlng;
   flag = 1;
   var tm128 = naver.maps.TransCoord.fromLatLngToTM128(latlng);
   infoWindow.close();

	naver.maps.Service.reverseGeocode({
		location : tm128,
		coordType : naver.maps.Service.CoordType.TM128
	}, function(status, response) {
		if (status === naver.maps.Service.Status.ERROR) {
			return alert('Something Wrong!');
		}
		//주소를 담아 둘 배열 htmlAddresses
		var items = response.result.items, //선택한 위도를 통해 주소를 가져온다.
		htmlAddresses = [];
		//클릭한 위치의 주소를 띄어 준다.
		
		for (var i = 0, ii = items.length, item, addrType; i < ii; i++) {
			item = items[i];
			addrType = item.isRoadAddress ? '[도로명 주소]' : '[지번 주소]';
			if (addrType == '[도로명 주소]')
				htmlAddresses.push(item.address);
		}
		//console.log(items)
		if (htmlAddresses.length == 0)
			htmlAddresses.push(items[0].address);
		//console.log(items);
		//search api를 호출한다.
		document.searchApi.findLocation.value = items[1].addrdetail.rest;
    	document.searchApi.address.value = items[1].address;
    	roadAddress2 = items[1].address + "</br>";	
    	callSearchApi(1);
    	
		//여행지 추천을 위한 시군구 분리
		var sigugun = items[0].addrdetail.sigugun;
		var sigugunArr = sigugun.split(" ");
		if (sigugunArr.length == 1) {
			document.SiData.clickSi.value = items[0].addrdetail.sido;
		} else {
			document.SiData.clickSi.value = sigugunArr[0];
		}
		
		document.saveAddress.lat.value = latlng.x;
		document.saveAddress.lng.value = latlng.y;
		document.saveAddress.address.value = htmlAddresses;
		document.saveAddress.si.value = items[0].addrdetail.sido;

        getLocalSearchData();
        document.SiData.Si.value = document.SiData.clickSi.value;
   });
}

function makeList(xmlStr) { //umtk 좌표를 latlng로 변환하고, 변환한 infoWindow를 띄어준다
   flag = 1;
   $(xmlStr).find("juso").each(
         function() {
            var tmp = new naver.maps.Point($(this).find('entX').text(), $(
                  this).find('entY').text());
            latlngTmp = naver.maps.TransCoord.fromUTMKToLatLng(tmp);
            infoWindow.close();
         });
   var tm128 = naver.maps.TransCoord.fromLatLngToTM128(latlngTmp);
   naver.maps.Service.reverseGeocode(
        {
           location : tm128,
           coordType : naver.maps.Service.CoordType.TM128
        },
        function(status, response) {
           if (status === naver.maps.Service.Status.ERROR) {
              return alert('Something Wrong!');
     }
     //form에 위도 경도를 저장해둔다.
     document.saveAddress.lat.value = latlngTmp.x;
     document.saveAddress.lng.value = latlngTmp.y;
     if(document.form.jibunAddr.value == "")
        document.saveAddress.address.value = document.form.roadAddrPart1.value;
     else
        document.saveAddress.address.value = document.form.jibunAddr.value;
  });

}

function initGeocoder() { //map 초기화
   map.addListener('click', function(e) { //지도를 클릭했을 때 이벤트
      document.form.roadAddrPart1.value = ""; //주소text 초기화
      searchCoordinateToAddress(e.coord); //위도 -> 주소        

   });
   map.addListener('rightclick', function(e) { //오른쪽 마우스 누르면 창이 닫힌다.
      infoWindow.close();
   });
}

naver.maps.onJSContentLoaded = initGeocoder;

/* 주소 입력 팝업을 호출한다*/
function goPopup() {
   // 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(http://www.juso.go.kr/addrlink/addrLinkUrl.do)를 호출하게 됩니다.
     $("#roadAddrPart1").val("");  //주소text 초기화
      var pop = window.open("jusoPopup.jsp","pop","width=570,height=420,  scrollbars=yes, resizable=yes");
      flag=1;
}

/*왜 있는지 모르겠음...하지만 주소API와 연관*/
function jusoCallBack(roadFullAddr, roadAddrPart1, addrDetail, roadAddrPart2,
      engAddr, jibunAddr, zipNo, admCd, rnMgtSn, bdMgtSn, detBdNmList, bdNm,
      bdKdcd, siNm, sggNm, emdNm, liNm, rn, udrtYn, buldMnnm, buldSlno, mtYn,
      lnbrMnnm, lnbrSlno, emdNo) {
   document.form.roadAddrPart1.value = roadAddrPart1;
   document.form.roadAddrPart2.value = roadAddrPart2;
   document.form.addrDetail.value = addrDetail;
   document.form.zipNo.value = zipNo;
}

/*좌표 제공API에서 umtk(x,y)좌표를 받아오는 부분*/
function getAddr() {
   $.ajax({
      url : "http://www.juso.go.kr/addrlink/addrCoordApiJsonp.do", //인터넷망
      type : "post",
      data : $("#form2").serialize()+"&customerID="+customerID,
      dataType : "jsonp",
      crossDomain : true,
      success : function(xmlStr) {
         if (navigator.appName.indexOf("Microsoft") > -1) {
            var xmlData = new ActiveXObject("Microsoft.XMLDOM");
            xmlData.loadXML(xmlStr.returnXml)
         } else {
            var xmlData = xmlStr.returnXml;
         }
         var errCode = $(xmlData).find("errorCode").text();
         var errDesc = $(xmlData).find("errorMessage").text();
         if (errCode != "0") {
            alert("주소를 입력해 주세요.");
         } else {
            if (xmlStr != null) {
               makeList(xmlData);
               callSearchApi(0);
            }
         }
      },
      error : function(xhr, status, error) {
         alert("에러발생");
      }
   });
}