package solutional.ken.homework.service;

import solutional.ken.homework.dto.OrderDto;

import java.util.UUID;

public interface Orders {
    OrderDto createNewOrder();
    OrderDto getOrderDetails(UUID orderId);
}
