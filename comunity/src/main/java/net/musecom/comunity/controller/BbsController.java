package net.musecom.comunity.controller;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.musecom.comunity.model.Bbs;
import net.musecom.comunity.model.BbsAdmin;
import net.musecom.comunity.model.BbsCategory;
import net.musecom.comunity.model.FileDto;
import net.musecom.comunity.model.Member;
import net.musecom.comunity.service.BbsAdminService;
import net.musecom.comunity.service.BbsAuthenticationService;
import net.musecom.comunity.service.BbsCategoryService;
import net.musecom.comunity.service.BbsFileCleanupService;
import net.musecom.comunity.service.BbsListService;
import net.musecom.comunity.service.BbsService;
import net.musecom.comunity.service.ContentsService;
import net.musecom.comunity.service.FileDeleteService;
import net.musecom.comunity.service.FileService;
import net.musecom.comunity.service.MemberService;
import net.musecom.comunity.service.PagingService;
import net.musecom.comunity.util.Paging;

@Controller
@RequestMapping("/bbs")
public class BbsController {

	@Autowired
	private BbsService bbsService;
	
	@Autowired
	private BbsAdminService adminService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private FileService fileService;

	@Autowired
	private ServletContext sc;

	@Autowired
	private ContentsService contentsControll;  //html �깭洹� �젙由щ�� �쐞�븳 �겢�옒�뒪
	
	@Autowired
	private BbsAuthenticationService autthenticationService;
	
	@Autowired
	private BbsFileCleanupService fileCleanupService;
	
	@Autowired
	private BbsCategoryService categoryService;
	
	@Autowired
	private BbsListService listService;
	
	@Autowired
	private PagingService pagingService;
	
	@Autowired
	private FileDeleteService fileDeleteService;
	
	/****************************************************************************
	 * list
	 * @param bbsid
	 * @param page
	 * @param searchKey
	 * @param searchVal
	 * @param model
	 * @return
	 */
	
	/*  
	 * 1. 沅뚰븳濡쒖쭅 :  BbsAuthenticationService 
       2. �벐�젅湲고뙆�씪�궘�젣 : BbsFileCleanupService
       3. 移댄뀒怨좊━ : BbsCategoryService 
       4. 寃뚯떆臾쇱“�쉶 諛� �뙆�씪泥섎━ : BbsListService
       5. �럹�씠吏뺤쿂由� : PaginService 
     */
	
	@GetMapping("/list")
	public String List(
		@RequestParam("bbsid") int bbsid, 
		@RequestParam(value="page", defaultValue="1") int page,
		@RequestParam(required=false) String searchKey,
		@RequestParam(required=false) String searchVal,
		Model model) {
			
		//沅뚰븳寃�利�
		if(!autthenticationService.chechAuthorization(bbsid, model)) {
			return "redirect: /comunity/";
		}
	  	
		//�벐�젅湲� �뙆�씪 �궘�젣
		fileCleanupService.cleanFiles(bbsid);
		
		//移댄뀒怨좊━ 議고쉶
		List<BbsCategory> categories = categoryService.getCategories(bbsid);
        model.addAttribute("categories", categories);
		
		//寃뚯떆臾� 議고쉶 諛� �럹�씠吏� 泥섎━
	    BbsAdmin bbsAdminDto = (BbsAdmin) model.getAttribute("adminBbs");
		int listCount = bbsAdminDto.getListcount();
		int pageCount = bbsAdminDto.getPagecount();
		int pg = (page -1) * listCount;
		
		int totalRecord = bbsService.getBbsCount(bbsid);
		Paging paging = pagingService.getPaging(totalRecord, listCount, page, pageCount);
		
		List<Bbs> bbslist = listService.getBbsList(bbsid, pg, listCount, searchKey, searchVal);
		listService.processBbsList(bbslist, totalRecord, pg, 100);
			
		model.addAttribute("paging", paging);	
		model.addAttribute("bbslist", bbslist);
	
		//인기검색어 출력
		List<Map<String, Object>> popularKeywords = bbsService.getPopularKeyword();
		model.addAttribute("popularKeywords", popularKeywords);
		
		//목록 조회
		List<BbsAdmin> bbsAdminLists = adminService.getAllBbsList();
		model.addAttribute("bbsAdminLists", bbsAdminLists);
		
		String skin = bbsAdminDto.getSkin();
		
		switch(skin) {
		   case "gallery":
			  return "gallery.list";
	   	   case "article":
			  return "article.list";
		   case "blog":
			  return "blog.list";
		   default:
			  return "bbs.list"; 
		}
		
		/*
		if(skin.equals("gallery")) {
		   return "gallery.list";
		}else if(skin.equals("article")) {
		   return "article.list";
		}else if(skin.equals("blog")) {
		   return "blog.list";	 
		}else {
			return "bbs.list";
		}
		*/
	}
	
	
	/****************************************************************************
	 * VIEW
	 * @param bbsid
	 * @param id
	 * @param pageF
	 */
	@GetMapping("/view")
	public String views(
	  @RequestParam("bbsid") int bbsid,
	  @RequestParam("id") long id,
	  @RequestParam(value="page", defaultValue="1") int page,
	  Model model
	) { 

		BbsAdmin bbsAdminDto = new BbsAdmin();
		bbsAdminDto = adminService.getBbsAdminData(bbsid);
		Member member = memberService.getAuthenticatedMember();
				
		//沅뚰븳寃�利�
		if(!autthenticationService.chechAuthorization(bbsid, model)) {
			return "redirect: /comunity/";
		}
		
		Bbs bbsView = bbsService.getBbs(id);
		int sec = bbsView.getSec();
		
		if( sec == 1 && member == null ||
			sec == 1 && member.getUserid() == null ||
			sec == 1 && !"admin".equals(member.getUserid()) &&
			sec == 1 && !member.getUserid().equals(bbsView.getUserid())) {
		    System.out.println("鍮꾨�湲��씠誘�濡� pass濡� 蹂대깂");
			return "redirect: /comunity/bbs/pass?mode=view&bbsid="+bbsid+"&id="+id+"&page="+page;
		}	
		//議고쉶�닔 利앷�
		bbsService.updateCount(id);
		
		//�뙆�씪�뾽濡쒕뱶 泥섎━
		List<FileDto> files = fileService.getFilesByBbsId(id);
		for(FileDto file : files) {
			System.out.println(file.getNewfilename());		
		}
		
		//移댄뀒怨좊━ 議고쉶
		List<BbsCategory> categories = categoryService.getCategories(bbsid);
        model.addAttribute("categories", categories);
		
		//�씤湲곌��깋�뼱 異쒕젰
		List<Map<String, Object>> popularKeywords = bbsService.getPopularKeyword();
		model.addAttribute("popularKeywords", popularKeywords);
		

		//목록 조회
		List<BbsAdmin> bbsAdminLists = adminService.getAllBbsList();
		model.addAttribute("bbsAdminLists", bbsAdminLists);
		
		
		model.addAttribute("files", files);
		model.addAttribute("adminBbs", bbsAdminDto);
	    model.addAttribute("bbsid", bbsid);
	    model.addAttribute("page", page);
		model.addAttribute("bbs", bbsView);
		return "bbs.view";
	}
	
	@GetMapping("/update")
	public String update(
			  @RequestParam("bbsid") int bbsid,
			  @RequestParam("id") long id,
			  @RequestParam(value="page", defaultValue="1") int page,
			  Model model,
			  HttpSession session
		) {
		System.out.println("�뾽�뜲�씠�듃");
		BbsAdmin bbsAdminDto = new BbsAdmin();
		bbsAdminDto = adminService.getBbsAdminData(bbsid);
		Member member = memberService.getAuthenticatedMember();
		
		//�꽭�뀡泥댄겕
		String sessionKey = "bbsAuth_" + id;
		Boolean isBbsAuthenticated = (Boolean) session.getAttribute(sessionKey);
		
		//沅뚰븳寃�利�
		if(!autthenticationService.chechAuthorization(bbsid, model)) {
			return "redirect: /comunity/";
		}
		
		Bbs bbsView = bbsService.getBbs(id);
		int sec = bbsView.getSec();
		
		if(isBbsAuthenticated == null || !isBbsAuthenticated) {
			if( member == null || member.getUserid() == null ||
			   !"admin".equals(member.getUserid()) &&
			   !member.getUserid().equals(bbsView.getUserid())) {
			    System.out.println(" pass濡� 蹂대깂");
				return "redirect: /comunity/bbs/pass?mode=edit&bbsid="+bbsid+"&id="+id+"&page="+page;
			}	
		}
		
		List<BbsCategory> categories = null;
        if(bbsAdminDto.getCategory() > 0) {
        	categories = adminService.getBbsCategoryById(bbsid);
        }
                
		//�씤湲곌��깋�뼱 異쒕젰
		List<Map<String, Object>> popularKeywords = bbsService.getPopularKeyword();
		model.addAttribute("popularKeywords", popularKeywords);
        

		//목록 조회
		List<BbsAdmin> bbsAdminLists = adminService.getAllBbsList();
		model.addAttribute("bbsAdminLists", bbsAdminLists);
		
        model.addAttribute("categories", categories);
		model.addAttribute("adminBbs", bbsAdminDto);
	    model.addAttribute("bbsid", bbsid);
	    model.addAttribute("page", page);
		model.addAttribute("bbs", bbsView);
		return "bbs.update";
	}
	
	@PostMapping("/update")
	public String updateForm() {
		
		return null;
	}
	
	
	@GetMapping("/pass")
	public String passForm(Model model) {

		//인기검색어 출력
		List<Map<String, Object>> popularKeywords = bbsService.getPopularKeyword();
		model.addAttribute("popularKeywords", popularKeywords);
		//목록 조회
		List<BbsAdmin> bbsAdminLists = adminService.getAllBbsList();
		model.addAttribute("bbsAdminLists", bbsAdminLists);
		
		return "bbs.pass";
	}
	
	
	/*****************************************************************************]
	 * write get
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/write")
	public String writeForm(@RequestParam("bbsid") int id, Model model ) {
		
		//�씤利앹젙蹂대�� �씠�슜�븳 �궗�슜�옄 �젙蹂� 媛��졇�삤湲�
		Member member = memberService.getAuthenticatedMember();
		model.addAttribute("member", member);
		
        BbsAdmin bbsAdminDto = new BbsAdmin();
		bbsAdminDto = adminService.getBbsAdminData(id);
		
		List<BbsCategory> categories = null;
        if(bbsAdminDto.getCategory() > 0) {
        	categories = adminService.getBbsCategoryById(id);
        }
        System.out.println("member" + member);
        
		//�씤湲곌��깋�뼱 異쒕젰
		List<Map<String, Object>> popularKeywords = bbsService.getPopularKeyword();
		model.addAttribute("popularKeywords", popularKeywords);

		//목록 조회
		List<BbsAdmin> bbsAdminLists = adminService.getAllBbsList();
		model.addAttribute("bbsAdminLists", bbsAdminLists);
		
        model.addAttribute("categories", categories);
		model.addAttribute("adminBbs", bbsAdminDto);
        
		return "bbs.write";
	}
	
	
	/***************************************************************************
	 * write post
	 * @param bbsid
	 * @param fileIds
	 * @param title
	 * @param content
	 * @param writer
	 * @param password
	 * @param sec
	 * @param userid
	 * @param category
	 * @param admin
	 * @param model
	 * @return
	 */
	@PostMapping("/write")
	public String writeAction(
		@RequestParam("bbsAdminId") int bbsid,	
        @RequestParam(value="fileId[]", required = false) List<Long> fileIds, 
        @RequestParam("title") String title,
        @RequestParam("content") String content,
        @RequestParam("writer") String writer,
        @RequestParam("password") String password,
        @RequestParam(name = "sec", defaultValue="0") byte sec,
        @RequestParam("userid") String userid,
        @RequestParam(name = "category", required = false) String category,
        Model model) {
		System.out.println("寃뚯떆�뙋 湲��벐湲� writeAction()");
		try {
	        Bbs bbs = new Bbs();
	        bbs.setTitle(title);
	        bbs.setContent(content);
	        bbs.setBbsid(bbsid);
	        bbs.setWriter(writer);
	        bbs.setPassword(password);
	        bbs.setSec(sec);	        
	        bbs.setUserid(userid);
	        bbs.setCategory(category);
	        
			bbsService.getBbsInsert(bbs, fileIds);
			
			if(userid.equals("admin") && bbsid==1) {
			     return "redirect:/admin/write";
			}     	
				return "redirect:/bbs/list?bbsid="+bbsid;
			
		}catch(Exception e) {
		    model.addAttribute("error", "湲� �옉�꽦以� �삤瑜섍� 諛쒖깮�뻽�뒿�땲�떎." + e.getMessage());
			if(userid.equals("admin") && bbsid==1) {
			     return "redirect:/admin/write";
			}
			return "redirect:/bbs/list?bbsid="+bbsid;
			
		}
	}
	
	/**********************
	 * 寃뚯떆臾� 鍮꾨쾲 �솗�씤
	 * @Param id
	 * @Param password
	 * @return int
	 */
	@PostMapping("/passwd")
	@ResponseBody
	public String equalPassword(
	   @RequestParam("id") long id,
	   @RequestParam("password") String password,
	   HttpSession session
	) {
		
		int r = bbsService.getBbsPassword(id, password);
		
		if(r > 0) {
			//�꽭�뀡
			try {
				String sessionKey = "bbsAuth_" + id;
				session.setAttribute(sessionKey, true);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		String res = Integer.toString(r);
		return res;
	}
	
	
	/******************************************************************************
	 * upload
	 * @param file
	 * @param bbsid
	 * @return
	 */
	
	@PostMapping("/upload")
	public ResponseEntity<Map<String, Object>> uploadFile(
		@RequestParam("file") MultipartFile file, 
		@RequestParam("bbsid") int bbsid){
        Map<String, Object> result = new HashMap<>();
        
        try {
            BbsAdmin bbsAdmin = new BbsAdmin();
            FileDto fileDto = new FileDto();
            bbsAdmin = adminService.getBbsAdminData(bbsid);
            String path = Integer.toString(bbsid);
            String extFilter = bbsAdmin.getFilechar();
            String[] ext = (extFilter != null && !extFilter.isEmpty()) ?
            		          extFilter.split(",") : null;
            long fileSize = bbsAdmin.getFilesize() * 1024 * 1024;
        	
        	fileDto = fileService.uploadFile(file, path, ext, fileSize);
        	
        	result.put("success", true);
        	result.put("fileId", fileDto.getId());
        	result.put("fileName", fileDto.getNewfilename());
        	result.put("orFileName", fileDto.getOrfilename());
        	result.put("fileSize", fileDto.getFilesize());
        	result.put("fileUrl", "/comunity/res/upload/"+path+"/"+fileDto.getNewfilename());
        	result.put("ext", fileDto.getExt());
        	
        }catch(Exception e) {
        	result.put("success" , false);
        	result.put("fileId", e.getMessage());
        	System.out.println(Arrays.toString(e.getStackTrace()));
        }

		return ResponseEntity.ok(result);
	}


	//�씠誘몄� 寃쎈줈 諛섑솚�븯湲�
	/*
	@GetMapping("/res/upload/{bbsId}/{fname}")
	@ResponseBody
	public ResponseEntity<Resource> getImage(
			@PathVariable("bbsId") int bbsId,
			@PathVariable("fname") String fname){
		try {
			Path imagePath = Paths.get("/comunity/res/upload/"+bbsId+"/"+fname);
			Resource resource = new UrlResource(imagePath.toUri().toURL());
			return ResponseEntity.ok()
				   .contentType(MediaType.IMAGE_JPEG)
				   .body(resource);
		}catch (MalformedURLException e) {
	        e.printStackTrace();
	        return ResponseEntity.notFound().build();
	    }
	}
		
	*/
	
	
	@GetMapping("/del")
	public String DeleteForm(
			@RequestParam("bbsid") int bbsid, 
			@RequestParam("id") long id, 
			@RequestParam(value="page", defaultValue="1") int page,
			Model model,
			HttpSession session) {
	
	    //�럡�뀡泥댄겕
		String sessionKey = "bbsAuth_" + id;
		Boolean isBbsAuthenticated = (Boolean) session.getAttribute(sessionKey);
		
		/* 愿�由ъ옄 沅뚰븳�씠硫� 臾댁“嫄� �궘�젣, 
		 * �쉶�썝沅뚰븳�씠硫� �븘�씠�뵒媛� 媛숈� 寃쎌슦 �궘�젣, 
		 * 洹몄쇅�뒗 鍮꾨쾲�쓣 �솗�씤�븯�뿬 �궘�젣 
		 * -- 1. �뙆�씪�쓣 �궘�젣 �븳 �썑 2. db瑜� �궘�젣
		 * */
		Member member = memberService.getAuthenticatedMember();
		//沅뚰븳寃�利�
		if(!autthenticationService.chechAuthorization(bbsid, model)) {
			return "redirect: /comunity/";
		}
		
		Bbs bbsView = bbsService.getBbs(id);
		
		if(isBbsAuthenticated == null || !isBbsAuthenticated) {
			if( member == null || member.getUserid() == null ||
			   !"admin".equals(member.getUserid()) &&
			   !member.getUserid().equals(bbsView.getUserid())) {
			    System.out.println(" pass濡� 蹂대깂");
				return "redirect: /comunity/bbs/pass?mode=del&bbsid="+bbsid+"&id="+id+"&page="+page;
			}	
		}
		
		/* 1. 泥⑤��뙆�씪�씠 �엳�뒗吏� �솗�씤�븳 �썑 �엳�쑝硫� 
		   2. 泥⑤��뙆�씪 �궘�젣
		   3. �뙆�씪db 吏��슦湲�
		   4. bbs table �궘�젣
		*/   
		if(fileDeleteService.hasFilesToDelete(id)) {
			//�궘�젣濡쒖쭅
			fileDeleteService.deleteFile(id, bbsid);
		}
		try {
			bbsService.setDeleteById(id);
		}catch(Exception e) {
			e.printStackTrace();
			return "redirect:list?bbsid="+bbsid;
		}
		
		return "redirect:list?bbsid="+bbsid;
	}
	
	
	@PostMapping("/deleteFile")
	@ResponseBody
	public Map<String, Object> deleteFile(
	  @RequestBody Map<String, String> request){
	  Map<String, Object> result = new HashMap<>();
 
	  try {
		 //�뙆�씪 �젙蹂�
		 long fileId = Long.parseLong(request.get("fileId"));
	     String bbsId = request.get("bbsId");   
	     FileDto fileDto = fileService.getFile(fileId); 
	     if(fileDto == null) {
	    	 result.put("success", false);
	    	 result.put("message", "�뙆�씪�젙蹂대�� 李얠쓣 �닔 �뾾�뒿�땲�떎.");
	    	 return result;
	     }
	       String path = "/comunity/res/upload/" + bbsId + "/";
	       String fullPath = path + fileDto.getNewfilename();
	       File file = new File(fullPath);
	       
	       //�뙆�씪�궘�젣 
	       if(file.exists() && file.delete()) {
	    	   //db �궘�젣
	    	   fileService.deleteFile(fileId);
	    	   result.put("success", true);
	    	   result.put("message", "�꽦怨듭쟻�쑝濡� �궘�젣�릺�뿀�뒿�땲�떎.");	      
	       }else{
		       result.put("success", false);
		       result.put("message", "�뙆�씪�궘�젣�뿉 �떎�뙣�뻽�뒿�땲�떎.");
	       }
	       
	  }catch(Exception e) {
	       result.put("success", false);
	       result.put("message", "�뙆�씪�궘�젣�뿉 �떎�뙣�뻽�뒿�땲�떎." + e.getMessage());
	  }
	  
	   return result;
	}
	
	
	
	@GetMapping("/download")
	public ResponseEntity<byte[]> downloadFile(
			@RequestParam("fileId") long fileId,
			@RequestParam("bbsId") String bbsid) {
		try {
		 	//�뙆�씪�젙蹂�
			FileDto fileDto = (FileDto) fileService.getFile(fileId);
			
			//�뙆�씪�씠 �엳�뒗 寃쎈줈
			//String filePath = "/comunity/res/upload/"+ bbsid + "/" + fileDto.getNewfilename();
			String basePath = System.getProperty("catalina.base")+"/wtpwebapps";
			String filePath = basePath + "/comunity/res/upload/"+ bbsid + "/" + fileDto.getNewfilename();
			//System.out.println(filePath);
			File file = new File(filePath);
			
			//�뙆�씪�씠 議댁옱�븯吏� �븡�뒗 寃쎌슦 �삁�쇅泥섎━
			if(!file.exists()) {
				throw new RuntimeException("寃쎈줈�뿉 �뙆�씪�씠 議댁옱�븯吏� �븡�뒿�땲�떎.");
			}
			
			//�뙆�씪 �뜲�씠�꽣 �씫�뼱�삤湲�
			byte[] fileContent = java.nio.file.Files.readAllBytes(file.toPath());
			
			//�뙆�씪 �떎�슫濡쒕뱶瑜� �쐞�븳 �뿤�뜑 �꽕�젙
			 String originalFileName = 
					 new String(fileDto.getOrfilename().getBytes("UTF-8"), 
							    "ISO-8859-1");
			
			return ResponseEntity.ok()
					.header("Content-Disposition", "attachment;filename=\""+originalFileName+"\"")
					.header("Content-Type", "application/octet-stream")
					.body(fileContent);
					
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/search")
    public String search(@RequestParam("searchVal") String searchVal, Model model) {
		//寃��깋 湲곕줉 ���옣
		bbsService.insertSearchKeyword(searchVal);
		
		//寃��깋 �떎�뻾
		Map<Integer, List<Bbs>> groupedResults = bbsService.searchBbsPostsGrouped(searchVal);
		model.addAttribute("groupedResults", groupedResults);
		model.addAttribute("searchVal", searchVal);
		
		//�씤湲곌��깋�뼱 異쒕젰
		List<Map<String, Object>> popularKeywords = bbsService.getPopularKeyword();
		model.addAttribute("popularKeywords", popularKeywords);

		//목록 조회
		List<BbsAdmin> bbsAdminLists = adminService.getAllBbsList();
		model.addAttribute("bbsAdminLists", bbsAdminLists);
		
		return "bbs.searchGroup";
	}
}
