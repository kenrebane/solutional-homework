package solutional.ken.homework.service;

import solutional.ken.homework.entity.OrderEntity;
import solutional.ken.homework.enums.OrderStatus;

import java.util.UUID;

public interface OrderData {
    OrderEntity createOrderWithStatus(OrderStatus status);
    OrderEntity getOrderById(UUID orderId);
}
