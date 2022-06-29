export class User {
  private _id: number;
  private _customerId: string;
  private _firstName: string;
  private _lastName: string;
  private _email: string;
  private _username: string;
  private _imageUrl: string;
  private _role: string;
  private _authorities: string;
  private _isActive: boolean;
  private _isNotLocked: boolean;


  get id(): number {
    return this._id;
  }

  set id(value: number) {
    this._id = value;
  }

  get customerId(): string {
    return this._customerId;
  }

  set customerId(value: string) {
    this._customerId = value;
  }

  get firstName(): string {
    return this._firstName;
  }

  set firstName(value: string) {
    this._firstName = value;
  }

  get lastName(): string {
    return this._lastName;
  }

  set lastName(value: string) {
    this._lastName = value;
  }

  get email(): string {
    return this._email;
  }

  set email(value: string) {
    this._email = value;
  }

  get username(): string {
    return this._username;
  }

  set username(value: string) {
    this._username = value;
  }

  get imageUrl(): string {
    return this._imageUrl;
  }

  set imageUrl(value: string) {
    this._imageUrl = value;
  }

  get role(): string {
    return this._role;
  }

  set role(value: string) {
    this._role = value;
  }

  get authorities(): string {
    return this._authorities;
  }

  set authorities(value: string) {
    this._authorities = value;
  }

  get isActive(): boolean {
    return this._isActive;
  }

  set isActive(value: boolean) {
    this._isActive = value;
  }

  get isNotLocked(): boolean {
    return this._isNotLocked;
  }

  set isNotLocked(value: boolean) {
    this._isNotLocked = value;
  }

  constructor(id: number, customerId: string, firstName: string, lastName: string, email: string, username: string, imageUrl: string, role: string, authorities: string, isActive: boolean, isNotLocked: boolean) {
    this._id = id;
    this._customerId = customerId;
    this._firstName = firstName;
    this._lastName = lastName;
    this._email = email;
    this._username = username;
    this._imageUrl = imageUrl;
    this._role = role;
    this._authorities = authorities;
    this._isActive = isActive;
    this._isNotLocked = isNotLocked;
  }
}
