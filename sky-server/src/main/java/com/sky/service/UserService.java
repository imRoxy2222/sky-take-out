package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

public interface UserService {
	public final static String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
	
	/**
	 * 微信登录
	 *
	 * @param userLoginDTO : 登录的DTO
	 * @return : 登录用户的实体
	 */
	public User wxLogin(UserLoginDTO userLoginDTO);
}
