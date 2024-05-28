package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressBookMapper {
	/**
	 * 新增地址
	 */
	void insert(AddressBook addressBook);
	
	/**
	 * 查询当前用户所有地址
	 *
	 * @param quaryAddressBook : 查询信息
	 * @return : 地址列表
	 */
	List<AddressBook> query(AddressBook quaryAddressBook);
	
	/**
	 * 更新地址信息
	 *
	 * @param updateParam : 要更新的参数
	 */
	void update(AddressBook updateParam);
	
	/**
	 * 删除地址
	 *
	 * @param deleteParam : 删除信息
	 */
	void delete(AddressBook deleteParam);
}
