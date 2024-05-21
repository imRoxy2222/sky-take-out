package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
	/**
	 * 更新category
	 *
	 * @param category
	 */
	@AutoFill(OperationType.UPDATE)
	void update(Category category);
	
	/**
	 * 显示所有菜品/套餐
	 *
	 * @param type
	 * @return
	 */
	@Select("select * from category where type = #{type}")
	List<Category> list(Long type);
	
	/**
	 * 分页查询
	 *
	 * @param categoryPageQueryDTO
	 * @return
	 */
	Page<Category> queryPage(CategoryPageQueryDTO categoryPageQueryDTO);
	
	/**
	 * 新增
	 *
	 * @param category
	 */
	@AutoFill(OperationType.INSERT)
	void insert(Category category);
	
	/**
	 * 根据id删除分类
	 *
	 * @param id
	 */
	@Delete("delete from category where id = #{id}")
	void deleteById(Integer id);
}
