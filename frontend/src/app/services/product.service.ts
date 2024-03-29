import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {Product} from "../common/product";
import {ProductCategory} from "../common/product-category";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private baseUrlProducts: string = environment.baseUrlProducts;
  private categoryUrl: string = environment.categoryUrl;

  constructor(private httpClient: HttpClient) { }

  //this is useful for showing details about the product
  getProduct(theProductId: number): Observable<Product> {
    const productUrl = `${this.baseUrlProducts}/${theProductId}`;

    return this.httpClient.get<Product>(productUrl);
  }

  getProductListPaginate(thePage: number, pageSize: number, theCategoryId: number): Observable<GetResponseProducts>{
    const searchUrl = `${this.baseUrlProducts}/search/findByProductCategoryId?id=${theCategoryId}&page=${thePage}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }
  getProductList(theCategoryId: number): Observable<Product[]>{
    const searchUrl = `${this.baseUrlProducts}/search/findByProductCategoryId?id=${theCategoryId}`;

    return this.getProducts(searchUrl);
  }

  getProductCategories() : Observable<ProductCategory[]>{
    return this.httpClient.get<GetResponseProductCategory>(this.categoryUrl).pipe(map(response => response._embedded.productCategory));
  }


  searchProducts(keyword: string | null): Observable<Product[]> {
    const searchUrl = this.baseUrlProducts+`/search/findByNameContainingIgnoreCase?name=${keyword}`;

    return this.getProducts(searchUrl);
  }

  private getProducts(searchUrl: string): Observable<Product[]> {
    return this.httpClient.get<GetResponseProducts>(searchUrl).pipe(map(data => data._embedded.products));
  }

  searchProductsPaginate(thePage: number, pageSize: number, keyword: string | null): Observable<GetResponseProducts>{
    const searchUrl = `${this.baseUrlProducts}/search/findByNameContainingIgnoreCase?name=${keyword}&page=${thePage}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }
}
interface GetResponseProducts{
  _embedded:{
    products: Product[];
  }
  page:{
    size: number,
    totalElements: number,
    totalPages: number,
    number: number
  }
}
interface GetResponseProductCategory{
  _embedded:{
    productCategory: ProductCategory[];
  }
}
