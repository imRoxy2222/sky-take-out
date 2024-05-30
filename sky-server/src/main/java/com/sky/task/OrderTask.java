package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
	@Autowired
	private OrderMapper orderMapper;
	
	/**
	 * 处理超时订单
	 */
	@Scheduled(cron = "0 * * * * ?") // 每分钟触发一次
	public void processTimeoutOrder() {
		log.info("处理超时订单");
		
		LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
		List<Orders> ordersList = orderMapper.queryByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
		
		if (ordersList == null || ordersList.isEmpty()) {
			return;
		}
		
		for (Orders order : ordersList) {
			order.setStatus(Orders.CANCELLED);
			order.setCancelReason("订单超时,自动取消");
			order.setCancelTime(LocalDateTime.now());
			orderMapper.update(order);
		}
	}
	
	/**
	 * 处理配送状态
	 */
	@Scheduled(cron = "0 0 1 * * ? ") // 每天早晨1点触发
	public void processDelivery() {
		log.info("处理配送状态");
		LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
		List<Orders> orders = orderMapper.queryByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
		
		if (orders == null || orders.isEmpty()) {
			return;
		}
		
		for (Orders order : orders) {
			order.setStatus(Orders.COMPLETED);
			order.setCancelReason("配送时间已达到,自动完成");
			order.setCancelTime(LocalDateTime.now());
			orderMapper.update(order);
		}
	}
}
