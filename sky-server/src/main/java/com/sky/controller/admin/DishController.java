package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Api("菜品相关接口")
public class DishController {
	
	@Autowired
	private DishService dishService;
	
	/**
	 * 根据分类id查询菜品
	 *
	 * @param categoryId: 分类id
	 * @return : 查询到的菜品数据
	 */
	@GetMapping("/list")
	@ApiOperation("根据分类id查询菜品")
	public Result<List<Dish>> queryByCategoryId(Integer categoryId) {
		log.info("根据分类id查询菜品参数 categoryId:{}", categoryId);
		return Result.success(dishService.queryByCategoryId(categoryId));
	}
	
	/**
	 * 新增菜品
	 *
	 * @param dishDTO: 新增菜品信息
	 *                 s * @return : success
	 */
	@PostMapping
	@ApiOperation("新增菜品")
	public Result addDish(@RequestBody DishDTO dishDTO) {
		log.info("新增菜品参数 dishDto:{}", dishDTO);
		
		dishService.addDish(dishDTO);
		return Result.success();
	}
	
	/**
	 * 分页查询
	 *
	 * @param dishPageQueryDTO: 查询参数
	 * @return : 查询结果
	 */
	@GetMapping("/page")
	@ApiOperation("菜品分页查询")
	public Result<PageResult> queryPage(DishPageQueryDTO dishPageQueryDTO) {
		log.info("菜品分页查询参数 dishPageQueryDTO:{}", dishPageQueryDTO);
		PageResult page = dishService.queryPage(dishPageQueryDTO);
		
		return Result.success(page);
	}
	
	/**
	 * 菜品起售停售
	 *
	 * @param status: 想更改成的状态
	 * @param id:     要修改记录的id
	 * @return : success
	 */
	@PostMapping("/status/{status}")
	@ApiOperation("菜品起售/停售")
	public Result modifyStatus(@PathVariable Integer status, Long id) {
		log.info("菜品起售/停售参数 status:{} id:{}", status, id);
		dishService.modifyStatus(status, id);
		
		return Result.success();
	}
	
	/**
	 * 通过id查询菜品
	 *
	 * @param id: 查询的id
	 * @return : 查询结果
	 */
	@GetMapping("/{id}")
	@ApiOperation("根据id查询菜品")
	public Result<DishVO> queryById(@PathVariable Long id) {
		log.info("根据id查询菜品参数 id:{}", id);
		return Result.success(dishService.queryById(id));
	}
	
	/**
	 * 批量删除菜品
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping
	@ApiOperation("批量删除菜品")
	public Result deleteByIds(@RequestParam List<Integer> ids) {
		dishService.deleteByIds(ids);
		return Result.success();
	}
	
	@PutMapping
	@ApiOperation("修改菜品")
	public Result modifyDish(@RequestBody DishDTO dishDTO) {
		dishService.modify(dishDTO);
		
		return Result.success();
	}
}
