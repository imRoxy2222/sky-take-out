package com.sky.mapper;

import com.sky.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
	
	/**
	 * 根据分类id查询
	 *
	 * @param categoryId
	 * @return
	 */
	@Select("select * from dish where category_id = #{categoryId}")
	List<Dish> queryByCategoryId(Integer categoryId);
	
	/**
	 * 通过自身id查询
	 *
	 * @param dishId
	 * @return
	 */
	@Select("select * from dish where id = #{dishId}")
	Dish queryById(Long dishId);
	
	/**
	 * 查询
	 *
	 * @param setmeal
	 * @return
	 */
//	Setmeal query(Setmeal setmeal);
}
