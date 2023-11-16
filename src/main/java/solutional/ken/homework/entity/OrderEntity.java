package solutional.ken.homework.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import solutional.ken.homework.enums.OrderStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderProductEntity> orderProducts = new HashSet<>();
    @Embedded
    private OrderAmounts amounts = new OrderAmounts();
}
