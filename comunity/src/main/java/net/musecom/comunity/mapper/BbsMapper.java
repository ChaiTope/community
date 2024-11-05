package net.musecom.comunity.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import net.musecom.comunity.model.Bbs;

@Mapper
public interface BbsMapper {
  void insertBbs(Bbs bbs);
  void refUpdateById(@Param("ref") long ref, @Param("id") long id);
  int reInsertBbs(Bbs bbs);
  int updateBbs(Bbs bbs);
  int deleteBbs(long id);
  int selectCountBbs(int bbsid);
  List<Bbs> selectBbs(@Param("bbsid") int bbsid, 
		              @Param("page") int page, 
		              @Param("recordsPerPage") int recordsPerPage);
  List<Bbs> selectSearchBbs(int bbsid, int page, int recordsPerPage, String key, String val);

  Bbs viewBbs(int id);
}
