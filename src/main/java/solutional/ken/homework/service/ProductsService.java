package solutional.ken.homework.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutional.ken.homework.dto.ProductDto;
import solutional.ken.homework.entity.ProductEntity;
import solutional.ken.homework.repository.ProductsRepository;
import solutional.ken.homework.mapper.ProductMapper;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductsService implements Products {
    private ProductsRepository repository;
    private ProductMapper mapper;

    @Override
    public List<ProductDto> getAllProducts() {
        List<ProductEntity> productEntityList = repository.findAll();
        return mapper.fromEntityListToDtoList(productEntityList);
    }
}
