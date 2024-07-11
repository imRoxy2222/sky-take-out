package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单接口")
public class OrderController {
	private final OrderService orderService;
	
	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	/**
	 * 取消订单
	 *
	 * @param ordersCancelDTO:取消订单参数
	 * @return : 统一返回结果
	 */
	@PutMapping("/cancel")
	@ApiOperation("取消订单")
	public Result<Object> cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO) {
		log.info("取消订单参数 ordersCancelDTO:{}", ordersCancelDTO);
		
		orderService.cancel(ordersCancelDTO);
		return Result.success();
	}
	
	/**
	 * 订单搜索
	 *
	 * @param ordersPageQueryDTO : 搜索条件
	 * @return : 查询结果
	 */
	@GetMapping("/conditionSearch")
	@ApiOperation("订单搜索")
	public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
		log.info("订单搜索参数 ordersPageQueryDTO:{}", ordersPageQueryDTO);
		PageResult pageResult = orderService.pageQuery(ordersPageQueryDTO);
		
		return Result.success(pageResult);
	}
	
	/**
	 * 接单
	 *
	 * @param ordersConfirmDTO : 接单参数
	 * @return : 统一返回结果
	 */
	@PutMapping("/confirm")
	@ApiOperation("接单")
	public Result<Object> confirmOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
		log.info("接单参数 ordersConfirmDTO:{}", ordersConfirmDTO);
		orderService.confirmOrder(ordersConfirmDTO);
		
		return Result.success();
	}
	
	/**
	 * 查询各个状态的订单数量
	 *
	 * @return : 查询结果
	 */
	@GetMapping("/statistics")
	@ApiOperation("查询各个状态的订单数量")
	public Result<OrderStatisticsVO> statistics() {
		log.info("查询各个状态的订单数量");
		OrderStatisticsVO orderStatisticsVO = orderService.statistics();
		
		return Result.success(orderStatisticsVO);
	}
	
	/**
	 * 完成订单
	 *
	 * @param id: 订单id
	 * @return : 统一返回结果
	 */
	@PutMapping("/complete/{id}")
	@ApiOperation("完成订单")
	public Result<Object> complete(@PathVariable Long id) {
		log.info("完成订单参数 id:{}", id);
		orderService.complete(id);
		
		return Result.success();
	}
	
	/**
	 * 拒单
	 *
	 * @param ordersRejectionDTO : 拒单参数
	 * @return : 统一返回结果
	 */
	@PutMapping("/rejection")
	@ApiOperation("拒单")
	public Result<Object> rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
		log.info("拒单参数 ordersRejectionDTO:{}", ordersRejectionDTO);
		orderService.rejection(ordersRejectionDTO);
		
		return Result.success();
	}
	
	/**
	 * 订单派送
	 *
	 * @param id : 派送订单的id
	 * @return : 同意返回结果
	 */
	@PutMapping("/delivery/{id}")
	@ApiOperation("订单派送")
	public Result<Object> delivery(@PathVariable Long id) {
		log.info("订单派送参数 id:{}", id);
		orderService.deliveryOrder(id);
		
		return Result.success();
	}
	
	/**
	 * 查询订单详情
	 *
	 * @param id : 查询订单id
	 * @return : 详情结果
	 */
	@GetMapping("/details/{id}")
	@ApiOperation("查询订单详情")
	public Result<OrderVO> details(@PathVariable Long id) {
		log.info("查询订单详情参数 id:{}", id);
		OrderVO vo = orderService.orderDetail(id);
		
		return Result.success(vo);
	}
}
