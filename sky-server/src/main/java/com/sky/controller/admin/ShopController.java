package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Slf4j
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "管理员端营业状态")
public class ShopController {
	
	private final RedisTemplate<String, Object> redisTemplate;
	private static final String SHOP_STATUS = "SHOP_STATUS";
	
	public ShopController(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	/**
	 * 获取营业状态
	 *
	 * @return : 营业状态Result
	 */
	@GetMapping("/status")
	@ApiOperation("获取营业状态")
	public Result<Integer> getStatus() {
		Integer shopStatus = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS);
		log.info("获取营业状态为: {}", shopStatus);
		return Result.success(shopStatus == null ? StatusConstant.DISABLE : shopStatus);
	}
	
	/**
	 * 设置营业状态
	 *
	 * @param status : 要设置的状态
	 * @return : 操作是否成功
	 */
	@PutMapping("/{status}")
	@ApiOperation("设置营业状态")
	public Result<String> setStatus(@PathVariable @NotNull Integer status) {
		log.info("设置营业状态为: {}", status);
		if (status.equals(StatusConstant.DISABLE)) {
			redisTemplate.opsForValue().set(SHOP_STATUS, StatusConstant.DISABLE);
		} else if (status.equals(StatusConstant.ENABLE)) {
			redisTemplate.opsForValue().set(SHOP_STATUS, StatusConstant.ENABLE);
		} else {
			return Result.error(MessageConstant.STATUS_UNKNOW);
		}
		return Result.success();
	}
}
