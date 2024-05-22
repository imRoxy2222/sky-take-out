package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealDishMapper {
	/**
	 * 根据id查询setmealDish
	 *
	 * @param id
	 * @return
	 */
	SetmealDish queryDishByDishId(Long id);
	
	/**
	 * 插入新的数据
	 *
	 * @param setmealDish
	 */
	void insert(SetmealDish setmealDish);
	
	/**
	 * 删除
	 *
	 * @param id
	 */
	@Delete("delete from setmeal_dish where setmeal_id = #{id}")
	void deleteBySetmealId(Long id);
}
