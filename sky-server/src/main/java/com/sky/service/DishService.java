package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
	/**
	 * 新增菜品
	 *
	 * @param dishDTO: 新增菜品的参数
	 */
	void addDish(DishDTO dishDTO);
	
	/**
	 * 根据分类id查询菜品
	 *
	 * @param categoryId: 分页id
	 * @retsurn : 查询到的数据
	 */
	List<Dish> queryByCategoryId(Integer categoryId);
	
	/**
	 * 分页查询
	 *
	 * @param dishPageQueryDTO: 分页查询的参数
	 * @return : 查询结果
	 */
	PageResult queryPage(DishPageQueryDTO dishPageQueryDTO);
	
	/**
	 * 菜品起售/停售
	 *
	 * @param status: 要修改成的状态
	 * @param id:     要修改记录的id
	 */
	void modifyStatus(Integer status, Long id);
	
	/**
	 * 根据id查询菜品
	 *
	 * @param id: 查询菜品的id
	 * @return : 查询到的菜品
	 */
	DishVO queryById(Long id);
	
	/**
	 * 批量删除
	 *
	 * @param ids: 要删除的id
	 */
	void deleteByIds(List<Integer> ids);
	
	/**
	 * 修改dish
	 *
	 * @param dishDTO: 前端穿的dish id
	 */
	void modify(DishDTO dishDTO);
}
