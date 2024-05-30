package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
