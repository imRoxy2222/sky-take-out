package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
public class SetmealController {
	
	@Autowired
	private SetmealService setmealService;
	
	/**
	 * 分页查询
	 *
	 * @param setmealPageQueryDTO
	 * @return
	 */
	@GetMapping("/page")
	@ApiOperation("分页查询")
	public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
		log.info("套餐分页查询参数 setmealPageQueryDTO:{}", setmealPageQueryDTO);
		PageResult setmeal = setmealService.pageQuery(setmealPageQueryDTO);
		
		return Result.success(setmeal);
	}
	
	/**
	 * 起售禁售套餐
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	@PostMapping("/status/{status}")
	@ApiOperation("起售禁售接口")
	@CacheEvict(cacheNames = "setmealCache", allEntries = true)
	public Result modifyStatus(Long id, @PathVariable Integer status) {
		log.info("套餐分页查询参数 id:{} status:{}", id, status);
		setmealService.modifyStatus(id, status);
		
		return Result.success();
	}
	
	/**
	 * 批量删除套餐
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping
	@ApiOperation("批量删除套餐")
	@CacheEvict(cacheNames = "setmealCache", allEntries = true)
	public Result deleteSetmeal(@RequestParam List<Integer> ids) {
		log.info("批量删除套餐参数 ids:{}", ids);
		setmealService.deleteSetmeal(ids);
		
		return Result.success();
	}
	
	/**
	 * 根据id查询套餐
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@ApiOperation("根据id查询套餐")
	public Result<SetmealVO> querySetmealById(@PathVariable Long id) {
		log.info("根据id查询套餐参数 id:{}", id);
		SetmealVO setmealVO = setmealService.queryById(id);
		
		return Result.success(setmealVO);
	}
	
	/**
	 * 新增套餐
	 *
	 * @param setmealDTO
	 * @return
	 */
	@PostMapping
	@ApiOperation("新增套餐")
	@CacheEvict(cacheNames = "setmealCache", allEntries = true)
	public Result addSetmeal(@RequestBody SetmealDTO setmealDTO) {
		log.info("新增套餐参数 setmealDTO:{}", setmealDTO);
		setmealService.addSetmeal(setmealDTO);
		
		return Result.success();
	}
	
	/**
	 * 修改套餐
	 *
	 * @param setmealDTO
	 * @return
	 */
	@PutMapping
	@ApiOperation("修改套餐")
	@CacheEvict(cacheNames = "setmealCache", allEntries = true)
	public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO) {
		setmealService.updateSetmeal(setmealDTO);
		
		return Result.success();
	}
}
