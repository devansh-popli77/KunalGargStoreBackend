package com.lcwd.store.services.impl;

import com.lcwd.store.dtos.OrderDto;
import com.lcwd.store.dtos.PageableResponse;
import com.lcwd.store.dtos.ProductDto;
import com.lcwd.store.entities.*;
import com.lcwd.store.exceptions.BadApiRequestException;
import com.lcwd.store.exceptions.ResourceNotFoundException;
import com.lcwd.store.helper.HelperUtils;
import com.lcwd.store.repositories.CartRepository;
import com.lcwd.store.repositories.OrderItemRepository;
import com.lcwd.store.repositories.OrderRepository;
import com.lcwd.store.repositories.UserRepository;
import com.lcwd.store.services.OrderService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${key}")
    private String key;
    @Value("${secret}")
    private String secret;
    private String currency="INR";
    @Override
    public OrderDto createOrder(OrderDto orderDto, String userId, String cartId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not Found with given id"));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("No cart Found"));
        List<CartItem> cartItems = cart.getItems();
        if (cartItems.size() <= 0) {
            throw new BadApiRequestException("Invalid number of items in cart");
        }
        Order order = Order.builder().billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(orderDto.getDeliveredDate())
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();
        AtomicReference<Long> orderAmount = new AtomicReference<>(0L);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order).build();
            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());
        order.setRazorPayOrderId(orderDto.getRazorPayOrderId());
        order.setRazorPayPaymentId(orderDto.getRazorPayPaymentId());

        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order is not found"));
        orderRepository.delete(order);

    }

    @Override
    public List<OrderDto> getOrdersOfUsers(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!!"));
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> orders = orderRepository.findAll(pageable);
        PageableResponse<OrderDto> response = HelperUtils.getPageableResponse(orders, OrderDto.class);
        return response;
    }

    @Override
    public OrderDto updateOrder(OrderDto orderDto, String orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found!!"));
        order.setPaymentStatus(orderDto.getPaymentStatus());
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setBillingName(orderDto.getBillingName());
        order.setBillingPhone(orderDto.getBillingPhone());
        order.setBillingAddress(orderDto.getBillingAddress());
        if(orderDto.getOrderStatus().equalsIgnoreCase("Delivered"))
        order.setDeliveredDate(orderDto.getDeliveredDate());
//        if(orderDto.getOrderStatus().equalsIgnoreCase("Delivered"))
//        order.setDeliveredDate(new Date());
        orderRepository.save(order);
        return modelMapper.map(order, OrderDto.class);
    }

    public TransactionDetails createTransaction(Long amount) throws RazorpayException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("amount",amount*100);
        jsonObject.put("currency",currency);
        RazorpayClient razorpayClient=new RazorpayClient(key,secret);
       com.razorpay.Order order =razorpayClient.orders.create(jsonObject);
      log.info("Order : {}",order);
      return prepareTransaction(order);
    }
    private TransactionDetails prepareTransaction(com.razorpay.Order order)
    {
        String orderId=order.get("id");
        String currency=order.get("currency");
        Integer amount=order.get("amount");
        return new TransactionDetails(orderId,currency,amount,key);
    }
}
