package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DishServiceImpl implements DishService {
	
	@Autowired
	private DishMapper dishMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private DishFlavorMapper dishFlavorMapper;
	
	/**
	 * 新增菜品
	 *
	 * @param dishDTO 添加参数
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void addDish(DishDTO dishDTO) {
		Integer status = dishDTO.getStatus();
		if (!status.equals(StatusConstant.DISABLE) && !status.equals(StatusConstant.ENABLE)) {
			return;
		}
		
		Dish dish = new Dish();
		BeanUtils.copyProperties(dishDTO, dish);
		dish.setCategoryId(Math.toIntExact(dishDTO.getCategoryId()));
		// 先插入dish,然后获取到返回id后插入flavors
		dishMapper.insert(dish);
		Long id = dish.getId();
		for (DishFlavor flavor : dishDTO.getFlavors()) {
			flavor.setDishId(id);
			dishFlavorMapper.insert(flavor);
		}
	}
	
	/**
	 * 根据分类id查询菜品
	 *
	 * @param categoryId : 要查询的分类id
	 * @return 查询结果
	 */
	@Override
	public List<DishVO> queryByCategoryId(Integer categoryId) {
		Dish queryDish = Dish.builder().status(StatusConstant.ENABLE).categoryId(categoryId).build();
		List<Dish> dishes = dishMapper.query(queryDish);
		
		if (dishes == null) {
			return null;
		}
		// 转换为VO对象
		List<DishVO> dishVOList = new ArrayList<>();
		for (Dish dish : dishes) {
			DishVO dishVO = new DishVO();
			BeanUtils.copyProperties(dish, dishVO);
			dishVOList.add(dishVO);
			
			// 查询风味
			Long id = dish.getId();
			List<DishFlavor> flavors = dishFlavorMapper.queryById(id);
			dishVO.setFlavors(flavors);
		}
		return dishVOList;
	}
	
	/**
	 * 分页查询
	 *
	 * @param dishPageQueryDTO : 前端传入的查询参数
	 * @return : PageHelper的PageResult对象
	 */
	@Override
	public PageResult queryPage(DishPageQueryDTO dishPageQueryDTO) {
		PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
		
		Dish dish = new Dish();
		BeanUtils.copyProperties(dishPageQueryDTO, dish);
		
		Page<Dish> page = dishMapper.query(dish);
		
		long total = page.getTotal();
		List<Dish> result = page.getResult();
		
		return new PageResult(total, result);
	}
	
	/**
	 * 菜品起售/停售
	 *
	 * @param status : 想要修改的状态
	 * @param id     : 想要修改记录的id
	 */
	@Override
	public void modifyStatus(Integer status, Long id) {
		if (status.equals(StatusConstant.DISABLE) && status.equals(StatusConstant.ENABLE)) {
			return;
		}
		Dish dish = Dish.builder()
				.status(status)
				.id(id)
				.build();
		dishMapper.update(dish);
	}
	
	/**
	 * 根据id查询菜品
	 *
	 * @param id: 查询菜品的id
	 * @return : 查询到的菜品
	 */
	@Override
	public DishVO queryById(Long id) {
		// 查询到这条id对应的记录
		Dish query = dishMapper.queryById(id);
		
		DishVO dishVO = new DishVO();
		// categoryId不一致,单独复制
		dishVO.setCategoryId(Long.valueOf(query.getCategoryId()));
		BeanUtils.copyProperties(query, dishVO);
		// 获取菜品分类的名称
		String categoryName = categoryMapper.queryByCategoryName(dishVO.getCategoryId());
		dishVO.setCategoryName(categoryName);
		
		List<DishFlavor> flavors = dishFlavorMapper.queryById(id);
		dishVO.setFlavors(flavors);
		
		return dishVO;
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids: 要删除的id
	 */
	@Transactional
	@Override
	public void deleteByIds(List<Integer> ids) {
		for (Integer id : ids) {
			Dish dish = dishMapper.queryById(Long.valueOf(id));
			if (Objects.equals(dish.getStatus(), StatusConstant.DISABLE)) {
				dishFlavorMapper.deleteByDishId(id);
				dishMapper.delete(id);
			}
		}
	}
	
	/**
	 * 修改dish
	 *
	 * @param dishDTO: 前端穿的dish id
	 */
	@Override
	public void modify(DishDTO dishDTO) {
		Dish dish = new Dish();
		BeanUtils.copyProperties(dishDTO, dish);
		dish.setCategoryId(Math.toIntExact(dishDTO.getCategoryId()));
		
		// 删除原先的dish flavour
		dishFlavorMapper.deleteByDishId(Math.toIntExact(dish.getId()));
		
		// 新建dish flavour
		for (DishFlavor flavor : dishDTO.getFlavors()) {
			flavor.setDishId(dish.getId());
			dishFlavorMapper.insert(flavor);
		}
		
		dishMapper.update(dish);
	}
}
