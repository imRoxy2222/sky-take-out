package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.SetmealDishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmeanlController")
@Slf4j
@RequestMapping("/user/setmeal")
@Api(tags = "套餐相关接口")
public class SetmeanlController {
	@Autowired
	private SetmealService setmealService;
	@Autowired
	private SetmealDishService setmealDishService;
	
	/**
	 * 根据分类id查询数据
	 *
	 * @param categoryId : 分类id
	 * @return List<SetmealVO>
	 */
	@ApiOperation("list")
	@GetMapping("/list")
	@Cacheable(cacheNames = "setmealCache", key = "#categoryId")
	public Result<List<SetmealVO>> list(Integer categoryId) {
		List<SetmealVO> setmeals = setmealService.queryByCategoryId(categoryId);
		return Result.success(setmeals);
	}
	
	/**
	 * 根据套餐id查询包含的菜品
	 *
	 * @param id: 套餐的id
	 * @return 菜品
	 */
	@GetMapping("/dish/{id}")
	@ApiOperation("根据套餐id查询包含的菜品")
	public Result<List<DishItemVO>> queryDishBySetmealId(@PathVariable Long id) {
		List<DishItemVO> list = setmealDishService.queryDish(id);
		
		return Result.success(list);
	}
}
