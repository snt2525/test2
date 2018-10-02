//여행지 데이터 배열
var title = new Array(100);
var LocationImg = new Array(100);
var address = new Array(100);
var roadaddress = new Array(100);
var tp = new Array(100);
var category = new Array(100);
var link = new Array(100);
var link2 = new Array(100);
var description = new Array(100);
var keyword = "여행지";

var latlngTmp2; //위도 경도
var cntNow = 1;
$.ajaxSetup({
   contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
   type:"post"
});

function button_click(value){
	keyword=value;
	document.SiData.keywordVal.value=value;
	//alert(document.SiData.keywordVal.value);
	document.SiData.Si.value = "미국";
	document.all("message").innerHTML="가이드 키워드입니다. 클릭한 해당지역의 키워드를 기준으로 오른쪽에 결과를 볼 수 있습니다. <b>" 
		+ value + "</b>을 선택하셨습니다.";
	getLocalSearchData();
}

var isStarted = 0;
function getLocalSearchData(){
	var word = keyword;
   $.ajax({
       url:"/CallSearchLocalApi",
       dataType: "text",
       data: $("#SiData").serialize()+"&customerID="+customerID,
       success: function(data){
    	   if(data!='0'){
	    	   var htmlStr = "";
	          htmlStr += "<div id='box'>";
	          $(data).find("CityData").each(function(){
	             htmlStr += "<h2 class='h2-style'>"+ $(this).find('LocationCity').text()+ " " + word +"</h2>";    
	          })
	          $(data).find("Data").each(function(){     
	             htmlStr += "<div class='item1'>";
	             htmlStr += "<div class='wow fadeInLeft' data-wow-delay='0.2s'>";
	             htmlStr += "<img class='image_container' src='"
	                + $(this).find('LocationImage').text()+"'></div>";   
	             htmlStr += "<div>";
	             htmlStr += "<h5 class='service-h5'>"+$(this).find('LocationTitle').text()+"</h5>";
	             htmlStr += "<a href='#' name='" + $(this).find('no').text()+"' id='"+$(this).find('no').text()+"' " +
	             "class='btnSearch btn-skin' onClick ='showAddressData("
	             +$(this).find('LocationMapx').text()+","+$(this).find('LocationMapy').text()+","+$(this).find('no').text()+");'>위치 보기</a>";
	           /*  htmlStr += "<img src='img/LocationButton.png' onmouseover='this.src=\"img/LocationButtonPressed.png\";'" +
	             		"onmouseout='this.src=\"img/LocationButton.png\";' onclick='this.src=\"img/LocationButtonPressed.png\";'/></a>";*/
	             htmlStr += "<input type='hidden' id='"+$(this).find('no').text()+"' name='"+$(this).find('no').text()+"' value='"+$(this).find('LocationTitle').text()+"'/>";
	             htmlStr += "</div></div>";
	             
	             var num = parseInt($(this).find('no').text());
	             link[num] = $(this).find('LocationLink').text();
	             if( link[num] != "" )
	                link2[num] =  $(this).find('LocationLink').text()+" </a></br>";
	             else
	                link2[num] = "";
	             title[num] = $(this).find('LocationTitle').text();
	             description[num] = $(this).find('LocationDescription').text();
	             if( description[num] != "" )
	                description[num] += "</br>";                       
	             if($(this).find('LocationRoadaddress').text() == "")
	                roadaddress[num] = $(this).find('LocationRoadaddress').text();
	             else
	                roadaddress[num] = $(this).find('LocationRoadaddress').text() +"</br>";
	             title[num] = $(this).find('LocationTitle').text();
	             category[num] = $(this).find('LocationCategory').text();
	             tp[num] = $(this).find('LocationTP').text();
	             if( tp[num] != "" )
	                tp[num] += " | ";
	             address[num] = $(this).find('LocationAddress').text() +"</br>";
	          });
	          htmlStr += "</div>";
	          
	          $("#crawlingData").html(htmlStr);
    	   }
       }, error: function(data){
    	   //console.log(data);
            // alert("데이터 받기 실패");
    }
   });
}

// 크롤링에서 어느 한 데이터 클릭했을 때
function showAddressData(xData,yData,no){  //나중에 marker가 안나온다면 latlngTmp.x와 y를 바꿔보자.'
   flag = 1;
   cntNow = 0; //앨범 순서
   document.getImgURL.localName.value = title[no]; //타이틀을 넣는다.
   var HOME_PATH = window.HOME_PATH || '.';
   var tm128 =  new naver.maps.Point(parseInt(xData),parseInt(yData));
   
   $.ajax({
       url:"/CallSearchLocalApi",
       dataType: "text",
       type : "post",
       data: $("#getImgURL").serialize()+"&customerID="+customerID,
       success: function(data){
    	  var cnt = 0;   	  
          $(data).find("Data").each(function(){
        	  LocationImg[cnt++] =  $(this).find('imgUrl').text(); 	 
          })	            
           naver.maps.Service.reverseGeocode({
                  location: tm128,
                  coordType: naver.maps.Service.CoordType.TM128
              }, function(status, response) {
                  if (status === naver.maps.Service.Status.ERROR) {
                      return alert('Something Wrong!');
                  }   
             
                  latlngTmp2 = new naver.maps.TransCoord.fromTM128ToLatLng(tm128);
                  infoWindow.setContent([
                	  '<div style="position:relative;padding:20px;width:750px;height:220px;">',
       	              '<div class="info-second">',    
       	              '<h6 class="info-h6">' + title[no] +'</h6>',
       	              '<a href="#" name="btn" onClick="clickADDBtn();">',
       	              '<img class="info-add-button" src="img/add_color.png" onmouseover="this.src=\'img/add_gray.png\';" onmouseout="this.src=\'img/add_color.png\';"/></a><br>',
       	              //'<input type="button" name="btn" class="info-add-button" value="담기" onClick="clickADDBtn();"/></br>',
       	              '<p class="info-p">' + address[no] + roadaddress[no],
       	              tp[no] + category[no] +'</p>',                                                                     
       	               '<p class="info-second-p">' + description[no],         
       	               '<a href="'+ link[no] +'" class="info-first-a"  target="_blank">'+ link2[no],
       	              '</a> </br> </p>',
       	           '</div>',
       	              '<div class="info-image1" >',
       	              '<a href="#" onClick="getImg('+no+', 0);"><img src="img/imageLeftButton.png" class="info-left-button"/></a>',
       	              '<img src="'+ LocationImg[cntNow] +'" width="250px" height="200px" style= "float:left;"/>',
       	              '<a href="#" onClick="getImg('+no+', 1);"><img src="img/imageRightButton.png" class="info-right-button"/></a>',
       	              '</div></div>'
       	         ].join('\n'));
       	         infoWindow.open(map, latlngTmp2);   
       	         map.setCenter(latlngTmp2);    
                  //이미지 url을 호출한다.
		   	     document.saveAddress.lat.value = latlngTmp2.x;  
		         document.saveAddress.lng.value = latlngTmp2.y;
		         document.saveAddress.address.value = title[no];
		         document.saveAddress.si.value =  document.SiData.Si.value;
                 
              });               
       }, error: function(data){
    	   console.log("실패");
       }
   });  
}


function getImg(no , i){
	if(i == 0){
		cntNow ++;
		if(cntNow == 9)
			cntNow = 0;
	}else if(i == 1){
		cntNow --;
		if(cntNow == -1)
			cntNow = 9;
	}
	infoWindow.close();
	  infoWindow.setContent([
    	  '<div style="position:relative;padding:20px;width:750px;height:220px;">',
             '<div class="info-second">',    
             '<h6 class="info-h6">' + title[no] +'</h6>',
             '<a href="#" name="btn" onClick="clickADDBtn();">',
	         '<img class="info-add-button" src="img/add_color.png" onmouseover="this.src=\'img/add_gray.png\';" onmouseout="this.src=\'img/add_color.png\';"/></a><br>',
             //'<input type="button" name="btn" class="info-add-button" value="담기" onClick="clickADDBtn();"/></br>',
             '<p class="info-p">' + address[no] + roadaddress[no],
               tp[no] + category[no] +'</p>',                                                                     
              '<p class="info-second-p">' + description[no],         
              '<a href="'+ link[no] +'" class="info-first-a"  target="_blank">'+ link2[no],
             '</a> </br> </p>',
          '</div>',
             '<div class="info-image1" >',
             	'<a href="#" onClick="getImg('+no+', 0);"><img src="img/imageLeftButton.png" class="info-left-button"/></a>',
	              '<img src="'+ LocationImg[cntNow] +'" width="250px" height="200px" style= "float:left;"/>',
	              '<a href="#" onClick="getImg('+no+', 1);"><img src="img/imageRightButton.png" class="info-right-button"/></a>',
             '</div></div>'
        ].join('\n'));
        infoWindow.open(map, latlngTmp2);   
        map.setCenter(latlngTmp2);  
}

function init(num){
   title2 = "";
   LocationImg2 = "";
   address2 = "";
   tp2 = "";
   category2 = "";
    link3 = "";
    link4 = "";
    description2 = "";  
    document.searchApi.num.value = num;
}

function callSearchApi(num){
      //초기화
      init(num);   
       if(num == 0){
          document.searchApi.findLocation.value = document.form.jibunAddr.value;
          document.searchApi.address.value = document.form.roadAddrPart1.value;
          roadAddress2 = document.form.roadAddrPart1.value + "</br>";   
       }
      
      if(document.searchApi.findLocation.value == ""){
         makeInfo();
      }else{       
         $.ajax({
            url:"/CallSearchLocalApi",
            type : "post",
            dataType: "xml",
            data: $("#searchApi").serialize()+"&customerID="+customerID,
            success: function(data){               
               $(data).find("ResultData").each(function(){   
                  if($(this).find('title').text() == "l.l"){
                     roadAddress2 = "<h5>"+roadAddress2+"</h5>";
                  }else{   
                     title2 = $(this).find('title').text();      
                     document.saveAddress.address.value = title2;
                     address2 = $(this).find('address').text();
                     tp2 = $(this).find('telephone').text();
                     category2 = $(this).find('category').text();
                     link3 = $(this).find('link').text();
                     description2 = $(this).find('description').text();   
                     //주소
                     if(address2 != "")
                        address2 += "</br>";
                     //전화번호
                     if( tp2 != "" )
                           tp2 += " | ";      
                     //링크
                     if( link3 != "" )
                           link4 =  $(this).find('link').text()+" </a></br>";
                        else
                           link4 = "";                
                     //설명
                     if(description2 != "" )
                           description2 += "</br>"                      
                  }
                    makeInfo();
               })            
                  
            }, error: function(data){
            	console.log("실패");
            }
         });
      }
   }
function makeInfo(flag){
   infoWindow.setContent([ 
       '<div style="position:relative;padding:20px;width:300px;height:50px;font-color:black">',
       '<h6 class="info-h6">' + title2 +'</h6>',
       '<a href="#" name="btn" onClick="clickADDBtn();">',
       '<img class="info-add-button" src="img/add_color.png" onmouseover="this.src=\'img/add_gray.png\';" onmouseout="this.src=\'img/add_color.png\';"/></a><br>',
       //'<input type="button" name="btn" class="info-add-button" value="담기" onClick="clickADDBtn();"/></br>',
       '<p class="info-p">' + address2 + roadAddress2,
        tp2 + category2 +'</p>',                                                                 
       '<p>' + description2,         
       '<a href="'+ link3 +'" class="info-first-a"  target="_blank">'+ link4,
       '</br> </p>',
      '</div>'
   ].join('\n'));
   
   map.setCenter(latlngTmp);
   infoWindow.open(map, latlngTmp);
}