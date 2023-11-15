package solutional.ken.homework.factory;

import org.springframework.stereotype.Component;
import solutional.ken.homework.entity.OrderEntity;
import solutional.ken.homework.enums.OrderStatus;

@Component
public class OrderFactory {
    public OrderEntity createNewOrderEntity() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setStatus(OrderStatus.NEW);
        return orderEntity;
    }
}
