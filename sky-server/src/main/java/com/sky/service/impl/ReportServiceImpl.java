package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrderStatistic;
import com.sky.dto.UserStatisticDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
	private final OrderMapper orderMapper;
	private final OrderDetailMapper orderDetailMapper;
	private final UserMapper userMapper;
	
	public ReportServiceImpl(OrderMapper orderMapper,
	                         OrderDetailMapper orderDetailMapper,
	                         UserMapper userMapper) {
		this.orderMapper = orderMapper;
		this.orderDetailMapper = orderDetailMapper;
		this.userMapper = userMapper;
	}
	
	/**
	 * 营业额统计
	 *
	 * @param begin : 统计的开始日期
	 * @param end   : 统计的结束日期(包含)
	 * @return : 查询结果
	 */
	@Override
	public TurnoverReportVO turnoverStatistic(LocalDate begin, LocalDate end) {
		if (begin.isAfter(end)) {
			throw new RuntimeException("起始时间大于结束时间");
		}
		
		List<LocalDate> dateList = new ArrayList<>();
		List<BigDecimal> turnovers = new ArrayList<>();
		end = end.plusDays(1); // 加一天,这样才能算到结束这天的金额
		while (begin.isBefore(end)) {
			BigDecimal turnover = orderMapper.queryTurnoverByCheckoutTime(begin);
			dateList.add(begin);
			if (turnover == null) {
				turnovers.add(BigDecimal.ZERO);
			} else {
				turnovers.add(turnover);
			}
			begin = begin.plusDays(1);
		}
		
		
		return TurnoverReportVO.builder().dateList(StringUtils.join(dateList, ",")).turnoverList(StringUtils.join(turnovers, ",")).build();
	}
	
	/**
	 * 查询销量排名top10
	 *
	 * @param begin : 开始日期
	 * @param end   : 结束日期
	 * @return : 查询结果
	 */
	@Override
	public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
		// 先查询这段时间内的有效订单id
		List<Long> orderIds = orderMapper.queryIdByTime(begin, end);
		if (orderIds == null || orderIds.isEmpty()) {
			return null;
		}
		// 再查询这段时间内的销售top10
		List<GoodsSalesDTO> goodsSalesDTOS = orderDetailMapper.queryTopNumber(10, begin, end, orderIds);
		
		StringBuilder nameList = new StringBuilder();
		StringBuilder numberList = new StringBuilder();
		for (GoodsSalesDTO goods : goodsSalesDTOS) {
			nameList.append(goods.getName()).append(",");
			numberList.append(goods.getNumber()).append(",");
		}
		
		nameList.deleteCharAt(nameList.length() - 1);
		numberList.deleteCharAt(numberList.length() - 1);
		return new SalesTop10ReportVO(nameList.toString(), numberList.toString());
	}
	
	/**
	 * 用户统计
	 *
	 * @param begin : 开始时间
	 * @param end   : 结束时间
	 * @return : 查询结果
	 */
	@Override
	public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
		// 总数列表
		Integer total = userMapper.queryUserTotalByTimeLT(begin);
		
		List<UserStatisticDTO> userStatisticDTOS = userMapper.queryByTime(begin.plusDays(-1), end);
		
		Map<LocalDate, Integer> collect = userStatisticDTOS.stream().collect(Collectors.toMap(UserStatisticDTO::getCreateTime, UserStatisticDTO::getCreateNumber));
		
		List<Integer> newUserList = new ArrayList<>(); // 新增用户列表
		List<Integer> totalUserList = new ArrayList<>(); // 用户总数列别
		List<LocalDate> dateList = new ArrayList<>(); // 日期列表
		end = end.plusDays(1);
		while (begin.isBefore(end)) {
			dateList.add(begin);
			if (collect.containsKey(begin)) {
				Integer userSum = collect.get(begin);
				newUserList.add(userSum);
			} else {
				newUserList.add(0);
			}
			begin = begin.plusDays(1);
		}
		
		totalUserList.add(0, total + newUserList.get(0));
		for (int i = 1; i < newUserList.size(); i++) {
			totalUserList.add(i, totalUserList.get(i - 1) + newUserList.get(i));
		}
		
		return UserReportVO.builder()
				.dateList(StringUtils.join(dateList, ","))
				.totalUserList(StringUtils.join(totalUserList, ","))
				.newUserList(StringUtils.join(newUserList, ","))
				.build();
	}
	
	/**
	 * 订单统计
	 *
	 * @param begin : 开始时间
	 * @param end   : 结束时间
	 * @return : 查询结果
	 */
	@Override
	public OrderReportVO ordersStatistic(LocalDate begin, LocalDate end) {
		// time orderNumber validNumber 列
		// 这段时间内的所有订单
		List<OrderStatistic> allOrder = orderMapper.queryOrderByTime(begin, end);
		
		// dateList string日期
		List<LocalDate> dateList = new ArrayList<>();
		dateList.add(begin);
		LocalDate tmp = begin.plusDays(1);
		do {
			dateList.add(tmp);
			tmp = tmp.plusDays(1);
		} while (tmp.isBefore(end));
		dateList.add(tmp);
		
		// validOrderCountList string 有效订单数列表
		List<Integer> validOrderCountList = new ArrayList<>();
		
		// orderCountList string 订单数列表
		List<Integer> orderCountList = new ArrayList<>();
		for (LocalDate localDate : dateList) {
			boolean flag = true;
			for (OrderStatistic orderStatistic : allOrder) {
				if (orderStatistic.getTime().equals(localDate)) {
					orderCountList.add(orderStatistic.getOrderNumber());
					validOrderCountList.add(orderStatistic.getValidNumber());
					flag = false;
				}
			}
			if (flag) {
				orderCountList.add(0);
				validOrderCountList.add(0);
			}
		}
		
		// totalOrderCount integer 订单总数
		Integer totalOrderCount = orderMapper.getOrderTotal(null, begin, end);
		
		// validOrderCount integer 有效订单数
		Integer validOrderCount = orderMapper.getOrderTotal(Orders.COMPLETED, begin, end);
		
		
		// orderCompletionRate number 订单完成率
		double orderCompletionRate = 1;
		if (totalOrderCount != 0 && validOrderCount != 0) {
			orderCompletionRate = validOrderCount * 1.0 / totalOrderCount;
		}
		
		return OrderReportVO.builder()
				.dateList(StringUtils.join(dateList, ","))
				.orderCompletionRate(orderCompletionRate)
				.orderCountList(StringUtils.join(orderCountList, ","))
				.totalOrderCount(totalOrderCount)
				.validOrderCount(validOrderCount)
				.validOrderCountList(StringUtils.join(validOrderCountList, ","))
				.build();
	}
}
