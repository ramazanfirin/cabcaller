(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CabinGroupDetailController', CabinGroupDetailController);

    CabinGroupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CabinGroup', 'Branch'];

    function CabinGroupDetailController($scope, $rootScope, $stateParams, previousState, entity, CabinGroup, Branch) {
        var vm = this;

        vm.cabinGroup = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cabcallerApp:cabinGroupUpdate', function(event, result) {
            vm.cabinGroup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
