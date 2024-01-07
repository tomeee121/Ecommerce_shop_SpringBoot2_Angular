import { Component, OnInit } from '@angular/core';
import {CartItem} from "../../common/cart-item";
import {CartService} from "../../services/cart.service";
import {ProductService} from "../../services/product.service";
import {Product} from "../../common/product";

@Component({
  selector: 'app-cart-details',
  templateUrl: './cart-details.component.html',
  styleUrls: ['./cart-details.component.css']
})
export class CartDetailsComponent implements OnInit {

  cartItems: CartItem[] = [];
  // @ts-ignore
  foundItem: CartItem;
  totalQuantityDetail: number = 0;
  totalPriceDetail: number = 0;
  // @ts-ignore
  product: Product;

  constructor(private cartService: CartService, private productService: ProductService) { }

  ngOnInit(): void {
    this.listCartDetails();
  }

  private listCartDetails() {
    this.cartService.totalQuantity.subscribe(data => this.totalQuantityDetail = data);
    this.cartService.totalPrice.subscribe(data => this.totalPriceDetail = data);
    this.cartItems = this.cartService.cartItems;
    this.cartService.calculateCart();
  }


  incrementAmount(tempCartItem: CartItem) {
    // this.productService.getProduct(tempCartItem.id).subscribe(data => this.product = data);
    //
    // //call method that change reactive field quantity & total price of type Subject to recalculate it after increase and then back emit
    // this.cartService.addToCart(this.product);

    this.cartService.cartItems.filter(item2 => item2.id === tempCartItem.id).map(item => item.quantity++);
    this.cartService.calculateCart();
  }

  decreaseAmount(tempCartItem: CartItem){
    if(tempCartItem.quantity>0) {
      // this.productService.getProduct(tempCartItem.id).subscribe(data => this.product = data);
      //
      // //call method that change reactive field quantity & total price of type Subject to recalculate it after decrease and then emit back
      // this.cartService.decreaseAmountOfItemInCart(this.product);
      this.cartService.cartItems.filter(item2 => item2.id === tempCartItem.id).map(item => item.quantity--);
      this.cartService.calculateCart();
    }
  }

  removeItem(tempCartItem: CartItem) {
    this.cartService.remove(tempCartItem);
  }
}
