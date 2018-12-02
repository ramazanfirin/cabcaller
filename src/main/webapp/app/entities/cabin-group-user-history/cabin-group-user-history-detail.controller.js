(function() {
    'use strict';

    angular
        .module('cabcallerApp')
        .controller('CabinGroupUserHistoryDetailController', CabinGroupUserHistoryDetailController);

    CabinGroupUserHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CabinGroupUserHistory', 'CabinGroup', 'Stuff'];

    function CabinGroupUserHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, CabinGroupUserHistory, CabinGroup, Stuff) {
        var vm = this;

        vm.cabinGroupUserHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cabcallerApp:cabinGroupUserHistoryUpdate', function(event, result) {
            vm.cabinGroupUserHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
