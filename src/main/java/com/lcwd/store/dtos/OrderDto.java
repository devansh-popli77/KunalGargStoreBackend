package com.lcwd.store.dtos;

import com.lcwd.store.entities.OrderItem;
import com.lcwd.store.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private String orderId;
    //pending delivered dispatched
    private String orderStatus="PENDING";
    //notpaid, paid
    private String paymentStatus="NOTPAID";
    private int orderAmount;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderedDate=new Date();
    private Date deliveredDate;
    private UserDto user;
    private List<OrderItemDto> orderItems=new ArrayList<>();
    private String razorPayOrderId;
    private String razorPayPaymentId;
}
