package pl.project.wwsis.ecommerceshop.shop_nonLogged.service;

import pl.project.wwsis.ecommerceshop.shop_nonLogged.DTO.Purchase;

public interface CheckoutService {
    Purchase placeOrder(Purchase purchase);
}
