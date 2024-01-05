import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
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

  constructor(private userService:UserService, private authenticationService:AuthenticationService, private notificationService:NotificationService) {}


  ngOnInit(): void {
    this.userEmail = this.authenticationService.getUserFromLocalCache().email;
    this.isAdmin = this.authenticationService.isAdmin();
    console.log(this.isAdmin);

    if(!this.isAdmin){
      // @ts-ignore
      this.shoppingHistory = this.subscriptions.push(this.userService.getShoppingHistory(this.userEmail, 0, 15)
        // @ts-ignore
        .subscribe((data: OrderHistoryDTO[]) => {
          this.shoppingHistory=data;
          console.log(data)
          this.notificationService.notify(NotificationType.SUCCESS, 'Orders history has been loaded')
        },
      (errorResponse: HttpErrorResponse) =>
          this.notificationService.notify(NotificationType.ERROR, 'No history received')))
  }
  else{
    // @ts-ignore
      this.subscriptions.push(this.userService.getAllShoppingHistory().subscribe((data: OrderHistoryDTO[]) => {
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
    this.subscriptions.push(this.userService.deleteOrder(order_tracking_number).subscribe((response: HttpResponse<CustomHttpResponse>) => {
        // @ts-ignore
        this.notificationService.notify(NotificationType.SUCCESS, 'Successfully deleted order');
            window.location.reload();
        },
            (error: HttpErrorResponse) => this.notificationService.notify(NotificationType.ERROR, error.message)));

  }

  refresh() {
//     window.location.reload();
  }

  changeStatus(order_tracking_number: string, status: string) {
    console.log(status)
    // @ts-ignore
    this.subscriptions.push(this.userService.updateOrderStatus(order_tracking_number, status).subscribe((response: HttpResponse<CustomHttpResponse>) =>
        // @ts-ignore
        this.notificationService.notify(NotificationType.SUCCESS, 'Successfully updated order status'),
      (error: HttpErrorResponse) => this.notificationService.notify(NotificationType.ERROR, error.message)));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

}
