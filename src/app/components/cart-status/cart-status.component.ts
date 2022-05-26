import { Component, OnInit } from '@angular/core';
import {CartService} from "../../services/cart.service";

@Component({
  selector: 'app-cart-status',
  templateUrl: './cart-status.component.html',
  styleUrls: ['./cart-status.component.css']
})
export class CartStatusComponent implements OnInit {

  cartItemsQuantity: number = 0;
  cartItemsAmountOfMoney: number = 0;

  constructor(private cartService: CartService) { }

  ngOnInit(): void {
    this.subscribeDataForCartItem();
  }

  subscribeDataForCartItem(){
    this.cartService.totalQuantity.subscribe(data => this.cartItemsQuantity=data);
    this.cartService.totalPrice.subscribe(data => this.cartItemsAmountOfMoney=data);
  }



}
