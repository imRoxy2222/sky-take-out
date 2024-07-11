package com.sky.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;   

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private WeChatProperties weChatProperties;
	@Autowired
	private UserMapper userMapper;
	
	/**
	 * 微信登录
	 *
	 * @param userLoginDTO : 前端传来的用户登录实体
	 * @return : 用户实体
	 */
	public User wxLogin(UserLoginDTO userLoginDTO) {
		String openid = getOpenid(userLoginDTO);
		
		// 判断openid是否为空
		if (openid == null) {
			throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
		}
		
		// 判断当前用户是否为新用户
		User user = userMapper.queryByOpenid(openid);
		if (user == null) {
			// 新用户 -> 完成注册
			user = new User();
			user.setOpenid(openid);
			user.setCreateTime(LocalDateTime.now());
			userMapper.insert(user);
		}
		
		// 返回
		return user;
	}
	
	private String getOpenid(UserLoginDTO userLoginDTO) {
		// 调用微信接口,获得当前用户的openid
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appid", weChatProperties.getAppid());
		paramMap.put("secret", weChatProperties.getSecret());
		paramMap.put("js_code", userLoginDTO.getCode());
		paramMap.put("grant_type", "authorization_code");
		
		String json = HttpClientUtil.doGet(WX_LOGIN, paramMap);
		JSONObject jsonObject = JSON.parseObject(json);
		
		return jsonObject.getString("openid");
	}
	
}
