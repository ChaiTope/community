package net.musecom.comunity.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.musecom.comunity.mapper.BbsMapper;
import net.musecom.comunity.model.Bbs;

@Service
public class BbsServiceImpl implements BbsService {
	
	@Autowired
	private BbsMapper bbsMapper;
	
	@Override
	public int getBbsInsert(Bbs bbs) {
		return bbsMapper.insertBbs(bbs);
	}

	@Override
	public int getBbsCount(int bbsid) {
		
		return bbsMapper.selectCountBbs(getBbsCount(bbsid));
	}

	@Override
	public List<Bbs> getBbsList(int bbsid, int page, int recordsPerPage) {
		return bbsMapper.selectBbs(bbsid, page, recordsPerPage);
	}

	@Override
	public List<Bbs> getSerchBbsList(int bbsid, int page, int recordsPerPage, String key, String val) {
		
		return bbsMapper.selectSearchBbs(bbsid, page, recordsPerPage, key, val);
	}


}
