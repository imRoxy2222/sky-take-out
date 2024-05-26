package com.sky.service.impl;

import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.service.SetmealDishService;
import com.sky.vo.DishItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealDishServiceImpl implements SetmealDishService {
	@Autowired
	private SetmealDishMapper setmealDishMapper;
	@Autowired
	private DishMapper dishMapper;
	
	/**
	 * 根据自身id删除
	 *
	 * @param id : setmeal dish id
	 */
	@Override
	public void delete(Long id) {
		setmealDishMapper.deleteBySetmealId(id);
	}
	
	/**
	 * 根据dish id查询dish
	 *
	 * @param id : dish id
	 * @return
	 */
	@Override
	public List<SetmealDish> queryDishByDishId(Long id) {
		List<SetmealDish> setmealDishes = setmealDishMapper.queryDishByDishId(id);
		return setmealDishes;
	}
	
	/**
	 * 根据套餐id查询包含的菜品
	 *
	 * @param id: setmeal id
	 * @return
	 */
	@Override
	public List<DishItemVO> queryDish(Long id) {
		/*
		 * 需要数据: copies, description, image, name
		 * 这里只能获取name, copies
		 */
		List<SetmealDish> setmealDishes = setmealDishMapper.queryBySetmealId(id);
		if (setmealDishes == null) {
			return null;
		}
		List<DishItemVO> res = new ArrayList<>();
		/*
		 * 获取description和image
		 */
		for (SetmealDish setmealDish : setmealDishes) {
			Dish dish = dishMapper.queryById(setmealDish.getDishId());
			DishItemVO vo = DishItemVO.builder()
					.name(setmealDish.getName())
					.copies(setmealDish.getCopies())
					.description(dish.getDescription())
					.image(dish.getImage())
					.build();
			res.add(vo);
		}
		return res;
	}
}
