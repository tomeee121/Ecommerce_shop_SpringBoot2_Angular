import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/authentication.service";
import {NotificationService} from "../../services/notification.service";
import {Subscription} from "rxjs";
import {User} from "../../common/user";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {NotificationType} from "../../enum/notification-type.enum";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit,OnDestroy {

  private subscriptions: Subscription[] = [];

  constructor(private router:Router, private authenticationService:AuthenticationService, private notificationService:NotificationService) { }

  ngOnInit(): void {
    if(this.authenticationService.isLoggedIn()){
      this.router.navigateByUrl("/user/management")
    }
    else{
      this.router.navigateByUrl("/login")

    }
  }

  public onLogin(user: User){
    // @ts-ignore
    this.subscriptions.push(this.authenticationService.login(user).subscribe((response: HttpResponse<User>) =>{
      const token = response.headers.get('Jwt-Token');
      // @ts-ignore
      this.authenticationService.saveToken(token);
      // @ts-ignore
      this.authenticationService.addUserToLocalCache(response.body);
      this.router.navigate(['/user/management']);
    },
      // @ts-ignore
      (error: HttpErrorResponse) => this.notificationService.notify(NotificationType.ERROR, error.message)));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

}
