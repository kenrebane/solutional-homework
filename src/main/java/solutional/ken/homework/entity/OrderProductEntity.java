package solutional.ken.homework.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "orders_products")
public class OrderProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name="order_id", nullable=false)
    private OrderEntity order;
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private ProductEntity product;
    private Integer quantity = 0;

    public void addQuantity() {
        this.quantity++;
    }
}
