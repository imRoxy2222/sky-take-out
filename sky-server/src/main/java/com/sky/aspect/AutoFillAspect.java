package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面类,实现公共字段自动填充的逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
	/**
	 * 自定义切入点
	 */
	@Pointcut("execution(* com.sky.mapper.*.*(..)) && " +
			"@annotation(com.sky.annotation.AutoFill)")
	private void autoFillPointCut() {
	
	}
	
	/**
	 * 实现自动对插入人/插入时间/更新人/更新时间自动赋值
	 */
	@Before("autoFillPointCut()")
	public void autoFill(JoinPoint joinPoint) {
		log.info("开始进行公共字段的自动填充");
		
		// 获取到当前被拦截的方法上的数据库操作类型
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
		AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class); // 获取注解对象
		OperationType value = autoFill.value(); // 获取注解上的值
		
		// 获取方法的参数
		Object[] args = joinPoint.getArgs();
		if (args == null || args.length == 0) {
			return;
		}
		
		Object entiry = args[0];
		
		// 准备赋值的数据
		LocalDateTime now = LocalDateTime.now();
		Long currentId = BaseContext.getCurrentId();
		
		// 根据操作类型(insert | update)赋值,用反射赋值
		if (value == OperationType.INSERT) {
			try {
				Method setCreateTime = entiry.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
				Method setUpdateTime = entiry.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
				Method setCreateUser = entiry.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
				Method setUpdateUser = entiry.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
				
				setCreateTime.invoke(entiry, now);
				setUpdateTime.invoke(entiry, now);
				setCreateUser.invoke(entiry, currentId);
				setUpdateUser.invoke(entiry, currentId);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else if (value == OperationType.UPDATE) {
			try {
				Method setUpdateTime = entiry.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
				Method setUpdateUser = entiry.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
				
				setUpdateTime.invoke(entiry, now);
				setUpdateUser.invoke(entiry, currentId);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
	}
}
