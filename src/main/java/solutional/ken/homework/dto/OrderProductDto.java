package solutional.ken.homework.dto;

import lombok.Data;

@Data
public class OrderProductDto {
    private String id;
    private Integer productId;
    private String name;
    private String price;
    private Integer quantity;
    private ReplaceOrderProductDto replacedWith;
}
