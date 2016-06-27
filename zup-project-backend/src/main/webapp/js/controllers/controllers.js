function ProductsCtrl($scope, $http, Product) {

    $scope.refresh = function() {
        $scope.products = Product.query();
    };

 // clear input fields
    $scope.reset = function() {
        $scope.product = {};
    };

    $scope.save = function() {
        $scope.successMessages = '';
        $scope.errorMessages = '';
        $scope.errors = {};

        Product.save($scope.product, function(data) {
            $scope.successMessages = [ 'O produto foi adicionado com sucesso.' ];
            $scope.refresh();
            $scope.reset();
        }, function(result) {
            if (result.status == 400) {
                $scope.errors = result.data;
            } else {
                $scope.errorMessages = [ 'Ocorreu um erro interno.' ];
            }
            $scope.$apply();
        });

    };
    
    $scope.refresh();

    $scope.reset();

    $scope.orderBy = 'name';
}