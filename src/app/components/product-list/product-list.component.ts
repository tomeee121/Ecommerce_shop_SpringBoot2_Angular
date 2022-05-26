import { Component, OnInit } from '@angular/core';
import {ProductService} from "../../services/product.service";
import {Product} from "../../common/product";
import {ActivatedRoute} from "@angular/router";
import {CartService} from "../../services/cart.service";


@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

  constructor(private productService: ProductService,
              private route: ActivatedRoute,
              private cartService: CartService) { }

  products: Product[] = [];
  previousCategoryId: number = 1;
  currentCategoryId: number = 1;
  // @ts-ignore
  currentCategoryName: string;

  searchMode: boolean = false;

  // @ts-ignore
  previousSearchedKeyword: string | null;

  thePageNumber: number = 1;
  thePageSize: number = 5;
  theTotalElements: number = 0;


  ngOnInit(): void {
    this.route.paramMap.subscribe(() => {this.subscribeProducts();})
  }

  private subscribeProducts() {
    this.searchMode = this.route.snapshot.paramMap.has('keyword');
    if(this.searchMode){
      this.handleSearchProducts()}
    else {
      this.handleList()};
  }

  handleSearchProducts() {
    const keyword = this.route.snapshot.paramMap.get('keyword');
    if(this.previousSearchedKeyword!=keyword){
      this.thePageNumber = 1;
    }
    this.previousSearchedKeyword = keyword;

    this.productService.searchProductsPaginate(this.thePageNumber, this.thePageSize, keyword).subscribe(this.processResults());
  }

  handleList(){
    //check if 'id' parameter is avaialble
    const hasIdParam: boolean = this.route.snapshot.paramMap.has('id');
    if(hasIdParam){
      // @ts-ignore
      this.currentCategoryId = +this.route.snapshot.paramMap.get('id');
      // @ts-ignore
      this.currentCategoryName = this.route.snapshot.paramMap.get('name');
    }
    else{
      //default category = 1 and name 'Books'
      this.currentCategoryId = 1;
      this.currentCategoryName = 'Books';
    }

    //If the CategoryId is Different than previouse because ActiveRoute detected change then we switch to a new data of products
    //that is the reason for setting page nr to 1
    if(this.previousCategoryId != this.currentCategoryId){
      this.thePageNumber = 1;
    }
    //if we continue paginating the same category setting page number constantly as 1 would be a bug
    this.previousCategoryId = this.currentCategoryId;

    this.productService.getProductListPaginate(this.thePageNumber-1,
                                              this.thePageSize,
                                              this.currentCategoryId)
                                              .subscribe(this.processResults());
  }

  private processResults() {
    // @ts-ignore
    return data => {this.products = data._embedded.products;
                    this.thePageNumber = data.page.number+1;
                    this.thePageSize = data.page.size;
                    this.theTotalElements = data.page.totalElements;}
  };

  updatePageSize(size: any) {
    this.thePageSize = size.target.value;
    this.thePageNumber = 1;
    this.handleList();

  }

  addToCart(product: Product) {
    console.log(`adding to cart: ${product.name} ${product.unitPrice}`);
    this.cartService.addToCart(product);
  }
}
