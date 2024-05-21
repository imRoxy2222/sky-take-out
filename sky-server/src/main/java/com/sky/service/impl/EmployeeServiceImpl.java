package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	/**
	 * 员工登录
	 *
	 * @param employeeLoginDTO
	 * @return employee
	 */
	@Override
	public Employee login(EmployeeLoginDTO employeeLoginDTO) {
		String username = employeeLoginDTO.getUsername();
		// md5加密
		String password = DigestUtils.md5DigestAsHex(employeeLoginDTO.getPassword().getBytes());
		
		//1、根据用户名查询数据库中的数据
		Employee employee = employeeMapper.getByUsername(username);
		
		//2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
		if (employee == null) {
			//账号不存在
			throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
		}
		
		//密码比对
		if (!password.equals(employee.getPassword())) {
			//密码错误
			throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
		}
		
		if (employee.getStatus() == StatusConstant.DISABLE) {
			//账号被锁定
			throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
		}
		
		//3、返回实体对象
		return employee;
	}
	
	@Override
	public void addEmployee(EmployeeDTO employeeDTO) {
		Employee employee = new Employee();
		BeanUtils.copyProperties(employeeDTO, employee);
		
		// 设置默认密码
		employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
		// 设置账号状态
		employee.setStatus(StatusConstant.ENABLE);
		// 设置创建时间/更新时间
		//employee.setCreateTime(LocalDateTime.now());
		//employee.setUpdateTime(LocalDateTime.now());
		// 设置创建用户/更新用户
		employee.setCreateUser(BaseContext.getCurrentId());
		employee.setUpdateUser(BaseContext.getCurrentId());
		
		employeeMapper.insert(employee);
		
	}
	
	/**
	 * 分页查询员工
	 *
	 * @param employeePageQueryDTO
	 * @return
	 */
	@Override
	public PageResult queryPage(EmployeePageQueryDTO employeePageQueryDTO) {
		PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
		
		Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
		
		long total = page.getTotal();
		List<Employee> records = page.getResult();
		
		return new PageResult(total, records);
	}
	
	/**
	 * 启用/禁用员工
	 *
	 * @param id
	 * @param status
	 */
	@Override
	public void modifyStatus(Integer id, Integer status) {
		if (id == 1) {
			return;
		}
		
		Employee employee = new Employee();
		employee.setStatus(status);
		employee.setId(Integer.toUnsignedLong(id));
		
		employeeMapper.update(employee);
	}
	
	/**
	 * 修改员工信息
	 *
	 * @param employeeDTO
	 */
	@Override
	public void modifyEmployee(EmployeeDTO employeeDTO) {
		Employee employee = new Employee();
		BeanUtils.copyProperties(employeeDTO, employee);
		// 设置更新人
		//employee.setUpdateUser(BaseContext.getCurrentId());
		// 设置更新时间
		//employee.setUpdateTime(LocalDateTime.now());
		
		employeeMapper.update(employee);
	}
	
	/**
	 * 根据id查询员工
	 *
	 * @param id
	 * @return
	 */
	@Override
	public Employee queryById(Integer id) {
		Employee employee = employeeMapper.queryById(id);
		employee.setPassword("******");
		
		return employee;
	}
	
}
