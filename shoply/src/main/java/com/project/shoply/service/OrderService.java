package com.project.shoply.service;

import com.project.shoply.entity.Order;
import com.project.shoply.entity.User;
import com.project.shoply.entity.enumerated.OrderStatus;
import com.project.shoply.entity.view.CartView;
import com.project.shoply.exception.GenericException;
import com.project.shoply.exception.ResourceNotFoundException;
import com.project.shoply.payload.request.OrderRequest;
import com.project.shoply.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Transactional
    public String deleteOrder(UserDetails userDetails, long orderId){
        User user = (User) userDetails;

        Order order = findOrderById(orderId);
        if (order.getStatus() != OrderStatus.PREPARED)
            throw new GenericException("only an order in preparation can be cancelled",HttpStatus.CONFLICT);
        if (order.getUser().getId() != user.getId())
            throw new GenericException("The order does not belong to the user", HttpStatus.CONFLICT);

        orderItemService.deleteOrderItemsByOrderId(orderId);
        orderRepository.delete(order);
        return "Order cancelled";
    }

    protected Order findOrderById(long id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }
}
