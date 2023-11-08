package com.example.stock.controllers;

import com.example.stock.dto.ProductDto;
import com.example.stock.dto_validation.groups.OnCreate;
import com.example.stock.dto_validation.groups.OnUpdate;
import com.example.stock.entities.Product;
import com.example.stock.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/products")
public class ProductController {
    private static final Class<OnCreate> CREATE_OPTION = OnCreate.class;
    private static final Class<OnUpdate> UPDATE_OPTION = OnUpdate.class;
    private static final String NO_SUCH_PRODUCT_MESSAGE = "no product with such ID is present";
    private static final String INVALID_ID_MESSAGE = "invalid ID; must be more than 0";
    private static final String NO_PROPERTIES_TO_UPDATE_MESSAGE = "no properties to update in request body";

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> showAll() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") int id) {
        validateProductId(id);
        return productService.findById(id).map(ResponseEntity::ok).orElseThrow
                (() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_SUCH_PRODUCT_MESSAGE));
    }
    @PostMapping(value = "/new")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto){
        validateProductDto(productDto, CREATE_OPTION);
        Product product = mapToProduct(productDto);
        return ResponseEntity.ok(productService.save(product));
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") int id, @RequestBody ProductDto productDto){
        validateProductId(id);
        validateProductDto(productDto, UPDATE_OPTION);
        Product product = checkForPresenceAndGetProduct(id);
        updateProduct(product, productDto);
        productService.save(product);
        return ResponseEntity.ok(product);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") int id){
        validateProductId(id);
        Product deleteproduct = checkForPresenceAndGetProduct(id);
        productService.delete(deleteproduct);
        return ResponseEntity.ok().build();
    }
    private void validateProductDto(ProductDto productDto, Class<?> option) {
        if (isNull(productDto)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    NO_PROPERTIES_TO_UPDATE_MESSAGE);
        }

        if (isNull(option) || !option.equals(UPDATE_OPTION)) {
            option = CREATE_OPTION;
        }

        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto, option);

        if (!violations.isEmpty()) {
            StringBuilder str = new StringBuilder();
            for (ConstraintViolation<ProductDto> violation : violations) {
                str.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
            }
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, str.toString());
        }
    }
    private void validateProductId(Integer pizzaId) {
        if (isNull(pizzaId) || pizzaId < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_ID_MESSAGE);
        }
    }
    private Product mapToProduct(ProductDto productDto){
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        return product;
    }

    private Product checkForPresenceAndGetProduct(Integer id){
        return productService.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_SUCH_PRODUCT_MESSAGE));
    }

    private void updateProduct(Product editable, ProductDto changes) {
        if(nonNull(changes.getName())){
            editable.setName(changes.getName());
        }
        if(nonNull(changes.getPrice())){
            editable.setPrice(changes.getPrice());
        }
    }
}
