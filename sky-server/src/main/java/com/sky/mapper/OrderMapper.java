package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrderStatistic;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
	
	/**
	 * 查询所有订单
	 *
	 * @return : 所有订单
	 */
	@Select("select * from orders")
	List<Orders> queryAll();
	
	/**
	 * 修改订单状态通过id
	 *
	 * @param id : 订单id
	 */
	@Update("update orders set status = #{status} where id = #{id}")
	void updateStatusById(Long id, Integer status);
	
	/**
	 * 根据状态查询order
	 *
	 * @param status : 要查询的状态
	 * @return : order 集合
	 */
	@Select("select * from orders where status = #{status} and order_time < #{time}")
	List<Orders> queryByStatusAndOrderTimeLT(Integer status, LocalDateTime time);
	
	/**
	 * 统计一段时间内的金额
	 *
	 * @param begin : 开始时间
	 * @param end   : 结束时间(包含)
	 * @return : 每一天的金额集合
	 */
	List<BigDecimal> turnoverStatistic(LocalDate begin, LocalDate end);
	
	/**
	 * 统计某一天的营业额
	 *
	 * @param begin : 统计营业额的天数
	 * @return : 营业额
	 */
	@Select("select sum(amount) from orders where date(checkout_time) = #{begin} and status = 5 group by date(checkout_time)")
	BigDecimal queryTurnoverByCheckoutTime(LocalDate begin);
	
	/**
	 * 查询一段时间内成功完成订单的订单id
	 *
	 * @param begin : 开始时间
	 * @param end   : 结束时间
	 * @return : 订单ids
	 */
	List<Long> queryIdByTime(LocalDate begin, LocalDate end);
	
	/**
	 * 查询一段时间内的所有订单
	 *
	 * @param begin :开始时间
	 * @param end   :结束时间
	 * @return : orders
	 */
	List<OrderStatistic> queryOrderByTime(LocalDate begin, LocalDate end);
	
	/**
	 * 获取订单总数
	 *
	 * @param status : 订单状态
	 * @return : 符合状态的订单总数
	 */
	Integer getOrderTotal(Integer status);
	
	/**
	 * 返回当天某状态的订单数量
	 *
	 * @param time    : 第几天
	 * @param status: 订单状态
	 * @return : 当前某状态订单数量
	 */
	Integer queryTotalForTime(LocalDate time, Integer status);
	
	/**
	 * 查询某一天的所有订单
	 *
	 * @param time : 时间
	 * @return : 当天所有订单
	 */
	@Select("select * from orders where date(checkout_time) = #{time}")
	List<Orders> queryAllByTime(LocalDate time);
}
