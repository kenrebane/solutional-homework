package solutional.ken.homework.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import solutional.ken.homework.dto.ProductDto;
import solutional.ken.homework.service.Products;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProductsController {
    private Products products;

    @GetMapping("/api/products")
    public List<ProductDto> getAllProducts() {
        return products.getAllProducts();
    }
}
