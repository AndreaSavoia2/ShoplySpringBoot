package com.project.shoply.service;

import com.project.shoply.entity.Order;
import com.project.shoply.entity.OrderItem;
import com.project.shoply.entity.Product;
import com.project.shoply.entity.User;
import com.project.shoply.entity.enumerated.OrderStatus;
import com.project.shoply.entity.view.CartView;
import com.project.shoply.exception.GenericException;
import com.project.shoply.exception.ResourceNotFoundException;
import com.project.shoply.payload.request.OrderRequest;
import com.project.shoply.payload.response.OrderItemResponse;
import com.project.shoply.payload.response.OrderResponse;
import com.project.shoply.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

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
        if (cartItems.isEmpty())
            throw new GenericException("cart not have items",HttpStatus.BAD_REQUEST);

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

    public OrderResponse getOrderDetails(UserDetails userDetails, long orderId){
        User user = (User) userDetails;

        Order order = findOrderById(orderId);
        if (order.getUser().getId() != user.getId())
            throw new GenericException(
                    "Order ownership conflict: the order does not belong to the specified user.",
                    HttpStatus.FORBIDDEN);

        List<OrderItemResponse> orderItemsResponse = orderItemService.findAllOrderItemsResponseByOrderId(orderId);

        return OrderResponse.builder()
                .orderId(order.getId())
                .shippingAddress(order.getShippingAddress())
                .status(order.getStatus())
                .orderItems(orderItemsResponse)
                .build();
    }

    public List<OrderResponse> getAllOrderByUser(UserDetails userDetails) {
        User user = (User) userDetails;

        // Recupero tutti gli ordini dell'utente
        List<Order> orders = findOrdersByUserId(user.getId());

        // Estraggo gli ID degli ordini
        Set<Long> orderIds = new HashSet<>();
        for (Order order : orders) {
            orderIds.add(order.getId());
        }

        List<OrderItem> orderItems = orderItemService.findAllByOrderIds(orderIds);

        // Mappo gli OrderItem per ordine
        Map<Long, List<OrderItemResponse>> orderItemsMap = new HashMap<>();
        for (OrderItem orderItem : orderItems) {
            Long orderId = orderItem.getOrder().getId();

            // Se la chiave non esiste, la inizializzo
            if (!orderItemsMap.containsKey(orderId)) {
                orderItemsMap.put(orderId, new ArrayList<>());
            }

            // Aggiungiamo l'item alla lista corrispondente all'ordine
            orderItemsMap.get(orderId).add(
                    new OrderItemResponse(orderItem.getId(), orderItem.getQuantity(), orderItem.getPrice(), orderItem.getProduct())
            );
        }

        // Creo la lista di risposte
        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            List<OrderItemResponse> items = orderItemsMap.get(order.getId());
            if (items == null) {
                items = new ArrayList<>();
            }
            responses.add(new OrderResponse(order.getId(), order.getShippingAddress(),order.getStatus(), items));
        }

        return responses;
    }


    protected Order findOrderById(long id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    protected List<Order> findOrdersByUserId(long userId){
        return orderRepository.findAllByUserId(userId);
    }
}
