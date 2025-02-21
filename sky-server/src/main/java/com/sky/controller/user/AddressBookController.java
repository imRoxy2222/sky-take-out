package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "地址簿相关接口")
public class AddressBookController {
	@Autowired
	private AddressBookService addressBookService;
	
	/**
	 * 新增地址
	 *
	 * @param addressBook : 新增的地址entity
	 * @return : 统一返回结果
	 */
	@PostMapping
	@ApiOperation("新增地址")
	public Result<Object> AddAddressBook(@RequestBody AddressBook addressBook) {
		log.info("新增地址 addressBook:{}", addressBook);
		addressBookService.addAddressBook(addressBook);
		
		return Result.success();
	}
	
	/**
	 * 查询当前用户所有地址
	 *
	 * @return : 统一返回结果
	 */
	@GetMapping("/list")
	@ApiOperation("查询当前登录用户的所有地址信息")
	public Result<List<AddressBook>> getAllAddressBook() {
		log.info("查询当前登录用户的所有地址信息");
		List<AddressBook> list = addressBookService.getAllAddressBook(false);
		return Result.success(list);
	}
	
	/**
	 * 查询当前用户的默认地址
	 *
	 * @return : 统一返回结果
	 */
	@GetMapping("/default")
	@ApiOperation("查询当前用户的默认地址")
	public Result<AddressBook> getDefaultAddressBook() {
		log.info("查询当前用户的默认地址");
		AddressBook list = addressBookService.getAllAddressBook(true).get(0);
		return Result.success(list);
	}
	
	/**
	 * 设置默认地址
	 *
	 * @param addressBook : 要设置默认地址的id
	 * @return : 统一返回结果
	 */
	@PutMapping("/default")
	@ApiOperation("设置默认地址")
	public Result<Object> setDefaultAddressBook(@RequestBody AddressBook addressBook) {
		log.info("设置默认地址 addressBook:{}", addressBook);
		addressBookService.setDefaultAddress(addressBook);
		
		return Result.success();
	}
	
	/**
	 * 根据id查询地址
	 *
	 * @param id : 查询id
	 * @return 返回地址信息
	 */
	@GetMapping("/{id}")
	@ApiOperation("根据id查询地址")
	public Result<AddressBook> getAddressBookById(@PathVariable Long id) {
		log.info("根据id查询地址 id:{}", id);
		AddressBook queryResult = addressBookService.queryById(id);
		
		return Result.success(queryResult);
	}
	
	/**
	 * 根据id删除地址
	 *
	 * @param id : 删除id
	 * @return 统一返回结果
	 */
	@DeleteMapping("/{id}")
	@ApiOperation("根据id删除地址")
	public Result<Object> deleteAddressBookById(@PathVariable Long id) {
		log.info("根据id删除地址 id:{}", id);
		addressBookService.deleteById(id);
		
		return Result.success();
	}
	
	/**
	 * 根据id修改地址
	 *
	 * @param addressBook : 要修改成的地址信息
	 * @return 统一返回结果
	 */
	@PutMapping
	@ApiOperation("根据id修改地址")
	public Result<Object> modifyAddressBookById(@RequestBody AddressBook addressBook) {
		log.info("根据id修改地址 addressBook:{}", addressBook);
		addressBookService.modifyAddressBookById(addressBook);
		
		return Result.success();
	}
}
