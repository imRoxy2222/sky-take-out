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
import java.util.Objects;

@Service
public class SetmealServiceImpl implements SetmealService {
	
	private final SetmealMapper setmealMapper;
	private final DishMapper dishMapper;
	private final SetmealDishMapper setmealDishMapper;
	private final CategoryMapper categoryMapper;
	
	public SetmealServiceImpl(SetmealMapper setmealMapper,
	                          DishMapper dishMapper,
	                          SetmealDishMapper setmealDishMapper,
	                          CategoryMapper categoryMapper) {
		this.setmealMapper = setmealMapper;
		this.dishMapper = dishMapper;
		this.setmealDishMapper = setmealDishMapper;
		this.categoryMapper = categoryMapper;
	}
	
	/**
	 * 分页查询套餐
	 *
	 * @param setmealPageQueryDTO 分页参数
	 * @return 分页结果
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
	 * @param id     套餐id
	 * @param status 修改成的状态
	 * @return 是否修改
	 */
	@Override
	public boolean modifyStatus(Long id, Integer status) {
		Setmeal setmeal = Setmeal.builder().status(status).id(id).build();
		if (Objects.equals(status, StatusConstant.DISABLE)) {
			setmealMapper.update(setmeal);
		} else if (Objects.equals(status, StatusConstant.ENABLE)) {
			// 起售之前查看是否有对应的菜品是禁售状态
			List<SetmealDish> dishes = setmealDishMapper.queryBySetmealId(id);
			if (dishes == null || dishes.isEmpty()) {
				return false;
			}
			
			for (SetmealDish dish : dishes) {
				Long dishId = dish.getDishId();
				Dish tmpDish = dishMapper.queryById(dishId);
				if (Objects.equals(tmpDish.getStatus(), StatusConstant.DISABLE)) {
					return false;
				}
			}
			setmealMapper.update(setmeal);
		}
		return true;
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids 要修改的id集合
	 */
	@Override
	public void deleteSetmeal(List<Integer> ids) {
		setmealMapper.delete(ids);
	}
	
	/**
	 * 根据id查询套餐
	 *
	 * @param id 套餐id
	 * @return 查询到的id
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
	 * @param setmealDTO 新增套餐参数
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
		setmeal.setStatus(StatusConstant.DISABLE);
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
	 * @param setmealDTO 修改参数
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
	 * @param categoryId 要查询的category id
	 * @return 查询结果
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
