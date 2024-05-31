package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class OrderStatistic implements Serializable {
	private LocalDate time;
	private Integer orderNumber;
	private Integer validNumber;
}
