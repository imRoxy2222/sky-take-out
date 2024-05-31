package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
	/**
	 * 营业额统计
	 *
	 * @param begin : 统计的开始日期
	 * @param end   : 统计的结束日期(包含)
	 * @return : 查询结果
	 */
	TurnoverReportVO turnoverStatistic(LocalDate begin, LocalDate end);
	
	/**
	 * 查询销量排名top10
	 *
	 * @param begin : 开始日期
	 * @param end   : 结束日期
	 * @return : 查询结果
	 */
	SalesTop10ReportVO top10(LocalDate begin, LocalDate end);
	
	/**
	 * 用户统计
	 *
	 * @param begin : 开始时间
	 * @param end   : 结束时间
	 * @return : 查询结果
	 */
	UserReportVO userStatistics(LocalDate begin, LocalDate end);
	
	/**
	 * 订单统计
	 *
	 * @param begin : 开始时间
	 * @param end   : 结束时间
	 * @return : 查询结果
	 */
	OrderReportVO ordersStatistic(LocalDate begin, LocalDate end);
}
