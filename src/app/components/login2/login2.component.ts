import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/authentication.service";
import {NotificationService} from "../../services/notification.service";
import {Subscription} from "rxjs";
import {User} from "../../common/user";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {NotificationType} from "../../enum/notification-type.enum";

@Component({
  selector: 'app-login2',
  templateUrl: './login2.component.html',
  styleUrls: ['./login2.component.css']
})
export class Login2Component implements OnInit,OnDestroy {

  private subscriptions: Subscription[] = [];
  username: string ='';
  password: string ='';

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
    console.log('kk')
    // @ts-ignore
    this.subscriptions.push(this.authenticationService.login(user).subscribe((response: HttpResponse<User>) =>{
        console.log('back to angular')

        this.notificationService.notify(NotificationType.SUCCESS, "Logged in successfully!")
        const token = response.headers.get('Jwt-Token');
        // @ts-ignore
        this.authenticationService.saveToken(token);
        // @ts-ignore
        this.authenticationService.addUserToLocalCache(response.body);
        this.router.navigateByUrl("/user/management");
        this.authenticationService.loginBehaviourSubject.next(true);
      },
      (error: HttpErrorResponse) => this.notificationService.notify(NotificationType.ERROR, error.error.message)));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

}
