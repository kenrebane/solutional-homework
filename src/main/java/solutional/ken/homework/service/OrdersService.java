package solutional.ken.homework.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutional.ken.homework.dto.OrderDto;
import solutional.ken.homework.entity.OrderEntity;
import solutional.ken.homework.repository.OrdersRepository;
import solutional.ken.homework.mapper.OrderMapper;
import solutional.ken.homework.factory.OrderFactory;

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
}