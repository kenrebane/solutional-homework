package solutional.ken.homework.entity;

import jakarta.persistence.*;
import lombok.Data;
import solutional.ken.homework.enums.OrderStatus;

import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
