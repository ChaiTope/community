package net.musecom.comunity.service;

import java.util.List;

import net.musecom.comunity.model.Bbs;

public interface BbsService {

	int getBbsInsert(Bbs bbs); 
	
	int getBbsCount(int bbsid);
	List<Bbs> getBbsList(int bbsid, int page, int recordsPerPage);
	List<Bbs> getSerchBbsList(int bbsid, int page, int recordsPerPage, String key, String val);
	
}
