package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
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
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private WeChatPayUtil weChatPayUtil;
	
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
		return OrderSubmitVO.builder()
				.id(currentUserId)
				.orderTime(insertParam.getOrderTime())
				.orderAmount(insertParam.getAmount())
				.orderNumber(insertParam.getNumber())
				.build();
		
	}
	
	
	/**
	 * 订单支付
	 *
	 * @param ordersPaymentDTO
	 * @return
	 */
	public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) {
		new Thread() {
			@Override
			public void run() {
				try {
					sleep(5000);
					paySuccess(ordersPaymentDTO.getOrderNumber());
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				
			}
		}.start();
		return null;
	}
	
	/**
	 * 支付成功，修改订单状态
	 *
	 * @param outTradeNo
	 */
	public void paySuccess(String outTradeNo) {
		
		// 根据订单号查询订单
		Orders ordersDB = orderMapper.getByNumber(outTradeNo);
		
		// 根据订单id更新订单的状态、支付方式、支付状态、结账时间
		Orders orders = Orders.builder()
				.id(ordersDB.getId())
				.status(Orders.TO_BE_CONFIRMED)
				.payStatus(Orders.PAID)
				.checkoutTime(LocalDateTime.now())
				.build();
		
		orderMapper.update(orders);
	}
}
