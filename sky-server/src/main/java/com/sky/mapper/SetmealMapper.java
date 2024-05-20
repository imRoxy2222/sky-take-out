package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {
	/**
	 * 统计数量通过category id
	 *
	 * @param id
	 * @return
	 */
	@Select("select count(id) from setmeal where category_id = #{id}")
	int countByCategoryId(Integer id);
}
