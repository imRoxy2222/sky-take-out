package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.User;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
	/**
	 * 根据openid查询用户
	 *
	 * @param openid : 要查询用户的openid
	 * @return : 用户实体
	 */
	@Select("select id, openid, name, phone, sex, id_number, avatar, create_time from user where openid = #{openid}")
	User queryByOpenid(String openid);
	
	/**
	 * 插入一条用户
	 *
	 * @param user: 要插入的用户
	 */
	void insert(User user);
}
