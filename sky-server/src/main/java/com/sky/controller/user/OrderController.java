package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "订单接口")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	/**
	 * 用户下单接口
	 *
	 * @param ordersSubmitDTO: 下单参数
	 * @return : 同意返回结果
	 */
	@PostMapping("/submit")
	@ApiOperation("用户下单")
	public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
		log.info("用户下单参数 ordersSubmitDTO:{}", ordersSubmitDTO);
		OrderSubmitVO result = orderService.ordersSubmit(ordersSubmitDTO);
		
		return Result.success(result);
	}
	
	/**
	 * 订单支付
	 *
	 * @param ordersPaymentDTO : 支付参数
	 * @return : 同意返回结果
	 */
	@PutMapping("/payment")
	@ApiOperation("订单支付")
	public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
		log.info("订单支付：{}", ordersPaymentDTO);
		OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
		log.info("生成预支付交易单：{}", orderPaymentVO);
		return Result.success(orderPaymentVO);
	}
	
	/**
	 * 查看历史订单
	 *
	 * @param ordersPageQueryDTO : 查询参数
	 * @return : 同意返回结果
	 */
	@GetMapping("/historyOrders")
	@ApiOperation("历史订单查询")
	public Result<PageResult> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
		log.info("历史订单查询");
		PageResult page = orderService.pageQuery(ordersPageQueryDTO);
		
		return Result.success(page);
	}
	
	/**
	 * 查询订单详情
	 *
	 * @param id : 订单id
	 * @return : 统一返回结果
	 */
	@GetMapping("/orderDetail/{id}")
	@ApiOperation("查询订单详情")
	public Result<OrderVO> queryById(@PathVariable Long id) {
		log.info("查询订单详情参数 id:{}", id);
		OrderVO vo = orderService.orderDetail(id);
		
		return Result.success(vo);
	}
	
	/**
	 * 取消订单
	 *
	 * @param id : 要取消订单的id
	 * @return : 统一返回结果
	 */
	@PutMapping("/cancel/{id}")
	@ApiOperation("取消订单")
	public Result<Object> cancelOrder(@PathVariable Long id) {
		log.info("取消订单参数 id:{}", id);
		orderService.cancelOrder(id);
		
		return Result.success();
	}
	
	/**
	 * 再来一单
	 *
	 * @param id : 要再来一单的单号id
	 * @return : 统一返回结果
	 */
	@PostMapping("/repetition/{id}")
	@ApiOperation("再来一单")
	public Result<Object> repetitionOrder(@PathVariable Long id) {
		log.info("再来一单参数 id:{}", id);
		orderService.repetition(id);
		
		return Result.success();
	}
}
