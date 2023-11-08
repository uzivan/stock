package com.example.stock.controllers;

import com.example.stock.dto.OrderDto;
import com.example.stock.dto_validation.groups.OnCreate;
import com.example.stock.dto_validation.groups.OnUpdate;
import com.example.stock.entities.Order;
import com.example.stock.entities.Product;
import com.example.stock.entities.User;
import com.example.stock.services.OrderService;
import com.example.stock.services.ProductService;
import com.example.stock.services.UserService;
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
@RequestMapping("/orders")
public class OrderController {
    private static final Class<OnCreate> CREATE_OPTION = OnCreate.class;
    private static final Class<OnUpdate> UPDATE_OPTION = OnUpdate.class;
    private static final String NO_SUCH_ORDER_MESSAGE = "no order with such ID is present";

    private static final String NO_SUCH_PIZZA_MESSAGE = "no such pizza with provided ID: ";
    private static final String INVALID_ID_MESSAGE = "invalid ID; must be more than 0";
    private static final String NO_PROPERTIES_TO_UPDATE_MESSAGE = "no properties to update in request body";
    private static final String NO_SUCH_INGREDIENT_MESSAGE = "no such ingredient with provided ID: ";
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @GetMapping
    public ResponseEntity<List<Order>> showAll(){
        return ResponseEntity.ok(orderService.findAll());
    }
    @PostMapping(value = "/new")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDto orderDto){
        validateOrderDto(orderDto, UPDATE_OPTION);
        Order order = mapToOrder(orderDto);
        orderService.save(order);
        return ResponseEntity.ok(order);
    }
    @GetMapping(value = "/{id}")
    private ResponseEntity<Order> getOrder(@PathVariable("id") int id){
        validateOrderId(id);
        return orderService.findById(id).map(ResponseEntity::ok).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_SUCH_ORDER_MESSAGE));
    }
    @PutMapping(value="/{id}")
    private ResponseEntity<Order> updateOrder(@PathVariable("id") int id, @RequestBody OrderDto orderDto){
        validateOrderId(id);
        validateOrderDto(orderDto, UPDATE_OPTION);
        Order order = checkForPresentAndGetOrder(id);
        updateOrders(order, orderDto);
        orderService.save(order);
        return ResponseEntity.ok(order);
    }
    @DeleteMapping(value="{/id}")
    private ResponseEntity<Order> deleteOrder(@PathVariable("id") int id){
        validateOrderId(id);
        Order order = checkForPresentAndGetOrder(id);
        orderService.delete(order);
        return ResponseEntity.ok().build();
    }
    private void validateOrderDto(OrderDto orderDto, Class<?> option) {
        if (isNull(orderDto)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    NO_PROPERTIES_TO_UPDATE_MESSAGE);
        }

        if (isNull(option) || !option.equals(UPDATE_OPTION)) {
            option = CREATE_OPTION;
        }

        Set<ConstraintViolation<OrderDto>> violations = validator.validate(orderDto, option);

        if (!violations.isEmpty()) {
            StringBuilder str = new StringBuilder();
            for (ConstraintViolation<OrderDto> violation : violations) {
                str.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
            }
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, str.toString());
        }
    }
    private void validateOrderId(Integer orderId) {
        if (isNull(orderId) || orderId < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_ID_MESSAGE);
        }
    }

    private Order mapToOrder(OrderDto orderDto){
        User user = userService.findByUsername("ivan");///тут надо потом пофиксить!!!!!!!!!!!!
        Order order = new Order();
        order.setStatus(orderDto.getStatus());
        order.setTime(orderDto.getOrderedTime());
        order.setUser(user);
        order.setProducts(dtoProductMapToList(orderDto.getProductId()));
        return order;
    }
    private List<Product> dtoProductMapToList(List<Integer> productId){
        List<Product> products = new ArrayList<>();
        for(int i = 0;i< productId.size();i++){
            Product product = checkForPresenceAndGetProduct(productId.get(i));
            products.add(product);
        }
        return products;
    }
    private void updateOrders(Order editable, OrderDto changes) {
        if(nonNull(changes.getOrderedTime())){
            editable.setTime(changes.getOrderedTime());
        }
        if(nonNull(changes.getStatus())){
            editable.setStatus(changes.getStatus());
        }
        if(nonNull(changes.getProductId())){
            List<Product> products = dtoProductMapToList(changes.getProductId());
            editable.setProducts(products);
        }
    }
    private Product checkForPresenceAndGetProduct(Integer id){
        return productService.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_SUCH_PIZZA_MESSAGE + id));
    }
    private Order checkForPresentAndGetOrder(Integer id){
        return orderService.findById(id).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_SUCH_ORDER_MESSAGE));
    }
}
