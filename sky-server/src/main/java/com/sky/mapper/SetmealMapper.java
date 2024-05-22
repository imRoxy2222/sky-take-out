package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {
	/**
	 * 统计数量通过category id
	 *
	 * @param id
	 * @return
	 */
	@Select("select count(id) from setmeal where category_id = #{id}")
	int countByCategoryId(Integer id);
	
	/**
	 * 分页查询
	 *
	 * @param setmealPageQueryDTO
	 * @return
	 */
	Page<Setmeal> page(SetmealPageQueryDTO setmealPageQueryDTO);
	
	/**
	 * 更新数据
	 *
	 * @param setmeal
	 */
	@AutoFill(OperationType.UPDATE)
	void update(Setmeal setmeal);
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 */
	void delete(List<Integer> ids);
	
	/**
	 * 查询
	 *
	 * @param setmeal
	 * @return
	 */
	Setmeal query(Setmeal setmeal);
	
	/**
	 * 插入一条数据
	 *
	 * @param setmeal
	 */
	@AutoFill(OperationType.INSERT)
	void insert(Setmeal setmeal);
	
	/**
	 * 查询当前id是否已被使用
	 * @param id
	 * @return
	 */
	@Select("select count(id) from setmeal where id = #{id}")
	int queryCountById(int id);
}
