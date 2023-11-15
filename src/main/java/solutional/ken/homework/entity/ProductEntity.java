package solutional.ken.homework.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class ProductEntity {
    @Id
    private Integer id;
    private String name;
    private Double price;
}
