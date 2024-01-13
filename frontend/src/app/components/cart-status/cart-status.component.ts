import { Component, OnInit } from '@angular/core';
import {CartService} from "../../services/cart.service";
import {AuthenticationService} from "../../services/authentication.service";
import {UserService} from "../../services/user.service";
import {environment} from "../../../environments/environment";

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
  uploadUrl: string = environment.uploadUrl;

  constructor(private cartService: CartService, private authenticationService:AuthenticationService, private userService: UserService) { }

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

  file(event) {
    let elem = event.target;
    if(elem.files.length > 0) {
      let formData = new FormData();
      formData.append('file', elem.files[0]);
      this.userService.uploadFile(formData, this.firstName!).subscribe(data => {
        window.location.reload();
      });
    }
  }

  logout() {
    this.authenticationService.logout();
  }
}
