package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.result.Result;

import java.util.List;

public interface DishService {
	/**
	 * 新增菜品
	 *
	 * @param dishDTO
	 */
	void addDish(DishDTO dishDTO);
	
	/**
	 * 根据分类id查询菜品
	 *
	 * @param categoryId
	 * @return
	 */
	List<Dish> queryByCategoryId(Integer categoryId);
}
