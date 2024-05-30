package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	 * @param outTradeNo : 订单号
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
	
	/**
	 * 分页查询历史订单
	 *
	 * @param ordersPageQueryDTO
	 * @return
	 */
	@Override
	public PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {
		// 设置分页
		PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
		
		OrdersPageQueryDTO ordersPageQueryDTOt = new OrdersPageQueryDTO();
		ordersPageQueryDTOt.setUserId(BaseContext.getCurrentId());
		ordersPageQueryDTOt.setStatus(ordersPageQueryDTO.getStatus());
		
		// 分页条件查询
		Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
		if (page == null || page.getTotal() == 0) {
			return new PageResult(0, null);
		}
		
		List<OrderVO> list = new ArrayList<>(page.size());
		
		// 查询出订单明细，并封装入OrderVO进行响应
		for (Orders orders : page) {
			Long orderId = orders.getId();// 订单id
			
			// 查询订单明细
			List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);
			
			OrderVO orderVO = new OrderVO();
			BeanUtils.copyProperties(orders, orderVO);
			orderVO.setOrderDetailList(orderDetails);
			
			list.add(orderVO);
		}
		
		return new PageResult(page.getTotal(), list);
	}
	
	/**
	 * 查询订单详情
	 *
	 * @param id : 订单id
	 * @return : 查询结果实体
	 */
	@Override
	public OrderVO orderDetail(Long id) {
		List<OrderDetail> orderDetail = orderDetailMapper.getByOrderId(id);
		Orders order = orderMapper.queryById(id);
		
		OrderVO vo = new OrderVO();
		BeanUtils.copyProperties(order, vo);
		vo.setOrderDetailList(orderDetail);
		
		return vo;
	}
	
	/**
	 * 取消订单
	 *
	 * @param id : 要取消订单的id
	 */
	@Override
	public void cancelOrder(Long id) {
		// 查询订单
		Orders ordersDb = orderMapper.getById(id);
		// 订单不存在
		if (ordersDb == null) {
			throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
		}
		// 订单状态 (已经接单或配送中需要退款,这里无法实现,因为没有微支付接口
		
		
		Orders updateParam = Orders.builder()
				.id(id)
				.status(Orders.CANCELLED)
				.payStatus(Orders.REFUND)
				.cancelTime(LocalDateTime.now())
				.cancelReason("用户取消")
				.build();
		orderMapper.update(updateParam);
	}
	
	/**
	 * 再来一单
	 *
	 * @param id : 再来一单的单号id
	 */
	@Transactional // 此方法有多个数据库写入操作,加入事物
	@Override
	public void repetition(Long id) {
		Long currentUserId = BaseContext.getCurrentId();
		// 获取要再来一单的单子中的所有商品信息
		List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
		// 将他转换成shopping cart
		List<ShoppingCart> shoppingCart = orderDetailList.stream().map(x -> {
			ShoppingCart cart = new ShoppingCart();
			// 复制公共属性
			BeanUtils.copyProperties(x, cart);
			cart.setUserId(currentUserId);
			cart.setCreateTime(LocalDateTime.now());
			
			return cart;
		}).collect(Collectors.toList());
		
		// 将购物车数据全部插入
		shoppingCartMapper.insertBatch(shoppingCart);
		
		// 获得上一次下单参数
		Orders order = orderMapper.getById(id);
		OrdersSubmitDTO ordersSubmitDTO = new OrdersSubmitDTO();
		BeanUtils.copyProperties(order, ordersSubmitDTO);
		// 更新一下预计送达时间
		ordersSubmitDTO.setEstimatedDeliveryTime(LocalDateTime.now().plusHours(1));
		this.ordersSubmit(ordersSubmitDTO);
	}
}
