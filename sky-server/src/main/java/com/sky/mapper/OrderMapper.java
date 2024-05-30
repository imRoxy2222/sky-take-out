package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {
	void insert(Orders insertParam);
	
	/**
	 * 根据订单号查询订单
	 *
	 * @param orderNumber : order订单号
	 */
	@Select("select * from orders where number = #{orderNumber}")
	Orders getByNumber(String orderNumber);
	
	/**
	 * 修改订单信息
	 *
	 * @param orders : 要修改成的orders
	 */
	void update(Orders orders);
	
	/**
	 * 分页查询详情
	 *
	 * @param ordersPageQueryDTO : 查询参数
	 * @return : 查询结果
	 */
	Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);
	
	/**
	 * 根据id查询order
	 *
	 * @param id : order id
	 * @return : orders
	 */
	@Select("select * from orders where id = #{id}")
	Orders queryById(Long id);
	
	/**
	 * 根据id获得order
	 *
	 * @param id : order id
	 * @return : order entity
	 */
	@Select("select * from orders where id = #{id}")
	Orders getById(Long id);
}
