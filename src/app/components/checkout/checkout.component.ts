import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {CartService} from "../../services/cart.service";
import {ShopFormService} from "../../services/shop-form.service";
import {Country} from "../../common/country";
import {State} from "../../common/state";

// @ts-ignore
@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {

  // @ts-ignore
  checkoutFormGroup: FormGroup;
  totalQuantity: number = 0;
  totalPrice: number = 0;

  creditCardMonths: number[] = [];
  creditCardYears: number[] = [];
  countries: Country[] = [];
  states: State[] = [];

  constructor(private formBuilder: FormBuilder, private cartService: CartService, private shopFormService: ShopFormService) { }

  ngOnInit(): void {
    this.checkoutFormGroup = this.formBuilder.group({
      customer: this.formBuilder.group({
        firstName: [''],
        lastName: [''],
        email: ['']
      }),
      shippingAdress: this.formBuilder.group({
        street: [''],
        city: [''],
        state: [''],
        country: [''],
        zipCode: ['']
      }),
      billingAdress: this.formBuilder.group({
        street: [''],
        city: [''],
        state: [''],
        country: [''],
        zipCode: ['']
      }),
      creditCard: this.formBuilder.group({
        cardType: [''],
        nameOnCard: [''],
        cardNumber: [''],
        securityCode: [''],
        expirationMonth: [''],
        expirationYear: ['']
      })
    })
    this.getCreditCardYears();
    this.checkExpirationYearChosen();
    this.shopFormService.getCountries().subscribe(data => this.countries = data);
    this.getStatesOnInit();
  }

  onSubmit(){
    console.log(this.checkoutFormGroup.get('customer')?.value.email);
  }

  // @ts-ignore
  makeBillingAdressFieldsEqualToShippingAdress(event){
    if(event.target.checked){
    // @ts-ignore
      this.checkoutFormGroup.controls.billingAdress.setValue(this.checkoutFormGroup.controls.shippingAdress.value);}
    else{
      // @ts-ignore
      this.checkoutFormGroup.controls.billingAdress.reset();
    }
  }

  //if during the complement of this component expiration year of credit card is current year do not show previous months to choose later in a drop-down list
  checkExpirationYearChosen(){
    // let expirationYearChosen: number = this.checkoutFormGroup.value.expirationYear;
    const creditCard = this.checkoutFormGroup.get('creditCard');
    const expirationYear: number = Number(creditCard?.value.expirationYear);
    console.log(expirationYear + ` expiration year`);

    let currentYear: number = new Date().getFullYear();
    console.log(currentYear + ` current year`);
    let startMonth: number = new Date().getMonth();
    console.log(startMonth + ` start month`);
    if(expirationYear === currentYear || expirationYear === 0){
      this.shopFormService.getCreditCardMonths(startMonth+1).subscribe(data => this.creditCardMonths = data);
    }
    else{this.shopFormService.getCreditCardMonths(1).subscribe(data => this.creditCardMonths = data);}
  }

  getCreditCardYears(){
    this.shopFormService.getCreditCardYears().subscribe(data=>this.creditCardYears=data);
  }

  getStatesOnInit(){
    this.shopFormService.getStates().subscribe(data => this.states = data);
  }

  getStates(shippingAdress: string) {
    //get form group name from html
    const shippingGroup = this.checkoutFormGroup.get(shippingAdress);
    //use it to get a control name value
    let countryChosenCode1 = shippingGroup?.value.country.code;
    // const chosenCountryCode: string = this.countries.filter(country => country.name === countryChosen.name).map(country => country.code)[0];
    this.shopFormService.getStatesForGivenCountryCode(countryChosenCode1).subscribe(data => this.states = data);
  }
}
