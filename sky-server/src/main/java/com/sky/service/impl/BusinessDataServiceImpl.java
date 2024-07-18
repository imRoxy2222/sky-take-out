package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.dto.UserStatisticDTO;
import com.sky.entity.Dish;
import com.sky.entity.Orders;
import com.sky.entity.Setmeal;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.BusinessDataService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.sky.entity.Orders.COMPLETED;

@Service
public class BusinessDataServiceImpl implements BusinessDataService {
	private final UserMapper userMapper;
	private final OrderMapper orderMapper;
	private final DishMapper dishMapper;
	private final SetmealMapper setmealMapper;
	
	public BusinessDataServiceImpl(UserMapper userMapper,
	                               OrderMapper orderMapper,
	                               DishMapper dishMapper,
	                               SetmealMapper setmealMapper) {
		this.userMapper = userMapper;
		this.orderMapper = orderMapper;
		this.dishMapper = dishMapper;
		this.setmealMapper = setmealMapper;
	}
	
	@Override
	public BusinessDataVO getData() {
		// 新增用户数
		Integer newUsers = 0;
		List<UserStatisticDTO> userStatisticDTOS = userMapper.queryByTime(LocalDate.MIN, LocalDate.MAX);
		for (UserStatisticDTO dto : userStatisticDTOS) {
			if (dto.getCreateTime().equals(LocalDate.now())) {
				newUsers = dto.getCreateNumber();
				break;
			}
		}
		
		// 订单完成率
		Integer orderTotal = orderMapper.queryTotalForTime(LocalDate.now(), null); // 当前订单总数
		Integer orderComplete = orderMapper.queryTotalForTime(LocalDate.now(), COMPLETED);// 完成了的订单
		
		double orderCompletionRate = 1.0;
		if (orderTotal != 0 && orderComplete != 0) {
			orderCompletionRate = orderComplete * 1.0 / orderTotal; // 订单完完成率
		}
		
		// 营业额
		BigDecimal sum = orderMapper.queryTurnoverByCheckoutTime(LocalDate.now());
		Double turnover = sum == null ? 0 : sum.doubleValue();
		
		// 平均客单价
		double unitPrice = 0.0;
		if (orderTotal != 0) {
			unitPrice = turnover / orderTotal;
		}
		
		// 有效订单数
		Integer validOrderCount = Math.toIntExact(orderComplete);
		
		return BusinessDataVO.builder()
				.newUsers(newUsers)
				.orderCompletionRate(orderCompletionRate)
				.turnover(turnover)
				.unitPrice(unitPrice)
				.validOrderCount(validOrderCount)
				.build();
	}
	
	@Override
	public OrderOverViewVO getOrders() {
		// 当前所有订单信息
		List<Orders> orders = orderMapper.queryAllByTime(LocalDate.now());
		
		int allOrders, cancelledOrders, completedOrders, deliveredOrders, waitingOrders;
		allOrders = cancelledOrders = completedOrders = deliveredOrders = waitingOrders = 0;
		if (orders != null && !orders.isEmpty()) {
			
			// 全部订单
			allOrders = orders.size();
			for (Orders order : orders) {
				switch (order.getStatus()) {
					case 6:
						cancelledOrders += 1;
						break; // 已取消
					case 5:
						completedOrders += 1;
						break; // 已完成
					case 3:
						deliveredOrders += 1;
						break; // 待派送
					case 2:
						waitingOrders += 1;
						break; // 待接单
				}
			}
		}
		return OrderOverViewVO.builder()
				.allOrders(allOrders)
				.cancelledOrders(cancelledOrders)
				.completedOrders(completedOrders)
				.deliveredOrders(deliveredOrders)
				.waitingOrders(waitingOrders)
				.build();
	}
	
	@Override
	public DishOverViewVO getDishes() {
		List<Dish> dish = dishMapper.queryAll();
		int discontinued = 0, sold = 0;
		if (dish != null && !dish.isEmpty()) {
			for (Dish d : dish) {
				if (Objects.equals(d.getStatus(), StatusConstant.ENABLE)) {
					sold += 1;
				} else if (Objects.equals(d.getStatus(), StatusConstant.DISABLE)) {
					discontinued += 1;
				}
			}
		}
		
		return new DishOverViewVO(sold, discontinued);
	}
	
	@Override
	public SetmealOverViewVO getSetmeals() {
		List<Setmeal> setmeals = setmealMapper.queryAll();
		int discontinued = 0, sold = 0;
		if (setmeals != null && !setmeals.isEmpty()) {
			for (Setmeal setmeal : setmeals) {
				if (Objects.equals(setmeal.getStatus(), StatusConstant.ENABLE)) {
					sold += 1;
				} else if (Objects.equals(setmeal.getStatus(), StatusConstant.DISABLE)) {
					discontinued += 1;
				}
			}
		}
		
		return new SetmealOverViewVO(sold, discontinued);
	}
}
