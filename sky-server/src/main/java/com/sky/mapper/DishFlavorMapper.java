package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
	/**
	 * 插入一条记录
	 *
	 * @param flavor: 插入的记录
	 */
	void insert(DishFlavor flavor);
	
	/**
	 * 通过id查询风味
	 *
	 * @param id: id
	 * @return: 实体s
	 */
	List<DishFlavor> queryById(Long id);
	
	/**
	 * 通过dish id删除
	 *
	 * @param id: dish id
	 */
	@Delete("delete from dish_flavor where dish_id = #{id}")
	void deleteByDishId(Integer id);
}
