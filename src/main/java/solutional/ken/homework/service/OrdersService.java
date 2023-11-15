package solutional.ken.homework.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutional.ken.homework.dto.OrderDto;
import solutional.ken.homework.dto.OrderProductDto;
import solutional.ken.homework.dto.OrderProductQuantityDto;
import solutional.ken.homework.dto.OrderStatusDto;
import solutional.ken.homework.entity.OrderEntity;
import solutional.ken.homework.entity.OrderProductEntity;
import solutional.ken.homework.entity.ProductEntity;
import solutional.ken.homework.exception.OrderNotFoundException;
import solutional.ken.homework.repository.OrdersRepository;
import solutional.ken.homework.mapper.OrderMapper;
import solutional.ken.homework.factory.OrderFactory;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrdersService implements Orders {
    private OrdersRepository repository;
    private OrderMapper mapper;
    private OrderFactory factory;

    @Override
    public OrderDto createNewOrder() {
        OrderEntity orderEntity = factory.createNewOrderEntity();
        repository.save(orderEntity);
        return mapper.fromEntityToDto(orderEntity);
    }

    @Override
    public OrderDto getOrderDetails(
            UUID orderId
    ) {
        OrderEntity orderEntity = this.getOrderEntityById(orderId);
        return mapper.fromEntityToDto(orderEntity);
    }

    @Override
    public OrderDto updateOrderStatus(
            UUID orderId,
            OrderStatusDto orderStatusDto
    ) {
        OrderEntity orderEntity = this.getOrderEntityById(orderId);
        orderEntity.setStatus(orderStatusDto.getStatus());
        orderEntity.getAmounts().setPaid(orderEntity.getAmounts().getTotal());
        repository.save(orderEntity);
        return mapper.fromEntityToDto(orderEntity);
    }

    @Override
    public OrderDto addProductsToOrder(
            UUID orderId,
            List<ProductEntity> productEntityList
    ) {
        OrderEntity orderEntity = this.getOrderEntityById(orderId);
        List<Integer> existingProductIds = orderEntity.getOrderProducts().stream()
                .map((orderProductEntity) -> orderProductEntity.getProduct().getId()).toList();

        for (ProductEntity productEntity:productEntityList) {
            if (existingProductIds.contains(productEntity.getId())) {
                for (OrderProductEntity orderProductEntity:orderEntity.getOrderProducts()) {
                    if (productEntity.getId().equals(orderProductEntity.getProduct().getId())) {
                        orderProductEntity.addQuantity();
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

        for (OrderProductEntity orderProductEntity:orderEntity.getOrderProducts()) {
            Double updatedTotal = orderEntity.getAmounts().getTotal() + orderProductEntity.getProduct().getPrice();
            orderEntity.getAmounts().setTotal(updatedTotal);
        }

        repository.save(orderEntity);

        return mapper.fromEntityToDto(orderEntity);
    }

    @Override
    public List<OrderProductDto> getOrderProducts(UUID orderId) {
        OrderEntity orderEntity = this.getOrderEntityById(orderId);
        OrderDto orderDto = mapper.fromEntityToDto(orderEntity);
        return orderDto.getProducts();
    }

    @Override
    public OrderDto updateOrderProductQuantity(
            UUID orderId,
            ProductEntity productEntity,
            OrderProductQuantityDto dto
    ) {
        OrderEntity orderEntity = this.getOrderEntityById(orderId);

        Integer currentQuantity = 0;

        for (OrderProductEntity orderProductEntity:orderEntity.getOrderProducts()) {
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

    private OrderEntity getOrderEntityById(
            UUID orderId
    ) {
        return repository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }
}
