package solutional.ken.homework.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import solutional.ken.homework.dto.*;
import solutional.ken.homework.entity.ProductEntity;
import solutional.ken.homework.repository.ProductsRepository;
import solutional.ken.homework.service.Orders;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class OrdersController {
    private Orders orders;
    private ProductsRepository productsRepository;

    @PostMapping("/api/orders")
    public OrderDto createNewOrder() {
        return orders.createNewOrder();
    }

    @GetMapping("/api/orders/{orderId}")
    public OrderDto getOrderDetails(
            @PathVariable UUID orderId
    ) {
        return orders.getOrderDetails(orderId);
    }

    @PatchMapping("/api/orders/{orderId}")
    public OrderDto updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestBody OrderStatusDto orderStatusDto
    ) {
        return orders.updateOrderStatus(orderId, orderStatusDto);
    }

    @PostMapping("/api/orders/{orderId}/products")
    public OrderDto addProductsToOrder(
            @PathVariable UUID orderId,
            @RequestBody List<Integer> productIds
    ) {
        List<ProductEntity> productEntityList = productsRepository.findAllById(productIds);
        return orders.addProductsToOrder(orderId, productEntityList);
    }

    @GetMapping("/api/orders/{orderId}/products")
    public List<OrderProductDto> getOrderProducts(
            @PathVariable UUID orderId
    ) {
        return orders.getOrderProducts(orderId);
    }

    @PatchMapping("/api/orders/{orderId}/products/{productId}")
    public OrderDto updateOrderProductQuantityOrReplaceOrderProduct(
            @PathVariable UUID orderId,
            @PathVariable Integer productId,
            @RequestBody UpdateOrderProductQuantityOrReplaceOrderProductDto dto
    ) {
        if (dto.getQuantity() != null) {
            ProductEntity productEntity = productsRepository.findById(productId).orElseThrow();
            UpdateOrderProductQuantityDto updateOrderProductQuantityDto = new UpdateOrderProductQuantityDto();
            updateOrderProductQuantityDto.setQuantity(dto.getQuantity());
            return orders.updateOrderProductQuantity(orderId, productEntity, updateOrderProductQuantityDto);
        }

        if (dto.getReplaceWith() != null) {
            ProductEntity productToReplace = productsRepository.findById(productId).orElseThrow();
            ProductEntity replacementProduct = productsRepository.findById(dto.getReplaceWith().getProductId()).orElseThrow();
            return orders.replaceOrderProduct(orderId, productToReplace, replacementProduct, dto.getReplaceWith());
        }

        return null;
    }
}
