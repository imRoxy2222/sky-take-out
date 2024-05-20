package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;


public interface CategoryService {
	/**
	 * 修改菜品分类
	 *
	 * @param categoryDTO
	 */
	void modifyCategory(CategoryDTO categoryDTO);
	
	/**
	 * 显示所有菜品/套餐
	 *
	 * @param type
	 * @return
	 */
	List<Category> list(Long type);
	
	/**
	 * 分页显示菜品/套餐
	 *
	 * @param categoryPageQueryDTO
	 * @return
	 */
	PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);
	
	/**
	 * 启用/禁用分类
	 *
	 * @param status
	 * @param id
	 */
	void modifyStatus(Integer status, Long id);
	
	/**
	 * 新增分类
	 *
	 * @param categoryDTO
	 */
	void addCategory(CategoryDTO categoryDTO);
	
	/**
	 * 根据id删除分类
	 *
	 * @param id
	 */
	void deleteById(Integer id);
}
