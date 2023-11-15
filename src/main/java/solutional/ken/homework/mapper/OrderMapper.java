package solutional.ken.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import solutional.ken.homework.dto.OrderDto;
import solutional.ken.homework.dto.OrderProductDto;
import solutional.ken.homework.entity.OrderEntity;
import solutional.ken.homework.entity.OrderProductEntity;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "orderProducts", target = "products", qualifiedByName = "orderProductEntityToDto")
    @Mapping(source = "amounts.discount", target = "amounts.discount", numberFormat = "0.00")
    @Mapping(source = "amounts.paid", target = "amounts.paid", numberFormat = "0.00")
    @Mapping(source = "amounts.returns", target = "amounts.returns", numberFormat = "0.00")
    @Mapping(source = "amounts.total", target = "amounts.total", numberFormat = "0.00")
    OrderDto fromEntityToDto(OrderEntity orderEntity);

    @Named("orderProductEntityToDto")
    default OrderProductDto orderProductEntityToDto(OrderProductEntity orderProductEntity) {
        OrderProductDto orderProductDto = new OrderProductDto();
        orderProductDto.setId(orderProductEntity.getId().toString());
        orderProductDto.setProductId(orderProductEntity.getProduct().getId());
        orderProductDto.setName(orderProductEntity.getProduct().getName());
        orderProductDto.setPrice(orderProductEntity.getProduct().getPrice().toString());
        orderProductDto.setQuantity(orderProductEntity.getQuantity());
        return orderProductDto;
    }
}
