package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
	
	@Autowired
	private SetmealMapper setmealMapper;
	@Autowired
	private DishMapper dishMapper;
	@Autowired
	private SetmealDishMapper setmealDishMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	
	/**
	 * 分页查询套餐
	 *
	 * @param setmealPageQueryDTO
	 * @return
	 */
	@Override
	public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
		
		PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
		
		Page<Setmeal> page = setmealMapper.page(setmealPageQueryDTO);
		
		long total = page.getTotal();
		List<Setmeal> records = page.getResult();
		
		return new PageResult(total, records);
	}
	
	/**
	 * 起售/禁售套餐
	 *
	 * @param id
	 * @param status
	 */
	@Override
	public void modifyStatus(Long id, Integer status) {
		if (status != StatusConstant.ENABLE && status != StatusConstant.DISABLE) {
			return;
		}
		
		Setmeal setmeal = Setmeal.builder().status(status).id(id).build();
		
		setmealMapper.update(setmeal);
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 */
	@Override
	public void deleteSetmeal(List<Integer> ids) {
		setmealMapper.delete(ids);
	}
	
	/**
	 * 根据id查询套餐
	 *
	 * @param id
	 * @return
	 */
	@Override
	public SetmealVO queryById(Long id) {
		Setmeal setmeal = Setmeal.builder().id(id).build();
		// 套餐查询
		Setmeal query = setmealMapper.query(setmeal);
		if (query == null) {
			return null;
		}
		// 获取分类名称
		String categoryName = categoryMapper.queryByCategoryName(query.getCategoryId());
		
		SetmealVO setmealVO = new SetmealVO();
		setmealVO.setCategoryName(categoryName);
		BeanUtils.copyProperties(query, setmealVO);
		
		return setmealVO;
	}
	
	/**
	 * 新增套餐
	 *
	 * @param setmealDTO
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void addSetmeal(SetmealDTO setmealDTO) {
		// 查看关联的菜品是否都存在
		if (setmealDTO.getSetmealDishes() != null) {
			for (SetmealDish setmealDish : setmealDTO.getSetmealDishes()) {
				Long dishId = setmealDish.getDishId();
				Dish dish = dishMapper.queryById(dishId);
				if (dish == null) {
					return;
				}
			}
		}
		
		// 插入套餐
		Setmeal setmeal = new Setmeal();
		BeanUtils.copyProperties(setmealDTO, setmeal);
		setmealMapper.insert(setmeal);
		
		
		// 关联菜品都存在,存储到数据库
		List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
		for (SetmealDish setmealDish : setmealDishes) {
			setmealDish.setSetmealId(setmeal.getId());
			setmealDishMapper.insert(setmealDish);
		}
	}
	
	/**
	 * 修改套餐
	 *
	 * @param setmealDTO
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void updateSetmeal(SetmealDTO setmealDTO) {
		Setmeal setmeal = new Setmeal();
		BeanUtils.copyProperties(setmealDTO, setmeal);
		
		Long id = setmeal.getId();
		
		// 删除原有菜品
		setmealDishMapper.deleteBySetmealId(id);
		
		// 添加新的菜品
		for (SetmealDish setmealDish : setmealDTO.getSetmealDishes()) {
			setmealDish.setSetmealId(id);
			setmealDishMapper.insert(setmealDish);
		}
	}
	
	/**
	 * 根据categoryId查询
	 *
	 * @param categoryId
	 * @return
	 */
	@Override
	public List<SetmealVO> queryByCategoryId(Integer categoryId) {
		Setmeal setmeal = Setmeal.builder()
				.categoryId(Long.valueOf(categoryId))
				.status(StatusConstant.ENABLE)
				.build();
		List<SetmealVO> result = new ArrayList<>();
		List<Setmeal> query = setmealMapper.queryByCategoryId(setmeal);
		for (Setmeal setmeal1 : query) {
			SetmealVO tmp = new SetmealVO();
			BeanUtils.copyProperties(setmeal1, tmp);
			
			// 添加categoryName
			Long TmpcategoryId = setmeal1.getCategoryId();
			String categoryName = categoryMapper.queryByCategoryName(TmpcategoryId);
			tmp.setCategoryName(categoryName);
			
			// 添加setmealDishes
			Long id = setmeal1.getId();
			List<SetmealDish> setmealDish = setmealDishMapper.queryDishByDishId(id);
			tmp.setSetmealDishes(setmealDish);
			// TODO unknown
			result.add(tmp);
		}
		return result;
	}
}
