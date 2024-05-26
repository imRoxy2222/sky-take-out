package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@Slf4j
@Api(tags = "category")
@RequestMapping("/user/category")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
	@ApiOperation("category list")
	@GetMapping("/list")
	public Result<List<Category>> list(Long type) {
		log.info("category list方法 type:{}", type);
		List<Category> categories = categoryService.list(type);
		return Result.success(categories);
	}
}
