
// Define the REST resource service, allowing us to interact with it as a high level service
angular.module('productsService', ['ngResource']).
    factory('Product', function($resource){    	
  return $resource('rest/products:productId', {});
});
