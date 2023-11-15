package solutional.ken.homework.mapper;

import org.mapstruct.Mapper;
import solutional.ken.homework.dto.ProductDto;
import solutional.ken.homework.entity.ProductEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    List<ProductDto> fromEntityListToDtoList(List<ProductEntity> productEntityList);
}
