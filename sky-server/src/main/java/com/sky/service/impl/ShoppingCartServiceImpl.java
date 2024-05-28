package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.*;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
	@Autowired
	private ShoppingCartMapper shoppingCartMapper;
	@Autowired
	private SetmealMapper setmealMapper;
	@Autowired
	private DishMapper dishMapper;
	@Autowired
	private DishFlavorMapper dishFlavorMapper;
	@Autowired
	private SetmealDishMapper setmealDishMapper;
	
	
	/**
	 * 查询购物车
	 *
	 * @return : 购物车内的物品信息
	 */
	@Override
	public List<ShoppingCart> list() {
		Long currentUserId = BaseContext.getCurrentId();
		ShoppingCart queryParam = ShoppingCart.builder()
				.userId(currentUserId)
				.build();
		List<ShoppingCart> query = shoppingCartMapper.query(queryParam);
		return query;
	}
	
	/**
	 * 添加购物车
	 *
	 * @param shoppingCartDTO : 添加参数
	 */
	@Override
	public void add(ShoppingCartDTO shoppingCartDTO) {
		ShoppingCart shopping = ShoppingCart.builder()
				.dishId(shoppingCartDTO.getDishId())
				.setmealId(shoppingCartDTO.getSetmealId())
				.dishFlavor(shoppingCartDTO.getDishFlavor())
				.userId(BaseContext.getCurrentId())
				.build();
		
		// 先查询有没有这条购物记录
		List<ShoppingCart> query = shoppingCartMapper.query(shopping);
		if (query == null || query.isEmpty()) {
			// 没有插入数据
			// 插入套餐
			if (shopping.getSetmealId() != null) {
				Setmeal setmealQuery = setmealMapper.query(Setmeal.builder().id(shopping.getSetmealId()).build());
				shopping.setImage(setmealQuery.getImage());
				shopping.setName(setmealQuery.getName());
				// 价格
				BigDecimal price = setmealDishMapper.queryPriceBySetmealId(shopping.getSetmealId());
				shopping.setAmount(price);
			} else if (shopping.getDishId() != null) {
				// 插入菜品
				Dish dishQuery = dishMapper.queryById(shopping.getDishId());
				shopping.setImage(dishQuery.getImage());
				shopping.setName(dishQuery.getName());
				shopping.setAmount(dishQuery.getPrice());
			}
			shopping.setNumber(1);
			shopping.setCreateTime(LocalDateTime.now());
			
			shoppingCartMapper.insert(shopping);
		} else {
			// 有的话直接商品数量加1
			shopping.setNumber(query.get(0).getNumber() + 1);
			shoppingCartMapper.update(shopping);
		}
		
	}
	
	/**
	 * 删除购物车
	 *
	 * @param shoppingCartDTO : 删除参数
	 */
	@Override
	public void delete(ShoppingCartDTO shoppingCartDTO) {
		ShoppingCart deleteParam = ShoppingCart.builder()
				.dishFlavor(shoppingCartDTO.getDishFlavor())
				.dishId(shoppingCartDTO.getDishId())
				.setmealId(shoppingCartDTO.getSetmealId())
				.userId(BaseContext.getCurrentId())
				.build();
		// 先查询当前商品的数量,如果数量大于1,就数量-1用update, 如果数量为1,就用delete
		List<ShoppingCart> query = shoppingCartMapper.query(deleteParam);
		if (query == null || query.isEmpty()) {
			return;
		}
		
		ShoppingCart shoppingCart = query.get(0);
		if (shoppingCart.getNumber() > 1) {
			// 当前菜品数量大于1
			shoppingCart.setNumber(shoppingCart.getNumber() - 1);
			shoppingCartMapper.update(shoppingCart);
		} else if (shoppingCart.getNumber() == 1) {
			// 当前菜品数量等于1
			shoppingCartMapper.delete(shoppingCart);
		}
	}
	
	/**
	 * 清空购物车
	 */
	@Override
	public void clean() {
		ShoppingCart deleteParam = ShoppingCart.builder()
				.userId(BaseContext.getCurrentId())
				.build();
		shoppingCartMapper.delete(deleteParam);
	}
}
