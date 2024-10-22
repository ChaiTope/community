package net.musecom.comunity.mapper;

import org.apache.ibatis.annotations.Mapper;

import net.musecom.comunity.model.BbsCategory;

@Mapper
public interface BbsCategoryMapper {
   int insertCategory(BbsCategory category);
   int deleteCategory(int id);
   int updateCategory(BbsCategory category);
}
