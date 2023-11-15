package solutional.ken.homework.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import solutional.ken.homework.dto.OrderDto;
import solutional.ken.homework.service.Orders;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class OrdersController {
    private Orders orders;

    @PostMapping("/api/orders")
    public OrderDto createNewOrder() {
        return orders.createNewOrder();
    }

    @GetMapping("/api/orders/{orderId}")
    public OrderDto getOrderDetails(@PathVariable UUID orderId) {
        return orders.getOrderDetails(orderId);
    }
}
