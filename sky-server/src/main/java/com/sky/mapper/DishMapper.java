package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {
	/**
	 * 统计数量通过category id
	 *
	 * @param id
	 * @return
	 */
	@Select("select count(id) from dish where category_id = #{id}")
	int countByCategoryId(Integer id);
}
