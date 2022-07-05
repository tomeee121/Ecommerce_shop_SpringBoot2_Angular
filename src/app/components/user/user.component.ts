import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {AuthenticationService} from "../../services/authentication.service";
import {OrderHistoryDTO} from "../../common/OrderHistoryDTO";
import {NotificationService} from "../../services/notification.service";
import {NotificationType} from "../../enum/notification-type.enum";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {Subscription} from "rxjs";
import {CustomHttpResponse} from "../../common/custom-http-response";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit, OnDestroy {
  // @ts-ignore
  userEmail: string;
  shoppingHistory: OrderHistoryDTO[] = [];
  isAdmin: boolean = false;
  private subscriptions: Subscription[] = [];
  // storage: Storage = sessionStorage;

  constructor(private userService:UserService, private authenticationService:AuthenticationService, private notificationService:NotificationService) {}

  ngOnInit(): void {
    this.userEmail = this.authenticationService.getUserFromLocalCache().email;
    this.isAdmin = this.authenticationService.isAdmin();
    console.log(this.isAdmin);

    if(!this.isAdmin){
    // @ts-ignore
    this.shoppingHistory = this.subscriptions.push(this.userService.getShoppingHistory(this.userEmail, 0, 15)
      .subscribe((data: any) => {
          this.shoppingHistory = data;
          // this.storage.setItem('orders', JSON.stringify(data));
          console.log(data)
          this.notificationService.notify(NotificationType.SUCCESS, 'Orders history has been loaded')
        },
      (errorResponse: HttpErrorResponse) =>
          this.notificationService.notify(NotificationType.ERROR, 'No history received')))
  }
  else{
    this.subscriptions.push(this.userService.getAllShoppingHistory().subscribe((data: any) => {
      this.shoppingHistory = data;
        // this.storage.setItem('orders', JSON.stringify(data));
        console.log(data)
      this.notificationService.notify(NotificationType.SUCCESS, 'Orders history has been loaded')
    },
      (errorResponse: HttpErrorResponse) =>
        this.notificationService.notify(NotificationType.ERROR, 'No history received')))
    }
  }

  deleteOrder(order_tracking_number: string) {
    // @ts-ignore
    this.subscriptions.push(this.userService.deleteOrder(order_tracking_number).subscribe((response: HttpResponse<CustomHttpResponse>) =>
        // @ts-ignore
        this.notificationService.notify(NotificationType.SUCCESS, response.body?.message.toString()),
                            (error: HttpErrorResponse) => this.notificationService.notify(NotificationType.ERROR, error.message)));
    // let index: number = this.shoppingHistory.findIndex(tempOrder => tempOrder.order_tracking_number === order_tracking_number);
    // this.shoppingHistory.splice(index, 1);
    // @ts-ignore
    // this.shoppingHistory = JSON.parse(this.storage.getItem('orders'));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
