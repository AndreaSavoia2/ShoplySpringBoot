package com.project.shoply.repository;

import com.project.shoply.entity.OrderItem;
import com.project.shoply.payload.response.OrderItemResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT new com.project.shoply.payload.response.OrderItemResponse(" +
            "oi.id," +
            "oi.quantity," +
            "oi.price," +
            "oi.product) " +
            "FROM OrderItem oi WHERE oi.order = :orderId")
    List<OrderItemResponse> findAllOrderItemsResponseByOrderId(long orderId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order in :orderItemsIds")
    List<OrderItem> findAllOrderItemsResponseByOrderId(Set<Long> orderItemsIds);

    List<OrderItem> findAllByOrderId(long orderId);

}
