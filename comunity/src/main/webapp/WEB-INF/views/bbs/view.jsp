<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>   
<script>
$(function(){
	$(".download").click(function(e){
		e.preventDefault();
		const link = $(this).attr("href");
		const csrfToken = $("#_csrf").val();
		const csrfHeader = $("#_csrf_header").val();
		$.ajax({
			url: link,
			type: "GET",
			beforeSend: function(xhr) {
				xhr.setRequestHeader(csrfHeader, csrfToken);
			},
			xhrFields: {
				responseType: 'blob'
			},
			success: function(data, status, xhr) {
				const disposition = xhr.getResponseHeader('Content-Disposition');
				const fileName = disposition?
						             disposition.split('filename=')[1].replace(/"/g, '') : 
						             'download_file';
				const blob = new Blob([data]);
				const url = window.URL.createObjectURL(blob);
				const a = document.createElement("a");
				a.href= url;
				a.download = decodeURIComponent(fileName);
				document.body.appendChild(a);
				a.click();
				a.remove();
				window.URL.revokeObjectURL(url);
			},
			error: function(){
				alert("파일 다운로드에 실패했습니다.");
			}
		})
	});
});
</script>
<div class="p-5 my-4 bg-white">
   <!-- 토큰 -->
   <input type="hidden" id="_csrf" value="${_csrf.token }"/>
   <input type="hidden" id="_csrf_header" value="${_csrf.headerName }" />
   
   <h1 class="text-center">${adminBbs.bbstitle }</h1>
   <div class="viewbox">
      <div class="border-bottom d-flex justify-content-between py-2 px-4 align-items-end">
          <h3>${bbs.title }</h3>
          <div class="writer">${bbs.writer }</div>
      </div>
      <div class="infobox d-flex justify-content-end py-3 border-bottom">
         <div class="wdate">${bbs.wdate }n</div>
         <div class="hit">조회수 : ${bbs.hit }</div>
      </div> 
      <ul class="bg-light p-3 text-right mb-3">           
         <c:forEach var="file" items="${files }"> 
            <a href="/comunity/bbs/download?fileId=${file.id }&bbsId=${adminBbs.id}" class="download" target="_blank">${file.orfilename } (${file.filesize } bytes)</a> <br />
         </c:forEach>
      </ul>
      <div class="content-box px-4">
        ${bbs.content }
      </div>
      <div class="btn-box text-right pt-5">
           <a href="/comunity/bbs/list?bbsid=${bbsid }&page=${page}" class="btn btn-primary me-3"> 목 록 </a>
           <c:if test="${adminBbs.rgrade == 0 || 
                      (member != null && member.grade!=null && adminBbs.rgrade <= member.grade)}">
               <a href="/comunity/bbs/update?bbsid=${bbsid }&page=${page}&id=${bbs.id}" class="btn btn-warning ms-3"> 수 정 </a>
               <a href="/comunity/bbs/del?bbsid=${bbsid }&id=${bbs.id}&page=${page}" class="btn btn-danger me-3"> 삭 제 </a>              
               <a href="write?bbsid=${bbsid }" class="btn btn-success">글쓰기</a>
           </c:if>
           <c:if test="${adminBbs.regrade > -1 }">
	          <c:if test="${(member != null && member.grade!=null && adminBbs.regrade <= member.grade)}">
	            <a href="#" class="btn btn-success ms-3"> 답 변 </a>	     
	          </c:if>
           </c:if>
      </div>
   </div>
  
</div>   