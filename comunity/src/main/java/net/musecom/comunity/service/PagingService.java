package net.musecom.comunity.service;

import org.springframework.stereotype.Service;

import net.musecom.comunity.util.Paging;

@Service
public class PagingService {
    public Paging getPaging(int totalRecord, int listCount, int page, int pageCount) {
    	return new Paging(totalRecord, listCount, page, pageCount);
    }
}
