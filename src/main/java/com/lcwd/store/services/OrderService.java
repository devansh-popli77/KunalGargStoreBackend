package com.lcwd.store.services;

import com.lcwd.store.dtos.OrderDto;
import com.lcwd.store.dtos.PageableResponse;
import com.lcwd.store.services.impl.TransactionDetails;
import com.razorpay.RazorpayException;

import java.util.List;

public interface OrderService {

    //create order
    OrderDto createOrder(OrderDto orderDto, String userId, String cartId);


    //remove order
    void removeOrder(String orderId);
    //get orders of users

    List<OrderDto> getOrdersOfUsers(String userId);

    //get orders
    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);
    public TransactionDetails createTransaction(Long amount) throws RazorpayException ;
    OrderDto updateOrder(OrderDto orderDto, String orderId);
}
