package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
	/**
	 * 查询购物车
	 *
	 * @return : 购物车内的物品信息
	 */
	List<ShoppingCart> list();
	
	/**
	 * 添加购物车
	 *
	 * @param shoppingCartDTO : 添加参数
	 */
	void add(ShoppingCartDTO shoppingCartDTO);
	
	/**
	 * 删除购物车
	 *
	 * @param shoppingCartDTO : 删除参数
	 */
	void delete(ShoppingCartDTO shoppingCartDTO);
	
	/**
	 * 清空购物车
	 */
	void clean();
}
