<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<c:if test="${not empty error }">
  <script>
    alert("${error}");
  </script>
</c:if>
<c:if test="${param.logout != null }">
  <script>
   alert("로그아웃 되었습니다.");
  </script> 
</c:if>
<h1>
	Hello world!  
</h1>
<c:if test="${not empty memberok }">
  <script>
   alert("정상적으로 가입되었습니다. 로그인하세요.");
  </script> 
</c:if>
<p>${test }</p>

