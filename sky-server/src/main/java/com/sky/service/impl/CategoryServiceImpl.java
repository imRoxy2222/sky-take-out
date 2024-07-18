package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	private final CategoryMapper categoryMapper;
	private final DishMapper dishMapper;
	private final SetmealMapper setmealMapper;
	
	public CategoryServiceImpl(CategoryMapper categoryMapper,
	                           DishMapper dishMapper,
	                           SetmealMapper setmealMapper) {
		this.categoryMapper = categoryMapper;
		this.dishMapper = dishMapper;
		this.setmealMapper = setmealMapper;
	}
	
	/**
	 * 修改菜品分类
	 *
	 * @param categoryDTO 修改信息
	 */
	@Override
	public void modifyCategory(CategoryDTO categoryDTO) {
		Category category = new Category();
		BeanUtils.copyProperties(categoryDTO, category);
		
		categoryMapper.update(category);
	}
	
	/**
	 * 显示所有菜品/套餐
	 *
	 * @param type 类型
	 * @return 查询结果
	 */
	@Override
	public List<Category> list(Long type) {
		return categoryMapper.list(type);
	}
	
	/**
	 * 分页显示菜品/套餐
	 *
	 * @param categoryPageQueryDTO 分页参数
	 * @return 分页结果
	 */
	@Override
	public PageResult page(CategoryPageQueryDTO categoryPageQueryDTO) {
		PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
		Page<Category> page = categoryMapper.queryPage(categoryPageQueryDTO);
		
		long total = page.getTotal();
		List<Category> categoryList = page.getResult();
		
		return new PageResult(total, categoryList);
	}
	
	/**
	 * 启用/禁用分类
	 *
	 * @param status 修改成的状态
	 * @param id     要修改的id
	 */
	@Override
	public void modifyStatus(Integer status, Long id) {
		Category category = Category
				.builder()
				.status(status)
				.id(id)
				.build();
		categoryMapper.update(category);
	}
	
	/**
	 * 新增分类
	 *
	 * @param categoryDTO 新增参数
	 */
	@Override
	public void addCategory(CategoryDTO categoryDTO) {
		Category category = new Category();
		BeanUtils.copyProperties(categoryDTO, category);
		
		// 状态先禁止
		category.setStatus(StatusConstant.DISABLE);
//		category.setCreateTime(LocalDateTime.now());
//		category.setUpdateTime(LocalDateTime.now());
//		category.setCreateUser(BaseContext.getCurrentId());
//		category.setUpdateUser(BaseContext.getCurrentId());
		
		categoryMapper.insert(category);
	}
	
	/**
	 * 根据id删除分类
	 *
	 * @param id 要删除的id
	 */
	@Override
	public void deleteById(Integer id) {
		// 查看当前分类下是否有菜品
		int count = dishMapper.countByCategoryId(id);
		if (count > 0) {
			throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
		}
		
		// 查看当前分类下是否有套餐
		count = setmealMapper.countByCategoryId(id);
		if (count > 0) {
			throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
		}
		
		categoryMapper.deleteById(id);
	}
}
