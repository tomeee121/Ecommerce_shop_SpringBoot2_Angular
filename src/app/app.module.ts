import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { ProductListComponent } from './components/product-list/product-list.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {ProductService} from "./services/product.service";
import {Router, RouterModule, Routes} from "@angular/router";
import { ProductCategoryMenuComponent } from './components/product-category-menu/product-category-menu.component';
import { SearchComponent } from './components/search/search.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import { CartStatusComponent } from './components/cart-status/cart-status.component';
import { CartDetailsComponent } from './components/cart-details/cart-details.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { CheckoutComponent } from './components/checkout/checkout.component';
import {AuthenticationService} from "./services/authentication.service";
import {AuthenticationGuard} from "./guard/authentication.guard";
import {CartService} from "./services/cart.service";
import {CheckoutServiceService} from "./services/checkout-service.service";
import {ShopFormService} from "./services/shop-form.service";
import {UserService} from "./services/user.service";
import {AuthInterceptor} from "./interceptor/auth.interceptor";
import {NotifierModule} from "angular-notifier";
import {NotificationService} from "./services/notification.service";
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { UserComponent } from './components/user/user.component';
import { Login2Component } from './components/login2/login2.component';

const routes: Routes = [

  {path: 'checkout', component: CheckoutComponent},
  {path: 'cart-details', component: CartDetailsComponent},
  {path: 'products/:id', component: ProductDetailsComponent},
  {path: 'search/:keyword', component: ProductListComponent},
  {path: 'category/:id/:name', component: ProductListComponent},
  {path: 'category', component: ProductListComponent},
  {path: 'products', component: ProductListComponent},
  {path: 'login', component: Login2Component},
  {path: 'register', component: RegisterComponent},
  {path: 'user/management', component: UserComponent},
  {path: '', redirectTo: '/products', pathMatch: 'full'},
  {path: '**', redirectTo: '/products', pathMatch: 'full'}
];
@NgModule({
  declarations: [
    AppComponent,
    ProductListComponent,
    ProductCategoryMenuComponent,
    SearchComponent,
    ProductDetailsComponent,
    CartStatusComponent,
    CartDetailsComponent,
    CheckoutComponent,
    LoginComponent,
    RegisterComponent,
    UserComponent,
    Login2Component,
  ],
    imports: [
        RouterModule.forRoot(routes),
        BrowserModule,
        HttpClientModule,
        NgbModule,
        ReactiveFormsModule,
        FormsModule,
        NotifierModule
    ],
  // @ts-ignore
  providers: [AuthenticationGuard, AuthenticationService,ProductService, CartService, CheckoutServiceService, ShopFormService, UserService,NotificationService,
              ,{provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule {}
