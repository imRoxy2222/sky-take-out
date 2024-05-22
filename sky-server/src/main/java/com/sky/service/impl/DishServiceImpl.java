package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.Result;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
	
	@Autowired
	private DishMapper dishMapper;
	
	/**
	 * 新增菜品
	 *
	 * @param dishDTO
	 */
	// TODO 未完成
	@Override
	public void addDish(DishDTO dishDTO) {
		Dish dish = new Dish();
		BeanUtils.copyProperties(dishDTO, dish);
		
	}
	
	/**
	 * 根据分类id查询菜品
	 *
	 * @param categoryId
	 * @return
	 */
	@Override
	public List<Dish> queryByCategoryId(Integer categoryId) {
		return dishMapper.queryByCategoryId(categoryId);
	}
}
