package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface SetmealDishMapper {
	/**
	 * 根据id查询setmealDish
	 *
	 * @param id : dish id
	 * @return
	 */
	List<SetmealDish> queryDishByDishId(Long id);
	
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
	
	/**
	 * 根据setmeal id查询setmeal dish
	 *
	 * @param id : setmeal id
	 * @return : setmeal dish集合
	 */
	@Select("select * from setmeal_dish where setmeal_id = #{id}")
	List<SetmealDish> queryBySetmealId(Long id);
	
	/**
	 * 通过setmeal id查询套餐价格
	 *
	 * @param id: setmeal id
	 * @return : price
	 */
	@Select("select price from setmeal_dish where dish_id = #{id}")
	BigDecimal queryPriceBySetmealId(Long id);
}
