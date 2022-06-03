package pl.project.wwsis.ecommerceshop.service;

import pl.project.wwsis.ecommerceshop.DTO.Purchase;
import pl.project.wwsis.ecommerceshop.DTO.PurchaseResponse;

public interface CheckoutService {
    Purchase placeOrder(Purchase purchase);
}
