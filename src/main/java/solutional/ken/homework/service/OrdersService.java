package solutional.ken.homework.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutional.ken.homework.dto.OrderDto;
import solutional.ken.homework.dto.OrderStatusDto;
import solutional.ken.homework.entity.OrderEntity;
import solutional.ken.homework.exception.OrderNotFoundException;
import solutional.ken.homework.repository.OrdersRepository;
import solutional.ken.homework.mapper.OrderMapper;
import solutional.ken.homework.factory.OrderFactory;

import java.util.UUID;

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
        repository.save(orderEntity);
        return mapper.fromEntityToDto(orderEntity);
    }

    private OrderEntity getOrderEntityById(
            UUID orderId
    ) {
        return repository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }
}
