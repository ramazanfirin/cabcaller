(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CabinDetailController', CabinDetailController);

    CabinDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cabin', 'CabinGroup'];

    function CabinDetailController($scope, $rootScope, $stateParams, previousState, entity, Cabin, CabinGroup) {
        var vm = this;

        vm.cabin = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cabcallerApp:cabinUpdate', function(event, result) {
            vm.cabin = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
