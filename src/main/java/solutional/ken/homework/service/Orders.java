package solutional.ken.homework.service;

import solutional.ken.homework.dto.OrderDto;
import solutional.ken.homework.dto.OrderStatusDto;

import java.util.UUID;

public interface Orders {
    OrderDto createNewOrder();
    OrderDto getOrderDetails(UUID orderId);
    OrderDto updateOrderStatus(UUID orderId, OrderStatusDto orderStatusDto);
}
