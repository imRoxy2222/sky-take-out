package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
	/**
	 * 订单提交
	 *
	 * @param ordersSubmitDTO : 提交参数
	 * @return : 提交结果
	 */
	OrderSubmitVO ordersSubmit(OrdersSubmitDTO ordersSubmitDTO);
	
	/**
	 * 订单支付
	 *
	 * @param ordersPaymentDTO : 支付信息
	 * @return : 支付结果
	 */
	OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;
	
	/**
	 * 支付成功，修改订单状态
	 *
	 * @param outTradeNo : 订单号
	 */
	void paySuccess(String outTradeNo);
	
	/**
	 * 分页查询历史订单
	 *
	 * @param ordersPageQueryDTO : 查询参数
	 * @return : 查询结果
	 */
	PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);
	
	/**
	 * 查询订单详情
	 *
	 * @param id : 订单id
	 * @return : 订单实体
	 */
	OrderVO orderDetail(Long id);
	
	/**
	 * 用户取消订单
	 *
	 * @param id : 要取消订单的id
	 */
	void cancelOrder(Long id);
	
	/**
	 * 再来一单
	 *
	 * @param id : 再来一单的单号id
	 */
	void repetition(Long id);
	
	/**
	 * 商家取消订单
	 *
	 * @param ordersCancelDTO : 取消参数
	 */
	void cancel(OrdersCancelDTO ordersCancelDTO);
	
	/**
	 * 接单
	 *
	 * @param ordersConfirmDTO : 接单参数
	 */
	void confirmOrder(OrdersConfirmDTO ordersConfirmDTO);
	
	/**
	 * 查询各个状态的订单数量
	 *
	 * @return : 查询结果
	 */
	OrderStatisticsVO statistics();
	
	/**
	 * 完成订单
	 *
	 * @param id: 订单id
	 */
	void complete(Long id);
	
	/**
	 * 拒单
	 *
	 * @param ordersRejectionDTO : 拒单参数
	 */
	void rejection(OrdersRejectionDTO ordersRejectionDTO);
	
	/**
	 * 订单派送
	 *
	 * @param id : 派送订单的id
	 */
	void deliveryOrder(Long id);
	
	/**
	 * 催单
	 *
	 * @param id : 催单订单号
	 */
	void reminder(Long id);
}
