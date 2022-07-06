import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpEvent, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../common/user";
import {environment} from "../../environments/environment";
import {CustomHttpResponse} from "../common/custom-http-response";
import {OrderHistoryDTO} from "../common/OrderHistoryDTO";
import {ProductCategory} from "../common/product-category";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private host: string = environment.apiCustomerUrl;
  private shoppingHistoryUrlBase: string = environment.shoppingHistoryUrlBase
  private allShoppingHistoryUrlBase: string = environment.allShoppingHistoryUrlBase
  private deleteOrderUrlBase: string = environment.deleteOrderUrlBase
  private customerUrl: string = environment.customerCustomUrl;


  constructor(private http: HttpClient) {
  }

  public getShoppingHistory(email: string, pageNumber: number, pageSize: number): Observable<OrderHistoryDTO[]| HttpErrorResponse> {
    const shoppingHistoryUrl = `${this.shoppingHistoryUrlBase}?email=${email}&pageNumber=${pageNumber}&pageSize=${pageSize}`;
    return this.http.get<OrderHistoryDTO[] | HttpErrorResponse>(shoppingHistoryUrl);
  }

  public getAllShoppingHistory(): Observable<OrderHistoryDTO[]| HttpErrorResponse> {
    const shoppingHistoryUrl = `${this.allShoppingHistoryUrlBase}`;
    return this.http.get<OrderHistoryDTO[] | HttpErrorResponse>(shoppingHistoryUrl);
  }

  public getUsers(): Observable<User[] | HttpErrorResponse> {
    return this.http.get<User[]>(`${this.host}/customer/list`);
  }

  public addUser(formData: FormData): Observable<User | HttpErrorResponse> {
    return this.http.post<User>(`${this.host}/customer/add`, formData);
  }

  public resetPassword(email: string): Observable<CustomHttpResponse | HttpErrorResponse> {
    return this.http.get<CustomHttpResponse>(`${this.host}/customer/resetPassword/${email}`);
  }

  public updateProfileImage(formData: FormData): Observable<HttpEvent<any> | HttpErrorResponse> {
    return this.http.post<User>(`${this.host}/customer/updateProfileImage`, formData,
      {
        observe: "events",
        reportProgress: true
      });
  }
  public updateOrderStatus(order_tracking_number: string, status: string): Observable<CustomHttpResponse | HttpErrorResponse>{
    return this.http.get<CustomHttpResponse>(`${this.customerUrl}/updateStatus?order_nr=${order_tracking_number}&status=${status}`);
  }

  public deleteUser(userId: number): Observable<CustomHttpResponse | HttpErrorResponse> {
    return this.http.delete<CustomHttpResponse>(`${this.host}/customer/delete/${userId}`);
  }

  public deleteOrder(order_tracking_number: string): Observable<CustomHttpResponse | HttpErrorResponse>{
    return this.http.delete<CustomHttpResponse>(`${this.deleteOrderUrlBase}?order_tracking_number=${order_tracking_number}`);
  }

  public addUsersToLocalCache(users: User[]): void {
    localStorage.setItem('users', JSON.stringify(users));
  }

  public getUsersFromLocalCache(): User[] {
    let users: User[] = [];
    if (localStorage.getItem('users')) {
      // @ts-ignore
      users = JSON.parse(localStorage.getItem('users'));
    }
    return users;
  }

  createUserFormData(loggedInUser: string, user: User, image: File): FormData {
    const formData = new FormData();
    formData.append('currentUsername', loggedInUser);
    formData.append('firstName', user.firstName);
    formData.append('lastName', user.lastName);
    formData.append('username', user.username);
    formData.append('email', user.email);
    formData.append('role', user.role);
    formData.append('isActive', JSON.stringify(user.isActive));
    formData.append('isNonLocked', JSON.stringify(user.isNotLocked));
    formData.append('imageUrl', image);
    return formData;
  }

}
