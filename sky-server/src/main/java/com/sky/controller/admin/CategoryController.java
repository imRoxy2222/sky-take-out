package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "菜品相关接口")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
	/**
	 * 修改菜品分类接口
	 *
	 * @param categoryDTO: 修改参数
	 * @return : null
	 */
	@PutMapping
	@ApiOperation("修改菜品分类")
	public Result<Object> modifyCategory(@RequestBody CategoryDTO categoryDTO) {
		log.info("修改菜品分页接口参数 categoryDTO:{}", categoryDTO);
		categoryService.modifyCategory(categoryDTO);
		
		return Result.success();
	}
	
	/**
	 * 查看根据type查看所有列表
	 *
	 * @param type : 菜品种类
	 * @return : 当前种类所有菜品
	 */
	@GetMapping("/list")
	@ApiOperation("显示所有菜品")
	public Result<List<Category>> list(Long type) {
		log.info("显示所有菜品接口参数 type:{}", type);
		List<Category> categorys = categoryService.list(type);
		
		return Result.success(categorys);
	}
	
	/**
	 * 分页查询菜品
	 *
	 * @param categoryPageQueryDTO: 查询参数
	 * @return : 分页结果
	 */
	@GetMapping("/page")
	@ApiOperation("分页查询菜品")
	public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
		log.info("分页查询菜品参数 categoryPageQueryDTO:{}", categoryPageQueryDTO);
		PageResult page = categoryService.page(categoryPageQueryDTO);
		
		return Result.success(page);
	}
	
	/**
	 * 启用/禁用分类
	 *
	 * @param status : 修改成的状态
	 * @param id     : 要修改的id
	 * @return : null
	 */
	@PostMapping("/status/{status}")
	@ApiOperation("启用/禁用分类")
	public Result<Object> modifyStatus(@PathVariable Integer status, Long id) {
		log.info("启用/禁用分类参数 id:{} status:{}", id, status);
		categoryService.modifyStatus(status, id);
		
		return Result.success();
	}
	
	/**
	 * 新增分类
	 *
	 * @param categoryDTO: 新增菜品参数
	 * @return : null
	 */
	@PostMapping
	@ApiOperation("新增分类")
	public Result<Object> addCategory(@RequestBody CategoryDTO categoryDTO) {
		log.info("新增分类参数 categoryDTO:{}", categoryDTO);
		categoryService.addCategory(categoryDTO);
		
		return Result.success();
	}
	
	/**
	 * 根据id删除分类
	 *
	 * @param id: 要删除的id
	 * @return : null
	 */
	@DeleteMapping
	@ApiOperation("根据id删除分类")
	public Result<Object> deleteCategory(Integer id) {
		log.info("根据id删除分类参数 id:{}", id);
		categoryService.deleteById(id);
		
		return Result.success();
	}
}
