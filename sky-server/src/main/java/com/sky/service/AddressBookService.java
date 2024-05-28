package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
	/**
	 * 新增地址
	 */
	void addAddressBook(AddressBook addressBook);
	
	/**
	 * 查询当前用户所有地址
	 *
	 * @param defalutAddress : 是否只查询默认地址
	 * @return : 地址列表
	 */
	List<AddressBook> getAllAddressBook(boolean defalutAddress);
	
	/**
	 * 设置默认地址
	 *
	 * @param addressBook : 要设置默认地址的id
	 */
	void setDefaultAddress(AddressBook addressBook);
	
	/**
	 * 根据id查询地址
	 *
	 * @param id : 查询id
	 * @return
	 */
	AddressBook queryById(Long id);
	
	/**
	 * 根据id删除地址
	 *
	 * @param id : 删除id
	 * @return
	 */
	void deleteById(Long id);
	
	/**
	 * 根据id修改地址
	 *
	 * @param addressBook : 要修改成的地址信息
	 */
	void modifyAddressBookById(AddressBook addressBook);
}
