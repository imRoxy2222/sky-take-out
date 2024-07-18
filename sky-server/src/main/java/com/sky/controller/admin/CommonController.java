package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {
	
	private final CommonService commonService;
	
	public CommonController(CommonService commonService) {
		this.commonService = commonService;
	}
	
	@PostMapping("/upload")
	@ApiOperation("文件上传接口")
	public Result<String> fileUpload(MultipartFile file) {
		return Result.success("https://picx.zhimg.com/70/v2-ebde7309e61d7ea0635a2f64a599625d_1440w.image?source=172ae18b&biz_tag=Post%201x,%20https://picx.zhimg.com/70/v2-ebde7309e61d7ea0635a2f64a599625d_1440w.image?source=172ae18b&biz_tag=Post%202x");
	}
	
}
