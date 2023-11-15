package solutional.ken.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutional.ken.homework.entity.OrderEntity;

import java.util.UUID;

public interface OrdersRepository extends JpaRepository<OrderEntity, UUID> {
}
