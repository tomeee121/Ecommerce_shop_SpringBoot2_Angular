// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  apiCustomerUrl: 'http://localhost:8080',
  shoppingHistoryUrlBase: 'http://localhost:8080/customer/shopping-history',
  allShoppingHistoryUrlBase:'http://localhost:8080/customer/all-shopping-history',
  deleteOrderUrlBase: 'http://localhost:8080/customer/deleteOrder',
  customerCustomUrl: 'http://localhost:8080/customer',
  baseStateUrl: 'http://localhost:8080/api/states',
  baseCountryUrl: 'http://localhost:8080/api/countries',
  baseUrlProducts: 'http://localhost:8080/api/products',
  categoryUrl: 'http://localhost:8080/api/product-category',
  purchaseUrl: 'http://localhost:8080/checkout/purchase',
  uploadUrl: 'http://localhost:8080/customer/profile-image'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
