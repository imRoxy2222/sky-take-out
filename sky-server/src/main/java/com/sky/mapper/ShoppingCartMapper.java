package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
	
	/**
	 * 查询购物车
	 *
	 * @return : 购物车内的物品信息
	 */
	List<ShoppingCart> query(ShoppingCart queryParam);
	
	/**
	 * 插入一条购物车数据
	 *
	 * @param shopping : shoppingCartEntity
	 */
	void insert(ShoppingCart shopping);
	
	/**
	 * 更新一条购物车数据
	 *
	 * @param shopping : shoppingCartEntity
	 */
	void update(ShoppingCart shopping);
	
	/**
	 * 删除一条shoppingCartEntity
	 *
	 * @param deleteParam : shoppingCartEntity
	 */
	void delete(ShoppingCart deleteParam);
}
