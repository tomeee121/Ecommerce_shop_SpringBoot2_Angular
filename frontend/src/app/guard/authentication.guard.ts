import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import { Observable } from 'rxjs';
import {AuthenticationService} from "../services/authentication.service";
import {NotificationService} from "../services/notification.service";
import {NotificationType} from "../enum/notification-type.enum";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationGuard implements CanActivate {

  constructor(private router: Router, private authenticationService: AuthenticationService, private notificationService: NotificationService) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
    return this.isUserLoggedIn();
  }

  isUserLoggedIn(): boolean{
    if(this.authenticationService.isLoggedIn()){
      return true
    }
    this.router.navigate(['/customer/login']);
    // @ts-ignore
    this.notificationService.notify(NotificationType.ERROR, 'You need to login.'.toUpperCase())
    return false;
  }

}
