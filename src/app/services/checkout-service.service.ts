import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Customer} from "../common/customer";
import {Address} from "../common/address";
import {Order} from "../common/order";
import {OrderItem} from "../common/order-item";
import {NotificationService} from "./notification.service";
import {NotificationType} from "../enum/notification-type.enum";

@Injectable({
  providedIn: 'root'
})
export class CheckoutServiceService {

  private purchaseUrl: string = 'http://localhost:8080/api/checkout/purchase';

  constructor(private httpClient: HttpClient, private notificationService: NotificationService) { }

  placeOrder(purchase: Purchase): Observable<Purchase>{
    return this.httpClient.post<Purchase>(this.purchaseUrl,purchase);
    this.notificationService.notify(NotificationType.SUCCESS, 'Order has been placed!')
  }
}

interface Purchase {
  customer: Customer;
  shippingAddress: Address;
  billingAddress: Address;
  order: Order;
  orderItems: OrderItem[];
  orderTrackingNumber: String;
}
