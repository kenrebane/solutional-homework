package solutional.ken.homework.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutional.ken.homework.dto.OrderStatusDto;
import solutional.ken.homework.entity.OrderEntity;
import solutional.ken.homework.enums.OrderStatus;
import solutional.ken.homework.exception.OrderNotFoundException;
import solutional.ken.homework.repository.OrdersRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderDataService implements OrderData {
    private OrdersRepository repository;

    @Override
    public OrderEntity createOrderWithStatus(OrderStatus status) {
        OrderEntity order = new OrderEntity();
        order.setStatus(status);
        repository.save(order);
        return order;
    }

    @Override
    public OrderEntity getOrderById(UUID orderId) {
        return repository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public void updateOrderStatus(
            OrderEntity order,
            OrderStatusDto dto
    ) {
        order.setStatus(dto.getStatus());
        repository.save(order);
    }

    @Override
    public void markOrderAsPaid(OrderEntity order) {
        order.getAmounts().setPaid(order.getAmounts().getTotal());
        repository.save(order);
    }
}
