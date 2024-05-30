package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
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
	 * 取消订单
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
}
