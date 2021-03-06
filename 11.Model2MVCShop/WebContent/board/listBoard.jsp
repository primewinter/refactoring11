<%@ page contentType="text/html; charset=EUC-KR" %>
<%@ page pageEncoding="EUC-KR"%>

<!--  ///////////////////////// JSTL  ////////////////////////// -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>

<html lang="ko">
	
<head>
	<meta charset="EUC-KR">
	
	<!-- 참조 : http://getbootstrap.com/css/   참조 -->
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	
	<!--  ///////////////////////// Bootstrap, jQuery CDN ////////////////////////// -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" >
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" >
	<link href="https://fonts.googleapis.com/css?family=Noto+Sans+KR:400,500&display=swap" rel="stylesheet">
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" ></script>
	
	
	<!-- Bootstrap Dropdown Hover CSS -->
   <link href="/css/animate.min.css" rel="stylesheet">
   <link href="/css/bootstrap-dropdownhover.min.css" rel="stylesheet">
    <!-- Bootstrap Dropdown Hover JS -->
   <script src="/javascript/bootstrap-dropdownhover.min.js"></script>
   
   
   <!-- jQuery UI toolTip 사용 CSS-->
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <!-- jQuery UI toolTip 사용 JS-->
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	
	<!--  ///////////////////////// CSS ////////////////////////// -->
	<style>
	  body {
            padding-top : 100px;
            font-family:  'Noto Sans KR', sans-serif;
        }
    </style>
<script type="text/javascript">
function fncGetList(){
	var a = arguments;
	switch (a.length){
     case 1: //-- 매개변수가 하나일 때
        var currentPage = a[0];
        $("#currentPage").val(currentPage)
        $("form").attr("method" , "POST").attr("action" , "/board/listBoard").submit();
		break;
	case 2: //-- 매개변수가 두 개 일 때
     	var currentPage = a[0];
    	var sort = a[1];
    	$("#sort").val(sort)
		$("#currentPage").val(currentPage)
		$("form").attr("method" , "POST").attr("action" , "/board/listBoard").submit();
		break;
	}
}
$(function() {
	 
	$(".col-md-4").mouseout( function() {
 		$(this).css({'background-color':"white"});
 	}).mouseover( function(){
 		$(this).css({'background-color':'powderblue'});
 	});
	
	$(".link").on("click", function() {
		var text = $(this).find("h5").html();
		var tranNo = text.substring(4);
		self.location = "/board/getBoard?boardNo="+tranNo.trim();
	 });
	
	
	 $( "button:contains('검색')" ).on("click" , function() {
		alert("검색");
		fncGetList(1);
	});
	 
	 $("a:contains('3일')").on("click", function() {
		 fncGetList(1, 'lately3days');
	 });
	 
	 $("a:contains('일주일')").on("click", function() {
		 fncGetList(1, 'lately1week');
	 });
	 
	 $(document).on('click', 'a[href="#"]', function(e) {
			e.preventDefault();
	});
	 
	 $("#addBoard").on("click", function() {
		 self.location="/board/addBoard.jsp"
	 });
	 
});

</script>
</head>

<body>
	
	<jsp:include page="/layout/toolbar.jsp" />


<form name="detailForm" method="post">
<input type = "hidden" name="list" value="board">
<input type="hidden" id="sort" name="sort" value="">
<input type="hidden" id="boardNo" name="boardNo" value="">

<div class="container">
	
		<div class="page-header text-info">
	       <h3>게시판</h3>
	    </div>
	    
		<div class="row">
	    
		    <div class="col-md-6 text-left">
		    	<p class="text-primary">
		    		전체  ${resultPage.totalCount } 건수, 현재 ${resultPage.currentPage}  페이지
		    	</p>
		    </div>
		    
		    <div class="col-md-6 text-right">
			    
				  <div class="form-group">
				    <select class="form-control" name="searchCondition" >
						<option value="0"  ${ ! empty search.searchCondition && search.searchCondition==0 ? "selected" : "" }>제목</option>
						<option value="1"  ${ ! empty search.searchCondition && search.searchCondition==1 ? "selected" : "" }>글 내용</option>
					</select>
				  </div>
				  
				  <div class="form-group">
				    <label class="sr-only" for="searchKeyword">검색어</label>
				    <input type="text" class="form-control" id="searchKeyword" name="searchKeyword"  placeholder="검색어"
				    			 value="${! empty search.searchKeyword ? search.searchKeyword : '' }"  >
				  </div>
				  
				  <button type="button" class="btn btn-default">검색</button>
				  <button type="button" id="addBoard">글쓰기</button>
				  
				  <!-- PageNavigation 선택 페이지 값을 보내는 부분 -->
				  <input type="hidden" id="currentPage" name="currentPage" value=""/>
				  
	    	</div>
	    	
		</div>

<p align="left">
<a href="#">[최근 3일]</a>
<a href="#">[최근 일주일]</a>
</p>


<div class="container-fluid">
      
        <div class="row">
					<c:set var="i" value="0" />
					<c:forEach var="board" items="${list}">
					<div class="col-md-4" style="text-align:left;">
						<div class="link">
								<h5>No. ${board.boardNo} </h5>
								<h3>${board.title }</h3>
								<br/>
								${board.regDate}<br/>
								by. ${board.userId }<br/>
						</div>
						<div>
						</div>
					</div>
					</c:forEach>
	</div>
</div>

</div>	
</form>

<jsp:include page="../common/pageNavigator.jsp"/>

</body>
</html>