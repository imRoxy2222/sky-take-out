package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Slf4j
@RequestMapping("/admin/report")
@Api(tags = "数据统计接口")
public class ReportController {
	private final ReportService reportService;
	
	@Autowired
	public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}
	
	/**
	 * 营业额统计
	 *
	 * @param begin : 统计的开始日期
	 * @param end   : 统计的结束日期(包含)
	 * @return : 统一返回结果
	 */
	@GetMapping("/turnoverStatistics")
	@ApiOperation("营业额统计")
	public Result<TurnoverReportVO> turnoverStatistic(
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
		log.info("营业额统计 begin:{},end:{}", begin, end);
		TurnoverReportVO turnoverReportVO = reportService.turnoverStatistic(begin, end);
		
		return Result.success(turnoverReportVO);
	}
	
	/**
	 * 查询销量排名top10
	 *
	 * @param begin : 开始日期
	 * @param end   : 结束日期
	 * @return : 查询结果
	 */
	@GetMapping("/top10")
	@ApiOperation("查询销量排名top10")
	public Result<SalesTop10ReportVO> top10(
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
		log.info("查询销量排名top10 begin:{},end:{}", begin, end);
		SalesTop10ReportVO salesTop10ReportVO = reportService.top10(begin, end);
		
		return Result.success(salesTop10ReportVO);
	}
	
	/**
	 * 用户统计
	 *
	 * @param begin : 开始时间
	 * @param end   : 结束时间
	 * @return : 查询结果
	 */
	@GetMapping("/userStatistics")
	@ApiOperation("用户统计")
	public Result<UserReportVO> userStatistics(
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
		log.info("用户统计 begin:{},end:{}", begin, end);
		UserReportVO userReportVO = reportService.userStatistics(begin, end);
		
		return Result.success(userReportVO);
	}
	
	/**
	 * 订单统计
	 *
	 * @param begin : 开始时间
	 * @param end   : 结束时间
	 * @return : 查询结果
	 */
	@GetMapping("/ordersStatistics")
	@ApiOperation("订单统计")
	public Result<OrderReportVO> ordersStatistic(
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
		log.info("订单统计 begin:{},end:{}", begin, end);
		OrderReportVO orderReportVO = reportService.ordersStatistic(begin, end);
		
		
		return Result.success(orderReportVO);
	}
}
