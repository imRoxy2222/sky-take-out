package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private AddressBookMapper addressBookMapper;
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	@Autowired
	private ShoppingCartMapper shoppingCartMapper;
	
	/**
	 * 提交订单
	 *
	 * @param ordersSubmitDTO
	 * @return
	 */
	@Override
	@Transactional
	public OrderSubmitVO ordersSubmit(OrdersSubmitDTO ordersSubmitDTO) {
		// 1. 处理业务异常(地址为空,购物车为空
		AddressBook addressBookInfo = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
		if (addressBookInfo == null) {
			throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
		}
		// 查询购物车
		Long currentUserId = BaseContext.getCurrentId();
		
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setUserId(currentUserId);
		List<ShoppingCart> shoppingCartList = shoppingCartMapper.query(shoppingCart);
		if (shoppingCartList == null || shoppingCartList.isEmpty()) {
			throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
		}
		
		// 2. 向orders插入一条数据
		Orders insertParam = new Orders();
		BeanUtils.copyProperties(ordersSubmitDTO, insertParam);
		insertParam.setOrderTime(LocalDateTime.now());
		insertParam.setPayStatus(Orders.UN_PAID);
		insertParam.setStatus(Orders.PENDING_PAYMENT);
		insertParam.setNumber(String.valueOf(System.currentTimeMillis()));
		insertParam.setPhone(addressBookInfo.getPhone());
		insertParam.setConsignee(addressBookInfo.getConsignee());
		insertParam.setUserId(currentUserId);
		insertParam.setAddress(addressBookInfo.getDetail());
		
		orderMapper.insert(insertParam);
		
		// 3. 向order detail插入数据
		List<OrderDetail> details = new ArrayList<>();
		for (ShoppingCart cart : shoppingCartList) {
			OrderDetail detail = new OrderDetail();
			BeanUtils.copyProperties(cart, detail);
			detail.setOrderId(insertParam.getId());
			details.add(detail);
		}
		orderDetailMapper.insertBatch(details);
		
		// 4. 清空购物车
		shoppingCartMapper.deleteByUserId(currentUserId);
		
		// 5. 返回vo对象
		OrderSubmitVO result = OrderSubmitVO.builder()
				.id(currentUserId)
				.orderTime(insertParam.getOrderTime())
				.orderAmount(insertParam.getAmount())
				.orderNumber(insertParam.getNumber())
				.build();
		return result;
		
	}
}
