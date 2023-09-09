package com.lcwd.store.controller;

import com.lcwd.store.dtos.ApiResponseMessage;
import com.lcwd.store.dtos.OrderDto;
import com.lcwd.store.dtos.PageableResponse;
import com.lcwd.store.dtos.OrderDto;
import com.lcwd.store.services.OrderService;
import com.lcwd.store.services.impl.TransactionDetails;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/{cartId}/user/{userId}")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto, @PathVariable String cartId, @PathVariable String userId) {
        System.out.println(orderDto.getOrderStatus());
        return new ResponseEntity<>(orderService.createOrder(orderDto, userId, cartId), HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId) {

        orderService.removeOrder(orderId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .status(HttpStatus.OK)
                .success(true)
                .message("order is removed").build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable String userId) {
        return new ResponseEntity<>(orderService.getOrdersOfUsers(userId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                   @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,

                                                                   @RequestParam(value = "sortBy", defaultValue = "billingName", required = false)
                                                                   String sortBy,
                                                                   @RequestParam(value = "sortDir", defaultValue = "asc", required = false)
                                                                   String sortDir) {
        return new ResponseEntity<>(orderService.getOrders(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @PutMapping("/{OrderId}")
    public ResponseEntity<OrderDto> updateOrder(@RequestBody OrderDto OrderDto, @PathVariable("OrderId") String OrderId) {
        OrderDto updatedOrderDto = orderService.updateOrder(OrderDto, OrderId);
        return new ResponseEntity<>(updatedOrderDto, HttpStatus.OK);
    }
    @GetMapping("/create-transaction-razorpay/{amount}")
    public ResponseEntity<TransactionDetails> updateOrder( @PathVariable("amount") Long amount) throws RazorpayException {
        TransactionDetails transactionDetails = orderService.createTransaction(amount);
        return new ResponseEntity<>(transactionDetails, HttpStatus.OK);
    }
}
