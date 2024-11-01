<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>   
<link rel="stylesheet" href="/comunity/res/css/summernote-bs5.css" />

<script src="/comunity/res/js/summernote-bs5.min.js"></script>
<script src="/comunity/res/js/lang/summernote-ko-KR.js"></script> 
<script>
  $(function(){
	 $("#content").summernote({
		 lange: 'ko-KR',
		 height: 350
	 }); 
	 $("#upfile").change(function(){
		 const fileInput = $("#upfile")[0];
		 const formData = new FormData();
		 
		 //파일 선택 확인
		 if(fileInput.files.length > 0) {
			formData.append('file', fileInput.files[0]);
			$.ajax({
				url: '/comunity/bbs/upload?${_csrf.parameterName}=${_csrf.token}',
				type: 'POST',
				data: formData,
				enctype: 'multipart/form-data',
				processData: false,
				contentType: false,
				success: function(res){
					const fileurl = res.fileUrl;  //서버에서 보낼 것
					const fileName = res.fileName; //서버에서 보낼 것
					
					$(".uploadinbox").append(
					  `<div class="upload-file"><a href="${fileUrl}" target="_blank">${fileName}</div>`		
					);
					
				},
				error: function(){
					alert("문제가 발생했습니다.");
				}
			})
		 }else{
			 alert("change론 안되지롱");
		 }
		 
	 });
  });
</script>
<div class="p-5 m-5">
<form class="row" action="/comunity/bbs/write" method="post">
    <label class="col-2 text-right py-3 my-3">
       카테고리
    </label>
    <div class="col-10  py-2 my-2">
       <select name="category" class="form-control">
          <c:forEach items="${categories }" var="category">
             <option value="${category.categorytext }">${category.categorytext }</option>
          </c:forEach>
       </select>
    </div>
    <label class="col-2 text-right  py-2 my-2">
       제목
    </label>
    <div class="col-10  py-2 my-2">
       <input type="text" class="form-control" name="title" id="title" />
    </div>   
    <div class="col-12  py-2 my-2">
       <textarea name="content" id="content"></textarea>
    </div>
    
    <!-- 파일업로드 -->
    <div class="col-12 my-2 py-2">
       <div class="uploadbox">
          <div class="text-right">
             <label for="upfile" class="btn btn-success" id="fileupload">파일업로드</label>
             <input type="file" name="upfile" id="upfile" class="upfile" />
          </div>
          <div class="uploadinbox">
          
          </div>
       </div>
    </div>
    
    <div class="col-12 text-center  py-2 my-2">
       <input type="hidden" name="bbsid" value="1" />
       <input type="hidden" name="writer" value="운영자" />
       <input type="hidden" name="password" value="admin" />
       <input type="hidden" name="userid" value="admin" />
       <input type="hidden" name="sec" value="0" />
       <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" />
       <button type="reset" class="btn btn-danger me-3"> 취 소 </button>
       <button type="submit" class="btn btn-primary ms-3"> 전 송 </button>
    </div>
    
</form>
</div>