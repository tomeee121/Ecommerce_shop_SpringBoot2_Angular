import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {CartService} from "../../services/cart.service";
import {ShopFormService} from "../../services/shop-form.service";
import {Country} from "../../common/country";
import {State} from "../../common/state";
import {FormValidators} from "../../validators/form-validators";
import {CheckoutServiceService} from "../../services/checkout-service.service";
import {Order} from "../../common/order";
import {OrderItem} from "../../common/order-item";
import {Purchase} from "../../common/purchase";
import {Address} from "../../common/address";
import {Router} from "@angular/router";
import {stringify} from "@angular/compiler/src/util";
import {isString} from "@ng-bootstrap/ng-bootstrap/util/util";

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

  constructor(private formBuilder: FormBuilder, private cartService: CartService, private shopFormService: ShopFormService, private checkoutService: CheckoutServiceService,
              private router: Router) {}

  ngOnInit(): void {
    this.checkoutFormGroup = this.formBuilder.group({
      customer: this.formBuilder.group({
        firstName: new FormControl('', [Validators.required, Validators.minLength(2), FormValidators.rejectOnlyWhiteSpace]),
        lastName: new FormControl('', [Validators.required, Validators.minLength(2), FormValidators.rejectOnlyWhiteSpace]),
        email: new FormControl('', [Validators.required, Validators.pattern('^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]{2,4}$'),FormValidators.rejectOnlyWhiteSpace])
      }),
      shippingAdress: this.formBuilder.group({
        country: new FormControl('', [Validators.required]),
        state: new FormControl('', [Validators.required]),
        city: new FormControl('', [Validators.required, Validators.minLength(2), FormValidators.rejectOnlyWhiteSpace]),
        street: new FormControl('', [Validators.required, Validators.minLength(2), FormValidators.rejectOnlyWhiteSpace]),
        zipCode: new FormControl('', [Validators.required, Validators.minLength(2), FormValidators.rejectOnlyWhiteSpace]),
      }),
      billingAdress: this.formBuilder.group({
        country: new FormControl('', [Validators.required]),
        state: new FormControl('', [Validators.required]),
        city: new FormControl('', [Validators.required, Validators.minLength(2), FormValidators.rejectOnlyWhiteSpace]),
        street: new FormControl('', [Validators.required, Validators.minLength(2), FormValidators.rejectOnlyWhiteSpace]),
        zipCode: new FormControl('', [Validators.required, Validators.minLength(2), FormValidators.rejectOnlyWhiteSpace])
      }),
      creditCard: this.formBuilder.group({
        cardType: new FormControl('', [Validators.required, FormValidators.rejectOnlyWhiteSpace]),
        nameOnCard: new FormControl('', [Validators.required, FormValidators.rejectOnlyWhiteSpace]),
        cardNumber: new FormControl('', [Validators.required, FormValidators.rejectOnlyWhiteSpace, Validators.pattern('[0-9]{16}')]),
        securityCode: new FormControl('', [Validators.required, FormValidators.rejectOnlyWhiteSpace, Validators.pattern('[0-9]{3}')]),
        expirationMonth: [''],
        expirationYear: ['']
      })
    })

    this.getCreditCardYears();
    this.checkExpirationYearChosen();
    this.shopFormService.getCountries().subscribe(
      data => {this.countries = data;
           this.checkoutFormGroup.get('shippingAdress')?.get('country')?.setValue(data[0])});
    this.getStatesOnInit();
    this.reviewCartTotal();
  }

  onSubmit(){

    //for validation
    if(this.checkoutFormGroup.invalid){
      this.checkoutFormGroup.markAllAsTouched();}

    //for backend to save purchase
    const order = new Order(this.totalQuantity, this.totalPrice);
    const cartItems = this.cartService.cartItems;
    let orderItems: OrderItem[] = [];
    for(let i = 0; i < cartItems.length; i++){
      orderItems[i] = new OrderItem(cartItems[i]);
    }

    const shippingAddress: Address = this.checkoutFormGroup?.controls['shippingAdress']?.value;
    // @ts-ignore
    shippingAddress.state = this.checkoutFormGroup?.controls['shippingAdress.state']?.value.name;
    // @ts-ignore
    shippingAddress.country = this.checkoutFormGroup?.controls['shippingAdress.country']?.value.name;

    const billingAddress: Address = this.checkoutFormGroup?.controls['billingAdress']?.value;
    // @ts-ignore
    billingAddress.state = this.checkoutFormGroup?.controls['billingAdress.state']?.value.name;
    // @ts-ignore
    billingAddress.country = this.checkoutFormGroup?.controls['billingAdress.country']?.value.name;

    const customer = this.checkoutFormGroup?.controls['customer']?.value;

    const purchase: Purchase = new Purchase(customer,shippingAddress,billingAddress,order,orderItems,'null');

    console.log(`purchase object done`);
    // @ts-ignore
    this.checkoutService.placeOrder(purchase).subscribe((data) => this.showAlert(data.orderTrackingNumber), (error) => this.showError());
    console.log(`order placed`);
    this.resetCard();
  }

  showAlert(response: any){
    alert(`Placed order successfully with number of `+JSON.parse(JSON.stringify(response)));
  }

  showError(){
    alert(`There was an error!`);
  }

  resetCard(){
    this.cartService.cartItems = [];
    this.cartService.totalPrice.next(0);
    this.cartService.totalQuantity.next(0);
    this.checkoutFormGroup.reset();
    this.router.navigateByUrl("/products");

  }

  get firstName() {return this.checkoutFormGroup?.get('customer.firstName');}
  get lastName() {return this.checkoutFormGroup.get('customer.lastName');}
  get email() {return this.checkoutFormGroup.get('customer.email');}

  get shippingAdressCountry(){return this.checkoutFormGroup.get('shippingAdress.country');}
  get shippingAdressState(){return this.checkoutFormGroup.get('shippingAdress.state');}
  get shippingAdressStreet(){return this.checkoutFormGroup.get('shippingAdress.street');}
  get shippingAdressCity(){return this.checkoutFormGroup.get('shippingAdress.city');}
  get shippingAdressZipCode(){return this.checkoutFormGroup.get('shippingAdress.zipCode');}

  get billingAdressCountry(){return this.checkoutFormGroup.get('billingAdress.country');}
  get billingAdressState(){return this.checkoutFormGroup.get('billingAdress.state');}
  get billingAdressStreet(){return this.checkoutFormGroup.get('billingAdress.street');}
  get billingAdressCity(){return this.checkoutFormGroup.get('billingAdress.city');}
  get billingAdressZipCode(){return this.checkoutFormGroup.get('billingAdress.zipCode');}

  get creditCardName(){return this.checkoutFormGroup.get('creditCard.nameOnCard');}
  get creditCardType(){return this.checkoutFormGroup.get('creditCard.cardType');}
  get creditCardNumber(){return this.checkoutFormGroup.get('creditCard.cardNumber');}
  get creditCardSecurityCode(){return this.checkoutFormGroup.get('creditCard.securityCode');}

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
    this.shopFormService.getStates().subscribe(
      data => {this.states = data;
           this.checkoutFormGroup.get('shippingAdress')?.get('state')?.setValue(data[0])});
  }

  getStates(shippingAdress: string) {
    //get form group name from html
    const shippingGroup = this.checkoutFormGroup.get(shippingAdress);
    //use it to get a control name value
    let countryChosenCode1 = shippingGroup?.value.country.code;
    // const chosenCountryCode: string = this.countries.filter(country => country.name === countryChosen.name).map(country => country.code)[0];
    this.shopFormService.getStatesForGivenCountryCode(countryChosenCode1).subscribe(
      data => {this.states = data;
          this.checkoutFormGroup.get('shippingAdress')?.get('state')?.setValue(data[0])});
}

  private reviewCartTotal() {
    this.cartService.totalQuantity.subscribe(data => this.totalQuantity = data);
    this.cartService.totalPrice.subscribe(data => this.totalPrice = data);
  }
}
