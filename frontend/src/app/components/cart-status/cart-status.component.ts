import { Component, OnInit } from '@angular/core';
import {CartService} from "../../services/cart.service";
import {AuthenticationService} from "../../services/authentication.service";

@Component({
  selector: 'app-cart-status',
  templateUrl: './cart-status.component.html',
  styleUrls: ['./cart-status.component.css']
})
export class CartStatusComponent implements OnInit {

  cartItemsQuantity: number = 0;
  cartItemsAmountOfMoney: number = 0;
  isLoggedIn: boolean = false;
  firstName: string | undefined;
  storage: Storage = sessionStorage;

  constructor(private cartService: CartService, private authenticationService:AuthenticationService) { }

  ngOnInit(): void {
    this.subscribeQuantityDataForCartItem();
    this.subscribeAmountDataForCartItem();

    this.authenticationService.isLoggedIn();
    this.authenticationService.loginBehaviourSubject.subscribe(data => {this.isLoggedIn = data})
    this.authenticationService.nameBehaviourSubject.subscribe(data => this.firstName = data);

  }

  subscribeQuantityDataForCartItem(){
    this.cartService.totalQuantity.subscribe(data => this.cartItemsQuantity=data);
  }
  subscribeAmountDataForCartItem(){
    this.cartService.totalPrice.subscribe(data2 => this.cartItemsAmountOfMoney=data2);
  }


  logout() {
    this.authenticationService.logout();
  }
}