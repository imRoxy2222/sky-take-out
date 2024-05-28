package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {
	@Autowired
	private AddressBookMapper addressBookMapper;
	
	private static final Integer DEFAULT_ADDRESS = 1;
	private static final Integer NOT_DEFAULT_ADDRESS = 0;
	
	/**
	 * 新增地址
	 */
	@Override
	public void addAddressBook(AddressBook addressBook) {
		if (addressBook.getIsDefault() == null) {
			addressBook.setIsDefault(NOT_DEFAULT_ADDRESS);
		}
		addressBook.setUserId(BaseContext.getCurrentId());
		if (addressBook.getConsignee() == null) {
			addressBook.setConsignee("用户" + BaseContext.getCurrentId());
		}
		addressBookMapper.insert(addressBook);
	}
	
	/**
	 * 查询当前用户所有地址
	 *
	 * @param defalutAddress : 是否只查询默认地址
	 * @return : 地址列表
	 */
	@Override
	public List<AddressBook> getAllAddressBook(boolean defalutAddress) {
		AddressBook quaryAddressBook = new AddressBook();
		quaryAddressBook.setUserId(BaseContext.getCurrentId());
//		if (defalutAddress) {
//			quaryAddressBook.setIsDefault(DEFAULT_ADDRESS);
//		} else {
//			quaryAddressBook.setIsDefault(NOT_DEFAULT_ADDRESS);
//		}
		
		List<AddressBook> list = addressBookMapper.query(quaryAddressBook);
		return list;
	}
	
	/**
	 * 设置默认地址
	 *
	 * @param addressBook : 要设置默认地址的id
	 */
	@Override
	public void setDefaultAddress(AddressBook addressBook) {
		Long userId = BaseContext.getCurrentId();
		Long id = addressBook.getId();
		addressBook.setUserId(userId);
		
		// 设置当前用户所有地址为非默认地址
		addressBook.setId(null);
		addressBook.setIsDefault(NOT_DEFAULT_ADDRESS);
		addressBookMapper.update(addressBook);
		
		// 设置当前地址为默认地址
		addressBook.setId(id);
		addressBook.setIsDefault(DEFAULT_ADDRESS);
		addressBookMapper.update(addressBook);
	}
	
	/**
	 * 根据id查询地址
	 *
	 * @param id : 查询id
	 * @return
	 */
	@Override
	public AddressBook queryById(Long id) {
		AddressBook queryParam = AddressBook.builder().id(id).userId(BaseContext.getCurrentId()).build();
		List<AddressBook> queryResult = addressBookMapper.query(queryParam);
		return queryResult.get(0);
	}
	
	/**
	 * 根据id删除地址
	 *
	 * @param id : 删除id
	 */
	@Override
	public void deleteById(Long id) {
		AddressBook deleteParam = AddressBook.builder().id(id).userId(BaseContext.getCurrentId()).build();
		addressBookMapper.delete(deleteParam);
	}
	
	/**
	 * 根据id修改地址
	 *
	 * @param addressBook : 要修改成的地址信息
	 */
	@Override
	public void modifyAddressBookById(AddressBook addressBook) {
		addressBook.setUserId(BaseContext.getCurrentId());
		addressBookMapper.update(addressBook);
	}
}
