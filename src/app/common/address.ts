export class Address {
  country: string;
  state: string;
  city: string;
  street: string;
  zipCode: string;


  constructor(country: string, state: string, city: string, street: string, zipCode: string) {
    this.country = country;
    this.state = state;
    this.city = city;
    this.street = street;
    this.zipCode = zipCode;
  }
}
