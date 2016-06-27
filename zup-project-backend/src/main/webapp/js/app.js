var app = angular.module('zupapp', ['productsService']).config(
        [ '$routeProvider', function($routeProvider) {
            $routeProvider.
            // if URL fragment is /home, then load the home partial, with the
            // ProjectCtrl controller
            when('/home', {
                templateUrl : 'partials/home.html',
                controller : ProductsCtrl
            }).otherwise({
                redirectTo : '/home'
            });
        } ]);