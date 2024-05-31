package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.BusinessDataService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/workspace")
@Api(tags = "工作台接口")
public class WorkSpaceController {
	@Autowired
	private BusinessDataService businessDataService;
	
	/**
	 * 工作台信息
	 *
	 * @return : 统一返回结果
	 */
	@GetMapping("/businessData")
	@ApiOperation("工作台信息")
	public Result<BusinessDataVO> businnessData() {
		log.info("工作台信息");
		BusinessDataVO businessDataVO = businessDataService.getData();
		
		return Result.success(businessDataVO);
	}
	
	/**
	 * 订单信息
	 *
	 * @return :统一返回结果
	 */
	@GetMapping("/overviewOrders")
	@ApiOperation("订单信息")
	public Result<OrderOverViewVO> overviewOrders() {
		log.info("订单信息");
		OrderOverViewVO orderOverViewVO = businessDataService.getOrders();
		
		return Result.success(orderOverViewVO);
	}
	
	/**
	 * 菜品套餐统计
	 *
	 * @return : 统一返回结果
	 */
	@GetMapping("/overviewDishes")
	@ApiOperation("菜品套餐统计")
	public Result<DishOverViewVO> overviewDishes() {
		log.info("菜品套餐统计");
		DishOverViewVO dishOverViewVO = businessDataService.getDishes();
		
		return Result.success(dishOverViewVO);
	}
	
	/**
	 * 套餐统计
	 *
	 * @return : 统一返回结果
	 */
	@GetMapping("/overviewSetmeals")
	@ApiOperation("套餐统计")
	public Result<SetmealOverViewVO> overviewSetmeals() {
		log.info("套餐统计");
		SetmealOverViewVO setmealOverViewVO = businessDataService.getSetmeals();
		
		return Result.success(setmealOverViewVO);
	}
}
