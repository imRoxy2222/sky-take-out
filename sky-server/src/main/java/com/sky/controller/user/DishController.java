package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController("userDishController")
@Slf4j
@Api(tags = "用户菜品接口")
@RequestMapping("/user/dish")
public class DishController {
	@Autowired
	private DishService dishService;
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 根据分类id查询菜品
	 *
	 * @param categoryId: 分类id
	 * @return :前端菜品VO
	 */
	@ApiOperation("根据分类id查询菜品")
	@GetMapping("/list")
	public Result<List<DishVO>> list(@NotNull Integer categoryId) {
		// 构建redis key
		String key = "dish_" + categoryId;
		
		// 查询redis
		List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
		
		if (list == null || list.isEmpty()) {
			// redis 无缓存,查询数据库
			list = dishService.queryByCategoryId(categoryId);
			redisTemplate.opsForValue().set(key, list);
		}
		
		return Result.success(list);
	}
}
