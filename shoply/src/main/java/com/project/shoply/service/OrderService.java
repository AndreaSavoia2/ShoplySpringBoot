package com.project.shoply.service;

import com.project.shoply.entity.Order;
import com.project.shoply.entity.User;
import com.project.shoply.entity.enumerated.OrderStatus;
import com.project.shoply.entity.view.CartView;
import com.project.shoply.payload.request.OrderRequest;
import com.project.shoply.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final OrderItemService orderItemService;

    @Transactional
    public String createOrder(UserDetails userDetails, OrderRequest orderRequest) {
        User user = (User) userDetails;

        Order order = Order.builder()
                .user(user)
                .total(cartService.getTotalCartValue(userDetails))
                .shippingAddress(orderRequest.getFullAddress())
                .status(OrderStatus.PREPARED)
                .build();
        orderRepository.save(order);

        List<CartView> cartItems = cartService.findAllByUserId(userDetails);
        orderItemService.addItemToOrder(cartItems, order);
        return "Order created";
    }
}
