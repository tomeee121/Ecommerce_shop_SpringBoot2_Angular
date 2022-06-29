import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {Customer} from "../common/customer";
import {User} from "../common/user";
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private host = environment.apiCustomerUrl;
  private token: any;
  private loggedInUsername: any;
  private jwtHelper = new JwtHelperService();

  constructor(private http: HttpClient) { }

  login(user: User): Observable<HttpResponse<User> | HttpErrorResponse>{
    // @ts-ignore
    return this.http.post<HttpResponse<User> | HttpErrorResponse>(`${this.host}/customer/login`, user, {observe: "response"});
  }

  register(user: User): Observable<HttpResponse<User> | HttpErrorResponse>{
    return this.http.post<HttpResponse<User> | HttpErrorResponse>(`${this.host}/customer/register`, user);
  }

  logout(): void{
    this.token = null;
    this.loggedInUsername = null;
    localStorage.removeItem('user');
    localStorage.removeItem('users');
    localStorage.removeItem('token');
  }

  saveToken(token: string): void{
    this.token = token;
    localStorage.setItem('token', token);
  }

  addUserToLocalCache(user: User): void{
    localStorage.setItem('user', JSON.stringify(user));
  }

  getUserFromLocalCache(): User {
    // @ts-ignore
    return JSON.parse(localStorage.getItem('user'));
  }

  loadToken(): void {
    // @ts-ignore
    this.token = JSON.parse(localStorage.getItem('token'));
  }

  getToken(): any {
    // @ts-ignore
    return JSON.parse(localStorage.getItem('token'));
  }

  public isLoggedIn(): boolean{
    this.loadToken();
    if(this.token != null && this.token !== ''){
      if(this.jwtHelper.decodeToken(this.token).sub != null || ''){
        if(!this.jwtHelper.isTokenExpired(this.token)){
          this.loggedInUsername = this.jwtHelper.decodeToken(this.token).sub;
          return true;
        }
      }

    }
    else{
      this.logout();
    }
    return false;
  }
}
