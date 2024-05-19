package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	/**
	 * 捕获业务异常
	 *
	 * @param ex
	 * @return Result<String>
	 */
	@ExceptionHandler
	public Result<String> exceptionHandler(BaseException ex) {
		log.error("异常信息：{}", ex.getMessage());
		return Result.error(ex.getMessage());
	}
	
	/**
	 * Mysql异常
	 *
	 * @param ex
	 * @return
	 */
	@ExceptionHandler
	public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
		String messgage = ex.getMessage();
		// Duplicate entry '511621200309202359' for key 'idx_username'
		if (messgage.contains("Duplicate")) {
			String username = messgage.split(" ")[2];
			return Result.error(username + MessageConstant.ALREADY_EXIST);
		} else {
			return Result.error(MessageConstant.UNKNOWN_ERROR);
		}
		
	}
}
