package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {
	/**
	 * 统计数量通过category id
	 *
	 * @param id
	 * @return
	 */
	@Select("select count(id) from dish where category_id = #{id}")
	int countByCategoryId(Integer id);
	
	/**
	 * 根据分类id查询
	 *
	 * @param categoryId
	 * @return
	 */
	@Select("select * from dish where category_id = #{categoryId}")
	List<Dish> queryByCategoryId(Integer categoryId);
	
	/**
	 * 通过自身id查询
	 *
	 * @param dishId
	 * @return
	 */
	@Select("select * from dish where id = #{dishId}")
	Dish queryById(Long dishId);
	
	/**
	 * 分页查询
	 *
	 * @param dish
	 * @return
	 */
	Page<Dish> query(Dish dish);
	
	/**
	 * 更新
	 *
	 * @param dish : 更新的实体
	 */
	@AutoFill(OperationType.UPDATE)
	void update(Dish dish);
	
	/**
	 * 插入一条记录
	 *
	 * @param dish : 记录实体类
	 */
	void insert(Dish dish);
	
	/**
	 * 根据id删除
	 *
	 * @param id: 自身id
	 */
	@Delete("delete from dish where id = #{id}")
	void delete(Integer id);
}
