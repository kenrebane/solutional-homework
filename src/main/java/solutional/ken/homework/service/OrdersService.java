package solutional.ken.homework.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutional.ken.homework.dto.*;
import solutional.ken.homework.entity.OrderEntity;
import solutional.ken.homework.entity.OrderProductEntity;
import solutional.ken.homework.entity.OrderProductReplacedWith;
import solutional.ken.homework.entity.ProductEntity;
import solutional.ken.homework.enums.OrderStatus;
import solutional.ken.homework.repository.OrdersRepository;
import solutional.ken.homework.mapper.OrderMapper;

import java.util.*;

@Service
@AllArgsConstructor
public class OrdersService implements Orders {
    private OrderData orderData;
    private OrdersRepository repository;
    private OrderMapper mapper;

    @Override
    public OrderDto createNewOrder() {
        OrderEntity orderEntity = orderData.createOrderWithStatus(OrderStatus.NEW);
        return mapper.fromEntityToDto(orderEntity);
    }

    @Override
    public OrderDto getOrderDetails(
            UUID orderId
    ) {
        OrderEntity orderEntity = orderData.getOrderById(orderId);
        return mapper.fromEntityToDto(orderEntity);
    }

    @Override
    public OrderDto updateOrderStatus(
            UUID orderId,
            OrderStatusDto orderStatusDto
    ) {
        // Validate status change constraints, from new to paid only
        OrderEntity order = orderData.getOrderById(orderId);
        orderData.updateOrderStatus(order, orderStatusDto);
        orderData.markOrderAsPaid(order);
        return mapper.fromEntityToDto(order);
    }

    @Override
    public OrderDto addProductsToOrder(
            UUID orderId,
            List<ProductEntity> productEntityList
    ) {
        // Products can be added to order which is in status NEW
        OrderEntity orderEntity = orderData.getOrderById(orderId);
        List<Integer> existingProductIds = orderEntity.getOrderProducts().stream()
                .map((orderProductEntity) -> orderProductEntity.getProduct().getId()).toList();

        for (ProductEntity productEntity : productEntityList) {
            if (existingProductIds.contains(productEntity.getId())) {
                for (OrderProductEntity orderProductEntity : orderEntity.getOrderProducts()) {
                    if (productEntity.getId().equals(orderProductEntity.getProduct().getId())) {
                        if (orderProductEntity.getReplacedWith() == null) {
                            orderProductEntity.addQuantity();
                        } else {
                            throw new RuntimeException("Order product has been replaced");
                        }
                    }
                }
            } else {
                OrderProductEntity orderProductEntity = new OrderProductEntity();
                orderProductEntity.setOrder(orderEntity);
                orderProductEntity.setProduct(productEntity);
                orderProductEntity.addQuantity();
                orderEntity.getOrderProducts().add(orderProductEntity);
            }
        }

        for (OrderProductEntity orderProductEntity : orderEntity.getOrderProducts()) {
            Double updatedTotal = orderEntity.getAmounts().getTotal() + orderProductEntity.getProduct().getPrice();
            orderEntity.getAmounts().setTotal(updatedTotal);
        }

        repository.save(orderEntity);

        return mapper.fromEntityToDto(orderEntity);
    }

    @Override
    public List<OrderProductDto> getOrderProducts(UUID orderId) {
        OrderEntity orderEntity = orderData.getOrderById(orderId);
        OrderDto orderDto = mapper.fromEntityToDto(orderEntity);
        return orderDto.getProducts();
    }

    @Override
    public OrderDto updateOrderProductQuantity(
            UUID orderId,
            ProductEntity productEntity,
            UpdateOrderProductQuantityDto dto
    ) {
        // Order product quantity can only be updated if order is in status NEW
        OrderEntity orderEntity = orderData.getOrderById(orderId);

        Integer currentQuantity = 0;

        for (OrderProductEntity orderProductEntity : orderEntity.getOrderProducts()) {
            if (orderProductEntity.getProduct().getId().equals(productEntity.getId())) {
                currentQuantity = orderProductEntity.getQuantity();
                orderProductEntity.setQuantity(dto.getQuantity());
            }
        }

        if (currentQuantity > dto.getQuantity()) {
            Double amountToDeductFromTotal = dto.getQuantity() * productEntity.getPrice();
            Double newTotalAmount = orderEntity.getAmounts().getTotal() - amountToDeductFromTotal;
            orderEntity.getAmounts().setTotal(newTotalAmount);
        }

        if (currentQuantity < dto.getQuantity()) {
            Double amountToAddToTotal = (dto.getQuantity() - currentQuantity) * productEntity.getPrice();
            Double newTotalAmount = orderEntity.getAmounts().getTotal() + amountToAddToTotal;
            orderEntity.getAmounts().setTotal(newTotalAmount);
        }

        repository.save(orderEntity);
        return mapper.fromEntityToDto(orderEntity);
    }

    // Replacement happens after product payment, so when replacing a product - return cash should be calculated for customer if replaced products total cost is less than the initial product cost
    @Override
    public OrderDto replaceOrderProduct(
            UUID orderId,
            ProductEntity productEntity,
            ProductEntity replacementProduct,
            ReplaceOrderProductDto replaceWith
    ) {
        // Replacements can only happen if order is in status PAID
        if (productEntity.getId().equals(replaceWith.getProductId())) {
            throw new RuntimeException("Can not replace product with the same product");
        }
        OrderEntity orderEntity = orderData.getOrderById(orderId);

        Double replacedProductTotalAmount = 0.00;

        for (OrderProductEntity orderProductEntity : orderEntity.getOrderProducts()) {
            if (orderProductEntity.getProduct().getId().equals(productEntity.getId())) {
                OrderProductReplacedWith replacedWith = new OrderProductReplacedWith();
                replacedWith.setProductId(replaceWith.getProductId());
                replacedWith.setQuantity(replaceWith.getQuantity());
                orderProductEntity.setReplacedWith(replacedWith);

                replacedProductTotalAmount = orderProductEntity.getProduct().getPrice() * orderProductEntity.getQuantity();

                break;
            }
        }

        Double newTotalAmountAfterRemovingReplacedProduct = orderEntity.getAmounts().getTotal() - replacedProductTotalAmount;
        orderEntity.getAmounts().setTotal(newTotalAmountAfterRemovingReplacedProduct);


        List<Integer> existingProductIds = orderEntity.getOrderProducts().stream()
                .map((orderProductEntity) -> orderProductEntity.getProduct().getId()).toList();

        if (existingProductIds.contains(replacementProduct.getId())) {
            for (OrderProductEntity orderProductEntity : orderEntity.getOrderProducts()) {
                if (replacementProduct.getId().equals(orderProductEntity.getProduct().getId())) {
                    orderProductEntity.addQuantity();
                }
            }
        } else {
            OrderProductEntity orderProductEntity = new OrderProductEntity();
            orderProductEntity.setOrder(orderEntity);
            orderProductEntity.setProduct(replacementProduct);
            orderProductEntity.addQuantity();
            orderEntity.getOrderProducts().add(orderProductEntity);
        }

        Double replacementProductTotal = replaceWith.getQuantity() * replacementProduct.getPrice();
        Double finalTotal = orderEntity.getAmounts().getTotal() + replacementProductTotal;
        orderEntity.getAmounts().setTotal(finalTotal);

        repository.save(orderEntity);

        return mapper.fromEntityToDto(orderEntity);
    }
}
