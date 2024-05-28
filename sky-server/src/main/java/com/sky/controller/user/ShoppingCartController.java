package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user/shoppingCart")
@Api(tags = "购物车相关接口")
public class ShoppingCartController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	/**
	 * 查询购物车
	 */
	@GetMapping("/list")
	@ApiOperation("查询购物车")
	public Result<List<ShoppingCart>> list() {
		log.info("查询购物车");
		List<ShoppingCart> cart = shoppingCartService.list();
		
		return Result.success(cart);
	}
	
	/**
	 * 添加购物车
	 *
	 * @param shoppingCartDTO : 添加参数
	 * @return : 统一结果
	 */
	@PostMapping("/add")
	@ApiOperation("添加购物车")
	public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
		log.info("添加购物车参数 shoppingCartDTO:{}", shoppingCartDTO);
		shoppingCartService.add(shoppingCartDTO);
		
		return Result.success();
	}
	
	/**
	 * 删除购物车
	 *
	 * @param shoppingCartDTO : 删除参数
	 * @return : 统一结果
	 */
	@PostMapping("/sub")
	@ApiOperation("删除购物车中一个商品")
	public Result deleteShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
		log.info("删除购物车参数 shoppingCartDTO:{}", shoppingCartDTO);
		shoppingCartService.delete(shoppingCartDTO);
		
		return Result.success();
	}
	
	/**
	 * 清空购物车
	 *
	 * @return : 统一结果
	 */
	@DeleteMapping("/clean")
	@ApiOperation("清空购物车")
	public Result cleanShoppingCart() {
		log.info("清空购物车");
		shoppingCartService.clean();
		
		return Result.success();
	}
}
