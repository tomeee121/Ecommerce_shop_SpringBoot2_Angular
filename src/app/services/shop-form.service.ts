import { Injectable } from '@angular/core';
import {Observable, of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ShopFormService {

  constructor() { }

  getCreditCardMonths(monthOfStart: number): Observable<number[]>{
    let data: number[] = [];

    for(let i = monthOfStart; i <= 12; i++){
      data.push(i);
    }

    return of(data);
  }

  getCreditCardYears(){
    let data: number[] = [];

    let years: number = new Date().getFullYear();
    let yearsPlus10: number = years+10;

    for(let i = years; i<= yearsPlus10; i++){
      data.push(i);
    }
    return of(data);
  }
}
