<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>    
<div class="row">
   <div class='col-md-1'>
      <h1 class="mx-4"><a href="/comunity"><i class="ri-bar-chart-horizontal-line"></i></a></h1>
   </div>
 
   <div class="col-md-6">
     <ul class="nav mt-3 justify-content-around">
     	<c:forEach var="list" items="${bbsAdminLists }">
        <sec:authorize access="!isAuthenticated()">
           <c:choose>
              <c:when test="${list.lgrade > 0 }">
                 <li class="nav-item"><a href="javascript:void(0)" onClick="alert('회원제입니다 로그인해주세요')">${list.bbstitle }</a></li>
              </c:when>
              <c:otherwise>
              	 <li class="nav-item"><a href="/comunity/bbs/list?bbsid=${list.id }">${list.bbstitle }</a></li>
              </c:otherwise>
           </c:choose>
        </sec:authorize>
        
		<sec:authorize access="isAuthenticated()">
			<c:choose>
	              <c:when test="${list.lgrade > member.grade }">
	                 <li class="nav-item"><a href="javascript:void(0)" onClick="alert('권한이 없습니다.')">${list.bbstitle }</a></li>
	              </c:when>
	              <c:otherwise>
	              	 <li class="nav-item"><a href="/comunity/bbs/list?bbsid=${list.id }">${list.bbstitle }</a></li>
	              </c:otherwise>
           </c:choose>
		</sec:authorize>
        </c:forEach>
     </ul>
   </div>
   <div class="col-md-2">
   	 chat
   </div>
   
   
   <form class="form-group col-md-3" action="/comunity/bbs/search" method="get">
		<div class="input-group mt-3 mb-3">
		  <input type="text" class="form-control" name="searchVal" placeholder="통합검색...">
		  <div class="input-group-append">
            <button class="btn btn-primary" type="submit">OK</button>
          </div>
		</div>
   </form>
</div>