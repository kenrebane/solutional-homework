package solutional.ken.homework.service;

import solutional.ken.homework.dto.OrderDto;
import solutional.ken.homework.dto.OrderProductDto;
import solutional.ken.homework.dto.OrderProductQuantityDto;
import solutional.ken.homework.dto.OrderStatusDto;
import solutional.ken.homework.entity.ProductEntity;

import java.util.List;
import java.util.UUID;

public interface Orders {
    OrderDto createNewOrder();
    OrderDto getOrderDetails(UUID orderId);
    OrderDto updateOrderStatus(UUID orderId, OrderStatusDto orderStatusDto);
    OrderDto addProductsToOrder(UUID orderId, List<ProductEntity> productEntityList);
    List<OrderProductDto> getOrderProducts(UUID orderId);
    OrderDto updateOrderProductQuantity(UUID orderId, ProductEntity productEntity, OrderProductQuantityDto dto);
}
