package solutional.ken.homework.dto;

import lombok.Data;

@Data
public class UpdateOrderProductQuantityOrReplaceOrderProductDto {
    private Integer quantity;
    private ReplaceOrderProductDto replaceWith;
}
