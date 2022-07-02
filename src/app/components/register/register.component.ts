import {Component, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/authentication.service";
import {NotificationService} from "../../services/notification.service";
import {User} from "../../common/user";
import {HttpErrorResponse} from "@angular/common/http";
import {NotificationType} from "../../enum/notification-type.enum";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  private subscriptions: Subscription[] = [];
  firstName: string ='';
  lastName: string ='';
  username: string ='';
  email: string ='';

  constructor(private router:Router, private authenticationService:AuthenticationService, private notificationService:NotificationService) { }

  ngOnInit(): void {
    if(this.authenticationService.isLoggedIn()){
      this.router.navigateByUrl("/user/management")
    }
    else{
      this.router.navigateByUrl("/login")

    }
  }

  public onRegister(user: User){
    // @ts-ignore
    this.subscriptions.push(this.authenticationService.register(user).subscribe((response: User) =>{
        this.notificationService.notify(NotificationType.SUCCESS, 'Successfully registered an account for you ' + response.firstName+'! Check your email for the password')
        this.router.navigateByUrl("/login");
      },
      // @ts-ignore
      (error: HttpErrorResponse) => this.notificationService.notify(NotificationType.ERROR, error.error.message)));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
