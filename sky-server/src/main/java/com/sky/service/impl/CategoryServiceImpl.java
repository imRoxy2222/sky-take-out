package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private DishMapper dishMapper;
	@Autowired
	private SetmealMapper setmealMapper;
	
	/**
	 * 修改菜品分类
	 *
	 * @param categoryDTO
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
	 * @param type
	 * @return
	 */
	@Override
	public List<Category> list(Long type) {
		return categoryMapper.list(type);
	}
	
	/**
	 * 分页显示菜品/套餐
	 *
	 * @param categoryPageQueryDTO
	 * @return
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
	 * @param status
	 * @param id
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
	 * @param categoryDTO
	 */
	@Override
	public void addCategory(CategoryDTO categoryDTO) {
		Category category = new Category();
		BeanUtils.copyProperties(categoryDTO, category);
		
		// 状态先禁止
		category.setStatus(StatusConstant.DISABLE);
		category.setCreateTime(LocalDateTime.now());
		category.setUpdateTime(LocalDateTime.now());
		category.setCreateUser(BaseContext.getCurrentId());
		category.setUpdateUser(BaseContext.getCurrentId());
		
		categoryMapper.insert(category);
	}
	
	/**
	 * 根据id删除分类
	 *
	 * @param id
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
