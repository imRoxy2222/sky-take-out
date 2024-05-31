package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UserStatisticDTO implements Serializable {
	private Integer createNumber;
	private LocalDate createTime;
}
