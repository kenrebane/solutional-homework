package solutional.ken.homework.mapper;

import org.mapstruct.Mapper;
import solutional.ken.homework.dto.OrderDto;
import solutional.ken.homework.entity.OrderEntity;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto fromEntityToDto(OrderEntity orderEntity);
}
