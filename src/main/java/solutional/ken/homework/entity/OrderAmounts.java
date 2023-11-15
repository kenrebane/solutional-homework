package solutional.ken.homework.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class OrderAmounts {
    private Double discount = 0.00;
    private Double paid = 0.00;
    private Double returns = 0.00;
    private Double total = 0.00;
}
