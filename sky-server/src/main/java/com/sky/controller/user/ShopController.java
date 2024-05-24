package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShopController")
@Slf4j
@Api(tags = "用户端商铺状状态接口")
@RequestMapping("/user/shop")
public class ShopController {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	private static final String SHOP_STATUS = "SHOP_STATUS";
	
	/**
	 * 获取商店状态
	 *
	 * @return : 商店状态
	 */
	@GetMapping("/status")
	@ApiOperation("获取商店状态")
	public Result<Integer> getStatus() {
		Integer shopStatus = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS);
		log.info("用户端获取商店状态 {}", shopStatus);
		return Result.success(shopStatus == null ? StatusConstant.DISABLE : shopStatus);
	}
}
