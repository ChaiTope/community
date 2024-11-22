<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<a href="/comunity"><img src="/comunity/res/images/logo.png" class="img-fluid log" alt="logo"></a>

<sec:authorize access="isAuthenticated()">
   <sec:authorize access="hasAnyRole('ROLE_USER', 'ROLE_ADMIN')">
      <p>안녕하세요? ${member.username }회원님 (level: ${member.grade})</p>  
   </sec:authorize> 
   <sec:authorize access="hasRole('ROLE_ADMIN')">
      <p>당신은 관리자 입니다.
         <a href="./admin" target="_blank">관리자모드</a>
      </p>
   </sec:authorize>
   <form action="${pageContext.request.contextPath}/logout" method="post">
      <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" />
      <button type="submit">로그아웃</button>
   </form>
</sec:authorize>

<sec:authorize access="!isAuthenticated()">
	<form action="/comunity/login" method="post">
	<div class="px-3 py-2">
	   <input type="text" name="userid" id="userid" class="form-control" placeholder="아이디">
	</div>
	<div class="px-3 py-2">
	   <input type="password" class="form-control" name="userpass" id="userpass" placeholder="비밀번호"></td>
	</div>
	<div class="px-3 py-2 text-right">
	   <input type="reset" value=" 취소 " class="btn btn-warning"/> 
	   <input type="submit" value=" 로 그 인 " class="btn btn-success" />
	</div>
	   <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" />
	</form>
	<div class="text-right my-2 mx-3 border-top">
	  <a href="/comunity/register">회원가입</a>
	</div>
</sec:authorize>

<h3 class="mt-5 mb-2">인기 검색어</h3>
<ul class="list-group">
   <c:forEach var="keyword" items="${popularKeywords }">
      <li class="list-group-item">
         ${keyword.keyword } (${keyword.search_count }회)
      </li>
   </c:forEach>
</ul>
