package solutional.ken.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutional.ken.homework.entity.ProductEntity;

public interface ProductsRepository extends JpaRepository<ProductEntity, Integer> {
}
