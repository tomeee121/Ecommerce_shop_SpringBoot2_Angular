import {Customer} from "./customer";
import {Address} from "./address";
import {Order} from "./order";
import {OrderItem} from "./order-item";

export class Purchase {
  private customer: Customer;
  private shippingAddress: Address;
  private billingAddress: Address;
  private order: Order;
  private orderItems: OrderItem[];
  private orderTrackingNumber: String;


  constructor(customer: Customer, shippingAddress: Address, billingAddress: Address, order: Order, orderItems: OrderItem[], orderTrackingNumber: String) {
    this.customer = customer;
    this.shippingAddress = shippingAddress;
    this.billingAddress = billingAddress;
    this.order = order;
    this.orderItems = orderItems;
    this.orderTrackingNumber = orderTrackingNumber;
  }
}
