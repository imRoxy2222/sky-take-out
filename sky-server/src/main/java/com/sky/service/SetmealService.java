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
	 * @param id     : 要修改套餐的id
	 * @param status : 要修改成的状态
	 * @return
	 */
	boolean modifyStatus(Long id, Integer status);
	
	/**
	 * 批量删除
	 *
	 * @param ids : 要删除的套餐ids
	 */
	void deleteSetmeal(List<Integer> ids);
	
	/**
	 * 根据id查询套餐
	 *
	 * @param id : 要查询套餐的id
	 * @return : 套餐实体
	 */
	SetmealVO queryById(Long id);
	
	/**
	 * 新增套餐
	 *
	 * @param setmealDTO : 要新增的套餐
	 */
	void addSetmeal(SetmealDTO setmealDTO);
	
	/**
	 * 修改套餐
	 *
	 * @param setmealDTO : 要修改的套餐
	 */
	void updateSetmeal(SetmealDTO setmealDTO);
	
	List<SetmealVO> queryByCategoryId(Integer categoryId);
}
