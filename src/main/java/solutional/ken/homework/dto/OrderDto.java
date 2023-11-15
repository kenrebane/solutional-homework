package solutional.ken.homework.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    private String id;
    private String status;
    private List<OrderProductDto> products = new ArrayList<>();
    private OrderAmountsDto amounts;
}
