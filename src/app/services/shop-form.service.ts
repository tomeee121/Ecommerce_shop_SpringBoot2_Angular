import { Injectable } from '@angular/core';
import {map, Observable, of} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Country} from "../common/country";
import {State} from "../common/state";


@Injectable({
  providedIn: 'root'
})
export class ShopFormService {

  baseStateUrl: string = 'http://localhost:8080/api/states';
  baseCountryUrl: string = 'http://localhost:8080/api/countries';
  chosenCountryCode: string = 'PL';

  constructor(private httpClient: HttpClient) { }

  getCountries(): Observable<Country[]>{
    return this.httpClient.get<countriesResponse>(this.baseCountryUrl).pipe(map(data => data._embedded.countries));
  }

  getStatesForGivenCountryCode(codeToSearch: string): Observable<State[]>{

    const searchUrl = `${this.baseStateUrl}/search/findStatesByCountryCode?code=${codeToSearch}`;

    return this.httpClient.get<statesResponse>(searchUrl)
      .pipe(map(data => data._embedded.states));
  }

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

  getStates(): Observable<State[]>{
    return this.httpClient.get<statesResponse>(this.baseStateUrl)
      .pipe(map(data => data._embedded.states));
  }
}

interface countriesResponse {
  _embedded: {
    countries: Country[];
  }
}

interface statesResponse {
  _embedded: {
    states: State[];
  }
}
