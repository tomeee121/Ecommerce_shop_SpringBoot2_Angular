import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {AuthenticationService} from "../../services/authentication.service";
import {OrderHistoryDTO} from "../../common/OrderHistoryDTO";
import {NotificationService} from "../../services/notification.service";
import {NotificationType} from "../../enum/notification-type.enum";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {error} from "@angular/compiler/src/util";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  // @ts-ignore
  userEmail: string;
  shoppingHistory: OrderHistoryDTO[] = [];
  isAdmin: boolean = false;

  constructor(private userService:UserService, private authenticationService:AuthenticationService, private notificationService:NotificationService) {}

  ngOnInit(): void {
    this.userEmail = this.authenticationService.getUserFromLocalCache().email;
    this.isAdmin = this.authenticationService.isAdmin();
    console.log(this.isAdmin);

    if(!this.isAdmin){
    // @ts-ignore
    this.shoppingHistory = this.userService.getShoppingHistory(this.userEmail, 0, 15)
      .subscribe((data: any) => {
          this.shoppingHistory = data;
          console.log(data)
          this.notificationService.notify(NotificationType.SUCCESS, 'Orders history has been loaded')
        }),
      (errorResponse: HttpErrorResponse) =>
          this.notificationService.notify(NotificationType.ERROR, 'No history received');
  }
  else{
    this.userService.getAllShoppingHistory().subscribe((data: any) => {
      this.shoppingHistory = data;
      console.log(data)
      this.notificationService.notify(NotificationType.SUCCESS, 'Orders history has been loaded')
    },
      (errorResponse: HttpErrorResponse) =>
        this.notificationService.notify(NotificationType.ERROR, 'No history received'))
    }
  }

}
