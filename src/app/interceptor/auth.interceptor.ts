import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthenticationService} from "../services/authentication.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService) {}

  intercept(httpRequest: HttpRequest<unknown>, httpHandler: HttpHandler): Observable<HttpEvent<any>> {
    //endpointy publiczne
    if(httpRequest.url.includes(`${this.authenticationService.host}/customer/login`)){
      return httpHandler.handle(httpRequest);
    }

    if(httpRequest.url.includes(`${this.authenticationService.host}/customer/register`)){
      return httpHandler.handle(httpRequest);
    }

    if(httpRequest.url.includes(`${this.authenticationService.host}/customer/resetPassword`)){
      return httpHandler.handle(httpRequest);
    }
    if(httpRequest.url.includes(`${this.authenticationService.host}/api`)){
      return httpHandler.handle(httpRequest);
    }
    if(httpRequest.url.includes(`${this.authenticationService.host}/checkout`)){
      return httpHandler.handle(httpRequest);
    }
    this.authenticationService.loadToken();
    const token = this.authenticationService.getToken();
    //w przypakdu gdy endpoint powinien zostac zabezpieczony request jest klonowany i dopuszcozny do back-endu z tokenem
    const request = httpRequest.clone({setHeaders: {Authorization: `Bearer ${token}` }});
    return httpHandler.handle(request);
  }
}
