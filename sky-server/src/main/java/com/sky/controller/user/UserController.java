package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.impl.UserServiceImpl;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/user/user")
@Slf4j
@Api(tags = "登录相关接口")
@RestController
public class UserController {
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private JwtProperties jwtProperties;
	
	/**
	 * 用户登录接口
	 *
	 * @param userLoginDTO : 获取到的code
	 * @return : 登录VO
	 */
	@ApiOperation("登录")
	@PostMapping("/login")
	public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
		log.info("微信登录参数 userLoginDTO:{}", userLoginDTO);
		
		User user = userService.wxLogin(userLoginDTO);
		
		// 生成jwt令牌
		Map<String, Object> claims = new HashMap<>();
		claims.put(JwtClaimsConstant.USER_ID, user.getId());
		String jwtToken = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
		
		UserLoginVO userLoginVO = new UserLoginVO();
		BeanUtils.copyProperties(user, userLoginVO);
		userLoginVO.setToken(jwtToken);
		
		return Result.success(userLoginVO);
	}
}
