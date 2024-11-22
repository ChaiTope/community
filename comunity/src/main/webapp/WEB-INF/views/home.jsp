<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<div class="pt-3">
	<div class="row main-row">
	   <c:set var="ct" value="0" />
	   <c:forEach var="post" items="${latestPosts }">
	      <c:choose>
             <c:when test="${post.bbsadmin_id != ct }">
                <c:if test="${ct != 0 }">
                   </ul>
                      </div>       
                        </div>
                </c:if>
             <!-- 새 그룹 시작 -->
             <div class="col-md-6">
                <div class="latest-box bg-white rounded p-3 shadow-sm mb-3"> 
                   <h5 class="text-center mt-5 mb-3"><a href="/comunity/bbs/list?bbsid=${post.bbsadmin_id}">${post.bbstitle }</a></h5>
                   <c:choose>
                       <c:when test="${post.skin == 'gallery' }">
                          <ul class="gallery row">
                       </c:when>
                       <c:otherwise>
                          <ul class="latest-list">
                       </c:otherwise>
                   </c:choose>
              </c:when>
          </c:choose>
          
          <c:choose>
              <c:when test="${post.skin == 'gallery' }">
                 <li class="col-4">
                    <img 
                       src="${pageContext.request.contextPath }/res/upload/${post.bbsadmin_id }/${post.fileFirst.newfilename }" 
                       class="img-thumbnail img-gall"/>
                 </li>
              </c:when>
               <c:otherwise>          
                   <li><a href="/comunity/bbs/view?bbsid=${post.bbsadmin_id}&id=${post.bbs_id}">
                       <i class="ri-arrow-right-circle-fill"></i> ${post.title}
                       <span class="date">${post.latesttime }</span></a>    
                   </li>
               </c:otherwise>
           </c:choose>
                
           <!-- ct 갱신 -->
           <c:set var="ct" value="${post.bbsadmin_id }" />
    	   </c:forEach>     
    	   </ul>
        </div>
        </div>
         
	</div>
</div>