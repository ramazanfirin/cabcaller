(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('StuffDetailController', StuffDetailController);

    StuffDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Stuff', 'Branch', 'Company', 'CabinGroup'];

    function StuffDetailController($scope, $rootScope, $stateParams, previousState, entity, Stuff, Branch, Company, CabinGroup) {
        var vm = this;

        vm.stuff = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cabcallerApp:stuffUpdate', function(event, result) {
            vm.stuff = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
