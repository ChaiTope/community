package net.musecom.comunity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.musecom.comunity.model.BbsAdmin;
import net.musecom.comunity.model.CustomUserDetails;
import net.musecom.comunity.model.FileDto;
import net.musecom.comunity.model.Member;
import net.musecom.comunity.service.BbsAdminService;
import net.musecom.comunity.service.BbsService;
import net.musecom.comunity.service.FileService;


@Controller
public class HomeController {

	@Autowired
	BbsService bbsService;
	
	@Autowired
	BbsAdminService bbsAdminService;
	
	@Autowired
	private FileService fileService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		String userid = null;
		
		//�씤利앹젙蹂대�� �씠�슜�븳 �궗�슜�옄 �젙蹂� 媛��졇�삤湲�
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("沅뚰븳" + auth);
		if(auth != null && auth.getPrincipal() instanceof CustomUserDetails) {	
		     CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
		     userid = userDetails.getUsername();
		     Member member = userDetails.getMember();
             model.addAttribute("member", member);
		}
		
		List<BbsAdmin> bbsAdminLists = bbsAdminService.getAllBbsList();
		List<Map<String, Object>> latestPosts = bbsService.selectLatestPostsMain();
		
		LocalDateTime now = LocalDateTime.now();
		
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		for(Map<String, Object> post : latestPosts) {
			Object wdateObj = post.get("wdate");
			LocalDateTime dateTime; 
			if(wdateObj instanceof LocalDateTime) {
				dateTime = (LocalDateTime) wdateObj;
			}else if(wdateObj instanceof Timestamp) {
				dateTime = ((Timestamp) wdateObj).toLocalDateTime();
			}else {
				continue; //���엯�씠 �븡留욎쑝硫� �떎�쓬�쑝濡� 
			}
			
			//24�떆媛� �씠�궡�씠硫� �떆:遺꾪삎�떇
			if(dateTime.isAfter(now.minusHours(24))){
			   post.put("latesttime", dateTime.format(timeFormatter));
			}else {			
			//24�떆媛� �씠�썑硫� �뀈�썡�씪 �삎�엵
				post.put("latesttime", dateTime.format(dateFormatter));
			}	
			
			//�씠誘몄� 戮묎린 (媛� id�쓽 泥ル쾲吏� �씠誘몄�留� 戮묒쓬)
			if("gallery".equals(post.get("skin"))) {
				long bbsid = (long) post.get("bbs_id");
				List<FileDto> files = fileService.getFilesByBbsId(bbsid);
				if(!files.isEmpty()) {
					post.put("fileFirst", files.get(0));
				}
			}
			
		}
		
		List<Map<String, Object>> popularKeywords = bbsService.getPopularKeyword();
		model.addAttribute("popularKeywords", popularKeywords);
		
		model.addAttribute("bbsAdminLists", bbsAdminLists );
		model.addAttribute("latestPosts", latestPosts);
		model.addAttribute("userid", userid );
		
		return "main.home";
	}
	
}
