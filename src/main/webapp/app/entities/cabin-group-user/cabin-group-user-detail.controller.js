(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CabinGroupUserDetailController', CabinGroupUserDetailController);

    CabinGroupUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CabinGroupUser', 'CabinGroup', 'Stuff'];

    function CabinGroupUserDetailController($scope, $rootScope, $stateParams, previousState, entity, CabinGroupUser, CabinGroup, Stuff) {
        var vm = this;

        vm.cabinGroupUser = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cabcallerApp:cabinGroupUserUpdate', function(event, result) {
            vm.cabinGroupUser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
