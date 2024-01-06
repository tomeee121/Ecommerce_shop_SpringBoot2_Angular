package pl.project.wwsis.ecommerceshop.shop_nonLogged.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.DTO.Purchase;
import pl.project.wwsis.ecommerceshop.shop_nonLogged.service.CheckoutService;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private CheckoutService checkoutService;

    @Autowired
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/purchase")
    public Purchase placeOrder(@RequestBody Purchase purchase){
        return checkoutService.placeOrder(purchase);
    }
}
