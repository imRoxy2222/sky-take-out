package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
	/**
	 * 分页查询套餐
	 *
	 * @param setmealPageQueryDTO
	 * @return
	 */
	PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
	
	/**
	 * 起售/禁售套餐
	 *
	 * @param id
	 * @param status
	 */
	void modifyStatus(Long id, Integer status);
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 */
	void deleteSetmeal(List<Integer> ids);
	
	/**
	 * 根据id查询套餐
	 *
	 * @param id
	 * @return
	 */
	SetmealVO queryById(Long id);
	
	/**
	 * 新增套餐
	 *
	 * @param setmealDTO
	 */
	void addSetmeal(SetmealDTO setmealDTO);
	
	/**
	 * 修改套餐
	 *
	 * @param setmealDTO
	 */
	void updateSetmeal(SetmealDTO setmealDTO);
}
