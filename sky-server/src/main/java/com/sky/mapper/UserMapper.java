package com.sky.mapper;

import com.sky.dto.UserStatisticDTO;
import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

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
	
	@Select("select * from user where id = #{userId}")
	User getById(Long userId);
	
	/**
	 * 查询一段时间内的用户
	 *
	 * @param begin : 开始时间
	 * @param end   : 结束时间
	 * @return : 查询到的用户集合
	 */
	List<UserStatisticDTO> queryByTime(LocalDate begin, LocalDate end);
	
	/**
	 * 总数
	 *
	 * @param begin
	 * @return
	 */
	Integer queryUserTotalByTimeLT(LocalDate begin);
}
