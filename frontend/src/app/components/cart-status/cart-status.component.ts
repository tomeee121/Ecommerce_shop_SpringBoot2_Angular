import { Component, OnInit } from '@angular/core';
import {CartService} from "../../services/cart.service";
import {AuthenticationService} from "../../services/authentication.service";
import {UserService} from "../../services/user.service";
import {environment} from "../../../environments/environment";
import {NotificationService} from "../../services/notification.service";
import {NotificationType} from "../../enum/notification-type.enum";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {CustomHttpResponse} from "../../common/custom-http-response";

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

  constructor(private cartService: CartService, private authenticationService:AuthenticationService, private userService: UserService,
              private notificationService:NotificationService) { }

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
      this.userService.uploadFile(formData, this.firstName!).subscribe((response: CustomHttpResponse) => {
          this.notificationService.notify(NotificationType.SUCCESS, 'Successfully uploaded a photo!');
          window.location.reload();
      },
      (error: HttpErrorResponse) => this.notificationService.notify(NotificationType.ERROR, error.message));
    }
  }

  logout() {
    this.authenticationService.logout();
  }
}
