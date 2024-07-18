package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
public class SetmealController {
	
	private final SetmealService setmealService;
	
	public SetmealController(SetmealService setmealService) {
		this.setmealService = setmealService;
	}
	
	/**
	 * 分页查询
	 *
	 * @param setmealPageQueryDTO 查询参数
	 * @return 统一返回结果
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
	 * @param id     setmeal id
	 * @param status modifyed status
	 * @return 统一返回结果
	 */
	@PostMapping("/status/{status}")
	@ApiOperation("起售禁售接口")
	@CacheEvict(cacheNames = "setmealCache", allEntries = true)
	public Result<String> modifyStatus(Long id, @PathVariable Integer status) {
		log.info("套餐分页查询参数 id:{} status:{}", id, status);
		boolean b = setmealService.modifyStatus(id, status);
		
		if (!b) {
			return Result.success(MessageConstant.SETMEAL_ENABLE_FAILED);
		}
		return Result.success();
	}
	
	/**
	 * 批量删除套餐
	 *
	 * @param ids setmeal ids
	 * @return 统一返回结果
	 */
	@DeleteMapping
	@ApiOperation("批量删除套餐")
	@CacheEvict(cacheNames = "setmealCache", allEntries = true)
	public Result<Objects> deleteSetmeal(@RequestParam List<Integer> ids) {
		log.info("批量删除套餐参数 ids:{}", ids);
		setmealService.deleteSetmeal(ids);
		
		return Result.success();
	}
	
	/**
	 * 根据id查询套餐
	 *
	 * @param id setmeal id
	 * @return 统一返回结果
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
	 * @param setmealDTO setmeal参数
	 * @return 统一返回结果
	 */
	@PostMapping
	@ApiOperation("新增套餐")
	@CacheEvict(cacheNames = "setmealCache", allEntries = true)
	public Result<Objects> addSetmeal(@RequestBody SetmealDTO setmealDTO) {
		log.info("新增套餐参数 setmealDTO:{}", setmealDTO);
		setmealService.addSetmeal(setmealDTO);
		
		return Result.success();
	}
	
	/**
	 * 修改套餐
	 *
	 * @param setmealDTO 修改参数
	 * @return 统一返回结果
	 */
	@PutMapping
	@ApiOperation("修改套餐")
	@CacheEvict(cacheNames = "setmealCache", allEntries = true)
	public Result<Objects> updateSetmeal(@RequestBody SetmealDTO setmealDTO) {
		setmealService.updateSetmeal(setmealDTO);
		
		return Result.success();
	}
}
