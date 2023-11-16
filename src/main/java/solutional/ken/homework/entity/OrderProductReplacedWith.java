package solutional.ken.homework.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class OrderProductReplacedWith {
    @Column(name = "order_product_replaced_by_product_id")
    private Integer productId;
    @Column(name = "order_product_replaced_by_product_quantity")
    private Integer quantity;
}
