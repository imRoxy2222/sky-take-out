package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OrderDetailMapper {
	/**
	 * 批量插入数据
	 *
	 * @param details : 要插入的order detail集合
	 */
	void insertBatch(List<OrderDetail> details);
	
	/**
	 * 根据order id查询order detail集合
	 *
	 * @param orderId : order id
	 * @return : order detail集合
	 */
	@Select("select  * from order_detail where order_id = #{orderId}")
	List<OrderDetail> getByOrderId(Long orderId);
	
	
	/**
	 * @param top      : 要查询top几
	 * @param begin    : 开始时间
	 * @param end      : 结束时间
	 * @param orderIds : 有效的order id
	 * @return : 查询结果
	 */
	List<GoodsSalesDTO> queryTopNumber(int top, LocalDate begin, LocalDate end, List<Long> orderIds);
}
