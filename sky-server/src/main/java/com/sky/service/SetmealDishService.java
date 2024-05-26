package com.sky.service;

import com.sky.entity.SetmealDish;
import com.sky.vo.DishItemVO;

import java.util.List;

public interface SetmealDishService {
	
	/**
	 * 删除
	 *
	 * @param id : setmeal dish id
	 */
	void delete(Long id);
	
	/**
	 * 根据dish id查询dish
	 *
	 * @param id : dish id
	 * @return
	 */
	List<SetmealDish> queryDishByDishId(Long id);
	
	/**
	 * 根据套餐id查询包含的菜品
	 *
	 * @param id: setmeal id
	 * @return
	 */
	List<DishItemVO> queryDish(Long id);
}
