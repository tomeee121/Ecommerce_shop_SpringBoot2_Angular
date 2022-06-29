import { Injectable } from '@angular/core';
import {CartItem} from "../common/cart-item";
import {BehaviorSubject, Subject} from "rxjs";
import {Product} from "../common/product";

@Injectable({
  providedIn: 'root'
})
export class CartService {

  cartItems: CartItem[] = [];
  totalPrice: Subject<number> = new BehaviorSubject<number>(0);
  totalQuantity: Subject<number> = new BehaviorSubject<number>(0);

  storage: Storage = sessionStorage;

  constructor() {
    //read data from storage
    // @ts-ignore
    let data = JSON.parse(this.storage.getItem('cartItems'));
    if(data != null){
      this.cartItems = data;

      this.calculateCart();
    }
  }


  addToCart(product: Product) {

    // let alreadyAddedToCart: boolean = false;
    // let productAlreadyAddedToCart: CartItem;
    let alreadyAddedToCart: boolean = false;
    let cartItem: CartItem = new CartItem(product);
    if(this.cartItems.length>0) {
      //check if we already have an item in the cart
      for (let tempCartItem of this.cartItems) {
        if(tempCartItem.id === cartItem.id){

          //if product selected was already in cart - increment its amount in user's cart
          tempCartItem.quantity++;
          alreadyAddedToCart = true;
          break;

        }
      }
    }
    //if cartItems array is empty selected product is automatically added to cart

    if(alreadyAddedToCart === false){
      cartItem.quantity = 1;
      this.cartItems.push(cartItem);
    }
    this.calculateCart();
  }

  decreaseAmountOfItemInCart(product: Product){

    let cartItem: CartItem = new CartItem(product);
      //search for the item user chose to decrease
      for (let tempCartItem of this.cartItems) {
        if(tempCartItem.id === cartItem.id){

          //decrease quantity
          tempCartItem.quantity--;
          if(tempCartItem.quantity === 0){
            this.remove(tempCartItem);
          }
          break;
        }
      }
      this.calculateCart();
    }

  calculateCart(){
    //after each CartItem added to cart
    let totalQuantityCalculated: number = 0;
    let totalAmountOfMoneyCalculated: number = 0;
    for(let tempCartItem of this.cartItems){
      totalQuantityCalculated+=tempCartItem.quantity;
      totalAmountOfMoneyCalculated+=tempCartItem.quantity*tempCartItem.unitPrice;
    }
    this.totalQuantity.next(totalQuantityCalculated);
    this.totalPrice.next(totalAmountOfMoneyCalculated);

    this.persistCartItems();
  }

  remove(cartItem: CartItem){
    let index: number = this.cartItems.findIndex(tempCartItem => tempCartItem.id === cartItem.id);
    this.cartItems.splice(index, 1);
    this.calculateCart();
  }

  persistCartItems(){
    this.storage.setItem('cartItems', JSON.stringify(this.cartItems));
  }


}
