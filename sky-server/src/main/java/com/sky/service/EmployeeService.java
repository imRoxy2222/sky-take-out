package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {
	
	/**
	 * 员工登录
	 *
	 * @param employeeLoginDTO
	 * @return
	 */
	Employee login(EmployeeLoginDTO employeeLoginDTO);
	
	/**
	 * 新增员工
	 *
	 * @param employeeDTO
	 */
	void addEmployee(EmployeeDTO employeeDTO);
	
	/**
	 * 分页查询员工
	 *
	 * @param employeePageQueryDTO
	 * @return
	 */
	PageResult queryPage(EmployeePageQueryDTO employeePageQueryDTO);
	
	/**
	 * 启用/禁用员工
	 *
	 * @param id
	 * @param status
	 */
	void modifyStatus(Integer id, Integer status);
	
	/**
	 * 编辑员工
	 *
	 * @param employeeDTO
	 */
	void modifyEmployee(EmployeeDTO employeeDTO);
	
	/**
	 * 根据id查询员工
	 *
	 * @param id
	 * @return
	 */
	Employee queryById(Integer id);
}
